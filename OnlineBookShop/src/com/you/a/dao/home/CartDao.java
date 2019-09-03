package com.you.a.dao.home;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.you.a.entity.home.Cart;

@Repository
public interface CartDao {
	public int add(Cart cart);
	public int edit(Cart cart);
	public int delete(Long id);
	public int deleteByUid(Long userId);
	public List<Cart> findList(Map<String, Object> queryMap);
	public Integer getTotal(Map<String, Object> queryMap);
	public Cart findById(Long id);
	public Cart findByIds(Map<String, Long> queryMap);
}
