package com.tztfsoft.tztfDoc.controller;



import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sun.star.lib.uno.protocols.urp.urp;
import com.tztfsoft.tztfDoc.dao.UserDao;
import com.tztfsoft.tztfDoc.entity.JsonResult;
import com.tztfsoft.tztfDoc.service.LoginService;

/**
 * 用户登录控制器
 * @author kuaiDSH
 *
 */
@Controller
@RequestMapping("/u")
public class LoginController {
	@Resource
	LoginService loginService;
	@Resource
	UserDao userDao;
	/**
	 * 登录
	 * @return
	 */
	@RequestMapping("/login.do")
	@ResponseBody
	public JsonResult login(HttpServletRequest request, 
			HttpServletResponse response,HttpSession session) {
		return loginService.Login(request, response, session);
		
	}
	
	/**
	 * 获取用户名 id 姓名
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public JsonResult getUser(HttpServletRequest request, 
			HttpServletResponse response,HttpSession session) {
		return loginService.getUser(request, response, session);
		
	}
	
	/**
	 * 新增用户
	 * @return
	 */
	@RequestMapping("/in.do")
	@ResponseBody
	public JsonResult insertUser(HttpServletRequest request, 
			HttpServletResponse response,HttpSession session) {
		return loginService.insertUser(request, response, session);
		
	}
	/**
	 * 获取用户
	 * @return
	 */
	@RequestMapping("/sel.do")
	@ResponseBody
	public JsonResult selectUser(HttpServletRequest request, 
			HttpServletResponse response,HttpSession session) {
		return loginService.selectUser(request, response, session);
		
	}
	/**
	 * 修改用户
	 * @return
	 */
	@RequestMapping("/update.do")
	@ResponseBody
	public JsonResult updateUser(HttpServletRequest request, 
			HttpServletResponse response,HttpSession session) {
		return loginService.updateUser(request, response, session);
		
	}
	/**
	 * 删除用户
	 * @return
	 */
	@RequestMapping("/del.do")
	@ResponseBody
	public JsonResult delteUser(HttpServletRequest request, 
			HttpServletResponse response,HttpSession session) {
		return loginService.delteUser(request, response, session);
		
	}
	
	/**
	 * 更改密码
	 * @return
	 */
	@RequestMapping("/uppw.do")
	@ResponseBody
	public JsonResult updatepassword(HttpServletRequest request, 
			HttpServletResponse response,HttpSession session) {
		return loginService.updatepassword(request, response, session);
		
	}
	/**
	 * 根据用户id 获取用户账号 和用户姓名
	 * @return
	 */
	@RequestMapping("/getu.do")
	@ResponseBody
	public JsonResult getuser(@RequestParam(value="id")Integer userid,HttpServletRequest request, 
			HttpServletResponse response,HttpSession session) {
		response.setContentType("text/html;charset=utf-8");
		JsonResult jsonResult = new JsonResult();
		Map<String,Object> map = userDao.getuserByID(userid);
		jsonResult.setResultTypeID(0);
		jsonResult.setData(map);
		return jsonResult;
		
	}
}









