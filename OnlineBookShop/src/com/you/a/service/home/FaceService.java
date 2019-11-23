package com.you.a.service.home;

import java.util.List;

import org.springframework.stereotype.Service;

import com.you.a.entity.home.Users;

@Service
public interface FaceService {
	public List<Users> selectAllUsers();
	
    public int save(Users user);
    
    public Users queryInfoByUsername(String username);
}
