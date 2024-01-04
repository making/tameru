package lol.maki.tameru.query.web;

import java.util.List;
import java.util.Optional;

import lol.maki.tameru.event.LogEvent;
import lol.maki.tameru.event.LogEventQuery;
import lol.maki.tameru.event.filter.Filter;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class QueryController {

	private final LogEventQuery logEventQuery;

	public QueryController(LogEventQuery logEventQuery) {
		this.logEventQuery = logEventQuery;
	}

	@GetMapping(path = "")
	public List<LogEvent> events(@RequestParam(defaultValue = "30") int size,
			@RequestParam(required = false) String filter) {
		return this.logEventQuery.findLatestLogEvents(new LogEventQuery.SearchRequest(null, size,
				StringUtils.hasText(filter) ? Filter.parser().parse(filter) : null));
	}

	@GetMapping(path = "", params = "query")
	public List<LogEvent> eventsSearch(@RequestParam(defaultValue = "30") int size, @RequestParam String query,
			@RequestParam(required = false) String filter) {
		if (query.isEmpty()) {
			return events(size, filter);
		}
		if (query.length() <= 2) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "the size of query must be greater than 2.");
		}
		return this.logEventQuery.findLatestLogEvents(new LogEventQuery.SearchRequest(query, size,
				StringUtils.hasText(filter) ? Filter.parser().parse(filter) : null));
	}

	@GetMapping(path = "/{eventId}")
	public Optional<LogEvent> event(@PathVariable Long eventId) {
		return this.logEventQuery.findByEventId(eventId);
	}

	@GetMapping(path = "/favicon.ico")
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public void favicon() {

	}

}
