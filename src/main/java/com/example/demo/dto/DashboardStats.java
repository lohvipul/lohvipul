package com.example.demo.dto;

/**
 * Summary counts for the admin home dashboard.
 */
public record DashboardStats(
		long activeProducts,
		long inactiveProducts,
		long lowStockProducts,
		long categories,
		long totalOrders,
		/** Orders not yet delivered / cancelled / completed — needs admin attention */
		long pendingOrders,
		long customerUsers,
		long adminUsers) {
}
