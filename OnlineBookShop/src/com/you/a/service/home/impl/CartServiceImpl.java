package com.you.a.service.home.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.you.a.dao.home.CartDao;
import com.you.a.entity.home.Cart;
import com.you.a.service.home.CartService;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private CartDao cartDao;

	@Override
	public int add(Cart cart) {
		// TODO Auto-generated method stub
		return cartDao.add(cart);
	}

	@Override
	public int edit(Cart cart) {
		// TODO Auto-generated method stub
		return cartDao.edit(cart);
	}

	@Override
	public int delete(Long id) {
		// TODO Auto-generated method stub
		return cartDao.delete(id);
	}

	@Override
	public List<Cart> findList(Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		return cartDao.findList(queryMap);
	}

	@Override
	public Integer getTotal(Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		return cartDao.getTotal(queryMap);
	}

	@Override
	public Cart findById(Long id) {
		// TODO Auto-generated method stub
		return cartDao.findById(id);
	}

	@Override
	public Cart findByIds(Map<String, Long> queryMap) {
		// TODO Auto-generated method stub
		return cartDao.findByIds(queryMap);
	}

	@Override
	public int deleteByUid(Long userId) {
		// TODO Auto-generated method stub
		return cartDao.deleteByUid(userId);
	}
	
}
