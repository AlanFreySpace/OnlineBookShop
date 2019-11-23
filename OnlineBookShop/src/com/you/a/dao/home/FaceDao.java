package com.you.a.dao.home;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.you.a.entity.home.Users;

@Repository
public interface FaceDao {

	public List<Users> selectAllUsers();

	public int save(Users users);
	
	 public Users queryInfoByUsername(String username);
}
