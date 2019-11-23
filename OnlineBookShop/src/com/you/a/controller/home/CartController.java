package com.you.a.controller.home;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Base64.Decoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.baidu.aip.face.AipFace;
import com.baidu.aip.util.Base64Util;
import com.fasterxml.jackson.databind.deser.DataFormatReaders.Match;
import com.you.a.entity.common.Account;
import com.you.a.entity.common.Product;
import com.you.a.entity.home.Cart;
import com.you.a.entity.home.Users;
import com.you.a.service.common.AccountService;
import com.you.a.service.common.ProductCategoryService;
import com.you.a.service.common.ProductService;
import com.you.a.service.home.AddressService;
import com.you.a.service.home.CartService;
import com.you.a.service.home.FaceService;
import com.you.a.util.FileUtil;
import com.you.a.util.GetTon;
import com.you.a.util.GsonUtils;
import com.you.a.util.HttpUtil;
import com.you.a.util.MenuUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import sun.misc.BASE64Encoder;

@RequestMapping("/cart")
@Controller
public class CartController {
	
	@Autowired 
	private AccountService accountService;
	
	@Autowired
	private ProductCategoryService productCategoryService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private AddressService addressService;

	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public ModelAndView list(ModelAndView model,HttpServletRequest request) {
		model.addObject("productCategoryList",MenuUtil.getTreeCategory(productCategoryService.findList(new HashMap<String, Object>())));
		model.addObject("allCategoryId","shop_hd_menu_all_category");
		Account onlineAccount=(Account)request.getSession().getAttribute("account");
		Map<String, Object> queryMap=new HashMap<String, Object>();
		queryMap.put("userId", onlineAccount.getId());
		List<Cart> cartList = cartService.findList(queryMap);
		model.addObject("cartList",cartList);
		double total=0;
		for(Cart cart:cartList) {
			Product product = productService.findById(cart.getProductId());
			if(product.getProductCategoryId()==123) {
				total+=cart.getMoney();
			}
		}
		if(total>=99) {
			model.addObject("zhekou",70);
		}
		model.addObject("currentCart","current_");
		model.setViewName("home/cart/list");
		return model;
	}
	
	
	@RequestMapping(value = "/list_2",method = RequestMethod.GET)
	public ModelAndView list2(ModelAndView model,HttpServletRequest request) {
		model.addObject("productCategoryList",MenuUtil.getTreeCategory(productCategoryService.findList(new HashMap<String, Object>())));
		model.addObject("allCategoryId","shop_hd_menu_all_category");
		Account onlineAccount=(Account)request.getSession().getAttribute("account");
		Map<String, Object> queryMap=new HashMap<String, Object>();
		queryMap.put("userId", onlineAccount.getId());
		List<Cart> cartList = cartService.findList(queryMap);
		model.addObject("cartList",cartList);
		double total=0;
		for(Cart cart:cartList) {
			Product product = productService.findById(cart.getProductId());
			if(product.getProductCategoryId()==123) {
				total+=cart.getMoney();
			}
		}
		if(total>=99) {
			model.addObject("zhekou",70);
		}
		
		model.addObject("currentCart","current_");
		model.addObject("addressList",addressService.findList(queryMap));
		model.setViewName("home/cart/list_2");
		return model;
	}
	

	@RequestMapping(value = "/add",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> add(Cart cart,HttpServletRequest request) {
		Map<String, String> ret=new HashMap<String, String>();
		Account onlineAccount=(Account)request.getSession().getAttribute("account");
		ret.put("type", "error");
		if(cart==null) {
			ret.put("msg", "请选择正确的商品信息");
			return ret;
		}
		if(cart.getProductId()==null) {
			ret.put("msg", "请选择要添加的商品");
			return ret;
		}
		if(cart.getNum()==0) {
			ret.put("msg", "请填写商品数量");
			return ret;
		}
		Product product = productService.findById(cart.getProductId());
		if(product==null) {
			ret.put("msg", "商品不存在");
			return ret;
		}
		
		Map<String, Long> queryMap=new HashMap<String, Long>();
		queryMap.put("userId", onlineAccount.getId());
		queryMap.put("productId", product.getId());
		Cart existCart = cartService.findByIds(queryMap);
		if(existCart!=null) {
			existCart.setNum(existCart.getNum()+cart.getNum());
			existCart.setMoney(existCart.getNum()*existCart.getPrice());
			if(cartService.edit(existCart)<=0) {
				ret.put("msg", "商品已经添加至购物车，但更新数量出错");
				return ret;
			}
			ret.put("type", "success");
			return ret;
		}
		
		cart.setImageUrl(product.getImageUrl());
		cart.setMoney(product.getPrice()*cart.getNum());
		cart.setName(product.getName());
		cart.setPrice(product.getPrice());
		cart.setUserId(onlineAccount.getId());
		cart.setCreateTime(new Date());
		if(cartService.add(cart)<=0) {
			ret.put("msg", "添加失败，请联系管理员");
			return ret;
		}
		ret.put("type", "success");
		return ret;
	}
	
	@RequestMapping(value = "/update_num",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> updateNum(Long cartId,Integer num) {
		Map<String, String> ret=new HashMap<String, String>();
		ret.put("type", "error");
		Cart cart = cartService.findById(cartId);
		if(cart==null) {
			ret.put("msg", "请选择正确的商品信息");
			return ret;
		}
		if(num==null) {
			ret.put("msg", "请填写商品数量");
			return ret;
		}
		Product product = productService.findById(cart.getProductId());
		if(product==null) {
			ret.put("msg", "购物车信息有误");
			return ret;
		}
		if(cart.getNum()+num.intValue()>product.getStock()) {
			ret.put("msg", "商品数量不能超过库存量");
			return ret;
		}
		
		cart.setNum(cart.getNum()+num);
		cart.setMoney(cart.getNum()*cart.getPrice());
		if(cartService.edit(cart)<=0) {
			ret.put("msg", "商品已经添加至购物车，但更新数量出错");
			return ret;
		}
		ret.put("type", "success");
		return ret;
	}
	
	@RequestMapping(value = "/delete",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> delete(Long cartId) {
		Map<String, String> ret=new HashMap<String, String>();
		ret.put("type", "error");
		if(cartId==null) {
			ret.put("msg", "请选择要删除的商品");
			return ret;
		}
		
		if(cartService.delete(cartId)<=0) {
			ret.put("msg", "删除失败，请联系管理员");
			return ret;
		}
		ret.put("type", "success");
		return ret;
	}
	
	//添加人脸识别的功能
	private static String accessToken;

	@Autowired
	private FaceService faceService;

	/**
	 * 用户人脸识别头像录入(每个用户建议只允许录入一次)
	 * @param request
	 * @param response
	 * @param model
	 */
	
	@RequestMapping(value = "/face_login",method = RequestMethod.GET)
	public ModelAndView faceregister(ModelAndView model) {
		model.addObject("title","人脸验证");
		model.setViewName("home/cart/index");
		return model;
	}
	
	@RequestMapping("/registe.action")
	public @ResponseBody void registe(HttpServletRequest request, HttpServletResponse response, Model model) {
		String img = request.getParameter("img"); // 图像数据
		String username = request.getParameter("username"); // 用户名
		// 注册百度云 人脸识别 提供的信息
		String APP_ID = "17791478";
		String API_KEY = "Xjojig0oGd49QBwlmqiq8zAM";
		String SECRET_KEY = "SPWtWXTaq6nqTgj1Y022BConsWwVHz8v";
		AipFace client = new AipFace(APP_ID, API_KEY, SECRET_KEY);
		face(username, img, response, request, client);
	}

	/**
	 *作用：	  1.将base64编码的图片保存 
	 *		  2.将图片路径保存在数据库里面
	 *        3.将人脸图片识别在人脸库中注册 
	 */
	public void face(String username, String img, HttpServletResponse response, HttpServletRequest request,
			AipFace client) {
		Users user = new Users();
		// 图片名称
		Object registeName = request.getSession().getAttribute("registeUser");
		String fileName = registeName + ".png";
		String basePath = request.getSession().getServletContext().getRealPath("picture/");
		File file = new File(basePath);
		if(!file.exists()){     //判断文件路径是否存在
		      file.mkdirs();              //不存在创建新的文件
		}
		// 往数据库里面插入注册信息
		user.setUsername(username);
		user.setHeadphoto("/picture/" + fileName);
		faceService.save(user);
		// 往服务器里面上传图片
		GenerateImage(img, basePath + "/" + fileName);
		// 给人脸库中加入一个脸
		boolean flag = facesetAddUser(client, fileName, username,img);
		try {
			PrintWriter out = response.getWriter();
			// 中文乱码，写个英文比较专业 哈哈
			if (flag == false) {
				out.print("Please aim at the camera!!");// 请把脸放好咯
			} else {
				out.print("Record the success of the image!!"); // 注册成功
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean GenerateImage(String imgStr, String imgFilePath) {
		if (imgStr == null) // 图像数据为空
			return false;
		Decoder decoder = Base64.getDecoder();
		try {
			// Base64解码
			byte[] bytes = decoder.decode(imgStr);
			for (int i = 0; i < bytes.length; ++i) {
				if (bytes[i] < 0) {// 调整异常数据
					bytes[i] += 256;
				}
			}
			// 生成jpeg图片
			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(bytes);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	

	public boolean facesetAddUser(AipFace client, String path, String username, String img) {
		
		 String url = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/add";
	        try {
	            Map<String, Object> map = new HashMap<>();
	            map.put("image", img);
	            map.put("group_id", "group_repeat");
	            map.put("user_id", "user1");
	            map.put("user_info", "abc");
	            map.put("liveness_control", "NORMAL");
	            map.put("image_type","BASE64");
	            map.put("quality_control", "LOW");

	            String param = GsonUtils.toJson(map);
                System.out.println(param);
	            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
	            accessToken = GetTon.getToken();

	            String result = HttpUtil.post(url, accessToken, "application/json", param);
	            System.out.println(result);
	            
	            JSONObject fromObject = JSONObject.fromObject(result);

	            Object errormsg = fromObject.get("error_msg");
					if (errormsg.toString().equals("SUCCESS")) {
						return true;
					}
	            return false; 
	        }catch (Exception e) {
	            e.printStackTrace();
	        }
	        return false;
	}

	 public boolean search(String img) {
	        // 请求url
	        String url = "https://aip.baidubce.com/rest/2.0/face/v3/search";
	        try {
	            Map<String, Object> map = new HashMap<>();
	            map.put("image", img);
	            map.put("liveness_control", "NORMAL");
	            map.put("group_id_list", "group_repeat");
	            map.put("image_type", "BASE64");
	            map.put("quality_control", "LOW");

	            String param = GsonUtils.toJson(map);

	            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
	            accessToken = GetTon.getToken();

	            String result = HttpUtil.post(url, accessToken, "application/json", param);
	            System.out.println(result);
	            
	            JSONObject fromObject = JSONObject.fromObject(result);
	            JSONObject resultscore = fromObject.getJSONObject("result");
	            JSONArray jsonArray = resultscore.getJSONArray("user_list");

				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject object = (JSONObject) jsonArray.get(i);
					double resultList = object.getDouble("score");
					System.out.println(resultList);
					if (resultList >= 90) {
						return true;
					}
				}
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return false;
	    }
	 
	 public boolean match(String img1,String img2) {
		 // 请求url
	        String url = "https://aip.baidubce.com/rest/2.0/face/v3/match";
	        try {
	        	List<Map<String, Object>> imagesList=new ArrayList<>();
	            Map<String, Object> map1 = new HashMap<>();
	            map1.put("image", img1);
	            map1.put("image_type", "BASE64");
	            map1.put("face_type", "LIVE");
	            map1.put("quality_control", "LOW");
	            map1.put("liveness_control", "NORMAL");
	            
	            Map<String, Object> map2 = new HashMap<>();
	            map2.put("image", img2);
	            map2.put("image_type", "BASE64");
	            map2.put("face_type", "LIVE");
	            map2.put("quality_control", "LOW");
	            map2.put("liveness_control", "NORMAL");
	            
	            imagesList.add(map1);
	            imagesList.add(map2);

	            String param = GsonUtils.toJson(imagesList);

	            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
	            accessToken = GetTon.getToken();

	            String result = HttpUtil.post(url, accessToken, "application/json", param);
	            System.out.println(result);
	            
	            JSONObject fromObject = JSONObject.fromObject(result);
	            JSONObject resultscore = fromObject.getJSONObject("result");
	            String error_msg=fromObject.getString("error_msg");
	            if(!error_msg.equals("SUCCESS")) {
	            	return false;
	            }
	            double finalScore=resultscore.getDouble("score");
	            //JSONArray jsonArray = resultscore.getJSONArray("user_list");
	            if(finalScore>=90) {
	            	return true;
	            }
	            /*
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject object = (JSONObject) jsonArray.get(i);
					double resultList = object.getDouble("score");
					System.out.println(resultList);
					if (resultList >= 90) {
						return true;
					}
				}
				*/
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return false;
	 }
	
	
	 /**
	  * 用户人脸识别登录功能
	  * @param request
	  * @param response
	  * @param model
	  * @return
	  */
	@RequestMapping("/facelogin.action")
	public @ResponseBody String onListStudent(HttpServletRequest request, HttpServletResponse response, Model model) {
		String img = request.getParameter("img"); // 图像数据
		
		Account account = (Account)request.getSession().getAttribute("account");
		String username= account.getName();
		String basePath = request.getSession().getServletContext().getRealPath("picture/");
		String fileName = basePath+"/"+username + ".png";
		try {
			byte[] bytes1=FileUtil.readFileByBytes(fileName);
			String img1=Base64Util.encode(bytes1);
			boolean tag = match(img, img1);
			
			Map<String, String> ret=new HashMap<String, String>();
			
			
			if(tag==true) {
				ret.put("type", "success");
			}else {
				ret.put("type", "error");
			}
			
			PrintWriter writer = response.getWriter();
			if (tag) {
				request.getSession().setAttribute("user", "likang");
				writer.print(tag);
				writer.close();
				return null;
			}else {
				writer.print(tag);
				writer.close();
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "redirect:/404.jsp";
		}
		return null;
	}

	
	public boolean getResult(HttpServletRequest request, String imStr1, String imgStr2) {

		accessToken = GetTon.getToken();
		boolean flag = false;
		// 请求url
		String url = "https://aip.baidubce.com/rest/2.0/face/v3/match";
		try {
			 byte[] bytes1 = FileUtil.readFileByBytes("");
			 byte[] bytes2 = FileUtil.readFileByBytes("");
			 String image1 = Base64Util.encode(bytes1);
			 String image2 = Base64Util.encode(bytes2);

			List<Map<String, Object>> images = new ArrayList<>();

			Map<String, Object> map1 = new HashMap<>();
			map1.put("image", image1);
			map1.put("image_type", "BASE64");
			map1.put("face_type", "LIVE");
			map1.put("quality_control", "LOW");
			map1.put("liveness_control", "NORMAL");

			Map<String, Object> map2 = new HashMap<>();
			map2.put("image", image2);
			map2.put("image_type", "BASE64");
			map2.put("face_type", "LIVE");
			map2.put("quality_control", "LOW");
			map2.put("liveness_control", "NORMAL");

			images.add(map1);
			images.add(map2);

			String param = GsonUtils.toJson(images);

			// 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间，
			// 客户端可自行缓存，过期后重新获取。

			String result = HttpUtil.post(url, accessToken, "application/json", param);
			System.out.println(result);
			// return result;
			JSONObject fromObject = JSONObject.fromObject(result);

			JSONArray jsonArray = fromObject.getJSONArray("result");

			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject object = (JSONObject) jsonArray.get(i);
				double resultList = object.getDouble("score");
				if (resultList >= 90) {
					flag = true;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
}
