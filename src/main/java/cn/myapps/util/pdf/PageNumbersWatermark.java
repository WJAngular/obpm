package cn.myapps.util.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

public class PageNumbersWatermark extends PdfPageEventHelper {
	protected BaseFont bfChinese;
	protected String watermark;
	protected PdfGState gstate;

	public PageNumbersWatermark(BaseFont bfChinese, String watermark) {
		this.bfChinese = bfChinese;
		this.watermark = watermark;
	}

	public void onOpenDocument(PdfWriter writer, Document document) {
		super.onOpenDocument(writer, document);

		gstate = new PdfGState();
		gstate.setFillOpacity(0.3f);
		gstate.setStrokeOpacity(0.3f);
	}

	public void onEndPage(PdfWriter writer, Document document) {
		PdfContentByte cb = writer.getDirectContent();
		cb.saveState();
		cb.setGState(gstate);
		cb.beginText();
		cb.setFontAndSize(bfChinese, 48);
		cb.showTextAligned(Element.ALIGN_CENTER, watermark, document.getPageSize().getWidth() / 2, document
				.getPageSize().getHeight() / 2, 45);
		cb.endText();
		cb.restoreState();

	}
}
