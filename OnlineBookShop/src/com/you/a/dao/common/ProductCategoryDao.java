package com.you.a.dao.common;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.you.a.entity.common.ProductCategory;

@Repository
public interface ProductCategoryDao {
	public int add(ProductCategory productCategory);
	public int edit(ProductCategory productCategory);
	public int delete(Long id);
	public List<ProductCategory> findList(Map<String, Object> queryMap);
	public Integer getTotal(Map<String, Object> queryMap);
	public ProductCategory findById(Long id);
}
