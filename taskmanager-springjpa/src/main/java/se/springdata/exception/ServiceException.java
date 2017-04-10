package se.springdata.exception;

public class ServiceException extends Exception {

	private static final long serialVersionUID = -5174595178651942788L;

	public ServiceException(String message) {
		super(message);
	}
}