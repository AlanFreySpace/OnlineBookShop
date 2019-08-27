package com.you.a.dao.common;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.you.a.entity.common.Account;

@Repository
public interface AccountDao {
	public int add(Account account);
	public int edit(Account account);
	public int delete(Long id);
	public List<Account> findList(Map<String, Object> queryMap);
	public Integer getTotal(Map<String, Object> queryMap);
	public Account findById(Long id);
	public Account findByName(String name);
}
