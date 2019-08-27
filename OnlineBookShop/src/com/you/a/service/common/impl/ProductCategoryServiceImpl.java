package com.you.a.service.common.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.you.a.dao.common.ProductCategoryDao;
import com.you.a.entity.common.ProductCategory;
import com.you.a.service.common.ProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
  
	@Autowired
	private ProductCategoryDao productCategoryDao;
	
	@Override
	public int add(ProductCategory productCategory) {
		// TODO Auto-generated method stub
		return productCategoryDao.add(productCategory);
	}

	@Override
	public int edit(ProductCategory productCategory) {
		// TODO Auto-generated method stub
		return productCategoryDao.edit(productCategory);
	}

	@Override
	public int delete(Long id) {
		// TODO Auto-generated method stub
		return productCategoryDao.delete(id);
	}

	@Override
	public List<ProductCategory> findList(Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		return productCategoryDao.findList(queryMap);
	}

	@Override
	public Integer getTotal(Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		return productCategoryDao.getTotal(queryMap);
	}

	@Override
	public ProductCategory findById(Long id) {
		// TODO Auto-generated method stub
		return productCategoryDao.findById(id);
	}

}
