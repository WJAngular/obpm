<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="cn.myapps.constans.Web"%>
<%@ page import="cn.myapps.util.OBPMDispatcher"%>
<%@ page import="cn.myapps.base.action.ParamsTable"%>
<%@ page import="cn.myapps.core.user.action.WebUser"%>
<%@ page import="cn.myapps.core.user.ejb.UserDefined"%>
<%@ page import="cn.myapps.core.widget.ejb.PageWidget"%>
<%@ page import="cn.myapps.core.homepage.action.HomePageHelper"%>

<%@page	import="cn.myapps.core.deploy.application.action.ApplicationHelper"%>
<%@page import="cn.myapps.core.deploy.application.ejb.ApplicationVO"%>
<%@page import="cn.myapps.core.dynaform.pending.ejb.PendingVO"%>
<%@page import="cn.myapps.core.dynaform.pending.ejb.PendingProcessBean"%>
<%@page import="cn.myapps.base.dao.DataPackage"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cn.myapps.core.deploy.application.ejb.ApplicationProcess"%>
<%@page import="cn.myapps.util.ProcessFactory"%>

<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="myapps" prefix="o"%>

<o:MultiLanguage value="FRONTMULTILANGUAGETAG">

<%
	String closeUrlStr = new OBPMDispatcher().getDispatchURL("../../../portal/dispatch/closeTab.jsp",request,response);
	String applicationId = request.getParameter("application");

	WebUser user = (WebUser) request.getSession().getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
	HomePageHelper homePageHelper = new HomePageHelper();
	Collection<PageWidget> widgetList = homePageHelper.getMyWidgets(user);

	StringBuffer appL = new StringBuffer();
	StringBuffer appM = new StringBuffer();
	StringBuffer appR = new StringBuffer();
	StringBuffer iconLinkStr = new StringBuffer();
	String str;
	
	int count = 0;//将Widget 分别初始化到左、中、右位置中
	
	for (PageWidget widget : widgetList) { 
		if((!PageWidget.TYPE_SYSTEM_ANNOUNCEMENT.equals(widget.getType()) 
				&& !PageWidget.TYPE_SYSTEM_WORKFLOW.equals(widget.getType()) 
				&& !PageWidget.TYPE_SYSTEM_WEATHER.equals(widget.getType())) 
				&& !applicationId.equals(widget.getApplicationid())) continue;
		String widget_icon = widget.getIcon();
		String _typeStr = widget.getType();
		
		if(widget.getIconShow()){
			str = "'" + widget.getId() + "':{'name':'" + widget.getName()
				+ "','titleColor':'"+widget.getTitleColor()+"','icon':'"
				+ widget_icon +"','titleBColor':'"+widget.getTitleBColor()+"','iconShow':'"
				+ widget.getIconShow()+ "','type':'"
				+ _typeStr;
			if("view".equals(_typeStr) || "page".equals(_typeStr)){
				str = str + "','actionContent':'"+widget.getActionContent();
			}	
			str = str + "'},";
			
			iconLinkStr.append(str);
		}else{
			str = "'" + widget.getId() + "':{'name':'" + widget.getName()
				+ "','titleColor':'"+widget.getTitleColor()+"','icon':'"
				+ widget_icon +"','titleBColor':'"+widget.getTitleBColor()+"','iconShow':'"
				+ widget.getIconShow()+ "','type':'"
				+ _typeStr+"'},";

			switch (count % 3) {
			case 0:
				appL.append(str);
				break;
			case 1:
				appM.append(str);
				break;
			case 2:
				appR.append(str);
				break;
			}
			count ++;
		}
	}
	
	if (appL.length() > 0)
		appL.deleteCharAt(appL.length() - 1);
	if (appM.length() > 0)
		appM.deleteCharAt(appM.length() - 1);
	if (appR.length() > 0)
		appR.deleteCharAt(appR.length() - 1);
	if (iconLinkStr.length() > 0)
		iconLinkStr.deleteCharAt(iconLinkStr.length() - 1);

	String defaultCfg = "'appIcon':{"+iconLinkStr+"},'layoutStyle':'1:1:1','appL':{"
						+appL+"},'appM':{"+appM+"},'appR':{"+appR+"}";

%>

<input style="display: none" type='hidden' id='userId' name='userId'
	value='<%=user.getId()%>' />
<textarea id='templateElement' name='templateElement'>
<%
	UserDefined userDefined = homePageHelper.getUserDefined(user);
	if (userDefined != null && userDefined.getTemplateElement() != null) {
		out.print(userDefined.getTemplateElement());
	}
%>
</textarea>
<textarea id="defaultCfg">{<%=defaultCfg %>}</textarea>
</o:MultiLanguage>