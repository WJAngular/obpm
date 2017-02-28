package cn.myapps.core.dynaform.form.ejb;

import java.util.Map;

import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.user.action.WebUser;

/**
 * 微信录音控件
 * @author Happy
 *
 */
public class WeixinRecordField extends FormField implements ValueStoreField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7461186182830819978L;
	

	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser, int permissionType)
			throws Exception {
		StringBuffer html = new StringBuffer();

		int displayType = getDisplayType(doc, runner, webUser, permissionType);
		
		if(doc != null) {
			if(displayType==PermissionType.HIDDEN){
				return getHiddenValue();
			}else{
				html.append(toHtml(doc, runner, webUser, displayType));
			}
			
			
		}
		
		return html.toString();
	}
	
	public String toHtml(Document doc, IRunner runner, WebUser webUser,int displayType)throws Exception {
		StringBuffer html = new StringBuffer();
		
		Object value = null;
		Item item = doc.findItem(this.getName());
		if (item != null){
			value = item.getValue();
		}
		value = value != null ? value : "";
		
		html.append("<input moduleType='weixinrecordfield' type='hidden' id='").append(doc.getId()+"_"+getId())
		.append("' name='").append(getName()).append("' ")
		.append("value='").append(value).append("' ")
		.append("data-display-type='").append(displayType).append("' ")
		.append("data-discription='").append(getDiscript()).append("' />");
		
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

	@Override
	public String toTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

	public String toGridHtmlText(Document doc, IRunner runner, WebUser webUser,
			Map<String, Options> columnOptionsCache) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


}
