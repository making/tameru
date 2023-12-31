package lol.maki.tameru.ingest.web;

import java.math.BigInteger;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

import am.ik.timeflake.Timeflake;
import lol.maki.tameru.MockRandomGenerator;
import lol.maki.tameru.event.LogEvent;
import lol.maki.tameru.event.LogEventMapper;
import lol.maki.tameru.event.LogEventStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// https://betterstack.com/docs/logs/http-rest-api/
@WebMvcTest(controllers = IngestController.class)
@Import({ LogEventMapper.class, LogEventStore.class })
class IngestControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	LogEventStore logEventStore;

	static AtomicLong counter = new AtomicLong();

	static Instant timestamp = Instant.parse("2023-12-30T16:41:52+09:00");

	@BeforeEach
	void setUp() {
		this.logEventStore.clear();
		counter.set(0);
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
			application/json|{"message":"logs is ready","nested":{"values":123}}
			""", delimiterString = "|")
	void testIngestSingleEvent(String contentType, String payload) throws Exception {
		mockMvc.perform(post("/").contentType(contentType).content(payload)).andExpect(status().isAccepted());
		List<LogEvent> logEvents = this.logEventStore.retrieveAll();
		assertThat(logEvents).hasSize(1);
		LogEvent logEvent = new LogEvent(Timeflake.create(timestamp, BigInteger.valueOf(0)), "logs is ready",
				Map.of("nested", Map.of("values", 123)));
		assertThat(logEvents.get(0)).isEqualTo(logEvent);
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
			application/json|{"message":"I have arrived on time","dt":"2023-08-09 07:03:30+00:00"}
			""", delimiterString = "|")
	void testIngestSingleEventWithTimestamp(String contentType, String payload) throws Exception {
		mockMvc.perform(post("/").contentType(contentType).content(payload)).andExpect(status().isAccepted());
		List<LogEvent> logEvents = this.logEventStore.retrieveAll();
		assertThat(logEvents).hasSize(1);
		LogEvent logEvent = new LogEvent(
				Timeflake.create(Instant.parse("2023-08-09T07:03:30+00:00"), BigInteger.valueOf(0)),
				"I have arrived on time", LogEventMapper.EMPTY_MAP);
		assertThat(logEvents.get(0)).isEqualTo(logEvent);
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
			application/json|[{"message":"A"},{"message":"B"}]
			""", delimiterString = "|")
	void testIngestMultipleEvents(String contentType, String payload) throws Exception {
		mockMvc.perform(post("/").contentType(contentType).content(payload)).andExpect(status().isAccepted());
		List<LogEvent> logEvents = this.logEventStore.retrieveAll();
		assertThat(logEvents).hasSize(2);
		Timeflake timeflake1 = Timeflake.create(timestamp, BigInteger.valueOf(0));
		Timeflake timeflake2 = Timeflake.create(timestamp, BigInteger.valueOf(1));
		LogEvent logEvent1 = new LogEvent(timeflake1, "A", LogEventMapper.EMPTY_MAP);
		LogEvent logEvent2 = new LogEvent(timeflake2, "B", LogEventMapper.EMPTY_MAP);
		assertThat(logEvents.get(0)).isEqualTo(logEvent1);
		assertThat(logEvents.get(1)).isEqualTo(logEvent2);
		assertThat(this.logEventStore.retrieve(timeflake1)).isEqualTo(logEvent1);
		assertThat(this.logEventStore.retrieve(timeflake2)).isEqualTo(logEvent2);
	}

	@TestConfiguration
	public static class Config {

		@Bean
		public Clock clock() {
			return Clock.fixed(timestamp, ZoneId.of("Asia/Tokyo"));
		}

		@Bean
		public Supplier<BigInteger> randomGenerator() {
			return new MockRandomGenerator(counter);
		}

	}

}