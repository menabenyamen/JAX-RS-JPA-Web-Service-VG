package se.mebe.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public final class ResourceException extends WebApplicationException {

	private static final long serialVersionUID = 1L;

	public ResourceException(final String message) {
		super(Response.status(Status.NOT_FOUND).entity(message).type(MediaType.TEXT_PLAIN).build());
	}
}
