package lol.maki.tameru.event;

import java.util.List;
import java.util.Optional;

import lol.maki.tameru.event.filter.Filter;

public interface LogEventQuery {

	Optional<LogEvent> findByEventId(Long eventId);

	List<LogEvent> findLatestLogEvents(SearchRequest request);

	record SearchRequest(String query, int size, Filter.Expression filterExpression) {
	}

}
