package com.you.a.service.common;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.you.a.entity.common.Product;

@Service
public interface ProductService {
	public int add(Product product);
	public int edit(Product product);
	public int delete(Long id);
	public List<Product> findList(Map<String, Object> queryMap);
	public Integer getTotal(Map<String, Object> queryMap);
	public Product findById(Long id);
}
