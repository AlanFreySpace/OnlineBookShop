package com.you.a.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.you.a.entity.common.Account;
import com.you.a.entity.common.Order;
import com.you.a.entity.common.OrderItem;
import com.you.a.entity.common.Product;
import com.you.a.page.admin.Page;
import com.you.a.service.common.AccountService;
import com.you.a.service.common.OrderService;
import com.you.a.service.common.ProductService;

import net.sf.json.JSONArray;


@RequestMapping("/admin/order")
@Controller
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired 
	private ProductService productService;
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public ModelAndView list(ModelAndView model) {
		model.setViewName("order/list");
		model.addObject("accountList",JSONArray.fromObject(accountService.findList(new HashMap<String, Object>())));
		return model;
	}
	
	@RequestMapping(value="/list",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> list(@RequestParam(name="sn",defaultValue = "")String sn,
			@RequestParam(name="username",required=false)String username,
			@RequestParam(name="moneyMin",required=false)Double moneyMin,
			@RequestParam(name="moneyMax",required=false)Double moneyMax,
			@RequestParam(name="status",required=false)Integer status,
			Page page) {
		Map<String, Object> ret=new HashMap<String, Object>();
		Map<String, Object> queryMap=new HashMap<String, Object>();
		queryMap.put("sn", sn);
		if(!StringUtils.isEmpty(username)) {
			Account account = accountService.findByName(username);
			if(account==null) {
				queryMap.put("userId", 0);
			}
			if(account!=null) {
				queryMap.put("userId", account.getId());
			}
		}
		if(moneyMin!=null) {
			queryMap.put("moneyMin", moneyMin);
		}
		if(moneyMax!=null) {
			queryMap.put("moneyMax", moneyMax);
		}
		if(status!=null) {
			queryMap.put("status", status);
		}
		queryMap.put("offset", page.getOffset());
		queryMap.put("pageSize",page.getRows());
		ret.put("rows", orderService.findList(queryMap));
		ret.put("total", orderService.getTotal(queryMap));
		return ret;
	}
	
	@RequestMapping(value="/get_item_list",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> itemList(Long orderId){
		Map<String, Object> ret=new HashMap<String, Object>();
		ret.put("rows", orderService.findOrderItemList(orderId));
		return ret;
	}
	
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> edit(Order order) {
		Map<String, Object> ret=new HashMap<String, Object>();
		if(order==null) {
			ret.put("type", "error");
			ret.put("msg", "请选择正确的订单信息！");
			return ret;
		}
		if(StringUtils.isEmpty(order.getAddress())) {
			ret.put("type", "error");
			ret.put("msg", "请填写订单收货地址！");
			return ret;
		}
		if(order.getMoney()==null) {
			ret.put("type", "error");
			ret.put("msg", "请填写订单金额！");
			return ret;
		}
		if(order.getStatus()==4) {
			List<OrderItem> orderItems = orderService.findOrderItemList(order.getId());
			for(OrderItem orderItem:orderItems) {
				Product product = productService.findById(orderItem.getProductId());
				int stockNum = orderItem.getNum()+product.getStock();
				int sellNum=product.getSellNum()-orderItem.getNum();
				product.setSellNum(sellNum);
				product.setStock(stockNum);
				productService.updateNum(product);
			}
		}
		if(orderService.edit(order)<=0) {
			ret.put("type", "error");
			ret.put("msg", "编辑失败，请联系管理员！");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "编辑成功！");
		return ret;
	}
	
}
