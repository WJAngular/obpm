package cn.myapps.core.dynaform.form.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.table.constants.MobileConstant;
import cn.myapps.core.table.constants.MobileConstant2;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.HtmlEncoder;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

public class UserField extends FormField implements ValueStoreField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7936079412655964741L;

	protected static String cssClass = "input-cmd";

	private String filterField;

	private boolean limitByUser;
	
	/**
	 * 限制选择用户的数量
	 */
	protected String limitSum;
	
	/**
	 * 用户选择模式
	 */
	private String selectMode;


	/**
	 * 是否限制用户列表,true: 是限制用户,则是定制时选择的角色下的用户,false: 不限制用户,带出系统所有用户
	 * 
	 * @return
	 */
	public boolean isLimitByUser() {
		return limitByUser;
	}

	/**
	 * 设置是否限制用户列表
	 * 
	 * @param limitByUser
	 *            boolean
	 */
	public void setLimitByUser(boolean limitByUser) {
		this.limitByUser = limitByUser;
	}

	/**
	 * 获取过滤条件
	 * 
	 * @return 过滤条件
	 */
	public String getFilterField() {
		return filterField;
	}

	/**
	 * 设置过滤条件
	 * 
	 * @param filterField
	 */
	public void setFilterField(String filterField) {
		this.filterField = filterField;
	}
	
	/**
	 * 获取选择模式
	 * @return selectMode
	 */
	public String getSelectMode() {
		return selectMode;
	}

	/**
	 * 设置选择模式
	 * @param selectMode
	 */
	public void setSelectMode(String selectMode) {
		this.selectMode = selectMode;
	}

	/**
	 * 用于为手机平台XML串生成
	 * 
	 * @param runner
	 *            动态脚本执行器
	 * @param doc
	 *            文档对象
	 * @param webUser
	 *            webUser
	 * @return 手机平台XML串生成
	 */
	public String toMbXMLText(Document doc, IRunner runner, WebUser webUser) throws Exception {
		StringBuffer xmlText = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);

		if (doc != null) {
			xmlText.append("<").append(MobileConstant.TAG_USERFIELD);
			xmlText.append(" ").append(MobileConstant.ATT_ID + "='" + getId() + "'");
			xmlText.append(" ").append(MobileConstant.ATT_NAME + "='" + getName() + "'");
			xmlText.append(" ").append(MobileConstant.ATT_LABEL).append("='").append(getMbLabel()).append("'");

			if (displayType == PermissionType.READONLY
					|| (getTextType() != null && getTextType().equalsIgnoreCase("readonly"))
					|| displayType == PermissionType.DISABLED) {
				xmlText.append(" ").append(MobileConstant.ATT_READONLY + "='true' ");
			}
			if (displayType == PermissionType.HIDDEN || 
					(getTextType() != null && getTextType().equalsIgnoreCase("hidden"))) {
				xmlText.append(" ").append(MobileConstant.ATT_HIDDEN).append(" ='true' ");
				if(!getHiddenValue().equals("") && !getHiddenValue().equals(null)){
					xmlText.append(" ").append(MobileConstant.ATT_HIDDENVALUE).append("='").append(getHiddenValue()+"' ");
				}
			}
			xmlText.append(" ").append(MobileConstant.ATT_SELECTMODE).append("='").append(getSelectMode()+ "' ");
			if (isRefreshOnChanged()) {
				xmlText.append(" ").append(MobileConstant.ATT_REFRESH).append(" ='true' ");
			}
			xmlText.append(">");
			try {
				xmlText.append(toOptionXML(doc, webUser));
				xmlText.append("</").append(MobileConstant.TAG_USERFIELD + ">");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return xmlText.toString();
	}
	
	/**
	 * from 2.5sp9
	 * 用于为手机平台XML串生成
	 * 
	 * @param runner
	 *            动态脚本执行器
	 * @param doc
	 *            文档对象
	 * @param webUser
	 *            webUser
	 * @return 手机平台XML串生成
	 */
	public String toMbXMLText2(Document doc, IRunner runner, WebUser webUser) throws Exception {
		StringBuffer xmlText = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);

		if (doc != null) {
			xmlText.append("<").append(MobileConstant2.TAG_USERFIELD);
			xmlText.append(" ").append(MobileConstant2.ATT_ID).append("='").append(getId()).append("'");
			xmlText.append(" ").append(MobileConstant2.ATT_NAME).append("='").append(getName()).append("'");
			xmlText.append(" ").append(MobileConstant2.ATT_LABEL).append("='").append(getMbLabel()).append("'");

			if (displayType == PermissionType.READONLY
					|| (getTextType() != null && getTextType().equalsIgnoreCase("readonly"))
					|| displayType == PermissionType.DISABLED) {
				xmlText.append(" ").append(MobileConstant2.ATT_READONLY).append("='true'");
			}
			if (displayType == PermissionType.HIDDEN || (getTextType() != null && getTextType().equalsIgnoreCase("hidden"))) {
				xmlText.append(" ").append(MobileConstant2.ATT_HIDDEN).append("='true'");
				if(!getHiddenValue().equals("") && !getHiddenValue().equals(null) && !getHiddenValue().equals("&nbsp;")){
					xmlText.append(" ").append(MobileConstant2.ATT_HIDDENVALUE).append("='").append(getHiddenValue()).append("'");
				}
			}
			xmlText.append(" ").append(MobileConstant2.ATT_MODE).append("='").append(getSelectMode()).append("'");
			if (isRefreshOnChanged()) {
				xmlText.append(" ").append(MobileConstant2.ATT_REFRESH).append("='true'");
			}
			xmlText.append(">");
			try {
				xmlText.append(toOptionXML2(doc, webUser));
				xmlText.append("</").append(MobileConstant2.TAG_USERFIELD).append(">");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return xmlText.toString();
	}

	private String toOptionXML(Document doc, WebUser webUser) {
		StringBuffer html = new StringBuffer();
		try {
			Collection<Option> deptOptions = getUserOptions(doc, webUser).getOptions();
			Object value = null;
			if (doc != null) {
				Item item = doc.findItem(this.getName());
				if (item != null)
					value = item.getValue();
			}

			Iterator<Option> iter = deptOptions.iterator();
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
					html.append("</").append(MobileConstant.TAG_OPTION).append(">");
					count++;
				}
			}
			return html.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		html.append("<").append(MobileConstant.TAG_OPTION).append(">");
		html.append("</").append(MobileConstant.TAG_OPTION).append(">");
		return html.toString();
	}
	
	private String toOptionXML2(Document doc, WebUser webUser) {
		StringBuffer html = new StringBuffer();
		try {
			Collection<Option> deptOptions = getUserOptions(doc, webUser).getOptions();
			String value = null;
			if (doc != null) {
				Item item = doc.findItem(this.getName());
				if (item != null)
					value = (String)item.getValue();
			}

			Iterator<Option> iter = deptOptions.iterator();
			while (iter.hasNext()) {
				Option element = (Option) iter.next();
				if (element.getValue() != null) {
					html.append("<").append(MobileConstant2.TAG_OPTION).append(" ");
					if (value != null && element.getValue() != null) {
						if (value.indexOf(element.getValue()) != -1) {
							html.append(" ").append(MobileConstant2.ATT_SELECTED).append("='true'");
						}
					} else {
						if (element.isDef()) {
							html.append(" ").append(MobileConstant2.ATT_SELECTED).append("='true'");
						}
					}
					html.append(" ").append(MobileConstant2.ATT_VALUE).append("='").append(HtmlEncoder.encode(element.getValue())).append("'");
					
					html.append(">");

					if (element.getOption() != null && !element.getOption().trim().equals(""))
						html.append(HtmlEncoder.encode(element.getOption()));
					else
						html.append("");
					html.append("</").append(MobileConstant2.TAG_OPTION).append(">");
				}
			}
			return html.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		html.append("<").append(MobileConstant2.TAG_OPTION).append(">");
		html.append("</").append(MobileConstant2.TAG_OPTION).append(">");
		return html.toString();
	}

	/**
	 * 获取用户组件的下拉选项
	 * 
	 * @param doc
	 *            Document(文档对象)
	 * @param webUser
	 *            webUser
	 * @return 获取用户选项
	 * @throws Exception
	 */
	private Options getUserOptions(Document doc, WebUser webUser) throws Exception {
		Collection<UserVO> usertList = new ArrayList<UserVO>();
		Options options = new Options();
		UserProcess process = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
//		Collection<UserVO> tempUserList = null;
		if (getFilterField() != null && isLimitByUser() == false) {
			ParamsTable params = new ParamsTable();
			params.setParameter("_orderby", "orderByNo");
			params.setParameter("domain", webUser.getDomainid());
			params.setParameter("_pagelines", String.valueOf(Integer.MAX_VALUE));
			DataPackage<UserVO> datas = process.queryForUserDialog(params,webUser,null,null);
//			tempUserList = process.queryByDomain(webUser.getDomainid(), 1, Integer.MAX_VALUE);

//			UserVO userVO = null;
//			if (tempUserList != null) {
//				for (Iterator<UserVO> iterator = tempUserList.iterator(); iterator.hasNext();) {
//					userVO = (UserVO) iterator.next();
//					usertList.add(userVO);
//				}
//			}
			//options.add(new Option("", ""));
			if(datas.rowCount >0){
				for (Iterator<UserVO> iterator = datas.datas.iterator(); iterator.hasNext();) {
					UserVO userVO1 = (UserVO) iterator.next();
					options.add(userVO1.getName(), userVO1.getId(), isDefaultValue(doc, userVO1.getId()));
				}
			}
		} else {
			DataPackage<UserVO> UserListTemp = process.doQueryByRoleId(getFilterField());
			Collection<UserVO> collection = UserListTemp.getDatas();
			UserVO userVO = null;
			if (collection != null) {
				for (Iterator<UserVO> iterator = collection.iterator(); iterator.hasNext();) {
					userVO = (UserVO) iterator.next();
					usertList.add(userVO);
				}
				//options.add(new Option("", ""));
				for (Iterator<UserVO> iterator = usertList.iterator(); iterator.hasNext();) {
					UserVO userVO1 = (UserVO) iterator.next();
					options.add(userVO1.getName(), userVO1.getId(), isDefaultValue(doc, userVO1.getId()));
				}
			}
		}
		return options;
	}

	/**
	 * 获取模板描述
	 * 
	 * @return 模板描述
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
		// template.append(" departmentlevel='" + getDepartmentLevel() + "'");
		template.append(" fileFiled='" + getFilterField() + "'");
		template.append("/>");
		return template.toString();
	}

	/**
	 * 根据Form模版的UserField组件内容结合Document中的ITEM存放的值,输出重定义后的html文本以网格显示
	 * 
	 * @param runner
	 *            动态脚本执行器
	 * @param doc
	 *            文档对象
	 * @param webUser
	 *            webUser
	 * @return 重定义后的html文本
	 */
	public String toGridHtmlText(Document doc, IRunner runner, WebUser webUser,Map<String,Options> columnOptionsCache) throws Exception {
		StringBuffer html = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);

		if (displayType == PermissionType.HIDDEN) {
			return this.getHiddenValue();
		} else {
			html.append("<input type='text' moduleType='userfield' ");
			if (getTextType().equalsIgnoreCase("hidden")) {
				html.append(" ishidden='true'");
			}if(displayType==PermissionType.DISABLED){
				html.append(" isDisabled='true'");
			}
			html.append(" _subGridView='true'");
			html.append(toAttr(doc, displayType));
		}
		return html.toString();
	}

	/**
	 * 
	 * Form模版的UserField内容结合Document中的ITEM存放的值,返回重定义后的html，
	 * 
	 * @param doc
	 *            文档对象
	 * @see cn.myapps.base.action.ParamsTable#params
	 * @see cn.myapps.core.macro.runner.AbstractRunner#run(String, String)
	 * @return 重定义后的html为Form模版的UserField内容结合Document中的ITEM存放的值,
	 * 
	 */
	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser, int permissionType) throws Exception {
		StringBuffer html = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser, permissionType);
			// Text Field HTML
			html.append("<input type='text' moduleType='userfield' ");
			html.append(toOtherpropsHtml());
			html.append(" displayType = '" + displayType + "'");
			html.append(toAttr(doc, displayType));

		return html.toString();
	}

	/**
	 * 生成控件相关属性
	 * 
	 * @param doc
	 * @param displayType
	 * @return
	 */
	public String toAttr(Document doc, int displayType) {
		StringBuffer html = new StringBuffer();
		// 输入框部分
		html.append(" class = '" + cssClass + "'");
		html.append(" _id = '" + getFieldId(doc) + "'");
		html.append(" _displayType = '" + displayType + "'");
		html.append(" _name='"+ getName() + "'");
		html.append(" _fieldType = '" + getTagName() + "'");
		html.append(" _limitSum= '" + this.getLimitSum()+"'");
		html.append(" _getFieldValue='" + getFieldValue(doc) + "'");
		html.append(" _isRefreshOnChanged= '" + isRefreshOnChanged() + "'");
		html.append(" _textType='" + getTextType() + "'");
		html.append(" _selectMode= '" + getSelectMode() + "'");
		html.append(" _hiddenValue= '" + getHiddenValue() + "'");
		//表单描述字段
		html.append(" _discript ='" + getDiscript() + "'");
		if (displayType == PermissionType.READONLY || getTextType().equals("readonly")|| displayType == PermissionType.DISABLED) {
			 html.append(" readonly= 'true'");
		}else if (displayType == PermissionType.MODIFY) {
			html.append(" readonly='false'");
		}
		if (doc != null) {
			html.append(" value='").append(getFieldText(doc)).append("'");
		}
		if (!getTextType().equalsIgnoreCase("hidden") && displayType != PermissionType.HIDDEN) {
			html.append(" noHidden='true'");
		}
		String roleid = "";
		if (getFilterField() != null && isLimitByUser()) {
			roleid = getFilterField();
		}
		html.append(" _clearlabel='{*[Clear]*}'");
		html.append(" _roleid='" + roleid + "'");
		html.append(" _selectUser='{*[Select]*}{*[User]*}'");
		html.append(" _add='{*[Add]*}' ");
		html.append(" />");
		return html.toString();
	}

	/**
	 * 获取表单域真实值
	 * 
	 * @param doc
	 * @return
	 */
	protected String getFieldValue(Document doc) {
		String rtn = "";
		if (doc != null) {
			Item item = doc.findItem(this.getName());
			// 文本类型取值
			if (item != null && item.getValue() != null) {
				Object value = item.getValue();
				if (StringUtil.isBlank((String) value)) {
				} else {
					String valueStr = HtmlEncoder.encode((String)value+"");
					valueStr = valueStr != null && !valueStr.equals("null") ? valueStr : "";
					rtn = valueStr;
				}
			}
		}
		return rtn;
	}

	/**
	 * 获取表单域显示值
	 * 
	 * @param doc
	 * @return
	 */
	protected String getFieldText(Document doc) {
		StringBuffer rtn = new StringBuffer();
		try {
			UserProcess process = (UserProcess) ProcessFactory.createProcess(UserProcess.class);

			String valueListStr = getFieldValue(doc);
			if (!StringUtil.isBlank(valueListStr)) {
				String[] values = valueListStr.split(";");
				if (values.length > 0) {
					for (int i = 0; i < values.length; i++) {
						UserVO user = (UserVO) process.doView(values[i]);
						if (user != null) {
							rtn.append(user.getName()).append(";");
						}
					}
					if(rtn.length()>0){
						rtn.setLength(rtn.length()-1);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rtn.toString();
	}

	/**
	 * @打印
	 */
	public String toPrintHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception {
		StringBuffer html = new StringBuffer();
		int displayType = getPrintDisplayType(doc, runner, webUser);
		int isdisplay = getDisplayType(doc, runner, webUser);
		
		if (displayType == PermissionType.HIDDEN || isdisplay==PermissionType.HIDDEN) {
			if(!this.getPrintHiddenValue().equals("")){
				return this.getPrintHiddenValue();
			}else{
				return "";
			}
		}

		if (!getTextType().equalsIgnoreCase("hidden")) {
			Item item = doc.findItem(this.getName());
			if (item != null && item.getValue() != null) {
				html.append("<SPAN>");
				html.append(getFieldText(doc));
				html.append("</SPAN>");
			}
		}
		return html.toString();

	}

	/**
	 * If the option value is default, return true.
	 * 
	 * @param doc
	 * @param optionValue
	 * @return true or false
	 */
	private boolean isDefaultValue(Document doc, String optionValue) {
		Item item = doc.findItem(getName());
		if (item != null && optionValue.equals(item.getValue())) {
			return true;
		}
		return false;
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
	 * 获取字段显示值
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
	public String getText(Document doc, IRunner runner, WebUser webUser, Map<String,Options> columnOptionsCache) throws Exception {
		String fileFullName = getFieldText(doc);
		if (fileFullName != null) {
			return fileFullName;
		}
		return super.getText(doc, runner, webUser);
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
		StringBuffer rtn = new StringBuffer();
		String result = null ;
		try {
			UserProcess process = (UserProcess) ProcessFactory.createProcess(UserProcess.class);

			String valueListStr = getFieldValue(doc);
			if (!StringUtil.isBlank(valueListStr)) {
				String[] values = valueListStr.split(";");
				if (values.length > 0) {
					for (int i = 0; i < values.length; i++) {
						UserVO user = (UserVO) process.doView(values[i]);
						if (user != null) {
							rtn.append(user.getName()).append(";");
						}
					}
					if(rtn.length()>0){
						rtn.setLength(rtn.length()-1);
					}
				}
			}
			
			result = "[{\"value\":\""+ valueListStr +"\",\"key\":\""+ rtn.toString() +"\"}]";
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		return result;
	}

	public String getLimitSum() {
		return limitSum;
	}

	public void setLimitSum(String limitSum) {
		this.limitSum = limitSum;
	}
	
	public String toPdfHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception {
		StringBuffer html = new StringBuffer();
		int isdisplay = getDisplayType(doc, runner, webUser);
		
		if (isdisplay==PermissionType.HIDDEN) {
				return "";
		}

		if (!getTextType().equalsIgnoreCase("hidden")) {
			Item item = doc.findItem(this.getName());
			if (item != null && item.getValue() != null) {
				html.append("<SPAN >");
				html.append(getFieldText(doc));
				html.append("</SPAN>");
				//html.append(printHiddenElement(doc));
			}
		}
		return html.toString();
	}
}
