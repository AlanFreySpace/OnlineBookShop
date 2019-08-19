package com.you.a.util;

import java.util.ArrayList;
import java.util.List;

import com.you.a.entity.admin.Menu;

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
}
