package com.tztfsoft.tztfDoc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;

import com.artofsolving.jodconverter.DefaultDocumentFormatRegistry;
import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.DocumentFormat;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;



/**
 * 文件工具类
 * @author Administrator
 *
 */
public class Doc2HtmlUtil {
	private static Doc2HtmlUtil doc2HtmlUtil;
	public static synchronized Doc2HtmlUtil getDoc2HtmlUtilInstance() {
		if(doc2HtmlUtil == null) {
			doc2HtmlUtil = new Doc2HtmlUtil();
		}
		return doc2HtmlUtil;
	}
	/**
	 * 文件转换为pdf
	 * @param docfile 传入文件
	 * @param saveFileName  文件名称
	 * @param savePath  保存地址
	 * @return
	 * @throws ConnectException 
	 */
	public String file2Pdf(File docfile,String saveFileName,String savePath) throws ConnectException {
		File htmlFile = new File(savePath+"/"+saveFileName+".pdf");
		if (!htmlFile.exists()) {
			OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
			connection.connect();
//			try {
//				
//			} catch (ConnectException e) {
//				e.printStackTrace();
//				System.out.println("openOffice未启动");
//			}
			DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
            converter.convert(docfile, htmlFile);
            connection.disconnect();
		}
		return htmlFile.getName();
	}
//    /**
//     * 获取Doc2HtmlUtil实例
//     */
//    public static synchronized Doc2HtmlUtil getDoc2HtmlUtilInstance() {
//        if (doc2HtmlUtil == null) {
//            doc2HtmlUtil = new Doc2HtmlUtil();
//        }
//        return doc2HtmlUtil;
//    }
//    /**
//     * 转换文件成html
//     * @param fromFileInputStream
//     * @param toFilePath
//     * @param type
//     * @param host
//     * @param port
//     * @return
//     * @throws IOException
//     */
//    public String file2Html(InputStream fromFileInputStream, String toFilePath,String type,String host,int port) throws IOException {
//        Date date = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//        String timesuffix = sdf.format(date);
//        String docFileName = null;
//        String htmFileName = null;
//        if("doc".equals(type)){
//            docFileName = "doc_" + timesuffix + ".doc";
//            htmFileName = "doc_" + timesuffix + ".html";
//        }else if("docx".equals(type)){
//            docFileName = "docx_" + timesuffix + ".docx";
//            htmFileName = "docx_" + timesuffix + ".html";
//        }else if("xls".equals(type)){
//            docFileName = "xls_" + timesuffix + ".xls";
//            htmFileName = "xls_" + timesuffix + ".html";
//        }else if("ppt".equals(type)){
//            docFileName = "ppt_" + timesuffix + ".ppt";
//            htmFileName = "ppt_" + timesuffix + ".html";
//        }else{
//            return null;
//        }
//
//        File htmlOutputFile = new File(toFilePath + File.separatorChar + htmFileName);
//        File docInputFile = new File(toFilePath + File.separatorChar + docFileName);
//        if (htmlOutputFile.exists())
//            htmlOutputFile.delete();
//        htmlOutputFile.createNewFile();
//        if (docInputFile.exists())
//            docInputFile.delete();
//        docInputFile.createNewFile();
//        System.out.println("转换地址");
//        System.out.println(toFilePath + File.separatorChar + htmFileName);
//        /**
//         * 由fromFileInputStream构建输入文件
//         */
//        try {
//            OutputStream os = new FileOutputStream(docInputFile);
//            int bytesRead = 0;
//            byte[] buffer = new byte[1024 * 8];
//            while ((bytesRead = fromFileInputStream.read(buffer)) != -1) {
//                os.write(buffer, 0, bytesRead);
//            }
//
//            os.close();
//            fromFileInputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        OpenOfficeConnection connection = new SocketOpenOfficeConnection(host,port);
//        try {
//            connection.connect();
//        } catch (ConnectException e) {
//            System.err.println("文件转换出错，请检查OpenOffice服务是否启动。");
//        }
//        // convert
//        DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
//        converter.convert(docInputFile, htmlOutputFile);
//        connection.disconnect();
//        // 转换完之后删除word文件
//        docInputFile.delete();
//        return htmFileName;
//    }
//	public static void doc2pdf(){
//		OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
//	}
    /**
     * 执行前，请启动openoffice服务
     * 进入$OO_HOME\program下
     * 执行soffice -headless -accept="socket,host=127.0.0.1,port=8100;urp;"
     * @param xlsfile
     * @param targetfile
     * @throws Exception
     */
    public static void fileConvertPdf(String xlsfile, String targetfile,String type,String host,int port)
            throws Exception {
        File xlsf = new File(xlsfile);
        File targetF = new File(targetfile);
        // 获得文件格式
        DefaultDocumentFormatRegistry formatReg = new DefaultDocumentFormatRegistry();
        DocumentFormat pdfFormat = formatReg.getFormatByFileExtension("pdf");
        DocumentFormat docFormat = null;
        if("doc".equals(type) || "docx".equals(type)){
            docFormat = formatReg.getFormatByFileExtension("doc");
        }else if("xls".equals(type) || "xlsx".equals(type)){
            docFormat = formatReg.getFormatByFileExtension("xls");
        }else if("ppt".equals(type)){
            docFormat = formatReg.getFormatByFileExtension("ppt");
        }else{
            docFormat = formatReg.getFormatByFileExtension("doc");
        }
        // stream 流的形式
        InputStream inputStream = new FileInputStream(xlsf);
        OutputStream outputStream = new FileOutputStream(targetF);

        /**
         *
         */
        OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
        try {

            connection.connect();
            DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
            converter.convert(inputStream, docFormat, outputStream, pdfFormat);
        } catch (ConnectException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
                connection = null;
            }
        }
    }
}