package com.you.a.service.common;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.you.a.entity.common.Order;
import com.you.a.entity.common.OrderItem;

@Service
public interface OrderService {
	public int add(Order order);
	public int edit(Order order);
	public int delete(Long id);
	public List<Order> findList(Map<String, Object> queryMap);
	public Integer getTotal(Map<String, Object> queryMap);
	public Order findById(Long id);
	public List<OrderItem> findOrderItemList(Long orderId);
}
