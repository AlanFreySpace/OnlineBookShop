package com.you.a.service.home.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.you.a.dao.home.FaceDao;
import com.you.a.entity.home.Users;
import com.you.a.service.home.FaceService;

@Service
public class FaceServiceImpl implements FaceService {

	@Autowired
	private FaceDao facedao;


	public List<Users> selectAllUsers() {
		return facedao.selectAllUsers();
	}


	@Override
	public int save(Users user) {
		 return facedao.save(user);
	}


	@Override
	public Users queryInfoByUsername(String username) {
		// TODO Auto-generated method stub
		 return facedao.queryInfoByUsername(username);
	}

}
