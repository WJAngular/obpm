// Source file:
// C:\\Java\\workspace\\SmartWeb3\\src\\com\\cyberway\\dynaform\\form\\ejb\\SelectField.java

package cn.myapps.core.dynaform.form.ejb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.core.dynaform.view.ejb.Column;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.table.constants.MobileConstant;
import cn.myapps.core.table.constants.MobileConstant2;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.HtmlEncoder;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

/**
 * @author Marky
 */
public class SelectAboutField extends FormField implements ValueStoreField {
	
	private static final long serialVersionUID = -8469984129928896643L;

	protected static String cssClass = "select-cmd";

	/**
	 * 计算多值选项
	 * 
	 * @uml.property name="optionsScript"
	 */
	protected String optionsScript;

	private String calculateOnRefresh;

	private String validateLibs;

	private String authority;

	private String editMode;

	/**
	 * 选项的编辑模式('00':设计 '01':脚本代码)
	 * 
	 * @uml.property name="optionsEditMode"
	 */
	protected String optionsEditMode;
	
	/**
	 * 选项设计模式关联的视图
	 * @uml.property name="dialogView"
	 */
	protected String dialogView = "";
	
	/**
	 * 选项真实值
	 * @uml.property name="optionsValue"
	 */
	protected String optionsValue = "";
	
	/**
	 * 选项显示值
	 * @uml.property name="optionsText"
	 */
	protected String optionsText = "";
	
	public String getEditMode() {
		return editMode;
	}

	public void setEditMode(String editMode) {
		this.editMode = editMode;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getValidateLibs() {
		return validateLibs;
	}

	public void setValidateLibs(String validateLibs) {
		this.validateLibs = validateLibs;
	}

	public String getCalculateOnRefresh() {
		return calculateOnRefresh;
	}

	public void setCalculateOnRefresh(String calculateOnRefresh) {
		this.calculateOnRefresh = calculateOnRefresh;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String name;

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String size;

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * @roseuid 41ECB66D031D
	 */
	public SelectAboutField() {

	}

	/**
	 * 返回多值选项脚本
	 * 
	 * @return Returns the optionsScript.
	 * @uml.property name="optionsScript"
	 */
	public String getOptionsScript() {
		return optionsScript;
	}

	/**
	 * 设置多值选项脚本
	 * 
	 * @param optionsScript
	 *            The optionsScript to set.
	 * @uml.property name="optionsScript"
	 */
	public void setOptionsScript(String optionsScript) {
		this.optionsScript = optionsScript;
	}

	/**
	 * 获取选项编辑模式
	 * @return
	 */
	public String getOptionsEditMode() {
		return optionsEditMode;
	}
	
	/**
	 * 设置选项编辑模式
	 * @param optionsEditMode
	 */
	public void setOptionsEditMode(String optionsEditMode) {
		this.optionsEditMode = optionsEditMode;
	}
	
	/**
	 * 获取所引用的视图
	 * 
	 * @return 引用的视图
	 * @uml.property name="dialogView"
	 */
	public String getDialogView() {
		return dialogView;
	}

	/**
	 * 设置引用的视图
	 * 
	 * @param dialogView
	 *            引用的视图
	 * @uml.property name="dialogView"
	 */
	public void setDialogView(String dialogView) {
		this.dialogView = dialogView;
	}

	/**
	 * 获取选项真实值
	 * @return
	 */
	public String getOptionsValue() {
		return optionsValue;
	}
	/**
	 * 设置选项真实值
	 * @param optionsValue
	 */
	public void setOptionsValue(String optionsValue) {
		this.optionsValue = optionsValue;
	}
	/**
	 * 获取选项显示值
	 * @return
	 */
	public String getOptionsText() {
		return optionsText;
	}
	/**
	 * 设置选项显示值
	 * @param optionsText
	 */
	public void setOptionsText(String optionsText) {
		this.optionsText = optionsText;
	}
	
	/**
	 * SelectField. 重新计算SelectField.
	 * 
	 * @roseuid 41DB89D700F9
	 */
	public void recalculate(IRunner runner, Document doc, WebUser webUser) throws Exception {
		getLog().debug("SelectAboutField.recalculate");
		runValueScript(runner, doc);
		runOptionsScript(runner, doc, webUser);
	}

	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser, int permissionType) throws Exception {
		StringBuffer html = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser, permissionType);
		if (doc != null) {
			try {
				html.append("<select data-enhance='false' moduleType='selectAbout'");
				html.append(toAttr(doc, displayType));
				html.append(">");
				html.append(runOptionsScript(runner, doc, webUser));
				html.append("</select>");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return html.toString();
	}

	public String toGridHtmlText(Document doc, IRunner runner, WebUser webUser, Map<String,Options> columnOptionsCache) throws Exception {
		StringBuffer html = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);
		if (doc != null) {
			try {
				html.append("<select moduleType='selectAbout'");
				html.append(toAttr(doc, displayType));
				html.append(">");
				html.append(runOptionsScript(runner, doc, webUser, "HTML", columnOptionsCache));
				html.append("</select>");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return html.toString();
	}

	public String getRefreshScript(IRunner runner, Document doc, WebUser webUser) throws Exception {
		return super.getRefreshScript(runner, doc, webUser);
	}

	private String toAttr(Document doc, int displayType) throws Exception {
		StringBuffer html = new StringBuffer();
		html.append(" hiddenValue = '" + this.getHiddenValue() + "'");
		html.append(" style='display:");
		html.append(getTextType().equals("hidden") ? "none" : "inline");
		html.append("'");
		html.append(" id='" + getFieldId(doc) + "'");
		html.append(" name='" + getName() + "'");
		html.append(" fieldType='" + getTagName() + "'");
		html.append(" multiple=true");
		html.append(" isRefreshOnChanged='" + isRefreshOnChanged() + "'");
		html.append(" displayType='" + displayType + "'");
		html.append(" textType='" + getTextType() + "'");
		//表单描述字段
		html.append(" discript ='" + getDiscript() + "'");
		return html.toString();
	}

	/**
	 * 返回模板描述下拉选项
	 * 
	 * @return java.lang.String
	 * @roseuid 41E7917A033F
	 */
	public String toTemplate() {
		StringBuffer template = new StringBuffer();
		template.append("<select'");
		template.append(" className='" + this.getClass().getName() + "'");
		template.append(" id='" + getId() + "'");
		template.append(" name='" + getName() + "'");
		template.append(" formid='" + getFormid() + "'");
		template.append(" discript='" + getDiscript() + "'");
		template.append(" hiddenScript='" + getHiddenScript() + "'");
		template.append(" hiddenPrintScript='" + getHiddenPrintScript() + "'");
		template.append(" refreshOnChanged='" + isRefreshOnChanged() + "'");
		template.append(" validateRule='" + getValidateRule() + "'");
		template.append(" valueScript='" + getValueScript() + "'");
		template.append(" optionScript='" + getOptionsScript() + "'");
		template.append(" onRefresh='" + isCalculateOnRefresh() + "'");
		template.append("/>");
		return template.toString();

	}

	/**
	 * 返回执行多值选项脚本后重定义后的html，通过强化HTML标签及语法，表达下拉选项的布局、属性、事件、样式、等。
	 * 
	 * @param runner
	 *            AbstractRunner<code>(执行脚本接口类)</code>
	 * @see cn.myapps.core.macro.runner.JavaScriptRunner#run(String, String)
	 * 
	 * @param doc
	 *            文档对象
	 * @return 字符串内容为重定义后的html的下拉选框标签及值
	 * @throws Exception
	 */
	public String runOptionsScript(IRunner runner, Document doc, WebUser webUser) throws Exception {
		return runOptionsScript(runner, doc, webUser, "HTML", null);
	}

	private String runOptionsScript(IRunner runner, Document doc, WebUser webUser, String stringType, Map<String,Options> columnOptionsCache) throws Exception {

		StringBuffer html = new StringBuffer();

		Options options = columnOptionsCache != null ? columnOptionsCache.get(this.getId()) : null;
		if(options == null){
			options = getOptions(runner, doc, webUser);
			if(columnOptionsCache != null && FormField.EDITMODE_VIEW.equals(optionsEditMode)){
				//选项是设计模式的时候才加入缓存
				columnOptionsCache.put(this.getId(), options);
			}
		}

		if (stringType.equals("HTML")) {
			if (options != null) {
				return toOptionForHtml(options, doc);
			}
		} else if (stringType.equals("XML")) {
			if (options != null) {
				return toOptionForXml(options, doc);
			} else {
				html.append("<").append(MobileConstant.TAG_OPTION).append(">");
				html.append("</").append(MobileConstant.TAG_OPTION).append(">");
			}
		} else if(stringType.equals("XML2")){
			if (options != null) {
				return toOptionForXml2(options, doc);
			} else {
				html.append("<").append(MobileConstant2.TAG_OPTION).append(">");
				html.append("</").append(MobileConstant2.TAG_OPTION).append(">");
			}
		}
		return html.toString();
	}

	private Options getOptions(IRunner runner, Document doc, WebUser webUser) throws Exception {
		Object result = null;
		Options options = null;
		if(FormField.EDITMODE_VIEW.equals(optionsEditMode)){
			//设计模式获取options
			options = getOptionsByDesign(webUser);
		}else{
			//脚本模式获取options
			if (getOptionsScript() != null && getOptionsScript().trim().length() > 0) {
	
				result = runner.run(getScriptLable("OptionsScript"), StringUtil.dencodeHTML(getOptionsScript()));
				if (result != null && result instanceof String) {
	
					String[] strlst = ((String) result).split(";");
					options = new Options();
					for (int i = 0; i < strlst.length; i++) {
						options.add(strlst[i], strlst[i]);
					}
				} else if (result instanceof Options) {
					options = (Options) result;
				}
			}
		}
		return options;
	}

	/**
	 * 返回执行多值选项脚本后重定义后的xml，通过强化HTML标签及语法，表达下拉选项的布局、属性、事件、样式、等。
	 * 
	 * @param runner
	 *            AbstractRunner<code>(执行脚本接口类)</code>
	 * @see cn.myapps.core.macro.runner.JavaScriptRunner#run(String, String)
	 * 
	 * @param doc
	 *            文档对象
	 * @return 字符串内容为重定义后的xml的下拉选框标签及值
	 * @throws Exception
	 */
	private String runOptionsScriptToXML(IRunner runner, Document doc, WebUser webUser) throws Exception {
		return runOptionsScript(runner, doc, webUser, "XML", null);
	}
	
	/**
	 * 返回执行多值选项脚本后重定义后的xml，通过强化HTML标签及语法，表达下拉选项的布局、属性、事件、样式、等。
	 * 
	 * @param runner
	 *            AbstractRunner<code>(执行脚本接口类)</code>
	 * @see cn.myapps.core.macro.runner.JavaScriptRunner#run(String, String)
	 * 
	 * @param doc
	 *            文档对象
	 * @return 字符串内容为重定义后的xml的下拉选框标签及值
	 * @throws Exception
	 */
	private String runOptionsScriptToXML2(IRunner runner, Document doc, WebUser webUser) throws Exception {
		return runOptionsScript(runner, doc, webUser, "XML2", null);
	}

	/**
	 * 根据打印时对应SelectField的显示类型不同,默认为MODIFY,返回的结果字符串不同.
	 * 若Document不为空且打印时对应SelectField的显示类型不为HIDDEN且字段类型不为HIDDEN,
	 * <P>
	 * 并根据Form模版的SelectField组件内容结合Document中的ITEM存放的值,返回重定义后的打印html文本. 否则为空字符串.
	 * 
	 * @param doc
	 *            Document
	 * @param runner
	 *            AbstractRunner(执行脚本的接口类)
	 * @param params
	 *            参数
	 * @param user
	 *            webuser
	 * 
	 * @see cn.myapps.core.macro.runner.AbstractRunner#run(String, String)
	 * @return 重定义后的打印html为Form模版的SelectField组件内容结合Document中的ITEM存放的值
	 * @throws Exception
	 */
	public String toPrintHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception {
		String html = "";
		if (doc != null) {
			int displayType = getPrintDisplayType(doc, runner, webUser);

			if (displayType == PermissionType.HIDDEN) {
				html = this.getPrintHiddenValue();
			}else{
				html = "<span>" + getText(doc, runner, webUser) + "</span>";
			}
		}
		return html;
	}

	public String toPdfHtmlTxt(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		String html = "";
		if (doc != null) {
				html = getText(doc, runner, webUser);
		}
		return html;
	}

	private String toOptionForHtml(Options options, Document doc) {
		StringBuffer html = new StringBuffer();

		Collection<String> selectedList = new ArrayList<String>();
		if (doc != null) {
			Item item = doc.findItem(this.getName());
			if (item != null) {
				String valueStr = item.getTextvalue();
				if (!StringUtil.isBlank(valueStr)) {
					String[] values = valueStr.split(";");
					selectedList = Arrays.asList(values);
				}
			}
		}

		Iterator<Option> iter = options.getOptions().iterator();
		while (iter.hasNext()) {
			Option element = (Option) iter.next();
			if (element.getValue() != null) {
				html.append("<option");
				if (selectedList.contains(element.getValue())) {
					html.append(" selected ");
				} else {
					if (element.isDef()) {
						html.append(" selected ");
					}
				}
				html.append(" class='" + cssClass + "'");
				html.append(" value=\"");
			}
			html.append(HtmlEncoder.encode(element.getValue()));
			html.append("\"");
			html.append(">");
			html.append(element.getOption()).append("</option>");
		}
		// 默认选中一个空选项
		html.append("<option class='" + cssClass + "' value=\"\"></option>");
		
		return html.toString();
	}

	public boolean isRender(String destVal, String origVal) {
		if (optionsScript != null && optionsScript.trim().length() > 0) {
			return true;
		}
		return super.isRender(destVal, origVal);
	}

	public String toMbXMLText(Document doc, IRunner runner, WebUser webUser) throws Exception {
		StringBuffer xmlText = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);

		if (doc != null) {
			xmlText.append("<").append(MobileConstant.TAG_SELECTABOUTFIELD);
			xmlText.append(" ").append(MobileConstant.ATT_ID).append("='").append(getId()).append("'");
			xmlText.append(" ").append(MobileConstant.ATT_NAME).append("='").append(getName()).append("'");
			xmlText.append(" ").append(MobileConstant.ATT_LABEL).append("='").append(getMbLabel()).append("'");

			if (displayType == PermissionType.READONLY || (getTextType() != null && getTextType().equalsIgnoreCase("readonly"))
					|| displayType == PermissionType.DISABLED) {
				xmlText.append(" ").append(MobileConstant.ATT_READONLY).append("='").append("='true'");
			}
			if (displayType == PermissionType.HIDDEN) {
				xmlText.append(" ").append(MobileConstant.ATT_HIDDEN).append(" ='true' ");
				if(!getHiddenValue().equals("") && !getHiddenValue().equals(null) && !getHiddenValue().equals("&nbsp;")){
					xmlText.append(" ").append(MobileConstant.ATT_HIDDENVALUE).append("='").append(getHiddenValue()).append("' ");
				}
			}
			if (isRefreshOnChanged()) {
				xmlText.append(" ").append(MobileConstant.ATT_REFRESH).append("='true'");
			}
			xmlText.append(">");
			try {
				xmlText.append(runOptionsScriptToXML(runner, doc, webUser));
				xmlText.append("</").append(MobileConstant.TAG_SELECTABOUTFIELD).append(">");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return xmlText.toString();
	}

	private String toOptionForXml(Options options, Document doc) {
		StringBuffer html = new StringBuffer();

		Object value = null;
		if (doc != null) {
			Item item = doc.findItem(this.getName());
			if (item != null)
				value = item.getValue();
		}

		Iterator<Option> iter = options.getOptions().iterator();
		int count = 0;
		while (iter.hasNext()) {
			Option element = (Option) iter.next();
			if (element.getValue() != null) {
				html.append("<").append(MobileConstant.TAG_OPTION).append("");
				if (value != null && element.getValue() != null) {
					if (value.toString().indexOf(element.getValue())!=-1) {
						html.append(" ").append(MobileConstant.ATT_SELECTED).append("='" + count + "'");
					}
				} else {
					if (element.isDef()) {
						html.append(" ").append(MobileConstant.ATT_SELECTED).append("='" + count + "'");
					}
				}
				html.append(" ").append(MobileConstant.ATT_VALUE).append("='");

				html.append(HtmlEncoder.encode(element.getValue()));
				html.append("'");

				html.append(">");

				if (element.getOption() != null && !element.getOption().trim().equals(""))
					html.append(HtmlEncoder.encode(element.getOption()));
				else
					html.append("{*[Select]*}");
				html.append("</").append(MobileConstant.TAG_OPTION).append(">");
				count++;
			}
		}
		return html.toString();
	}
	
	private String toOptionForXml2(Options options, Document doc) {
		StringBuffer html = new StringBuffer();

		Object value = null;
		if (doc != null) {
			Item item = doc.findItem(this.getName());
			if (item != null)
				value = item.getValue();
		}

		Iterator<Option> iter = options.getOptions().iterator();
		int count = 0;
		while (iter.hasNext()) {
			Option element = (Option) iter.next();
			if (element.getValue() != null) {
				html.append("<").append(MobileConstant2.TAG_OPTION);
				if (value != null && element.getValue() != null) {
					if (value.toString().indexOf(element.getValue())!=-1) {
						html.append(" ").append(MobileConstant2.ATT_SELECTED).append("='true'");
					}
				} else {
					if (element.isDef()) {
						html.append(" ").append(MobileConstant2.ATT_SELECTED).append("='true'");
					}
				}
				html.append(" ").append(MobileConstant2.ATT_VALUE).append("='");

				html.append(HtmlEncoder.encode(element.getValue()));
				html.append("'");

				html.append(">");

				if (element.getOption() != null && !element.getOption().trim().equals(""))
					html.append(HtmlEncoder.encode(element.getOption()));
				else
					html.append("");
				html.append("</").append(MobileConstant2.TAG_OPTION).append(">");
				count++;
			}
		}
		return html.toString();
	}

	/**
	 * 获取字段显示值
	 * 
	 * @param doc 文档
 	 * @param runner 脚本运行器
 	 * @param webUser webuser
	 * @return
	 * @throws Exception
	 */
	public String getText(Document doc, IRunner runner, WebUser webUser) throws Exception {
		return getText(doc, runner, webUser, null);
	}
	/**
	 * 视图中显示的真实值
	 */
	public String getText(Document doc, IRunner runner, WebUser webUser, Map<String,Options> columnOptionsCache) throws Exception {
		if (!StringUtil.isBlank(doc.getParentid()) && getDisplayType(doc, runner, webUser) == PermissionType.HIDDEN) {
			return this.getHiddenValue();
		}
		Options options = columnOptionsCache != null ? columnOptionsCache.get(this.getId()) : null;
		if(options == null){
			options = getOptions(runner, doc, webUser);
			if(columnOptionsCache != null && FormField.EDITMODE_VIEW.equals(optionsEditMode)){
				//选项是设计模式的时候才加入缓存
				columnOptionsCache.put(this.getId(), options);
			}
		}
		if (options != null&&!doc.getItemValueAsString(getName()).equals("")) {
			StringBuffer sb = new StringBuffer();
			for (Iterator<Option> iterator = options.getOptions().iterator(); iterator.hasNext();) {
				Option option = (Option) iterator.next();
				String[] itemArray = doc.getItemValueAsString(getName()).split(";");
				for(int i=0;i<itemArray.length;i++){
					if (option.getValue().equals(itemArray[i])) {
						sb.append(option.getOption());
						sb.append(";");
						break;
					}
				}
			}
			if(sb.lastIndexOf(";")!=-1){
				sb.deleteCharAt(sb.lastIndexOf(";"));
			}
			return sb.toString();
		}

		return "";
	}
	
	/**
	 * 选项设计模式下获取options
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Options getOptionsByDesign(WebUser user) throws Exception{
		ViewProcess vp = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
		View viewVO =(View)vp.doView(dialogView);
		Options options = new Options();
		if(viewVO != null){		
			DataPackage<Document> dataPackage = viewVO.getViewTypeImpl()
					.getViewDatas(new ParamsTable(), 1, Integer.MAX_VALUE, user, new Document());
			if(dataPackage !=null && dataPackage.datas.size()>0){
				Column optionsValueColumn = new Column();
				Column optionsTextColumn = new Column();
				Collection<Column> columns = viewVO.getColumns();
				for (Iterator<Column> iterator = columns.iterator(); iterator.hasNext();) {
					Column col = (Column) iterator.next();
					if(col.getId().equals(optionsValue)){
						optionsValueColumn = col;
					}
					if(col.getId().equals(optionsText)){
						optionsTextColumn = col;
					}
				}
				IRunner runner = JavaScriptFactory.newInstance(viewVO.getApplicationid());
				for(Iterator<Document> iter = dataPackage.datas.iterator();iter.hasNext();){
					Document doc = iter.next();
					options.add(this.createOption(runner, doc, optionsValueColumn, optionsTextColumn, new ParamsTable(), user));
				}
			}
		}
		return options;
	}
	
	private Option createOption(IRunner runner, Document doc,Column optionsValueColumn,Column optionsTextColumn, ParamsTable params, WebUser user) throws Exception {
		runner.initBSFManager(doc, params, user, new ArrayList<ValidateMessage>());
		Option option = new Option();
		Object resultValue = optionsValueColumn.getText(doc, runner, user);
		Object resultText = optionsTextColumn.getText(doc, runner, user);
		option.setValue(resultValue.toString());
		option.setOption(resultText.toString());
		return option;
	}

	@Override
	public String toMbXMLText2(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		StringBuffer xmlText = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);

		if (doc != null) {
			xmlText.append("<").append(MobileConstant2.TAG_SELECTABOUTFIELD);
			xmlText.append(" ").append(MobileConstant2.ATT_ID).append("='").append(getId()).append("'");
			xmlText.append(" ").append(MobileConstant2.ATT_NAME).append("='").append(getName()).append("'");
			xmlText.append(" ").append(MobileConstant2.ATT_LABEL).append("='").append(getMbLabel()).append("'");

			if (displayType == PermissionType.READONLY || (getTextType() != null && getTextType().equalsIgnoreCase("readonly"))
					|| displayType == PermissionType.DISABLED) {
				xmlText.append(" ").append(MobileConstant2.ATT_READONLY).append("='true'");
			}
			if (displayType == PermissionType.HIDDEN) {
				xmlText.append(" ").append(MobileConstant2.ATT_HIDDEN).append("='true'");
				if(!getHiddenValue().equals("") && !getHiddenValue().equals(null) && !getHiddenValue().equals("&nbsp;")){
					xmlText.append(" ").append(MobileConstant2.ATT_HIDDENVALUE).append("='").append(getHiddenValue()).append("'");
				}
			}
			if (isRefreshOnChanged()) {
				xmlText.append(" ").append(MobileConstant2.ATT_REFRESH).append("='").append("true").append("'");
			}
			xmlText.append(">");
			try {
				xmlText.append(runOptionsScriptToXML2(runner, doc, webUser));
				xmlText.append("</").append(MobileConstant2.TAG_SELECTABOUTFIELD).append(">");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return xmlText.toString();
	}
	
}
