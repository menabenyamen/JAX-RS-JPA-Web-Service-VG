package se.mebe.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public final class BadMessageExceptionMapper implements ExceptionMapper<BadMessageException> {

	@Override
	public Response toResponse(BadMessageException exception) {
		return Response.status(Status.BAD_REQUEST).entity(exception.getMessage()).build();
	}

}
