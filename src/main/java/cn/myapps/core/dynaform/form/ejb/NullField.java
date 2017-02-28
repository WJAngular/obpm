package cn.myapps.core.dynaform.form.ejb;

import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.user.action.WebUser;

/**
 * 空组件
 * 
 * 
 */
public class NullField extends FormField {
	
	private static final long serialVersionUID = -8808850990782938908L;

	public String toMbXMLText(Document doc, IRunner runner, WebUser webUser) throws Exception {
		return null;
	}

	public String toTemplate() {
		return null;
	}

	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser, int permissionType) throws Exception {
		return null;
	}

	public String toPrintHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception {
		return null;
	}

	@Override
	public String toMbXMLText2(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
