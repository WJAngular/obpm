package cn.myapps.core.dynaform.form.ejb;

import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.core.macro.runner.AbstractRunner;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.StringUtil;

/**
 * 代码编辑组件
 * 
 * @author Marky
 * 
 */
public class CalctextField extends FormField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2523261172025936144L;

	/**
	 * 空实现
	 */
	public ValidateMessage validate(IRunner bsf, Document doc) throws Exception {
		return null;
	}

	/**
	 * 返回模板描述计算文本
	 * 
	 * @return java.lang.String
	 * @roseuid 41E7917A033F
	 */
	public String toTemplate() {
		StringBuffer template = new StringBuffer();
		template.append("<span'");
		template.append(" className='" + this.getClass().getName() + "'");
		template.append(" id='" + getId() + "'");
		template.append(" name='" + getName() + "'");
		template.append(" formid='" + getFormid() + "'");
		template.append(" valueScript='" + getValueScript() + "'");
		template.append("/>");
		return template.toString();
	}
	
	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception {
		return toHtmlTxt(doc,runner,webUser,PermissionType.MODIFY);
	}

	/**
	 * 
	 * Form模版的代码编辑组件内容结合Document中的ITEM存放的值, 返回字符串值为ITEM的值.
	 * 若代码编辑组件的valueScript为空，返回一个空字符串。
	 * 
	 * @param doc
	 *            文档对象(Document)
	 * @see cn.myapps.core.macro.runner.AbstractRunner#run(String, String)
	 * @return 字符串内容为重定义后的html
	 */
	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser, int permissionType) throws Exception {
		
		StringBuffer html = new StringBuffer();
		String valueScript = this.getValueScript();
		
		if (valueScript != null && valueScript.trim().length() > 0) {
			try {
				if (valueScript != null && valueScript.trim().length() > 0) {
					Object result = runner.run(getScriptLable("toHtmlText"), valueScript);
					if (result !=null) {
						result = String.valueOf(result);
						html.append("<span class=\"calctext-field\">")
							.append(result)
							.append("</span>");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return html.toString();
	}

	@Override
	public String toPdfHtmlTxt(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		
		return toHtmlTxt(doc, runner, webUser);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * 
	 * Form模版的代码编辑组件内容结合Document中的ITEM存放的值,返回字符串值为打印的ITEM的值.
	 * 
	 * @param doc
	 *            文档对象(Document)
	 * @param runner
	 *            动态执行脚本器
	 * @param params
	 *            参数
	 * @param user
	 *            webuser
	 * 
	 * @see cn.myapps.core.dynaform.form.ejb.CalctextField#toHtmlTxt(Document,
	 *      AbstractRunner, WebUser, int)
	 * @see cn.myapps.core.macro.runner.AbstractRunner#run(String, String)
	 * @return 字符串值为打印的ITEM的值.
	 * @throws Exception
	 */
	public String toPrintHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception {
		return toHtmlTxt(doc, runner, webUser);
	}

	/**
	 * 空实现
	 */
	public String toMbXMLText(Document doc, IRunner runner, WebUser webUser) throws Exception {
		return "";
	}

	public String getRefreshScript(IRunner runner, Document doc, WebUser webUser, boolean isHidden) throws Exception {
		StringBuffer buffer = new StringBuffer();
		String divid = this.getId() + "_divid";
		String fieldHTML = "";
		if (!isHidden) {
			fieldHTML = StringUtil.encodeHTML(this.toHtmlTxt(doc, runner, webUser)); // 对HTML进行编码
			fieldHTML = fieldHTML.replaceAll("\"", "\\\\\"");
			fieldHTML = fieldHTML.replaceAll("\r\n", "");
		}
		buffer.append("refreshField(\"").append(divid).append("\",\"");
		String isDecode = "true"; // 是否反编码
		buffer.append(this.getName()).append("\",\"").append(fieldHTML).append("\", " + isDecode + ");");

		return buffer.toString();
	}

	@Override
	public String toMbXMLText2(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		if (valueScript != null && valueScript.trim().length() > 0) {
			try {
				if (valueScript != null && valueScript.trim().length() > 0) {
					Object result = runner.run(getScriptLable("toHtmlText"), valueScript);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}
}
