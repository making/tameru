package am.ik.tameru.event;

import java.time.Instant;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

public record LogEvent(@JsonInclude(JsonInclude.Include.NON_ABSENT) Long eventId,
		@JsonInclude(JsonInclude.Include.NON_ABSENT) String message, Instant timestamp,
		@JsonInclude(JsonInclude.Include.NON_EMPTY) Map<String, Object> metadata) {

}
