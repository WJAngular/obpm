package cn.myapps.mobile2.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.html.ViewHtmlBean;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.privilege.res.ejb.ResVO;
import cn.myapps.core.table.constants.MobileConstant2;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.StringUtil;

public class MbViewXMLBuilder {
	
	public static String toMobileXML(View view,WebUser user,ParamsTable params){
		StringBuffer sb = new StringBuffer();
		try{
			if(view != null){
				HttpServletRequest request = ServletActionContext.getRequest();
				Document searchDocument  = null;
				if(view.getSearchForm() != null){
					searchDocument = view.getSearchForm().createDocument(params, user);
				}
				
				Document parent = (Document) request.getAttribute("parent");
				Document tdoc = parent != null ? parent : new Document();
				
				boolean isEdit = true;
				if (parent != null || !StringUtil.isBlank(params.getParameterAsString("parentid"))) {
					isEdit = !StringUtil.isBlank(request.getParameter("isedit")) ? Boolean
							.parseBoolean(request.getParameter("isedit"))
							: true;
				}
				
				IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(),
						view.getApplicationid());
				runner.initBSFManager(tdoc, params, user,
						new java.util.ArrayList<ValidateMessage>());
				
				ViewHtmlBean htmlBean = new ViewHtmlBean();
				htmlBean.setHttpRequest(request);
			    htmlBean.setWebUser(user);
				
				int rootCount = 0;
				DataPackage<Document> datas = MbViewHelper.doData(view, searchDocument, params, user);
				if(datas != null){
					rootCount = datas.rowCount;
				}
				
		 		/*************  ACTION START  *************/
				sb.append("<").append(MobileConstant2.TAG_VIEWDATA);
				
				if(StringUtil.isBlank(view.getDescription())){
					sb.append(" ").append(MobileConstant2.ATT_TITLE).append("='").append(view.getName()).append("'");
				}else{
					sb.append(" ").append(MobileConstant2.ATT_TITLE).append("='").append(view.getDescription()).append("'");
				}
				
				sb.append(" ").append(MobileConstant2.ATT_VIEWID).append("='").append(view.getId()).append("'");
				sb.append(" ").append(MobileConstant2.ATT_APPLICATION).append("='").append(view.getApplicationid()).append("'");
				if(rootCount > 99){
					sb.append(" ").append(MobileConstant2.ATT_ROOTCOUNT).append("='").append("99+").append("'");
				}else{
					sb.append(" ").append(MobileConstant2.ATT_ROOTCOUNT).append("='").append(rootCount).append("'");
				}
				sb.append(" ").append(MobileConstant2.ATT_READONLY).append("='").append(view.getReadonly()).append("'");
				sb.append(">");
				
				/*************  FORMDATA START  *************/
				if (view.getSearchForm() != null) {
					sb.append("<").append(MobileConstant2.TAG_ACTION);
					sb.append(" ").append(MobileConstant2.ATT_TYPE).append("='23'");
					sb.append(" ").append(MobileConstant2.ATT_NAME).append("='{*[Query]*}'");
					sb.append(" ").append(MobileConstant2.ATT_ACTIONID).append("=''>");
					sb.append("</").append(MobileConstant2.TAG_ACTION).append(">");
				}
				
				String actXML = htmlBean.toActXml(PermissionType.MODIFY);
				sb.append(actXML);
				/*************  ACTION END  *************/
				
				/*************  COLUMNS START  *************/
			    String xml = htmlBean.toXMLText(datas);
			    sb.append(xml);
			    
		        /*************  COLUMNS END  *************/
		        sb.append("</").append(MobileConstant2.TAG_VIEWDATA).append(">");
		        /*************  VIEWDATA END  *************/
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return sb.toString();
        
	}
	
}
