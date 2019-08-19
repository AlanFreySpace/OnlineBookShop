package com.you.a.controller.admin;

import java.util.Date;
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

import com.you.a.entity.common.Product;
import com.you.a.entity.common.ProductCategory;
import com.you.a.page.admin.Page;
import com.you.a.service.common.ProductCategoryService;
import com.you.a.service.common.ProductService;


@RequestMapping("/admin/product")
@Controller
public class ProductController {
	
	
	@Autowired
	private ProductCategoryService productCategoryService;
	
	@Autowired
	private ProductService productService;
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public ModelAndView list(ModelAndView model) {
		model.setViewName("product/list");
		return model;
	}
	
	
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public ModelAndView add(ModelAndView model) {
		model.setViewName("product/add");
		return model;
	}
	
	
	@RequestMapping(value="/list",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> list(@RequestParam(name="name",defaultValue = "")String name,
			@RequestParam(name="tags",required=false)String tags,
			@RequestParam(name="priceMin",required=false)Double priceMin,
			@RequestParam(name="priceMax",required=false)Double priceMax,
			Page page) {
		Map<String, Object> ret=new HashMap<String, Object>();
		Map<String, Object> queryMap=new HashMap<String, Object>();
		queryMap.put("name", name);
		if(!StringUtils.isEmpty(tags)) {
			queryMap.put("tags", tags);
		}
		if(priceMin!=null) {
			queryMap.put("priceMin", priceMin);
		}
		if(priceMax!=null) {
			queryMap.put("priceMax", priceMax);
		}
		queryMap.put("offset", page.getOffset());
		queryMap.put("pageSize",page.getRows());
		ret.put("rows", productService.findList(queryMap));
		ret.put("total", productService.getTotal(queryMap));
		return ret;
	}
	
	
	
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> add(Product product) {
		Map<String, Object> ret=new HashMap<String, Object>();
		if(product==null) {
			ret.put("type", "error");
			ret.put("msg", "����д��ȷ����Ʒ��Ϣ��");
			return ret;
		}
		if(StringUtils.isEmpty(product.getName())) {
			ret.put("type", "error");
			ret.put("msg", "����д������");
			return ret;
		}
		if(product.getProductCategoryId()==null) {
			ret.put("type", "error");
			ret.put("msg", "����д��ķ��࣡");
			return ret;
		}
		if(product.getPrice()==null) {
			ret.put("type", "error");
			ret.put("msg", "����д�۸�");
			return ret;
		}
		if(StringUtils.isEmpty(product.getImageUrl())) {
			ret.put("type", "error");
			ret.put("msg", "���ϴ���ƷͼƬ��");
			return ret;
		}
		ProductCategory productCategory = productCategoryService.findById(product.getProductCategoryId());
		product.setTags(productCategory.getTags()+","+productCategory.getId());
		product.setCreateTime(new Date());
		if(productService.add(product)<=0) {
			ret.put("type", "error");
			ret.put("msg", "���ʧ�ܣ�����ϵ����Ա��");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "��ӳɹ���");
		return ret;
	}
	
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> edit(Product product) {
		Map<String, Object> ret=new HashMap<String, Object>();
		if(product==null) {
			ret.put("type", "error");
			ret.put("msg", "����д��ȷ����Ʒ��Ϣ��");
			return ret;
		}
		if(StringUtils.isEmpty(product.getName())) {
			ret.put("type", "error");
			ret.put("msg", "����д������");
			return ret;
		}
		if(product.getProductCategoryId()==null) {
			ret.put("type", "error");
			ret.put("msg", "����д��ķ��࣡");
			return ret;
		}
		if(product.getPrice()==null) {
			ret.put("type", "error");
			ret.put("msg", "����д�۸�");
			return ret;
		}
		if(StringUtils.isEmpty(product.getImageUrl())) {
			ret.put("type", "error");
			ret.put("msg", "���ϴ���ƷͼƬ��");
			return ret;
		}
		ProductCategory productCategory = productCategoryService.findById(product.getProductCategoryId());
		product.setTags(productCategory.getTags()+","+productCategory.getId());
		product.setCreateTime(new Date());
		if(productService.edit(product)<=0) {
			ret.put("type", "error");
			ret.put("msg", "�༭ʧ�ܣ�����ϵ����Ա��");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "�༭�ɹ���");
		return ret;
	}
	
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> delete(Long id) {
		Map<String, Object> ret=new HashMap<String, Object>();
		if(id==null) {
			ret.put("type", "error");
			ret.put("msg", "����дҪɾ����ͼ�飡");
			return ret;
		}
		try {
			if(productService.delete(id)<=0) {
				ret.put("type", "error");
				ret.put("msg", "ɾ��ʧ�ܣ�����ϵ����Ա��");
				return ret;
			}
		} catch (Exception e) {
			// TODO: handle exception
			ret.put("type", "error");
			ret.put("msg", "�÷����´�����Ʒ��Ϣ��������ɾ����");
			return ret;
		}
		
		ret.put("type", "success");
		ret.put("msg", "ɾ���ɹ���");
		return ret;
	}
	
	
}
