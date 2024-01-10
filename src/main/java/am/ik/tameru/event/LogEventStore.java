package am.ik.tameru.event;

import java.util.List;

public interface LogEventStore {

	void saveEvent(LogEvent logEvent);

	void saveEvents(List<LogEvent> logEvents);

	void remove(Long eventId);

	void clear();

}
