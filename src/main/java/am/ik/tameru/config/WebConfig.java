package am.ik.tameru.config;

import java.util.List;

import am.ik.pagination.web.CursorPageRequestHandlerMethodArgumentResolver;
import am.ik.tameru.auth.AuthProps;
import am.ik.tameru.auth.SimpleAuthInterceptor;
import am.ik.tameru.event.LogEventQuery.Cursor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({ CorsProps.class, AuthProps.class })
public class WebConfig implements WebMvcConfigurer {

	private final CorsProps corsProps;

	private final AuthProps authProps;

	public WebConfig(CorsProps corsProps, AuthProps authProps) {
		this.corsProps = corsProps;
		this.authProps = authProps;
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins(this.corsProps.allowedOrigins())
			.allowedMethods("*")
			.allowedHeaders("*")
			.maxAge(3600);
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new CursorPageRequestHandlerMethodArgumentResolver<>(Cursor::valueOf,
				props -> props.withSizeDefault(30)));
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new SimpleAuthInterceptor(this.authProps));
	}

}
