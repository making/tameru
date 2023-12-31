package lol.maki.tameru.config;

import java.math.BigInteger;
import java.time.Clock;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

import am.ik.accesslogger.AccessLogger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

	@Bean
	public AccessLogger accessLogger() {
		return new AccessLogger(httpExchange -> {
			final String uri = httpExchange.getRequest().getUri().getPath();
			return uri != null && !(uri.equals("/readyz") || uri.equals("/livez") || uri.startsWith("/actuator"));
		});
	}

	@Bean
	public Clock clock() {
		return Clock.systemDefaultZone();
	}

	@Bean
	public Supplier<BigInteger> randomGenerator() {
		return () -> new BigInteger(80, ThreadLocalRandom.current());
	}

}