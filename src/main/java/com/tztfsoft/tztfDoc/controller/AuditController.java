package com.tztfsoft.tztfDoc.controller;



import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tztfsoft.tztfDoc.entity.JsonResult;
import com.tztfsoft.tztfDoc.service.AuditService;

/**
 * 审核控制器
 * @author kuaiDSH
 *
 */
@Controller
@RequestMapping("/a")
public class AuditController {
	@Resource
	AuditService auditService;
	/**
	 * 创建审核
	 * @return
	 */
	@RequestMapping("/c.do")
	@ResponseBody
	public JsonResult createAudit(HttpServletRequest request, 
			HttpServletResponse response) {
		return auditService.createAudit(request, response);
		
	}
	/**
	 * 查看审核
	 * @return
	 */
	@RequestMapping("/s.do")
	@ResponseBody
	public JsonResult selectAudit(HttpServletRequest request, 
			HttpServletResponse response) {
		return auditService.selectAudit(request, response);
		
	}
	/**
	 * 新增审核记录
	 * @return
	 */
	@RequestMapping("/ir.do")
	@ResponseBody
	public JsonResult insertRecord(HttpServletRequest request, 
			HttpServletResponse response) {
		return auditService.insertAuditRecord(request, response);
		
	}
	/**
	 * 获取审核记录
	 * @return
	 */
	@RequestMapping("/gr.do")
	@ResponseBody
	public JsonResult getRecord(HttpServletRequest request, 
			HttpServletResponse response) {
		return auditService.getAuditRecord(request, response);
		
	}
}









