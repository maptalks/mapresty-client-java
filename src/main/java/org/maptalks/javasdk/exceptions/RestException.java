package org.maptalks.javasdk.exceptions;

public class RestException extends Exception {
	private final int errCode;
	private String errorMsg;

	public RestException(int errCode, final String error) {
		super(errCode + ":" + error);
		this.errCode = errCode;
		this.errorMsg = error;
	}

	public int getErrCode() {
		return errCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
