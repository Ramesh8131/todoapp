package com.app.todoapp.exception;

public class BadGatewayRequest extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BadGatewayRequest(String msg){
		super(msg);
	}
	
	public BadGatewayRequest(){
	}
}
