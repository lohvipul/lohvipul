package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	public List<Product> findByIsActiveTrue();

	public List<Product> findByCategoryAndIsActiveTrue(String category);

	public List<Product> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch,String ch2);

	public Page<Product> findByIsActiveTrue(Pageable pageable);

	Page<Product> findByIsActiveFalse(Pageable pageable);

	public Page<Product> findByCategoryAndIsActiveTrue(Pageable pageable,String category);

	public Page<Product> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch, String ch2,
			Pageable pageable);

	public Page<Product> findByIsActiveTrueAndTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch1, String ch2, Pageable pageable);

	long countByIsActiveTrue();

	long countByIsActiveFalse();

	long countByIsActiveTrueAndStockLessThanEqual(int maxStock);

   /* @Query( "SELECT p from product p WHERE " +
            "LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%' )) OR "+
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%' )) OR "+
            "LOWER(p.price) LIKE LOWER(CONCAT('%', :keyword, '%' )) OR "+
            "LOWER(p.category) LIKE LOWER(CONCAT('%', :keyword, '%' ))")
    List<Product> searchProducts(String keyword);*/

}
