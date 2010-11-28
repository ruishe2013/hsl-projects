package com.taobao.matrix.eagle.claw.resource;

import com.taobao.matrix.eagle.claw.ClawException;

public class ResourceNotFoundException extends ClawException {

	private static final long serialVersionUID = 5279521972395172679L;

	public ResourceNotFoundException() {
		super();
	}

	public ResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}

	public ResourceNotFoundException(Throwable cause) {
		super(cause);
	}

}
