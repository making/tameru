package am.ik.tameru.config;

import java.time.Clock;

import am.ik.accesslogger.AccessLogger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class AppConfig {

	@Bean
	public AccessLogger accessLogger(UriFilter uriFilter) {
		return new AccessLogger(httpExchange -> uriFilter.test(httpExchange.getRequest().getUri().getPath()));
	}

	@Bean
	public UriFilter uriFilter() {
		return new UriFilter();
	}

	@Bean
	public Clock clock() {
		return Clock.systemDefaultZone();
	}

}