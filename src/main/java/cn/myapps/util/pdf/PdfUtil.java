package cn.myapps.util.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;

import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextOutputDevice;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.pdf.ITextUserAgent;
import org.xhtmlrenderer.resource.XMLResource;
import org.xml.sax.InputSource;

import cn.myapps.util.StringUtil;

import com.lowagie.text.DocListener;
import com.lowagie.text.ElementTags;
import com.lowagie.text.html.HtmlParser;
import com.lowagie.text.html.HtmlPeer;
import com.lowagie.text.html.HtmlTagMap;
import com.lowagie.text.html.HtmlTags;
import com.lowagie.text.html.SAXmyHtmlHandler;
import com.lowagie.text.pdf.BaseFont;

public class PdfUtil {

	public static ObpmPdfDocument createDocument(String webFilename,
			String watermark) {
		return new ObpmPdfDocument(webFilename, watermark);
	}

	public static String getTestString() {
		StringBuffer content = new StringBuffer();
		content.append("<html><head>");
		// content
		// .append("<link rel='stylesheet' href='file://F:/work/teemlinkWorkSpace/obpm/src/main/webapp/resource/css/main-front.css'/>");
		content.append("</head><body>");

		content.append("</body></html>");
		return content.toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// args = new String[] { "D:/PDFRenderStr.html", "D:/PDFRenderStr.pdf"
		// };
		File pdf = new File("test.pdf");
		if (!pdf.exists()) {
			pdf.createNewFile();
		}
		OutputStream os = new FileOutputStream(pdf);
		htmlToPDF(getTestString(), os);
	}

	public static void htmlToPDF(String html, OutputStream os) throws Exception {
		// html = getHtmlStr();
		// String pdf="D:/obpm.pdf";
		// html = "<?xml version=\"1.0\" encoding=\"GB2312\"?>" + html;
		html = encodeLatin(html);  //转义拉丁字符,特殊字符
		html = html.replace("&nbsp;", "");
		html = html.replace("rowSpan", "rowspan");
		html = html.replace("colSpan", "colspan");
		html = html.replace("<br>", "<br/>");
		html = html.replace("&ldquo;", "&quot;");
		html = html.replace("&rdquo;", "&quot;");
		html = html.replaceAll("&", "&amp;");
		//html = html.replace("//", "/");
		try {
			// os = new FileOutputStream(pdf);
			ITextRenderer renderer = new ITextRenderer();
			ITextFontResolver fontResolver = renderer.getFontResolver();
			fontResolver.addFont(PdfUtil.class.getResource("simsun.ttc")
					.getPath(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			/*
			 * PdfUtil.class.getResource("simsun.ttc")
					.getPath()
			 * standard approach ITextRenderer renderer = new ITextRenderer();
			 * 
			 * renderer.setDocument(url); renderer.layout();
			 * renderer.createPDF(os);
			 */

			ResourceLoaderUserAgent callback = new ResourceLoaderUserAgent(
					renderer.getOutputDevice());
			callback.setSharedContext(renderer.getSharedContext());
			renderer.getSharedContext().setUserAgentCallback(callback);
//			org.w3c.dom.Document doc = XMLResource.load(new StringReader(html))
//					.getDocument();
			// goGB(doc,);
//			renderer.setDocument(doc, html);
			renderer.setDocumentFromString(html);
			renderer.layout();
			renderer.createPDF(os);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("HTML代码不符合标准，请严格遵守XHTML标准编写代码！");
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}

	private static class ResourceLoaderUserAgent extends ITextUserAgent {
		public ResourceLoaderUserAgent(ITextOutputDevice outputDevice) {
			super(outputDevice);
		}

		protected InputStream resolveAndOpenStream(String uri) {
			InputStream is = super.resolveAndOpenStream(uri);
			return is;
		}
	}

	public static String getHtmlStr() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<html>");

		stringBuffer.append("<body><h1>aa中文</h1>");
		stringBuffer.append("<table width='394' height='117' border='1'>");
		stringBuffer.append("<tbody>");
		stringBuffer.append("<tr>");
		stringBuffer.append("<td rowspan='2'>显示中文测试</td>");
		stringBuffer.append("<td>aa</td>");
		stringBuffer.append("<td></td>");
		stringBuffer.append("</tr>");
		stringBuffer.append("<tr>");
		stringBuffer.append("<td></td>");
		stringBuffer.append("<td></td>");
		stringBuffer.append("</tr>");
		stringBuffer.append("<tr>");
		stringBuffer.append("<td></td>");
		stringBuffer.append("<td>aaaaaaa</td>");
		stringBuffer.append("<td>aaaaaaa</td>");
		stringBuffer.append("</tr>");
		stringBuffer.append("</tbody>");
		stringBuffer.append("</table>");
		stringBuffer.append("</body>");

		stringBuffer.append("</html>");

		return stringBuffer.toString();
	}
	
	/**
	 * 转义拉丁字符,特殊字符
	 * @param html
	 * @return
	 */
	private static String encodeLatin(String html){
		if(!StringUtil.isBlank(html)){
			html = html.replaceAll("&iexcl;", "¡");
			html = html.replaceAll("&cent;", "¢");
			html = html.replaceAll("&pound;", "£");
			html = html.replaceAll("&curren;", "¤");
			html = html.replaceAll("&yen;", "¥");
			html = html.replaceAll("&brvbar;", "¦");
			html = html.replaceAll("&sect;", "§");
			html = html.replaceAll("&uml;", "¨");
			html = html.replaceAll("&copy;", "©");
			html = html.replaceAll("&ordf;", "ª");
			html = html.replaceAll("&laquo;", "«");
			html = html.replaceAll("&not;", "¬");
			html = html.replaceAll("&shy;", "­");
			html = html.replaceAll("&reg;", "®");
			html = html.replaceAll("&macr;", "¯");
			html = html.replaceAll("&deg;", "°");
			html = html.replaceAll("&plusmn;", "±");
			html = html.replaceAll("&sup2;", "²");
			html = html.replaceAll("&sup3;", "³");
			html = html.replaceAll("&acute;", "´");
			html = html.replaceAll("&micro;", "µ");
			html = html.replaceAll("&para;", "¶");
			html = html.replaceAll("&middot;", "·");
			html = html.replaceAll("&cedil;", "¸");
			html = html.replaceAll("&sup1;", "¹");
			html = html.replaceAll("&ordm;", "º");
			html = html.replaceAll("&raquo;", "»");
			html = html.replaceAll("&frac14;", "¼");
			html = html.replaceAll("&frac12;", "½");
			html = html.replaceAll("&frac34;", "¾");
			html = html.replaceAll("&iquest;", "¿");
			html = html.replaceAll("&times;", "×");
			html = html.replaceAll("&divide;", "÷");
			html = html.replaceAll("&fnof;", "ƒ");
			html = html.replaceAll("&bull;", "•");
			html = html.replaceAll("&hellip;", "…");
			html = html.replaceAll("&prime;", "′");
			html = html.replaceAll("&Prime;", "″");
			html = html.replaceAll("&oline;", "‾");
			html = html.replaceAll("&frasl;", "⁄");
			html = html.replaceAll("&weierp;", "℘");
			html = html.replaceAll("&image;", "ℑ");
			html = html.replaceAll("&real;", "ℜ");
			html = html.replaceAll("&trade;", "™");
			html = html.replaceAll("&alefsym;", "ℵ");
			html = html.replaceAll("&larr;", "←");
			html = html.replaceAll("&uarr;", "↑");
			html = html.replaceAll("&rarr;", "→");
			html = html.replaceAll("&darr;", "↓");
			html = html.replaceAll("&harr;", "↔");
			html = html.replaceAll("&crarr;", "↵");
			html = html.replaceAll("&lArr;", "⇐");
			html = html.replaceAll("&uArr;", "⇑");
			html = html.replaceAll("&rArr;", "⇒");
			html = html.replaceAll("&dArr;", "⇓");
			html = html.replaceAll("&hArr;", "⇔");
			html = html.replaceAll("&forall;", "∀");
			html = html.replaceAll("&part;", "∂");
			html = html.replaceAll("&exist;", "∃");
			html = html.replaceAll("&empty;", "∅");
			html = html.replaceAll("&nabla;", "∇");
			html = html.replaceAll("&isin;", "∈");
			html = html.replaceAll("&notin;", "∉");
			html = html.replaceAll("&ni;", "∋");
			html = html.replaceAll("&prod;", "∏");
			html = html.replaceAll("&sum;", "∑");
			html = html.replaceAll("&minus;", "−");
			html = html.replaceAll("&lowast;", "∗");
			html = html.replaceAll("&radic;", "√");
			html = html.replaceAll("&prop;", "∝");
			html = html.replaceAll("&infin;", "∞");
			html = html.replaceAll("&ang;", "∠");
			html = html.replaceAll("&and;", "∧");
			html = html.replaceAll("&or;", "∨");
			html = html.replaceAll("&cap;", "∩");
			html = html.replaceAll("&cup;", "");
			html = html.replaceAll("&;", "∪");
			html = html.replaceAll("&int;", "∫");
			html = html.replaceAll("&there4;", "∴");
			html = html.replaceAll("&sim;", "∼");
			html = html.replaceAll("&cong;", "≅");
			html = html.replaceAll("&asymp;", "≈");
			html = html.replaceAll("&ne;", "≠");
			html = html.replaceAll("&le;", "≤");
			html = html.replaceAll("&equiv;", "≡");
			html = html.replaceAll("&ge;", "≥");
			html = html.replaceAll("&sub;", "⊂");
			html = html.replaceAll("&sup;", "⊃");
			html = html.replaceAll("&nsub;", "⊄");
			html = html.replaceAll("&sube;", "⊆");
			html = html.replaceAll("&supe;", "⊇");
			html = html.replaceAll("&oplus;", "⊕");
			html = html.replaceAll("&otimes;", "⊗");
			html = html.replaceAll("&perp;", "⊥");
			html = html.replaceAll("&sdot;", "⋅");
			html = html.replaceAll("&lceil;", "\u2308");
			html = html.replaceAll("&lceil;", "\u2308");
			html = html.replaceAll("&rceil;", "\u2309");
			html = html.replaceAll("&lfloor;", "\u230a");
			html = html.replaceAll("&rfloor;", "\u230b");
			html = html.replaceAll("&lang;", "\u2329");
			html = html.replaceAll("&rang;", "\u232a");
			html = html.replaceAll("&loz;", "◊");
			html = html.replaceAll("&spades;", "♠");
			html = html.replaceAll("&clubs;", "♣");
			html = html.replaceAll("&hearts;", "♥");
			html = html.replaceAll("&diams;", "♦");
			html = html.replaceAll("&circ;", "ˆ");
			html = html.replaceAll("&tilde;", "˜");
			html = html.replaceAll("&ndash;", "–");
			html = html.replaceAll("&mdash;", "—");
			html = html.replaceAll("&lsquo;", "‘");
			html = html.replaceAll("&rsquo;", "’");
			html = html.replaceAll("&sbquo;", "‚");
			html = html.replaceAll("&ldquo;", "“");
			html = html.replaceAll("&rdquo;", "”");
			html = html.replaceAll("&bdquo;", "„");
			html = html.replaceAll("&dagger;", "†");
			html = html.replaceAll("&Dagger;", "‡");
			html = html.replaceAll("&permil;", "‰");
			html = html.replaceAll("&lsaquo;", "‹");
			html = html.replaceAll("&rsaquo;", "›");
			html = html.replaceAll("&euro;", "€");
			html = html.replaceAll("&Agrave;", "À");
			html = html.replaceAll("&Aacute;", "Á");
			html = html.replaceAll("&Acirc;", "Â");
			html = html.replaceAll("&Atilde;", "Ã");
			html = html.replaceAll("&Auml;", "Ä");
			html = html.replaceAll("&Aring;", "Å");
			html = html.replaceAll("&AElig;", "Æ");
			html = html.replaceAll("&Ccedil;", "Ç");
			html = html.replaceAll("&Egrave;", "È");
			html = html.replaceAll("&Eacute;", "É");
			html = html.replaceAll("&Ecirc;", "Ê");
			html = html.replaceAll("&Euml;", "Ë");
			html = html.replaceAll("&Igrave;", "Ì");
			html = html.replaceAll("&Iacute;", "Í");
			html = html.replaceAll("&Icirc;", "Î");
			html = html.replaceAll("&Iuml;", "Ï");
			html = html.replaceAll("&ETH;", "Ð");
			html = html.replaceAll("&Ntilde;", "Ñ");
			html = html.replaceAll("&Ograve;", "Ò");
			html = html.replaceAll("&Oacute;", "Ó");
			html = html.replaceAll("&Ocirc;", "Ô");
			html = html.replaceAll("&Otilde;", "Õ");
			html = html.replaceAll("&Ouml;", "Ö");
			html = html.replaceAll("&Oslash;", "Ø");
			html = html.replaceAll("&Ugrave;", "Ù");
			html = html.replaceAll("&Uacute;", "Ú");
			html = html.replaceAll("&Ucirc;", "Û");
			html = html.replaceAll("&Uuml;", "Ü");
			html = html.replaceAll("&Yacute;", "Ý");
			html = html.replaceAll("&THORN;", "Þ");
			html = html.replaceAll("&szlig;", "ß");
			html = html.replaceAll("&agrave;", "à");
			html = html.replaceAll("&aacute;", "á");
			html = html.replaceAll("&acirc;", "â");
			html = html.replaceAll("&atilde;", "ã");
			html = html.replaceAll("&auml;", "ä");
			html = html.replaceAll("&aring;", "å");
			html = html.replaceAll("&aelig;", "æ");
			html = html.replaceAll("&ccedil;", "ç");
			html = html.replaceAll("&egrave;", "è");
			html = html.replaceAll("&eacute;", "é");
			html = html.replaceAll("&ecirc;", "ê");
			html = html.replaceAll("&euml;", "ë");
			html = html.replaceAll("&igrave;", "ì");
			html = html.replaceAll("&iacute;", "í");
			html = html.replaceAll("&icirc;", "î");
			html = html.replaceAll("&iuml;", "ï");
			html = html.replaceAll("&eth;", "ð");
			html = html.replaceAll("&ntilde;", "ñ");
			html = html.replaceAll("&ograve;", "ò");
			html = html.replaceAll("&oacute;", "ó");
			html = html.replaceAll("&ocirc;", "ô");
			html = html.replaceAll("&otilde;", "õ");
			html = html.replaceAll("&ouml;", "ö");
			html = html.replaceAll("&oslash;", "ø");
			html = html.replaceAll("&ugrave;", "ù");
			html = html.replaceAll("&uacute;", "ú");
			html = html.replaceAll("&ucirc;", "û");
			html = html.replaceAll("&uuml;", "ü");
			html = html.replaceAll("&yacute;", "ý");
			html = html.replaceAll("&thorn;", "þ");
			html = html.replaceAll("&yuml;", "ÿ");
			html = html.replaceAll("&OElig;", "Œ");
			html = html.replaceAll("&oelig;", "œ");
			html = html.replaceAll("&Scaron;", "Š");
			html = html.replaceAll("&scaron;", "š");
			html = html.replaceAll("&Yuml;", "Ÿ");
			html = html.replaceAll("&Alpha;", "Α");
			html = html.replaceAll("&Beta;", "Β");
			html = html.replaceAll("&Gamma;", "Γ");
			html = html.replaceAll("&Delta;", "Δ");
			html = html.replaceAll("&Epsilon;", "Ε");
			html = html.replaceAll("&Zeta;", "Ζ");
			html = html.replaceAll("&Eta;", "Η");
			html = html.replaceAll("&Theta;", "Θ");
			html = html.replaceAll("&Iota;", "Ι");
			html = html.replaceAll("&Kappa;", "Κ");
			html = html.replaceAll("&Lambda;", "Λ");
			html = html.replaceAll("&Mu;", "Μ");
			html = html.replaceAll("&Nu;", "Ν");
			html = html.replaceAll("&Xi;", "Ξ");
			html = html.replaceAll("&Omicron;", "Ο");
			html = html.replaceAll("&Pi;", "Π");
			html = html.replaceAll("&Rho;", "Ρ");
			html = html.replaceAll("&Sigma;", "Σ");
			html = html.replaceAll("&Tau;", "Τ");
			html = html.replaceAll("&Upsilon;", "Υ");
			html = html.replaceAll("&Phi;", "Φ");
			html = html.replaceAll("&Chi;", "Χ");
			html = html.replaceAll("&Psi;", "Ψ");
			html = html.replaceAll("&Omega;", "Ω");
			html = html.replaceAll("&alpha;", "α");
			html = html.replaceAll("&beta;", "β");
			html = html.replaceAll("&gamma;", "γ");
			html = html.replaceAll("&delta;", "δ");
			html = html.replaceAll("&epsilon;", "ε");
			html = html.replaceAll("&zeta;", "ζ");
			html = html.replaceAll("&eta;", "η");
			html = html.replaceAll("&theta;", "θ");
			html = html.replaceAll("&iota;", "ι");
			html = html.replaceAll("&kappa;", "κ");
			html = html.replaceAll("&lambda;", "λ");
			html = html.replaceAll("&mu;", "μ");
			html = html.replaceAll("&nu;", "ν");
			html = html.replaceAll("&xi;", "ξ");
			html = html.replaceAll("&omicron;", "ο");
			html = html.replaceAll("&pi;", "π");
			html = html.replaceAll("&rho;", "ρ");
			html = html.replaceAll("&sigmaf;", "ς");
			html = html.replaceAll("&sigma;", "σ");
			html = html.replaceAll("&tau;", "τ");
			html = html.replaceAll("&upsilon;", "υ");
			html = html.replaceAll("&phi;", "φ");
			html = html.replaceAll("&chi;", "χ");
			html = html.replaceAll("&psi;", "ψ");
			html = html.replaceAll("&omega;", "ω");
			html = html.replaceAll("&thetasym;", "\u03d1");
			html = html.replaceAll("&upsih;", "\u03d2");
			html = html.replaceAll("&piv;", "\u03d6");
		}
		return html;
	}
}

/**
 * The inner class extend the com.lowagie.text.html.HtmlParser, its purpose is
 * to support the Chinese.
 */
class ITextSurportHtmlParser extends HtmlParser {
	public ITextSurportHtmlParser() {
		super();
	}

	public void goGB(DocListener document, InputStream is) {
		try {
			// BaseFont bfChinese = BaseFont.createFont(
			// "c:\\windows\\fonts\\simsun.ttc,1", BaseFont.IDENTITY_H,
			// BaseFont.EMBEDDED);
			BaseFont bfChinese = BaseFont.createFont("STSong-Light",
					"UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);

			// 去除p
			HtmlTagMap myTags = new HtmlTagMap();
			HtmlPeer peer = new HtmlPeer(ElementTags.PARAGRAPH,
					HtmlTags.PARAGRAPH);
			myTags.remove(peer.getAlias());

			parser.parse(new InputSource(is), new SAXmyHtmlHandler(document,
					bfChinese));
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}
}