package com.you.a.service.home.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.you.a.dao.home.AddressDao;
import com.you.a.entity.home.Address;
import com.you.a.service.home.AddressService;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	private AddressDao addressDao;

	@Override
	public int add(Address address) {
		// TODO Auto-generated method stub
		return addressDao.add(address);
	}

	@Override
	public int edit(Address address) {
		// TODO Auto-generated method stub
		return addressDao.edit(address);
	}

	@Override
	public int delete(Long id) {
		// TODO Auto-generated method stub
		return addressDao.delete(id);
	}

	@Override
	public List<Address> findList(Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		return addressDao.findList(queryMap);
	}

	@Override
	public Integer getTotal(Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		return addressDao.getTotal(queryMap);
	}

	@Override
	public Address findById(Long id) {
		// TODO Auto-generated method stub
		return addressDao.findById(id);
	}

}
