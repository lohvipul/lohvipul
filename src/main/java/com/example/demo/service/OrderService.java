package com.example.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.demo.model.OrderRequest;
import com.example.demo.model.ProductOrder;

public interface OrderService {
	public ProductOrder saveOrder(Integer userId, OrderRequest orderRequest) throws Exception ;
	
	public List<ProductOrder> getOrdersByUser(Integer userId);
	
	public ProductOrder updateOrderStatus(Integer id,String status);
	
	public List<ProductOrder> getAllOrders();
	
	public ProductOrder getOrdersById(String orderId);
	
	public Page<ProductOrder> getAllOrdersPagination(Integer pageNo,Integer pageSize);

	int findMaxOrderId();

	long countOrdersWithIdGreaterThan(Integer lastSeenId);

	List<ProductOrder> getRecentOrdersForDashboard(int limit);
	
}
