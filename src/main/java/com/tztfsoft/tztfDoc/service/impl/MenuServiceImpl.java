package com.tztfsoft.tztfDoc.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tztfsoft.tztfDoc.dao.FileDao;
import com.tztfsoft.tztfDoc.dao.MenuDao;
import com.tztfsoft.tztfDoc.entity.FileBean;
import com.tztfsoft.tztfDoc.entity.JsonResult;
import com.tztfsoft.tztfDoc.entity.MenuBean;
import com.tztfsoft.tztfDoc.service.MenuService;
import com.tztfsoft.tztfDoc.util.DataUtil;
import com.tztfsoft.tztfDoc.util.ImplGetJsonStr;

/**
 * 菜单接口实现类
 * @author kuaiDSH
 *
 */
@Service("menuService")
public class MenuServiceImpl implements MenuService {
	@Resource
	MenuDao menuDao;
	@Resource
	FileDao fileDao;
	/**
	 * 获取菜单目录
	 * @param request
	 * @param response
	 * @return 返回的数据格式为bootstrap treeview 所需格式
	 */
	@Override
	public JsonResult getMenu(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		JsonResult jsonResult = new JsonResult();
		List<MenuBean> data = menuDao.getMenu();
		if(data.size() > 0) {
			System.out.println("菜单目录数据：");
			System.out.println(data);
//			List<Map<String,Object>> l = DataUtil.getMenuList(null, data);
			List<Map<String,Object>> l  = DataUtil.getMenuList1(data);
			System.out.println(l);
			jsonResult.setData(l);
			jsonResult.setResultTypeID(0);
		}else {
			jsonResult.setResultTypeID(-1);
			jsonResult.setHead("获取菜单失败");
		}
		
		return jsonResult;
	}
	/**
	 * 新增菜单节点
	 */
	@Override
	public JsonResult insertMenu(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		String jsonstr = ImplGetJsonStr.getJsonResult(request);
		JsonResult jsonResult = new JsonResult();
		try {
			MenuBean bean = new ObjectMapper().readValue(jsonstr,MenuBean.class);
			System.out.println(bean);
			menuDao.insertMenu(bean);
			jsonResult.setData(bean);
			jsonResult.setResultTypeID(0);
		} catch (JsonParseException e) {
			jsonResult.setResultTypeID(-1);
			jsonResult.setHead("新增目录失败，json转换失败");
			e.printStackTrace();
		} catch (JsonMappingException e) {
			jsonResult.setResultTypeID(-1);
			jsonResult.setHead("新增目录失败，json转换失败");
			e.printStackTrace();
		} catch (IOException e) {
			jsonResult.setResultTypeID(-1);
			jsonResult.setHead("新增目录失败，io错误");
			e.printStackTrace();
		}
		return jsonResult;
	}
	/**
	 * 修改菜单
	 */
	@Override
	public JsonResult updateMenu(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		String jsonstr = ImplGetJsonStr.getJsonResult(request);
		JsonResult jsonResult = new JsonResult();
		try {
			MenuBean bean = new ObjectMapper().readValue(jsonstr,MenuBean.class);
			System.out.println(bean);
			menuDao.updateMenu(bean);
			jsonResult.setData(bean);
			jsonResult.setResultTypeID(0);
		} catch (JsonParseException e) {
			jsonResult.setResultTypeID(-1);
			jsonResult.setHead("修改目录失败，json转换失败");
			e.printStackTrace();
		} catch (JsonMappingException e) {
			jsonResult.setResultTypeID(-1);
			jsonResult.setHead("修改目录失败，json转换失败");
			e.printStackTrace();
		} catch (IOException e) {
			jsonResult.setResultTypeID(-1);
			jsonResult.setHead("修改目录失败，io错误");
			e.printStackTrace();
		}
		return jsonResult;
	}
	/**
	 * 删除菜单
	 */
	@Override
	public JsonResult deleteMenu(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		String jsonstr = ImplGetJsonStr.getJsonResult(request);
		JsonResult jsonResult = new JsonResult();
		try {
			MenuBean bean = new ObjectMapper().readValue(jsonstr,MenuBean.class);
			System.out.println(bean);
			menuDao.deleteMenu(bean.getId());
			jsonResult.setResultTypeID(0);
		} catch (JsonParseException e) {
			jsonResult.setResultTypeID(-1);
			jsonResult.setHead("删除目录失败，json转换失败");
			e.printStackTrace();
		} catch (JsonMappingException e) {
			jsonResult.setResultTypeID(-1);
			jsonResult.setHead("删除目录失败，json转换失败");
			e.printStackTrace();
		} catch (IOException e) {
			jsonResult.setResultTypeID(-1);
			jsonResult.setHead("删除目录失败，io错误");
			e.printStackTrace();
		}
		return jsonResult;
	}
	/**
	 * 分析菜单完成情况
	 */
	@Override
	public JsonResult fenxiMenu(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		List<Map<String,Object>> reslist = new ArrayList<Map<String,Object>>();
		//1,得到存在完成时间的文档
		List<MenuBean> list = menuDao.fenxi();
		System.out.println("得到的菜单");
		System.out.println(list);
		//2.遍历文档
		for(MenuBean bean : list) {
			Map<String,Object> map = new HashMap<String, Object>();
			//获取文档记录
			FileBean fileBean = fileDao.fenxi(bean.getId());
			System.out.println("文件记录");
			System.out.println(fileBean);
			map.put("id", bean.getId());
			map.put("doneTime", bean.getDoneTime());
			map.put("parent", bean.getParent());
			map.put("name", bean.getName());
			map.put("content", bean.getContent());
			if(fileBean == null) {
				map.put("done", "未完成");
			}else {
				//完成时间
				String doneTime = bean.getDoneTime();
				if(doneTime.indexOf(',') > -1) {
					String[] arr = doneTime.split(",");
					Date date = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					System.out.println(sdf.format(date));
					String dater = sdf.format(date);
					String dateyear = dater.substring(0,4);
					System.out.println(dateyear);
					String datemoth = dater.substring(5,7);
					System.out.println(datemoth);
					String dateday = dater.substring(8);
					System.out.println(dateday);
					String filedate = fileBean.getFdate();
					String fileyear = filedate.substring(0,4);
					String filemoth = filedate.substring(5,7);
					String fileday = filedate.substring(8);
					System.out.println(fileyear.equals(dateyear));
					System.out.println();
					if(fileyear.equals(dateyear)) {
						//文件上传时间 是否是系统的今年,如果是，判断月份
						if("year".equals(arr[0])) {
							System.out.println("走这里");
							//根据每年几月判断
							System.out.println("文件上传年："+fileyear);
							System.out.println("文件上传月："+filemoth);
							System.out.println("文件上传天："+fileday);
							Integer filem = Integer.parseInt(filemoth);
							Integer m = Integer.parseInt(arr[1]);
							
							if(filem > m ) {
								//如果文件上传月份 大于应该上传月份
								map.put("done", "超时完成");
							}else if(filem < m || filem == m) {
								map.put("done", "完成");
							}
						}else if("moth".equals(arr[1])) {
							//根据每月几天之内完成
							if(filemoth.equals(datemoth)) {
								//文件上传月份必须是当前系统时间月份
								if(Integer.parseInt(fileday) > Integer.parseInt(arr[1])) {
									map.put("done", "超时完成");
								}else if(Integer.parseInt(fileday) < Integer.parseInt(arr[1]) || 
										Integer.parseInt(fileday) == Integer.parseInt(arr[1])) {
									map.put("done", "完成");
								}
							}else {
								map.put("done", "未完成");
							}
						}
					}else {
						map.put("done", "未完成");
//						map.put("", value);
					}
//					date.g
				}
			}
			reslist.add(map);
		}
		JsonResult jsonResult = new JsonResult();
		jsonResult.setData(reslist);
		System.out.println("返回数据");
		System.out.println(reslist);
		return jsonResult;
	}
}





