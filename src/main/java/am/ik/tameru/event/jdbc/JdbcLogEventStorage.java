package am.ik.tameru.event.jdbc;

import java.sql.Timestamp;
import java.util.List;

import am.ik.tameru.json.Json;
import com.fasterxml.jackson.databind.ObjectMapper;
import am.ik.tameru.event.LogEvent;
import am.ik.tameru.event.LogEventStorage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class JdbcLogEventStorage implements LogEventStorage {

	private final JdbcTemplate jdbcTemplate;

	private final ObjectMapper objectMapper;

	private static final String insertSql = """
			INSERT INTO log_event (message, timestamp, metadata) VALUES(?, ?, ?)
			""".trim();

	public JdbcLogEventStorage(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.objectMapper = objectMapper;
	}

	@Override
	public void saveEvent(LogEvent logEvent) {
		this.jdbcTemplate.update(insertSql, logEvent.message(), Timestamp.from(logEvent.timestamp()),
				Json.stringify(this.objectMapper, logEvent.metadata()));
	}

	@Override
	public void saveEvents(List<LogEvent> logEvents) {
		this.jdbcTemplate.batchUpdate(insertSql,
				logEvents.stream()
					.map(logEvent -> new Object[] { logEvent.message(), Timestamp.from(logEvent.timestamp()),
							Json.stringify(this.objectMapper, logEvent.metadata()) })
					.toList());
	}

	@Override
	public void remove(Long eventId) {
		this.jdbcTemplate.update("DELETE FROM log_event WHERE event_id = ?", eventId);
	}

	@Override
	public void clear() {
		this.jdbcTemplate.update("DELETE FROM log_event");
	}

}
