package cn.myapps.core.dynaform.printer.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.core.dynaform.form.ejb.AttachmentUploadField;
import cn.myapps.core.dynaform.form.ejb.AttachmentUploadToDataBaseField;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormField;
import cn.myapps.core.dynaform.form.ejb.IncludeField;
import cn.myapps.core.dynaform.form.ejb.InputField;
import cn.myapps.core.dynaform.form.ejb.TabField;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.dynaform.view.ejb.Column;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.dynaform.view.ejb.editmode.DesignEditMode;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.DateUtil;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.cache.MemoryCacheUtil;

/**
 * @author Happy
 * 
 */

public class PrinterUtils {
	private static PrinterUtils printerUtils;

	public PrinterUtils() {

	}

	public static PrinterUtils getInstance() {
		if (printerUtils == null)
			printerUtils = new PrinterUtils();
		return printerUtils;
	}

	/**
	 * 返回 包含表单字段的XML字符串
	 * 
	 * @param fields
	 *            表单字段的map集合
	 * @return
	 */
	public String getFields(Map<String, String> fields) {
		StringBuffer strXML = new StringBuffer();
		strXML.append("<fields>\n");
	    Set<Map.Entry<String, String>> set = fields.entrySet();
        for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
            //System.out.println(entry.getKey() + "--->" + entry.getValue());
            String value = entry.getKey();
            if(value.startsWith("$")){
            	value =decorateFieldName(value);
            }
            strXML.append("<field name=\"" + value + "\"/>\n");
        }
		strXML.append("</fields>");
		return strXML.toString();
	}
	
	private String decorateFieldName(String fieldName){
		String rtn ="";
		if(fieldName.equalsIgnoreCase("$Id")){
			rtn = fieldName+"(表单ID)";
		}else if(fieldName.equalsIgnoreCase("$StateLabel")){
			rtn = fieldName+"(表单状态标签)";
		}else if(fieldName.equalsIgnoreCase("$AuditDate")){
			rtn = fieldName+"(表单审批日期)";
		}else if(fieldName.equalsIgnoreCase("$LastModified")){
			rtn = fieldName+"(表单最后修改时间)";
		}else if(fieldName.equalsIgnoreCase("$Created")){
			rtn = fieldName+"(表单创建日期)";
		}else if(fieldName.equalsIgnoreCase("$Author")){
			rtn = fieldName+"(表单作者)";
		}else if(fieldName.equalsIgnoreCase("$AuditorNames")){
			rtn = fieldName+"(表单审批人名称)";
		}else if(fieldName.equalsIgnoreCase("$LastFlowOperation")){
			rtn = fieldName+"(表单最后流程操作)";
		}else if(fieldName.equalsIgnoreCase("$FormName")){
			rtn = fieldName+"(表单名称)";
		}
		
		return rtn;
		
	}
	
	
	/**返回 包含表单子视图的XML字符串
	 * @param views
	 * @return
	 */
	public String getSubViews(Map<String, String> views) {
		org.dom4j.Document document = DocumentHelper.createDocument();
		Element subView = document.addElement("subView");
		Iterator<String> keys = views.keySet().iterator();
		Iterator<String> values = views.values().iterator();
		for(int i=0;i<views.size();i++){
			Element view = subView.addElement("view");
			view.addAttribute("name", keys.next().toString());
			view.addAttribute("id", values.next().toString());
		}
		return document.asXML();
	}

	/**
	 * 返回 myAppsReport 打印数据
	 * @param template
	 * @param doc
	 * @param formid
	 * @param flowid
	 * @return
	 * @throws Exception 
	 */
	public String getReportData(String template, Document doc, String formid,
			String flowid,WebUser user,ParamsTable params) throws Exception {
		
		return createDocument(template,doc,user,params);
	}

/*
	public void parseForm(Node form){
		List fields = form.selectNodes("textbox");
		for(Iterator it = fields.iterator();it.hasNext();){
			Element field =(Element)it.next();
		}
	}
*/	
	
	/**
	 * 根据字段名 获取 Document 私有字段的值
	 * @param doc
	 * @param fieldName
	 * @return
	 */
	public String getDocPrivateItemValue(Document doc,String fieldName){
		if(fieldName.equals("AuditDate")){
			return DateUtil.getDateStr(doc.getAuditdate());
		}else if(fieldName.equals("AuditorNames")){
			return doc.getAuditorNames();
		}else if(fieldName.equals("Author")){
			return doc.getAuthor().getName();
		}else if(fieldName.equals("Created")){
			return DateUtil.getDateStr(doc.getCreated());
		}else if(fieldName.equals("FormName")){
			return doc.getFormname();
		}else if(fieldName.equals("LastFlowOperation")){
			return doc.getLastFlowOperation();
		}else if(fieldName.equals("LastModified")){
			return DateUtil.getDateStr(doc.getLastmodified());
		}else if(fieldName.equals("StateLabel")){
			return doc.getStateLabel();
		}else if(fieldName.equals("Id")){
			return doc.getId();
		}else
			return "";
		
	}

	/**
	 * 创建 flexReport 打印数据 xml文档
	 * 
	 * @SuppressWarnings selectNodes方法不支持泛型
	 * @param strTemplate
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String createDocument(String strTemplate, Document doc,WebUser user,ParamsTable params) throws Exception {
		org.dom4j.Document document = DocumentHelper.createDocument();
		org.dom4j.Document template = DocumentHelper.parseText(strTemplate);
		
		Element report =createReport(document,template);
		
		List<Element> formList = template.selectNodes("//report/form");
		List<Element> detailList = template.selectNodes("//report/detail");
		List<Element> viewList = template.selectNodes("//report/view");
		List<Element> headerList = template.selectNodes("//report/header");
		List<Element> footerList = template.selectNodes("//report/footer");
		
		this.createSingleCanvas(formList, "form", report, doc,user,params);
		this.createDetail(detailList, report, doc,user,params);
		this.createView(viewList,report,doc,user,params);
		//this.createSingleCanvas(headerList, "header", report, doc);
		//this.createSingleCanvas(footerList, "footer", report, doc);
		this.createHeader(headerList, report, doc,user,params);
		this.createFooter(footerList, report, doc,user,params);
//System.out.println(document.asXML());
		return document.asXML();
	}
	
	/**
	 * @param document
	 * @param template
	 * @return
	 * @throws Exception
	 */
	public Element createReport(org.dom4j.Document document,org.dom4j.Document template)throws Exception{
		Element report = document.addElement("report");
		String paperFormat = template.getRootElement().attributeValue("paperFormat");
		String width = template.getRootElement().attributeValue("width");
		String height = template.getRootElement().attributeValue("height");
        String paddingTop = template.getRootElement().attributeValue("paddingTop");
        String paddingBottom = template.getRootElement().attributeValue("paddingBottom");
        String paddingLeft = template.getRootElement().attributeValue("paddingLeft");
        String paddingRight = template.getRootElement().attributeValue("paddingRight");
		report.addAttribute("paperFormat", paperFormat);
		if(width !=null && height!=null){ //兼容 2.3
			report.addAttribute("width", width);
			report.addAttribute("height", height);
		}
        if (paddingTop != null || paddingBottom != null || paddingLeft != null || paddingRight != null) {
            report.addAttribute("paddingTop", paddingTop);
            report.addAttribute("paddingBottom", paddingBottom);
            report.addAttribute("paddingLeft", paddingLeft);
            report.addAttribute("paddingRight", paddingRight);
        }
		return report;
	}
	
	
	/**
	 * @param forms
	 * @param parentNode
	 * @param doc
	 * @throws Exception
	 */
	//@SuppressWarnings("unchecked")
	public void createSingleCanvas(List<Element> canvases,String tag,Element parentNode,Document doc,WebUser user,ParamsTable params)throws Exception{
		for(Iterator<Element> it = canvases.iterator();it.hasNext();){
			Element canvasNode = it.next();
			String name = canvasNode.attributeValue("name");
			String startX = canvasNode.attributeValue("startX");
			String startY = canvasNode.attributeValue("startY");
			String endX = canvasNode.attributeValue("endX");
			String endY = canvasNode.attributeValue("endY");
			Element canvas = parentNode.addElement(tag);
			canvas.addAttribute("name", name);
			canvas.addAttribute("startX", startX);
			canvas.addAttribute("startY", startY);
			canvas.addAttribute("endX", endX);
			canvas.addAttribute("endY", endY);
			createItems(canvasNode,canvas,doc,user,params,0);
			
		}
	}
	
	
	/**
	 * 创建 视图组件对象
	 * @param details
	 * @param parentNode
	 * @param doc
	 * @throws Exception
	 */
	//@SuppressWarnings("unchecked")
	public void createView(List<Element> views,Element parentNode,Document doc,WebUser user,ParamsTable params)throws Exception{
		for(Iterator<Element> it = views.iterator();it.hasNext();){
			Element viewNode =(Element)it.next();
			String viewId = viewNode.attributeValue("bindingView");
			int repeat = Integer.parseInt(viewNode.attributeValue("repeat"));
			String startX = viewNode.attributeValue("startX");
			String startY = viewNode.attributeValue("startY");
			String endX = viewNode.attributeValue("endX");
			String endY = viewNode.attributeValue("endY");
			if(viewId.trim().length()>0){
				
				Form form = doc.getForm();
				Collection<FormField> fileds = form.getFields();
				for(Iterator<FormField> iter = fileds.iterator();iter.hasNext();){
					FormField filed = iter.next();
					if(filed instanceof IncludeField){
						if(((IncludeField)filed).getIncludeView() !=null && ((IncludeField)filed).getIncludeView().getId().equals(viewId)){
							if(((IncludeField)filed).isRelate()){
								params.setParameter("parentid", doc.getId());
								break;
							}
						}
					}
				}
				
				params.setParameter("viewId", viewId);
				params.setParameter("isRelate","true");
				ViewProcess vp = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
//				PrinterProcess pp = (PrinterProcess) ProcessFactory.createProcess(PrinterProcess.class);
				View viewVO =(View)vp.doView(viewId);
				
				if(viewVO !=null){
					//DataPackage dataPackage = pp.getViewDatas(viewVO, repeat, user,params);
					DataPackage<Document> dataPackage = viewVO.getViewTypeImpl().getViewDatas(params, 1, repeat, user, new Document());
					if(dataPackage !=null && dataPackage.datas.size()>0){
						Element view = parentNode.addElement("view");
						view.addAttribute("startX", startX);
						view.addAttribute("startY", startY );
						view.addAttribute("endX", endX);
						view.addAttribute("endY",endY);
						Collection<Document> docs = dataPackage.datas;
						addSumRow(viewVO, dataPackage, docs, params, user);
						int rownum = 1;
						for(Iterator<Document> iter = docs.iterator();iter.hasNext();){
							Document idoc = iter.next();
							params.setParameter("rownum", rownum ++);//设置rownum，作为输出序号列的值
							this.createViewItem(view,idoc, viewVO,params,user);
						}
					}
				}
			}
		}
	}
	
	protected void addSumRow(View view,DataPackage<Document> dataPackage,Collection<Document> docs,ParamsTable params, WebUser user) throws Exception {
		if(view.isSum()){
			Document doc = (Document) ((Document)docs.toArray()[0]).deepClone();
			IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), view.getApplicationid());
			runner.initBSFManager(doc, params, user, new ArrayList<ValidateMessage>());
			
			for (Iterator<Column> iterator = view.getColumns().iterator(); iterator.hasNext();) {
				Column col = iterator.next();
				String sum = col.getSumByDatas(dataPackage, runner, user) + getSumTotal(view, col, params, user.getDomainid(), user);
				if(!StringUtil.isBlank(sum)){
					sum = sum.replace("{*[cn.myapps.core.dynaform.view.current_page]*}", "当页");
					sum = sum.replace("{*[cn.myapps.core.dynaform.view.current_page_total]*}","当页小计");
					sum = sum.replace("{*[cn.myapps.core.dynaform.view.Grant_Total]*}", "总计");
				}
				Item docitem = doc.findItem(col.getName());
				if(docitem != null){
					docitem.setValue(sum);
				}
			}
			docs.add(doc);
		}
	}
	
	public void createViewItem(Element parentNode,Document doc,View view,ParamsTable params, WebUser user) throws Exception {
		Element item = parentNode.addElement("item");
		Collection<Column> columns = view.getColumns();
		IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), view.getApplicationid());;
		runner.initBSFManager(doc, params, user, new ArrayList<ValidateMessage>());
		Object rownum = params.getParameter("rownum");
		for (Iterator<Column> iterator = columns.iterator(); iterator.hasNext();) {
			Column col = (Column) iterator.next();
			if(col.isVisible4Print()){
				Object result = col.getText(doc, runner, user);
				if(Column.COLUMN_TYPE_ROWNUM.equals(col.getType())){
					result = rownum;
				}
				String colName = col.getName();
				item.addAttribute(colName.replaceAll(" ", "&nbsp;"), result.toString().replaceAll(" ", "&nbsp;"));
			}
		}
	}
	
	//@SuppressWarnings("unchecked")
	public void createDetail(List<Element> details,Element parentNode,Document doc,WebUser user,ParamsTable params)throws Exception{
		for(Iterator<Element> it = details.iterator();it.hasNext();){
			Element detailNode = it.next();
			String repeatType = detailNode.attributeValue("repeatType");
			int repeat = Integer.parseInt(detailNode.attributeValue("repeat"));
			String name = detailNode.attributeValue("name");
			String startX = detailNode.attributeValue("startX");
			String startY = detailNode.attributeValue("startY");
			String endX = detailNode.attributeValue("endX");
			String endY = detailNode.attributeValue("endY");
			int height =Integer.parseInt(endY) - Integer.parseInt(startY);
			if(repeatType.equals("static")){
				for(int i=0;i<repeat;i++){
					int _height =height*i;
					Element detail = parentNode.addElement("detail");
					detail.addAttribute("name", name);
					detail.addAttribute("startX", startX);
					detail.addAttribute("startY", String.valueOf(Integer.parseInt(startY)+_height) );
					detail.addAttribute("endX", endX);
					detail.addAttribute("endY", String.valueOf(Integer.parseInt(endY)+_height));
					createItems(detailNode,detail,doc,user,params,_height);
				}
			}
			
			
		}
	}
	
	/**
	 * @param forms
	 * @param parentNode
	 * @param doc
	 * @throws Exception
	 */
	//@SuppressWarnings("unchecked")
	public void createForm(List<Element> forms,Element parentNode,Document doc,WebUser user,ParamsTable params)throws Exception{
		for(Iterator<Element> it = forms.iterator();it.hasNext();){
			Element formNode = it.next();
			String name = formNode.attributeValue("name");
			String startX = formNode.attributeValue("startX");
			String startY = formNode.attributeValue("startY");
			String endX = formNode.attributeValue("endX");
			String endY = formNode.attributeValue("endY");
			Element form = parentNode.addElement("form");
			form.addAttribute("name", name);
			form.addAttribute("startX", startX);
			form.addAttribute("startY", startY);
			form.addAttribute("endX", endX);
			form.addAttribute("endY", endY);
			createItems(formNode,form,doc,user,params,0);
			
		}
	}
	
	/**
	 * @param headers
	 * @param parentNode
	 * @param doc
	 * @throws Exception
	 */
	//@SuppressWarnings("unchecked")
	public void createHeader(List<Element> headers,Element parentNode,Document doc,WebUser user,ParamsTable params)throws Exception{
		for(Iterator<Element> it = headers.iterator();it.hasNext();){
			Element headerNode = it.next();
			String viewStyle = headerNode.attributeValue("viewStyle");
			String startX = headerNode.attributeValue("startX");
			String startY = headerNode.attributeValue("startY");
			String endX = headerNode.attributeValue("endX");
			String endY = headerNode.attributeValue("endY");
			Element header = parentNode.addElement("header");
			header.addAttribute("viewStyle", viewStyle);
			header.addAttribute("startX", startX);
			header.addAttribute("startY", startY);
			header.addAttribute("endX", endX);
			header.addAttribute("endY", endY);
			createItems(headerNode,header,doc,user,params,0);
			
		}
	}
	
	/**
	 * @param footers
	 * @param parentNode
	 * @param doc
	 * @throws Exception
	 */
	//@SuppressWarnings("unchecked")
	public void createFooter(List<Element> footers,Element parentNode,Document doc,WebUser user,ParamsTable params)throws Exception{
		for(Iterator<Element> it = footers.iterator();it.hasNext();){
			Element footerNode = it.next();
			String viewStyle = footerNode.attributeValue("viewStyle");
			String startX = footerNode.attributeValue("startX");
			String startY = footerNode.attributeValue("startY");
			String endX = footerNode.attributeValue("endX");
			String endY = footerNode.attributeValue("endY");
			Element footer = parentNode.addElement("footer");
			footer.addAttribute("viewStyle", viewStyle);
			footer.addAttribute("startX", startX);
			footer.addAttribute("startY", startY);
			footer.addAttribute("endX", endX);
			footer.addAttribute("endY", endY);
			createItems(footerNode,footer,doc,user,params,0);
			
		}
	}
	
	/**
	 * @SuppressWarnings selectNodes方法不支持泛型
	 * @param parentNode
	 * @param parent
	 * @param doc
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void createItems(Node parentNode,Element parent,Document doc,WebUser user,ParamsTable params,int _height)throws Exception{
		List<Element> fields = parentNode.selectNodes("textbox");//解析 field-----------
		createField(fields,parent,doc,user,params,_height);
		List<Element> staticLabels = parentNode.selectNodes("staticLabel");//解析 staticLabel-----------
		createStaticLabel(staticLabels,parent,doc,_height);
		List<Element> lines = parentNode.selectNodes("line");
		createLine(lines,parent,doc,_height);
		List<Element> sheets = parentNode.selectNodes("sheet");
		createSheet(sheets,parent,doc,_height);
		List<Element> pageNumber = parentNode.selectNodes("pageNumber");
		createPageNumber(pageNumber,parent,doc,_height);
		List<Element> barcode = parentNode.selectNodes("barcode");
		createBarcode(barcode,parent,doc,_height);
	}
	
	/**
	 * @param fields
	 * @param parentNode
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	//@SuppressWarnings("unchecked")
	public Element createField(List<Element> fields,Element parentNode,Document doc,WebUser user,ParamsTable params,int _height)throws Exception{
		Element field =null;
		Collection<FormField> formFields = doc.getForm().getFields();
		IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), doc.getApplicationid());
		runner.initBSFManager(doc, params, user, new ArrayList<ValidateMessage>());
		for(Iterator<Element> it = fields.iterator();it.hasNext();){
			Element tfield = it.next();
			String fieldName = tfield.attributeValue("bindingField");
			String fontSize = tfield.attributeValue("fontSize");
			String color = tfield.attributeValue("color");
			String startX = tfield.attributeValue("startX");
			String startY = tfield.attributeValue("startY");
			String endX = tfield.attributeValue("endX");
			String endY = tfield.attributeValue("endY");
			String value = "";
			if(fieldName.startsWith("$")){
				value = getDocPrivateItemValue(doc,fieldName.substring(1, fieldName.length()));
			}else if(fieldName.trim().length()>0){
				for (Iterator<FormField> iterator = formFields.iterator(); iterator
						.hasNext();) {
					FormField formField = (FormField) iterator.next();
					if(formField.getName().equals(fieldName)){
						if(formField instanceof AttachmentUploadField) {
							value = ((AttachmentUploadField)formField).getText2(doc, runner, user);
							break;
						}
						if(formField instanceof AttachmentUploadToDataBaseField) {
							value = ((AttachmentUploadToDataBaseField)formField).getText2(doc, runner, user);
							break;
						}
						value = formField.getText(doc, runner, user);
						break;
					}
					if(formField instanceof TabField){
						Collection<Form> forms = ((TabField)formField).getForms();
						for (Iterator<Form> tabFormsits = forms.iterator(); tabFormsits
								.hasNext();) {
							Form tabForm = tabFormsits.next();
							Collection<FormField> tabFormFields = tabForm.getFields();
							for (Iterator<FormField> tabFormFieldsits = tabFormFields.iterator(); tabFormFieldsits
									.hasNext();) {
								FormField tabFormField = tabFormFieldsits.next();
								if(tabFormField.getName().equals(fieldName)){
									if(tabFormField instanceof AttachmentUploadField) {
										value = ((AttachmentUploadField)tabFormField).getText2(doc, runner, user);
										break;
									}
									if(tabFormField instanceof AttachmentUploadToDataBaseField) {
										value = ((AttachmentUploadToDataBaseField)tabFormField).getText2(doc, runner, user);
										break;
									}
									value = tabFormField.getText(doc, runner, user);
									break;
								}
							}
						}
					}
					
				}
//				value = doc.getItemValueAsString(fieldName);
			}
			
			field = parentNode.addElement("field");
			field.addAttribute("fieldName", fieldName);
			field.addAttribute("value", value);
			field.addAttribute("fontSize", fontSize);
			field.addAttribute("color", color);
			field.addAttribute("startX", startX);
			field.addAttribute("startY", String.valueOf(Integer.parseInt(startY)+_height));
			field.addAttribute("endX", endX);
			field.addAttribute("endY", String.valueOf(Integer.parseInt(endY)+_height));
		}
		return field;
	}
	
	/**
	 * @param staticLabels
	 * @param parentNode
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	//@SuppressWarnings("unchecked")
	public Element createStaticLabel(List<Element> staticLabels,Element parentNode,Document doc,int _height)throws Exception{
		Element staticLabel =null;
		for(Iterator<Element> it = staticLabels.iterator();it.hasNext();){
			Element tstaticLabel = it.next();
			String text = tstaticLabel.attributeValue("text");
			String fontSize = tstaticLabel.attributeValue("fontSize");
			String color = tstaticLabel.attributeValue("color");
			String startX = tstaticLabel.attributeValue("startX");
			String startY = tstaticLabel.attributeValue("startY");
			String endX = tstaticLabel.attributeValue("endX");
			String endY = tstaticLabel.attributeValue("endY");
			staticLabel = parentNode.addElement("staticLabel");
			staticLabel.addAttribute("text", text);
			staticLabel.addAttribute("fontSize", fontSize);
			staticLabel.addAttribute("color", color);
			staticLabel.addAttribute("startX", startX);
			staticLabel.addAttribute("startY", String.valueOf(Integer.parseInt(startY)+_height));
			staticLabel.addAttribute("endX", endX);
			staticLabel.addAttribute("endY", String.valueOf(Integer.parseInt(endY)+_height));
			
		}
		return staticLabel;
	}
	
	
	/**
	 * @param lines
	 * @param parentNode
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	//@SuppressWarnings("unchecked")
	public Element createLine(List<Element> lines,Element parentNode,Document doc,int _height)throws Exception{
		Element line =null;
		for(Iterator<Element> it = lines.iterator();it.hasNext();){
			Element tLine = it.next();
			String thickness = tLine.attributeValue("thickness");
			String color = tLine.attributeValue("color");
			String startX = tLine.attributeValue("startX");
			String startY = tLine.attributeValue("startY");
			String endX = tLine.attributeValue("endX");
			String endY = tLine.attributeValue("endY");
			line = parentNode.addElement("line");
			line.addAttribute("thickness", thickness);
			line.addAttribute("color", color);
			line.addAttribute("startX", startX);
			line.addAttribute("startY", String.valueOf(Integer.parseInt(startY)+_height));
			line.addAttribute("endX", endX);
			line.addAttribute("endY", String.valueOf(Integer.parseInt(endY)+_height));
			
		}
		return line;
	}
	
	/**
	 * @param sheets
	 * @param parentNode
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	//@SuppressWarnings("unchecked")
	public Element createSheet(List<Element> sheets,Element parentNode,Document doc,int _height)throws Exception{
		Element sheet =null;
		for(Iterator<Element> it = sheets.iterator();it.hasNext();){
			Element tSheet = it.next();
			String thickness = tSheet.attributeValue("thickness");
			String color = tSheet.attributeValue("color");
			String startX = tSheet.attributeValue("startX");
			String startY = tSheet.attributeValue("startY");
			String endX = tSheet.attributeValue("endX");
			String endY = tSheet.attributeValue("endY");
			String row = tSheet.attributeValue("row");
			String column = tSheet.attributeValue("column");
			sheet = parentNode.addElement("sheet");
			sheet.addAttribute("thickness", thickness);
			sheet.addAttribute("color", color);
			sheet.addAttribute("startX", startX);
			sheet.addAttribute("startY", String.valueOf(Integer.parseInt(startY)+_height));
			sheet.addAttribute("endX", endX);
			sheet.addAttribute("endY", String.valueOf(Integer.parseInt(endY)+_height));
			sheet.addAttribute("row", row);
			sheet.addAttribute("column", column);
			
		}
		return sheet;
	}
	
	//@SuppressWarnings("unchecked")
	public Element createPageNumber(List<Element> pageNumbers,Element parentNode,Document doc,int _height)throws Exception{
		Element pageNumber =null;
		for(Iterator<Element> it = pageNumbers.iterator();it.hasNext();){
			Element tPageNumber = it.next();
			String startX = tPageNumber.attributeValue("startX");
			String startY = tPageNumber.attributeValue("startY");
			String endX = tPageNumber.attributeValue("endX");
			String endY = tPageNumber.attributeValue("endY");
			pageNumber = parentNode.addElement("pageNumber");
			pageNumber.addAttribute("startX", startX);
			pageNumber.addAttribute("startY", String.valueOf(Integer.parseInt(startY)+_height));
			pageNumber.addAttribute("endX", endX);
			pageNumber.addAttribute("endY", String.valueOf(Integer.parseInt(endY)+_height));
			
		}
		return pageNumber;
	}
	
	//@SuppressWarnings("unchecked")
	public Element createBarcode(List<Element> barcodes,Element parentNode,Document doc,int _height)throws Exception{
		Element barcode =null;
		for(Iterator<Element> it = barcodes.iterator();it.hasNext();){
			Element tBarcode = it.next();
			String startX = tBarcode.attributeValue("startX");
			String startY = tBarcode.attributeValue("startY");
			String endX = tBarcode.attributeValue("endX");
			String endY = tBarcode.attributeValue("endY");
			String fieldName = tBarcode.attributeValue("bindingField");
			String typeCode = tBarcode.attributeValue("typeCode");
			String contents = "";
			if(fieldName.startsWith("$")){
				contents = getDocPrivateItemValue(doc,fieldName.substring(1, fieldName.length()));
			}else if(fieldName.trim().length()>0){
				contents = doc.getItemValueAsString(fieldName);
			}
			barcode = parentNode.addElement("barcode");
			barcode.addAttribute("contents", contents);
			barcode.addAttribute("typeCode", typeCode);
			barcode.addAttribute("startX", startX);
			barcode.addAttribute("startY", String.valueOf(Integer.parseInt(startY)+_height));
			barcode.addAttribute("endX", endX);
			barcode.addAttribute("endY", String.valueOf(Integer.parseInt(endY)+_height));
			
		}
		return barcode;
	}
	
	
	
	
	@SuppressWarnings("unused")
	private String getColumnValue(Document doc,String fieldName) throws Exception{
		String result = null;
		if (!StringUtil.isBlank(fieldName)) {
			result = doc.getValueByField(fieldName);
		}
		return (result != null ? result.toString() : "");
	}
	
	private String getSumTotal(View view,Column col,ParamsTable params,String domainid,WebUser webUser){
		if(col.getType().equals(Column.COLUMN_TYPE_SCRIPT)){
			return "";
		}
		double total = 0;
		String numberPattern = "";
		String num = "0";
		if (col.isTotal()) {
			try {
				FormField formField = col.getFormField();
				if (formField instanceof InputField) {
					InputField inputField = (InputField) formField;
					numberPattern = inputField.getNumberPattern();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				if(view.getEditModeType() instanceof DesignEditMode){
					params.setParameter("_viewid", view.getId());
					total = view.getViewTypeImpl().getSumTotal(params, webUser, col.getFieldName(), col.getFormid(), domainid);
				}else {
					return "";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			if(col.getFormatType() != null && !col.getFormatType().equals(Column.FORMATTYPE_SIMPLE)){
				num = "" + total;
			} else if (!StringUtil.isBlank(numberPattern)){
				DecimalFormat format = new DecimalFormat(numberPattern);
				num = format.format(total);
			}else {
				num = "" + total;
			}

			return "  总计:"+num;
		}
		return "";
	}

//	public static void main(String[] args){
//		String str = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"+
//		"<report  paperFormat=\"A4\">"+
//		"<detail repeatType=\"static\" repeat=\"3\" name=\"Detail782731\" startX=\"0\" startY=\"170\" endX=\"595\" endY=\"270\" >"+
//		    "<line name=\"Line154584\" fillColor=\"4f0dd\" startX=\"50\" startY=\"190\" endX=\"150\" endY=\"190\"></line>"+
//		"</detail>"+
//		"</report>";
//		try {
//			PrinterUtils.getInstance().createDocument(str, new Document());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
