package com.example.demo.util;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.example.demo.model.ProductOrder;
import com.example.demo.model.UserDtls;
import com.example.demo.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CommonUtil {
	
	@Autowired
	private  JavaMailSender mailSender;
	
	@Autowired
	private UserService userService;
	
	public  Boolean sendMail(String url,String reciepentEmail) throws UnsupportedEncodingException, MessagingException {
	MimeMessage message=	mailSender.createMimeMessage();
	MimeMessageHelper helper=new MimeMessageHelper(message);
	
	helper.setFrom("mihirc855@gmail.com", "Arbuda Online Shopping Cart");
	helper.setTo(reciepentEmail);
	
	String contetn=  "<p>Hi there,</p>"
	        + "<p>We received a request to reset your password for your account.</p>"
	        + "<p>If you made this request, please click the button below to set a new password:</p>"
	        + "<p style=\"margin-top: 20px;\"><a href=\"" + url + "\" "
	        + "style=\"background-color: #0d6efd; color: white; padding: 10px 20px; "
	        + "text-decoration: none; border-radius: 5px; font-weight: bold;\">Reset Password</a></p>"
	        + "<p style=\"margin-top: 30px;\">If you didn’t request a password reset, you can safely ignore this email.</p>"
	        + "<p>Thanks,<br>The Support Team</p>";
 
	helper.setSubject("Password Reset Regarding mail");
	helper.setText(contetn,true);
	mailSender.send(message);
		
		return true;
	}

	public static String generateUrl(HttpServletRequest request) {
	 String siteUrl=	request.getRequestURL().toString();
	 
	return siteUrl.replace(request.getServletPath(), "");	
	
	}
	
	String msg=null;
			
	public Boolean sendMailForProduct(ProductOrder order,String status)throws Exception {
		Double taxPrice=100.0;
		Double deliveryCharge=250.0;
		Double prices = order.getPrice();
		Double total=taxPrice+deliveryCharge+prices;
		long totalRounded = Math.round(total); // round to nearest whole number
		
		msg="<p>Thank you for shopping with us! We're happy to inform you that your order has been <strong>[[orderStatus]]</strong>.</p>"
				+ "<p><b>Product Details : <b></p>"
				+"<p>Name : [[productName]] </p>"
				+"<p>Category : [[category]] </p>"
				+"<p>Quantity : [[quantity]] </p>"
				+"<p>Price: [[price]] </p>"
				+"<p>Payment Type : [[paymentType]] </p>"
				+"<br>"
				+"<p>Best regards,<br>The Online Shopping Cart Team</p>";
		 
		MimeMessage message=	mailSender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message);
		
		helper.setFrom("mihirc855@gmail.com", "Chaudhary_shooping Cart");
		helper.setTo(order.getOrderAddress().getEmail());
		
		msg=msg.replace("[[name]]",order.getOrderAddress().getFirstName());
		msg=msg.replace("[[orderStatus]]", status);
		msg=msg.replace("[[productName]]", order.getProduct().getTitle());
		msg=msg.replace("[[category]]", order.getProduct().getCategory());
		msg=msg.replace("[[quantity]]", order.getQuantity().toString());
		msg = msg.replace("[[price]]", Long.toString(totalRounded));
		msg=msg.replace("[[paymentType]]", order.getPaymentType());
	 
		helper.setSubject("Product Order Status");
		helper.setText(msg,true);
		mailSender.send(message);
			
			return true;
	}
	public UserDtls getLoggedInUserDetails(Principal p) {
		String email=p.getName();
		UserDtls userDtls= userService.getUserByEmail(email);
		return userDtls;
	}
}
