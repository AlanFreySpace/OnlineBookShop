package com.you.a.controller.admin;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.you.a.entity.common.Comment;
import com.you.a.page.admin.Page;
import com.you.a.service.common.CommentService;


@RequestMapping("/admin/comment")
@Controller
public class CommentController {
	
	
	@Autowired
	private CommentService commentService;
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public ModelAndView list(ModelAndView model) {
		model.setViewName("comment/list");
		return model;
	}
	
	
	@RequestMapping(value="/list",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> list(@RequestParam(name="productName",required=false)String productName,
			@RequestParam(name="username",required=false)String username,
			@RequestParam(name="type",required=false)Integer type,
			Page page) {
		Map<String, Object> ret=new HashMap<String, Object>();
		Map<String, Object> queryMap=new HashMap<String, Object>();
		queryMap.put("productName", productName);
		queryMap.put("username", username);
		if(type!=null) {
			queryMap.put("type", type);
		}
		queryMap.put("offset", page.getOffset());
		queryMap.put("pageSize",page.getRows());
		ret.put("rows", commentService.findList(queryMap));
		ret.put("total", commentService.getTotal(queryMap));
		return ret;
	}
	
	
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> edit(Comment comment) {
		Map<String, Object> ret=new HashMap<String, Object>();
		if(comment==null) {
			ret.put("type", "error");
			ret.put("msg", "请填写正确的评论信息！");
			return ret;
		}
		if(StringUtils.isEmpty(comment.getContent())) {
			ret.put("type", "error");
			ret.put("msg", "请填写评论内容！");
			return ret;
		}
		if(commentService.edit(comment)<=0) {
			ret.put("type", "error");
			ret.put("msg", "编辑失败，请联系管理员！");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "编辑成功！");
		return ret;
	}
	
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> delete(Long id) {
		Map<String, Object> ret=new HashMap<String, Object>();
		if(id==null) {
			ret.put("type", "error");
			ret.put("msg", "请选择要删除的评论！");
			return ret;
		}
		if(commentService.delete(id)<=0) {
			ret.put("type", "error");
			ret.put("msg", "删除失败，请联系管理员！");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "删除成功！");
		return ret;
	}
	
	
}
