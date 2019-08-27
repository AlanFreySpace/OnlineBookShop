package com.you.a.controller.home;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.you.a.entity.common.Account;
import com.you.a.entity.home.Address;
import com.you.a.service.common.AccountService;
import com.you.a.service.common.ProductCategoryService;
import com.you.a.service.common.ProductService;
import com.you.a.service.home.AddressService;
import com.you.a.util.MenuUtil;

@RequestMapping("/address")
@Controller
public class AddressController {
	
	@Autowired 
	private AccountService accountService;
	
	@Autowired
	private ProductCategoryService productCategoryService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private AddressService addressService;

	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public ModelAndView index(ModelAndView model,HttpServletRequest request) {
		model.addObject("productCategoryList",MenuUtil.getTreeCategory(productCategoryService.findList(new HashMap<String, Object>())));
		model.addObject("allCategoryId","shop_hd_menu_all_category");
		Account onlineAccount=(Account)request.getSession().getAttribute("account");
		Map<String, Object> queryMap=new HashMap<String, Object>();
		queryMap.put("userId", onlineAccount.getId());
		model.addObject("addressList",addressService.findList(queryMap));
		model.addObject("currentUser","current_");
		model.setViewName("home/address/list");
		return model;
	}
	

	@RequestMapping(value = "/add",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> add(Address address,HttpServletRequest request) {
		Map<String, String> ret=new HashMap<String, String>();
		Account onlineAccount=(Account)request.getSession().getAttribute("account");
		ret.put("type", "error");
		if(address==null) {
			ret.put("msg", "请选择正确的收货信息");
			return ret;
		}
		if(StringUtils.isEmpty(address.getName())) {
			ret.put("msg", "请填写收货人");
			return ret;
		}
		if(StringUtils.isEmpty(address.getAddress())) {
			ret.put("msg", "请填写收货地址");
			return ret;
		}
		if(StringUtils.isEmpty(address.getPhone())) {
			ret.put("msg", "请填写手机号");
			return ret;
		}
		
		address.setUserId(onlineAccount.getId());
		address.setCreateTime(new Date());
		if(addressService.add(address)<=0) {
			ret.put("msg", "添加失败，请联系管理员");
			return ret;
		}
		ret.put("type", "success");
		return ret;
	}
	
	@RequestMapping(value = "/edit",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> edit(Address address,HttpServletRequest request) {
		Map<String, String> ret=new HashMap<String, String>();
		Account onlineAccount=(Account)request.getSession().getAttribute("account");
		ret.put("type", "error");
		if(address==null) {
			ret.put("msg", "请选择正确的收货信息");
			return ret;
		}
		Address existAddress = addressService.findById(address.getId());
		if(existAddress==null) {
			ret.put("msg", "不存在该地址");
			return ret;
		}
		if(StringUtils.isEmpty(address.getName())) {
			ret.put("msg", "请填写收货人");
			return ret;
		}
		if(StringUtils.isEmpty(address.getAddress())) {
			ret.put("msg", "请填写收货地址");
			return ret;
		}
		if(StringUtils.isEmpty(address.getPhone())) {
			ret.put("msg", "请填写手机号");
			return ret;
		}
		
		address.setUserId(onlineAccount.getId());
		if(addressService.edit(address)<=0) {
			ret.put("msg", "编辑失败，请联系管理员");
			return ret;
		}
		ret.put("type", "success");
		return ret;
	}
	
	@RequestMapping(value = "/delete",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> delete(Long id) {
		Map<String, String> ret=new HashMap<String, String>();
		ret.put("type", "error");
		if(id==null) {
			ret.put("msg", "请选择要删除的地址");
			return ret;
		}
		
		if(addressService.delete(id)<=0) {
			ret.put("msg", "删除失败，请联系管理员");
			return ret;
		}
		ret.put("type", "success");
		return ret;
	}
	
}
