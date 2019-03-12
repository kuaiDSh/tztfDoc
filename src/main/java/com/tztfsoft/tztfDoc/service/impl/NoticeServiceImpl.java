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

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tztfsoft.tztfDoc.dao.NoticeDao;
import com.tztfsoft.tztfDoc.entity.JsonResult;
import com.tztfsoft.tztfDoc.entity.NoticeBean;
import com.tztfsoft.tztfDoc.service.NoticeService;
import com.tztfsoft.tztfDoc.util.ImplGetJsonStr;
@Service("noticeService")
public class NoticeServiceImpl implements NoticeService{
	@Resource
	NoticeDao noticeDao;
	/**
	 * 根据用户id 通知类型 获取相关通知信息
	 */
	@Override
	public JsonResult getNotice(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		response.setContentType("text/html;charset=utf-8");
		String jsonstr = ImplGetJsonStr.getJsonResult(request);
		JsonResult jsonResult = new JsonResult();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			@SuppressWarnings("unchecked")
			Map<String,Object> data = objectMapper.readValue(jsonstr,HashMap.class);
			//用户id
			Integer userid = (Integer) data.get("user");
			//通知类型
			Integer state = (Integer) data.get("state");
			List<NoticeBean> list = noticeDao.getNotice(userid, state);
			jsonResult.setData(list);
			jsonResult.setResultTypeID(0);
		} catch (JsonParseException e) {
			
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
	/**
	 * 删除通知
	 */
	@Override
	public JsonResult delNotice(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		JsonResult jsonResult = new JsonResult();
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=utf-8");
			
			String idr = request.getParameter("id");
			Integer id = Integer.parseInt(idr);
			noticeDao.deleteNotice(id);
			jsonResult.setResultTypeID(0);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonResult;
	}
	/**
	 * 修改通知
	 */
	@Override
	public JsonResult updateNotice(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=utf-8");
			String idr = request.getParameter("id");
			Integer id = Integer.parseInt(idr);
			noticeDao.updateNotice(id, 0);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
