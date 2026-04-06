
package com.example.demo.service.impl;

//import java.util.Date;
import java.time.LocalDate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Cart;
import com.example.demo.model.OrderAddress;
import com.example.demo.model.OrderRequest;
import com.example.demo.model.Product;
import com.example.demo.model.ProductOrder;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductOrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.OrderService;
import com.example.demo.util.CommonUtil;
import com.example.demo.util.OrderStatus;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductOrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CommonUtil commonUtil;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductOrder saveOrder(Integer userId, OrderRequest orderRequest) throws Exception {

        List<Cart> carts = cartRepository.findByUserId(userId);
        ProductOrder lastSavedOrder = null; // Keep reference to the last saved order

        for (Cart cart : carts) {
            Product product = cart.getProduct();
            int orderedQty = cart.getQuantity();

            int updatedStock = product.getStock() - orderedQty;
            product.setStock(updatedStock);
            productRepository.save(product);

            ProductOrder order = new ProductOrder();
            order.setOrderId(UUID.randomUUID().toString());
            order.setOrderDate(LocalDate.now());
            order.setProduct(product);
            order.setPrice(product.getDiscountPrice());
            order.setQuantity(orderedQty);
            order.setUser(cart.getUser());
            order.setStatus(OrderStatus.IN_PROGRESS.getName());
            order.setPaymentType(orderRequest.getPaymentType());

            OrderAddress address = new OrderAddress();
            address.setFirstName(orderRequest.getFirstName());
            address.setLastName(orderRequest.getLastName());
            address.setEmail(orderRequest.getEmail());
            address.setMobileNo(orderRequest.getMobileNo());
            address.setAddress(orderRequest.getAddress());
            address.setCity(orderRequest.getCity());
            address.setState(orderRequest.getState());
            address.setPincode(orderRequest.getPincode());
            order.setOrderAddress(address);

            try {
                lastSavedOrder = orderRepository.save(order); // Save and keep reference
                commonUtil.sendMailForProduct(lastSavedOrder, "success");
            } catch (Exception e) {
                throw new RuntimeException("Order Failed: Email not sent. Transaction rolled back. Check internet.");
            }
        }

        cartRepository.deleteByUserId(userId);

        return lastSavedOrder; // Return last saved order
    }

	@Override
	public List<ProductOrder> getOrdersByUser(Integer userId) {
		
	List<ProductOrder> orders =	orderRepository.findByUserId(userId);
		return orders;
	}

	@Override
	public ProductOrder updateOrderStatus(Integer id, String status) {
		Optional<ProductOrder> findById=orderRepository.findById(id);
		
		if(findById.isPresent()) {
		 ProductOrder productOrder =findById.get();
		 productOrder.setStatus(status);
	ProductOrder updateOrder =	 orderRepository.save(productOrder);
		 return updateOrder;
		}
		return null;
	}

	@Override
	public List<ProductOrder> getAllOrders() {
		
		return orderRepository.findAll();
	}

	
	
	@Override
	public Page<ProductOrder> getAllOrdersPagination(Integer pageNo, Integer pageSize) {
		Pageable pageable=PageRequest.of(pageNo, pageSize);
		return orderRepository.findAll(pageable);
		
	}



	@Override
	public ProductOrder getOrdersById(String orderId) {
		
		ProductOrder findByOrderId= orderRepository.findByOrderId(orderId);
		
		return findByOrderId;
	}

	@Override
	public int findMaxOrderId() {
		return orderRepository.findMaxOrderId();
	}

	@Override
	public long countOrdersWithIdGreaterThan(Integer lastSeenId) {
		if (lastSeenId == null) {
			return orderRepository.count();
		}
		return orderRepository.countByIdGreaterThan(lastSeenId);
	}

	@Override
	public List<ProductOrder> getRecentOrdersForDashboard(int limit) {
		Pageable p = PageRequest.of(0, Math.max(1, limit), Sort.by(Sort.Direction.DESC, "id"));
		return orderRepository.findAll(p).getContent();
	}
    
   
}




/*package com.example.demo.service.impl;
//
//import java.util.Date;
//import java.util.List;
//import java.util.UUID;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.example.demo.model.Cart;
//import com.example.demo.model.OrderAddress;
//import com.example.demo.model.OrderRequest;
//import com.example.demo.model.ProductOrder;
//import com.example.demo.repository.CartRepository;
//import com.example.demo.repository.ProductOrderRepository;
//import com.example.demo.repository.ProductRepository;
//import com.example.demo.service.OrderService;
//import com.example.demo.util.OrderStatus;
//
//@Service
//public class OrderServiceImpl  implements OrderService{
//
//	@Autowired
//	private ProductOrderRepository orderRepository;
//	
//	@Autowired
//	private CartRepository cartRepository;
//	@Override
//	public void saveOrder(Integer userId,OrderRequest orderRequest) {
//		
//	List<Cart>	carts = cartRepository.findByUserId(userId);
//	for(Cart cart :carts) {
//		
//		ProductOrder order=new ProductOrder();
//		
//		order.setOrderId(UUID.randomUUID().toString());
//		order.setOrderDate(new Date());
//		
//		order.setProduct(cart.getProduct());
//		order.setPrice(cart.getProduct().getDiscountPrice());
//		
//		order.setQuantity(cart.getQuantity());
//		order.setUser(cart.getUser());
//		
//		order.setStatus(OrderStatus.IN_PROGRESS.getName());
//		order.setPaymentType(orderRequest.getPaymentType());
//		
//		OrderAddress address=new OrderAddress();
//		address.setFirstName(orderRequest.getFirstName());
//		address.setLastName(orderRequest.getLastName());
//		address.setEmail(orderRequest.getEmail());
//		address.setMobileNo(orderRequest.getMobileNo());
//		address.setCity(orderRequest.getCity());
//		address.setState(orderRequest.getState());
//		address.setPincode(orderRequest.getPincode());
//		
//		order.setOrderAddress(address);
//		
//		orderRepository.save(order);
//		
//	}
//		
//		
//		
//	}
//	
//}*/
