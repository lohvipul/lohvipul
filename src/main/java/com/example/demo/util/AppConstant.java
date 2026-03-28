package com.example.demo.util;

public class AppConstant {

	/** Set on logout; consumed when /signin is opened once. */
	public static final String SESSION_FLASH_LOGOUT = "SESSION_FLASH_LOGOUT";

	/** Set on successful login; consumed on first home or admin dashboard load. */
	public static final String SESSION_FLASH_LOGIN_OK = "SESSION_FLASH_LOGIN_OK";

	public static final long UNLOCK_DURATION_TIME=1000;
			//1*60*60*1000;
	
	public static final long ATTEMPT_TIME=3;
}
