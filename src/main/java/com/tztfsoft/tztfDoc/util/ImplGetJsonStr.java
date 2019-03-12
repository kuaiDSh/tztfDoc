package com.tztfsoft.tztfDoc.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

/**
 * service实现类request数据工具
 * @author Administrator
 *
 */
public class ImplGetJsonStr {
	public static String getJsonResult(HttpServletRequest request) {
		try {
			request.setCharacterEncoding("utf-8");
			//获取输入流
			InputStream is = request.getInputStream();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] readData = new byte[1024];
			int readLen = 0;
			while((readLen = is.read(readData)) != -1) {
				bos.write(readData,0,readLen);
			}
			is.close();
			//返回的json字符串
			String jsonstr = bos.toString("utf-8");
			return jsonstr;
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
