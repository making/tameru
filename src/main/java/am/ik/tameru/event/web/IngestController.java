package am.ik.tameru.event.web;

import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import am.ik.tameru.event.LogEvent;
import am.ik.tameru.event.LogEventConverter;
import am.ik.tameru.event.LogEventStore;
import com.fasterxml.jackson.databind.JsonNode;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IngestController {

	private final LogEventConverter logEventConverter;

	private final LogEventStore logEventStore;

	private final Logger log = LoggerFactory.getLogger(IngestController.class);

	private final Counter eventsCounter;

	public IngestController(LogEventConverter logEventConverter, LogEventStore logEventStore,
			MeterRegistry meterRegistry) {
		this.logEventConverter = logEventConverter;
		this.logEventStore = logEventStore;
		this.eventsCounter = Counter.builder("tameru_events").register(meterRegistry);
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
		this.logEventStore.saveEvent(logEvent);
		this.eventsCounter.increment();
	}

	void ingestJsonArray(Stream<JsonNode> stream) {
		List<LogEvent> logEvents = stream.map(this.logEventConverter::convert).toList();
		this.logEventStore.saveEvents(logEvents);
		this.eventsCounter.increment(logEvents.size());
		if (log.isTraceEnabled()) {
			log.trace("Ingest {} event(s)", logEvents.size());
		}
	}

}
