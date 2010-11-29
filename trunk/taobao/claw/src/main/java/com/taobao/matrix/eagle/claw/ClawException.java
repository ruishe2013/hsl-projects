package com.taobao.matrix.eagle.claw;

public class ClawException extends RuntimeException {

	private static final long serialVersionUID = -1845448665423683331L;

	public ClawException() {
	}

	public ClawException(String message) {
		super(message);
	}

	public ClawException(Throwable cause) {
		super(cause);
	}

	public ClawException(String message, Throwable cause) {
		super(message, cause);
	}

}
