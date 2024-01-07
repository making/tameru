package am.ik.tameru.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(CorsProps.class)
public class WebConfig implements WebMvcConfigurer {
	private final CorsProps corsProps;

	public WebConfig(CorsProps corsProps) {
		this.corsProps = corsProps;
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins(this.corsProps.allowedOrigins())
				.allowedMethods("*")
				.allowedHeaders("*")
				.maxAge(3600);
	}
}
