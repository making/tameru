package lol.maki.tameru.event.jdbc;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import lol.maki.tameru.event.LogEvent;
import lol.maki.tameru.event.LogEventQuery;
import lol.maki.tameru.json.Json;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

@Component
public class JdbcLogEventQuery implements LogEventQuery {

	private final JdbcClient jdbcClient;

	private final RowMapper<LogEvent> logEventRowMapper;

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
	public List<LogEvent> findLatestLogEvents(int size) {
		return this.jdbcClient.sql("""
				SELECT event_id, message, timestamp, metadata FROM log_event ORDER BY timestamp DESC, event_id DESC
				""".trim() + //
				" LIMIT %d".formatted(size)) //
			.query(logEventRowMapper) //
			.list();
	}

	@Override
	public List<LogEvent> findLatestLogEventsWithKeyword(String keyword, int size) {
		return this.jdbcClient.sql("""
				SELECT log_event.event_id, log_event.message, log_event.timestamp, log_event.metadata
				FROM log_event_fts
				JOIN log_event ON log_event_fts.rowid = log_event.event_id
				WHERE log_event_fts MATCH(?)
				ORDER BY timestamp DESC, event_id DESC
				""".trim() + //
				" LIMIT %d".formatted(size)) //
			.param("\"" + keyword + "\"") //
			.query(logEventRowMapper) //
			.list();
	}

}
