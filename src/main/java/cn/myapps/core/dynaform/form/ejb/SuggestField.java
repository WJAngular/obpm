// Source file:
// C:\\Java\\workspace\\SmartWeb3\\src\\com\\cyberway\\dynaform\\form\\ejb\\SuggestField.java

package cn.myapps.core.dynaform.form.ejb;

import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.table.constants.MobileConstant;
import cn.myapps.core.table.constants.MobileConstant2;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.DbTypeUtil;
import cn.myapps.util.HtmlEncoder;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

/**
 * 下拉提示输入框组件
 * 
 * 
 */
public class SuggestField extends FormField implements ValueStoreField ,OptionItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8469984129928896643L;
	
	/**
	 * 查询数据方式-本地数据
	 */
	private static final String DATA_MODE_LOCAL = "local";
	/**
	 * 查询数据方式-远程数据
	 */
	private static final String DATA_MODE_REMOTE = "remote";

	protected static String cssClass = "select-cmd";

	/**
	 * 计算多值选项
	 * 
	 * @uml.property name="optionsScript"
	 */
	protected String optionsScript;

	
	/**
	 * 查询数据的方式（本地|远程）
	 */
	protected String dataMode = DATA_MODE_LOCAL;
	
	/**
	 * @roseuid 41ECB66D031D
	 */
	public SuggestField() {

	}

	public String getDataMode() {
		return dataMode;
	}

	public void setDataMode(String dataMode) {
		this.dataMode = dataMode;
	}

	/**
	 * 获取多值选项脚本
	 * 
	 * @return 多值选项脚本
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
	 * 重新计算SelectField.
	 * 
	 * @roseuid 41DB89D700F9
	 */
	public void recalculate(IRunner runner, Document doc, WebUser webUser) throws Exception {
		getLog().debug("SelectField.recalculate");
		runValueScript(runner, doc);
		if(DATA_MODE_LOCAL.equals(this.getDataMode())){
			runOptionsScript(runner, doc, webUser);
		}
		
	}

	/**
	 * 根据CheckboxField的显示类型不同,返回的结果字符串不同.
	 * 新建的Document,SelectField的显示类型为默认的MODIFY。
	 * 此时根据Form模版的SelectField内容结合Document的Item的值,返回的字符串为重定义后的html.
	 * 根据流程节点设置对应SelectField的显示类型不同,返回的结果字符串不同.
	 * 
	 * 1)节点设置对应SelectField的显示类型为MODIFY,
	 * 并根据Form模版的SelectField组件内容结合Document中的ITEM存放的值,返回重定义后的html.
	 * 2)节点设置对应SelectField的显示类型为HIDDEN,返回"******".
	 * 3)节点设置对应SelectField的显示类型为READONLY,Document的ITEM存放的值为只读.
	 * 并根据Form模版的SelectField组件内容结合Document中的ITEM存放的值,返回重定义后的html.
	 * 4)节点设置对应SelectField的显示类型为DISABLED,Document的ITEM存放的值为DISABLED.
	 * 并根据Form模版的SelectField组件内容结合Document中的ITEM存放的值,返回重定义后的html.
	 * 
	 * 通过强化HTML的单值选项标签及语法，表达列表框的布局、属性、事件等。
	 * 
	 * @param doc
	 *            文档对象
	 * @see cn.myapps.base.action.ParamsTable#params
	 * @see cn.myapps.core.macro.runner.AbstractRunner#run(String, String)
	 * @return 字符串
	 * 
	 */
	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser, int permissionType) throws Exception {
		StringBuffer html = new StringBuffer();

		int displayType = getDisplayType(doc, runner, webUser, permissionType);
		
		if(doc != null) {
			html.append("<input moduleType='suggest' ");
			html.append(toAttr(doc, runner, webUser, displayType, null));
		}
		
		return html.toString();
	}

	/**
	 * 根据Form模版的SelectField组件内容结合Document中的ITEM存放的值,输出重定义后的html文本以网格显示
	 * 
	 * @param runner
	 *            动态脚本执行器
	 * @param doc
	 *            文档对象
	 * @param webUser
	 *            webUser
	 * @param columnOptionsCache           
	 *             缓存控件选项(选项设计模式构建的Options)的Map
	 *             key: fieldId
	 *             value: Options
	 * @return 重定义后的html文本
	 */
	public String toGridHtmlText(Document doc, IRunner runner, WebUser webUser, Map<String,Options> columnOptionsCache) throws Exception {
		StringBuffer html = new StringBuffer();

		int displayType = getDisplayType(doc, runner, webUser);
		if(displayType == PermissionType.HIDDEN){
			return getHiddenValue();
		}else if(doc != null) {
			html.append("<input moduleType='suggest' ");
			html.append(" _subGridView='true'");
			html.append(toAttr(doc, runner, webUser, displayType, columnOptionsCache));
		}
		
		return html.toString();
	}

	private String toAttr(Document doc, IRunner runner, WebUser webUser, int displayType, Map<String,Options> columnOptionsCache) throws Exception {
		StringBuffer html = new StringBuffer();
		//通过系统冗余字段获取对应的显示值
		String text = getText(doc, runner, webUser);
		
		Options options = null;
		options = columnOptionsCache != null ? columnOptionsCache.get(this.getId()) : null;
		if(options == null && DATA_MODE_LOCAL.equals(dataMode)){
			options = getOptions(runner);
		}

		Object value = null;
		Item item = doc.findItem(this.getName());
		if (item != null)
		value = item.getValue();
		
		value = value != null ? value : "";
		
		String json = "[]";
		if(options != null){
			if (isShowOptions(displayType, options)) {
				json = options.toJSON4SuggestField();
			}
		}
		
		html.append(" _fieldId='" +  getFieldId(doc) + "'");
		html.append(" _name='" +  getName() + "'");
		html.append(" _isRefreshOnChanged='" +  isRefreshOnChanged() + "'");
		html.append(" _displayType='" +  displayType + "'");
		html.append(" _fieldtype='SuggestField'");
		html.append(" _textType='" +  getTextType() + "'");
		html.append(" _hiddenValue='" + getHiddenValue() + "'");
		html.append(" text='" + (!StringUtil.isBlank(text)?text:value.toString()) + "'");
		html.append(" _formid='" + getFormid() + "'");
		html.append(" _domainId='" + doc.getDomainid() + "'");
		html.append(" _fieldId4sych='" + getId() + "'");
		boolean flag = false; //用于判断是否有设置默认值
		if(options != null){
			Iterator<Option> iter = options.getOptions().iterator();
			while (iter.hasNext()) {//遍历所有的选项，如果有默认值，将默认值设入
				Option element = (Option) iter.next();
				if( element.isDef() && value==""){
					html.append(" value=\"" +  element.getValue()  + "\"");
					flag = true;
				}
			}
		}
		if(!flag){
			html.append(" value=\"" +  value + "\"");
		}		
		html.append(" _json=\"" +  json + "\"");
		html.append(" _otherAttrs='suggest_" +  getFieldId(doc) + "'");
		//表单描述字段
		html.append(" _discript ='" + getDiscript() + "'");
			html.append(" _dataMode='"+dataMode+"'");
		html.append(" />");
		
		html.append("<input type='hidden' moduleOtherAttrs='suggest_" +  getFieldId(doc) + "'");
		html.append(" " + toOtherpropsHtml());
		html.append(" />");
		
		return html.toString();
	}

	/**
	 * 是否显示选项
	 * 
	 * @param displayType
	 *            显示类型
	 * @param options
	 *            选项集合
	 * @return
	 */
	private boolean isShowOptions(int displayType, Options options) {
		return !TEXT_TYPE_READONLY.equalsIgnoreCase(getTextType()) && !TEXT_TYPE_HIDDEN.equalsIgnoreCase(getTextType())
				&& displayType != PermissionType.READONLY && options != null;
	}

	/**
	 * @param tmpltStr
	 * @return FormField
	 * @roseuid 41ECB66D0381
	 */
	public FormField init(String tmpltStr) {
		return null;
	}

	/**
	 * 获取模板描述下拉选项
	 * 
	 * @return 模板描述下拉选项
	 * @roseuid 41E7917A033F
	 */
	public String toTemplate() {
		StringBuffer template = new StringBuffer();
		template.append("<input type='text'");
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
		return runOptionsScript(runner, doc, webUser, "HTML");
	}

	private String runOptionsScript(IRunner runner, Document doc, WebUser webUser, String stringType) throws Exception {
		StringBuffer html = new StringBuffer();
		Options options = getOptions(runner);

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
		}else if (stringType.equals("XML2")) {
				if (options != null) {
					return toOptionForXml2(options, doc);
				} else {
					html.append("<").append("OPTION").append(">");
					html.append("</").append("OPTION").append(">");
				}
			}
		return html.toString();
	}

	public Options getOptions(IRunner runner) throws Exception {
		Object result = null;
		Options options = null;
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
		return runOptionsScript(runner, doc, webUser, "XML");
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
		return runOptionsScript(runner, doc, webUser, "XML2");
	}


	/**
	 * 根据打印时对应SelectField的显示类型不同,默认为MODIFY,返回的结果字符串不同.
	 * 若Document不为空且打印时对应SelectField的显示类型不为HIDDEN且字段类型不为HIDDEN,
	 * <P>
	 * 并根据Form模版的SelectField组件内容结合Document中的ITEM存放的值,返回重定义后的打印html文本. 否则为空字符串.
	 * 
	 * @param doc
	 *            Document(文档对象)
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
		Object value = null;
		if (doc != null) {
			Item item = doc.findItem(this.getName());
			if (item != null)
				value = item.getValue();
		}
		Iterator<Option> iter = options.getOptions().iterator();
		while (iter.hasNext()) {
			Option element = (Option) iter.next();
			if (element.getValue() != null) {
				html.append("<option");
				if (value != null && element.getValue() != null) {
					if (value.equals(element.getValue())) {
						html.append(" selected ");
					}
				} else {
					if (element.isDef()) {
						html.append(" selected ");
					}
				}
				html.append(" class='" + cssClass + "'");
				html.append(" value='");
			}
			html.append(HtmlEncoder.encode(element.getValue()));
			html.append("'");
			html.append(">");
			html.append(element.getOption()).append("</option>");
		}
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
			xmlText.append("<").append(MobileConstant.TAG_SUGGESTFIELD);
			xmlText.append(" ").append(MobileConstant.ATT_ID + "='" + getId() + "'");
			xmlText.append(" ").append(MobileConstant.ATT_NAME + "='" + getName() + "'");
			String value = "";
			if (doc != null) {
				Item item = doc.findItem(this.getName());
				if (item != null && item.getValue()!=null)
					value = item.getValue().toString();
			}
			xmlText.append(" ").append(MobileConstant.ATT_VALUE + "='" + value.toString() + "'");
			xmlText.append(" ").append(MobileConstant.ATT_LABEL).append("='").append(getMbLabel()).append("'");

			if (displayType == PermissionType.READONLY
					|| (getTextType() != null && getTextType().equalsIgnoreCase("readonly"))
					|| displayType == PermissionType.DISABLED) {
				xmlText.append(" ").append(MobileConstant.ATT_READONLY + "='true' ");
			}
			if (displayType == PermissionType.HIDDEN || (getTextType() != null && getTextType().equalsIgnoreCase("hidden"))) {
				xmlText.append(" ").append(MobileConstant.ATT_HIDDEN).append(" ='true' ");
				if(!getHiddenValue().equals("") && !getHiddenValue().equals(null) && !getHiddenValue().equals("&nbsp;")){
					xmlText.append(" ").append(MobileConstant.ATT_HIDDENVALUE).append("='").append(getHiddenValue()+"' ");
				}
			}

			if (isRefreshOnChanged()) {
				xmlText.append(" ").append(MobileConstant.ATT_REFRESH).append(" ='true' ");
			}
			xmlText.append(">");
			try {
				xmlText.append(runOptionsScriptToXML(runner, doc, webUser));
				xmlText.append("</").append(MobileConstant.TAG_SUGGESTFIELD + ">");
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
		boolean flag = true;
		while (iter.hasNext()) {
			Option element = (Option) iter.next();
			if (element.getValue() != null) {
				html.append("<").append(MobileConstant.TAG_OPTION).append("");
				if (flag) {
					if (value != null && element.getValue() != null) {
						if (value.equals(element.getValue())) {
							html.append(" ").append(MobileConstant.ATT_SELECTED).append("='" + count + "'");
							flag = false;
						}
					} else {
						if (element.isDef()) {
							html.append(" ").append(MobileConstant.ATT_SELECTED).append("='" + count + "'");
							flag = false;
						}
					}
				}
				html.append(" ").append(MobileConstant.ATT_VALUE).append("='");

				html.append(HtmlEncoder.encode(element.getValue()));
				html.append("'");

				html.append(">");

				if (element.getOption() != null && !element.getOption().trim().equals(""))
					html.append(HtmlEncoder.encode(element.getOption()));
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
		boolean flag = true;
		while (iter.hasNext()) {
			Option element = (Option) iter.next();
			if (element.getValue() != null) {
				html.append("<").append(MobileConstant2.TAG_OPTION);
				if (flag) {
					if (value != null && element.getValue() != null) {
						if (value.equals(element.getValue())) {
							html.append(" ").append(MobileConstant2.ATT_SELECTED).append("='true'");
							flag = false;
						}
					} else {
						if (element.isDef()) {
							html.append(" ").append(MobileConstant2.ATT_SELECTED).append("='true'");
							flag = false;
						}
					}
				}
				html.append(" ").append(MobileConstant2.ATT_VALUE).append("='");

				html.append(HtmlEncoder.encode(element.getValue()));
				html.append("'");

				html.append(">");

				if (element.getOption() != null && !element.getOption().trim().equals(""))
					html.append(HtmlEncoder.encode(element.getOption()));
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
	 * 获取Form模版的SelectField的选项脚本，通过动态语言执行器执行脚本后生成下拉选项,并验证下拉选项的可见性,如果可见就显示正常,否则显示
	 * "******"
	 * 
	 * @param runner
	 *            AbstractRunner(执行脚本的接口类)
	 * @param doc
	 *            (Document)文档对象
	 * @param webUser
	 *            webUser
 	 * @param columnOptionsCache
 	 * 			缓存控件选项(选项设计模式构建的Options)的Map
 	 * 			key--fieldId
	 *		    value--Options
	 * @return 执行后生成的下拉选项
	 */
	public String getText(Document doc, IRunner runner, WebUser webUser, Map<String,Options> columnOptionsCache) throws Exception {
		/*if(DATA_MODE_REMOTE.equals(this.getDataMode())){
			return "";
		}else {
			String text = (String) getValueFromSystemOptionItem(doc,doc.getItemValueAsString(getName()));
			return !StringUtil.isBlank(text)? text : getText4Synchronous(doc, runner, webUser, columnOptionsCache);
		}*/
		 // 不对模式进行校验
		String text = (String) getValueFromSystemOptionItem(doc,doc.getItemValueAsString(getName()));
		return !StringUtil.isBlank(text)? text : getText4Synchronous(doc, runner, webUser, columnOptionsCache);
	}
	
	private String getValueByDBType(String value, String applicationId) throws Exception{
		String dbType = DbTypeUtil.getDBType(applicationId);
		if(dbType.equals(DbTypeUtil.DBTYPE_MSSQL)){
			value = value.replaceAll("%", "[%]");
			value = value.replaceAll("_", "[_]");
		}else {
			value = value.replaceAll("%", "\\\\%");
			value = value.replaceAll("_", "\\\\_");
		}
		return value;
	}
	
	/**
	 * 返回下拉框同步模式的真实值
	 * @param doc
	 * @param runner
	 * @param webUser
	 * @return
	 * @throws Exception 
	 */
	public String getText4Synchronous(Document doc, IRunner runner, WebUser webUser, Map<String,Options> columnOptionsCache) throws Exception{
		if (!StringUtil.isBlank(doc.getParentid()) && getDisplayType(doc, runner, webUser) == PermissionType.HIDDEN) {
			return this.getHiddenValue();
		}

		Options options = columnOptionsCache != null ? columnOptionsCache.get(this.getId()) : null;
		if(options == null){
			options = getOptions(runner);
		}
		if (options != null) {
			for (Iterator<Option> iterator = options.getOptions().iterator(); iterator.hasNext();) {
				Option option = (Option) iterator.next();
				String tempValue = option.getValue();
				if(tempValue.contains("\\")){
					tempValue = tempValue.replaceAll("\\\\", "");
				}
				if (tempValue.equals(doc.getItemValueAsString(getName()))) {
					return option.getOption();
				}
			}
		}

		return "";
	}
	
	@Override
	public String toMbXMLText2(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		StringBuffer xmlText = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);

		if (doc != null) {
			xmlText.append("<").append(MobileConstant2.TAG_SUGGESTFIELD);
			xmlText.append(" ").append(MobileConstant2.ATT_ID).append("='").append(getId()).append("'");
			xmlText.append(" ").append(MobileConstant2.ATT_NAME).append("='").append(getName()).append("'");
			String value = "";
			if (doc != null) {
				Item item = doc.findItem(this.getName());
				if (item != null && item.getValue()!=null)
					value = item.getValue().toString();
			}
			xmlText.append(" ").append(MobileConstant2.ATT_VALUE).append("='").append(value.toString()).append("'");
			xmlText.append(" ").append(MobileConstant2.ATT_LABEL).append("='").append(getMbLabel()).append("'");

			if (displayType == PermissionType.READONLY
					|| (getTextType() != null && getTextType().equalsIgnoreCase("readonly"))
					|| displayType == PermissionType.DISABLED) {
				xmlText.append(" ").append(MobileConstant2.ATT_READONLY).append("='true'");
			}
			if (displayType == PermissionType.HIDDEN || (getTextType() != null && getTextType().equalsIgnoreCase("hidden"))) {
				xmlText.append(" ").append(MobileConstant2.ATT_HIDDEN).append(" ='true'");
				if(!getHiddenValue().equals("") && !getHiddenValue().equals(null) && !getHiddenValue().equals("&nbsp;")){
					xmlText.append(" ").append(MobileConstant2.ATT_HIDDENVALUE).append("='").append(getHiddenValue()).append("'").append(" ");
				}
			}

			if (isRefreshOnChanged()) {
				xmlText.append(" ").append(MobileConstant2.ATT_REFRESH).append("='true'");
			}
			xmlText.append(">");
			try {
				xmlText.append(runOptionsScriptToXML2(runner, doc, webUser));
				xmlText.append("</").append(MobileConstant2.TAG_SUGGESTFIELD).append(">");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return xmlText.toString();
	}

	@Override
	public Document createSystemOptionItem(Document doc, ParamsTable params,Item item) {
		//1.该控件下冗余字段的命名规则
		//2.拼接JSON数据  判断该item 是否存在，数据是否为空
		//3.返回
		Object code = item.getValue();
		
		//1.该控件值是否为空
		if(code != null){
			String fieldName = this.getName();
			//suggestField控件的拼接条件为(字段名)_show
			String OptionField = fieldName+"_show";
			String show = params.getParameterAsString(OptionField);
			
			if(!StringUtil.isBlank(fieldName) && !StringUtil.isBlank(OptionField) && !StringUtil.isBlank(show) ){
				JSONArray Sys_Option = StringUtil.isBlank(doc.getOptionItem())?new JSONArray():JSONArray.fromObject(doc.getOptionItem());
				JSONObject column =  new JSONObject();
				column.put("column", item.getName());
			    //显示值和隐藏值
				JSONObject Value_Code =  new JSONObject();
				Value_Code.put("key",show);
				Value_Code.put("value",code);
				column.put("value", Value_Code);
				
				int size = Sys_Option.size();
				
				JSONArray _Sys_Option = new JSONArray();
				//去重
				for(int index = 0 ; index < size ; index ++){
					JSONObject obj = Sys_Option.getJSONObject(index);
					if(!obj.get("column").equals(item.getName())){
						_Sys_Option.add(obj);
					}
				}
				_Sys_Option.add(column);
				doc.setOptionItem(_Sys_Option.toString());
			}
		}
		
		return doc;
	}

	@Override
	public Object getValueFromSystemOptionItem(Document doc,Object value) {
		
		String optionItem = doc.getOptionItem();
		if(!StringUtil.isBlank(optionItem)){
			JSONArray optionItemArray = JSONArray.fromObject(optionItem);
			Iterator<JSONObject> iter = optionItemArray.iterator();
			while(iter.hasNext()){
				JSONObject next = iter.next();
				String column = next.getString("column");
				if(this.getName().equals(column)){
					JSONObject colValue = next.getJSONObject("value");
					value = colValue.get("key");
					return value;
			}
		 }
		}
		return value;
	}
	
	
	/**
	 * 获取字段的显示值和真实值
	 * 
	 * @param doc 文档
 	 * @param runner 脚本运行器
 	 * @param webUser webuser
 	 * @param columnOptionsCache
 	 * 			缓存控件选项(选项设计模式构建的Options)的Map
 	 * 			key--fieldId
	 *		    value--Options
	 * @return
	 * @throws Exception
	 */
	public String getValueAndText(Document doc, IRunner runner, WebUser webUser, Map<String,Options> columnOptionsCache) throws Exception {
		String fileFullName = getFieldValueAndText(doc);
		if (fileFullName != null) {
			return fileFullName;
		}
		return super.getText(doc, runner, webUser);
	}

	/**
	 * 获取表单域真实值和显示值
	 * 
	 * @param doc
	 * @return
	 */
	protected String getFieldValueAndText(Document doc) {
		String result = null ;
		try {
			result = getFieldValue(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	 String getFieldValue(Document doc) {
		 String rtn = "";
			if (doc != null) {
				String optionItem = doc.getOptionItem();
				JSONArray opts = JSONArray.fromObject(optionItem);
				if(opts.size()>0){
					for(int index = 0 ; index < opts.size() ; index ++){
						JSONObject next = opts.getJSONObject(index);
						if(!next.isNullObject() && next.getString("column").equals(this.getName())){
							rtn = next.getString("value");
							break ;
						}
					}
				}
		     }
			if(StringUtil.isBlank(rtn)){   //防止前台JS渲染出错
				rtn = "[{\"value\":\"\",\"key\":\"\"}]";
			}
		return rtn;
	}
}

	
