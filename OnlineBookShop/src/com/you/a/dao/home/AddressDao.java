package com.you.a.dao.home;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.you.a.entity.home.Address;
import com.you.a.entity.home.Cart;

@Repository
public interface AddressDao {
	public int add(Address address);
	public int edit(Address address);
	public int delete(Long id);
	public List<Address> findList(Map<String, Object> queryMap);
	public Integer getTotal(Map<String, Object> queryMap);
	public Address findById(Long id);
}
