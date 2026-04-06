package com.example.demo.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.ProductOrder;

public interface ProductOrderRepository extends JpaRepository<ProductOrder,Integer>{
           
	List<ProductOrder> findByUserId(Integer userId);
	
	ProductOrder findByOrderId(String orderid);

	@Query("SELECT COUNT(o) FROM ProductOrder o WHERE o.status IS NULL OR o.status NOT IN :terminal")
	long countPendingOrdersExcludingStatuses(@Param("terminal") Collection<String> terminal);

	long countByIdGreaterThan(Integer id);

	@Query("SELECT COALESCE(MAX(o.id), 0) FROM ProductOrder o")
	int findMaxOrderId();

	@Query("SELECT o.status, COUNT(o) FROM ProductOrder o GROUP BY o.status ORDER BY COUNT(o) DESC")
	List<Object[]> countOrdersGroupedByStatus();
}
