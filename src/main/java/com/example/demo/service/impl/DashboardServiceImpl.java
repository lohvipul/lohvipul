package com.example.demo.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.dto.DashboardStats;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductOrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.DashboardService;
import com.example.demo.util.OrderStatus;

@Service
public class DashboardServiceImpl implements DashboardService {

	private static final int LOW_STOCK_THRESHOLD = 5;

	private static final List<String> ORDER_TERMINAL_STATUSES = List.of(
			OrderStatus.DELIVERED.getName(),
			OrderStatus.CANCEL.getName(),
			OrderStatus.SUCCESS.getName());

	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final ProductOrderRepository productOrderRepository;
	private final UserRepository userRepository;

	public DashboardServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository,
			ProductOrderRepository productOrderRepository, UserRepository userRepository) {
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
		this.productOrderRepository = productOrderRepository;
		this.userRepository = userRepository;
	}

	@Override
	public DashboardStats getAdminDashboardStats() {
		return new DashboardStats(
				productRepository.countByIsActiveTrue(),
				productRepository.countByIsActiveFalse(),
				productRepository.countByIsActiveTrueAndStockLessThanEqual(LOW_STOCK_THRESHOLD),
				categoryRepository.count(),
				productOrderRepository.count(),
				productOrderRepository.countPendingOrdersExcludingStatuses(ORDER_TERMINAL_STATUSES),
				userRepository.countByRole("ROLE_USER"),
				userRepository.countByRole("ROLE_ADMIN"));
	}

	@Override
	public Map<String, Long> getOrderStatusBreakdown() {
		Map<String, Long> map = new LinkedHashMap<>();
		for (Object[] row : productOrderRepository.countOrdersGroupedByStatus()) {
			String key = row[0] != null ? String.valueOf(row[0]) : "(no status)";
			long cnt = row[1] != null ? ((Number) row[1]).longValue() : 0L;
			map.put(key, cnt);
		}
		return map;
	}
}
