package com.you.a.controller.home;

import java.util.Date;
import java.util.HashMap;
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
import com.you.a.entity.home.Favorite;
import com.you.a.service.common.AccountService;
import com.you.a.service.common.ProductCategoryService;
import com.you.a.service.common.ProductService;
import com.you.a.service.home.FavoriteService;
import com.you.a.util.MenuUtil;

@RequestMapping("/favorite")
@Controller
public class FavoriteController {
	
	@Autowired 
	private AccountService accountService;
	
	@Autowired
	private ProductCategoryService productCategoryService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private FavoriteService favoriteService;

	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public ModelAndView index(ModelAndView model,Integer page,HttpServletRequest request) {
		model.addObject("productCategoryList",MenuUtil.getTreeCategory(productCategoryService.findList(new HashMap<String, Object>())));
		model.addObject("allCategoryId","shop_hd_menu_all_category");
		Account onlineAccount=(Account)request.getSession().getAttribute("account");
		Map<String, Object> queryMap=new HashMap<String, Object>();
		if(page==null||page.intValue()<=0) {
			page=1;
		}
		queryMap.put("offset", (page-1)*16);
		queryMap.put("pageSize", 16);
		queryMap.put("userId", onlineAccount.getId());
		queryMap.put("orderBy", "createTime");
		queryMap.put("sort", "desc");
		model.addObject("favoriteList",favoriteService.findList(queryMap));
		model.addObject("currentUser","current_");
		model.addObject("page",page);
		model.setViewName("home/favorite/list");
		return model;
	}
	

	@RequestMapping(value = "/add",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> add(Favorite favorite,HttpServletRequest request) {
		Map<String, String> ret=new HashMap<String, String>();
		Account onlineAccount=(Account)request.getSession().getAttribute("account");
		ret.put("type", "error");
		if(favorite==null) {
			ret.put("msg", "请选择正确的商品信息");
			return ret;
		}
		if(favorite.getProductId()==null) {
			ret.put("msg", "请选择要添加的商品");
			return ret;
		}
		
		Product product = productService.findById(favorite.getProductId());
		if(product==null) {
			ret.put("msg", "商品不存在");
			return ret;
		}
		
		Map<String, Long> queryMap=new HashMap<String, Long>();
		queryMap.put("userId", onlineAccount.getId());
		queryMap.put("productId", product.getId());
		Favorite existFavorite = favoriteService.findByIds(queryMap);
		if(existFavorite!=null) {
				ret.put("msg", "商品已经被收藏");
				return ret;
		}
		
		favorite.setImageUrl(product.getImageUrl());
		favorite.setName(product.getName());
		favorite.setPrice(product.getPrice());
		favorite.setUserId(onlineAccount.getId());
		favorite.setCreateTime(new Date());
		if(favoriteService.add(favorite)<=0) {
			ret.put("msg", "添加失败，请联系管理员");
			return ret;
		}
		ret.put("type", "success");
		return ret;
	}
	
	
	
	@RequestMapping(value = "/delete",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> delete(Long favoriteId) {
		Map<String, String> ret=new HashMap<String, String>();
		ret.put("type", "error");
		if(favoriteId==null) {
			ret.put("msg", "请选择要删除的商品");
			return ret;
		}
		
		if(favoriteService.delete(favoriteId)<=0) {
			ret.put("msg", "删除失败，请联系管理员");
			return ret;
		}
		ret.put("type", "success");
		return ret;
	}
	
}
