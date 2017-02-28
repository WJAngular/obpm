package cn.myapps.core.dynaform.form.ejb;

import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Map;

import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.table.constants.MobileConstant;
import cn.myapps.core.table.constants.MobileConstant2;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.core.dynaform.form.ejb.Options;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.HtmlEncoder;

public class UserSelectField extends FormField implements ValueStoreField {
	
	private static final long serialVersionUID = 1837414164014754832L;

	protected static String cssClass = "user-cmd";

	private String filterField;

	private boolean limitByUser;
	

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
			xmlText.append("<").append(MobileConstant.TAG_SELECTFIELD);
			xmlText.append(" ").append(MobileConstant.ATT_ID + "='" + getId() + "'");
			xmlText.append(" ").append(MobileConstant.ATT_NAME + "='" + getName() + "'");
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
				xmlText.append(toOptionXML(doc, webUser));
				xmlText.append("</").append(MobileConstant.TAG_SELECTFIELD + ">");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return xmlText.toString();
	}

	private String toOptionXML2(Document doc, WebUser webUser) {
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
			boolean flag = true;
			while (iter.hasNext()) {
				Option element = (Option) iter.next();
				if (element.getValue() != null) {
					html.append("<OPTION").append(" ");
					if (flag) {
						if (value != null && element.getValue() != null) {
							if (value.equals(element.getValue())) {
								html.append("SELECTED='true'").append(" ");
								flag = false;
							}
						} else {
							if (element.isDef()) {
								html.append("SELECTED='true'").append(" ");
								flag = false;
							}
						}
					}
					html.append("VALUE").append("='").append(HtmlEncoder.encode(element.getValue())).append("'");
					
					html.append(">");

					if (element.getOption() != null && !element.getOption().trim().equals(""))
						html.append(HtmlEncoder.encode(element.getOption()));
					else
						html.append("");
					html.append("</OPTION>");
				}
			}
			return html.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		html.append("<OPTION>");
		html.append("</OPTION>");
		return html.toString();
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
			xmlText.append("<").append(MobileConstant2.TAG_SELECTFIELD);
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
			if (isRefreshOnChanged()) {
				xmlText.append(" ").append(MobileConstant2.ATT_REFRESH).append("='true'");
			}
			xmlText.append(">");
			try {
				xmlText.append(toOptionXML2(doc, webUser));
				xmlText.append("</").append(MobileConstant2.TAG_SELECTFIELD).append(">");
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
					else
						html.append("{*[Select]*}");
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
		Collection<UserVO> tempUserList = null;
		if (getFilterField() != null && isLimitByUser() == false) {
			tempUserList = process.queryByDomain(webUser.getDomainid(), 1, Integer.MAX_VALUE);

			UserVO userVO = null;
			if (tempUserList != null) {
				for (Iterator<UserVO> iterator = tempUserList.iterator(); iterator.hasNext();) {
					userVO = (UserVO) iterator.next();
					usertList.add(userVO);
				}
			}
			options.add(new Option("", ""));
			for (Iterator<UserVO> iterator = usertList.iterator(); iterator.hasNext();) {
				UserVO userVO1 = (UserVO) iterator.next();
				options.add(userVO1.getName(), userVO1.getId(), isDefaultValue(doc, userVO1.getId()));
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
				options.add(new Option("", ""));
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
	public String toGridHtmlText(Document doc, IRunner runner, WebUser webUser, Map<String,Options> columnOptionsCache) throws Exception {
		StringBuffer html = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);

		if (displayType == PermissionType.HIDDEN) {
			return toHiddenHtml(doc);
		} else {
			if (doc != null) {
				html.append("<select");
				html.append(" style='display:");
				html.append(getTextType().equals("hidden") ? "none" : "inline");
				html.append(";width:100%'");
				html.append(toAttr(doc, displayType));
				html.append(">");
				try {
					html.append(toOptionsHtml(doc, webUser));
					html.append("</select>");
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
		return html.toString();
	}

	private String toOptionsHtml(Document doc, WebUser webUser) {
		StringBuffer htmlBuilder = new StringBuffer();
		try {
			Collection<Option> deptOptions = getUserOptions(doc, webUser).getOptions();
			for (Iterator<Option> iterator = deptOptions.iterator(); iterator.hasNext();) {
				Option option = (Option) iterator.next();
				if (option.getValue() != null) {
					htmlBuilder.append("<option value=");
					htmlBuilder.append("\"");
					htmlBuilder.append(option.getValue());
					htmlBuilder.append("\"");
					if (option.isDef()) {
						htmlBuilder.append(" selected ");
					}
					htmlBuilder.append(" class='" + cssClass + "'");
					htmlBuilder.append(">");
					htmlBuilder.append(option.getOption());
					htmlBuilder.append("</option>");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return htmlBuilder.toString();

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
		int displayType = getDisplayType(doc, runner, webUser,permissionType);

		if (displayType == PermissionType.HIDDEN) {
			return toHiddenHtml(doc);
		} else {
			if (doc != null) {
				html.append("<select");
				html.append(" style='display:");
				html.append(getTextType().equals("hidden") ? "none" : "inline");
				html.append("'");
				html.append(toAttr(doc, displayType));
				html.append(" " + toOtherpropsHtml());
				html.append(">");
				try {
					html.append(toOptionsHtml(doc, webUser));
					html.append("</select>");
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
		return html.toString();
	}

	public String toAttr(Document doc, int displayType) {
		StringBuffer html = new StringBuffer();
		html.append(" id='" + getFieldId(doc) + "'");
		html.append(" name='" + getName() + "'");
		html.append(" fieldType='" + getTagName() + "'");
		if (displayType == PermissionType.READONLY || getTextType().equals("readonly")) {
			html.append(" disabled ");
		} else if (displayType == PermissionType.DISABLED) {
			html.append(" disabled ");
		}
		if (isRefreshOnChanged()) {
			html.append(" onchange='dy_refresh(this.id)'");
		}

		return html.toString();
	}

	/**
	 * @打印
	 */
	public String toPrintHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception {
		StringBuffer html = new StringBuffer();
		int displayType = getPrintDisplayType(doc, runner, webUser);

		if (displayType == PermissionType.HIDDEN) {
			return toHiddenHtml(doc);
		}

//		Iterator iter = getUserOptions(doc, webUser).getOptions().iterator();
//		while (iter.hasNext()) {
//			Option element = (Option) iter.next();
//			if (element.getValue() != null) {
//				html.append("<SPAN style=\"FONT-SIZE: 9pt\">");
//				html.append(element.getOption());
//				html.append("</SPAN>");
//			}
//		}
		
		
		if (!getTextType().equalsIgnoreCase("hidden")) {
			Item item = doc.findItem(this.getName());
			if (item != null && item.getValue() != null) {
				Object value = item.getValue();
				html.append("<SPAN >");
//				if (value instanceof Number) {
//					DecimalFormat format = new DecimalFormat(getNumberPattern());
//					html.append(format.format((Number) item.getValue()));
//				} else {
				UserProcess process = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
				    UserVO vo=  (UserVO)process.doView(value.toString());
					html.append(HtmlEncoder.encode(vo.getName()+""));
//				}
				html.append("</SPAN>");
				html.append(printHiddenElement(doc));

			}
		}
		return html.toString();

	}

	public String toPdfHtmlTxt(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		StringBuffer html = new StringBuffer();
		
		if (!getTextType().equalsIgnoreCase("hidden")) {
			Item item = doc.findItem(this.getName());
			if (item != null && item.getValue() != null) {
				Object value = item.getValue();
				html.append("<SPAN >");
				UserProcess process = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
				    UserVO vo=  (UserVO)process.doView(value.toString());
					html.append(HtmlEncoder.encode(vo.getName()+""));
				html.append("</SPAN>");
				//html.append(printHiddenElement(doc));

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
	 * @throws ParseException 
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
		Options options = getUserOptions(doc, webUser);
		for (Iterator<Option> iterator = options.getOptions().iterator(); iterator.hasNext();) {
			Option option = (Option) iterator.next();
			if (option.getValue().equals(doc.getItemValueAsString(getName()))) {
				return option.getOption();
			}
		}
		return "";
	}

}
