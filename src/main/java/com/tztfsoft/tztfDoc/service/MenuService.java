package com.tztfsoft.tztfDoc.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.tztfsoft.tztfDoc.entity.JsonResult;
/**
 * 菜单接口
 * @author kuaiDSH
 *
 */
public interface MenuService {
	/**
	 * 获取菜单目录
	 * @param request
	 * @param response
	 * @return
	 */
	public JsonResult getMenu(HttpServletRequest request,
			HttpServletResponse response);
	/**
	 * 新增菜单
	 * @param request
	 * @param response
	 * @return
	 */
	public JsonResult insertMenu(HttpServletRequest request,
			HttpServletResponse response);
	/**
	 * 修改菜单
	 * @param request
	 * @param response
	 * @return
	 */
	public JsonResult updateMenu(HttpServletRequest request,
			HttpServletResponse response);
	/**
	 * 删除菜单
	 * @param request
	 * @param response
	 * @return
	 */
	public JsonResult deleteMenu(HttpServletRequest request,
			HttpServletResponse response);
	/**
	 * 分析菜单
	 * @param request
	 * @param response
	 * @return
	 */
	public JsonResult fenxiMenu(HttpServletRequest request,
			HttpServletResponse response);
}




