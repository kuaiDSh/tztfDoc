package com.tztfsoft.tztfDoc.service.impl;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.DocumentException;
import com.tztfsoft.tztfDoc.dao.AuditDao;
import com.tztfsoft.tztfDoc.dao.FileDao;
import com.tztfsoft.tztfDoc.dao.NoticeDao;
import com.tztfsoft.tztfDoc.entity.AuditBean;
import com.tztfsoft.tztfDoc.entity.AuditItemBean;
import com.tztfsoft.tztfDoc.entity.AuditRecordBean;
import com.tztfsoft.tztfDoc.entity.FileBean;
import com.tztfsoft.tztfDoc.entity.JsonResult;
import com.tztfsoft.tztfDoc.entity.LabelBean;
import com.tztfsoft.tztfDoc.entity.NoticeBean;
import com.tztfsoft.tztfDoc.service.AuditService;
import com.tztfsoft.tztfDoc.util.FileUtil;
import com.tztfsoft.tztfDoc.util.ImplGetJsonStr;

/**
 * 用户接口实现类
 * @author kuaiDSH
 *
 */
@Service("auditService")
public class AuditServiceImpl implements AuditService {
	@Resource
	AuditDao auditDao;
	@Resource
	FileDao fileDao;
	@Resource
	NoticeDao noticeDao;
	/**
	 * 创建审核流程
	 */
	@Override
	public JsonResult createAudit(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		String jsonstr = ImplGetJsonStr.getJsonResult(request);
		JsonResult jsonResult = new JsonResult();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			@SuppressWarnings("unchecked")
			Map<String,Object> data = objectMapper.readValue(jsonstr,HashMap.class);
			@SuppressWarnings("unchecked")
			Map<String,Object> auditdata = (Map<String, Object>) data.get("audit");
			List<Map<String,Object>> auditItemdata = (List<Map<String, Object>>) data.get("item");
			String auditstr = objectMapper.writeValueAsString(auditdata);
			String auditItemstr = objectMapper.writeValueAsString(auditItemdata);
			AuditBean bean = objectMapper.readValue(auditstr, AuditBean.class);
			List<AuditItemBean> list = objectMapper.readValue(auditItemstr, new TypeReference<List<AuditItemBean>>(){} );
			System.out.println("审核实体类");
			System.out.println(bean);
			System.out.println("审核节点");
			System.out.println(list);
			if(bean.getId() == 0) {
				//新增
				System.out.println("新增");
				auditDao.insertAudit(bean);
				System.out.println(bean.getId());
				if(list.size() > 0) {
					for(int i = 0;i<list.size();i++) {
						AuditItemBean a = list.get(i);
						a.setId(bean.getId());
					}
				}
				auditDao.insertAuditItem(list);
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("audit", bean);
				map.put("item", list);
				jsonResult.setData(map);
			}else {
				//先修改审核内容
				auditDao.updateAudit(bean);
				//再删除
				auditDao.deleteAuditItem(bean.getId());
				if(list.size() > 0) {
					for(int i = 0;i<list.size();i++) {
						AuditItemBean a = list.get(i);
						a.setId(bean.getId());
					}
				}
				auditDao.insertAuditItem(list);
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("audit", bean);
				map.put("item", list);
				jsonResult.setData(map);
			}
			jsonResult.setResultTypeID(0);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
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
	 * 根据菜单id 获取审核数据
	 */
	@Override
	public JsonResult selectAudit(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		String jsonstr = ImplGetJsonStr.getJsonResult(request);
		JsonResult jsonResult = new JsonResult();
		
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			@SuppressWarnings("unchecked")
			Map<String,Integer> data = objectMapper.readValue(jsonstr,HashMap.class);
			Integer menuid = data.get("menu");
			AuditBean bean = auditDao.getAudit(menuid);
			System.out.println("获取到的审核数据");
			System.out.println(bean);
			if(bean == null) {
				AuditBean bean1 = new AuditBean();
				bean1.setId(0);
				bean1.setMenuID(menuid);
				bean1.setName("");
				jsonResult.setData(bean1);
			}else {
				List<AuditItemBean> list = auditDao.getAuditItem(bean.getId());
				System.out.println("获取到的审核节点数据");
				System.out.println(list);
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("audit", bean);
				map.put("item", list);
				jsonResult.setData(map);
				jsonResult.setResultTypeID(0);
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
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
	 * 新增审核记录
	 */
	@Override
	public JsonResult insertAuditRecord(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		String jsonstr = ImplGetJsonStr.getJsonResult(request);
		JsonResult jsonResult = new JsonResult();
		
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			AuditRecordBean bean = objectMapper.readValue(jsonstr, AuditRecordBean.class);
			auditDao.insertAuditRecord(bean);
			//1.根据文件id，获取文件信息
			List<Map<String, Object>> listFile = fileDao.getFileRecordByID(bean.getFileID());
			String newName = (String) listFile.get(0).get("newName");
			String path = (String) listFile.get(0).get("path");
			String menuname = (String) listFile.get(0).get("menuid");
			path = path.replace('\\', '/')+"pdf/"+newName+"/";
			String savepath = path+"/a.pdf";
			String path1 = path+newName+".pdf";
			FileUtil.delete(path1, savepath);
			//1.审核记录新增之后，改变文件pdf签核页
			//获取审核记录
			List<Map<String,Object>> list_record = auditDao.getRecordBymff(bean.getMenuID(),bean.getFileID());
			List<AuditItemBean> list = auditDao.getAuditItem(bean.getAuditID());
			List<LabelBean> listlabel = new ArrayList<LabelBean>();
			for(int i = 1;i<list_record.size();i++) {
				LabelBean labelBean = new LabelBean();
				String user = (String) list_record.get(i).get("auditor");
				String userpath = (String) list_record.get(i).get("path");
				if(userpath != null && !userpath.equals("")) {
					String p = request.getSession().getServletContext().getRealPath("/temp/user/"+userpath);
					labelBean.setAuditor(p);
				}else {
					labelBean.setAuditor(user);
				}
				String count = (String) list_record.get(i).get("aditContent");
				labelBean.setContent(count);
//				String date = (String) list_record.get(i).get("auditDate");
				Date date =  (Date) list_record.get(i).get("auditDate");
				labelBean.setDate(date.toString());
				Integer fIndex = (Integer) list_record.get(i).get("fIndex");
				for(int j = 0;j<list.size();j++) {
					if(fIndex == list.get(j).getIndex()) {
						String auditname = list.get(j).getName();
						labelBean.setAudit(auditname);
					}
				}
				listlabel.add(labelBean);
			}
			FileUtil.initPdf(savepath, path1, "泰州风讯电子工程设备有限公司", menuname, "", bean.getFileID(), listlabel);
			
			
			
			//新增完成之后，判断审核逻辑
			//新增通知信息
			Integer auditState = bean.getAuditState();
			//通知信息实体类 a.html?m=5&f=25&a=1
			NoticeBean noticeBean = new NoticeBean();
			noticeBean.setUrl("a.html?m="+bean.getMenuID()+"&f="+bean.getFileID()+"&a="+bean.getAuditID());
			noticeBean.setState(1);
			noticeBean.setId(0);
//			noticeBean.setCount("");
//			noticeBean.setUser(1);
			if(auditState == 0) {
				/*
				 * 同意、判断index是否为最后一步
				 * 1.根据审核id  获取审核节点数据
				 */
				
				if(bean.getfIndex() == list.size()) {
					//最后一步、审核通过
					fileDao.updateFileStatus(bean.getFileID(), 2);
					//审核通过发送给原文件上传人，告诉他文件已经审核完成
					//获取原文件上传人id
					Integer uploaduserid = (Integer) listFile.get(0).get("userid1");
					//原始文件名
					String fileOldName = (String) listFile.get(0).get("oldName");
					noticeBean.setUser(uploaduserid);
					noticeBean.setCount("您的文件【"+menuname+"--"+fileOldName+"】已审核通过，双击查看");
					noticeDao.insertNotices(noticeBean);
				}else {
					//审核中
					fileDao.updateFileStatus(bean.getFileID(), 1);
					//得到下一步审核信息
					Integer index = 0;
					for(int j = 0;j<list.size();j++) {
						if(bean.getfIndex() == list.get(j).getIndex()) {
							index = j+1;
							break;
						}
					}
					AuditItemBean auditItemBean = list.get(index);
					//下一步审核人
					String auditor = auditItemBean.getAuditor();
					String uploadUser = (String) listFile.get(0).get("userid");
					if(auditor.indexOf(',') > -1) {
						//有多个审核人
						List<NoticeBean> list_noBeans = new ArrayList<NoticeBean>();
						String[] arr = auditor.split(",");
						for(int i = 0;i<arr.length;i++) {
							NoticeBean noticeBean1 = new NoticeBean();
							noticeBean1.setUrl("a.html?m="+bean.getMenuID()+"&f="+bean.getFileID()+"&a="+bean.getAuditID());
							noticeBean1.setState(1);
							noticeBean1.setId(0);
							noticeBean1.setUser(Integer.parseInt(arr[i]));
							noticeBean1.setCount("您有一份关于【"+uploadUser+"--"+menuname+"】的审核，双击进行审核");
							list_noBeans.add(noticeBean1);
						}
						for(int i = 0;i<list_noBeans.size();i++) {
							noticeDao.insertNotices(list_noBeans.get(i));
						}
					}else {
						//一个审核人
						noticeBean.setUser(Integer.parseInt(auditor));
						noticeBean.setCount("您有一份关于【"+uploadUser+"--"+menuname+"】的审核，双击进行审核");
						noticeDao.insertNotices(noticeBean);
					}
				}
			}else if(auditState == 1 || auditState == 2) {
				//不同意
				fileDao.updateFileStatus(bean.getFileID(), 4);
				Integer uploaduserid = (Integer) listFile.get(0).get("userid1");
				//原始文件名
				String fileOldName = (String) listFile.get(0).get("oldName");
				noticeBean.setUser(uploaduserid);
				noticeBean.setCount("您的文件【"+menuname+"--"+fileOldName+"】审核未通过，双击查看");
				noticeDao.insertNotices(noticeBean);
			}else if(auditState == 2) {
//				//被驳回
//				fileDao.updateFileStatus(bean.getFileID(), 4);
			}
			jsonResult.setResultTypeID(0);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			
			e.printStackTrace();
		}

		return jsonResult;
	}
	/**
	 * 根据菜单id 文件id  获取审核记录
	 */
	@Override
	public JsonResult getAuditRecord(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		String jsonstr = ImplGetJsonStr.getJsonResult(request);
		JsonResult jsonResult = new JsonResult();
		
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			Map<String,Object> map = objectMapper.readValue(jsonstr, HashMap.class);
			Integer menuid = (Integer) map.get("menuid");
			Integer fileid = (Integer) map.get("fileid");
			List<Map<String,Object>> list = auditDao.getRecordBymf(menuid, fileid);
			jsonResult.setData(list);
			jsonResult.setResultTypeID(0);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
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
}





