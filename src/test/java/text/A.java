package text;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfStream;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.tztfsoft.tztfDoc.entity.LabelBean;

public class A {
//	private static BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false);
	
	public static void main(String[] args) {
		String path = "F:/aa/a.pdf";
		String savepath = "F:/aa/a/a.pdf";
		String savepath1 = "F:/aa/a/b.pdf";
		int id = 2333;
		String title = "标题文件名";
//		a(); 
		try {
//			initpdf(path, savepath, id, title);
//			a(savepath, savepath1);
			path(savepath,savepath1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 操作pdf文件
	 * 1.新建空白页（签核页）
	 * 2.每一页添加页脚：文件编号：
	 * @param path 文件地址
	 * @param savepath  文件保存地址
	 * @param id  文件编号
	 * @param title  文件签核页标题
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public static void initpdf(String path,String savepath,int id,String title) throws IOException, DocumentException {
		BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		//获取pdf文件对象
		PdfReader reader = new PdfReader(path);
//		reader.getp
		//操作pdf
		PdfStamper stamper = new PdfStamper(reader,new FileOutputStream(savepath));
		//设置一个A4空白页
		Rectangle rectangle = new Rectangle(PageSize.A4);
		//插入到第一页
		stamper.insertPage(1,rectangle);
		//得到pdf文件总页数（包含插入的空白页）
		int pages = reader.getNumberOfPages();
		System.out.println("当前文件总页数："+pages);
		//设置页脚内容
		Phrase footer = new Phrase("文件编号: "+id+" ", new Font(BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false), 12, Font.NORMAL));
		if(pages > 1) {
			for(int i = 1;i<=pages;i++) {
				PdfContentByte content = stamper.getOverContent(i);
//				ColumnText
				// 获取指定页信息
				Document document = new Document(reader.getPageSize(i));
				//插入页脚编号信息
				ColumnText.showTextAligned(content, Element.ALIGN_CENTER, footer,document.right()-20, document.bottom() - 20, 0);
				if(i == 1) {
					Phrase footer1 = new Phrase("签核页", new Font(BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false), 12, Font.NORMAL));
					ColumnText.showTextAligned(content, Element.ALIGN_CENTER, footer1,document.right()/2, document.top()+20, 0);
					content.beginText();
					content.setFontAndSize(BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false), 18);
					//设置文本绝对坐标
					content.setTextMatrix(document.left(),document.top()-10);
					content.showText("泰州风讯电子工程设备有限公司");
					content.setFontAndSize(bf, 10);
					content.setTextMatrix(document.right()/2,document.top()-30);
					content.showText(title);
					content.endText();
//					document.open();
					List<LabelBean> l = new ArrayList<LabelBean>();
					l.add(new LabelBean("2018-10-10","审批一","人员一","意见"));
					l.add(new LabelBean("2018-10-11","审批2","C:/Users/Administrator/Desktop/img/audit.png","意见1"));
					l.add(new LabelBean("2018-10-12","审批3","人员3","意见2意见2意见2意见2意见2意见2意见2意见2意见2意见2意见2意见2意见2意见2意见2意见2意见2意见2意见2意见2意见2意见2意见2意见2意见2意见2"));
					PdfPTable table = createTab(l, document,"create");
					table.writeSelectedRows(0, -1, document.left(), document.top()-40, content);
				}
			}
		}
		
		stamper.close();
		reader.close();
		
	}
	
	
	
	
	
	
	/**
	 * 生成表格
	 * @return
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public static PdfPTable createTab(List<LabelBean> list,Document document,String type) throws DocumentException, IOException {
		BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		Font fontChinese = new Font(bfChinese, 10.5F, Font.NORMAL);// 五号
		Paragraph ret = new Paragraph();
		PdfPTable table = new PdfPTable(4);
		table.setWidths(new float[] { 0.2f, 0.2f, 0.2f ,0.4f});// 每个单元格占多宽
		table.setWidthPercentage(80f);
		table.setTotalWidth(document.right()-document.rightMargin());
		if("create".equals(type)) {
			table.addCell(getCell(new Phrase("审批", fontChinese), false, 1, 1));
			table.addCell(getCell(new Phrase("签名", fontChinese), false, 1, 1));
			table.addCell(getCell(new Phrase("日期", fontChinese), false, 1, 1));
			table.addCell(getCell(new Phrase("意见", fontChinese), false, 1, 1));
		}
		
		for(LabelBean bean : list) {
			//审批
			table.addCell(getCell(new Phrase(bean.getAudit(), fontChinese), false, 1, 1));
			//签名
			if(bean.getAuditor().indexOf("/") > 0) {
//				Image image = Image.getInstance("C:/Users/Administrator/Desktop/img/audit.png");// 图片名称
				Image image = Image.getInstance(bean.getAuditor());// 图片名称
				table.addCell(image);
			}else {
				table.addCell(getCell(new Phrase(bean.getAuditor(), fontChinese), false, 1, 1));
			}
			//日期
			table.addCell(getCell(new Phrase(bean.getDate(), fontChinese), false, 1, 1));
			//意见
			table.addCell(getCell(new Phrase(bean.getContent(), fontChinese), false, 1, 1));
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
	
	
	public static String path(String path,String savepath) throws IOException {
		
		File file = new File(path);
		PDDocument doc = PDDocument.load(file);
		doc.removePage(0);
		 doc.save(savepath); 
		 doc.close();
//		reader.getPageContent(arg0)
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void a(String path,String savepath) throws DocumentException, IOException {
		BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		//获取pdf文件对象
		PdfReader reader = new PdfReader(path);
		PdfStamper stamper = new PdfStamper(reader,new FileOutputStream(savepath));
		Document document = new Document(reader.getPageSize(1));
//		document.open();
//		List<LabelBean> l = new ArrayList<LabelBean>();
//		l.add(new LabelBean("2018-10-10","审批一","人员一","意见"));
//		PdfPTable table = createTab(l, document, "");
//		document.add(table);
//		stamper.
		String pageContent = PdfTextExtractor.getTextFromPage(reader, 1);//读取第i页的文档内容
		System.out.println(pageContent);
//		PdfContentByte cb = stamper.getOverContent(1);
//		table.writeSelectedRows(0, -1, document.left(), document.top()-50, cb);
		stamper.close();
		document.close();
		reader.close();
		
	}
}












