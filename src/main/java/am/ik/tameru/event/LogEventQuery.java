package am.ik.tameru.event;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import am.ik.pagination.CursorPageRequest;
import am.ik.tameru.event.filter.Filter;
import am.ik.yavi.builder.ValidatorBuilder;
import am.ik.yavi.core.ApplicativeValidator;
import am.ik.yavi.core.Validated;

public interface LogEventQuery {

	Optional<LogEvent> findByEventId(Long eventId);

	List<LogEvent> findLatestLogEvents(SearchRequest request);

	record SearchRequest(String query, CursorPageRequest<Cursor> pageRequest, Filter.Expression filterExpression) {

		public static Validated<SearchRequest> validated(String query, CursorPageRequest<Cursor> pageRequest,
				Filter.Expression filterExpression) {
			return validator.validate(new SearchRequest(query, pageRequest, filterExpression));
		}

		private static final ApplicativeValidator<SearchRequest> validator = ValidatorBuilder.<SearchRequest>of() //
			.constraint(SearchRequest::query, "query", c -> c.greaterThanOrEqual(3)) //
			._integer(r -> r.pageRequest().pageSize(), "size", c -> c.greaterThanOrEqual(1)) //
			.build()
			.applicative();

	}

	record Cursor(Instant timestamp, Long eventId) {

		public static Cursor valueOf(String s) {
			String[] vals = s.split(",", 2);
			return new Cursor(Instant.parse(vals[0]), Long.valueOf(vals[1]));
		}
	}

}
