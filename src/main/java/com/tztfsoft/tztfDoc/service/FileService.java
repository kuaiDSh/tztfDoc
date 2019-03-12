package com.tztfsoft.tztfDoc.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.tztfsoft.tztfDoc.entity.JsonResult;
/**
 * 文件service接口
 * @author kuaiDSH
 *
 */
public interface FileService {
	/**
	 * 文件上传接口
	 * @param request
	 * @param response
	 * @return
	 */
	public JsonResult upload(CommonsMultipartFile file,HttpServletRequest request,
			HttpServletResponse response);
	/**
	 * 新增文件上传记录
	 * @param request
	 * @param response
	 * @return
	 */
	public JsonResult insertRecord(HttpServletRequest request,
			HttpServletResponse response);
	/**
	 * 查询文件
	 * @param request
	 * @param response
	 * @return
	 */
	public JsonResult selectRecord(HttpServletRequest request,
			HttpServletResponse response);
	/**
	 * 上传图片
	 * @param file
	 * @param request
	 * @param response
	 * @return
	 */
	public JsonResult uploadImg(CommonsMultipartFile file,
			HttpServletRequest request, HttpServletResponse response);
}




