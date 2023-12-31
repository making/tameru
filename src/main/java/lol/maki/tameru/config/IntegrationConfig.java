package lol.maki.tameru.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;

@Configuration
public class IntegrationConfig {

	@Bean
	public IntegrationFlow integrationFlow() {
		Logger log = LoggerFactory.getLogger("messageHandler");
		return IntegrationFlow.from("logEventMessageChannel").split().handle(m -> log.info("Received {}", m)).get();
	}

}
