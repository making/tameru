package lol.maki.tameru.event;

import java.util.List;
import java.util.Optional;

public interface LogEventsQuery {

	Optional<LogEvent> findByEventId(Long eventId);

	List<LogEvent> findLatestLogEvents(int size);

	List<LogEvent> findLatestLogEventsWithKeyword(String keyword, int size);

}
