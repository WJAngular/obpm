package cn.myapps.core.dynaform.form.ejb;

import java.util.Map;

import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.user.action.WebUser;

/**
 * 微信GPS定位控件
 * @author Happy
 *
 */
public class WeixinGpsField extends FormField implements ValueStoreField{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3234574103742805731L;

	@Override
	public String toTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser, int permissionType)
			throws Exception {
		
		int displayType = getDisplayType(doc, runner, webUser, permissionType);
		
		StringBuffer html = new StringBuffer();
		html.append("<input type=\"hidden\" moduleType=\"").append(this.getType())
		.append("\" displayType=\"").append(displayType)
		.append("\" isRefreshOnChanged=\"").append(isRefreshOnChanged())
		.append("\" id=\"").append(this.getId())
		.append("\" name=\"").append(this.getName())
		.append("\" value='").append(doc.getItemValueAsString(name))
		.append("' />");
		if(displayType == PermissionType.HIDDEN){
			html.append(getHiddenValue());
		}
		
		return html.toString();
		
		
	}
	

	public String toPrintHtmlTxt(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toMbXMLText(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toMbXMLText2(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public String toGridHtmlText(Document doc, IRunner runner, WebUser webUser,
			Map<String, Options> columnOptionsCache) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
