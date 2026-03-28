package com.example.demo.config;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import com.example.demo.util.AppConstant;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthSucessHandlerImpl implements AuthenticationSuccessHandler{

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		Collection<?extends GrantedAuthority> authorities=authentication.getAuthorities();
		
	Set<String> roles=	AuthorityUtils.authorityListToSet(authorities);

		request.getSession().setAttribute(AppConstant.SESSION_FLASH_LOGIN_OK, Boolean.TRUE);

		if(roles.contains("ROLE_ADMIN")) {
			response.sendRedirect("/admin/");
		}else{
			response.sendRedirect("/");
		}
	
	}

}
