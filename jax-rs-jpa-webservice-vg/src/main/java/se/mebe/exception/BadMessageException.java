package se.mebe.exception;

public final class BadMessageException extends Exception {

	private static final long serialVersionUID = 399364321702895417L;

	public BadMessageException(final String message){
		super(message);
	}
}
