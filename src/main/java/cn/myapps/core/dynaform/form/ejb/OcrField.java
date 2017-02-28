//Source file: C:\\Java\\workspace\\SmartWeb3\\src\\com\\cyberway\\dynaform\\form\\ejb\\OcrField.java

package cn.myapps.core.dynaform.form.ejb;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.user.action.WebUser;

public class OcrField extends FormField 
{
   
   /**
	 * 
	 */
	private static final long serialVersionUID = 7346290720046520313L;

/**
   @roseuid 41ECB66C0362
    */
   public OcrField() throws Exception
   {
    throw new OBPMValidateException("This type field does not support yeat!");
   }
   
   /**
   @return boolean
   @roseuid 41ECB66C0380
    */
   public ValidateMessage validate(IRunner runner, Document doc) throws Exception 
   {
    return null;
   }
   
   /**
   @roseuid 41ECB66C038A
    */
   public void store() 
   {
    
   }
   
   /**
   @param tmpltStr
   @return FormField
   @roseuid 41ECB66D001A
    */
   public FormField init(String tmpltStr) 
   {
    return null;
   }
	public String toTemplate(){
		return null;
	}

	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser, int permissionType) throws Exception {
		return null;
	}

	public String toPrintHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception {
		return null;
	}

	public String toMbXMLText(Document doc, IRunner runner, WebUser webUser) throws Exception {
		return null;
	}

	@Override
	public String toMbXMLText2(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


}
