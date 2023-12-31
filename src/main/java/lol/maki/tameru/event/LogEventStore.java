package lol.maki.tameru.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

import am.ik.timeflake.Timeflake;

import org.springframework.stereotype.Component;

@Component
public class LogEventStore {

	private final Map<Timeflake, LogEvent> logEvents;

	private final LogEventGateway logEventGateway;

	private final List<LogEventSubscriber> subscribers = new CopyOnWriteArrayList<>();

	public LogEventStore(LogEventGateway logEventGateway) {
		this.logEventGateway = logEventGateway;
		int capacity = 10;
		this.logEvents = Collections.synchronizedMap(new LinkedHashMap<>(capacity, 0.75f, true) {
			protected boolean removeEldestEntry(Map.Entry<Timeflake, LogEvent> eldest) {
				return size() > capacity;
			}
		});
	}

	public void store(LogEvent logEvent) {
		this.logEvents.put(logEvent.eventId(), logEvent);
		this.logEventGateway.sendEvent(logEvent);
		this.subscribers.forEach(subscriber -> subscriber.onEvent(logEvent));
	}

	public void store(List<LogEvent> logEvents) {
		this.logEvents.putAll(logEvents.stream()
			.collect(Collectors.toMap(LogEvent::eventId, Function.identity(), (o1, o2) -> o1, LinkedHashMap::new)));
		this.logEventGateway.sendEvents(logEvents);
		this.subscribers.forEach(subscriber -> logEvents.forEach(subscriber::onEvent));
	}

	public List<LogEvent> retrieveAll() {
		return new ArrayList<>(this.logEvents.values());
	}

	public LogEvent retrieve(Timeflake eventId) {
		return this.logEvents.get(eventId);
	}

	public void remove(Timeflake eventId) {
		this.logEvents.remove(eventId);
	}

	public void clear() {
		this.logEvents.clear();
	}

	public void subscribe(LogEventSubscriber subscriber) {
		this.subscribers.add(subscriber);
	}

	public void unsubscribe(LogEventSubscriber subscriber) {
		this.subscribers.remove(subscriber);
	}

}
