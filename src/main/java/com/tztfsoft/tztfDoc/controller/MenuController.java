package com.tztfsoft.tztfDoc.controller;



import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;

import com.tztfsoft.tztfDoc.entity.JsonResult;

import com.tztfsoft.tztfDoc.service.MenuService;

/**
 * 菜单控制器
 * @author kuaiDSH
 *
 */
@Controller
@RequestMapping("/m")
public class MenuController {
	@Resource
	MenuService menuService;
	/**
	 * 获取菜单目录
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public JsonResult getMenu(HttpServletRequest request, 
			HttpServletResponse response) {
		return menuService.getMenu(request, response);
		
	}
	
	/**
	 * 获取菜单目录
	 * @return
	 */
	@RequestMapping("/in.do")
	@ResponseBody
	public JsonResult insertMenu(HttpServletRequest request, 
			HttpServletResponse response) {
		return menuService.insertMenu(request, response);
		
	}
	/**
	 * 修改菜单
	 * @return
	 */
	@RequestMapping("/update.do")
	@ResponseBody
	public JsonResult updateMenu(HttpServletRequest request, 
			HttpServletResponse response) {
		return menuService.updateMenu(request, response);
		
	}
	/**
	 * 删除菜单
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public JsonResult deleteMenu(HttpServletRequest request, 
			HttpServletResponse response) {
		return menuService.deleteMenu(request, response);
		
	}
	
	/**
	 * 分析菜单完成情况
	 * @return
	 */
	@RequestMapping("/fenxi.do")
	@ResponseBody
	public JsonResult fenxiMenu(HttpServletRequest request, 
			HttpServletResponse response) {
		return menuService.fenxiMenu(request, response);
		
	}

}









