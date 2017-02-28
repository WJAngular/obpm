//package net.gdsc.util;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//
//import com.lowagie.text.Document;
//import com.lowagie.text.DocumentException;
//import com.lowagie.text.Element;
//import com.lowagie.text.Font;
//import com.lowagie.text.PageSize;
//import com.lowagie.text.Paragraph;
//import com.lowagie.text.pdf.BaseFont;
//import com.lowagie.text.pdf.PdfPCell;
//import com.lowagie.text.pdf.PdfPRow;
//import com.lowagie.text.pdf.PdfPTable;
//import com.lowagie.text.pdf.PdfWriter;
//
//public class PdfDefined {
//
//	/** The resulting PDF file. */
//	public static final String RESULT = "d:/hello.pdf";
//
//	public void createPdf(String filename) throws IOException,
//			DocumentException {
//		Date date = new Date();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Document doc = new Document(PageSize.A4);
//		// 文档路径
//		PdfWriter.getInstance(doc, new FileOutputStream(RESULT));
//		// 文档标题
//		doc.addTitle("惠州市海洋与渔业局1");
//		// 文档主题
//		doc.addSubject("惠州市水产良种推进工程项目合同书");
//		// 文档作者
//		doc.addAuthor("CC");
//		// 文档创建人
//		doc.addCreator("CC");
//		// 关键字
//		doc.addKeywords("惠州市水产良种推进工程项目合同书");
//		doc.open();
//		// step 4
//		// 标题字体
//		BaseFont bfTitle = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
//				BaseFont.NOT_EMBEDDED);
//		Font titleFont = new Font(bfTitle, 26, Font.BOLD);
//
//		// 内容字体
////		BaseFont bfComic = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
////				BaseFont.NOT_EMBEDDED);
//		BaseFont bfComic = BaseFont.createFont("C:\\Windows\\Fonts\\SIMSUN.TTC,1", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//		Font font = new Font(bfComic, 14, Font.NORMAL);
//		Paragraph bank_par = new Paragraph("\n\n\n\n", font);
//		doc.add(bank_par);
//
//		Paragraph fileRefno = new Paragraph("文件编号：111111111111\n\n", font);
//		fileRefno.setAlignment(fileRefno.ALIGN_RIGHT);
//		doc.add(fileRefno);
//		Paragraph titleP = new Paragraph("惠州市水产良种推进工程项目 \n合   同   书\n\n\n",
//				titleFont);
//		titleP.setAlignment(titleP.ALIGN_CENTER);
//		doc.add(titleP);
//		// 生成4列的表格
//		PdfPTable table = new PdfPTable(4);
//		table.setHorizontalAlignment(2);
//		table.getDefaultCell().setVerticalAlignment(3);
//		table.setWidthPercentage(90);
//		int width[] = {13,16,9,12};//设置每列宽度比例   
//        table.setWidths(width);  
//
//		table.addCell(new Paragraph("项目名称", font));
//		PdfPCell cell = new PdfPCell(new Paragraph("XXXXXXXXX", font));
//		cell.setColspan(3);
//		table.addCell(cell);
//
//		// 添加第一行
//		table.addCell(new Paragraph("承担单位：", font));
//		cell = new PdfPCell(new Paragraph("广东思程科技有限公司", font));
//		cell.setColspan(3);
//		table.addCell(cell);
//
//		// 添加第二行
//		table.addCell(new Paragraph("项目负责人:", font));
//		table.addCell(new Paragraph("小张张", font));
//		table.addCell(new Paragraph("手机:", font));
//		table.addCell(new Paragraph("12345678911", font));
//
//		// 添加第8行
//		table.addCell(new Paragraph("单位联系电话:", font));
//		cell = new PdfPCell(new Paragraph("0752-2212345", font));
//		cell.setColspan(3);
//		table.addCell(cell);
//
//		// 添加第9行
//		table.addCell(new Paragraph("主管部门(公章):", font));
//		cell = new PdfPCell(new Paragraph("111111", font));
//		cell.setVerticalAlignment(Element.ALIGN_LEFT);
//		cell.setColspan(3);
//		table.addCell(cell);
//
//		// 添加第10行
//		table.addCell(new Paragraph("起止时间:", font));
//		cell = new PdfPCell(new Paragraph("2014-12-12,2015-12-12", font));
//		cell.setColspan(3);
//		table.addCell(cell);
//		// 添加第11行
//		table.addCell(new Paragraph("通讯地址：", font));
//		table.addCell(new Paragraph("广东惠州惠城区", font));
//		table.addCell(new Paragraph("邮政编码:", font));
//		table.addCell(new Paragraph("516000", font));
//
//		for (PdfPRow row : (ArrayList<PdfPRow>) table.getRows()) {
//			for (PdfPCell cells : row.getCells()) {
//				if (cells != null) {
//					cells.setPadding(8.0f);
//					cells.setBorderWidth(0);
//				}
//			}
//		}
//
//		doc.add(table);
//
//		doc.add(bank_par);
//
//		BaseFont bf_booder = BaseFont.createFont("STSong-Light",
//				"UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
//		Font bf_booderFont = new Font(bf_booder, 18, Font.NORMAL);
//		Paragraph FirstPage_booder = new Paragraph("惠州市海洋与渔业局 \n"
//				+ sdf.format(date) + " 制\n\n", bf_booderFont);
//		FirstPage_booder.setAlignment(FirstPage_booder.ALIGN_CENTER);
//		doc.add(FirstPage_booder);
//
//		// -----------------第二页----------------------------------------------------
//		doc.newPage();
//
//		doc.add(bank_par);
//		Font Sec_titleFont = new Font(bfTitle, 18, Font.BOLD);
//		Paragraph Sec_title = new Paragraph("有   关   说   明\n\n", Sec_titleFont);
//		Sec_title.setAlignment(Sec_title.ALIGN_CENTER);
//		doc.add(Sec_title);
//
//		Font font2 = new Font(bfComic, 14, Font.NORMAL);
//		Paragraph Sec_content = new Paragraph(
//				"    一、合同书各内容要实事求是，逐项认真填写。表达要明确、严谨，字迹要清楚易辨，外来语要同时用原文和中文表达。",
//				font2);
//		Sec_content.setIndentationLeft(70);
//		Sec_content.setIndentationRight(55);
//		Sec_content.setSpacingAfter(10);
//		Sec_content.setLeading(35);
//		doc.add(Sec_content);
//
//		Paragraph Sec_content2 = new Paragraph(
//				"    二、各栏空格不够时，可自行加页。", font2);
//		Sec_content2.setIndentationLeft(70);
//		Sec_content2.setSpacingAfter(10);
//		Sec_content2.setIndentationRight(55);
//		doc.add(Sec_content2);
//
//		Paragraph Sec_content3 = new Paragraph(
//				"    三、合同书为A4开本，正文打印，左侧装订。合同书一式3份，并同时报电子版至市海洋与渔业局渔业科。",
//				font2);
//		Sec_content3.setIndentationLeft(70);
//		Sec_content3.setIndentationRight(55);
//		Sec_content3.setSpacingAfter(10);
//		doc.add(Sec_content3);
//
//		// ----------------------第三页-----------------------------------------------
//		doc.newPage();
//
//		Paragraph Thr_title = new Paragraph("一、基本信息\n\n", Sec_titleFont);
//		Thr_title.setIndentationLeft(50);
//		doc.add(Thr_title);
//
//		PdfPTable table_3 = new PdfPTable(6);
//		table_3.setHorizontalAlignment(2);
//		table_3.getDefaultCell().setVerticalAlignment(3);
//		table_3.setWidthPercentage(100);
//		int width3[] = {12,11,9,11,6,12};//设置每列宽度比例   
//		table_3.setWidths(width3);
//		table_3.addCell(new Paragraph("项目名称", font));
//		PdfPCell cell_3 = new PdfPCell(new Paragraph("XXXXXXXXX", font));
//		cell_3.setColspan(5);
//		table_3.addCell(cell_3);
//
//		table_3.addCell(new Paragraph("单位名称", font));
//		cell_3 = new PdfPCell(new Paragraph("广东思程科技有限公司", font));
//		cell_3.setColspan(5);
//		table_3.addCell(cell_3);
//
//		table_3.addCell(new Paragraph("单位性质", font));
//		cell_3 = new PdfPCell(new Paragraph("私企", font));
//		cell_3.setColspan(2);
//		table_3.addCell(cell_3);
//		table_3.addCell(new Paragraph("邮政编码", font));
//		cell_3 = new PdfPCell(new Paragraph("516000", font));
//		cell_3.setColspan(2);
//		table_3.addCell(cell_3);
//
//		table_3.addCell(new Paragraph("通讯地址", font));
//		cell_3 = new PdfPCell(new Paragraph("惠州市惠城区江北云山西路2号帝景商务国际大厦C718", font));
//		cell_3.setColspan(5);
//		table_3.addCell(cell_3);
//
//		table_3.addCell(new Paragraph("县级主管部门", font));
//		table_3.addCell(new Paragraph("渔业局", font));
//		table_3.addCell(new Paragraph("联系人", font));
//		table_3.addCell(new Paragraph("AAAA", font));
//		table_3.addCell(new Paragraph("电话", font));
//		table_3.addCell(new Paragraph("0752-1234567", font));
//
//		table_3.addCell(new Paragraph("市级主管部门", font));
//		table_3.addCell(new Paragraph("渔业局", font));
//		table_3.addCell(new Paragraph("联系人", font));
//		table_3.addCell(new Paragraph("AAAA", font));
//		table_3.addCell(new Paragraph("电话", font));
//		table_3.addCell(new Paragraph("0752-1234567", font));
//
//		table_3.addCell(new Paragraph("项目负责人", font));
//		table_3.addCell(new Paragraph("aaa", font));
//		table_3.addCell(new Paragraph("职务职称", font));
//		table_3.addCell(new Paragraph("科长", font));
//		table_3.addCell(new Paragraph("电话", font));
//		table_3.addCell(new Paragraph("0752-1234567", font));
//
//		cell_3 = new PdfPCell(new Paragraph("项目联系人", font));
//		cell_3.setBorderWidthBottom(0);
//		cell_3.setBorderWidthTop(0);
//		cell_3.setBorderWidthRight(0);
//		table_3.addCell(cell_3);
//		cell_3 = new PdfPCell(new Paragraph("aa", font));
//		cell_3.setBorderWidthBottom(0);
//		cell_3.setBorderWidthTop(0);
//		cell_3.setBorderWidthRight(0);
//		table_3.addCell(cell_3);
//		table_3.addCell(new Paragraph("职务职称", font));
//		table_3.addCell(new Paragraph("科长", font));
//		table_3.addCell(new Paragraph("电话", font));
//		table_3.addCell(new Paragraph("0752-1234567", font));
//
//		cell_3 = new PdfPCell(new Paragraph("", font));
//		cell_3.setBorderWidthTop(0);
//		cell_3.setBorderWidthRight(0);
//		cell_3.setBorderWidthBottom(0);
//		table_3.addCell(cell_3);
//		cell_3 = new PdfPCell(new Paragraph("", font));
//		cell_3.setBorderWidthTop(0);
//		cell_3.setBorderWidthBottom(0);
//		cell_3.setBorderWidthRight(0);
//		table_3.addCell(cell_3);
//		table_3.addCell(new Paragraph("手机", font));
//		table_3.addCell(new Paragraph("11234566783", font));
//		table_3.addCell(new Paragraph("传真", font));
//		table_3.addCell(new Paragraph("0752-1234567", font));
//
//		table_3.addCell(new Paragraph("项目实施地点", font));
//		cell_3 = new PdfPCell(new Paragraph("惠州市惠城区江北云山西路2号帝景商务国际大厦C718", font));
//		cell_3.setColspan(5);
//		table_3.addCell(cell_3);
//
//		table_3.addCell(new Paragraph("项目建设内容及规模", font));
//		cell_3 = new PdfPCell(new Paragraph("惠州市惠城区江北云山西路2号帝景商务国际大厦C718", font));
//		cell_3.setMinimumHeight(80);
//		cell_3.setColspan(5);
//		table_3.addCell(cell_3);
//
//		cell_3 = new PdfPCell(new Paragraph("资金投入", font));
//		cell_3.setBorderWidthBottom(0);
//		cell_3.setBorderWidthRight(0);
//		table_3.addCell(cell_3);
//		cell_3 = new PdfPCell(new Paragraph("总资金（万元）", font));
//		cell_3.setColspan(2);
//		table_3.addCell(cell_3);
//		cell_3 = new PdfPCell(new Paragraph("1000万元", font));
//		cell_3.setColspan(3);
//		table_3.addCell(cell_3);
//
//		cell_3 = new PdfPCell(new Paragraph("", font));
//		cell_3.setBorderWidthBottom(0);
//		cell_3.setBorderWidthTop(0);
//		cell_3.setBorderWidthRight(0);
//		table_3.addCell(cell_3);
//		cell_3 = new PdfPCell(new Paragraph("其", font));
//		cell_3.setBorderWidthBottom(0);
//		cell_3.setBorderWidthTop(0);
//		cell_3.setBorderWidthRight(0);
//		table_3.addCell(cell_3);
//		table_3.addCell(new Paragraph("财政资金", font));
//		cell_3 = new PdfPCell(new Paragraph("1000万元", font));
//		cell_3.setColspan(3);
//		table_3.addCell(cell_3);
//
//		cell_3 = new PdfPCell(new Paragraph("", font));
//		cell_3.setBorderWidthTop(0);
//		cell_3.setBorderWidthRight(0);
//		table_3.addCell(cell_3);
//		cell_3 = new PdfPCell(new Paragraph("中", font));
//		cell_3.setBorderWidthTop(0);
//		cell_3.setBorderWidthRight(0);
//		table_3.addCell(cell_3);
//		table_3.addCell(new Paragraph("单位投入资金", font));
//		cell_3 = new PdfPCell(new Paragraph("1000万元", font));
//		cell_3.setColspan(3);
//		table_3.addCell(cell_3);
//
//		table_3.addCell(new Paragraph("项目计划完成绩效指标", font));
//		cell_3 = new PdfPCell(new Paragraph("惠州市惠城区江北云山西路2号帝景商务国际大厦C718", font));
//		cell_3.setColspan(5);
//		cell_3.setMinimumHeight(80);
//		table_3.addCell(cell_3);
//
//		table_3.addCell(new Paragraph("项目建设计划完成的时间", font));
//		cell_3 = new PdfPCell(new Paragraph("惠州市惠城区江北云山西路2号帝景商务国际大厦C718", font));
//		cell_3.setColspan(5);
//		cell_3.setMinimumHeight(80);
//		table_3.addCell(cell_3);
//
//		for (PdfPRow row : (ArrayList<PdfPRow>) table_3.getRows()) {
//			for (PdfPCell cells : row.getCells()) {
//				if (cells != null) {
//					cells.setPadding(8.0f);
//				}
//			}
//		}
//		doc.add(table_3);
//
//		// -----------------第四页----------------------------------------------------
//		doc.newPage();
//
//		Paragraph Four_title = new Paragraph("二、财政专项资金使用计划\n\n", Sec_titleFont);
//		Four_title.setIndentationLeft(50);
//		doc.add(Four_title);
//
//		PdfPTable table_4 = new PdfPTable(1);
//		table_4.setHorizontalAlignment(2);
//		table_4.getDefaultCell().setVerticalAlignment(3);
//		table_4.setWidthPercentage(100);
//		table_4.getDefaultCell().setMinimumHeight(700);
//
//		table_4.addCell(new Paragraph("甲方安排给乙方财政专项资金_万元，主要用于：", font));
//		for (PdfPRow row : (ArrayList<PdfPRow>) table_4.getRows()) {
//			for (PdfPCell cells : row.getCells()) {
//				if (cells != null) {
//					cells.setPadding(8.0f);
//				}
//			}
//		}
//		doc.add(table_4);
//		// -----------------第五页----------------------------------------------------
//		doc.newPage();
//
//		Paragraph five_title = new Paragraph("\n三、责任\n", Sec_titleFont);
//		five_title.setIndentationLeft(50);
//		doc.add(five_title);
//
//		Font font5 = new Font(bfComic, 14, Font.NORMAL);
//		Paragraph first_content_5 = new Paragraph(
//				"    （一）乙方的法定代表人为项目第一责任人，对项目的建设和资金的使用负总责。\r"
//						+ "     （二）在项目建设和资金的使用过程中，乙方承诺严格执行市财政局、市海洋与渔业局有关财政专项资金管理的有关规定，并保证：\r"
//						+ "     1、严格按项目建设计划实施，按市财政局和市海洋与渔业局下达的项目建设内容进行项目建设，未经市财政局和市海洋与渔业局批准不得改变建设地点、建设内容和规模、建设目标等。\r"
//						+ "     2、所有项目资金的使用不超出本合同所规定的范围，年度专项资金的使用不超出市财政局和市海洋与渔业局下达计划时所核定的范围。\r"
//						+ "     3、专项资金按规定实行专帐管理，遵守有关财务制度和现金使用规定，保证专款专用。\r"
//						+ "     4、按有关规定及甲方要求及时上报项目建设进度和资金使用情况。\r"
//						+ "     （三）在项目建设和专项资金使用过程中，乙方如有下列情形的，甲方将按规定对项目进行整改、追究有关人员的责任；情节严重的，停止项目实施，追回项目资金，并移交司法部门处理：\r"
//						+ "     1、违反国家法律、法规和财经纪律，不执行（粤财农〔2009〕218号）文精神；\r",
//				font5);
//		first_content_5.setIndentationLeft(60);
//		first_content_5.setIndentationRight(55);
//		first_content_5.setLeading(32);
//		doc.add(first_content_5);
//
//		doc.newPage();
//		Paragraph first_content_6 = new Paragraph(
//				  "    2、不履行本合同所承诺的责任和义务；\r"
//				+ "     3、结算手续不完备、支付审批手续不规范；\r"
//				+ "     4、弄虚作假骗取资金拨款；挤占、截留、挪用资金等。\r"
//				+ "     （四）本责任书一式三份，甲、乙双方各执一份，县（区）渔业主管部门存档一份。\r\r\r",
//				font5);
//		first_content_6.setIndentationLeft(60);
//		first_content_6.setIndentationRight(55);
//		first_content_6.setLeading(35);
//		doc.add(first_content_6);
//		
//		
//		Paragraph Sec_content_6 = new Paragraph(
//				  "       甲方：                       代表人：\r\r\r\r\r"
//				+ "       乙方：                        法人代表：\r\r\r",
//				font5);
//		Sec_content_6.setIndentationLeft(60);
//		Sec_content_6.setIndentationRight(55);
//		Sec_content_6.setLeading(35);
//		doc.add(Sec_content_6);
//		
//		Paragraph Thr_content_6 = new Paragraph("年     月      日",
//				font5);
//		Thr_content_6.setIndentationLeft(60);
//		Thr_content_6.setIndentationRight(55);
//		Thr_content_6.setAlignment(Element.ALIGN_RIGHT);
//		doc.add(Thr_content_6);
//	
//
//		doc.close();
//	}
//
//	/**
//	 * 给pdf文件添加水印
//	 * 
//	 * @param InPdfFile
//	 *            要加水印的原pdf文件路径
//	 **/
//	private static void addPdfMark(String InPdfFile) throws Exception {
////		// 图片水印
////		PdfReader reader = new PdfReader(InPdfFile);
////		PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(
////				"D:/TEST.PDF"));
////		Image img = Image.getInstance("Images/shuiyin.png");
////		img.setAbsolutePosition(0, 0);
////		PdfContentByte under = null;
////		for (int i = 1; i <= reader.getNumberOfPages(); i++) {
////			under = stamp.getUnderContent(i);
////			under.addImage(img);
////		}
////
////		stamp.close();
////		reader.close();
//
//	}
//
//	public static void main(String[] args) throws IOException,
//			DocumentException {
//		new PdfDefined().createPdf(RESULT);
//		try {
//			new PdfDefined();
//			PdfDefined.addPdfMark(RESULT);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	public void ExportPdf() throws IOException, DocumentException{
//		new PdfDefined().createPdf(RESULT);
//	}
//	
//}
