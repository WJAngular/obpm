package cn.myapps.core.dynaform.form.ejb;

import java.util.Map;

import cn.myapps.constans.Environment;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgProcess;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgVO;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;

public class ReminderField extends FormField implements ValueStoreField{
	
	private static final long serialVersionUID = 3011283097413156840L;
	
	protected String reminderid;//提醒编号
	
	public String getReminderid() {
		return reminderid;
	}

	public void setReminderid(String reminderid) {
		this.reminderid = reminderid;
	}

	@Override
	public String toMbXMLText(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		return null;
	}

	@Override
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
	 * 以网格的形式显示
	 */
	public String toGridHtmlText(Document doc, IRunner runner, WebUser webUser, Map<String,Options> columnOptionsCache)
			throws Exception {
		return toHtmlTxt(doc,runner,webUser);
	}
	
	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception {
		return toHtmlTxt(doc,runner,webUser,PermissionType.MODIFY);
	}
	/**
	 * 以一般网页的形式显示
	 */
	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser, int permissionType) throws Exception {
		StringBuffer htmlBuilder = new StringBuffer();
		int displayType = getDisplayType(doc, runner, webUser, permissionType);
		String skinType = "";

		//获取皮肤参数
		if(webUser.getUserSetup() != null){
			skinType = webUser.getUserSetup().getUserSkin();
		}else{
			DomainProcess domPro=(DomainProcess) ProcessFactory.createProcess(DomainProcess.class);
			DomainVO domainVO = (DomainVO) domPro.doView(webUser.getDomainid());
			if(domainVO != null){
				skinType = domainVO.getSkinType();
			}
		}
		
		if (displayType == PermissionType.HIDDEN) {
			return this.getHiddenValue();
		}else if(displayType == PermissionType.MODIFY|| displayType == PermissionType.DISABLED ){
		
			SummaryCfgProcess summaryCfgProcess = (SummaryCfgProcess) ProcessFactory.createProcess(SummaryCfgProcess.class);
			SummaryCfgVO summaryCfg = (SummaryCfgVO) summaryCfgProcess.doView(getReminderid());
			if(summaryCfg!=null && summaryCfg.getScope() == SummaryCfgVO.SCOPE_PENDING){
				String contextPath = Environment.getInstance().getContextPath();
				htmlBuilder.append("<span id='columns'><span class='column'><span class='ElementDiv'");
				htmlBuilder.append(" src='" + contextPath);
				htmlBuilder.append("/portal/dynaform/document/pendinglist.action?formid=");
				htmlBuilder.append(summaryCfg.getFormId());
				htmlBuilder.append("&application="+get_form().getApplicationid());
				htmlBuilder.append("&_pagelines=10&summaryCfgId=" + summaryCfg.getId()+ "&_orderby=" + summaryCfg.getOrderby()).append("'");
//				htmlBuilder.append(" frameborder='0'");
				htmlBuilder.append(" height='250px'");
				htmlBuilder.append(" width='" + 320 + "px'");
				htmlBuilder.append(" style='display:block;margin-left:5px;'");
				htmlBuilder.append(">");
				htmlBuilder.append("</span></span></span>");
			}else if(summaryCfg!=null && summaryCfg.getScope() == SummaryCfgVO.SCOPE_CIRCULATOR){
				String contextPath = Environment.getInstance().getContextPath();
				htmlBuilder.append("<span id='columns'><span class='column'><span class='ElementDiv'");
				htmlBuilder.append(" src='" + contextPath);
				htmlBuilder.append("/portal/workflow/storage/runtime/circulatorlist.action?formid=");
				htmlBuilder.append(summaryCfg.getFormId());
				htmlBuilder.append("&application="+get_form().getApplicationid());
				htmlBuilder.append("&_pagelines=10&summaryCfgId=" + summaryCfg.getId()+ "&_orderby=" + summaryCfg.getOrderby()).append("'");
//				htmlBuilder.append(" frameborder='0'");
				htmlBuilder.append(" height='250px'");
				htmlBuilder.append(" width='" + 320 + "px'");
				htmlBuilder.append(" style='display:block;margin-left:5px;'");
				htmlBuilder.append(">");
				htmlBuilder.append("</span></span></span>");
			}
		}
		return htmlBuilder.toString();
	}

	/**
	 * 打印
	 */
	public String toPrintHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception {
		StringBuffer htmlBuilder = new StringBuffer();
		String contextPath = Environment.getInstance().getContextPath();
		int displayType = getPrintDisplayType(doc, runner, webUser);
		if (displayType == PermissionType.HIDDEN) {
			return this.getPrintHiddenValue();
		}else if(displayType == PermissionType.MODIFY){
		
			htmlBuilder.append("<table border='0'>");
			htmlBuilder.append("<tr>");
			htmlBuilder.append("<td>");
			SummaryCfgProcess summaryCfgProcess = (SummaryCfgProcess) ProcessFactory.createProcess(SummaryCfgProcess.class);
			SummaryCfgVO summaryCfg = (SummaryCfgVO) summaryCfgProcess.doView(getReminderid());
			if(summaryCfg!=null&& summaryCfg.getScope() == SummaryCfgVO.SCOPE_PENDING){
			
			htmlBuilder.append("<div class='ElementDiv'");
			htmlBuilder.append(" src='" + contextPath);
			htmlBuilder.append("/portal/dynaform/document/pendinglist.action?formid=");
			htmlBuilder.append(summaryCfg.getFormId());
			htmlBuilder.append("&application="+get_form().getApplicationid());
			htmlBuilder.append("&_pagelines=10&summaryCfgId=" + summaryCfg.getId()+ "&_orderby=" + summaryCfg.getOrderby()).append("'");
			htmlBuilder.append(" height='250px'");
			htmlBuilder.append(" width='" + 320 + "px'");
			htmlBuilder.append(" style='margin-left:5px;'");
			htmlBuilder.append(">");
			htmlBuilder.append("</div>");
			}else if(summaryCfg!=null && summaryCfg.getScope() == SummaryCfgVO.SCOPE_CIRCULATOR){
				htmlBuilder.append("<div class='ElementDiv'");
				htmlBuilder.append(" src='" + contextPath);
				htmlBuilder.append("/portal/workflow/storage/runtime/circulatorlist.action?formid=");
				htmlBuilder.append(summaryCfg.getFormId());
				htmlBuilder.append("&application="+get_form().getApplicationid());
				htmlBuilder.append("&_pagelines=10&summaryCfgId=" + summaryCfg.getId()+ "&_orderby=" + summaryCfg.getOrderby()).append("'");
				htmlBuilder.append(" height='320px'");
				htmlBuilder.append(" width='" + 320 + "px'");
				htmlBuilder.append(" style='margin-left:5px;'");
				htmlBuilder.append(">");
				htmlBuilder.append("</div>");
				
			}
			htmlBuilder.append("</td>");
			htmlBuilder.append("</tr><tr>");
			htmlBuilder.append("</table>");
		}
		return htmlBuilder.toString();
	}
	
	

	public String toPdfHtmlTxt(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		StringBuffer htmlBuilder = new StringBuffer();
		String contextPath = Environment.getInstance().getContextPath();
		
		htmlBuilder.append("<table border='0'>");
		htmlBuilder.append("<tr>");
		htmlBuilder.append("<td>");
		SummaryCfgProcess summaryCfgProcess = (SummaryCfgProcess) ProcessFactory.createProcess(SummaryCfgProcess.class);
		SummaryCfgVO summaryCfg = (SummaryCfgVO) summaryCfgProcess.doView(getReminderid());
		if(summaryCfg!=null&& summaryCfg.getScope() == SummaryCfgVO.SCOPE_PENDING){
		
		htmlBuilder.append("<div class='ElementDiv'");
		htmlBuilder.append(" src='" + contextPath);
		htmlBuilder.append("/portal/dynaform/document/pendinglist.action?formid=");
		htmlBuilder.append(summaryCfg.getFormId());
		htmlBuilder.append("&application="+get_form().getApplicationid());
		htmlBuilder.append("&_pagelines=10&summaryCfgId=" + summaryCfg.getId()+ "&_orderby=" + summaryCfg.getOrderby()).append("'");
		htmlBuilder.append(" height='250px'");
		htmlBuilder.append(" width='" + 320 + "px'");
		htmlBuilder.append(" style='margin-left:5px;'");
		htmlBuilder.append(">");
		htmlBuilder.append("</div>");
		}else if(summaryCfg!=null && summaryCfg.getScope() == SummaryCfgVO.SCOPE_CIRCULATOR){
			htmlBuilder.append("<div class='ElementDiv'");
			htmlBuilder.append(" src='" + contextPath);
			htmlBuilder.append("/portal/workflow/storage/runtime/circulatorlist.action?formid=");
			htmlBuilder.append(summaryCfg.getFormId());
			htmlBuilder.append("&application="+get_form().getApplicationid());
			htmlBuilder.append("&_pagelines=10&summaryCfgId=" + summaryCfg.getId()+ "&_orderby=" + summaryCfg.getOrderby()).append("'");
			htmlBuilder.append(" height='320px'");
			htmlBuilder.append(" width='" + 320 + "px'");
			htmlBuilder.append(" style='margin-left:5px;'");
			htmlBuilder.append(">");
			htmlBuilder.append("</div>");
			
		}
		htmlBuilder.append("</td>");
		htmlBuilder.append("</tr><tr>");
		htmlBuilder.append("</table>");
		return htmlBuilder.toString();
	}

	@Override
	public String toMbXMLText2(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
