package com.you.a.service.home;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.you.a.entity.home.Address;

@Service
public interface AddressService {
	public int add(Address address);
	public int edit(Address address);
	public int delete(Long id);
	public List<Address> findList(Map<String, Object> queryMap);
	public Integer getTotal(Map<String, Object> queryMap);
	public Address findById(Long id);
}
