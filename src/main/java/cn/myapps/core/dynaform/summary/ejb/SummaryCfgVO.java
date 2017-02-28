package cn.myapps.core.dynaform.summary.ejb;

import java.util.ArrayList;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.form.ejb.FormField;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.util.StringUtil;

/**
 * @author Happy
 *
 */
public class SummaryCfgVO extends ValueObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4163612089696620469L;
	
	/**
	 * 作用于首页的代办概要描述
	 */
	public static final int SCOPE_PENDING = 0;
	/**
	 * 作用于流程的通知模板
	 */
	public static final int SCOPE_NOTIFY = 1;
	
	/**
	 * 作用于流程的邮件提醒
	 */
	public static final int SCOPE_EMAIL = 2;
	
	/**
	 * 作用于流程的手机短信提醒
	 */
	public static final int SCOPE_SMS = 3;
	
	/**
	 *作用于流程的站内短信提醒 
	 */
	public static final int SCOPE_MESSAGE = 4;
	
	/**
	 * 作用于我的工作和流程监控的主题字段
	 */
	public static final int SCOPE_SUBJECT = 5;
	
	/**
	 * 作用于抄送代阅列表
	 */
	public static final int SCOPE_CIRCULATOR= 6;
	
	
	/**
	 * 标题
	 */
	private String title;
	
	/**
	 * 设计模式
	 */
	private String type;
	
	
	/**
	 * 关联表单的ID
	 */
	private String formId;
	
	/**
	 * 摘要字段
	 */
	private String fieldNames;
	
	/**
	 * 摘要脚本
	 */
	private String summaryScript;
	
//	private UserDefined userDefined;
	
	/**
	 * 作用域
	 */
	private int scope;
	
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFieldNames() {
		return fieldNames;
	}

	public void setFieldNames(String fieldNames) {
		this.fieldNames = fieldNames;
	}

	public String getSummaryScript() {
		return summaryScript;
	}

	public void setSummaryScript(String summaryScript) {
		this.summaryScript = summaryScript;
	}

	/**
	 * @deprecated 2.5sp8开始移除摘要对数据排序的设置、并计划在后续版本把此功能添加到小工具里面
	 * @return
	 */
	public String getOrderby() {
		return "LASTMODIFIED";
	}
	
	public int getScope() {
		return scope;
	}

	public void setScope(int scope) {
		this.scope = scope;
	}

	/**生成摘要文本
	 * @param doc
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public String toSummay(Document doc,WebUser user) throws Exception {
		StringBuffer ItemValue = new StringBuffer();
		StringBuffer builder = new StringBuffer();
		if (getType() != null && getType().equals("00")) {
			IRunner runner = JavaScriptFactory.getInstance("", doc.getApplicationid());
			runner.initBSFManager(doc, new ParamsTable(), user, new ArrayList<ValidateMessage>());
			if (!StringUtil.isBlank(fieldNames)) {
				String[] _fieldNames = fieldNames.split(";");
				for (int i = 0; i < _fieldNames.length; i++) {
					String fieldName = _fieldNames[i];
					if(fieldName !=null && fieldName.trim().length()>0){
						if(fieldName.startsWith("$")){
							ItemValue.append(doc.getValueByField(fieldName)).append("    ");
						}else{
							FormField field = doc.getForm().findFieldByName(fieldName);
							if(field != null){
								ItemValue.append(field.getText(doc, runner, user)).append("    ");
							}
						}
					}
				}
				builder.append(ItemValue.toString() != null ? ItemValue.toString() : "" + " ");
			} else {
				throw new OBPMValidateException("*[FieldNames is empty]*");
			}
			return builder.toString();
		} else if (getType() != null && getType().equals("01")) {
			IRunner runner = JavaScriptFactory.getInstance("", doc.getApplicationid());
			runner.initBSFManager(doc, new ParamsTable(), user, new ArrayList<ValidateMessage>());
			String js = (String) runner.run("Documment SummaryScript:"+this.getId() + doc.getFormid(), this.getSummaryScript());
			return js;
		}
		return "";

	}
	
	public String toText(Document doc, BaseUser user) throws Exception {
		StringBuffer ItemValue = new StringBuffer();
		StringBuffer builder = new StringBuffer();
		if (getType() != null && getType().equals("00")) {
			if (!StringUtil.isBlank(fieldNames)) {
				String[] _fieldNames = fieldNames.split(";");
				for (int i = 0; i < _fieldNames.length; i++) {
					String fieldName = _fieldNames[i];
					if(fieldName !=null && fieldName.trim().length()>0){
						ItemValue.append(doc.getValueByField(fieldName)).append("&#160;&#160;&#160;");
					}
				}
				builder.append(ItemValue.toString() != null ? ItemValue.toString() : "" + " ");
			} else {
				throw new OBPMValidateException("*[Culum is empty]*");
			}
			return replace(builder.toString());
		} else if (getType() != null && getType().equals("01")) {
			IRunner runner = JavaScriptFactory.getInstance("", getApplicationid());
			runner.initBSFManager(doc, new ParamsTable(), new WebUser(user), new ArrayList<ValidateMessage>());
			String js = (String) runner.run(getText(doc), this.getSummaryScript());
			return js;
		}
		return "";
	}

	public String getText(Document doc) throws Exception {
		StringBuffer builder = new StringBuffer();
		builder.append("REMINDER.").append(title);
		builder.append(".Column(").append(getId()).append(")." + title);
		return builder.toString();
	}
	
	private String replace(String str) {
		str = str == null ? "" : str;
		str = str.replaceAll("\\<br>", "\n");
		str = str.replaceAll("\\<br/>", "\n");
		return str;
	}

/*	public UserDefined getUserDefined() {
		return userDefined;
	}

	public void setUserDefined(UserDefined userDefined) {
		this.userDefined = userDefined;
	}*/

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}
