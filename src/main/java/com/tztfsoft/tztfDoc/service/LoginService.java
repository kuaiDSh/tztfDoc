package com.tztfsoft.tztfDoc.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import javax.servlet.http.HttpSession;

import com.tztfsoft.tztfDoc.entity.JsonResult;
/**
 * 用户接口
 * @author kuaiDSH
 *
 */
public interface LoginService {
	/**
	 * 用户登录
	 * @param request
	 * @param response
	 * @return
	 */
	public JsonResult Login(HttpServletRequest request,
			HttpServletResponse response,HttpSession session);
	/**
	 * 获取用户名 id 姓名
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	public JsonResult getUser(HttpServletRequest request,
			HttpServletResponse response, HttpSession session);
	/**
	 * 新增用户
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	public JsonResult insertUser(HttpServletRequest request,
			HttpServletResponse response, HttpSession session);
	/**
	 * 查询所有用户数据
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	public JsonResult selectUser(HttpServletRequest request,
			HttpServletResponse response, HttpSession session);
	/**
	 * 修改用户
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	public JsonResult updateUser(HttpServletRequest request,
			HttpServletResponse response, HttpSession session);
	/**
	 * 删除用户
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	public JsonResult delteUser(HttpServletRequest request,
			HttpServletResponse response, HttpSession session);
	/**
	 * 更改密码
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	public JsonResult updatepassword(HttpServletRequest request,
			HttpServletResponse response, HttpSession session);
	
}




