package am.ik.tameru.query.web;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import am.ik.pagination.CursorPageRequest;
import am.ik.tameru.event.LogEvent;
import am.ik.tameru.event.LogEventQuery;
import am.ik.tameru.event.LogEventQuery.Cursor;
import am.ik.tameru.event.LogEventQuery.SearchRequest;
import am.ik.tameru.event.filter.Filter;
import am.ik.tameru.json.Json;
import am.ik.yavi.core.ConstraintViolationsException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
public class QueryController {

	private final LogEventQuery logEventQuery;

	private final ObjectMapper objectMapper;

	public QueryController(LogEventQuery logEventQuery, ObjectMapper objectMapper) {
		this.logEventQuery = logEventQuery;
		this.objectMapper = objectMapper;
	}

	@GetMapping(path = "")
	public Resource index() {
		return new ClassPathResource("META-INF/resources/index.html");
	}

	@GetMapping(path = "", params = "query")
	public List<LogEvent> events(@RequestParam String query, CursorPageRequest<Cursor> pageRequest,
			@RequestParam(required = false) String filter) {
		Filter.Expression filterExpression = StringUtils.hasText(filter) ? Filter.parser().parse(filter) : null;
		SearchRequest searchRequest = SearchRequest
			.validated(StringUtils.hasText(query) ? query : null, pageRequest, filterExpression)
			.orElseThrow(ConstraintViolationsException::new);
		return this.logEventQuery.findLatestLogEvents(searchRequest);
	}

	@GetMapping(path = "", params = "query", produces = "text/tsv")
	public ResponseEntity<StreamingResponseBody> eventsAsTsv(@RequestParam(required = false) String query,
			CursorPageRequest<Cursor> pageRequest, @RequestParam(required = false) String filter) {
		List<LogEvent> events = this.events(query, pageRequest, filter);
		StreamingResponseBody stream = outputStream -> {
			int i = 1;
			outputStream.write("eventId	timestamp	message	metadata".getBytes(StandardCharsets.UTF_8));
			outputStream.write(System.lineSeparator().getBytes(StandardCharsets.UTF_8));
			for (LogEvent event : events) {
				outputStream.write(
						"%d	%s	%s	%s"
							.formatted(event.eventId(), event.timestamp(), event.message(),
									Json.stringify(this.objectMapper, event.metadata()))
							.getBytes(StandardCharsets.UTF_8));
				outputStream.write(System.lineSeparator().getBytes(StandardCharsets.UTF_8));
				if (i++ % 30 == 0) {
					outputStream.flush();
				}
			}
			outputStream.flush();
		};
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "text/tsv").body(stream);
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
