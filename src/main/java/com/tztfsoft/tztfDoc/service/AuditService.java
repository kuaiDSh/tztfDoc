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
public interface AuditService {
	/**
	 * 创建审核流程
	 * @param request
	 * @param response
	 * @return
	 */
	public JsonResult createAudit(HttpServletRequest request,
			HttpServletResponse response);
	/**
	 * 根据菜单id 获取审核数据
	 * @param request
	 * @param response
	 * @return
	 */
	public JsonResult selectAudit(HttpServletRequest request,
			HttpServletResponse response);
	/**
	 * 新增审核记录
	 * @param request
	 * @param response
	 * @return
	 */
	public JsonResult insertAuditRecord(HttpServletRequest request,
			HttpServletResponse response);
	/**
	 * 根据菜单id 文件id 获取审核记录
	 * @param request
	 * @param response
	 * @return
	 */
	public JsonResult getAuditRecord(HttpServletRequest request,
			HttpServletResponse response);
	
	
}




