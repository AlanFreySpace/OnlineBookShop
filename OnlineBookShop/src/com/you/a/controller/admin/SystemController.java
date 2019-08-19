package com.you.a.controller.admin;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.you.a.entity.admin.Authority;
import com.you.a.entity.admin.Menu;
import com.you.a.entity.admin.Role;
import com.you.a.entity.admin.User;
import com.you.a.service.admin.AuthorityService;
import com.you.a.service.admin.LogService;
import com.you.a.service.admin.MenuService;
import com.you.a.service.admin.RoleService;
import com.you.a.service.admin.UserService;
import com.you.a.util.CpachaUtil;
import com.you.a.util.MenuUtil;

@Controller
@RequestMapping("/system")
public class SystemController {
	
	@Autowired
	private UserService UserService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private MenuService menuService;
	
	@Autowired
	private LogService logService;
	
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public ModelAndView index(ModelAndView model,HttpServletRequest request) {
		List<Menu> userMenus=(List<Menu>)request.getSession().getAttribute("userMenus");
		model.addObject("topMenuList",MenuUtil.getAllTopMenu(userMenus));
		model.addObject("secondMenuList",MenuUtil.getAllSecondMenu(userMenus));
		model.setViewName("system/index");
		return model;
	}
	
	@RequestMapping(value="/welcome",method=RequestMethod.GET)
	public ModelAndView welcome(ModelAndView model) {
		model.setViewName("system/welcome");
		return model;
	}
	
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public ModelAndView login(ModelAndView model) {
		model.setViewName("system/login");
		return model;
		
	}
	
	@RequestMapping(value = "/login",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> loginAct(User user,String cpacha,HttpServletRequest request){
		Map<String, String> ret=new HashMap<String, String>();
		if(user==null) {
			ret.put("type", "error");
			ret.put("msg", "����д�û���Ϣ!");
			return ret;
		}
		if(StringUtils.isEmpty(user.getUsername())) {
			ret.put("type", "error");
			ret.put("msg", "����д�û���!");
			return ret;
		}
		if(StringUtils.isEmpty(user.getPassword())) {
			ret.put("type", "error");
			ret.put("msg", "����д����!");
			return ret;
		}
		if(StringUtils.isEmpty(cpacha)) {
			ret.put("type", "error");
			ret.put("msg", "����д��֤��!");
			return ret;
		}
		Object loginCpacha = request.getSession().getAttribute("loginCpacha");
		if(loginCpacha==null) {
			ret.put("type", "error");
			ret.put("msg", "�Ự��ʱ����ˢ��ҳ��!");
			return ret;
		}
		if(!cpacha.toUpperCase().equals(loginCpacha.toString().toUpperCase())) {
			ret.put("type", "error");
			ret.put("msg", "��֤�����!");
			logService.add("�û���Ϊ"+user.getUsername()+"���û���¼ʱ�������֤�����");
			return ret;
		}
		User findByUsername = UserService.findByUsername(user.getUsername());
		if(findByUsername==null) {
			ret.put("type", "error");
			ret.put("msg", "�û���������!");
			return ret;
		}
		if(!user.getPassword().equals(findByUsername.getPassword())) {
			ret.put("type", "error");
			ret.put("msg", "�������!");
			logService.add("�û���Ϊ"+user.getUsername()+"���û���¼ʱ������������");
			return ret;
		}
		Role role = roleService.find(findByUsername.getRoleId());
		List<Authority> authorityList = authorityService.findListByRoleId(role.getId());
		String menuIds="";
		for(Authority authority:authorityList) {
			menuIds+=authority.getMenuId()+",";
		}
		if(!StringUtils.isEmpty(menuIds)) {
			menuIds=menuIds.substring(0,menuIds.length()-1);
		}
		List<Menu> userMenus = menuService.findListByIds(menuIds);
		request.getSession().setAttribute("admin", findByUsername);
		request.getSession().setAttribute("role", role);
		request.getSession().setAttribute("userMenus", userMenus);
		ret.put("type", "success");
		ret.put("msg", "��¼�ɹ�!");
		logService.add("�û���Ϊ"+user.getUsername()+"���û���¼�ɹ���");
		return ret;
	}
	
	@RequestMapping(value="/logout",method = RequestMethod.GET)
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute("admin", null);
		session.setAttribute("role", null);
		session.setAttribute("userMenus", null);
		return "redirect:login";
	}
	
	@RequestMapping(value="/edit_password",method=RequestMethod.GET)
	public ModelAndView editPassword(ModelAndView model) {
		model.setViewName("system/edit_password");
		return model;
	}
	
	@RequestMapping(value = "/edit_password",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> editPasswordAct(String newPassword,String oldPassword,HttpServletRequest request){
		Map<String, String> ret=new HashMap<String, String>();
		if(StringUtils.isEmpty(newPassword)) {
			ret.put("type", "error");
			ret.put("msg", "����д������!");
			return ret;
		}
		User user=(User) request.getSession().getAttribute("admin");
		if(!user.getPassword().equals(oldPassword)) {
			ret.put("type", "error");
			ret.put("msg", "ԭ�������!");
			return ret;
		}
		user.setPassword(newPassword);
		if(UserService.editPassword(user)<=0) {
			ret.put("type", "error");
			ret.put("msg", "�����޸�ʧ�ܣ�����ϵ����Ա!");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "�����޸ĳɹ�!");
		logService.add("�û���Ϊ"+user.getUsername()+"���û��ɹ��޸����룡");
		return ret;
	}
	
	@RequestMapping(value="/get_cpacha",method=RequestMethod.GET)
	public void generateCpacha(
			@RequestParam(name="vl",required=false,defaultValue = "4") Integer vcodeLen,
			@RequestParam(name="w",required=false,defaultValue = "100") Integer width,
			@RequestParam(name="h",required=false,defaultValue = "30") Integer height,
			@RequestParam(name="type",required=true,defaultValue = "loginCpacha") String cpachaType,
			HttpServletRequest request,
			HttpServletResponse response) {
		CpachaUtil cpachaUtil=new CpachaUtil(vcodeLen, width, height);
		String generatorVCode=cpachaUtil.generatorVCode();
		request.getSession().setAttribute(cpachaType, generatorVCode);
		BufferedImage generatorRotateVCodeImage = cpachaUtil.generatorRotateVCodeImage(generatorVCode, true);
		try {
			ImageIO.write(generatorRotateVCodeImage, "gif", response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
}
