package com.newpecunia.unicredit;


public class UnicreditConnectorException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public UnicreditConnectorException() {
		super();
	}
	
	public UnicreditConnectorException(String msg) {
		super(msg);
	}
	
	public UnicreditConnectorException(Exception cause) {
		super(cause);
	}
	
	public UnicreditConnectorException(String msg, Exception cause) {
		super(msg, cause);
	}
}
