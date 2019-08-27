package com.you.a.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.you.a.entity.admin.Menu;
import com.you.a.entity.common.ProductCategory;

public class MenuUtil {
	public static List<Menu> getAllTopMenu(List<Menu> menuList){
		List<Menu> ret=new ArrayList<Menu>();
		for(Menu menu:menuList) {
			if(menu.getParentId()==0) {
				ret.add(menu);
			}
		}
		return ret;
	}
	
	public static List<Menu> getAllSecondMenu(List<Menu> menuList){
		List<Menu> ret=new ArrayList<Menu>();
		List<Menu> allTopMenu = getAllTopMenu(menuList);
		for(Menu menu:menuList) {
			for(Menu topMenu:allTopMenu) {
				if(menu.getParentId()==topMenu.getId()) {
					ret.add(menu);
					break;
				}
			}
		}
		return ret;
	}
	
	public static List<Menu> getAllThirdMenu(List<Menu> menuList,Long secondMenuId){
		List<Menu> ret=new ArrayList<Menu>();
		for(Menu menu:menuList) {
			if(menu.getParentId()==secondMenuId) {
				ret.add(menu);
			}
		}
		return ret;
	}
	
	public static List<Map<String, Object>> getTreeCategory(List<ProductCategory> productCategoryList){
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
						child.put("children", new ArrayList<Map<String, Object>>());
						children.add(child);
					}
				}
			}
		}
		for(ProductCategory productCategory:productCategoryList) {
			if(productCategory.getParentId()!=null) {
				for(Map<String,Object> map:ret) {
					List<Map<String,Object>> children=(List<Map<String,Object>>)map.get("children");
					for(Map<String,Object> child:children) {
						if(productCategory.getParentId().longValue()==Long.valueOf(child.get("id")+"")) {
							List grandsons=(List)child.get("children");
							Map<String, Object> grandson=new HashMap<String, Object>();
							grandson.put("id", productCategory.getId());
							grandson.put("text", productCategory.getName());
							grandsons.add(grandson);
						}
					}

				}
			}
		}
		return ret;
	}
}
