package com.bharath.rm.filters;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import com.bharath.rm.common.Utils;
import com.bharath.rm.security.ValidateRequest;
import com.bharath.rm.constants.Constants;

/**
 * Servlet Filter implementation class SecurityFilter
 */
@WebFilter
public class SecurityFilter implements Filter {

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		JSONObject resp=ValidateRequest.checkValidity(req);
		if(resp.has(Constants.STATUS) && Constants.SUCCESS.equals(resp.get(Constants.STATUS))) {
			chain.doFilter(request, response);
		}else {
			Utils.sendJSONResponse((HttpServletResponse)response, resp.toString());
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
