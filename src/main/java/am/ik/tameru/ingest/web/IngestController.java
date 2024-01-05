package am.ik.tameru.ingest.web;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import am.ik.tameru.event.LogEvent;
import am.ik.tameru.event.LogEventConverter;
import am.ik.tameru.event.LogEventStore;
import com.fasterxml.jackson.databind.JsonNode;
import am.ik.tameru.event.LogEventSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class IngestController {

	private final LogEventConverter logEventConverter;

	private final LogEventStore logEventStore;

	private final Logger log = LoggerFactory.getLogger(IngestController.class);

	public IngestController(LogEventConverter logEventConverter, LogEventStore logEventStore) {
		this.logEventConverter = logEventConverter;
		this.logEventStore = logEventStore;
	}

	@PostMapping(path = "")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void ingest(@RequestBody JsonNode body) {
		if (body.isArray()) {
			this.ingestJsonArray(StreamSupport.stream(body.spliterator(), false));
			return;
		}
		this.ingestJsonObject(body);
	}

	@DeleteMapping(path = "/{eventId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long eventId) {
		this.logEventStore.remove(eventId);
	}

	void ingestJsonObject(JsonNode body) {
		LogEvent logEvent = this.logEventConverter.convert(body);
		this.logEventStore.store(logEvent);
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

	void ingestJsonArray(Stream<JsonNode> stream) {
		List<LogEvent> logEvents = stream.map(this.logEventConverter::convert).toList();
		this.logEventStore.store(logEvents);
		if (log.isTraceEnabled()) {
			log.trace("Ingest {} event(s)", logEvents.size());
		}
	}

}
