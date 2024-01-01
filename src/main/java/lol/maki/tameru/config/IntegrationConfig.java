package lol.maki.tameru.config;

import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lol.maki.tameru.event.LogEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

@Configuration
public class IntegrationConfig {

	@Bean
	public IntegrationFlow integrationFlow(ObjectMapper objectMapper) {
		return IntegrationFlow.from("logEventMessageChannel")
			.split()
			.splitWith(spec -> spec.expectedType(LogEvent.class).<LogEvent>function(logEvent -> {
				List<Message<?>> list = new ArrayList<>(2);
				String eventId = logEvent.eventId().base62();
				String directory = logEvent.eventId().toUuid().toString().substring(0, 8);
				list.add(MessageBuilder.withPayload(eventId + "," + logEvent.message())
					.setHeader("file_name", "payload")
					.setHeader("directory", directory)
					.build());
				if (!logEvent.metadata().isEmpty()) {
					try {
						list.add(MessageBuilder
							.withPayload(eventId + "," + objectMapper.writeValueAsString(logEvent.metadata()))
							.setHeader("file_name", "metadata")
							.setHeader("directory", directory)
							.build());
					}
					catch (JsonProcessingException e) {
						throw new UncheckedIOException(e);
					}
				}
				return list;
			}))
			.log()
			.handle(Files.<LogEvent>outboundAdapter(m -> "logEvents/" + m.getHeaders().get("directory"))
				.fileExistsMode(FileExistsMode.APPEND)
				.appendNewLine(true))
			.get();
	}

}
