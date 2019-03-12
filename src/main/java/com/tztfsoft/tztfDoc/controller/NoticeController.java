package com.tztfsoft.tztfDoc.controller;



import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tztfsoft.tztfDoc.entity.JsonResult;
import com.tztfsoft.tztfDoc.service.MenuService;
import com.tztfsoft.tztfDoc.service.NoticeService;

/**
 * 通知控制器
 * @author kuaiDSH
 *
 */
@Controller
@RequestMapping("/n")
public class NoticeController {
	@Resource
	NoticeService noticeService;
	/**
	 * 根据用户id 获取其相关通知
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public JsonResult getNotice(HttpServletRequest request, 
			HttpServletResponse response,HttpSession session) {
		return noticeService.getNotice(request,response,session);
		
	}
	
	/**
	 * 修改通知
	 * @return
	 */
	@RequestMapping("/update.do")
	@ResponseBody
	public JsonResult updateNotice(HttpServletRequest request, 
			HttpServletResponse response,HttpSession session) {
		return noticeService.updateNotice(request,response,session);
		
	}
	/**
	 * 删除通知
	 * @return
	 */
	@RequestMapping("/del.do")
	@ResponseBody
	public JsonResult delNotice(HttpServletRequest request, 
			HttpServletResponse response,HttpSession session) {
		return noticeService.delNotice(request,response,session);
		
	}
}









