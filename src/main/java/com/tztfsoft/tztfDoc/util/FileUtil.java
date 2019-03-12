package com.tztfsoft.tztfDoc.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.tztfsoft.tztfDoc.entity.LabelBean;

/**
 * 文件工具类
 * @author kuaiDSH
 *
 */
public class FileUtil {
	/**
	 * 初始化pdf文件
	 * 1.新建空白页（签核页）
	 * 2.每一页添加页脚：文件编号：
	 * @param path 文件地址
	 * @param savePath  保存地址
	 * @param title  签核页标题 【泰州风讯电子工程设备有限公司】
	 * @param tabletitle 文件分类 菜单名称
	 * @param type init 初始化    create 
	 * @param id  文件编号
	 * @param list 签核页信息 初始化时为null
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public static void initPdf(String path,String savePath,String title,String tabletitle,String type,int id,List<LabelBean> list) throws DocumentException, IOException{
		//字体
		BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		//获取pdf文件对象
		PdfReader reader = new PdfReader(path);
		//操作pdf
		PdfStamper stamper = new PdfStamper(reader,new FileOutputStream(savePath));
		//设置一个A4空白页
		Rectangle rectangle = new Rectangle(PageSize.A4);
		//插入到第一页
		stamper.insertPage(1,rectangle);
		//得到pdf文件总页数（包含插入的空白页）
		int pages = reader.getNumberOfPages();
		System.out.println("当前文件总页数："+pages);
		//设置页脚内容
		Phrase footer = new Phrase("文件编号: "+id+" ", new Font(BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false), 12, Font.NORMAL));
		Document document = new Document(reader.getPageSize(1));
		if(pages > 1) {
			for(int i = 1;i<=pages;i++) {
				PdfContentByte content = stamper.getOverContent(i);
				if(i == 1) {
					ColumnText.showTextAligned(content, Element.ALIGN_CENTER, footer,document.right()-20, document.bottom() - 20, 0);
					Phrase footer1 = new Phrase("签核页", new Font(BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false), 15, Font.NORMAL));
					ColumnText.showTextAligned(content, Element.ALIGN_CENTER, footer1,document.right()/2, document.top()+20, 0);
					content.beginText();
					content.setFontAndSize(BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false), 18);
					//设置文本绝对坐标
					content.setTextMatrix(document.left(),document.top()-10);
					content.showText(title);
					content.setFontAndSize(bf, 10);
					content.setTextMatrix(document.right()/2,document.top()-30);
					content.showText(tabletitle);
					content.endText();
					PdfPTable table = createTab(list, document);
					table.writeSelectedRows(0, -1, document.left(), document.top()-40, content);
				}else {
					if("init".equals(type)) {
						//插入页脚编号信息
						ColumnText.showTextAligned(content, Element.ALIGN_CENTER, footer,document.right()-20, document.bottom() - 20, 0);
					}
				}
			}
		}
		document.close();
		stamper.close();
		reader.close();
	}
	
	
	/**
	 * 生成表格
	 * @return
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public static PdfPTable createTab(List<LabelBean> list,Document document) throws DocumentException, IOException {
		BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		Font fontChinese = new Font(bfChinese, 10.5F, Font.NORMAL);// 五号
		PdfPTable table = new PdfPTable(4);
		table.setWidths(new float[] { 0.2f, 0.2f, 0.2f ,0.4f});// 每个单元格占多宽
		table.setWidthPercentage(80f);
		table.setTotalWidth(document.right()-document.rightMargin());
		table.addCell(getCell(new Phrase("审批", fontChinese), false, 1, 1));
		table.addCell(getCell(new Phrase("签名", fontChinese), false, 1, 1));
		table.addCell(getCell(new Phrase("日期", fontChinese), false, 1, 1));
		table.addCell(getCell(new Phrase("意见", fontChinese), false, 1, 1));
		if(null != list) {
			for(LabelBean bean : list) {
				//审批
				table.addCell(getCell(new Phrase(bean.getAudit(), fontChinese), false, 1, 1));
				//签名
				String user = bean.getAuditor();
				user = user.replace('\\', '/');
				if(user.indexOf("/") > 0) {
//					Image image = Image.getInstance("C:/Users/Administrator/Desktop/img/audit.png");// 图片名称
					Image image = Image.getInstance(user);// 图片名称
					table.addCell(image);
				}else {
					table.addCell(getCell(new Phrase(user, fontChinese), false, 1, 1));
				}
				//日期
				table.addCell(getCell(new Phrase(bean.getDate(), fontChinese), false, 1, 1));
				//意见
				table.addCell(getCell(new Phrase(bean.getContent(), fontChinese), false, 1, 1));
			}
		}
		return table;
	}
	private static PdfPCell getCell(Phrase phrase, boolean yellowFlag, int colSpan, int rowSpan) {
		PdfPCell cells = new PdfPCell(phrase);
		cells.setUseAscender(true);
		cells.setMinimumHeight(20f);
		cells.setHorizontalAlignment(1);
		cells.setVerticalAlignment(5);
		cells.setColspan(colSpan);
		cells.setRowspan(rowSpan);
		cells.setNoWrap(false);
		return cells;
	}
	
	/**
	 * 删除pdf文件第一页签核页
	 * @param path 源文件 地址
	 * @param savepath  保存地址
	 * @throws IOException
	 */
	public static void delete(String path,String savepath) throws IOException {
		
		File file = new File(path);
		PDDocument doc = PDDocument.load(file);
		doc.removePage(0);
		doc.save(savepath); 
		doc.close();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 作废
	 * 对pdf文件 具体页数、绝对位置插入图片
	 * @param page 页数
	 * @param pdfpath pdf文件地址
	 * @param imgpath 图片地址
	 * @param savepath pdf保存位置【新的pdf】
	 * @throws IOException 
	 */
	public static void addPdfImg(int page,String pdfpath,String imgpath,String savepath) throws IOException{
		int marginLeft = 30;// 左边距
		int marginBottom = 100;// 底边距
		//获取pdf对象
		PdfReader pdfreader = new PdfReader(pdfpath);
		// 获得PDF总页数
		int pdfPage = pdfreader.getNumberOfPages();
		if(page == 0) {
			page = pdfPage;
		}
		if (page < 0 || page > pdfPage) {
			throw new RuntimeException("pdf无当前页");
		}else {
			// 获取指定页的宽和高
			Document document = new Document(pdfreader.getPageSize(page));
			// 获取页面宽度
			float width = document.getPageSize().getWidth();
			// 获取页面高度
			float height = document.getPageSize().getHeight();
			if (pdfreader != null) {
				pdfreader.close();
			}
			if (document != null) {
				document.close();
			}
			System.out.println("pdfPage=" + pdfPage + ",width = " + width
					+ ", height = " + height);
			//
			PdfReader pdf = new PdfReader(pdfpath);
			PdfStamper stamper = null;
			try {
				stamper = new PdfStamper(pdf, new FileOutputStream(savepath));// 生成的PDF
				PdfContentByte overContent = stamper.getOverContent(page);
				Image image = Image.getInstance(imgpath);// 图片名称
				image.setAbsolutePosition(marginLeft, marginBottom);// 左边距、底边距
				overContent.addImage(image);
				overContent.stroke();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (null != stamper) {
						stamper.close();
					}
					if (pdf != null) {
						pdf.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	}
	
	
	
}





