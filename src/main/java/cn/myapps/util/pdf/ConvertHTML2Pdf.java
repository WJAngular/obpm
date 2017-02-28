package cn.myapps.util.pdf;

import java.awt.Insets;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URL;

import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.zefer.pd4ml.PD4Constants;
import org.zefer.pd4ml.PD4ML;

import com.lowagie.text.pdf.BaseFont;

/**
 * HTMl转换pdf文件
 * @author Happy
 *
 */
public class ConvertHTML2Pdf {
	
	
	/**
	 * 生成pdf
	 * @param HTML
	 * @param outputstream
	 * @throws Exception
	 */
	public void generatePDF(String html, OutputStream outputstream) throws Exception{
		PD4ML pd4ml = getPD4ML();
        pd4ml.render(new StringReader(html), outputstream);
	}	
	
	/**
	 * 生成pdf
	 * @param HTML
	 * @param outputstream
	 * @throws Exception
	 */
	public void generatePDFByFlyingSaucer(String html, OutputStream outputstream) throws Exception{
		ITextRenderer renderer = getITextRenderer();
		renderer.setDocumentFromString(html);
		renderer.layout();
		renderer.createPDF(outputstream);
	}
	
	/**
	 * 生成pdf
	 * @param url
	 * @param outputstream
	 * @throws Exception
	 */
	public void generatePDF(URL url, OutputStream outputstream) throws Exception{
		PD4ML pd4ml = getPD4ML();
		pd4ml.render(url, outputstream);
	}
	
	
	private ITextRenderer getITextRenderer() throws Exception{
		ITextRenderer renderer = new ITextRenderer();
		ITextFontResolver fontResolver = renderer.getFontResolver();
		fontResolver.addFont(ConvertHTML2Pdf.class.getResource("simsun.ttc").getPath(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		return renderer;
	}
	
	private PD4ML getPD4ML() throws FileNotFoundException{
		PD4ML pd4ml = new PD4ML();
		pd4ml.setPageInsets(new Insets(20, 10, 10, 10));
		pd4ml.setPageInsetsMM(new Insets(5, 5, 5, 5)); 
		pd4ml.setHtmlWidth(950);
		pd4ml.setPageSize(pd4ml.changePageOrientation(PD4Constants.A4));
		pd4ml.useTTF("java:fonts", true);//字体库
		pd4ml.setDefaultTTFs("SimHei", "YouYuan", "KaiTi");//设置默认字体
//		pd4ml.enableDebugInfo();
		//pd4ml.addStyle("BODY {margin: 0}", true);
		return pd4ml;
	}
	
	public static void main(String[] args) {
		 StringBuffer html = new StringBuffer();
	 	html.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"> \n");
        html.append("<html>")
            .append("<head>")
            .append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />")
            .append("</head>")
            .append("<body>哈哈哈")
            //.append("<font face=\"KaiTi_GB2312\">")
            .append("<span style=\"font-family: Arial\">备     注</span>")
            .append("</font>")
            .append("</body></html>");
	      File file = new File("E:\\_demo.pdf");
	      
	      try {
			FileOutputStream os = new FileOutputStream(file);
			new ConvertHTML2Pdf().generatePDF(html.toString(), os);
			os.flush();
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
