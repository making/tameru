package lol.maki.tameru.query.web;

import java.util.List;
import java.util.Optional;

import am.ik.timeflake.Timeflake;
import lol.maki.tameru.event.LogEvent;
import lol.maki.tameru.event.LogEventStore;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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

	@GetMapping(path = "/favicon.ico")
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public void favicon() {

	}

}
