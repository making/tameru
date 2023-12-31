package lol.maki.tameru.event;

import java.util.List;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.scheduling.annotation.Async;

@MessagingGateway
public interface LogEventGateway {

	@Gateway(requestChannel = "logEventMessageChannel")
	@Async
	void sendEvent(LogEvent logEvent);

	@Gateway(requestChannel = "logEventMessageChannel")
	@Async
	void sendEvents(List<LogEvent> logEvents);

}
