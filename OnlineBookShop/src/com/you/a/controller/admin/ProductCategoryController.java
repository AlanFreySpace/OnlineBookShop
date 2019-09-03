package com.you.a.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.you.a.entity.common.ProductCategory;
import com.you.a.page.admin.Page;
import com.you.a.service.common.ProductCategoryService;


@RequestMapping("/admin/product_category")
@Controller
public class ProductCategoryController {
	
	
	@Autowired
	private ProductCategoryService productCategoryService;
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public ModelAndView list(ModelAndView model) {
		model.setViewName("product_category/list");
		return model;
	}
	
	@RequestMapping(value="/list",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> list(@RequestParam(name="name",defaultValue = "")String name,
			Page page) {
		Map<String, Object> ret=new HashMap<String, Object>();
		Map<String, Object> queryMap=new HashMap<String, Object>();
		queryMap.put("name", name);
		//queryMap.put("offset", page.getOffset());
		//queryMap.put("pageSize",page.getRows());
		ret.put("rows", productCategoryService.findList(queryMap));
		ret.put("total", productCategoryService.getTotal(queryMap));
		return ret;
	}
	
	@RequestMapping(value="/tree_list",method=RequestMethod.POST)
	@ResponseBody
	public List<Map<String, Object>> treeList() {
		Map<String, Object> queryMap=new HashMap<String, Object>();
		return getTreeCategory(productCategoryService.findList(queryMap));
	}
	
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> add(ProductCategory productCategory) {
		Map<String, Object> ret=new HashMap<String, Object>();
		if(productCategory==null) {
			ret.put("type", "error");
			ret.put("msg", "请填写正确的分类信息！");
			return ret;
		}
		if(StringUtils.isEmpty(productCategory.getName())) {
			ret.put("type", "error");
			ret.put("msg", "请填写分类名称！");
			return ret;
		}
		if(productCategory.getParentId()!=null) {
			ProductCategory productCategoryParent = productCategoryService.findById(productCategory.getParentId());
			if(productCategoryParent!=null) {
				String tags="";
				if(productCategoryParent.getTags()!=null) {
					tags+=productCategoryParent.getTags()+",";
				}
				productCategory.setTags(tags+productCategory.getParentId());
			}
			
		}
		if(productCategoryService.add(productCategory)<=0) {
			ret.put("type", "error");
			ret.put("msg", "添加失败，请联系管理员！");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "添加成功！");
		return ret;
	}
	
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> edit(ProductCategory productCategory) {
		Map<String, Object> ret=new HashMap<String, Object>();
		if(productCategory==null) {
			ret.put("type", "error");
			ret.put("msg", "请填写正确的分类信息！");
			return ret;
		}
		if(StringUtils.isEmpty(productCategory.getName())) {
			ret.put("type", "error");
			ret.put("msg", "请填写分类名称！");
			return ret;
		}
		if(productCategory.getParentId()!=null) {
			ProductCategory productCategoryParent = productCategoryService.findById(productCategory.getParentId());
			if(productCategoryParent!=null) {
				String tags="";
				if(productCategoryParent.getTags()!=null) {
					tags+=productCategoryParent.getTags()+",";
				}
				productCategory.setTags(tags+productCategory.getParentId());
			}
			
		}
		if(productCategoryService.edit(productCategory)<=0) {
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
			ret.put("msg", "请填写要删除的分类！");
			return ret;
		}
		try {
			if(productCategoryService.delete(id)<=0) {
				ret.put("type", "error");
				ret.put("msg", "删除失败，请联系管理员！");
				return ret;
			}
		} catch (Exception e) {
			// TODO: handle exception
			ret.put("type", "error");
			ret.put("msg", "该分类下存在商品信息，不允许删除！");
			return ret;
		}
		
		ret.put("type", "success");
		ret.put("msg", "删除成功！");
		return ret;
	}
	
	private List<Map<String, Object>> getTreeCategory(List<ProductCategory> productCategoryList){
		List<Map<String, Object>> ret=new ArrayList<Map<String,Object>>();
		for(ProductCategory productCategory:productCategoryList) {
			if(productCategory.getParentId()==null) {
				Map<String, Object> top=new HashMap<String, Object>();
				top.put("id", productCategory.getId());
				top.put("text", productCategory.getName());
				top.put("children", new ArrayList<Map<String, Object>>());
				ret.add(top);
			}
		}
		for(ProductCategory productCategory:productCategoryList) {
			if(productCategory.getParentId()!=null) {
				for(Map<String,Object> map:ret) {
					if(productCategory.getParentId().longValue()==Long.valueOf(map.get("id")+"")) {
						List children=(List)map.get("children");
						Map<String, Object> child=new HashMap<String, Object>();
						child.put("id", productCategory.getId());
						child.put("text", productCategory.getName());
						children.add(child);
					}
				}
			}
		}
		return ret;
	}
}
