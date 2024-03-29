package com.you.a.dao.common;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.you.a.entity.common.Product;

@Repository
public interface ProductDao {
	public int add(Product product);
	public int edit(Product product);
	public int delete(Long id);
	public List<Product> findList(Map<String, Object> queryMap);
	public Integer getTotal(Map<String, Object> queryMap);
	public Product findById(Long id);
	public int updateNum(Product product);
}
