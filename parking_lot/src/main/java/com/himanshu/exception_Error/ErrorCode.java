/**
 * 
 */
package com.himanshu.exception_Error;

/**
 * @author Himanshu Tiwari
 *
 */
public enum ErrorCode {
	PARKING_ALREADY_EXIST("Sorry Parking creation has been done you can not recreate."),
	PARKING_NOT_EXIST_ERROR("Sorry, Car Parking Does not Exist"), INVALID_VALUE("{variable} value is incorrect"),
	INVALID_FILE("Invalid File"), PROCESSING_ERROR("Processing Error "), INVALID_REQUEST("Invalid Request");

	private String message = "";

	/**
	 * @param value
	 */
	private ErrorCode(String message) {
		this.message = message;
	}

	/**
	 * @return String
	 */
	public String getMessage() {
		return message;
	}
}
