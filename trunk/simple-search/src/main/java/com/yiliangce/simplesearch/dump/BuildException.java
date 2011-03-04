package com.yiliangce.simplesearch.dump;

public class BuildException extends RuntimeException {

	private static final long serialVersionUID = -2315098260766521224L;

	public BuildException() {
	}

	public BuildException(String message) {
		super(message);
	}

	public BuildException(Throwable cause) {
		super(cause);
	}

	public BuildException(String message, Throwable cause) {
		super(message, cause);
	}

}
