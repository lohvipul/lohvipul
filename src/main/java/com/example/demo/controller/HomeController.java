package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.model.UserDtls;
import com.example.demo.service.CartService;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import com.example.demo.util.AppConstant;
import com.example.demo.util.CommonUtil;

import ch.qos.logback.core.util.StringUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private CartService cartService;
	
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
				//System.out.print("category="+allActiveCategory);
			
		
	}
	
	@GetMapping("/")
	public String index(Model m, HttpSession session) {
		List<Category> allActiveCategory = categoryService.getAllActiveCategory().stream()
				.sorted((c1,c2)->c2.getId().compareTo(c1.getId()))
				.limit(6).toList();
		List<Product> allActiveProducts=productService.getAllActiveProducts("").stream()
				.sorted((p1,p2)->p2.getId().compareTo(p1.getId())).limit(8).toList();
		m.addAttribute("category",allActiveCategory);
		m.addAttribute("products",allActiveProducts);
		if (Boolean.TRUE.equals(session.getAttribute(AppConstant.SESSION_FLASH_LOGIN_OK))) {
			m.addAttribute("showLoginSuccess", true);
			session.removeAttribute(AppConstant.SESSION_FLASH_LOGIN_OK);
		}
		return "index";
	}
	
	@GetMapping("/signin")
	public String login(HttpSession session, Model model) {
		if (Boolean.TRUE.equals(session.getAttribute(AppConstant.SESSION_FLASH_LOGOUT))) {
			model.addAttribute("showLogoutSuccess", true);
			session.removeAttribute(AppConstant.SESSION_FLASH_LOGOUT);
		}
		return "login";
	}
	@GetMapping("/register")
	public String register() {
		return "register";
	}
//	@GetMapping("/base")
//	public String base() {
//		return "base";
//	}
	
	
	@GetMapping("/products")
	public String product(Model m,@RequestParam (value = "category",defaultValue = "") String category,
			@RequestParam(name="pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(name="pageSize", defaultValue = "12") Integer pageSize,@RequestParam(defaultValue = "") String ch ) {
		List<Category> categories = categoryService.getAllActiveCategory();
		System.out.println(category);
		m.addAttribute("paramValue",category);
		m.addAttribute("categories",categories);
		
		
//		List<Product>  products= productService.getAllActiveProducts(category);
//		m.addAttribute("products",products);
		Page<Product>page =null;
		if(StringUtils.isEmpty(ch)) {
			page=productService.getAllActiveProductWithPagination(pageNo,pageSize,category);
		}
		else {
			page=productService.searchActiveProductPagination(pageNo,pageSize,category,ch);
		}
	
		List<Product> products= page.getContent();
		m.addAttribute("products",products);
		m.addAttribute("productsSize",products.size());
		m.addAttribute("pageNo",page.getNumber());
		m.addAttribute("pageSize",pageSize);
		m.addAttribute("totalElements",page.getTotalElements());
		m.addAttribute("totalPages",page.getTotalPages());
		m.addAttribute("isFirst",page.isFirst());
		m.addAttribute("isLast",page.isLast());
		
		return "product";
	}
	@GetMapping("/product/{id}")
	public String view_product(@PathVariable int id,Model m) {
		Product productById= productService.getProductById(id);
		m.addAttribute("product",productById);
		return "view_product";
	}
	
	
//	@PostMapping("/saveUser")
//	public String saveUser(@ModelAttribute UserDtls user,@RequestParam("img") MultipartFile file,HttpSession session) throws IOException {
//		String imageName=file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
//		user.setProfileImage(imageName);
//		UserDtls saveUser=userService.saveUser(user);
//		
//		if(!ObjectUtils.isEmpty(saveUser));{
//			if(!file.isEmpty()) {
//					File saveFile=new ClassPathResource("static/img").getFile();
//				
//				
//				Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+"profile_img"+File.separator+file.getOriginalFilename());
//				System.out.println(path);
//				
//				Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
//				
//			session.setAttribute("succMsg", "Saved Succesfully");
//			}else {
//				session.setAttribute("errorMsg", "somthing worng in server");
//			}
//		}
//		
//		return "redirect:/register";
//	}
//	
	
//	@PostMapping("/saveUser")
//	public String saveUser(@ModelAttribute UserDtls user,
//	                       @RequestParam("img") MultipartFile file,
//	                       HttpSession session) throws IOException {
//
//	    // If no file uploaded → use default.jpg
//	    String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
//	    user.setProfileImage(imageName);
//
//	    UserDtls saveUser = userService.saveUser(user);
//
//	    if (!ObjectUtils.isEmpty(saveUser)) {
//	        if (!file.isEmpty()) {
//	            try {
//	                // Save uploaded image in external folder
//	                File saveDir = new File("uploads/profile_img");
//	                if (!saveDir.exists()) {
//	                    saveDir.mkdirs();
//	                }
//
//	                Path path = Paths.get(saveDir.getAbsolutePath(), file.getOriginalFilename());
//	                System.out.println("Saving profile image: " + path);
//
//	                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//	            } catch (Exception e) {
//	                e.printStackTrace();
//	                session.setAttribute("errorMsg", "Something went wrong while saving image.");
//	            }
//	        }
//	        session.setAttribute("succMsg", "Saved Successfully");
//	    } else {
//	        session.setAttribute("errorMsg", "Something went wrong while saving user.");
//	    }
//
//	    return "redirect:/register";
//	}

	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute UserDtls user,
	                       @RequestParam("img") MultipartFile file,
	                       HttpSession session) throws IOException {

	    // Check if email already exists
	    if (userService.existsByEmail(user.getEmail())) {
	        session.setAttribute("errorMsg", "Email already registered!");
	        return "redirect:/register";
	    }

	    // If no file uploaded → use default.jpg
	    String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
	    user.setProfileImage(imageName);

	    UserDtls saveUser = userService.saveUser(user);

	    if (!ObjectUtils.isEmpty(saveUser)) {
	        if (!file.isEmpty()) {
	            try {
	                // Save uploaded image in external folder
	                File saveDir = new File("uploads/profile_img");
	                if (!saveDir.exists()) {
	                    saveDir.mkdirs();
	                }

	                Path path = Paths.get(saveDir.getAbsolutePath(), file.getOriginalFilename());
	                System.out.println("Saving profile image: " + path);

	                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	            } catch (Exception e) {
	                e.printStackTrace();
	                session.setAttribute("errorMsg", "Something went wrong while saving image.");
	            }
	        }
	        session.setAttribute("succMsg", "Saved Successfully");
	    } else {
	        session.setAttribute("errorMsg", "Something went wrong while saving user.");
	    }

	    return "redirect:/register";
	}

	//forgot password logic code
	@GetMapping("/forgot-password")
	public String showFrogotPassword() {
		return "forgot_password.html";
	}
	
	@PostMapping("/forgot-password")
	public String processFrogotPassword(@RequestParam String email,HttpSession session,HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		
		UserDtls userByEmail= userService.getUserByEmail(email);
		if(ObjectUtils.isEmpty(userByEmail)) {
			session.setAttribute("succMsg", "invalid email");
		}else {
			
			String resetToken = UUID.randomUUID().toString();
			
			userService.updateUserResetToken(email,resetToken);
			
			//Genrate Url : http://localhost:8080/reset-password?token=sfgdbgf
			
		String url=	CommonUtil.generateUrl(request)+"/reset-password?token="+resetToken;
			
			Boolean sendMail=commonUtil.sendMail(url,email);
			
			if(sendMail) {
				session.setAttribute("succMsg", "please check your mail..Password REset Link is sent");
			}else {
				session.setAttribute("errorMsg", "somthing wrong in server ! mail not send..");
			}
		}
		
		
		return "redirect:/forgot-password";
	}
	
	@GetMapping("/reset-password")
	public String showResetPassword(@RequestParam String token,HttpSession session,Model m) {
		
		UserDtls userByToken = userService.getUserByToken(token);
		
		if(userByToken == null) {
			m.addAttribute("msg","you link is invalied");
		return "messege";	
		}
		m.addAttribute("token",token);
		return "reset_password.html";
		
	}
	
	@PostMapping("/reset-password")
	public String resetPassword(@RequestParam String token,@RequestParam String password,HttpSession session,Model m) {
		
		UserDtls userByToken = userService.getUserByToken(token);
		
		if(userByToken == null) {
			m.addAttribute("msg","you link is invalied");
		return "messege";	
		}else {
			userByToken.setPassword(passwordEncoder.encode(password));
			userByToken.setResetToken(null);
			userService.updateUser(userByToken);
			session.setAttribute("succMsg", "Password changed Successfully");
			m.addAttribute("msg","password change succesfully");
			return "messege";
		}
		
		
		
	}
	
	@GetMapping("/search")
	public String searchProduct(@RequestParam String ch,Model m) {
			List<Product> searchProducts =	productService.searchProduct(ch);
			m.addAttribute("products",searchProducts);
				return "product";
	}

}
