package com.example.demo.util;

public class AppConstant {

	/** Set on logout; consumed when /signin is opened once. */
	public static final String SESSION_FLASH_LOGOUT = "SESSION_FLASH_LOGOUT";

	/** Set on successful login; consumed on first home or admin dashboard load. */
	public static final String SESSION_FLASH_LOGIN_OK = "SESSION_FLASH_LOGIN_OK";

	/** Last seen max product_order.id — opening /admin/orders updates this (new-order badge). */
	public static final String DASH_LAST_MAX_ORDER_ID = "DASH_LAST_MAX_ORDER_ID";

	/** Customer count snapshot when admin opened /admin/users?type=1 */
	public static final String DASH_SNAP_CUSTOMER_COUNT = "DASH_SNAP_CUSTOMER_COUNT";

	/** Admin user count snapshot when admin opened /admin/users?type=2 */
	public static final String DASH_SNAP_ADMIN_COUNT = "DASH_SNAP_ADMIN_COUNT";

	/** Pending-order count when admin last opened /admin/orders */
	public static final String DASH_PENDING_SNAPSHOT = "DASH_PENDING_SNAPSHOT";

	public static final long UNLOCK_DURATION_TIME=1000;
			//1*60*60*1000;
	
	public static final long ATTEMPT_TIME=3;
}
