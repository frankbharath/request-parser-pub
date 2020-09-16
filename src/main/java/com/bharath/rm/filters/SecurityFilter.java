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

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.bharath.rm.common.Utils;
import com.bharath.rm.configuration.I18NConfig;
import com.bharath.rm.configuration.RentPalThreadLocal;
import com.bharath.rm.security.SecurityXMLConfig;
import com.bharath.rm.security.UserSession;
import com.bharath.rm.security.ValidateRequest;
import com.bharath.rm.service.SpringUserDetailsService;
import com.bharath.rm.service.UserServiceImpl;
import com.bharath.rm.service.interfaces.UserService;
import com.bharath.rm.constants.Constants;
import com.bharath.rm.constants.ErrorCodes;
import com.bharath.rm.exception.APIException;
import com.bharath.rm.model.SpringUserDetails;

/**
 * Servlet Filter implementation class SecurityFilter
 */
@WebFilter
public class SecurityFilter implements Filter {

	private final UserSession userSession;
		
	@Autowired
	public SecurityFilter(UserSession userSession) {
		this.userSession=userSession;
	}
	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest servlertRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)servlertRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String url=request.getRequestURI();
		if(url.startsWith("/login") && userSession.isAuthenticated()) {
			response.sendRedirect(request.getContextPath() + "/home");
			return;
		}
		if(SecurityXMLConfig.isAuthUrl(url) && userSession.isAuthenticated()) {
			SpringUserDetails details=userSession.getLoggedInUserDetails();
			RentPalThreadLocal.init();
			RentPalThreadLocal.add("userId", details.getUserId());
			RentPalThreadLocal.add("email", details.getUsername());
		}
		chain.doFilter(request, response);
		if(SecurityXMLConfig.isAuthUrl(url) && userSession.isAuthenticated()) {
			 RentPalThreadLocal.clear();
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
