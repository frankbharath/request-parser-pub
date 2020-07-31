package com.bharath.rm.filters;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.bharath.rm.common.Utils;
import com.bharath.rm.configuration.I18NConfig;
import com.bharath.rm.configuration.RentPalThreadLocal;
import com.bharath.rm.security.SecurityXMLConfig;
import com.bharath.rm.security.ValidateRequest;
import com.bharath.rm.service.SpringUserDetailsService;
import com.bharath.rm.service.UserServiceImpl;
import com.bharath.rm.service.interfaces.UserService;
import com.bharath.rm.constants.Constants;
import com.bharath.rm.constants.ErrorCodes;
import com.bharath.rm.model.SpringUserDetails;

/**
 * Servlet Filter implementation class SecurityFilter
 */
@WebFilter
public class SecurityFilter implements Filter {

	private final SpringUserDetailsService userDetailsService;
	
	private final UserService userService;
	
	@Autowired
	public SecurityFilter(SpringUserDetailsService userDetailsService, UserService userService) {
		this.userDetailsService=userDetailsService;
		this.userService=userService;
	}
	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String url=req.getRequestURI();
		if(url.startsWith("/login") && userDetailsService.isAuthenticated()) {
			resp.sendRedirect(req.getContextPath() + "/home");
			return;
		}
		if((!url.startsWith("/resources/") && !SecurityXMLConfig.isNoAuthUrl(url)) && userDetailsService.isAuthenticated()) {
			SpringUserDetails details=userDetailsService.getLoggedInUserDetails();
			if(!userService.userVerificationStatus(details.getUserId())) {
				if(url.startsWith("/api")) {
					Utils.sendJSONResponse(resp, Utils.getErrorObject(ErrorCodes.EMAIL_NOT_VERIFIED, I18NConfig.getMessage("error.user.email_not_verified")).toString());
				}else {
					resp.sendRedirect(req.getContextPath() + "/verify");
				}
				return;
			}
			RentPalThreadLocal.init();
			RentPalThreadLocal.add("userId", details.getUserId());
			RentPalThreadLocal.add("email", details.getUsername());
		}
		if(!url.startsWith("/resources/") && !url.equals("/")) {
			JSONObject respObj=ValidateRequest.checkValidity(req);
			if(respObj.has(Constants.STATUS) && Constants.SUCCESS.equals(respObj.get(Constants.STATUS))) {
				chain.doFilter(request, response);
			}else {
				resp.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
				Utils.sendJSONResponse(resp, respObj.toString());
			}
		}else {
			chain.doFilter(request, response);
		}
		if((!url.startsWith("/resources/") && !SecurityXMLConfig.isNoAuthUrl(url)) && userDetailsService.isAuthenticated()) {
			RentPalThreadLocal.clear();
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
