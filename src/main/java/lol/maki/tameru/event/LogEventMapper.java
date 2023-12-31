package lol.maki.tameru.event;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Clock;
import java.time.Instant;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

import am.ik.timeflake.Timeflake;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

@Component
public class LogEventMapper {

	public static final String MESSAGE_KEY = "message";

	public static final String DT_KEY = "dt";

	public static final Map<String, Object> EMPTY_MAP = Collections.emptyMap();

	private final Clock clock;

	private final Supplier<BigInteger> randomGenerator;

	private final ObjectMapper objectMapper;

	private final Logger logger = LoggerFactory.getLogger(LogEventMapper.class);

	public LogEventMapper(Clock clock, Supplier<BigInteger> randomGenerator, ObjectMapper objectMapper) {
		this.clock = clock;
		this.randomGenerator = randomGenerator;
		this.objectMapper = objectMapper;
	}

	public LogEvent map(JsonNode node) {
		String message = node.has(MESSAGE_KEY) ? node.get(MESSAGE_KEY).asText() : null;
		Instant timestamp = node.has(DT_KEY) ? parseDt(node.get(DT_KEY)) : this.clock.instant();
		Map<String, Object> metadata = null;
		Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
		while (fields.hasNext()) {
			Map.Entry<String, JsonNode> field = fields.next();
			if (!field.getKey().equals(MESSAGE_KEY) && !field.getKey().equals(DT_KEY)) {
				if (metadata == null) {
					metadata = new LinkedHashMap<>();
				}
				metadata.put(field.getKey(), fieldValue(field.getValue()));
			}
		}
		return new LogEvent(Timeflake.create(timestamp, this.randomGenerator.get()), message,
				Objects.requireNonNullElse(metadata, EMPTY_MAP));
	}

	private static Instant parseDt(JsonNode node) {
		if (node.isNumber()) {
			return Instant.ofEpochSecond(node.asLong());
		}
		String dt = node.asText();
		if (dt.contains(" ")) {
			dt = dt.trim().replace(" UTC", "Z").replace(" ", "T");
		}
		return Instant.parse(dt);
	}

	private Object fieldValue(JsonNode node) {
		if (node.isTextual()) {
			return node.asText();
		}
		else if (node.isInt()) {
			return node.asInt();
		}
		else if (node.isLong()) {
			return node.asLong();
		}
		else if (node.isDouble()) {
			return node.asDouble();
		}
		else if (node.isBoolean()) {
			return node.asBoolean();
		}
		else if (node.isBigInteger()) {
			return new BigInteger(node.asText());
		}
		else if (node.isBigDecimal()) {
			return new BigDecimal(node.asText());
		}
		else if (node.isObject()) {
			return this.objectMapper.convertValue(node, LinkedHashMap.class);
		}
		else if (node.isArray()) {
			return StreamSupport.stream(node.spliterator(), false).map(this::fieldValue).toList();
		}
		else {
			logger.warn("unexpected type {}:{}", node.getNodeType(), node);
			return node.toString();
		}
	}

}
