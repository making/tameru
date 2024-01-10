package am.ik.tameru.event.web;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import am.ik.tameru.event.LogEventConverter;
import am.ik.tameru.event.LogEventStore;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// https://betterstack.com/docs/logs/http-rest-api/
@WebMvcTest(controllers = IngestController.class)
@Import({ LogEventConverter.class })
class IngestControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	LogEventStore logEventStore;

	static Instant timestamp = Instant.parse("2023-12-30T16:41:52+09:00");

	@BeforeEach
	void setUp() {
		this.logEventStore.clear();
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
			application/json|{"message":"logs is ready","nested":{"values":123}}
			""", delimiterString = "|")
	void testIngestSingleEvent(String contentType, String payload) throws Exception {
		mockMvc.perform(post("/").contentType(contentType).content(payload)).andExpect(status().isAccepted());
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
			application/json|{"message":"I have arrived on time","dt":"2023-08-09 07:03:30+00:00"}
			""", delimiterString = "|")
	void testIngestSingleEventWithTimestamp(String contentType, String payload) throws Exception {
		mockMvc.perform(post("/").contentType(contentType).content(payload)).andExpect(status().isAccepted());
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
			application/json|[{"message":"A"},{"message":"B"}]
			""", delimiterString = "|")
	void testIngestMultipleEvents(String contentType, String payload) throws Exception {
		mockMvc.perform(post("/").contentType(contentType).content(payload)).andExpect(status().isAccepted());
	}

	@TestConfiguration
	public static class Config {

		@Bean
		public Clock clock() {
			return Clock.fixed(timestamp, ZoneId.of("Asia/Tokyo"));
		}

		@Bean
		public MeterRegistry meterRegistry() {
			return new SimpleMeterRegistry();
		}

	}

}