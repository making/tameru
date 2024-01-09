package am.ik.tameru.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tameru.auth")
public record AuthProps(String token) {

}
