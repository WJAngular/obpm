package cn.myapps.util;

import junit.framework.TestCase;
import cn.myapps.util.pdf.ObpmPdfDocument;

public class PdfUtilTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testDocumentToPDF() throws Exception {
//		String docid = "11de-bc84-49a8124f-86b5-8d552f00803d";
//		String appid = "11de-b6fa-88e06532-8248-c50a4fd06641";
//
//		DocumentProcess docProcess = new DocumentProcessBean(appid);
//		Document doc = (Document) docProcess.doView(docid);

		ObpmPdfDocument obpmDoc = new ObpmPdfDocument("d:/test.pdf", "水印测试");

		obpmDoc.addTitle("一、项目检查表");

		// 加入行
		// Collection items = doc.getItems();
		// for (Iterator iterator = items.iterator(); iterator.hasNext();) {
		// Item item = (Item) iterator.next();
		// obpmDoc.addTextRow(item.getName(),
		// doc.getItemValueAsString(item.getName()));
		// }

		obpmDoc.addPage();
		obpmDoc.addTitle("图片1");
		obpmDoc.addImage("D:/登录窗口-专家版.JPG");
		
		obpmDoc.addPage();
		obpmDoc.addTitle("图片2");
		obpmDoc.addImage("D:/transparency.gif");
		

		obpmDoc.addPage();
		obpmDoc.addTitle("二、项目计划书");
		obpmDoc.addTableStart(1);
		obpmDoc.addCell("lable: name");
		obpmDoc.addTableEnd();

		obpmDoc.addPage();
		obpmDoc.addTitle("三、项目人员名单");

		obpmDoc.addPage();
		obpmDoc.addTitle("四、立项决议文件");

		obpmDoc.close();
	}

	public void testHtmlToPDF() throws Exception {

		// FormProcess formProcess = (FormProcess)
		// ProcessFactory.createProcess(FormProcess.class);
		// DocumentProcess docProcess = new DocumentProcessBean(appid);
		// UserProcess userProcess = (UserProcess)
		// ProcessFactory.createProcess(UserProcess.class);
		//
		// Document doc = (Document) docProcess.doView(docid);
		// String formid = doc.getFormid();
		// Form form = (Form) formProcess.doView(formid);
		// UserVO user = (UserVO) userProcess.doView(userid);
		//
		// WebUser webUser = new WebUser(user);
		// webUser.setApplicationid(appid);

		String html = "";
		html += "<html>";
		html += "<body>";
		// String lasthtml = form.toPdfHtml(doc, new ParamsTable(), webUser, new
		// ArrayList());
		// lasthtml = lasthtml.replaceAll("&nbsp;", "");
		// lasthtml = lasthtml.replaceAll("&nbsp", "");
		// html += lasthtml;
		html += "</body>";
		html += "</html>";

		html = "<!DOCTYPE html[<!ENTITY nbsp \" \">]>";
		html += "<html>";
		html += "<body>";
		//		
		html += "<TABLE borderColor='#c0c0c0' cellSpacing='1' cellPadding='1' width='100%' border='1'>";
		html += "<TBODY>";
		html += "<TR>";
		html += "<TD colSpan='2'>name: <SPAN style=\"FONT-SIZE: 9pt\">GGGGG</SPAN>&nbsp; age: <SPAN style=\"FONT-SIZE: 9pt\">30</SPAN>&nbsp;<SPAN style=\"FONT-SIZE: 9pt\">6.0</SPAN>&nbsp;gender: 男的</TD></TR>";
		html += "<TR>";
		html += "<TD colSpan='2'>";
		html += "<P><table><tr><td>姓名</td><td>上传</td><td>图片上传</td></tr><tr><td>GGGGG</td><td>令脑贵庚？.xls</td><td>untitled.bmp</td></tr></table></P></TD></TR>";
		html += "<TR>";
		html += "<TD colSpan='2'>&nbsp;天数: <SPAN style=\"FONT-SIZE: 9pt\">20</SPAN> &nbsp; 性别：<SPAN style=\"FONT-SIZE: 9pt\"></SPAN>男;&nbsp;爱好：<SPAN style=\"FONT-SIZE: 9pt\">篮球;羽毛球;</SPAN>公司：<SPAN style=\"FONT-SIZE: 9pt\"></SPAN><SPAN style=\"FONT-SIZE: 9pt\">公司</SPAN>&nbsp;&nbsp;部门：<SPAN style=\"FONT-SIZE: 9pt\"></SPAN><SPAN style=\"FONT-SIZE: 9pt\">测试部</SPAN><SPAN style=\"FONT-SIZE: 9pt\">人事部</SPAN><SPAN style=\"FONT-SIZE: 9pt\">开发部</SPAN></TD></TR>";
		html += "<TR>";
		html += "<TD colSpan='2'>备注：&nbsp;<SPAN style=\"FONT-SIZE: 9pt\">0.0</SPAN>&nbsp;</TD></TR>";
		html += "<TR>";
		html += "<TD>";
		html += "<P>文件上传： <SPAN style=\"FONT-SIZE: 9pt\">令脑贵庚？.xls</SPAN> </P>";
		html += "<P>图片上传：<img border='0' width='100' height='40' src='D:\\java\\workspace\\obpm\\src\\main\\webapp\\/uploads/item/11de-b730-841a1ad0-aaec-e539445ba127.bmp' /></P></TD>";
		html += "<TD>tttttttttttt</TD>";
		html += "</TR>";
		html += "</TBODY></TABLE>日期：<SPAN style=\"FONT-SIZE: 9pt\">2009.10.29</SPAN>&nbsp;";
		html += "</body>";
		html += "</html>";
		// PdfUtil.htmlToPDF(html, new FileOutputStream("d:/test.pdf"));
	}

}
