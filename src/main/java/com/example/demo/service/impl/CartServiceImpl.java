package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.example.demo.model.Cart;
import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.model.UserDtls;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CartService;

import jakarta.servlet.http.HttpSession;

@Service
public class CartServiceImpl implements CartService {

	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Override
	public Cart saveCart(Integer productId, Integer userId) {

		UserDtls userDtls = userRepository.findById(userId).get();
		Product product = productRepository.findById(productId).get();

		Cart cartStatus = cartRepository.findByProductIdAndUserId(productId, userId);

		Cart cart = null;

		if (ObjectUtils.isEmpty(cartStatus)) {
			cart = new Cart();
			cart.setProduct(product);
			cart.setUser(userDtls);
			cart.setQuantity(1);
			cart.setTotalPrice(1 * product.getDiscountPrice());
		} else {
			cart = cartStatus;
			cart.setQuantity(cart.getQuantity() + 1);
			cart.setTotalPrice(cart.getQuantity() * cart.getProduct().getDiscountPrice());
		}
		Cart saveCart = cartRepository.save(cart);

		return saveCart;
	}

	@Override
	public List<Cart> getCartsByUser(Integer userId) {
		 List<Cart> carts= cartRepository.findByUserId(userId);
		 
		 Double totalOrderPrice=0.0;
		 List<Cart> updatedCart = new ArrayList<>();
		 for(Cart c : carts) {
		Double	totalPrice = (double) Math.round(c.getProduct().getDiscountPrice()*c.getQuantity());
			c.setTotalPrice(totalPrice);
			totalOrderPrice += (double)Math.round( totalPrice);
			c.setTotalOrderPrice(totalOrderPrice); 
			updatedCart.add(c);
		 }
		
		 
		return updatedCart;
	}

	@Override
	public Integer getCountCart(Integer userId) {
	Integer countByUserId =	cartRepository.countByUserId(userId);
		return countByUserId;
	}

//	@Override
//	public void updateQuantity(String sy, Integer cid) {
//		
//		Cart cart=cartRepository.findById(cid).get();
//		int updateQuantity;
//		if(sy.equalsIgnoreCase("de")) {
//			updateQuantity = cart.getQuantity()-1;
//			
//			if(updateQuantity <= 0) {
//				cartRepository.delete(cart);
//				}else {
//					cart.setQuantity(updateQuantity);
//					cartRepository.save(cart);
//				}
//		}else {
//			updateQuantity=cart.getQuantity()+1;
//			cart.setQuantity(updateQuantity);
//			cartRepository.save(cart);
//		}
//		
//		
//	
//	}

	//my own logic code
	@Override
	public void updateQuantity(String sy, Integer cid, HttpSession session) {

	    Cart cart = cartRepository.findById(cid).get();
	    int currentQuantity = cart.getQuantity();
	    int updatedQuantity;

	    if (sy.equalsIgnoreCase("de")) {
	        updatedQuantity = currentQuantity - 1;

	        if (updatedQuantity <= 0) {
	            cartRepository.delete(cart);
	            session.setAttribute("succMsg", "Item removed from cart");
	        } else {
	            cart.setQuantity(updatedQuantity);
	            cartRepository.save(cart);
	        }

	    } else {
	        if (currentQuantity >= 10) {
	            session.setAttribute("errorMsg", "Maximum 10 quantity allowed");
	            return;
	        }

	        updatedQuantity = currentQuantity + 1;
	        cart.setQuantity(updatedQuantity);
	        cartRepository.save(cart);
	    }
	}

	
	
}
