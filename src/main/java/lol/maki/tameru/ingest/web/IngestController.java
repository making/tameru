package lol.maki.tameru.ingest.web;

import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import am.ik.timeflake.Timeflake;
import com.fasterxml.jackson.databind.JsonNode;
import lol.maki.tameru.event.LogEvent;
import lol.maki.tameru.event.LogEventMapper;
import lol.maki.tameru.event.LogEventStore;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IngestController {

	private final LogEventMapper logEventMapper;

	private final LogEventStore logEventStore;

	public IngestController(LogEventMapper logEventMapper, LogEventStore logEventStore) {
		this.logEventMapper = logEventMapper;
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
	public void delete(@PathVariable Timeflake eventId) {
		this.logEventStore.remove(eventId);
	}

	void ingestJsonObject(JsonNode body) {
		LogEvent logEvent = this.logEventMapper.map(body);
		this.logEventStore.store(logEvent);
	}

	void ingestJsonArray(Stream<JsonNode> stream) {
		List<LogEvent> logEvents = stream.map(this.logEventMapper::map).toList();
		this.logEventStore.store(logEvents);
	}

}
