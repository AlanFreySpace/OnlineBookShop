package com.you.a.controller.admin;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.you.a.entity.admin.Role;
import com.you.a.entity.admin.User;
import com.you.a.page.admin.Page;
import com.you.a.service.admin.RoleService;
import com.you.a.service.admin.UserService;

@RequestMapping("/admin/user")
@Controller
public class UserController {
	@Autowired 
	private UserService userService;
	@Autowired
	private RoleService roleService;
	
	@RequestMapping(value="/list",method = RequestMethod.GET)
	public ModelAndView list(ModelAndView model) {
		Map<String, Object> queryMap=new HashMap<String, Object>();
		model.addObject("roleList",roleService.findList(queryMap));
		model.setViewName("user/list");
		return model;
	}
	
	@RequestMapping(value="/list",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getList(Page page,
			@RequestParam(name="username",required = false,defaultValue = "") String username,
			@RequestParam(name="roleId",required=false) Long roleId,
			@RequestParam(name="sex",required=false) Integer sex
			){
		Map<String, Object> ret=new HashMap<String, Object>();
		Map<String, Object> queryMap=new HashMap<String, Object>();
		queryMap.put("username", username);
		queryMap.put("roleId", roleId);
		queryMap.put("sex", sex);
		queryMap.put("offset", page.getOffset());
		queryMap.put("pageSize", page.getRows());
		ret.put("rows", userService.findList(queryMap));
		ret.put("total", userService.getTotal(queryMap));
		return ret;
	}
	
	@RequestMapping(value="/add",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> add(User user){
		Map<String, String> ret=new HashMap<String, String>();
		if(user==null) {
			ret.put("type", "error");
			ret.put("msg", "请填写正确的用户信息！");
			return ret;
		}
		if(StringUtils.isEmpty(user.getUsername())) {
			ret.put("type", "error");
			ret.put("msg", "请填写用户名！");
			return ret;
		}
		if(StringUtils.isEmpty(user.getPassword())) {
			ret.put("type", "error");
			ret.put("msg", "请填写密码！");
			return ret;
		}
		if(user.getRoleId()==null) {
			ret.put("type", "error");
			ret.put("msg", "请选择所属角色！");
			return ret;
		}
		if(isExist(user.getUsername(), 0l)) {
			ret.put("type", "error");
			ret.put("msg", "该用户名已存在");
			return ret;
		}
		if(userService.add(user)<=0) {
			ret.put("type", "error");
			ret.put("msg", "用户添加失败，请联系管理员！");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "角色添加成功！");
		return ret;
	}
	
	@RequestMapping(value="/edit",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> edit(User user){
		Map<String, String> ret=new HashMap<String, String>();
		if(user==null) {
			ret.put("type", "error");
			ret.put("msg", "请填写正确的用户信息！");
			return ret;
		}
		if(StringUtils.isEmpty(user.getUsername())) {
			ret.put("type", "error");
			ret.put("msg", "请填写用户名！");
			return ret;
		}
		if(user.getRoleId()==null) {
			ret.put("type", "error");
			ret.put("msg", "请选择所属角色！");
			return ret;
		}
		if(isExist(user.getUsername(), user.getId())) {
			ret.put("type", "error");
			ret.put("msg", "该用户名已存在");
			return ret;
		}
		if(userService.edit(user)<=0) {
			ret.put("type", "error");
			ret.put("msg", "用户添加失败，请联系管理员！");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "角色添加成功！");
		return ret;
	}
	
	@RequestMapping(value="/delete",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> delete(String ids){
		Map<String, String> ret=new HashMap<String, String>();
		if(StringUtils.isEmpty(ids)) {
			ret.put("type", "error");
			ret.put("msg", "请选择要删除的数据！");
			return ret;
		}
		if(ids.contains(",")) {
			ids=ids.substring(0, ids.length()-1);
		}
		if(userService.delete(ids)<=0) {
			ret.put("type", "error");
			ret.put("msg", "用户删除失败，请联系管理员！");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "用户删除成功！");
		return ret;
	}
	
	
	@RequestMapping(value="/upload_photo",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> uploadPhoto(MultipartFile photo,HttpServletRequest request){
		Map<String, String> ret=new HashMap<String, String>();
		if(photo==null) {
			ret.put("type", "error");
			ret.put("msg", "请选择要上传的文件！");
			return ret;
		}
		if(photo.getSize()>1024*10240) {
			ret.put("type", "error");
			ret.put("msg", "文件大小不能超过10M！");
			return ret;
		}
		String suffix=photo.getOriginalFilename().substring(photo.getOriginalFilename().lastIndexOf(".")+1, photo.getOriginalFilename().length());
		if(!"jpg,jpeg,gif,png".toUpperCase().contains(suffix.toUpperCase())) {
			ret.put("type", "error");
			ret.put("msg", "请选择jpg,jpeg,gif,png格式的图片！");
			return ret;
		}
		
		String savePath=request.getServletContext().getRealPath("/")+"/resources/upload/";
		File savePathFile=new File(savePath);
		if(!savePathFile.exists()) {
			savePathFile.mkdir();
		}
		String filename=new Date().getTime()+"."+suffix;
		try {
			photo.transferTo(new File(savePath+filename));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ret.put("type", "error");
			ret.put("msg", "保存文件异常！");
			e.printStackTrace();
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "图片上传成功！");
		ret.put("filepath", request.getServletContext().getContextPath()+"/resources/upload/"+filename);
		return ret;
	}
	
	
	private boolean isExist(String username,Long id) {
		User user = userService.findByUsername(username);
		if(user==null) return false;
		if(user.getId().longValue()==id.longValue()) return false;
		return true;
	}
}
