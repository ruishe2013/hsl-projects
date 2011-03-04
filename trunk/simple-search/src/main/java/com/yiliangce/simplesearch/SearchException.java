package com.yiliangce.simplesearch;

public class SearchException extends RuntimeException {

	private static final long serialVersionUID = -1373123583953070255L;

	public SearchException() {
		super();
	}

	public SearchException(String message, Throwable cause) {
		super(message, cause);
	}

	public SearchException(String message) {
		super(message);
	}

	public SearchException(Throwable cause) {
		super(cause);
	}

}
