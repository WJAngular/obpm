package cn.myapps.desktop.login;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.constans.Web;
import cn.myapps.core.deploy.application.action.ApplicationHelper;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.dynaform.pending.ejb.PendingProcess;
import cn.myapps.core.dynaform.pending.ejb.PendingProcessBean;
import cn.myapps.core.dynaform.pending.ejb.PendingVO;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgProcess;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgVO;
import cn.myapps.core.homepage.ejb.Reminder;
import cn.myapps.core.personalmessage.dao.PersonalMessageDAO;
import cn.myapps.core.personalmessage.ejb.PersonalMessageProcess;
import cn.myapps.core.personalmessage.ejb.PersonalMessageVO;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.core.table.constants.MobileConstant;
import cn.myapps.core.user.action.UserDefinedHelper;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserDefined;
import cn.myapps.core.user.ejb.UserDefinedProcess;
import cn.myapps.core.widget.ejb.PageWidget;
import cn.myapps.core.widget.ejb.PageWidgetProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorRT;
import cn.myapps.core.workflow.storage.runtime.ejb.Circulator;
import cn.myapps.core.workflow.storage.runtime.ejb.CirculatorProcess;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

public class DLoginHelper {

	private static final Logger LOG = Logger.getLogger(DLoginHelper.class);
	
	private HttpServletRequest request;
	private List<String> newCompare = new ArrayList<String>();
	private List<String> compareList = new ArrayList<String>();
	//private HttpServletResponse response;
	
	public DLoginHelper(HttpServletRequest request, 
			HttpServletResponse response) {
		this.request = request;
		//this.response = response;
	}
	
	/**
	 * @deprecated 旧版本方法，已掉弃
	 * @return
	 */
	public String getChangePendingXml() {
		return processPending2();
	}
	
	/**
	 * 代码需要优化
	 * @deprecated 旧版本方法，已掉弃
	 * @SuppressWarnings Servlet API不支持泛型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String processPending() {
		StringBuffer sb = new StringBuffer();
		List<String> newCompare = new ArrayList<String>();
		HttpSession session = request.getSession();
		List<String> compareList = (List<String>) session.getAttribute(DLoginAction.PENGING_LIST);
		WebUser webUser = (WebUser) session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
		try {
			DomainProcess process = (DomainProcess) ProcessFactory.createProcess(DomainProcess.class);
			DomainVO vo = (DomainVO) process.doView(webUser.getDomainid());
			Collection<ApplicationVO> apps = vo.getApplications();
			for (Iterator<ApplicationVO> it = apps.iterator(); it.hasNext();) {
				ApplicationVO app = (ApplicationVO) it.next();
				UserDefinedHelper hph = new UserDefinedHelper();
				hph.setApplicationid(app.getId());
				Collection<UserDefined> homes = hph.getDefaultHomePage(webUser);
				if (homes == null || homes.isEmpty()) {
					continue;
				}
				UserDefined home = (UserDefined) homes.iterator().next();
				Collection<Reminder> reminders = home.getReminders();
				for (Iterator<Reminder> it2 = reminders.iterator(); it2.hasNext();) {
					Reminder reminder = (Reminder) it2.next();
					PendingProcess pProcess = new PendingProcessBean(home
							.getApplicationid());
					ParamsTable params = new ParamsTable();
					params.setParameter("formid", reminder.getFormId());
					params.setParameter("_orderby", reminder.getOrderby());
					params.setParameter("_pagelines", Integer.MAX_VALUE + "");
					DataPackage<PendingVO> datas = pProcess.doQueryByFilter(params, webUser);
					Collection<PendingVO> list = datas.datas;
					for (Iterator<PendingVO> it3 = list.iterator(); it3.hasNext();) {
						PendingVO pending = (PendingVO) it3.next();
						if (!compareList.contains(pending.getId())) {
							// 增加一条待办
							sb.append("<"+MobileConstant.TAG_CHANGE + " " + MobileConstant.ATT_ID + "='" + pending.getId() + "' " + MobileConstant.ATT_GROUPID + "='" + app.getId() + "' " + MobileConstant.ATT_OPTION + "='1'>");
							
							sb.append("<" + MobileConstant.TAG_PENDING_ITEM + " ");
							String url = "/portal/dynaform/document/view.action";
							url += "?_docid=" + pending.getId();
							url += "&amp;_formid=" + pending.getFormid();
							url += "&amp;_backURL=" + request.getContextPath()
									+ "/portal/dispatch/homepage.jsp";
							sb.append(MobileConstant.ATT_ID + "='" + pending.getId()
									+ "' ");
							sb.append(MobileConstant.ATT_URL + "='" + url + "'>");
							sb.append("(" + reminder.getTitle() + ")" + pending.getSummary());
							sb.append("</" + MobileConstant.TAG_PENDING_ITEM + ">");
							
							sb.append("</"+MobileConstant.TAG_CHANGE + ">");
						}
						newCompare.add(pending.getId());
					}
				}
				try {
					PersistenceUtils.closeSessionAndConnection();
				} catch (Exception e) {
					LOG.warn(e);
				}
			}
			
			List<String> list = compare(compareList, newCompare);
			for (Iterator<String> it = list.iterator(); it.hasNext();) {
				String id = (String) it.next();
				// 删除一条待办
				sb.append("<"+MobileConstant.TAG_CHANGE + " " + MobileConstant.ATT_OPTION + "='0'>");
				sb.append(id).append("</"+MobileConstant.TAG_CHANGE + ">");
			}
			session.setAttribute(DLoginAction.PENGING_LIST, newCompare);
		} catch (Exception e) {
			LOG.warn(e);
			return sb.toString();
		}
		return sb.toString();
	}
	
	/**
	 * 刷新时判断是有增加或减少
	 * @author kharry
	 */
	@SuppressWarnings("unchecked")
	private String processPending2() {
		StringBuffer sb = new StringBuffer();
		newCompare = new ArrayList<String>();
		HttpSession session = request.getSession();
		compareList = (List<String>) session.getAttribute(DLoginAction.PENGING_LIST);
		String changpendlist = ((String[]) request.getParameterMap().get(DLoginAction.CHANGEPENGINGLIST))[0];
		if(!StringUtil.isBlank(changpendlist)){
			List<String> pendinglist = new ArrayList<String>();
			String[] pendings = changpendlist.split(";");
			for(int i=0;i<pendings.length;i++){
				pendinglist.add(pendings[i]);
			}
			compareList = pendinglist;
		}
		
		WebUser webUser = (WebUser) session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
		try {
			DomainProcess process = (DomainProcess) ProcessFactory.createProcess(DomainProcess.class);
			DomainVO vo = (DomainVO) process.doView(webUser.getDomainid());
			Collection<ApplicationVO> apps = vo.getApplications();
			for (Iterator<ApplicationVO> it = apps.iterator(); it.hasNext();) {
				Collection<PendingVO> cfgs = new ArrayList<PendingVO>();
				ApplicationVO app = (ApplicationVO) it.next();
				if(!app.isActivated()){
					continue;
				}
				UserDefinedProcess udprocss = (UserDefinedProcess) ProcessFactory.createProcess(UserDefinedProcess.class);
				ParamsTable params1 = new ParamsTable();
				//获取当前用户自定义首页		
				params1.setParameter("t_applicationid", app.getId());
				params1.setParameter("t_userId", webUser.getId());
				params1.setParameter("i_useddefined", UserDefined.IS_DEFINED);
				params1.setParameter("_orderby", "id");
				DataPackage<UserDefined> dataPackage=udprocss.doQuery(params1);
				if(dataPackage.rowCount >0){
					UserDefined userDefined = new UserDefined();
					for(Iterator<UserDefined> ite1 = dataPackage.datas.iterator();ite1.hasNext();){
						userDefined = (UserDefined)ite1.next();
						sb.append(changeTemplateElement(app.getId(), userDefined,webUser));
					}
				}else{//无自定义首页时,获取后台定制的默认首页
					Collection<RoleVO> userRoles = webUser.getRoles();
					RoleVO roleVO = new RoleVO();
					params1 = new ParamsTable();
					params1.setParameter("t_applicationid", app.getId());
					params1.setParameter("n_published", true);
					params1.setParameter("_orderby", "id");
					DataPackage<UserDefined> dataPackage1=udprocss.doQuery(params1);
					if(dataPackage1.rowCount>0){
						for(Iterator<UserDefined> ite1 = dataPackage1.datas.iterator();ite1.hasNext();){
							UserDefined userDefined = (UserDefined)ite1.next();
							//判断是否适用于所有角色
							if("1".equals(userDefined.getDisplayTo())){
								sb.append(changeTemplateElement(app.getId(), userDefined,webUser));
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
													sb.append(changeTemplateElement(app.getId(), userDefined,webUser));
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			List<String> list = compare(compareList, newCompare);
			for (Iterator<String> it4 = list.iterator(); it4.hasNext();) {
				String id = (String) it4.next();
				// 删除一条待办
				sb.append("<"+MobileConstant.TAG_CHANGE + " " + MobileConstant.ATT_OPTION + "='0'>");
				sb.append(id).append("</"+MobileConstant.TAG_CHANGE + ">");
			}
			session.setAttribute(DLoginAction.PENGING_LIST, newCompare);
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				LOG.warn(e);
			}
		} catch (Exception e) {
			LOG.warn(e);
			return sb.toString();
		}
		return sb.toString();
	}
	/**
	 * 查找参数1在参数2不存在的对象
	 * @param list1
	 * @param list2
	 * @return
	 */
	private List<String> compare(List<String> list1, List<String> list2) {
		List<String> list = new ArrayList<String>();
		for (Iterator<String> it = list1.iterator(); it.hasNext();) {
			String object = it.next();
			if (!list2.contains(object)) {
				list.add(object);
			}
		}
		return list;
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
	
	private String changeTemplateElement(String id, UserDefined userDefined,WebUser user) throws Exception {
		String templateElement = userDefined.getTemplateElement();
		StringBuffer sb = new StringBuffer();
		Collection<PendingVO> cfg = new ArrayList<PendingVO>();
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
							summaryCfgParams.setParameter("application", id);
							summaryCfgParams.setParameter("_orderby", summaryCfg.getOrderby());
							PendingProcess pendingProcess = new PendingProcessBean(id);
							DataPackage<PendingVO> pendings = pendingProcess.doQueryByFilter(summaryCfgParams, user);
							for (Iterator<PendingVO> iterator = pendings.datas.iterator(); iterator.hasNext();) {
								PendingVO pendingVO = (PendingVO) iterator.next();
								if (!compareList.contains(pendingVO.getDocId())) {
									// 增加一条待办
									sb.append("<"+MobileConstant.TAG_CHANGE + " " + MobileConstant.ATT_ID + "='" + pendingVO.getId() + "' " + MobileConstant.ATT_GROUPID + "='" + id + "' " + MobileConstant.ATT_OPTION + "='1'>");
									sb.append("<" + MobileConstant.TAG_PENDING_ITEM + " ");
									String scgurl = "/portal/dynaform/document/view.action";
									scgurl += "?_docid=" + pendingVO.getDocId();
									scgurl += "&amp;_formid=" + pendingVO.getFormid();
									scgurl += "&amp;_backURL=" + request.getContextPath()
											+ "/portal/dispatch/homepage.jsp";
									sb.append(MobileConstant.ATT_ID + "='" + pendingVO.getDocId()
											+ "' ");
									sb.append(MobileConstant.ATT_URL + "='" + scgurl + "'>");
									sb.append("[" + summaryCfg.getTitle() + "] "
											+ pendingVO.getSummary());
									sb.append("</" + MobileConstant.TAG_PENDING_ITEM + ">");
									
									sb.append("</"+MobileConstant.TAG_CHANGE + ">");
								}
								newCompare.add(pendingVO.getDocId());
							}
						}
						else if(summaryCfg.getScope()==SummaryCfgVO.SCOPE_CIRCULATOR){//代阅
							ParamsTable circulatorParams = new ParamsTable();
							circulatorParams.setParameter("formid", summaryCfg.getFormId());
							circulatorParams.setParameter("application", id);
							CirculatorProcess circulatorProcess = (CirculatorProcess) ProcessFactory.createRuntimeProcess(CirculatorProcess.class, id);
							DataPackage<Circulator> circulators = circulatorProcess.getPendingByUser(circulatorParams,user);
							for (Iterator<Circulator> iterator = circulators.datas.iterator(); iterator.hasNext();) {
								Circulator circulator = (Circulator) iterator.next();
								sb.append("<"+MobileConstant.TAG_CHANGE + " " + MobileConstant.ATT_ID + "='" + circulator.getId() + "' " + MobileConstant.ATT_GROUPID + "='" + id + "' " + MobileConstant.ATT_OPTION + "='1'>");
								sb.append("<" + MobileConstant.TAG_PENDING_ITEM + " ");
								String scgurl = "/portal/dynaform/document/view.action";
								scgurl += "?_docid=" + circulator.getId();
								scgurl += "&amp;_formid=" + circulator.getFormId();
								scgurl += "&amp;_backURL=" + request.getContextPath()
										+ "/portal/dispatch/homepage.jsp";
								sb.append(MobileConstant.ATT_ID + "='" + circulator.getId()
										+ "' ");
								sb.append(MobileConstant.ATT_URL + "='" + scgurl + "'>");
								sb.append("[" + summaryCfg.getTitle() + "] "
										+ circulator.getSummary());
								sb.append("</" + MobileConstant.TAG_PENDING_ITEM + ">");
								
								sb.append("</"+MobileConstant.TAG_CHANGE + ">");
							}
						}
					}
				}
			}
		}
		return sb.toString();
	}
	
	/**
     * 获取摘要待办
     * 
     * @param widget 摘要工具
     * @param user  用户
     * @param params 参数
     * @return
     * @throws Exception
     */
    public static Collection<PendingVO> getSummaryPendVO(PageWidget widget,WebUser user,ParamsTable params) throws Exception{
    	String applicationId = widget.getApplicationid();
    	SummaryCfgProcess summaryCfgProcess = (SummaryCfgProcess) ProcessFactory
				.createProcess(SummaryCfgProcess.class);
		params.removeParameter("_pagelines");

		String summaryCfgId = widget.getActionContent();
		SummaryCfgVO summaryCfg = (SummaryCfgVO) summaryCfgProcess
				.doView(summaryCfgId);

		if (summaryCfg != null) {
			
			try{
			
				params.setParameter("formid", summaryCfg.getFormId());
				params.setParameter("_orderby", summaryCfg.getOrderby());
				PendingProcess process = new PendingProcessBean(
						applicationId);
				DataPackage<PendingVO> result = process.doQueryByFilter(
						params, user);
				return result.getDatas();
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				try {
					PersistenceUtils.closeSessionAndConnection();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
    }
    
    /**
	 * 校验是否已读
	 * @param vo
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public static String checkRead(PendingVO vo,WebUser user) throws Exception{
		try{
			Collection<ActorRT> actors = vo.getState().getActors();
			for (Iterator<ActorRT> iter = actors.iterator(); iter.hasNext();) {
				ActorRT actor = (ActorRT) iter.next();
				if (user.getId().equals(actor.getActorid()) && actor.getIsread()) {
					return "true";
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		return "false";
	}
	
	public String doQueryNewMessage(WebUser user,ParamsTable params){
		StringBuffer sb = new StringBuffer();
		String time = params.getParameterAsString("time");
		String operationDate = "";
		SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		try{
			if(time != null && time.trim().length()>0){
				Date date = new Date(Long.valueOf(time));
				operationDate = format.format(date);
			}
		}catch(Exception e){
			 time = format.format(new Date());
		}
		sb.append("<Message>");
		try{
			PersonalMessageProcess process = (PersonalMessageProcess) ProcessFactory.createProcess(PersonalMessageProcess.class);
			int count = process.doQueryNewMessagWithTime(user.getId(), operationDate, params);
			sb.append(count);
		}catch(Exception e){
			sb.append(0);
		}
		sb.append("</Message>");
		return sb.toString();
	}
}
