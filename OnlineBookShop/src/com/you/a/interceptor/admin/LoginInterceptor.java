package com.you.a.interceptor.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonObject;
import com.you.a.entity.admin.Menu;
import com.you.a.util.MenuUtil;

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
		Object admin = request.getSession().getAttribute("admin");
		if(admin==null) {
			System.out.println("Á´½Ó"+requestURI+"±»À¹½Ø");
			String header = request.getHeader("X-Requested-With");
			
			if("XMLHttpRequest".equals(header)) {
				Map<String, String> ret=new HashMap<String, String>();
				ret.put("type", "error");
				ret.put("msg", "µÇÂ¼»á»°³¬Ê±»ò»¹Î´µÇÂ¼£¬ÇëµÇÂ¼£¡");
				response.getWriter().write(JSONObject.fromObject(ret).toString());
				return false;
			}
			
			response.sendRedirect(request.getServletContext().getContextPath()+"/system/login");//login
			return false;
		}
		String mid = request.getParameter("_mid");
		if(!StringUtils.isEmpty(mid)) {
			List<Menu> allThirdMenu = MenuUtil.getAllThirdMenu((List<Menu>)request.getSession().getAttribute("userMenus"), Long.valueOf(mid));
			request.setAttribute("thirdMenuList", allThirdMenu);
		}
		return true;
	}

}
