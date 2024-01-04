package am.ik.tameru.event;

import java.util.List;
import java.util.Optional;

import am.ik.tameru.event.filter.Filter;
import am.ik.yavi.builder.ValidatorBuilder;
import am.ik.yavi.core.Validator;

public interface LogEventQuery {

	Optional<LogEvent> findByEventId(Long eventId);

	List<LogEvent> findLatestLogEvents(SearchRequest request);

	record SearchRequest(String query, int size, Filter.Expression filterExpression) {

		public static final Validator<SearchRequest> validator = ValidatorBuilder.<SearchRequest>of() //
			.constraint(SearchRequest::query, "query", c -> c.greaterThanOrEqual(3)) //
			.constraint(SearchRequest::size, "size", c -> c.greaterThanOrEqual(1)) //
			.build();

	}

}
