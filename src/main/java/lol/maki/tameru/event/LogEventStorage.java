package lol.maki.tameru.event;

import java.util.List;

public interface LogEventStorage {

	void saveEvent(LogEvent logEvent);

	void saveEvents(List<LogEvent> logEvents);

}
