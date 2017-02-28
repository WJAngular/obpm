package cn.myapps.core.dynaform.form.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.table.constants.MobileConstant;
import cn.myapps.core.table.constants.MobileConstant2;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.HtmlEncoder;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

public class DepartmentField extends FormField implements ValueStoreField {

	private static final long serialVersionUID = -8469984159928806643L;

	protected static String cssClass = "department-cmd";

	protected static final int DEFAULT_TYPE_FIRST_OPTION = 16;
	protected static final int DEFAULT_TYPE_DEPARTMENT_OF_USER = 256;

	/**
	 * 计算多值选项
	 * 
	 * @uml.property name="departmentLevel"
	 */
	protected int departmentLevel;

	protected boolean limitByUser;

	protected int defaultOptionType;

	protected boolean allowEmpty;

	/**
	 * 级联的部门Field ID, 即上一级部门
	 * 
	 */
	protected String relatedField;

	/**
	 * 设置是否连动关联字段
	 * 
	 * @return 是否连动关联字段
	 */
	public String getRelatedField() {
		return relatedField;
	}

	/**
	 * 设置是否连动关联字段
	 * 
	 * @param relatedField
	 *            是否连动关联字段
	 */
	public void setRelatedField(String relatedField) {
		this.relatedField = relatedField;
	}

	/**
	 * 返回多值选项脚本
	 * 
	 * @return Returns the departmentLevel.
	 * @uml.property name="departmentLevel"
	 */
	public int getDepartmentLevel() {
		return departmentLevel;
	}

	/**
	 * 设置多值选项脚本
	 * 
	 * @param optionsScript
	 *            The optionsScript to set.
	 * @uml.property name="optionsScript"
	 */
	public void setDepartmentLevel(int departmentLevel) {
		this.departmentLevel = departmentLevel;
	}

	/**
	 * 重新计算SelectField.
	 * 
	 * @roseuid 41DB89D700F9
	 */
	public void recalculate(IRunner runner, Document doc, WebUser webUser) throws Exception {
		getLog().debug("SelectField.recalculate");
		runValueScript(runner, doc);
		Collection<Option> deptOptions = getDepartmentOptions(doc, webUser).getOptions();
		boolean isClear = true;
		if (!deptOptions.isEmpty()) {
			for (Iterator<Option> iterator = deptOptions.iterator(); iterator.hasNext();) {
				Option option = (Option) iterator.next();
				String value = doc.getItemValueAsString(this.getName());
				if (option.getValue().equals(value)) {
					isClear = false;
					break;
				}
			}
		}
		if (isClear) {
			Item item = doc.findItem(this.getName());
			if (item != null) {
				item.setValue("");
			}
		}
		
	}

	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser, int permissionType) throws Exception {
		StringBuffer html = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser, permissionType);

		if (doc != null) {
			html.append("<select moduleType='department' data-enhance='false' ");
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
		return html.toString();
	}

	public String toAttr(Document doc, int displayType) {
		StringBuffer html = new StringBuffer();
		html.append(" _id='" + getFieldId(doc) + "'");
		html.append(" _name='" + getName() + "'");
		html.append(" _fieldType='" + getTagName() + "'");
		html.append(" _isRefreshOnChanged='" + isRefreshOnChanged() + "'");
		html.append(" _displayType='" + displayType + "'");
		html.append(" _textType='" + getTextType() + "'");
		html.append(" _discript ='" + getDiscript() + "'");
		html.append(" _hiddenValue='" + getHiddenValue() + "'");
		return html.toString();
	}

	private String toOptionsHtml(Document doc, WebUser webUser) {
		StringBuffer htmlBuilder = new StringBuffer();
		try {
			Collection<Option> deptOptions = new ArrayList<Option>();
			// 是否允许空选项
			if (allowEmpty) {
				deptOptions.add(new Option("", ""));
			}
			deptOptions.addAll(getDepartmentOptions(doc, webUser).getOptions());

			// 设置默认值
			setDefaultValue(doc, deptOptions, webUser);

			// 输出选项HTML
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

	public void setDefaultValue(Document doc, Collection<Option> deptOptions, WebUser webUser) {
		// 是否已有选项
		boolean isSelectedOne = false;
		for (Option option : deptOptions) {
			if (option.isDef()) {
				isSelectedOne = true;
			}
		}

		// 设置默认选项
		if (!isSelectedOne) {
			Option defOption = null;
			switch (defaultOptionType) {
				case DEFAULT_TYPE_DEPARTMENT_OF_USER :
					String defaultDept = webUser.getDefaultDepartment();
					
					//若未找到默认部门，则设置第一个部门为默认选项
					if (!StringUtil.isBlank(defaultDept)) {
						for (Option option : deptOptions) {
							if (defaultDept.equals(option.getValue())) {
								defOption = option;
								option.setDef(true);
								break;
							}
						}
					} else {
						Option option = (Option) deptOptions.iterator().next();
						defOption = option;
						option.setDef(true);
					}
					break;
				case DEFAULT_TYPE_FIRST_OPTION :
				default :
					// 第一个为默认选项
					Option option = (Option) deptOptions.iterator().next();
					defOption = option;
					option.setDef(true);
					break;
			}

			// 为文档设置默认值
			Item item = doc.findItem(getName());
			if (item != null && defOption != null) {
				item.setValue(defOption.getValue());
			}
		}
	}

	/**
	 * 
	 * 拿到的什来拿到对应的部门 0时返回一个null 此时会拿到关联的部门值，为1时就是最顶给部门 ，下一级为2-1；
	 * 
	 * @return list
	 * @throws Exception
	 */
	private Options getDepartmentOptions(Document doc, WebUser webUser) throws Exception {
		Collection<DepartmentVO> deptList = new ArrayList<DepartmentVO>();
		Options options = new Options();

		DepartmentProcess process = (DepartmentProcess) ProcessFactory.createProcess(DepartmentProcess.class);
		Collection<DepartmentVO> tempDeptList = new ArrayList<DepartmentVO>();
		if (getDepartmentLevel() < 0) {		//添加所有级别的部门
			tempDeptList.addAll(process.queryByDomain(webUser.getDomainid()));
		} else {	//添加对应级别的部门
			tempDeptList.addAll(process.getDepartmentByLevel(getDepartmentLevel(), _form.getApplicationid(), webUser.getDomainid()));
		}

		if (!StringUtil.isBlank(relatedField)) {	//根据上级部门联动设置值过滤选项
			String superiorId = doc.getItemValueAsString(relatedField);
			if (!StringUtil.isBlank(superiorId)) {
				for (Iterator<DepartmentVO> iterator = tempDeptList.iterator(); iterator.hasNext();) {
					DepartmentVO tempDepartmentVO = (DepartmentVO) iterator.next();

					DepartmentVO currentTempDepartmentVO = tempDepartmentVO;
					// Recursive get superior department
					while (currentTempDepartmentVO.getSuperior() != null) {
						if (currentTempDepartmentVO.getSuperior().getId().equals(superiorId)) {
							if (isAllowedDepartment(tempDepartmentVO, webUser)) {	//只允许显示当前用户所属及下属部门时，过滤选项
								deptList.add(tempDepartmentVO);
							}
						}
						currentTempDepartmentVO = currentTempDepartmentVO.getSuperior();
					}

				}
			}
		} else {	//添加所有tempDeptList中的部门
			for (Iterator<DepartmentVO> iterator = tempDeptList.iterator(); iterator.hasNext();) {
				DepartmentVO departmentVO = (DepartmentVO) iterator.next();
				if (isAllowedDepartment(departmentVO, webUser)) {	//只允许显示当前用户所属及下属部门时，过滤选项
					if(!deptList.contains(departmentVO))
						deptList.add(departmentVO);
				}
			}
		}

		//过滤无效的部门
		for (Iterator<DepartmentVO> iterator = deptList.iterator(); iterator.hasNext();) {
			DepartmentVO departmentVO = (DepartmentVO) iterator.next();
			if(departmentVO.getValid() == 1){
				if(departmentVO.getSuperior() == null || (departmentVO.getSuperior() != null && departmentVO.getSuperior().getValid() == 1)){
					options.add(departmentVO.getName(), departmentVO.getId(), isDefaultValue(doc, departmentVO.getId()));
				}
			}
		}
		
		//字段值不在选项中时添加字段值为选项
		if(doc.findItem(name) !=null && doc.findItem(name).getValue() !=null && !StringUtil.isBlank((String) doc.findItem(name).getValue()) && isLimitByUser()){
			DepartmentVO departmentVO = (DepartmentVO) process.doView((String) doc.findItem(name).getValue());
			if(departmentVO !=null && !deptList.contains(departmentVO) && departmentVO.getValid() == 1)
				if(departmentVO.getSuperior() == null || (departmentVO.getSuperior() != null && departmentVO.getSuperior().getValid() == 1)){
					options.add(departmentVO.getName(), departmentVO.getId(), isDefaultValue(doc, departmentVO.getId()));
				}
		}

		return options;
	}

	/**
	 * Check the department whether to show by current user's department list
	 * 
	 * @param needCheckDepartment
	 * @param webUser
	 * @return true|false
	 * @throws Exception
	 */
	private boolean isAllowedDepartment(DepartmentVO needCheckDepartment, WebUser webUser) throws Exception {
		if (isLimitByUser()) {
			Collection<DepartmentVO> userDepartmentList = webUser.getDepartments();
			String lowerDepartmentListStr = webUser.getLowerDepartmentList();

			for (Iterator<DepartmentVO> iterator = userDepartmentList.iterator(); iterator.hasNext();) {
				DepartmentVO userDepartment = (DepartmentVO) iterator.next();
				if (userDepartment.getLevel() < needCheckDepartment.getLevel()) {
					if (lowerDepartmentListStr.indexOf(needCheckDepartment.getId()) != -1) {
						return true;
					}
				} else if (userDepartment.getLevel() == needCheckDepartment.getLevel()) {
					if (userDepartment.getId().equals(needCheckDepartment.getId())) {
						return true;
					}
				} else if (userDepartment.getLevel() > needCheckDepartment.getLevel()) {
					DepartmentVO currentDepartment = userDepartment;
					while (currentDepartment.getSuperior() != null) {
//						if (userDepartment.getSuperior().getId().equals(needCheckDepartment.getId())) {
//							return true;
//						}
						
						//不索取上级没选定的部门
						if (userDepartment.getId().equals(needCheckDepartment.getId())) {
							return true;
						}
						currentDepartment = currentDepartment.getSuperior();
					}
				}
			}

			return false;
		} else {
			return true;
		}
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
		template.append(" departmentlevel='" + getDepartmentLevel() + "'");
		template.append(" fileFiled='" + getRelatedField() + "'");
		template.append("/>");
		return template.toString();
	}

	public String toPrintHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception {
		StringBuffer html = new StringBuffer();
		int displayType = getPrintDisplayType(doc, runner, webUser);
		
		if (displayType == PermissionType.HIDDEN) {
			return this.getPrintHiddenValue();
		}

		// Iterator iter = getDepartmentOptions(doc,
		// webUser).getOptions().iterator();
		// while (iter.hasNext()) {
		// Option element = (Option) iter.next();
		// if (element.getValue() != null) {
		// html.append("<SPAN style=\"FONT-SIZE: 9pt\">");
		// html.append(element.getOption());
		// html.append("</SPAN>");
		// }
		// }

		if (!getTextType().equalsIgnoreCase("hidden")) {
			Item item = doc.findItem(this.getName());
			if (item != null && item.getValue() != null) {
				Object value = item.getValue();
				html.append("<SPAN>");
				// if (value instanceof Number) {
				// DecimalFormat format = new DecimalFormat(getNumberPattern());
				// html.append(format.format((Number) item.getValue()));
				// } else {
				DepartmentProcess process = (DepartmentProcess) ProcessFactory.createProcess(DepartmentProcess.class);
				DepartmentVO vo = (DepartmentVO) process.doView(value.toString());
				html.append(vo.getName());
				// }
				html.append("</SPAN>");
			}
		}
		return html.toString();

	}

	public boolean isRender(String destVal, String origVal) {
		if (departmentLevel == 0 || StringUtil.isBlank(relatedField)) {
			return super.isRender(destVal, origVal);
		}
		return true;
	}

	private String toOptionXML(Document doc, WebUser webUser) {
		StringBuffer html = new StringBuffer();
		try {
			Collection<Option> deptOptions = getDepartmentOptions(doc, webUser).getOptions();
			Object value = null;
			if (doc != null) {
				Item item = doc.findItem(this.getName());
				if (item != null)
					value = item.getValue();
			}
			int count = 0;
			// 是否允许空选项
			if (allowEmpty) {
				count = 1;
				html.append("<").append(MobileConstant.TAG_OPTION).append("");
				html.append(" ").append(MobileConstant.ATT_VALUE).append("=''>");
				html.append("{*[Select]*}");
				html.append("</").append(MobileConstant.TAG_OPTION).append(">");
			}
			
			//默认用户所属部门
			String defaultDepartment = "";
			if(this.getDefaultOptionType() == DEFAULT_TYPE_DEPARTMENT_OF_USER){
				Collection<DepartmentVO> departments = webUser.getDepartments();
				if(departments != null && !departments.isEmpty()){
					DepartmentVO dept = (DepartmentVO) departments.iterator().next();
					defaultDepartment = dept.getId();
				}
			}
			
			Iterator<Option> iter = deptOptions.iterator();
			boolean flag = true;
			while (iter.hasNext()) {
				Option element = (Option) iter.next();
				if (element.getValue() != null) {
					html.append("<").append(MobileConstant.TAG_OPTION).append("");
					if (flag) {
						if (value != null && !value.equals("") && element.getValue() != null) {
							if (value.equals(element.getValue())) {
								html.append(" ").append(MobileConstant.ATT_SELECTED).append("='" + count + "'");
								flag = false;
							}
						}else if(this.getDefaultOptionType() == DEFAULT_TYPE_DEPARTMENT_OF_USER){
							if(defaultDepartment.equals(element.getValue())){
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
			Collection<Option> deptOptions = getDepartmentOptions(doc, webUser).getOptions();
			Object value = null;
			if (doc != null) {
				Item item = doc.findItem(this.getName());
				if (item != null)
					value = item.getValue();
			}
			
			// 是否允许空选项
			if (allowEmpty) {
				html.append("<OPTION");
				html.append(" ").append("VALUE").append("=''>");
				html.append("</OPTION>");
			}
			
			//默认用户所属部门
			String defaultDepartment = "";
			if(this.getDefaultOptionType() == DEFAULT_TYPE_DEPARTMENT_OF_USER){
				Collection<DepartmentVO> departments = webUser.getDepartments();
				if(departments != null && !departments.isEmpty()){
					DepartmentVO dept = (DepartmentVO) departments.iterator().next();
					defaultDepartment = dept.getId();
				}
			}
			
			Iterator<Option> iter = deptOptions.iterator();
			boolean flag = true;
			while (iter.hasNext()) {
				Option element = (Option) iter.next();
				if (element.getValue() != null) {
					html.append("<OPTION");
					if (flag) {
						if (value != null && !value.equals("") && element.getValue() != null) {
							if (value.equals(element.getValue())) {
								html.append(" ").append("SELECTED='true'");
								flag = false;
							}
						} else if(this.getDefaultOptionType() == DEFAULT_TYPE_DEPARTMENT_OF_USER){
							if(element.getValue().equals(defaultDepartment)){
								html.append(" ").append("SELECTED='true'");
								flag = false;
							}
						} else {
						
							if (element.isDef()) {
								html.append(" ").append("SELECTED='true'");
								flag = false;
							}
						}
					}
					html.append(" ").append("VALUE").append("='").append(HtmlEncoder.encode(element.getValue())).append("'");

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
		html.append("<").append(MobileConstant.TAG_OPTION).append(">");
		html.append("</").append(MobileConstant.TAG_OPTION).append(">");
		return html.toString();
	}
	
	@Override
	public String toMbXMLText2(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
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

	/**
	 * 获取是否过滤用户
	 * 
	 * @return 是否过滤用户
	 */
	public boolean isLimitByUser() {
		return limitByUser;
	}

	/**
	 * 设置是否过滤用户
	 * 
	 * @param isLimitByUser
	 *            是否过滤用户
	 */
	public void setLimitByUser(boolean isLimitByUser) {
		this.limitByUser = isLimitByUser;
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
	 * @return 重定义后的html文本
	 */

	public String toGridHtmlText(Document doc, IRunner runner, WebUser webUser, Map<String,Options> columnOptionsCache) throws Exception {
		StringBuffer html = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);

		if (displayType == PermissionType.HIDDEN) {
			return this.getHiddenValue();
		} else {
			if (doc != null) {
				html.append("<select moduleType='department'");
				html.append(" style='display:");
				html.append(getTextType().equals("hidden") ? "none" : "inline");
				html.append(";width:100%'");
				html.append(" _subGridView='true'");
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
		Options options = getDepartmentOptions(doc, webUser);
		for (Iterator<Option> iterator = options.getOptions().iterator(); iterator.hasNext();) {
			Option option = (Option) iterator.next();
			if (option.getValue().equals(doc.getItemValueAsString(getName()))) {
				return option.getOption();
			}
		}
		return "";
	}

	/**
	 * 获取默认下拉选项的类型
	 * 
	 * @return 下拉选项的类型
	 */
	public int getDefaultOptionType() {
		return defaultOptionType;
	}

	/**
	 * 设置下拉选项的类型
	 * 
	 * @param defaultOptionType
	 *            下拉选项的类型
	 */
	public void setDefaultOptionType(int defaultOptionType) {
		this.defaultOptionType = defaultOptionType;
	}

	/**
	 * 是否允许空值
	 * 
	 * @return
	 */
	public boolean isAllowEmpty() {
		return allowEmpty;
	}

	/**
	 * 设置是否允许空值
	 * 
	 * @param allowEmpty
	 *            是否允许空值
	 */
	public void setAllowEmpty(boolean allowEmpty) {
		this.allowEmpty = allowEmpty;
	}
	
	public String toPdfHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception {
		StringBuffer html = new StringBuffer();

		if (!getTextType().equalsIgnoreCase("hidden")) {
			Item item = doc.findItem(this.getName());
			if (item != null && item.getValue() != null) {
				Object value = item.getValue();
				html.append("<SPAN >");
				DepartmentProcess process = (DepartmentProcess) ProcessFactory.createProcess(DepartmentProcess.class);
				DepartmentVO vo = (DepartmentVO) process.doView(value.toString());
				html.append(vo.getName());
				html.append("</SPAN>");
			}
		}
		return html.toString();
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
			DepartmentProcess process = (DepartmentProcess) ProcessFactory.createProcess(DepartmentProcess.class);

			String valueListStr = getFieldValue(doc);
			if (!StringUtil.isBlank(valueListStr)) {
				String[] values = valueListStr.split(";");
				if (values.length > 0) {
					for (int i = 0; i < values.length; i++) {
						DepartmentVO dept = (DepartmentVO) process.doView(values[i]);
						if (dept != null) {
							rtn.append(dept.getName()).append(";");
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
	
	/**
	 * 获取表单域真实值
	 * 
	 * @param doc
	 * @return
	 */
	protected String getFieldValue(Document doc) {
		String rtn = "";
		if (doc != null) {
			Item item = doc.findItem(getName());
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

}
