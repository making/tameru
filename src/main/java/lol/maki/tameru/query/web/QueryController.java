package lol.maki.tameru.query.web;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import am.ik.timeflake.Timeflake;
import lol.maki.tameru.event.LogEvent;
import lol.maki.tameru.event.LogEventStore;
import lol.maki.tameru.event.LogEventSubscriber;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class QueryController {

	private final LogEventStore logEventStore;

	public QueryController(LogEventStore logEventStore) {
		this.logEventStore = logEventStore;
	}

	@GetMapping(path = "")
	public List<LogEvent> events() {
		return this.logEventStore.retrieveAll();
	}

	@GetMapping(path = "/{eventId}")
	public Optional<LogEvent> event(@PathVariable Timeflake eventId) {
		return Optional.ofNullable(this.logEventStore.retrieve(eventId));
	}

	@GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter stream() {
		SseEmitter emitter = new SseEmitter(-1L);
		LogEventSubscriber subscriber = logEvent -> {
			try {
				emitter.send(logEvent, MediaType.APPLICATION_JSON);
			}
			catch (IOException e) {
				emitter.completeWithError(e);
			}
		};
		this.logEventStore.subscribe(subscriber);
		Runnable unsubscribe = () -> this.logEventStore.unsubscribe(subscriber);
		emitter.onCompletion(unsubscribe);
		emitter.onError(e -> unsubscribe.run());
		emitter.onTimeout(unsubscribe);
		return emitter;
	}

	@GetMapping(path = "/favicon.ico")
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public void favicon() {

	}

}
