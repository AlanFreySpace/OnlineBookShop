package com.you.a.service.home;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.you.a.entity.home.Favorite;

@Service
public interface FavoriteService {
	public int add(Favorite favorite);
	public int delete(Long id);
	public List<Favorite> findList(Map<String, Object> queryMap);
	public Integer getTotal(Map<String, Object> queryMap);
	public Favorite findById(Long id);
	public Favorite findByIds(Map<String, Long> queryMap);
}
