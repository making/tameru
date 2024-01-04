package am.ik.tameru.event;

import java.util.List;
import java.util.Optional;

import am.ik.tameru.event.filter.Filter;
import am.ik.yavi.builder.ValidatorBuilder;
import am.ik.yavi.core.ApplicativeValidator;
import am.ik.yavi.core.Validated;
import am.ik.yavi.core.Validator;

public interface LogEventQuery {

	Optional<LogEvent> findByEventId(Long eventId);

	List<LogEvent> findLatestLogEvents(SearchRequest request);

	record SearchRequest(String query, int size, Filter.Expression filterExpression) {

		public static Validated<SearchRequest> validated(String query, int size, Filter.Expression filterExpression) {
			return validator.validate(new SearchRequest(query, size, filterExpression));
		}

		private static final ApplicativeValidator<SearchRequest> validator = ValidatorBuilder.<SearchRequest>of() //
			.constraint(SearchRequest::query, "query", c -> c.greaterThanOrEqual(3)) //
			.constraint(SearchRequest::size, "size", c -> c.greaterThanOrEqual(1)) //
			.build()
			.applicative();

	}

}
