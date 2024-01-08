package am.ik.tameru.event.jdbc;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import am.ik.tameru.event.LogEvent;
import am.ik.tameru.event.LogEventQuery;
import am.ik.tameru.event.filter.converter.FilterExpressionConverter;
import am.ik.tameru.event.filter.converter.Sqlite3FilterExpressionConverter;
import am.ik.tameru.json.Json;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JdbcLogEventQuery implements LogEventQuery {

	private final JdbcClient jdbcClient;

	private final RowMapper<LogEvent> logEventRowMapper;

	private final FilterExpressionConverter converter = new Sqlite3FilterExpressionConverter();

	private final Logger log = LoggerFactory.getLogger(JdbcLogEventQuery.class);

	public JdbcLogEventQuery(JdbcClient jdbcClient, ObjectMapper objectMapper) {
		this.jdbcClient = jdbcClient;
		this.logEventRowMapper = (rs, rowNum) -> {
			Long eventId = rs.getLong("event_id");
			String message = rs.getString("message");
			Instant timestamp = rs.getTimestamp("timestamp").toInstant();
			Map<String, Object> metadata = Json.parse(objectMapper, rs.getString("metadata"));
			return new LogEvent(eventId, message, timestamp, metadata);
		};
	}

	@Override
	public Optional<LogEvent> findByEventId(Long eventId) {
		return this.jdbcClient.sql("""
				SELECT event_id, message, timestamp, metadata FROM log_event WHERE event_id = ?
				""".trim()) //
			.param(eventId) //
			.query(logEventRowMapper) //
			.optional();
	}

	@Override
	public List<LogEvent> findLatestLogEvents(SearchRequest request) {
		StringBuilder sql = new StringBuilder("""
				SELECT log_event.event_id, log_event.message, log_event.timestamp, log_event.metadata
				""");
		Map<String, Object> params = new HashMap<>();
		if (StringUtils.hasText(request.query())) {
			sql.append("""
					FROM log_event_fts
					JOIN log_event ON log_event_fts.rowid = log_event.event_id
					""");
		}
		else {
			sql.append("""
					FROM log_event
					""");
		}
		sql.append("""
				WHERE timestamp < COALESCE(:cursor, 1e10000) -- Infinity
				""");
		params.put("cursor", request.pageRequest().cursorOptional().map(Timestamp::from).orElse(null));
		if (StringUtils.hasText(request.query())) {
			sql.append("""
					AND log_event_fts MATCH(:query)
					""");
			params.put("query", "\"" + request.query() + "\"");
		}
		if (request.filterExpression() != null) {
			sql.append("AND ")
				.append(converter.convertExpression(request.filterExpression()))
				.append(System.lineSeparator());
		}
		sql.append("""
				ORDER BY timestamp DESC, event_id DESC
				""");
		if (request.pageRequest().pageSize() > 0) {
			sql.append("LIMIT %d".formatted(request.pageRequest().pageSize()));
		}
		log.trace("Execute {}", sql);
		return this.jdbcClient.sql(sql.toString()) //
			.params(params) //
			.query(logEventRowMapper) //
			.list();
	}

}
