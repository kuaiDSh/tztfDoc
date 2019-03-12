package com.tztfsoft.tztfDoc.controller;



import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.tztfsoft.tztfDoc.dao.FileDao;
import com.tztfsoft.tztfDoc.entity.FileBean;
import com.tztfsoft.tztfDoc.entity.JsonResult;
import com.tztfsoft.tztfDoc.service.FileService;
import com.tztfsoft.tztfDoc.util.FileUtil;

/**
 * 文件控制器
 * @author kuaiDSH
 *
 */
@Controller
@RequestMapping("/f")
public class FileController {
	@Resource
	FileService fileService;
	@Resource
	FileDao fileDao;
	/**
	 * 上传文件
	 * @return
	 */
	@RequestMapping("/upload.do")
	@ResponseBody
	public JsonResult upload(@RequestParam("file") CommonsMultipartFile file,HttpServletRequest request, 
			HttpServletResponse response) {
		return fileService.upload(file, request, response);
		
	}
	/**
	 * 新增文件
	 * @return
	 */
	@RequestMapping("/in.do")
	@ResponseBody
	public JsonResult insertRecord(HttpServletRequest request, 
			HttpServletResponse response) {
		return fileService.insertRecord(request, response);
		
	}
	/**
	 * 查询文件
	 * @return
	 */
	@RequestMapping("/s.do")
	@ResponseBody
	public JsonResult selectRecord(HttpServletRequest request, 
			HttpServletResponse response) {
		return fileService.selectRecord(request, response);
		
	}
	/**
	 * 预览图片
	 * @return
	 */
	@RequestMapping("/loadimg.do")
	@ResponseBody
	public void loadImg(@RequestParam(value="p")String path,HttpServletRequest request, 
			HttpServletResponse response) {
		String path1 = request.getSession().getServletContext().getRealPath("/temp/user/")+path;
		File file = new File(path1);
		InputStream in;
		try {
			in = new FileInputStream(file);
			BufferedOutputStream  bos = new BufferedOutputStream(response.getOutputStream());
			byte[] readData = new byte[1024];
			int len = 0;  
			while((len = in.read(readData)) != -1){  
				bos.write(readData,0,len);
//	            	bos.write(len);  
//	            	bos.flush();  
	        }  
//	        while((len = in.read()) != -1){  
//	        	bos.write(len);  
//	        	bos.flush();  
//	        }  
	        in.close();
	        bos.close(); 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * 读取文件 预览 pdf
	 * @return
	 */
	@RequestMapping("/load.do")
	@ResponseBody
	public void load(@RequestParam(value="id")Integer id,HttpServletRequest request, 
			HttpServletResponse response) {
		Date date = new Date();
		long time = date.getTime();
		System.out.println("开始预览时间:"+time);
		List<Map<String, Object>> list = fileDao.getFileRecordByID(id);
		if(list.size() >0) {
			System.out.println(list);
			//获取地址
			String path = (String) list.get(0).get("path");
			String name = (String) list.get(0).get("newName");
			path = path.replace('\\', '/')+"pdf/"+name+"/"+name+".pdf";
			File file = new File(path);
			try {
				InputStream in = new FileInputStream(file);
				BufferedOutputStream  bos = new BufferedOutputStream(response.getOutputStream());
				byte[] readData = new byte[1024];
				int len = 0;  
				while((len = in.read(readData)) != -1){  
					bos.write(readData,0,len);
		        }  
	            in.close();
	            bos.close(); 
	            Date date1 = new Date();
	            long time1 = date1.getTime();
	            System.out.println("结束预览时间："+time1);
	            System.out.println("耗时："+(time1-time));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	/**
	 * pdf文件盖章
	 * @return
	 */
	@RequestMapping("/addimg.do")
	@ResponseBody
	public boolean pdfAddImg(@RequestParam(value="id")Integer id,HttpServletRequest request, 
			HttpServletResponse response) {
		boolean flag = false;
		List<Map<String, Object>> list = fileDao.getFileRecordByID(id);
		//获取地址
		String path = (String) list.get(0).get("path");
		String fileOldName = (String) list.get(0).get("oldName");
		//文件后缀
		String suffix = fileOldName.substring(fileOldName.lastIndexOf(".")+1);
		path = request.getSession().getServletContext().getRealPath("/temp/"+suffix+"/");
		String name = (String) list.get(0).get("newName");
		System.out.println(path);
//		path = path.replaceAll("\\", "/");
		System.out.println(path+"/pdf/"+name+".pdf/"+name+".pdf");
		String path1 = path+"/pdf/"+name+"/";
		File file = new File(path1);
		if(!file.exists()) {
			System.out.println("这里没走吗");
			file.mkdirs();
		}
		path1 += name+".pdf";
		String imagePath = "C:/Users/Administrator/Desktop/img/audit.png";
		try {
			FileUtil.addPdfImg(0, path+"/pdf/"+name+".pdf", imagePath, path1);
			flag = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
//		File file = new File(path+"/pdf/"+name+".pdf");
	}
	
	
	/**
	 * 上传图片入口
	 * @return
	 */
	@RequestMapping("/upimg.do")
	@ResponseBody
	public JsonResult uploadImg(@RequestParam("file") CommonsMultipartFile file,HttpServletRequest request, 
			HttpServletResponse response) {
		return fileService.uploadImg(file, request, response);
		
	}
	
	/**
	 * 修改文件状态
	 * @return
	 */
	@RequestMapping("/updateS.do")
	@ResponseBody
	public JsonResult uploadFileState(HttpServletRequest request, 
			HttpServletResponse response) {
		JsonResult jsonResult = new JsonResult();
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=utf-8");
			String idr = request.getParameter("i");
			String stater = request.getParameter("s");
			Integer id = Integer.parseInt(idr);
			Integer state = Integer.parseInt(stater);
			fileDao.updateFileStatus(id, state);
			jsonResult.setResultTypeID(0);
		} catch (UnsupportedEncodingException e) {
			jsonResult.setResultTypeID(-1);
			e.printStackTrace();
		}
		return jsonResult;
	}
	
	/**
	 * 删除文件
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public JsonResult deleteFile(HttpServletRequest request, 
			HttpServletResponse response) {
		JsonResult jsonResult = new JsonResult();
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=utf-8");
			String idr = request.getParameter("id");
			Integer id = Integer.parseInt(idr);
			List<Map<String,Object>> list = fileDao.getFileRecordByID(id);
			//获取地址
			String path = (String) list.get(0).get("path");
			String fileOldName = (String) list.get(0).get("oldName");
			//文件后缀
			String suffix = fileOldName.substring(fileOldName.lastIndexOf(".")+1);
			path = path.replace('\\', '/');
			String name = (String) list.get(0).get("newName");
			String filePath = path+name+"."+suffix;
			String pdfPath =  path+"/pdf/"+name;
			
			File file = new File(filePath);
			if(file.isFile()) {
				file.delete();
			}
			File file2 = new File(pdfPath);
			if(file2.exists()) {
				if(file2.isDirectory()) {
					FileUtils.deleteDirectory(file2);
				}
			}
			//删除记录
			fileDao.deleteFileRecord(id);
			jsonResult.setResultTypeID(0);
		} catch (UnsupportedEncodingException e) {
			jsonResult.setResultTypeID(-1);
			e.printStackTrace();
		} catch (IOException e) {
			jsonResult.setResultTypeID(-1);
			e.printStackTrace();
		}
		return jsonResult;
	}
	
	
	
//	//删除指定文件夹下所有文件
//	//param path 文件夹完整绝对路径
//	   public static boolean delAllFile(String path) {
//	       boolean flag = false;
//	       File file = new File(path);
//	       if (!file.exists()) {
//	         return flag;
//	       }
//	       if (!file.isDirectory()) {
//	         return flag;
//	       }
//	       String[] tempList = file.list();
//	       File temp = null;
//	       for (int i = 0; i < tempList.length; i++) {
//	          if (path.endsWith(File.separator)) {
//	             temp = new File(path + tempList[i]);
//	          } else {
//	              temp = new File(path + File.separator + tempList[i]);
//	          }
//	          if (temp.isFile()) {
//	             temp.delete();
//	          }
//	          if (temp.isDirectory()) {
//	             delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
//	             delFolder(path + "/" + tempList[i]);//再删除空文件夹
//	             flag = true;
//	          }
//	       }
//	       return flag;
//	     }
//	}

	 
}









