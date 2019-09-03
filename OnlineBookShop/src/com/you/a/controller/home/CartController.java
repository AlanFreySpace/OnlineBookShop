package com.you.a.controller.home;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.you.a.entity.common.Account;
import com.you.a.entity.common.Product;
import com.you.a.entity.home.Cart;
import com.you.a.service.common.AccountService;
import com.you.a.service.common.ProductCategoryService;
import com.you.a.service.common.ProductService;
import com.you.a.service.home.AddressService;
import com.you.a.service.home.CartService;
import com.you.a.util.MenuUtil;

@RequestMapping("/cart")
@Controller
public class CartController {
	
	@Autowired 
	private AccountService accountService;
	
	@Autowired
	private ProductCategoryService productCategoryService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private AddressService addressService;

	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public ModelAndView list(ModelAndView model,HttpServletRequest request) {
		model.addObject("productCategoryList",MenuUtil.getTreeCategory(productCategoryService.findList(new HashMap<String, Object>())));
		model.addObject("allCategoryId","shop_hd_menu_all_category");
		Account onlineAccount=(Account)request.getSession().getAttribute("account");
		Map<String, Object> queryMap=new HashMap<String, Object>();
		queryMap.put("userId", onlineAccount.getId());
		List<Cart> cartList = cartService.findList(queryMap);
		model.addObject("cartList",cartList);
		double total=0;
		for(Cart cart:cartList) {
			Product product = productService.findById(cart.getProductId());
			if(product.getProductCategoryId()==123) {
				total+=product.getPrice();
			}
		}
		if(total>=99) {
			model.addObject("zhekou",70);
		}
		model.addObject("currentCart","current_");
		model.setViewName("home/cart/list");
		return model;
	}
	
	
	@RequestMapping(value = "/list_2",method = RequestMethod.GET)
	public ModelAndView list2(ModelAndView model,HttpServletRequest request) {
		model.addObject("productCategoryList",MenuUtil.getTreeCategory(productCategoryService.findList(new HashMap<String, Object>())));
		model.addObject("allCategoryId","shop_hd_menu_all_category");
		Account onlineAccount=(Account)request.getSession().getAttribute("account");
		Map<String, Object> queryMap=new HashMap<String, Object>();
		queryMap.put("userId", onlineAccount.getId());
		List<Cart> cartList = cartService.findList(queryMap);
		model.addObject("cartList",cartList);
		double total=0;
		for(Cart cart:cartList) {
			Product product = productService.findById(cart.getProductId());
			if(product.getProductCategoryId()==123) {
				total+=product.getPrice();
			}
		}
		if(total>=99) {
			model.addObject("zhekou",70);
		}
		
		model.addObject("currentCart","current_");
		model.addObject("addressList",addressService.findList(queryMap));
		model.setViewName("home/cart/list_2");
		return model;
	}
	

	@RequestMapping(value = "/add",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> add(Cart cart,HttpServletRequest request) {
		Map<String, String> ret=new HashMap<String, String>();
		Account onlineAccount=(Account)request.getSession().getAttribute("account");
		ret.put("type", "error");
		if(cart==null) {
			ret.put("msg", "请选择正确的商品信息");
			return ret;
		}
		if(cart.getProductId()==null) {
			ret.put("msg", "请选择要添加的商品");
			return ret;
		}
		if(cart.getNum()==0) {
			ret.put("msg", "请填写商品数量");
			return ret;
		}
		Product product = productService.findById(cart.getProductId());
		if(product==null) {
			ret.put("msg", "商品不存在");
			return ret;
		}
		
		Map<String, Long> queryMap=new HashMap<String, Long>();
		queryMap.put("userId", onlineAccount.getId());
		queryMap.put("productId", product.getId());
		Cart existCart = cartService.findByIds(queryMap);
		if(existCart!=null) {
			existCart.setNum(existCart.getNum()+cart.getNum());
			existCart.setMoney(existCart.getNum()*existCart.getPrice());
			if(cartService.edit(existCart)<=0) {
				ret.put("msg", "商品已经添加至购物车，但更新数量出错");
				return ret;
			}
			ret.put("type", "success");
			return ret;
		}
		
		cart.setImageUrl(product.getImageUrl());
		cart.setMoney(product.getPrice()*cart.getNum());
		cart.setName(product.getName());
		cart.setPrice(product.getPrice());
		cart.setUserId(onlineAccount.getId());
		cart.setCreateTime(new Date());
		if(cartService.add(cart)<=0) {
			ret.put("msg", "添加失败，请联系管理员");
			return ret;
		}
		ret.put("type", "success");
		return ret;
	}
	
	@RequestMapping(value = "/update_num",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> updateNum(Long cartId,Integer num) {
		Map<String, String> ret=new HashMap<String, String>();
		ret.put("type", "error");
		Cart cart = cartService.findById(cartId);
		if(cart==null) {
			ret.put("msg", "请选择正确的商品信息");
			return ret;
		}
		if(num==null) {
			ret.put("msg", "请填写商品数量");
			return ret;
		}
		Product product = productService.findById(cart.getProductId());
		if(product==null) {
			ret.put("msg", "购物车信息有误");
			return ret;
		}
		if(cart.getNum()+num.intValue()>product.getStock()) {
			ret.put("msg", "商品数量不能超过库存量");
			return ret;
		}
		
		cart.setNum(cart.getNum()+num);
		cart.setMoney(cart.getNum()*cart.getPrice());
		if(cartService.edit(cart)<=0) {
			ret.put("msg", "商品已经添加至购物车，但更新数量出错");
			return ret;
		}
		ret.put("type", "success");
		return ret;
	}
	
	@RequestMapping(value = "/delete",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> delete(Long cartId) {
		Map<String, String> ret=new HashMap<String, String>();
		ret.put("type", "error");
		if(cartId==null) {
			ret.put("msg", "请选择要删除的商品");
			return ret;
		}
		
		if(cartService.delete(cartId)<=0) {
			ret.put("msg", "删除失败，请联系管理员");
			return ret;
		}
		ret.put("type", "success");
		return ret;
	}
	
}
