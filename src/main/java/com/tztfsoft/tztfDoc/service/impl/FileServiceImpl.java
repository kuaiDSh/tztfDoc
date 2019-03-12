package com.tztfsoft.tztfDoc.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.DocumentException;
import com.tztfsoft.tztfDoc.dao.FileDao;
import com.tztfsoft.tztfDoc.dao.MenuDao;
import com.tztfsoft.tztfDoc.entity.FileBean;
import com.tztfsoft.tztfDoc.entity.JsonResult;
import com.tztfsoft.tztfDoc.service.FileService;
import com.tztfsoft.tztfDoc.util.Doc2HtmlUtil;
import com.tztfsoft.tztfDoc.util.FileUtil;
import com.tztfsoft.tztfDoc.util.ImplGetJsonStr;
/**
 * 文件接口实现类
 * @author kuaiDSH
 *
 */
@Service("fileService")
public class FileServiceImpl implements FileService {
	@Resource
	FileDao fileDao;
	@Resource
	MenuDao menuDao;
	/**
	 * 上传签名图片
	 */
	@Override
	public JsonResult uploadImg(CommonsMultipartFile file,
			HttpServletRequest request, HttpServletResponse response) {
		//文件原始名
		String fileOldName = file.getOriginalFilename();
		//文件后缀
		String suffix = fileOldName.substring(fileOldName.lastIndexOf(".")+1);
		Date date = new Date();
		//加密过后名称
		String newName = DigestUtils.md5Hex(fileOldName+date.getTime())+"."+suffix;	
		
		//临时文件夹地址
		String path = request.getSession().getServletContext().getRealPath("/temp/user/");
		File f = new File(path);
		//如果该路径不存在
		if(!f.exists()) {
			//创建文件  生成所有目录
			f.mkdirs();
		}
		File file1 = new File(path+newName);
		JsonResult jsonResult = new JsonResult();
		//输出流、写入文件
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file1);
			//输入流、读取前台传入的文件
			InputStream in = file.getInputStream();
			byte[] readData = new byte[1024];
			int b = 0;
			//读取
			while ((b = in.read(readData)) != -1) {
				fos.write(readData,0,b);
//				fos.write(b);
			}
			fos.close();
			in.close();
			jsonResult.setData(newName);
			jsonResult.setResultTypeID(0);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			jsonResult.setResultTypeID(-1);
			jsonResult.setHead("文件不存在");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonResult;
	}
	/**
	 * 文件上传实现
	 * 1.解析上传文件，获取文件名、后缀名
	 * 2.文件名加密、保证唯一  MD5(原始文件名+时间戳)+"."+后缀
	 * 3.存入临时文件夹 	temp/后缀/
	 */
	public JsonResult upload(CommonsMultipartFile file,
			HttpServletRequest request, HttpServletResponse response) {
		System.out.println("进入上传文件");
		response.setContentType("text/html;charset=utf-8");
		//文件原始名
		String fileOldName = file.getOriginalFilename();
		//文件后缀
		String suffix = fileOldName.substring(fileOldName.lastIndexOf(".")+1);
		System.out.println("上传文件原始名："+fileOldName);
		System.out.println("文件后缀名:"+suffix);
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//加密过后名称
		String newName = DigestUtils.md5Hex(fileOldName+date.getTime());	
		//临时文件夹地址
		String path = request.getSession().getServletContext().getRealPath("/temp/"+suffix+"/");
		File f = new File(path);
		//如果该路径不存在
		if(!f.exists()) {
			//创建文件  生成所有目录
			f.mkdirs();
		}
//		path = path+newName;
		//写入文件
		File file1 = new File(path+newName+"."+suffix);
		JsonResult jsonResult = new JsonResult();
		//输出流、写入文件
		try {
			Long time1 = date.getTime();
			System.out.println("开始写入文件时间:"+time1);
			//输出流、写入文件
			FileOutputStream fos = new FileOutputStream(file1);
			//输入流、读取前台传入的文件
			InputStream in = file.getInputStream();
			byte[] readData = new byte[1024];
			int b = 0;
			//读取
			while ((b = in.read(readData)) != -1) {
				fos.write(readData,0,b);
//				fos.write(b);
			}
			fos.close();
			in.close();
			Date date1 = new Date();
			Long time2 = date1.getTime();
			System.out.println("文件地址："+path);
			System.out.println("写入文件完成时间:"+time2);
			System.out.println("耗时:"+(time2-time1));
//			File file2 = new File(path+"/html");
//			if(!file2.exists()) {
//				file2.mkdirs();
//			}
			
			Doc2HtmlUtil.getDoc2HtmlUtilInstance().file2Pdf(file1, newName, path+"/pdf");
			//转码成功之后，返回前台相关数据
			Date date2 = new Date();
			Long time3 = date2.getTime();
			System.out.println("文件转换pdf耗时："+(time3-time2));
//			//上传完成后、记录数据
			FileBean bean = new FileBean();
			bean.setId(0);
			bean.setDownNum(0);
			bean.setNewName(newName);
			bean.setOldName(fileOldName);
			bean.setPath(path);
			bean.setStatus(0);
			bean.setFdate(sdf.format(date));
//			bean.setMenuid(menuid);
//			fileDao.insertFileRecord(bean);
//			
//			
			jsonResult.setResultTypeID(0);
			jsonResult.setData(bean);
			jsonResult.setHead("文件【"+fileOldName+"】上传成功");
//			return ;
		} catch (FileNotFoundException e) {
			jsonResult.setResultTypeID(-1);
			jsonResult.setHead("输出流错误：服务器在写入文件【"+fileOldName+"】发生错误、未发现该文件");
			e.printStackTrace();
			System.out.println("输出流错误：服务器在写入文件【"+fileOldName+"】发生错误、未发现该文件");
		} catch (ConnectException e) {
			jsonResult.setResultTypeID(-1);
			jsonResult.setHead("openoffice服务未启动，请联系管理员");
			e.printStackTrace();
			System.out.println("openoffice服务未启动，请联系管理员");
		} catch (IOException e) {
			jsonResult.setResultTypeID(-1);
			jsonResult.setHead("获取输入流错误：服务器读取传入文件【"+fileOldName+"】错误");
			e.printStackTrace();
			System.out.println("获取输入流错误：服务器读取传入文件【"+fileOldName+"】错误");
		}
		return jsonResult;	
	}
	/**
	 * 新增文件上传记录
	 * 新增文件上传目录之前、先初始化pdf文件
	 */
	@Override
	public JsonResult insertRecord(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		String jsonstr = ImplGetJsonStr.getJsonResult(request);
		JsonResult jsonResult = new JsonResult();
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				FileBean bean = objectMapper.readValue(jsonstr,FileBean.class);
				fileDao.insertFileRecord(bean);
				
				System.out.println(bean.getMenuid());
				////////////以下操作初始化pdf
				String newname = bean.getNewName();
				String path = bean.getPath().replace('\\', '/')+"/pdf/";
				//文件目录
				String filepath = path+newname+".pdf";
				String savepath = path+newname+"/";
				File file = new File(savepath);
				if(!file.exists()) {
					file.mkdirs();
				}
				savepath += newname+".pdf";
				System.out.println(filepath);
				Map<String,String> map = menuDao.getMenuNameByID(bean.getMenuid());
				String tabletitle = map.get("name");
				//以上初始化pdf
				FileUtil.initPdf(filepath, savepath, "泰州风讯电子工程设备有限公司", tabletitle, "init", bean.getId(), null);
				List<Map<String,Object>> list = fileDao.getFileRecordByID(bean.getId());
				jsonResult.setResultTypeID(0);
				jsonResult.setData(list);
			} catch (JsonParseException e) {
				jsonResult.setResultTypeID(-1);
				jsonResult.setHead("json转换错误");
				e.printStackTrace();
			} catch (JsonMappingException e) {
				jsonResult.setResultTypeID(-1);
				jsonResult.setHead("json转换错误");
				e.printStackTrace();
			} catch (IOException e) {
				jsonResult.setResultTypeID(-1);
				jsonResult.setHead("io错误");
				e.printStackTrace();
			} catch (DocumentException e) {
				jsonResult.setResultTypeID(-1);
				jsonResult.setHead("初始化文件编号时发生错误");
				e.printStackTrace();
			}
		return jsonResult;
	}
	/**
	 * 查询文件
	 */
	@Override
	public JsonResult selectRecord(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		String jsonstr = ImplGetJsonStr.getJsonResult(request);
		JsonResult jsonResult = new JsonResult();
			ObjectMapper objectMapper = new ObjectMapper();
			
		try {
			@SuppressWarnings("unchecked")
			Map<String,Object> map = objectMapper.readValue(jsonstr,HashMap.class);
			String type = (String) map.get("type");
			if(type != null) {
				List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
				if("user".equals(type)) {
					//查看本人的上传记录
					Integer userid = (Integer) map.get("userid");
					Integer menuid = (Integer) map.get("menuid");
					list = fileDao.getFileRecord(menuid, userid);
				}else if("id".equals(type)) {
					//根据文件id 获取相关记录
					Integer id = (Integer) map.get("id");
					list = fileDao.getFileRecordByID(id);
				}else if("status".equals(type)) {
					
					//根据文件状态查看所有文件
					Integer menuid = (Integer) map.get("menuid");
					Integer status = (Integer) map.get("status");
					if(status == 0) {
						list = fileDao.getFileRecordByMenu(menuid);
					}else {
						list = fileDao.getFileRecordByMenuAndState(menuid, status);
					}
					
				}
				jsonResult.setData(list);
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
}





