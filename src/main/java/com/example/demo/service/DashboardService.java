package com.example.demo.service;

import java.util.Map;

import com.example.demo.dto.DashboardStats;

public interface DashboardService {

	DashboardStats getAdminDashboardStats();

	Map<String, Long> getOrderStatusBreakdown();
}
