package com.you.a.dao.common;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.you.a.entity.common.Comment;

@Repository
public interface CommentDao {
	public int add(Comment comment);
	public int edit(Comment comment);
	public int delete(Long id);
	public List<Comment> findList(Map<String, Object> queryMap);
	public Integer getTotal(Map<String, Object> queryMap);
	public Comment findById(Long id);
}
