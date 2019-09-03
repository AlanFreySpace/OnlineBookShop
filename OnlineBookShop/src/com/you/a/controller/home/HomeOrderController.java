package com.you.a.controller.home;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.you.a.entity.home.Address;
import com.you.a.entity.home.Cart;
import com.you.a.service.common.AccountService;
import com.you.a.service.common.OrderService;
import com.you.a.service.common.ProductCategoryService;
import com.you.a.service.common.ProductService;
import com.you.a.service.home.AddressService;
import com.you.a.service.home.CartService;
import com.you.a.util.MenuUtil;

@RequestMapping("/order")
@Controller
public class HomeOrderController {
	
	@Autowired 
	private AccountService accountService;
	
	@Autowired
	private ProductCategoryService productCategoryService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private AddressService addressService;

	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public ModelAndView list(ModelAndView model,Integer page,HttpServletRequest request) {
		model.addObject("productCategoryList",MenuUtil.getTreeCategory(productCategoryService.findList(new HashMap<String, Object>())));
		model.addObject("allCategoryId","shop_hd_menu_all_category");
		Account onlineAccount=(Account)request.getSession().getAttribute("account");
		Map<String, Object> queryMap=new HashMap<String, Object>();
		if(page==null||page.intValue()<=0) {
			page=1;
		}
		queryMap.put("offset", (page-1)*2);
		queryMap.put("pageSize", 2);
		queryMap.put("userId", onlineAccount.getId());
		queryMap.put("orderBy", "createTime");
		queryMap.put("sort", "desc");
		List<Order> orderList = orderService.findList(queryMap);
		for(Order order:orderList) {
			order.setOrderItems(orderService.findOrderItemList(order.getId()));
		}
		model.addObject("orderList",orderList);
		model.addObject("currentUser","current_");
		model.addObject("page",page);
		model.setViewName("home/order/list");
		return model;
	}
	
	

	@RequestMapping(value = "/add",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> add(Long addressId,Double totalMoney,
			@RequestParam(name="remark",required = false)String remark,
			HttpServletRequest request) {
		Map<String, String> ret=new HashMap<String, String>();
		Account onlineAccount=(Account)request.getSession().getAttribute("account");
		ret.put("type", "error");
		if(addressId==null) {
			ret.put("msg", "请选择收货地址");
			return ret;
		}
		Address address = addressService.findById(addressId);
		if(address==null) {
			ret.put("msg", "地址不存在");
			return ret;
		}
		
		
		Map<String, Object> queryMap=new HashMap<String, Object>();
		queryMap.put("userId", onlineAccount.getId());
		List<Cart> cartList = cartService.findList(queryMap);
		if(cartList==null) {
			ret.put("msg", "该用户购物车中没有商品");
			return ret;
		}
		Order order=new Order();
//		double totalMoney=0;
		int totalNum=0;
		for(Cart cart:cartList) {
			OrderItem orderItem=new OrderItem();
			orderItem.setImageUrl(cart.getImageUrl());
			orderItem.setMoney(cart.getMoney());
			orderItem.setName(cart.getName());
			orderItem.setNum(cart.getNum());
			orderItem.setPrice(cart.getPrice());
			orderItem.setProductId(cart.getProductId());
//			totalMoney+=cart.getMoney();
			totalNum+=cart.getNum();
			order.getOrderItems().add(orderItem);
		}
		order.setAddress(address.getAddress()+" "+address.getName()+"(收)"+address.getPhone());
		order.setMoney(totalMoney);
		order.setProductNum(totalNum);
		order.setSn("O"+System.currentTimeMillis());
		order.setStatus(Order.ORDER_STATUS_WAITING);
		order.setUserId(onlineAccount.getId());
		order.setRemark(remark);
		order.setCreateTime(new Date());
		if(orderService.add(order)<=0) {
			ret.put("msg", "添加失败，请联系管理员");
			return ret;
		}
		for(Cart cart:cartList) {
			Product product = productService.findById(cart.getProductId());
			product.setStock(product.getStock()-cart.getNum());
			product.setSellNum(product.getSellNum()+cart.getNum());
			productService.updateNum(product);
		}
		cartService.deleteByUid(onlineAccount.getId());
		ret.put("type", "success");
		ret.put("oid", order.getId()+"");
		return ret;
	}
	
	@RequestMapping(value = "/order_success",method = RequestMethod.GET)
	public ModelAndView orderSuccess(ModelAndView model,Long orderId,HttpServletRequest request) {
		model.addObject("productCategoryList",MenuUtil.getTreeCategory(productCategoryService.findList(new HashMap<String, Object>())));
		model.addObject("allCategoryId","shop_hd_menu_all_category");
		model.addObject("currentCart","_current");
		model.addObject("order",orderService.findById(orderId));
		model.setViewName("home/cart/order_success");
		return model;
	}
	
	@RequestMapping(value = "/comment",method = RequestMethod.GET)
	public ModelAndView comment(ModelAndView model,Long pid,HttpServletRequest request) {
		model.addObject("productCategoryList",MenuUtil.getTreeCategory(productCategoryService.findList(new HashMap<String, Object>())));
		model.addObject("allCategoryId","shop_hd_menu_all_category");
		model.addObject("currentUser","_current");
		model.addObject("product",productService.findById(pid));
		model.setViewName("home/order/comment");
		return model;
	}
	
	@RequestMapping(value = "/finish_order",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> finishOrder(Long id) {
		Map<String, String> ret=new HashMap<String, String>();
		ret.put("type", "error");
		if(id==null) {
			ret.put("msg", "请选择要收货的订单");
			return ret;
		}
		Order order = orderService.findById(id);
		if(order==null) {
			ret.put("msg", "订单不存在");
			return ret;
		}
		if(order.getStatus()!=Order.ORDER_STATUS_SENT) {
			ret.put("msg", "当前订单状态不可更改");
			return ret;
		}
		order.setStatus(Order.ORDER_STATUS_FINISH);
		if(orderService.edit(order)<=0) {
			ret.put("msg", "确认收货失败，请联系管理员");
			return ret;
		}
		ret.put("type", "success");
		return ret;
	}
	
	@RequestMapping(value = "/cancel_order",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> cancelOrder(Long id) {
		Map<String, String> ret=new HashMap<String, String>();
		ret.put("type", "error");
		if(id==null) {
			ret.put("msg", "请选择要退货的订单");
			return ret;
		}
		Order order = orderService.findById(id);
		if(order==null) {
			ret.put("msg", "订单不存在");
			return ret;
		}
		if(order.getStatus()==Order.ORDER_STATUS_SENT) {
			ret.put("msg", "当前订单状态不可更改");
			return ret;
		}
		order.setStatus(Order.ORDER_STATUS_CANCEL_WAITNG);
		if(orderService.edit(order)<=0) {
			ret.put("msg", "确认收货失败，请联系管理员");
			return ret;
		}
		ret.put("type", "success");
		return ret;
	}
}
