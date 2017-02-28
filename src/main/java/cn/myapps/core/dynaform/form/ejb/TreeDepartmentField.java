package cn.myapps.core.dynaform.form.ejb;

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

public class TreeDepartmentField extends FormField implements ValueStoreField{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7936079412655964741L;

	protected static String cssClass = "department-cmd";
	private String textFieldId="";//真实值
	private String valueFieldId="";//id

	/**
	 * 限制选择部门的数量
	 */
	protected String limit;
	/**
	 * 获得限制选择部门的数量
	 * @return
	 */
	public String getLimit() {
		return limit;
	}
	/**
	 * 设置限制选择部门的数量
	 * @param limit
	 */
	public void setLimit(String limit) {
		this.limit = limit;
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
			xmlText.append("<").append(MobileConstant.TAG_TREEDEPARTMENTFIELD);
			xmlText.append(" ").append(MobileConstant.ATT_LIMIT + "='" + (!StringUtil.isBlank(limit) ? limit : Integer.MAX_VALUE) + "'");
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
				if(!getHiddenValue().equals("") && !getHiddenValue().equals(null)){
					xmlText.append(" ").append(MobileConstant.ATT_HIDDENVALUE).append("='").append(getHiddenValue()+"' ");
				}
			}
			if (isRefreshOnChanged()) {
				xmlText.append(" ").append(MobileConstant.ATT_REFRESH).append(" ='true' ");
			}
			xmlText.append(">");
			try {
				xmlText.append(toOptionXML(doc, webUser));
				xmlText.append("</").append(MobileConstant.TAG_TREEDEPARTMENTFIELD + ">");
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

			Iterator<Option> iter = deptOptions.iterator();
			while (iter.hasNext()) {
				Option element = (Option) iter.next();
				if (element.getValue() != null) {
					html.append("<").append(MobileConstant2.TAG_OPTION).append("");
					if (value != null && element.getValue() != null) {
						if (value.toString().indexOf(element.getValue())!=-1) {
							html.append(" ").append("SELECTED").append("='TRUE'");
						}
					} else {
						if (element.isDef()) {
							html.append(" ").append(MobileConstant2.ATT_SELECTED).append("='TRUE'");
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
			return getHiddenValue();
		} else if(doc != null) {
				html.append("<input moduleType='treeDepartment'");
				html.append(" _subGridView='true'");
				html.append(toAttr(doc, runner, webUser, displayType));	
		}
		return html.toString();
	}

	/**
	 * Form模版的动态组件内容结合Document中的ITEM存放的值,返回字符串为重定义后的生成html文本
	 * 
	 * @param doc
	 *            Document(文档对象)
	 * @param runner
	 *            动态脚本执行器
	 * @param webUser
	 *            webUser
	 * @return 重定义后的生成html文本
	 * @throws Exception
	 */
	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser, int permissionType) throws Exception {
		StringBuffer html = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser, permissionType);
		if(doc != null) {
				html.append("<input moduleType='treeDepartment' data-enhance='false' ");
				html.append(toAttr(doc, runner, webUser, displayType));
		}
		return html.toString();
	}
	
	private String toAttr(Document doc, IRunner runner, WebUser webUser, int displayType) throws Exception {
		StringBuffer html = new StringBuffer();
		html.append(" _textType='" + getTextType() + "'");
		html.append(" _displayType = '" + displayType + "'");
		html.append(" _fieldId='" + getFieldId(doc) + "'");
		html.append(" _fieldType='" + getTagName() + "'");
		html.append(" _cssClass='" + cssClass + "'");
		html.append(" _fieldText='" + getFieldText(doc) + "'");
		html.append(" _hiddenValue='" + getHiddenValue() + "'");
//		表单描述字段
		html.append(" _discript ='" + getDiscript() + "'");
		html.append(" _treeTip ='{*[please.select]*}'");
		html.append(toOtherpropsHtml());
		try {
			html.append(" _limit=" + (!StringUtil.isBlank(limit) ? limit : Integer.MAX_VALUE) + "");
			html.append(" _name='" + getName() + "'");
			html.append(" _fieldValue='" + getFieldValue(doc) + "'");
			html.append(" _isRefreshOnChanged='" + isRefreshOnChanged() + "'");
			html.append(" _title='{*[Readonly]*}{*[State]*}'");
			html.append(" />");
		} catch (Exception e) {
			e.printStackTrace();
		}
	
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
	
	/**
	 * 获取表单域显示值
	 * 
	 * @param doc
	 * @return
	 */
	protected String getFieldText(Document doc) {
		StringBuffer rtn = new StringBuffer();
		try {
			DepartmentProcess process = (DepartmentProcess) ProcessFactory.createProcess(DepartmentProcess.class);

			String valueListStr = getFieldValue(doc);
			if (!StringUtil.isBlank(valueListStr)) {
				String[] values = valueListStr.split(";");
				if (values.length > 0) {
					for (int i = 0; i < values.length; i++) {
						DepartmentVO department = (DepartmentVO) process.doView(values[i]);
						if (department != null) {
							rtn.append(department.getName()).append(";");
						}
					}
					rtn.deleteCharAt(rtn.lastIndexOf(";"));
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

		if (displayType == PermissionType.HIDDEN) {
			return this.getPrintHiddenValue();
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
	public String getText(Document doc, IRunner runner, WebUser webUser, Map<String,Options> columnOptionsCache)
	throws Exception {
	String fileFullName = getFieldText(doc);
	if (fileFullName!=null) {
			return fileFullName;
	}
	return super.getText(doc, runner, webUser);
	}
	
	/**
	 * 获取部门组件的下拉选项
	 * 
	 * @param doc
	 *            Document(文档对象)
	 * @param webUser
	 *            webUser
	 * @return 获取用户选项
	 * @throws Exception
	 */
	private Options getDepartmentOptions(Document doc, WebUser webUser) throws Exception {
		Options options = new Options();

		DepartmentProcess process = (DepartmentProcess) ProcessFactory.createProcess(DepartmentProcess.class);
		Collection<DepartmentVO> tempDeptList = null;
			tempDeptList = process.queryByDomain(webUser.getDomainid());

		for (Iterator<DepartmentVO> iterator = tempDeptList.iterator(); iterator.hasNext();) {
			DepartmentVO departmentVO = (DepartmentVO) iterator.next();
			options.add(departmentVO.getName(), departmentVO.getId(), isDefaultValue(doc, departmentVO.getId()));
		}

		return options;
	}
	
	public String toPdfHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception {
		StringBuffer html = new StringBuffer();
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
	@Override
	public String toMbXMLText2(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		
        StringBuffer xmlText2 = new StringBuffer();
        int displayType = getDisplayType(doc, runner, webUser);

        if (doc != null) {
            xmlText2.append("<").append(MobileConstant2.TAG_TREEDEPARTMENTFIELD);
            xmlText2.append(" ").append(MobileConstant2.ATT_LIMIT).append("='").append((!StringUtil.isBlank(limit) ? limit : Integer.MAX_VALUE)).append("'");
            xmlText2.append(" ").append(MobileConstant2.ATT_ID).append("='").append(getId()).append("'");
            xmlText2.append(" ").append(MobileConstant2.ATT_NAME).append("='").append(getName()).append("'");
            xmlText2.append(" ").append(MobileConstant2.ATT_LABEL).append("='").append(getMbLabel()).append("'");

            if (displayType == PermissionType.READONLY
                    || (getTextType() != null && getTextType().equalsIgnoreCase("readonly"))
                    || displayType == PermissionType.DISABLED) {
                xmlText2.append(" ").append(MobileConstant2.ATT_READONLY).append("='true'");
            }
            if (displayType == PermissionType.HIDDEN || (getTextType() != null && getTextType().equalsIgnoreCase("hidden"))) {
                xmlText2.append(" ").append(MobileConstant2.ATT_HIDDEN).append(" ='true'");
                if(!getHiddenValue().equals("") && !getHiddenValue().equals(null) && !getHiddenValue().equals("&nbsp;")){
                    xmlText2.append(" ").append(MobileConstant2.ATT_HIDDENVALUE).append("='").append(getHiddenValue()+"'");
                }
            }
            if (isRefreshOnChanged()) {
                xmlText2.append(" ").append(MobileConstant2.ATT_REFRESH).append(" ='true' ");
        }
            xmlText2.append(">");
            try {
                xmlText2.append(toOptionXML2(doc, webUser));
                xmlText2.append("</").append(MobileConstant2.TAG_TREEDEPARTMENTFIELD).append(">");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return xmlText2.toString();
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
}
