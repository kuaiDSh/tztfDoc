package com.tztfsoft.tztfDoc.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tztfsoft.tztfDoc.dao.UserDao;
import com.tztfsoft.tztfDoc.entity.JsonResult;
import com.tztfsoft.tztfDoc.entity.UserBean;
import com.tztfsoft.tztfDoc.service.LoginService;
import com.tztfsoft.tztfDoc.util.DataUtil;
import com.tztfsoft.tztfDoc.util.ImplGetJsonStr;

/**
 * 用户接口实现类
 * @author kuaiDSH
 *
 */
@Service("loginService")
public class LoginServiceImpl implements LoginService {
	@Resource
	UserDao userDao;
	/**
	 * 用户登录
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@Override
	public JsonResult Login(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		response.setContentType("text/html;charset=utf-8");
		String jsonstr = ImplGetJsonStr.getJsonResult(request);
		JsonResult jsonResult = new JsonResult();
		try {
			UserBean bean = new ObjectMapper().readValue(jsonstr,UserBean.class);
			List<Map<String,Object>> login = userDao.login(bean.getUsername(), bean.getPassword());
			if(login.size()>0) {
				jsonResult.setResultTypeID(0);
				jsonResult.setData(login);
			}else {
				jsonResult.setResultTypeID(-1);
				jsonResult.setHead("登录失败、账号密码不正确");
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonResult;
	}
	/**
	 * 获取用户名姓名
	 */
	@Override
	public JsonResult getUser(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		response.setContentType("text/html;charset=utf-8");
		List<Map<String,Object>> data = userDao.getUser();
		JsonResult jsonResult = new JsonResult();
		jsonResult.setData(data);
		jsonResult.setResultTypeID(0);
		return jsonResult;
	}
	/**
	 * 新增用户
	 */
	@Override
	public JsonResult insertUser(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		response.setContentType("text/html;charset=utf-8");
		String jsonstr = ImplGetJsonStr.getJsonResult(request);
		JsonResult jsonResult = new JsonResult();
		try {
			//得到数据
			UserBean bean = new ObjectMapper().readValue(jsonstr,UserBean.class);
			//判断用户名是否重复
			List<Map<String,Object>> list = userDao.getIDByUsername(bean.getUsername());
			System.out.println(list);
			if(list.size() == 0) {
				//设置初始密码123456
				bean.setPassword(DigestUtils.md5Hex("123456"));
				System.out.println(DigestUtils.md5Hex("123456"));
				//开始新增用户
				userDao.insertUser(bean);
				jsonResult.setData(bean);
				jsonResult.setResultTypeID(0);
			}else {
				jsonResult.setResultTypeID(-1);
				jsonResult.setHead("新增用户失败，用户名已存在");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonResult;
	}
	/**
	 * 查询所有用户数据
	 */
	@Override
	public JsonResult selectUser(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		response.setContentType("text/html;charset=utf-8");
		List<UserBean> list = userDao.getAllUser();
		JsonResult jsonResult = new JsonResult();
		jsonResult.setData(list);
		return jsonResult;
	}
	/**
	 * 修改用户
	 */
	@Override
	public JsonResult updateUser(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		response.setContentType("text/html;charset=utf-8");
		String jsonstr = ImplGetJsonStr.getJsonResult(request);
		JsonResult jsonResult = new JsonResult();
	
		//得到数据
		try {
			UserBean bean = new ObjectMapper().readValue(jsonstr,UserBean.class);
			userDao.updateUser(bean);
			jsonResult.setData(bean);
			jsonResult.setResultTypeID(0);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonResult;
	}
	
	@Override
	public JsonResult delteUser(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		JsonResult jsonResult = new JsonResult();
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=utf-8");
			String idr = request.getParameter("id");
			Integer id = Integer.parseInt(idr);
			userDao.deleteUser(id);
			jsonResult.setData(id);
			jsonResult.setResultTypeID(0);
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		}
		return jsonResult;
	}
	/**
	 * 更改密码
	 */
	@Override
	public JsonResult updatepassword(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		response.setContentType("text/html;charset=utf-8");
		String jsonstr = ImplGetJsonStr.getJsonResult(request);
		JsonResult jsonResult = new JsonResult();
		try {
			@SuppressWarnings("unchecked")
			Map<String,Object> reqm = new ObjectMapper().readValue(jsonstr,HashMap.class);
			//原密码
			String pw = (String) reqm.get("old");
			//用户id
			Integer userid = (Integer) reqm.get("id");
			//新密码
			String newpw = (String) reqm.get("new");
			Map<String,Object> map = userDao.getByPWAndID(userid, pw);
			if(map != null) {
				userDao.updatepassword(userid, newpw);
				jsonResult.setResultTypeID(0);
			}else {
				jsonResult.setResultTypeID(-1);
				jsonResult.setHead("原密码错误");
			}
		} catch (JsonParseException e) {
			jsonResult.setResultTypeID(-1);
			jsonResult.setHead("json错误");
			e.printStackTrace();
		} catch (JsonMappingException e) {
			jsonResult.setResultTypeID(-1);
			jsonResult.setHead("json错误");
			e.printStackTrace();
		} catch (IOException e) {
			jsonResult.setResultTypeID(-1);
			jsonResult.setHead("io错误");
			e.printStackTrace();
		}
		return jsonResult;
	}
}





