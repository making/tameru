package am.ik.tameru.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "tameru.cors")
public record CorsProps(@DefaultValue("*") String allowedOrigins) {
}