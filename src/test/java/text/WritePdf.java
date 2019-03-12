package text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class WritePdf {
	public static void main(String[] args) throws IOException {
//		imageTest();
//		updatePdf();
		String path = "E:/java/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/tztfDoc/temp/doc/html8e4251d1b0a3dc172e3a7974780ae1fc.pdf";   // PDF 的输出位置
		String imagePath = "C:/Users/Administrator/Desktop/img/audit.png";
		addSeal(16,path,imagePath,"E:/java/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/tztfDoc/temp/doc/b.pdf");
//		Document document = new Document();
////		PdfWriter pdfWriter = new PdfWriter();
//		try {
//			PdfWriter.getInstance(document, new FileOutputStream(new File("E:/java/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/tztfDoc/temp/docx/pdf/70098e2eca12c5b67386267528520c18.pdf")));
//			document.open();
//			document.add(new Paragraph("Hello World"));  
//		    //Step 5—Close the Document.  
//		    document.close();
//		} catch (FileNotFoundException | DocumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	public static void updatePdf() {
		//pdf文件位置
		String path = "E:/java/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/tztfDoc/temp/doc/html8e4251d1b0a3dc172e3a7974780ae1fc.pdf";   // PDF 的输出位置
		
        try {
        	// 读入PDF
			PdfReader pdfReader = new PdfReader(path);
			//计算pdf 页数
			int page = pdfReader.getNumberOfPages();
			System.out.println("页数："+page);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			syso
			e.printStackTrace();
		}
	
	}
	
	
	/**
	 * 给PDF上添加图片
	 * @param 指定要添加图片的pdf页
	 * @param savePdf原PDF路径
	 * @param savePng原图片路径
	 * @param sealPdf新生成PDF路径
	 * @throws IOException
	 */
	public static void addSeal(int page, String savePdf, String savePng,
			String sealPdf) throws IOException {
		int marginLeft = 30;// 左边距
		int marginBottom = 100;// 底边距
		PdfReader pdfreader = new PdfReader(savePdf);
		// 获得PDF总页数
		int pdfPage = pdfreader.getNumberOfPages();
		if (page <= 0 || page > pdfPage) {
			System.out.println("pdf文件无当前页");
		}
		// 获取指定页的宽和高
		Document document = new Document(pdfreader.getPageSize(page));
		// 获取页面宽度
		float width = document.getPageSize().getWidth();
		// 获取页面高度
		float height = document.getPageSize().getHeight();
		if (pdfreader != null)
			pdfreader.close();
		if (document != null)
			document.close();
		System.out.println("pdfPage=" + pdfPage + ",width = " + width
				+ ", height = " + height);
		PdfReader pdf = new PdfReader(savePdf);
		PdfStamper stamper = null;
		try {
			stamper = new PdfStamper(pdf, new FileOutputStream(sealPdf));// 生成的PDF
			PdfContentByte overContent = stamper.getOverContent(page);
			Image image = Image.getInstance(savePng);// 图片名称
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
	
	
	
	
	
	
	
	
	
	
	
	
	public static void imageTest(){
//        Rectangle rectangle = new Rectangle(PageSize.A4);       // 设置 PDF 纸张矩形，大小采用 A4
//        rectangle.setBackgroundColor(BaseColor.ORANGE);       // 设置背景色
        //创建一个文档对象，设置初始化大小和页边距
        Document document = new Document();     // 上、下、左、右页间距

        String pdfPath = "E:/java/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/tztfDoc/temp/docx/pdf/47bceb52d27292997b62a9365ebae19d.pdf";   // PDF 的输出位置
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
        } catch (DocumentException e) {
//            log.error("将文档对象设置到文件输出流中 - 出错了！", e);
        } catch (FileNotFoundException e) {
//            log.error("未找到指定的文件！", e);
        }
        document.open();    // 打开文档对象

        String imagePath = "C:/Users/Administrator/Desktop/img/audit.png";      // 图片的绝对路径
        Image image = null;     // 声明图片对象
        try {
			image = Image.getInstance(imagePath);
		} catch (BadElementException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        image.scaleAbsolute(image.getScaledWidth(),image.getScaledHeight());
//        image.scaleAbsolute(ImageUtil.getImageWidth(imagePath), ImageUtil.getImageHeight(imagePath));
        image.setAbsolutePosition(10, 20);      // （以左下角为原点）设置图片的坐标

        try {
            document.add(image);
        } catch (DocumentException e) {
//            log.error("将图片对象加入到文档对象中时 - 出错了！", e);
        }

        document.close();       // 关闭文档
    }


	
}
