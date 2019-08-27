package com.you.a.service.home;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.you.a.entity.home.Cart;

@Service
public interface CartService {
	public int add(Cart cart);
	public int edit(Cart cart);
	public int delete(Long id);
	public int deleteByUid(Long userId);
	public List<Cart> findList(Map<String, Object> queryMap);
	public Integer getTotal(Map<String, Object> queryMap);
	public Cart findById(Long id);
	public Cart findByIds(Map<String, Long> queryMap);
}
