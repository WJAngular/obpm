package cn.myapps.core.dynaform.form.ejb;

import java.util.Map;

import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.HtmlEncoder;
import cn.myapps.util.ProcessFactory;

/**
 * InformationFeedbackField(信息反馈控件)
 * @author jodg
 */
public class InformationFeedbackField  extends FormField  implements ValueStoreField {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8489940245597248994L;
	
	private String areaWidth;
	
	private String areaHeight;
	
	public String getAreaWidth() {
		if(this.areaWidth==null || "".equals(this.areaWidth)){
			this.areaWidth = "350px";
		}
		if(!this.areaWidth.contains("%") && !this.areaWidth.contains("px")){
			return this.areaWidth + "px";
		}
		return this.areaWidth;
	}

	public void setAreaWidth(String areaWidth) {
		this.areaWidth = areaWidth;
	}

	public String getAreaHeight() {
		if(this.areaHeight==null || "".equals(this.areaHeight)){
			this.areaHeight = "200px";
		}
		if(!this.areaHeight.contains("px")){
			return this.areaHeight + "px";
		}
		return this.areaHeight;
	}

	public void setAreaHeight(String areaHeight) {
		this.areaHeight = areaHeight;
	}
	
	public String getFieldtype() {
		return Item.VALUE_TYPE_TEXT;
	}
	
	public InformationFeedbackField() {
		
	}
	
	@Override
	public boolean isCalculateOnRefresh() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser, int permissionType)
			throws Exception {
		// TODO Auto-generated method stub
		StringBuffer html = new StringBuffer();
		
		if (doc != null) {
			int displayType = getDisplayType(doc, runner, webUser, permissionType);
			
			//即使单据审批完成，信息反馈控件也能修改
			if (displayType != PermissionType.HIDDEN) {
				displayType = PermissionType.MODIFY;
			}
			
			Item item = doc.findItem(this.getName());

			String text = item.getTextvalue();
			String tmp = null;
			html.append("<textarea moduleType='informationfeedbackField' width='" +getAreaWidth()+ "' height='" +getAreaHeight()+ "' style='display:none;width: " + getAreaWidth() + "; height: " + getAreaHeight() + "' ");
			html.append(" displayType='" + displayType + "'");
			html.append(" id='" + getId() + "'");
			html.append(" name='" + getName() + "' ");
			html.append(" _discript ='" + getDiscript() + "'");
			html.append(">");
			if (text != null) {
				tmp = HtmlEncoder.encode(text);
				html.append(tmp);
			}
			html.append("</textarea>");
			

			if(displayType == PermissionType.HIDDEN){
				html.append(getHiddenValue());
			}
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

	@Override
	public void recalculate(IRunner runner, Document doc, WebUser webUser)
			throws Exception {
		// TODO Auto-generated method stub
		if (doc!=null && !doc.getIstmp()) {
			DocumentProcess  dp = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, doc.getApplicationid());
			Document oldDoc = (Document)dp.doView(doc.getId());
			if (oldDoc!=null) {
				doc.addTextItem(this.getName(), oldDoc.getItemValueAsString(this.getName()));
			}
		}
	}
	
}
