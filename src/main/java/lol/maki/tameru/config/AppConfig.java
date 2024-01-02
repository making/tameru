package lol.maki.tameru.config;

import java.time.Clock;

import am.ik.accesslogger.AccessLogger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
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

}