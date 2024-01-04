package am.ik.tameru.query.web;

import java.util.List;
import java.util.Optional;

import am.ik.tameru.event.LogEvent;
import am.ik.tameru.event.LogEventQuery;
import am.ik.tameru.event.LogEventQuery.SearchRequest;
import am.ik.tameru.event.filter.Filter;
import am.ik.yavi.core.ConstraintViolationsException;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueryController {

	private final LogEventQuery logEventQuery;

	public QueryController(LogEventQuery logEventQuery) {
		this.logEventQuery = logEventQuery;
	}

	@GetMapping(path = "")
	public List<LogEvent> events(@RequestParam(required = false) String query,
			@RequestParam(defaultValue = "30") int size, @RequestParam(required = false) String filter) {
		SearchRequest searchRequest = new SearchRequest(StringUtils.hasText(query) ? query : null, size,
				StringUtils.hasText(filter) ? Filter.parser().parse(filter) : null);
		SearchRequest.validator.validate(searchRequest).throwIfInvalid(ConstraintViolationsException::new);
		return this.logEventQuery.findLatestLogEvents(searchRequest);
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
