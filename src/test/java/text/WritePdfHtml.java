package text;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Header;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.api.Spaceable;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.ElementHandlerPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.tztfsoft.tztfDoc.entity.LabelBean;

/**
 * 测试pdf写入html
 * @author Administrator
 *
 */
public class WritePdfHtml {
	static String html = "<html><body><table><tr><th>Month</th><th>Savings</th></tr><tr><td>January</td><td>$100</td></tr></table></body></html>";
	
	static String path = "E:/java/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/tztfDoc/temp/doc/pdf/0a7b2a7e54c84a0937f9c41586330d2b/a.pdf";
	static String save = "E:/java/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/tztfDoc/temp/doc/pdf/0a7b2a7e54c84a0937f9c41586330d2b/";
	public static void main(String[] args) throws DocumentException {
		File f = new File(save);
		if(!f.exists()) {
			f.mkdirs();
		}
		save += "b.pdf";
		a();
	}
	public static void a() throws DocumentException {

		//获取pdf对象
		PdfReader pdfreader;
		try {
			
			FileOutputStream outputStream = new FileOutputStream(save);
			pdfreader = new PdfReader(path);
//			PdfWriter writer = new PdfWriter("");
//			PdfReader pdfreader
//			PdfStamper stamper = new PdfStamper(pdfreader, new FileOutputStream(save));
//			stamper.
//			PdfContentByte con = stamper.getOverContent(1);
			Rectangle pageSize = pdfreader.getPageSize(1);
			Document document = new Document(pageSize);
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			BaseFont fontChinese = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);//设置中文字体
			Font chinese = new Font(fontChinese, 10, Font.NORMAL); 
//			HeaderFooter footer=new HeaderFooter(new Phrase("-",chinese),new Phrase("-",chinese));
			// 5.写入页脚1，x轴就是(右margin+左margin + right() -left()- len)/2.0F 再给偏移20F适合人类视觉感受，否则肉眼看上去就太偏左了 ,y轴就是底边界-20,否则就贴边重叠到数据体里了就不是页脚了；注意Y轴是从下往上累加的，最上方的Top值是大于Bottom好几百开外的。
//			ColumnText.showTextAligned(arg0, Element.ALIGN_CENTER, "", arg3, arg4, arg5, arg6, arg7);
			document.open();
			PdfContentByte cbUnder = writer.getDirectContentUnder();
			PdfContentByte cb = writer.getDirectContent();
			// 3.计算前半部分的foot1的长度，后面好定位最后一部分的'Y页'这俩字的x轴坐标，字体长度也要计算进去 = len
//	        float len = bf.getWidthPoint(foot1, presentFontSize);
			Phrase footer = new Phrase("aaaa", new Font(BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false), 12, Font.NORMAL));
			ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, footer,(document.rightMargin() + document.right() + document.leftMargin() - document.left() - 10) / 2.0F + 20F, document.bottom() - 20, 0);
			
//			PdfImportedPage pageTemplate = writer.getImportedPage(pdfreader, 1);
//			cbUnder.addTemplate(pageTemplate, 0, 0);
//			document.newPage();//新创建一页来存放后面生成的表格
			
			List<LabelBean> l = new ArrayList<LabelBean>();
			l.add(new LabelBean("2018-10-10","审批一","人员一","意见"));
			l.add(new LabelBean("2018-10-11","审批2","人员2","意见1"));
			l.add(new LabelBean("2018-10-12","审批3","人员3","意见2意见2意见2意见2意见2意见2意见2意见2意见2意见2意见2意见2意见2意见2意见2意见2意见2意见2意见2意见2意见2意见2意见2意见2意见2意见2"));
			
			Paragraph paragraph = createTab(l);
//			con.add
			document.add(paragraph);
			document.close();
			//设置一个空白页
//			Rectangle rect = new Rectangle(PageSize.A4);  
//			stamper.insertPage(0, rect);
			
//			stamper.close();
//			pdfreader.close();
//			Document document = new Document(stamper.);
//			Document document = new Document(pdfreader.getPageSize(1));
//			document.open();
//			Paragraph context  = new Paragraph();
////			PdfContentByte
////			PdfContentByte overContent = stamper.getOverContent(1);
////			Paragraph context  = new Paragraph();
//			 ElementList elementList =parseToElementList(html, null);
//			 System.out.println(elementList.toString());
//	         for (Element element : elementList) {
//	        	 System.out.println(element.toString());
//	        	 context.add(element);
////	        	 overContent.a
//	         }
//	         context.setSpacingBefore(10f);
//	         System.out.println(context);
//	         document.add(context);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	

	}
	/**
	 * 生成表格
	 * @return
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public static Paragraph createTab(List<LabelBean> list) throws DocumentException, IOException {
		BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		Font fontChinese = new Font(bfChinese, 10.5F, Font.NORMAL);// 五号
		Paragraph ret = new Paragraph();
		PdfPTable table = new PdfPTable(4);
		table.setWidths(new float[] { 0.2f, 0.2f, 0.2f ,0.4f});// 每个单元格占多宽
		table.setWidthPercentage(80f);
//		table.set
		table.addCell(getCell(new Phrase("日期", fontChinese), false, 1, 1));
		table.addCell(getCell(new Phrase("审批", fontChinese), false, 1, 1));
		table.addCell(getCell(new Phrase("人员", fontChinese), false, 1, 1));
		table.addCell(getCell(new Phrase("意见", fontChinese), false, 1, 1));
		Image image = Image.getInstance("C:/Users/Administrator/Desktop/img/audit.png");// 图片名称
		for(LabelBean bean : list) {
			//日期
			table.addCell(getCell(new Phrase(bean.getDate(), fontChinese), false, 1, 1));
			//审批
			table.addCell(getCell(new Phrase(bean.getAudit(), fontChinese), false, 1, 1));
			//人员
			table.addCell(image);
			//意见
			table.addCell(getCell(new Phrase(bean.getContent(), fontChinese), false, 1, 1));
		}
		ret.add(table);
		return ret;
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
//		cells.setMinimumHeight(36);
//		cells.MinimumHeight = 36;//设置行高
		return cells;
	}

	
	
	
	
	
	 public static class MyFontsProvider extends XMLWorkerFontProvider {
	        public MyFontsProvider() {
	            super(null, null);
	        }
	 
	        @Override
	        public Font getFont(final String fontname, String encoding, float size, final int style) {
	            try {
	                BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);//中文字体
	                return new Font(bfChinese, size, style);
	            } catch (Exception ex) {
	                return new Font(Font.FontFamily.UNDEFINED, size, style);
	            }
	        }
	    }
	 
	    public static ElementList parseToElementList(String html, String css) throws IOException {
	        // CSS
	        CSSResolver cssResolver = new StyleAttrCSSResolver();
	        if (css != null) {
	            CssFile cssFile = XMLWorkerHelper.getCSS(new ByteArrayInputStream(css.getBytes()));
	            cssResolver.addCss(cssFile);
	        }
	 
	        // HTML
	        MyFontsProvider fontProvider = new MyFontsProvider();
	        CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
	        HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
	        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
	        htmlContext.autoBookmark(false);
	 
	        // Pipelines
	        ElementList elements = new ElementList();
	        ElementHandlerPipeline end = new ElementHandlerPipeline(elements, null);
	        HtmlPipeline htmlPipeline = new HtmlPipeline(htmlContext, end);
	        CssResolverPipeline cssPipeline = new CssResolverPipeline(cssResolver, htmlPipeline);
	 
	        // XML Worker
	        XMLWorker worker = new XMLWorker(cssPipeline, true);
	        XMLParser p = new XMLParser(worker);
	        html = html.replace("<br>", "<br/>").replace("<hr>", "<hr/>").replace("<img>", "").replace("<param>", "").replace("<link>", "");//不支持单独标签
	        p.parse(new ByteArrayInputStream(html.getBytes()));
	        return elements;
	    }

}
