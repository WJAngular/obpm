package cn.myapps.mobile.login;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.constans.Web;
import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.dynaform.pending.ejb.PendingProcess;
import cn.myapps.core.dynaform.pending.ejb.PendingProcessBean;
import cn.myapps.core.dynaform.pending.ejb.PendingVO;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgProcess;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgVO;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.core.table.constants.MobileConstant;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserDefined;
import cn.myapps.core.user.ejb.UserDefinedProcess;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.widget.ejb.PageWidget;
import cn.myapps.core.widget.ejb.PageWidgetProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.Circulator;
import cn.myapps.core.workflow.storage.runtime.ejb.CirculatorProcess;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.property.MultiLanguageProperty;

public class MbLoginHelper {

	public static WebUser initLogin(HttpServletRequest request, UserVO user) throws Exception {
		
		WebUser webUser = new WebUser(user);
		
		DomainProcess domainProcess = (DomainProcess) ProcessFactory.createProcess(DomainProcess.class);
		DomainVO domain  = (DomainVO) domainProcess.doView(webUser.getDomainid());;
		
		updateDefaultApplicationToUser(user, webUser, domain, user.getDefaultApplication());

		HttpSession session = request.getSession();

		String language = request.getParameter("language");
		if (StringUtil.isBlank(language)) {
			language = MultiLanguageProperty.getName(2);
		}
		session.setAttribute(Web.SESSION_ATTRIBUTE_USERLANGUAGE, language);
		// session.setMaxInactiveInterval(20 * 60); // 20 minutes
		session.setAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER, webUser);
		return webUser;
	}
	
	/**
	 * 为用户更新默认软件
	 * 
	 * @param user
	 * @param webUser
	 * @param domain
	 * @param application
	 * @throws Exception
	 */
	public static void updateDefaultApplicationToUser(UserVO user, WebUser webUser, DomainVO domain, String application)
			throws Exception {
		UserProcess process = (UserProcess) ProcessFactory.createProcess(UserProcess.class);

		// 更新默认应用
		String applicationid = getDefaultApplication(webUser, domain, application);
		process.doUpdateDefaultApplication(user.getId(), applicationid);
		user.setApplicationid(applicationid);
		webUser.setDefaultApplication(applicationid);
	}

	/**
	 * 获取默认软件
	 * 
	 * @param webUser
	 * @param domain
	 * @param application
	 * @return
	 * @throws Exception
	 */
	private static String getDefaultApplication(WebUser webUser, DomainVO domain, String application) throws Exception {
		String rtn = application;
		if (StringUtil.isBlank(application)) {
			ApplicationProcess appProcess = (ApplicationProcess) ProcessFactory.createProcess(ApplicationProcess.class);
			ApplicationVO appvo = appProcess.getDefaultApplication(webUser.getDefaultApplication(), webUser);
			if (domain.getApplications().contains(appvo)) {// 如果默认应用包含于域中
				rtn = appvo.getId();
			} else {
				if (!domain.getApplications().isEmpty()) {
					rtn = ((ApplicationVO) domain.getApplications().toArray()[0]).getId(); // 默认进入域中的第一个应用
				}
			}
		}
		return rtn;
	}
	
	/**
	 * 替换文本中html标签
	 * 
	 * @param htmlString
	 * @return noHTMLString
	 * @author kharry
	 * @throws 
	 */
	public static String replaceHTML(String htmlString){
        String noHTMLString = htmlString.replaceAll("</?[^>]+>", "");
        noHTMLString = noHTMLString.replaceAll("\\&nbsp;", " ");
        return noHTMLString;
    }
	
	/**
	 * 查询摘要
	 * 有摘要则返回此摘要对象
	 * 否则返回null
	 * @param summaryid
	 * @author jack
	 * @return summaryCfg
	 * @throws Exception
	 */
	public static SummaryCfgVO summaryIdCheck(String summaryid) throws ClassNotFoundException{
		SummaryCfgVO summaryCfg = null;
		try {
			SummaryCfgProcess summaryCfgPro=(SummaryCfgProcess) ProcessFactory.createProcess(SummaryCfgProcess.class);
			if(!StringUtil.isBlank(summaryid))
				summaryCfg = (SummaryCfgVO) summaryCfgPro.doView(summaryid);
			if(summaryCfg == null){//兼容旧数据
				PageWidgetProcess process = (PageWidgetProcess)ProcessFactory.createProcess(PageWidgetProcess.class);
				PageWidget widget = (PageWidget)process.doView(summaryid);
				if(widget !=null && PageWidget.TYPE_SUMMARY.equals(widget.getType())){
					summaryCfg = (SummaryCfgVO)summaryCfgPro.doView(widget.getActionContent());
				}
			}
		} catch (Exception e) {
			System.err.println(e);
		}
		return summaryCfg;
	}
	
	public static String pendingNumberXml(WebUser webUser) throws Exception{
		String userid = webUser.getId();
		UserDefinedProcess udprocss = (UserDefinedProcess) ProcessFactory.createProcess(UserDefinedProcess.class);
		String defaultApplication = webUser.getDefaultApplication();
		ParamsTable params1 = new ParamsTable();
		//获取当前用户自定义首页		
		params1.setParameter("t_applicationid", webUser.getDefaultApplication());
		params1.setParameter("t_userId", userid);
		params1.setParameter("i_useddefined", UserDefined.IS_DEFINED);
		params1.setParameter("_orderby", "id");
		DataPackage<UserDefined> dataPackage=udprocss.doQuery(params1);
		if(dataPackage.rowCount > 0){
			UserDefined userDefined = new UserDefined();
			for(Iterator<UserDefined> ite1 = dataPackage.datas.iterator();ite1.hasNext();){
				userDefined = (UserDefined)ite1.next();
			}
			return pendingElement(defaultApplication, userDefined,webUser);
		}else{
			//无自定义首页时,获取后台定制的默认首页
			Collection<RoleVO> userRoles = webUser.getRoles();
			RoleVO roleVO = new RoleVO();
			params1 = new ParamsTable();
			params1.setParameter("t_applicationid", defaultApplication);
			params1.setParameter("n_published", true);
			params1.setParameter("_orderby", "id");
			DataPackage<UserDefined> dataPackage1=udprocss.doQuery(params1);
			if(dataPackage1.rowCount>0){
				for(Iterator<UserDefined> ite1 = dataPackage1.datas.iterator();ite1.hasNext();){
					UserDefined userDefined = (UserDefined)ite1.next();
					//判断是否适用于所有角色
					if("1".equals(userDefined.getDisplayTo())){
						return pendingElement(defaultApplication, userDefined,webUser);
					}else{
						//获取某一首页的角色
						String roleIds = userDefined.getRoleIds();
						if(!StringUtil.isBlank(roleIds)){
							String[] userRoleIds = roleIds.split(",");
							for(int i=0;i<userRoleIds.length;i++){
								if(userRoles.size()>0){
									for(Iterator<RoleVO> ite2 = userRoles.iterator();ite2.hasNext();){
										roleVO = (RoleVO)ite2.next();
										if(userRoleIds[i].equals(roleVO.getId())){
											//当前角色与 后台首页待办设置的角色 相同时，返回此后台定制的首页待办信息
											return pendingElement(defaultApplication, userDefined,webUser);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return "<"+MobileConstant.TAG_PENDINGNUMBER+" size='0'></"+MobileConstant.TAG_PENDINGNUMBER+">";
	}
	
	/**
	 * 获取默认代办总数
	 * 
	 * @param applicationid
	 * @param userDefined
	 * @param webUser
	 * @return
	 * @throws Exception
	 */
	public static String pendingElement(String applicationid, UserDefined userDefined, WebUser webUser) throws Exception{
		int tab1count = 0;
		int tab2count = 0;
		
		String templateElement = userDefined.getTemplateElement();
		if(!StringUtil.isBlank(templateElement) && templateElement.length() > 1){
			templateElement = templateElement.substring(1, templateElement.length() - 1);
			//获取各布局单元格和对应的元素
			String[] templateElements = (String[]) templateElement.split(",");
			for (int i = 0; i < templateElements.length; i++) {
				String[] templateElementSubs = (String[]) templateElements[i].split(":");
				if(!StringUtil.isBlank(templateElementSubs[0]) && templateElementSubs[0].length() > 1){
					//单元格摘要Id和title
					String[] summaryIds = templateElementSubs[1].split(";");
					String templateTdEle = summaryIds[0].substring(1, summaryIds[0].length() - 1);
					//摘要id数组
					summaryIds = templateTdEle.split("\\|");
					if(summaryIds.length == 1 && summaryIds[0].equals("")){
						continue;
					}
					for(int j = 0; j<summaryIds.length; j++){
						SummaryCfgVO summaryCfg = summaryIdCheck(summaryIds[j]);
						if(summaryCfg == null){
							continue;
						}
						if(summaryCfg.getScope()==SummaryCfgVO.SCOPE_PENDING){//代办
							ParamsTable summaryCfgParams = new ParamsTable();
							summaryCfgParams.setParameter("formid", summaryCfg.getFormId());
							summaryCfgParams.setParameter("application", applicationid);
							summaryCfgParams.setParameter("_orderby", summaryCfg.getOrderby());
							PendingProcess pendingProcess = new PendingProcessBean(applicationid);
							DataPackage<PendingVO> pendings = pendingProcess.doQueryByFilter(summaryCfgParams, webUser);
							int size = pendings.rowCount;
							if(size >0){
								for(Iterator<PendingVO> ites = pendings.datas.iterator();ites.hasNext();){
									PendingVO pending = (PendingVO)ites.next();
									if(pending.getState() == null){
										size--;
									}
								}
							}
							tab1count += size;
						}else if(summaryCfg.getScope()==SummaryCfgVO.SCOPE_CIRCULATOR){//代阅
							ParamsTable circulatorParams = new ParamsTable();
							circulatorParams.setParameter("formid", summaryCfg.getFormId());
							circulatorParams.setParameter("application", applicationid);
							CirculatorProcess circulatorProcess = (CirculatorProcess) ProcessFactory.createRuntimeProcess(CirculatorProcess.class, applicationid);
							DataPackage<Circulator> circulators = circulatorProcess.getPendingByUser(circulatorParams,webUser);
							tab2count += circulators.rowCount;
						}
					}
				}
			}
		}
		return "<"+MobileConstant.TAG_PENDINGNUMBER+" size='"+(tab1count+tab2count)+"'></"+MobileConstant.TAG_PENDINGNUMBER+">";
	}
	
	public Collection<RoleVO> getRolesByApplication(WebUser user, String application) throws Exception {
		UserProcess up = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
		UserVO currUser = (UserVO) up.doView(user.getId());
		Collection<RoleVO> roles = currUser.getRoles();
		Collection<RoleVO> rtn = new ArrayList<RoleVO>();
		for (Iterator<RoleVO> iterator = roles.iterator(); iterator.hasNext();) {
			RoleVO roleVO = (RoleVO) iterator.next();
			if (roleVO.getApplicationid().equals(application)) {
				rtn.add(roleVO);
			}
		}

		return rtn;
	}
	
}
