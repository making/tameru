package am.ik.tameru.event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;

@Component
public class LogEventStore {

	private final LogEventStorage logEventStorage;

	private final List<LogEventSubscriber> subscribers = new CopyOnWriteArrayList<>();

	public LogEventStore(LogEventStorage logEventStorage) {
		this.logEventStorage = logEventStorage;
	}

	public void store(LogEvent logEvent) {
		this.logEventStorage.saveEvent(logEvent);
		this.subscribers.forEach(subscriber -> subscriber.onEvent(logEvent));
	}

	public void store(List<LogEvent> logEvents) {
		this.logEventStorage.saveEvents(logEvents);
		this.subscribers.forEach(subscriber -> logEvents.forEach(subscriber::onEvent));
	}

	public void remove(Long eventId) {
		this.logEventStorage.remove(eventId);
	}

	public void clear() {
		this.logEventStorage.clear();
	}

	public void subscribe(LogEventSubscriber subscriber) {
		this.subscribers.add(subscriber);
	}

	public void unsubscribe(LogEventSubscriber subscriber) {
		this.subscribers.remove(subscriber);
	}

}
