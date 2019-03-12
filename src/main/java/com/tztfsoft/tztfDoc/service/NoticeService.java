package com.tztfsoft.tztfDoc.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import javax.servlet.http.HttpSession;

import com.tztfsoft.tztfDoc.entity.JsonResult;
/**
 * 通知service
 * @author kuaiDSH
 *
 */
public interface NoticeService {
	/**
	 * 获取相关通知
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	public JsonResult getNotice(HttpServletRequest request,
			HttpServletResponse response, HttpSession session);
	/**
	 * 修改通知状态
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	public JsonResult updateNotice(HttpServletRequest request,
			HttpServletResponse response, HttpSession session);
	/**
	 * 删除通知
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	public JsonResult delNotice(HttpServletRequest request,
			HttpServletResponse response, HttpSession session);
	
}




