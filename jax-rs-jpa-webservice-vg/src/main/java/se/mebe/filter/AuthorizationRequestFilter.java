package se.mebe.filter;

import java.io.IOException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;
import org.springframework.stereotype.Component;

@Component
@Provider
public final class AuthorizationRequestFilter implements ContainerRequestFilter {

	public static final String AUTH_TOKEN = "password";

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		String authHeader = requestContext.getHeaderString("Authorization");

		if (authHeader == null || !authHeader.equalsIgnoreCase(AUTH_TOKEN)) {
			throw new WebApplicationException(Status.UNAUTHORIZED);
		}

	}
}
