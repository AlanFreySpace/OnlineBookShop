package com.you.a.service.common.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.you.a.dao.common.AccountDao;
import com.you.a.dao.common.ProductDao;
import com.you.a.entity.common.Account;
import com.you.a.entity.common.Product;
import com.you.a.service.common.AccountService;
import com.you.a.service.common.ProductService;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountDao accountDao;

	@Override
	public int add(Account account) {
		// TODO Auto-generated method stub
		return accountDao.add(account);
	}

	@Override
	public int edit(Account account) {
		// TODO Auto-generated method stub
		return accountDao.edit(account);
	}

	@Override
	public int delete(Long id) {
		// TODO Auto-generated method stub
		return accountDao.delete(id);
	}

	@Override
	public List<Account> findList(Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		return accountDao.findList(queryMap);
	}

	@Override
	public Integer getTotal(Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		return accountDao.getTotal(queryMap);
	}

	@Override
	public Account findById(Long id) {
		// TODO Auto-generated method stub
		return accountDao.findById(id);
	}

	@Override
	public Account findByName(String name) {
		// TODO Auto-generated method stub
		return accountDao.findByName(name);
	}
	
	
	
}
