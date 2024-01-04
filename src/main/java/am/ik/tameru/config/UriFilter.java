package am.ik.tameru.config;

import java.util.function.Predicate;

public class UriFilter implements Predicate<String> {

	@Override
	public boolean test(String uri) {
		boolean deny = uri != null && (uri.equals("/readyz") || uri.equals("/livez") || uri.startsWith("/actuator")
				|| uri.startsWith("/_static"));
		return !deny;
	}

}