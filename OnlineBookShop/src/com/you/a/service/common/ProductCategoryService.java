package com.you.a.service.common;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.you.a.entity.common.ProductCategory;

@Service
public interface ProductCategoryService {
	public int add(ProductCategory productCategory);
	public int edit(ProductCategory productCategory);
	public int delete(Long id);
	public List<ProductCategory> findList(Map<String, Object> queryMap);
	public Integer getTotal(Map<String, Object> queryMap);
	public ProductCategory findById(Long id);
}
