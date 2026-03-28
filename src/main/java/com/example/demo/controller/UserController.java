package com.example.demo.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Cart;
import com.example.demo.model.Category;
import com.example.demo.model.OrderRequest;
import com.example.demo.model.Payment;
import com.example.demo.model.ProductOrder;
import com.example.demo.model.UserDtls;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.service.CartService;
import com.example.demo.service.CategoryService;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;
import com.example.demo.util.CommonUtil;
import com.example.demo.util.OrderStatus;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@GetMapping("/")
	public String home() {
		return "user/home";
	}
	@Autowired
	private UserService userService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private PaymentRepository paymentRepository;
	
	@ModelAttribute
	public void getUserDetails(Principal p,Model m) {
		if(p!=null) {
			String email=p.getName();
			UserDtls userDtls=userService.getUserByEmail(email);
			m.addAttribute("user",userDtls);
			Integer countCart =	cartService.getCountCart(userDtls.getId());
			m.addAttribute("countCart", countCart);
		}
			List<Category> allActiveCategory=categoryService.getAllActiveCategory();
			m.addAttribute("categories",allActiveCategory);
			System.out.println("Categories: " + allActiveCategory);

		
		
	}
	
	@GetMapping("/addCart")
	public String addToCart(@RequestParam Integer pid, @RequestParam Integer uid, HttpSession session) {
		Cart saveCart = cartService.saveCart(pid, uid);

		if (ObjectUtils.isEmpty(saveCart)) {
			session.setAttribute("errorMsg", "Product add to cart failed");
		} else {
			session.setAttribute("succMsg", "Product added to cart");
		}
		return "redirect:/product/" + pid;
	}
	
	@GetMapping("/cart")
	public String loadCart(Principal p,Model m) {
		UserDtls user=	getLoggedInUserDetails(p);
	List<Cart> carts  =	cartService.getCartsByUser(user.getId());
  
		m.addAttribute("carts",carts);
		if(carts.size() >0) {
		Double totalOrderPrice =carts.get(carts.size()-1).getTotalOrderPrice();
		m.addAttribute("totalOrderPrice",totalOrderPrice);
		}
		return "/user/cart";
	}
	
	@GetMapping("/cartQuantityUpdate")
	public String UpdateCartQuantity(@RequestParam String sy,@RequestParam Integer cid,HttpSession session)
	{
		cartService.updateQuantity(sy, cid,session);
		return "redirect:/user/cart";
	}
	
	
	private UserDtls getLoggedInUserDetails(Principal p) {
		String email=p.getName();
		UserDtls userDtls= userService.getUserByEmail(email);
		return userDtls;
	}
	
//	@GetMapping("/orders")
//	public String orderPage(Principal p,Model m) {
//		UserDtls user=	getLoggedInUserDetails(p);
//		List<Cart> carts  =	cartService.getCartsByUser(user.getId());
//	  
//			m.addAttribute("carts",carts);
//			if(carts.size() >0) {
//			Double orderPrice =carts.get(carts.size()-1).getTotalOrderPrice();
//			Double totalOrderPrice =carts.get(carts.size()-1).getTotalOrderPrice()+250+100;
//			m.addAttribute("orderPrice",orderPrice);
//			m.addAttribute("totalOrderPrice",totalOrderPrice);
//			}
//		return "user/order";
//	}
	//my code start
		@GetMapping("/orders")
		public String orderPage(Principal p, Model m, HttpSession session) {
		    UserDtls user = getLoggedInUserDetails(p);
		    List<Cart> carts = cartService.getCartsByUser(user.getId());
		    m.addAttribute("carts", carts);

		    boolean stockError = false;
		    StringBuilder errorProducts = new StringBuilder();

		    for (Cart cart : carts) {
		        int requestedQty = cart.getQuantity();
		        int availableStock = cart.getProduct().getStock();

		        if (requestedQty > availableStock) {
		            stockError = true;
		            errorProducts.append(cart.getProduct().getTitle())
		                         .append(" (Only ")
		                         .append(availableStock)
		                         .append(" left), ");
		        }
		    }

		    if (stockError) {
		        session.setAttribute("errorMsg", "The following product(s) have insufficient stock: " +
		            errorProducts.toString() + "please update your cart before placing the order.");
		        return "redirect:/user/cart"; // Or redirect to another page if you prefer
		    }

		    if (!carts.isEmpty()) {
		        Double orderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
		        Double totalOrderPrice = orderPrice + 250 + 100;
		        m.addAttribute("orderPrice", orderPrice);
		        m.addAttribute("totalOrderPrice", totalOrderPrice);
		        session.setAttribute("totalOrderPrice", totalOrderPrice);
		    }

		    return "/user/order";
		}
	//my code is End
	
//		@PostMapping("/save-order")
//		public String saveOrder(@ModelAttribute OrderRequest request, Principal p, Model model) {
//		    try {
//		        UserDtls user = getLoggedInUserDetails(p);
//		        orderService.saveOrder(user.getId(), request);
//		        return "redirect:/user/success";
//		    } catch (Exception e) {
//		        model.addAttribute("errorMsg", e.getMessage());  // 👈 Send message to HTML page
//		        return "user/order"; // 👈 Return to order/checkout page with error
//		    }
//		}/
		
		//my code
		@PostMapping("/save-order")
		public String saveOrder(@ModelAttribute OrderRequest request,
		                        Principal p,
		                        Model model,
		                        HttpSession session) {
		    try {
		        UserDtls user = getLoggedInUserDetails(p);

		        if ("COD".equalsIgnoreCase(request.getPaymentType())) {
		            // ✅ Cash On Delivery → Save order immediately
		            orderService.saveOrder(user.getId(), request);
		            return "redirect:/user/success";

		        } else if ("ONLINE".equalsIgnoreCase(request.getPaymentType())) {
		            // ✅ Online Payment → Store pending order in session, redirect to payment page
		            session.setAttribute("pendingOrder", request);
		            session.setAttribute("userId", user.getId());
		            return "redirect:/user/payment"; // this shows your debit card page

		        } else {
		            model.addAttribute("errorMsg", "Invalid payment type selected!");
		            return "user/user/order";
		        }

		    } catch (Exception e) {
		        model.addAttribute("errorMsg", e.getMessage());
		        return "user/order";
		    }
		}
		
		    // ---------------- Payment Page ----------------
		    @GetMapping("/payment")
		    public String payment(HttpSession session, Model model, HttpServletResponse response) {
		        // Disable browser caching
		        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		        response.setHeader("Pragma", "no-cache");
		        response.setHeader("Expires", "0");

		        // Get pending order
		        OrderRequest pendingOrder = (OrderRequest) session.getAttribute("pendingOrder");
		        Double totalOrderPrice = (Double) session.getAttribute("totalOrderPrice");

		        // If no pending order, redirect to success page
		        if (pendingOrder == null || totalOrderPrice == null) {
		            return "redirect:/user/success";
		        }

		        model.addAttribute("totalOrderPrice", totalOrderPrice);
		        model.addAttribute("orderId", UUID.randomUUID().toString());

		        return "user/payment";
		    }

		    // ---------------- Process Payment ----------------
		    @PostMapping("/payment/process")
		    public String processPayment(@RequestParam String cardHolderName,
		                                 @RequestParam String cardNumber,
		                                 @RequestParam String expiryMonth,
		                                 @RequestParam String expiryYear,
		                                 @RequestParam String cvv,
		                                 @RequestParam Double amount,
		                                 HttpSession session) throws Exception {

		        // Get pending order
		        OrderRequest pendingOrder = (OrderRequest) session.getAttribute("pendingOrder");
		        Integer userId = (Integer) session.getAttribute("userId");

		        if (pendingOrder == null || userId == null) {
		            return "redirect:/"; // no pending order
		        }

		        // ---------------- Save Order ----------------
		     // Save order after payment
		        ProductOrder savedOrder = orderService.saveOrder(userId, pendingOrder);

		        // Save payment with same orderId
		        Payment payment = new Payment();
		        payment.setOrderId(savedOrder.getId()); // Use same order ID
		        payment.setTransactionId(UUID.randomUUID().toString());
		        payment.setAmount(amount);
		        payment.setPaymentType("DEBIT_CARD");
		        payment.setStatus("SUCCESS");
		        payment.setCardLast4(cardNumber.substring(cardNumber.length() - 4));
		        payment.setPaymentDate(LocalDateTime.now());
		        paymentRepository.save(payment);

		        // ---------------- Clear Session ----------------
		        session.removeAttribute("pendingOrder");
		        session.removeAttribute("userId");
		        session.removeAttribute("totalOrderPrice");

		        return "redirect:/user/success";
		    }


		    // ---------------- Payment Success Page ----------------
		    @GetMapping("/success")
		    public String loadSuccess(HttpSession session, HttpServletResponse response) {
		        // Disable browser caching
		        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		        response.setHeader("Pragma", "no-cache");
		        response.setHeader("Expires", "0");

		        // Clear session data just in case
		        session.removeAttribute("pendingOrder");
		        session.removeAttribute("userId");
		        session.removeAttribute("totalOrderPrice");

		        return "user/success"; // your success HTML page
		    }
		
	
	
	@GetMapping("/user-orders")
	public String myOrder(Model m,Principal p) {
		UserDtls loginUser= getLoggedInUserDetails(p);
	List<ProductOrder> orders =	orderService.getOrdersByUser(loginUser.getId());
	m.addAttribute("orders", orders);
		 return "/user/my_orders";
	}
	
	@GetMapping("/update-status")
	public String updateOrderStatus(@RequestParam Integer id,@RequestParam Integer st,HttpSession session) {
		
		OrderStatus[] values =OrderStatus.values();
		String status=null;
		
		for(OrderStatus orderSt :values) {
			if(orderSt.getId().equals(st)) {
				status = orderSt.getName();
			}
		}
		
		ProductOrder updateOrder=orderService.updateOrderStatus(id, status);
		try {
			commonUtil.sendMailForProduct(updateOrder, status);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		if(!ObjectUtils.isEmpty(updateOrder)) {
			session.setAttribute("succMsg", "Status Updated");
		}else {
			session.setAttribute("errorMsg", "Status not update");
		}
		
		return "redirect:/user/user-orders"; 
	}

	@GetMapping("/profile")
	public String profile() {
		return "user/profile";
	}
	
	@PostMapping("/update-profile")
	public String updateProfile(@ModelAttribute UserDtls user,@RequestParam MultipartFile img,HttpSession session) {
		
		UserDtls updateUserProfile= userService.updateUserProfile(user, img);
		
		if(ObjectUtils.isEmpty(updateUserProfile)){
			session.setAttribute("erroMsg", "Profile not updated");
		}else {
			session.setAttribute("succMsg", "Profile Updated");
		}
		return "redirect:/user/profile";
	}
	
	@PostMapping("/change-password")
	public String changePassword(@RequestParam String newPassword,@RequestParam String currentPassword,Principal p,HttpSession session) {
		
		UserDtls loggedUserDetails=getLoggedInUserDetails(p);
		
		boolean matches =passwordEncoder.matches(currentPassword, loggedUserDetails.getPassword());
		
		if(matches) {
				String encodePassword=	passwordEncoder.encode(newPassword);
				
				loggedUserDetails.setPassword(encodePassword);
				UserDtls  updateUser=userService.updateUser(loggedUserDetails);
				if(!ObjectUtils.isEmpty(updateUser)) {
					session.setAttribute("succMsg","password in changed successfully");
				}else {
					session.setAttribute("errorMsg","Password in not changing");
				}
		}else {
			session.setAttribute("errorMsg","Current Password is incorrect");
		}
		return "redirect:/user/profile";
	}
	
	
	
} 
	
