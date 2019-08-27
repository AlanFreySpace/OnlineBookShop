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
import com.you.a.entity.common.Comment;
import com.you.a.entity.common.Product;
import com.you.a.service.common.AccountService;
import com.you.a.service.common.CommentService;
import com.you.a.service.common.ProductCategoryService;
import com.you.a.service.common.ProductService;
import com.you.a.util.MenuUtil;

@RequestMapping("/comment")
@Controller
public class HomeCommentController {
	
	@Autowired 
	private AccountService accountService;
	
	@Autowired
	private ProductCategoryService productCategoryService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CommentService commentService;

	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public ModelAndView index(ModelAndView model,Integer page,HttpServletRequest request) {
		model.addObject("productCategoryList",MenuUtil.getTreeCategory(productCategoryService.findList(new HashMap<String, Object>())));
		model.addObject("allCategoryId","shop_hd_menu_all_category");
		Account onlineAccount=(Account)request.getSession().getAttribute("account");
		Map<String, Object> queryMap=new HashMap<String, Object>();
		if(page==null||page.intValue()<=0) {
			page=1;
		}
		queryMap.put("offset", (page-1)*10);
		queryMap.put("pageSize", 10);
		queryMap.put("userId", onlineAccount.getId());
//		queryMap.put("orderBy", "createTime");
//		queryMap.put("sort", "desc");
		model.addObject("commentList",commentService.findList(queryMap));
		model.addObject("currentUser","current_");
		model.addObject("page",page);
		model.setViewName("home/comment/list");
		return model;
	}
	

	@RequestMapping(value = "/add",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> add(Comment comment,HttpServletRequest request) {
		Map<String, String> ret=new HashMap<String, String>();
		Account onlineAccount=(Account)request.getSession().getAttribute("account");
		ret.put("type", "error");
		if(comment==null) {
			ret.put("msg", "请填写正确的评价信息");
			return ret;
		}
		if(StringUtils.isEmpty(comment.getContent())) {
			ret.put("msg", "请填写评价内容");
			return ret;
		}
		comment.setCreateTime(new Date());
		comment.setUserId(onlineAccount.getId());
		if(commentService.add(comment)<=0) {
			ret.put("msg", "评论失败，请联系管理员");
			return ret;
		}
		Product product = productService.findById(comment.getProductId());
		product.setCommentNum(product.getCommentNum()+1);
		productService.updateNum(product);
		ret.put("type", "success");
		return ret;
	}
	
	@RequestMapping(value = "/delete",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> delete(Long id) {
		Map<String, String> ret=new HashMap<String, String>();
		ret.put("type", "error");
		if(id==null) {
			ret.put("msg", "请选择要删除的评论");
			return ret;
		}
		Comment comment = commentService.findById(id);
		if(comment==null) {
			ret.put("msg", "评论不存在");
			return ret;
		}
		if(commentService.delete(id)<=0) {
			ret.put("msg", "删除失败，请联系管理员");
			return ret;
		}
		Product product = productService.findById(comment.getProductId());
		product.setCommentNum(product.getCommentNum()-1);
		productService.updateNum(product);
		ret.put("type", "success");
		return ret;
	}
}
