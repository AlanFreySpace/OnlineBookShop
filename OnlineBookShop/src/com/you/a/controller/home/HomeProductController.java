package com.you.a.controller.home;

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
import org.springframework.web.servlet.ModelAndView;

import com.you.a.entity.common.Product;
import com.you.a.entity.common.ProductCategory;
import com.you.a.service.common.AccountService;
import com.you.a.service.common.CommentService;
import com.you.a.service.common.ProductCategoryService;
import com.you.a.service.common.ProductService;
import com.you.a.util.MenuUtil;

@RequestMapping("/product")
@Controller
public class HomeProductController {
	
	@Autowired 
	private AccountService accountService;
	
	@Autowired
	private ProductCategoryService productCategoryService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CommentService commentService;

	@RequestMapping(value = "/detail",method = RequestMethod.GET)
	public ModelAndView index(ModelAndView model,Long id) {
		model.addObject("productCategoryList",MenuUtil.getTreeCategory(productCategoryService.findList(new HashMap<String, Object>())));
		model.addObject("allCategoryId","shop_hd_menu_all_category");
		if(id==null) {
			model.addObject("msg","非法商品id");
			model.setViewName("home/common/error");
			return model;
		}
		Product product = productService.findById(id);
		if(product==null) {
			model.addObject("msg","商品不存在");
			model.setViewName("home/common/error");
			return model;
		}
		model.addObject("product",product);
		Map<String, Object> queryMap=new HashMap<String, Object>();
		queryMap.put("offset", 0);
		queryMap.put("pageSize", 10);
		queryMap.put("sort", "desc");
		queryMap.put("orderBy", "sellNum");
		queryMap.put("productId", id);
		model.addObject("sellProductList",productService.findList(queryMap));
		model.addObject("currentHome","current_");
		model.addObject("commentList",commentService.findList(queryMap));
		model.setViewName("home/product/detail");
		product.setViewNum(product.getViewNum()+1);
		productService.updateNum(product);
		return model;
	}
	
	@RequestMapping(value = "/product_category_list",method = RequestMethod.GET)
	public ModelAndView login(
			@RequestParam(name="cid",required = true)Long cid,
			@RequestParam(name="orderby",required = false)String orderby,
			@RequestParam(name="priceMin",required = false)Double priceMin,
			@RequestParam(name="priceMax",required = false)Double priceMax,
			@RequestParam(name="page",required = false)Integer page,
			ModelAndView model
			) {
		model.addObject("productCategoryList",MenuUtil.getTreeCategory(productCategoryService.findList(new HashMap<String, Object>())));
		model.addObject("currentHome","current_");
		model.addObject("allCategoryId","shop_hd_menu_all_category");
		if(cid==null) {
			model.addObject("msg","分类不存在");
			model.setViewName("home/common/error");
			return model;
		}
		ProductCategory productCategory = productCategoryService.findById(cid);
		if(productCategory==null) {
			model.addObject("msg","分类不存在");
			model.setViewName("home/common/error");
			return model;
		}
		model.addObject("title","分类【"+productCategory.getName()+"】下的商品");
		Map<String, Object> queryListMap=new HashMap<String, Object>();
		queryListMap.put("tags", cid);
		if(!StringUtils.isEmpty(orderby)) {
			queryListMap.put("orderBy", orderby);
			queryListMap.put("sort", "desc");
		}
		if(priceMin!=null) {
			queryListMap.put("priceMin", priceMin);
		}
		if(priceMax!=null) {
			queryListMap.put("priceMax", priceMax);
		}
		if(page==null||page.intValue()<=0) {
			page=1;
		}
		queryListMap.put("offset", (page-1)*20);
		queryListMap.put("pageSize", 20);
		model.addObject("productList",productService.findList(queryListMap));
		model.addObject("cid",cid);
		model.addObject("orderby",orderby);
		model.addObject("priceMin",priceMin);
		model.addObject("priceMax",priceMax);
		model.addObject("page",page);
		
		Map<String, Object> queryMap=new HashMap<String, Object>();
		queryMap.put("offset", 0);
		queryMap.put("pageSize", 10);
		queryMap.put("sort", "desc");
		queryMap.put("orderBy", "sellNum");
		model.addObject("sellProductList",productService.findList(queryMap));
//		model.addObject("allCategoryId","shop_hd_menu_all_category");
		model.setViewName("home/product/list");
		return model;
	}
	
	@RequestMapping(value = "/search")
	@ResponseBody
	public ModelAndView search(
			@RequestParam(name="search_content",required = true)String search_content,
			@RequestParam(name="orderby",required = false)String orderby,
			@RequestParam(name="priceMin",required = false)Double priceMin,
			@RequestParam(name="priceMax",required = false)Double priceMax,
			@RequestParam(name="page",required = false)Integer page,
			ModelAndView model,HttpServletRequest request
			) {
		
		
		model.addObject("title","搜索【"+search_content+"】下的商品");
		model.addObject("currentHome","current_");
		Map<String, Object> queryListMap=new HashMap<String, Object>();
		queryListMap.put("name", search_content);
		if(!StringUtils.isEmpty(orderby)) {
			queryListMap.put("orderBy", orderby);
			queryListMap.put("sort", "desc");
		}
		if(priceMin!=null) {
			queryListMap.put("priceMin", priceMin);
		}
		if(priceMax!=null) {
			queryListMap.put("priceMax", priceMax);
		}
		if(page==null||page.intValue()<=0) {
			page=1;
		}
		queryListMap.put("offset", (page-1)*20);
		queryListMap.put("pageSize", 20);
		model.addObject("productList",productService.findList(queryListMap));
		
		
		
		model.addObject("search_content",search_content);
		model.addObject("orderby",orderby);
		model.addObject("priceMin",priceMin);
		model.addObject("priceMax",priceMax);
		model.addObject("page",page);
		
		Map<String, Object> queryMap=new HashMap<String, Object>();
		queryMap.put("offset", 0);
		queryMap.put("pageSize", 10);
		queryMap.put("sort", "desc");
		queryMap.put("orderBy", "sellNum");
		model.addObject("sellProductList",productService.findList(queryMap));
		model.addObject("productCategoryList",MenuUtil.getTreeCategory(productCategoryService.findList(new HashMap<String, Object>())));
		model.addObject("allCategoryId","shop_hd_menu_all_category");
		model.setViewName("home/product/search");
		return model;
	}
	
	
}
