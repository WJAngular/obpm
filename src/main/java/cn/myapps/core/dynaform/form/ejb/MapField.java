package cn.myapps.core.dynaform.form.ejb;

import java.text.DecimalFormat;
import java.util.Map;

import net.sf.json.JSONObject;
import cn.myapps.constans.Environment;
import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.table.constants.MobileConstant;
import cn.myapps.core.table.constants.MobileConstant2;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.HtmlEncoder;
import cn.myapps.util.StringUtil;

public class MapField extends FormField implements ValueStoreField {
	
	private static final long serialVersionUID = 3900336554465032659L;
	protected String openType;//打开地图类型
	
	public String getOpenType() {
		return openType;
	}

	public void setOpenType(String openType) {
		this.openType = openType;
	}

	@Override
	public String toMbXMLText(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		StringBuffer xmlText = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);

		if (doc != null) {
			xmlText.append("<").append(MobileConstant.TAG_MAPFILED);
			xmlText.append(" ").append(MobileConstant.ATT_ID + "='" + getId() + "'");
			xmlText.append(" ").append(MobileConstant.ATT_NAME + "='" + getName() + "'");
			xmlText.append(" ").append(MobileConstant.ATT_LABEL).append("='").append(getMbLabel()).append("'");

			if (displayType == PermissionType.READONLY || (getTextType() != null && getTextType().equalsIgnoreCase("readonly"))
					|| displayType == PermissionType.DISABLED) {
				xmlText.append(" ").append(MobileConstant.ATT_READONLY + "='true' ");
			}
			if (displayType == PermissionType.HIDDEN) {
				xmlText.append(" ").append(MobileConstant.ATT_HIDDEN).append(" ='true' ");
				if(!getHiddenValue().equals("") && !getHiddenValue().equals(null) && !getHiddenValue().equals("&nbsp;")){
					xmlText.append(" ").append(MobileConstant.ATT_HIDDENVALUE).append("='").append(getHiddenValue()+"' ");
				}
			}
			if (isRefreshOnChanged()) {
				xmlText.append(" ").append(MobileConstant.ATT_REFRESH).append(" ='true' ");
			}
			xmlText.append(">");
			if (doc != null) {
				Item item = doc.findItem(this.getName());
				if (item != null && item.getValue() != null) {
					String value = item.getValue().toString();
					xmlText.append(value);
				}
			}
			xmlText.append("</").append(MobileConstant.TAG_MAPFILED + ">");

		}
		return xmlText.toString();
	}

	/**
	 *模板
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
		template.append(">");
		return template.toString();
	}

	/**
	 * 网格格式输出HTML
	 */
	public String toGridHtmlText(Document doc, IRunner runner, WebUser webUser, Map<String,Options> columnOptionsCache)
			throws Exception {
		return toHtmlTxt(doc,runner,webUser);
	}
	
	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception {
		return toHtmlTxt(doc,runner,webUser,PermissionType.MODIFY);
	}

	/**
	 *普通模式输出HTML
	 */
	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser, int permissionType)
			throws Exception {
		if(WebUser.TYPE_BG_USER.equals(webUser.getType()))
			return toPreviewHtml(doc, runner, webUser);
		else
			return toHtml(doc, runner, webUser);
	}
	
	public String toPreviewHtml(Document doc, IRunner runner, WebUser webUser) throws Exception {
		StringBuffer html = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);
			html.append("<input moduleType='mapField' type='hidden'");
			html.append(" id='" + getFieldId(doc) + "'");
			html.append(" name='" + getName() + "'");
			html.append(" fieldType='" + getTagName() + "'");
			html.append(" mapLabel= '{*[map]*}'");
			html.append(" application= '"+doc.getApplicationid()+"'");
			html.append(" displayType= '" + displayType + "'");
			//html.append(" discript= '" + getDiscript() + "'");
			html.append(" srcEnvironment= '" + Environment.getInstance().getContextPath() + "'");
			html.append(" openType= '" + openType + "' ");
			html.append("/>");
		return html.toString();
	}
	
	public String toHtml(Document doc, IRunner runner, WebUser webUser) throws Exception {
		StringBuffer html = new StringBuffer();
		Item item = null;
		int displayType = getDisplayType(doc, runner, webUser);

		if (displayType == PermissionType.HIDDEN) {
			return this.getHiddenValue();
		} else {
			if (doc != null) {
				item = doc.findItem(this.getName());
				boolean isnew = true;
				if (item != null && item.getValue() != null) {
					isnew = false;
				}
				
				html.append("<input moduleType='mapField' type='hidden'");
				html.append(" id='" + getFieldId(doc) + "'");
				html.append(" name='" + getName() + "'");
				html.append(" fieldType='" + getTagName() + "'");
				if (!isnew) {
					html.append(" value='" + item.getValue() + "'");
				}
				if(openType=="dialog"||openType.equals("dialog")){
					html.append("display='true'");
				}else{
					html.append("nodialog='true'");
				}
				html.append("mapLabel= '{*[map]*}'");
				html.append("application= '"+doc.getApplicationid()+"'");
				html.append("displayType= '" + displayType + "'");
				//html.append("discript= '" + getDiscript() + "'");
				html.append("srcEnvironment= '" + Environment.getInstance().getContextPath() + "'");
				html.append("openType= '" + openType + "' ");
				html.append("/>");
			}
		}
		return html.toString();
	}

	/**
	 * 打印输出HTML
	 */
	public String toPrintHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception {
		StringBuffer html = new StringBuffer();

		if (doc != null) {
			int displayType = getPrintDisplayType(doc, runner, webUser);

			if (displayType == PermissionType.HIDDEN) {
				return this.getPrintHiddenValue();
			}
		}
		return html.toString();
	}

	public String toPdfHtmlTxt(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		StringBuffer html = new StringBuffer();
		html.append("<SPAN >");
		html.append("<img border='0' width='"+16+"' height='"+16+"' src='" +webUser.getServerAddr() + doc.get_params().getContextPath() + "/core/dynaform/form/webeditor/editor/plugins/mapfield/img.png" + "' />");
		html.append("</SPAN>");
		return html.toString();
	}

	@Override
	public String toMbXMLText2(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		StringBuffer xmlText = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser);

		if (doc != null) {
			xmlText.append("<").append(MobileConstant2.TAG_MAPFIELD);
			xmlText.append(" ").append(MobileConstant2.ATT_ID + "='" + getId() + "'");
			xmlText.append(" ").append(MobileConstant2.ATT_NAME + "='" + getName() + "'");
			xmlText.append(" ").append(MobileConstant2.ATT_LABEL).append("='").append(getMbLabel()).append("'");

			if (displayType == PermissionType.READONLY || (getTextType() != null && getTextType().equalsIgnoreCase("readonly"))
					|| displayType == PermissionType.DISABLED) {
				xmlText.append(" ").append(MobileConstant2.ATT_READONLY + "='true' ");
			}
			if (displayType == PermissionType.HIDDEN) {
				xmlText.append(" ").append(MobileConstant2.ATT_HIDDEN).append(" ='true' ");
				if(!getHiddenValue().equals("") && !getHiddenValue().equals(null) && !getHiddenValue().equals("&nbsp;")){
					xmlText.append(" ").append(MobileConstant2.ATT_HIDDENVALUE).append("='").append(getHiddenValue()+"' ");
				}
			}
			if (isRefreshOnChanged()) {
				xmlText.append(" ").append(MobileConstant2.ATT_REFRESH).append(" ='true' ");
			}
			xmlText.append(">");
			if (doc != null) {
				Item item = doc.findItem(this.getName());
				if (item != null && item.getValue() != null) {
					String value = item.getValue().toString();
					xmlText.append(value);
				}
			}
			xmlText.append("</").append(MobileConstant2.TAG_MAPFIELD + ">");

		}
		return xmlText.toString();
	}
	
}
