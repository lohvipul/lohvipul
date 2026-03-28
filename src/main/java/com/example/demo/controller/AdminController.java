package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.ObjectUtils;

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.model.ProductOrder;
import com.example.demo.model.UserDtls;
import com.example.demo.service.CartService;
import com.example.demo.service.CategoryService;
import com.example.demo.service.OrderService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import com.example.demo.util.CommonUtil;
import com.example.demo.util.OrderStatus;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CartService  cartService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@ModelAttribute
	public void getUserDetails(Principal p,Model m) {
		if(p!=null) {
			String email=p.getName();
			UserDtls userDtls=userService.getUserByEmail(email);
			m.addAttribute("user",userDtls);
			Integer countCart =	cartService.getCountCart(userDtls.getId());
			m.addAttribute("countCart", countCart);
		}
		
			List<Category> allActiveCategory = categoryService.getAllActiveCategory();
			m.addAttribute("categories", allActiveCategory);
			System.out.println("Categories: " + allActiveCategory);

		
	}
	
	@GetMapping("/")
	public String index() {
		return "admin/index";
	}
	
	@GetMapping("/loadAddProduct")
	public String loadAddProduct(Model m) {
		List<Category> categories = categoryService.getAllCategory();
		m.addAttribute("categories",categories);
		
		return "admin/add_product";
	}
	
	@GetMapping("/category")
	public String category(Model m,@RequestParam(name="pageNo",defaultValue = "0")Integer pageNo
			,@RequestParam(name="pageSize",defaultValue = "4") Integer pageSize){
		
		//m.addAttribute("categorys",categoryService.getAllCategory());
		
		Page<Category> page =categoryService.getAllCategoryPagination(pageNo, pageSize);
		List<Category> categorys = page.getContent();
		m.addAttribute("categorys",categorys);
		
		m.addAttribute("pageNo",page.getNumber());
		m.addAttribute("pageSize",pageSize);
		m.addAttribute("totalElements",page.getTotalElements());
		m.addAttribute("totalPages",page.getTotalPages());
		m.addAttribute("isFirst",page.isFirst());
		m.addAttribute("isLast",page.isLast());
		
		
		return "admin/category";
	}
	
//	@PostMapping("/saveCategory")
//	public String saveCategory(@ModelAttribute Category category,
//			@RequestParam("file") MultipartFile file , HttpSession session) throws IOException {
//		
//		String imageName= file != null ? file.getOriginalFilename() : "default.jpg";
//		
//		category.setImageName(imageName);
//		Boolean existCategory = categoryService.existCategory(category.getName());
//		if(existCategory) {
//			session.setAttribute("errorMsg", "Category Name is already exists ");
//		}else {
//			Category saveCategory = categoryService.saveCategory(category);
//			
//			if(org.springframework.util.ObjectUtils.isEmpty(saveCategory)) {
//				session.setAttribute("errorMsg", "Not saved ! internal server Error");
//			}else {
//				
//				File saveFile=new ClassPathResource("static/img").getFile();
//				
//				
//				Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+"category_img"+File.separator+file.getOriginalFilename());
//				System.out.println(path);
//				
//				Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
//				session.setAttribute("succMsg", "Saved Succesfully");
//			}
//		}
//		
//		return "redirect:/admin/category";
//		
//		
//	}
	@PostMapping("/saveCategory")
	public String saveCategory(@ModelAttribute Category category,
	                           @RequestParam("file") MultipartFile file,
	                           HttpSession session) throws IOException {

	    String imageName = (file != null && !file.isEmpty()) ? file.getOriginalFilename() : "default.jpg";
	    category.setImageName(imageName);

	    Boolean existCategory = categoryService.existCategory(category.getName());
	    if (existCategory) {
	        session.setAttribute("errorMsg", "Category Name already exists");
	    } else {
	        Category saveCategory = categoryService.saveCategory(category);

	        if (saveCategory == null) {
	            session.setAttribute("errorMsg", "Not saved! Internal server error");
	        } else {
	            // Save to external uploads folder
	            File saveDir = new File("uploads/category_img");
	            if (!saveDir.exists()) {
	                saveDir.mkdirs();
	            }

	            Path path = Paths.get(saveDir.getAbsolutePath(), imageName);
	            System.out.println("Saving category image to: " + path);

	            if (!file.isEmpty()) {
	                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	            }

	            session.setAttribute("succMsg", "Saved Successfully");
	        }
	    }
	    return "redirect:/admin/category";
	}

	@GetMapping("/deleteCategory/{id}")
	public 	String deleteCategory(@PathVariable int id,HttpSession session) {
		
		Boolean deleteCatogary= categoryService.deleteCategory(id);
		if(deleteCatogary) {
			session.setAttribute("succMsg", "category deleted successfully");
		}else {
			session.setAttribute("errorMsg", "something worng on server");

		}
		return "redirect:/admin/category";
	}
	
	@GetMapping("/loadEditCategory/{id}")
	public String loadEditCategory(@PathVariable int id,Model m) {
		m.addAttribute("category",categoryService.getCategoryById(id));
		
		return "admin/edit_category";
	}
	
//	@PostMapping("/updateCategory")
//	public String updateCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file , HttpSession session) throws IOException {
//		
//		Category oldCategory= categoryService.getCategoryById(category.getId());
//		String imageName = file.isEmpty() ?  oldCategory.getImageName():file.getOriginalFilename() ;
//		
//		if(!org.springframework.util.ObjectUtils.isEmpty(category)) {
//			oldCategory.setName(category.getName());
//			oldCategory.setIsActive(category.getIsActive());
//			oldCategory.setImageName(imageName);
//			
//		
//					
//		}
//		 Category updateCategory=categoryService.saveCategory(oldCategory);
//		
//		if(!org.springframework.util.ObjectUtils.isEmpty(updateCategory)) {
//			
//			if(!file.isEmpty()) {
//				File saveFile=new ClassPathResource("uploads/category_img").getFile();
//				
//				
//				Path path=Paths.get(saveFile.getAbsolutePath() + File.separator + "category_img" + File.separator +
//						file.getOriginalFilename());
//				System.out.println(path);
//				
//				Files.copy(file.getInputStream(), path ,StandardCopyOption.REPLACE_EXISTING);
//				
//			}
//			
//			session.setAttribute("succMsg", "Category update success");
//		}else {
//			session.setAttribute("errorMsg", "something wrong on server ");
//		}
//		
//		return "redirect:/admin/loadEditCategory/"+category.getId();
//	}
//	
	@PostMapping("/updateCategory")
	public String updateCategory(@ModelAttribute Category category,
	                             @RequestParam("file") MultipartFile file,
	                             HttpSession session) throws IOException {

	    Category oldCategory = categoryService.getCategoryById(category.getId());

	    // Keep old image if no new file uploaded
	    String imageName = file.isEmpty() ? oldCategory.getImageName() : file.getOriginalFilename();

	    if (!org.springframework.util.ObjectUtils.isEmpty(category)) {
	        oldCategory.setName(category.getName());
	        oldCategory.setIsActive(category.getIsActive());
	        oldCategory.setImageName(imageName);
	    }

	    Category updatedCategory = categoryService.saveCategory(oldCategory);

	    if (!org.springframework.util.ObjectUtils.isEmpty(updatedCategory)) {

	        // Save new file if uploaded
	        if (!file.isEmpty()) {
	            File saveDir = new File("uploads/category_img");
	            if (!saveDir.exists()) {
	                saveDir.mkdirs();
	            }

	            Path path = Paths.get(saveDir.getAbsolutePath(), imageName);
	            System.out.println("Updating category image: " + path);

	            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	        }

	        session.setAttribute("succMsg", "Category updated successfully");
	    } else {
	        session.setAttribute("errorMsg", "Something went wrong on server");
	    }

	    return "redirect:/admin/loadEditCategory/" + category.getId();
	}

	
//	@PostMapping("/saveProduct")
//	public String saveProduct(@ModelAttribute Product product,
//			@RequestParam("file") MultipartFile image,HttpSession session) throws IOException {
//		
//		String imageName=image.isEmpty() ? "default.jpg" :image.getOriginalFilename();
//		
//		product.setImage(imageName);
//		product.setDiscount(0);
//		product.setDiscountPrice(product.getPrice());
//		Product saveProduct = productService.saveProduct(product);
//		
//		
//		 if(!org.springframework.util.ObjectUtils.isEmpty(saveProduct)) {
//			 session.setAttribute("succMsg", "product saved successfully");
//			 
//			 File saveFile=new ClassPathResource("static/img").getFile();
//				
//				Path path=Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img" + File.separator +
//						image.getOriginalFilename());
//				System.out.println(path);
//				
//				Files.copy(image.getInputStream(), path ,StandardCopyOption.REPLACE_EXISTING);
//				
//		 }else {
//			 session.setAttribute("errorMsg", "somthing worng in server");
//		 }
//		
//		return "redirect:/admin/loadAddProduct";
//	}
//	
	
	@PostMapping("/saveProduct")
	public String saveProduct(@ModelAttribute Product product,
	                          @RequestParam("file") MultipartFile image,
	                          HttpSession session) throws IOException {

	    String imageName = image.isEmpty() ? "default.jpg" : image.getOriginalFilename();

	    product.setImage(imageName);
	    product.setDiscount(0);
	    product.setDiscountPrice(product.getPrice());

	    Product savedProduct = productService.saveProduct(product);

	    if (!org.springframework.util.ObjectUtils.isEmpty(savedProduct)) {
	        session.setAttribute("succMsg", "Product saved successfully");

	        // Save in external uploads/product_img folder
	        File saveDir = new File("uploads/product_img");
	        if (!saveDir.exists()) {
	            saveDir.mkdirs();
	        }

	        Path path = Paths.get(saveDir.getAbsolutePath(), imageName);
	        System.out.println("Saving product image: " + path);

	        if (!image.isEmpty()) {
	            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	        }

	    } else {
	        session.setAttribute("errorMsg", "Something went wrong on server");
	    }

	    return "redirect:/admin/loadAddProduct";
	}

	@GetMapping("/products")
	public String loadViewProduct(Model m,@RequestParam(defaultValue = "") String ch,@RequestParam(name="pageNo",defaultValue = "0")Integer pageNo
			,@RequestParam(name="pageSize",defaultValue = "4") Integer pageSize) {
//		List<Product> products =null;
//		if(ch!=null & ch.length()>0) {
//			products=productService.searchProduct(ch);
//		}else {
//			products=productService.getAllProducts();
//		}
//		m.addAttribute("products",products);
		
		Page<Product> page =null;
		if(ch!=null & ch.length()>0) {
			page=productService.searchProductPagination(pageNo, pageSize, ch);
		}else {
			page=productService.getAllProductsPagination(pageNo,pageSize);
		}
		m.addAttribute("products",page.getContent());
		
		m.addAttribute("pageNo",page.getNumber());
		m.addAttribute("pageSize",pageSize);
		m.addAttribute("totalElements",page.getTotalElements());
		m.addAttribute("totalPages",page.getTotalPages());
		m.addAttribute("isFirst",page.isFirst());
		m.addAttribute("isLast",page.isLast());
		
		
		return "admin/products";
	}
	
	@GetMapping("/deleteProduct/{id}")
	public String deleteProduct(@PathVariable int id,HttpSession session ) {
	Boolean deleteProduct=	productService.deleteProduct(id);
	
	if(deleteProduct) {
		session.setAttribute("succMsg", "Product deleted sucessfully");
	}else {
		session.setAttribute("erroMsg", "Somthing worng in server");
	}
		return "redirect:/admin/products";
	}
	
	@GetMapping("/editProduct/{id}")
	public String editProducts (@PathVariable int id,Model m) {
		m.addAttribute("product",productService.getProductById(id));
		m.addAttribute("categories",categoryService.getAllCategory());
		return "admin/edit_product";
	}
	@PostMapping("/updateProduct")
	public String updateProducts (@ModelAttribute Product product,HttpSession session
			,@RequestParam("file") MultipartFile image, Model m) {
		
		if(product.getDiscount()<0 || product.getDiscount()>100) {
			session.setAttribute("succMsg", "invalid Discount");
		}else {
		Product updateProduct=productService.updateProduct(product, image);
		
		if(!org.springframework.util.ObjectUtils.isEmpty(updateProduct)){
			
			session.setAttribute("succMsg", "Product update sucessfully");
		}else {
			session.setAttribute("erroMsg", "Somthing worng in server");
		}
		}
		return "redirect:/admin/editProduct/"+product.getId();
	}
	
	@GetMapping("/users")
	public String getAllUser(Model m,@RequestParam Integer type) {
		List<UserDtls> users=null;
		if(type==1) {
			users=userService.getUser("ROLE_USER");
		}else {
			users=userService.getUser("ROLE_ADMIN");
		}
		m.addAttribute("userType",type);
	m.addAttribute("users", users);
		return "admin/users";
	}

	@GetMapping("updateSts")
	public String updateUSerAccountStatus(@RequestParam Boolean status,@RequestParam Integer id,
			@RequestParam Integer type,HttpSession session) {
		
		Boolean f=userService.updateAccountStatus(id,status);
		if(f) {
			session.setAttribute("succMsg", "Account Status Update");
		}else {
			session.setAttribute("errorMsg", "Somthing worng in server");
		}
		return "redirect:/admin/users?type="+type;
	}
	
	/*@GetMapping("/orders")
	public String getAllOrders(Model m) {
	   List<ProductOrder> allOrders = orderService.getAllOrders();
		m.addAttribute("orders",allOrders);
		return "admin/orders";
	}*/
	
	//my code 
	@GetMapping("/orders")
	public String getAllOrders(Model m,
	    @RequestParam(defaultValue = "") String ch,
	    @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
	    @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize) {

	    List<ProductOrder> allOrders = orderService.getAllOrders();

	    // Sort: pending/processing first
	    allOrders.sort((o1, o2) -> {
	        List<String> completed = List.of("Delivered", "Cancelled");
	        boolean o1Done = completed.contains(o1.getStatus());
	        boolean o2Done = completed.contains(o2.getStatus());
	        return Boolean.compare(o1Done, o2Done);
	    });

	    // Manual pagination
	    int start = pageNo * pageSize;
	    int end = Math.min(start + pageSize, allOrders.size());
	    List<ProductOrder> pageOrders = allOrders.subList(start, end);

	    int totalPages = (int) Math.ceil((double) allOrders.size() / pageSize);

	    m.addAttribute("orders", pageOrders);
	    m.addAttribute("pageNo", pageNo);
	    m.addAttribute("pageSize", pageSize);
	    m.addAttribute("totalElements", allOrders.size());
	    m.addAttribute("totalPages", totalPages);
	    m.addAttribute("isFirst", pageNo == 0);
	    m.addAttribute("isLast", pageNo == totalPages - 1);
	    m.addAttribute("srch", false);

	    return "admin/orders";
	}

	//end of my code
	
	@PostMapping("/update-order-status")
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!org.springframework.util.ObjectUtils.isEmpty(updateOrder)) {
			session.setAttribute("succMsg", "Status Updated");
		}else {
			session.setAttribute("errorMsg", "Status not update");
		}
		
		return "redirect:/admin/orders"; 
	}
	
	@GetMapping("/search-order")
	public String searchProduct(@RequestParam(required = false) String orderId,
	                            Model m,
	                            HttpSession session) {
	    // ✅ If orderId is empty, redirect to show all orders
	    if (orderId == null || orderId.trim().isEmpty()) {
	        return "redirect:/admin/orders";
	    }

	    ProductOrder orders = orderService.getOrdersById(orderId.trim());

	    if (orders == null) {
	        session.setAttribute("errorMsg", "Incorrect OrderId");
	        m.addAttribute("orderDtls", null);
	    } else {
	        m.addAttribute("orderDtls", orders);
	    }

	    m.addAttribute("srch", true);

	    return "admin/orders";
	}

	@GetMapping("/add-admin")
	public String loadAdminAdd() {
		return "admin/add_admin";
	}
	
	@PostMapping("/save-admin")
	public String saveAdmin(@ModelAttribute UserDtls user,@RequestParam("img") MultipartFile file,HttpSession session) throws IOException {
		String imageName=file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
		user.setProfileImage(imageName);
		UserDtls saveUser=userService.saveAdmin(user);
		
		if(!org.springframework.util.ObjectUtils.isEmpty(saveUser));{
			if(!file.isEmpty()) {
					File saveFile=new ClassPathResource("static/img").getFile();
				
				
				Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+"profile_img"+File.separator+file.getOriginalFilename());
				System.out.println(path);
				
				Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
				
			session.setAttribute("succMsg", "Saved Succesfully");
			}else {
				session.setAttribute("errorMsg", "somthing worng in server");
			}
		}
		
		return "redirect:/admin/add-admin";
	}
	
	@GetMapping("/profile")
	public String profile() {
		return "admin/profile";
	}
	@PostMapping("/update-profile")
	public String updateProfile(@ModelAttribute UserDtls user,@RequestParam MultipartFile img,HttpSession session) {
		
		UserDtls updateUserProfile= userService.updateUserProfile(user, img);
		
		if(org.springframework.util.ObjectUtils.isEmpty(updateUserProfile)){
			session.setAttribute("erroMsg", "Profile not updated");
		}else {
			session.setAttribute("succMsg", "Profile Updated");
		}
		return "redirect:/admin/profile";
	}
	
	@PostMapping("/change-password")
	public String changePassword(@RequestParam String newPassword,@RequestParam String currentPassword,Principal p,HttpSession session) {
		
		UserDtls loggedUserDetails=commonUtil.getLoggedInUserDetails(p);
		
		boolean matches =passwordEncoder.matches(currentPassword, loggedUserDetails.getPassword());
		
		if(matches) {
				String encodePassword=	passwordEncoder.encode(newPassword);
				
				loggedUserDetails.setPassword(encodePassword);
				UserDtls  updateUser=userService.updateUser(loggedUserDetails);
				if(!org.springframework.util.ObjectUtils.isEmpty(updateUser)) {
					session.setAttribute("succMsg","password in changed successfully");
				}else {
					session.setAttribute("errorMsg","Password in not changing");
				}
		}else {
			session.setAttribute("errorMsg","Current Password is incorrect");
		}
		return "redirect:/admin/profile";
	}
}
