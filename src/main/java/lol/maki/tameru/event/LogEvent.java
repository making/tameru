package lol.maki.tameru.event;

import java.util.Map;

import am.ik.timeflake.Timeflake;
import com.fasterxml.jackson.annotation.JsonInclude;

public record LogEvent(Timeflake eventId, @JsonInclude(JsonInclude.Include.NON_ABSENT) String message,
		Map<String, Object> metadata) {

}
