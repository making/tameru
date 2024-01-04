package lol.maki.tameru.event.jdbc;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import lol.maki.tameru.event.LogEvent;
import lol.maki.tameru.event.LogEventQuery;
import lol.maki.tameru.event.filter.converter.FilterExpressionConverter;
import lol.maki.tameru.event.filter.converter.Sqlite3FilterExpressionConverter;
import lol.maki.tameru.json.Json;
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

	// @Override
	// public List<LogEvent> findLatestLogEvents(int size) {
	// return this.jdbcClient.sql("""
	// SELECT event_id, message, timestamp, metadata FROM log_event ORDER BY timestamp
	// DESC, event_id DESC
	// """.trim() + //
	// " LIMIT %d".formatted(size)) //
	// .query(logEventRowMapper) //
	// .list();
	// }
	//
	// @Override
	// public List<LogEvent> findLatestLogEventsWithKeyword(String keyword, int size) {
	// return this.jdbcClient.sql("""
	// SELECT log_event.event_id, log_event.message, log_event.timestamp,
	// log_event.metadata
	// FROM log_event_fts
	// JOIN log_event ON log_event_fts.rowid = log_event.event_id
	// WHERE log_event_fts MATCH(?)
	// ORDER BY timestamp DESC, event_id DESC
	// """.trim() + //
	// " LIMIT %d".formatted(size)) //
	// .param("\"" + keyword + "\"") //
	// .query(logEventRowMapper) //
	// .list();
	// }

	@Override
	public List<LogEvent> findLatestLogEvents(SearchRequest request) {
		StringBuilder sql = new StringBuilder("""
				SELECT log_event.event_id, log_event.message, log_event.timestamp, log_event.metadata
				""");
		List<Object> params = new ArrayList<>();
		if (StringUtils.hasText(request.query())) {
			sql.append("""
					FROM log_event_fts
					JOIN log_event ON log_event_fts.rowid = log_event.event_id
					""");
			params.add("\"" + request.query() + "\"");
		}
		else {
			sql.append("""
					FROM log_event
					""");
		}
		sql.append("""
				WHERE 1 = 1
				""");
		if (StringUtils.hasText(request.query())) {
			sql.append("""
					AND log_event_fts MATCH(?)
					""");
		}
		if (request.filterExpression() != null) {
			sql.append("AND ")
				.append(converter.convertExpression(request.filterExpression()))
				.append(System.lineSeparator());
		}
		sql.append("""
				ORDER BY timestamp DESC, event_id DESC
				""");
		if (request.size() > 0) {
			sql.append("LIMIT %d".formatted(request.size()));
		}
		log.trace("Execute {}", sql);
		return this.jdbcClient.sql(sql.toString()) //
			.params(params) //
			.query(logEventRowMapper) //
			.list();
	}

}
