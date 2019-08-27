package com.you.a.interceptor.home;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import net.sf.json.JSONObject;



public class LoginInterceptor implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		// TODO Auto-generated method stub
		String requestURI = request.getRequestURI();
		Object account = request.getSession().getAttribute("account");
		if(account==null) {
			System.out.println("链接"+requestURI+"被拦截");
			String header = request.getHeader("X-Requested-With");
			
			if("XMLHttpRequest".equals(header)) {
				Map<String, String> ret=new HashMap<String, String>();
				ret.put("type", "error");
				ret.put("msg", "登录会话超时或还未登录，请登录！");
				response.getWriter().write(JSONObject.fromObject(ret).toString());
				return false;
			}
			
			response.sendRedirect(request.getServletContext().getContextPath()+"/home/login");//login
			return false;
		}
		return true;
	}

}
