package cn.myapps.core.widget.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.constans.Web;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.dynaform.pending.ejb.PendingProcess;
import cn.myapps.core.dynaform.pending.ejb.PendingProcessBean;
import cn.myapps.core.dynaform.pending.ejb.PendingVO;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgProcess;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgVO;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.personalmessage.ejb.PersonalMessageProcess;
import cn.myapps.core.personalmessage.ejb.PersonalMessageVO;
import cn.myapps.core.report.crossreport.definition.ejb.CrossReportProcess;
import cn.myapps.core.report.crossreport.definition.ejb.CrossReportVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.widget.ejb.PageWidget;
import cn.myapps.core.widget.ejb.PageWidgetProcess;
import cn.myapps.core.workflow.analyzer.AnalyzerProcess;
import cn.myapps.core.workflow.analyzer.AnalyzerProcessBean;
import cn.myapps.core.workflow.analyzer.FlowAnalyzerVO;
import cn.myapps.core.workflow.storage.runtime.ejb.Circulator;
import cn.myapps.core.workflow.storage.runtime.ejb.CirculatorProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.CirculatorProcessBean;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.json.JsonUtil;
import cn.myapps.util.report.ChartBuilderFactory;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class PageWidgetRuntimeAction extends ActionSupport implements Action {
	
	private static final String SUMMARY_CIRCULATOR = "summary_circulator";

	private static final long serialVersionUID = 6562101325414269627L;
	private DataPackage datas;
	private PageWidget widget;

	public PageWidget getWidget() {
		return widget;
	}

	public void setWidget(PageWidget widget) {
		this.widget = widget;
	}

	public DataPackage getDatas() {
		return datas;
	}

	public void setDatas(DataPackage datas) {
		this.datas = datas;
	}

	private WebUser getUser() {
		Map session = ActionContext.getContext().getSession();
		WebUser user = (WebUser) session.get(Web.SESSION_ATTRIBUTE_FRONT_USER);
		return user;
	}

	public String doDisplayWidget() {
		try {
			WebUser user = getUser();
			ParamsTable params = ParamsTable.convertHTTP(ServletActionContext
					.getRequest());
			String widgetId = params.getParameterAsString("id");
			
			PageWidget widget = null;
			if(widgetId.startsWith("system_")){
				widget = PageWidget.newSystemWidget(widgetId);
			}else{
				widget = (PageWidget) getProcess().doView(widgetId);
			}
			setWidget(widget);//设置内容
			String applicationId = widget.getApplicationid();
			if (PageWidget.TYPE_SUMMARY.equals(widget.getType())) {
				SummaryCfgProcess summaryCfgProcess = (SummaryCfgProcess) ProcessFactory
						.createProcess(SummaryCfgProcess.class);
				String lines = params.getParameterAsString("_pagelines");
				if (lines==null || lines.isEmpty()) {
					params.setParameter("_pagelines", "6");					
				}

				String summaryCfgId = widget.getActionContent();
				SummaryCfgVO summaryCfg = (SummaryCfgVO) summaryCfgProcess
						.doView(summaryCfgId);

				if (summaryCfg != null) {
					PendingProcess process = new PendingProcessBean(
							applicationId);
					CirculatorProcess cprocess = new CirculatorProcessBean(applicationId);
					if(summaryCfg.getScope()==0){
						params.setParameter("formid", summaryCfg.getFormId());
						params.setParameter("_orderby", summaryCfg.getOrderby());
						DataPackage<PendingVO> result = process.doQueryByFilter(
								params, getUser());
						setDatas(result);
						return PageWidget.TYPE_SUMMARY;
					}
					else if(summaryCfg.getScope()==6){
						params.setParameter("_isRead", 0);
						DataPackage<Circulator> result = cprocess.getWorksByUser(
								params, getUser());
						setDatas(result);
						return SUMMARY_CIRCULATOR;
					}
				}
				
			} else if (PageWidget.TYPE_VIEW.equals(widget.getType())) {
				String viewid = widget.getActionContent();
				ViewProcess viewProcess = (ViewProcess) ProcessFactory
						.createProcess(ViewProcess.class);
				View view = (View) viewProcess.doView(viewid);
				if (view != null) {
					String[] orderFields = view.getDefaultOrderFieldArr();
					params.setParameter("_sortCol", orderFields);	//增加排序
					
					DataPackage result = view.getViewTypeImpl().getViewDatas(
							params, getUser(), new Document());

					//ViewHtmlBean是一个很糟糕的类，需要重构，现在先用，并为此准备session数据
					ServletActionContext.getRequest().setAttribute("content",
							view);
					setDatas(result);
				}
				return PageWidget.TYPE_VIEW;
			} else if (PageWidget.TYPE_ACTIVITY.equals(widget.getType())) {
			} else if (PageWidget.TYPE_PAGE.equals(widget.getType())) {
				return PageWidget.TYPE_PAGE;
			} else if (PageWidget.TYPE_LINK.equals(widget.getType())) {
				//返回widget，由link页面处理
				return PageWidget.TYPE_LINK;
			} else if (PageWidget.TYPE_REPORT.equals(widget.getType())) {

			} else if (PageWidget.TYPE_RUNQUAN_REPORT.equals(widget.getType())) {

			}else if (PageWidget.TYPE_CUSTOMIZE_REPORT.equals(widget.getType())) {
				return PageWidget.TYPE_CUSTOMIZE_REPORT;
			}else if (PageWidget.TYPE_SYSTEM_ANNOUNCEMENT.equals(widget.getType())) {
				PersonalMessageProcess personalMessageProcess = (PersonalMessageProcess) ProcessFactory.createProcess(PersonalMessageProcess.class);
				Collection<PersonalMessageVO> list = personalMessageProcess.doQueryAnnouncementsByUser(user);
				ServletActionContext.getRequest().setAttribute("data", list);
				return PageWidget.TYPE_SYSTEM_ANNOUNCEMENT;
				
			}else if (PageWidget.TYPE_SYSTEM_WORKFLOW.equals(widget.getType())) {
				return PageWidget.TYPE_SYSTEM_WORKFLOW;
				
			}else if (PageWidget.TYPE_SYSTEM_WEATHER.equals(widget.getType())) {
				return PageWidget.TYPE_SYSTEM_WEATHER;
				
			}else if(PageWidget.TYPE_ISCRIPT.equals(widget.getType())){
				String html = "";
				if(!StringUtil.isBlank(widget.getActionContent())){
					IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(),widget.getApplicationid());
					runner.initBSFManager(null, params, user, new ArrayList<ValidateMessage>());
					try {
						Object result = runner.run("Widget["+widget.getName()+"]-"+widget.getId(), widget.getActionContent());
						if(result instanceof String){
							html = (String) result;
						}else{
							throw new Exception("Widget["+widget.getName()+"]脚本运算结果不合法，请返回字符串！");
						}
					} catch (Exception e) {
						html = e.getMessage();
					}
					ActionContext.getContext().put("data", html);
					
				}
			}

			return widget.getType();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}


	//TODO 代码逻辑需要优化
	public void doDisplayWidgetTotal() {
		int total = 0;
		
		try {
			WebUser user = getUser();
			ParamsTable params = ParamsTable.convertHTTP(ServletActionContext
					.getRequest());
			String widgetId = params.getParameterAsString("id");
			
			PageWidget widget = null;
			if(widgetId.startsWith("system_")){
				widget = PageWidget.newSystemWidget(widgetId);
			}else{
				widget = (PageWidget) getProcess().doView(widgetId);
			}
			setWidget(widget);//设置内容
			String applicationId = widget.getApplicationid();
			
			JSONObject jsonO = new JSONObject();
			jsonO.put("widgetType", widget.getType());
			
			if (PageWidget.TYPE_SUMMARY.equals(widget.getType())) {
				SummaryCfgProcess summaryCfgProcess = (SummaryCfgProcess) ProcessFactory
						.createProcess(SummaryCfgProcess.class);
				params.setParameter("_pagelines", Integer.MAX_VALUE);

				String summaryCfgId = widget.getActionContent();
				SummaryCfgVO summaryCfg = (SummaryCfgVO) summaryCfgProcess
						.doView(summaryCfgId);

				if (summaryCfg != null) {
					params.setParameter("formid", summaryCfg.getFormId());
					params.setParameter("_orderby", summaryCfg.getOrderby());
					PendingProcess process = new PendingProcessBean(
							applicationId);
					DataPackage<PendingVO> result = process.doQueryByFilter(
							params, getUser());
					
					total = result.datas.size();
					jsonO.put("id", "");
					jsonO.put("total", total);
					PrintWriter writer = ServletActionContext.getResponse().getWriter();
					writer.print(jsonO);
					writer.close();
					
				}
			}else if (PageWidget.TYPE_SYSTEM_ANNOUNCEMENT.equals(widget.getType())) {
				PersonalMessageProcess personalMessageProcess = (PersonalMessageProcess) ProcessFactory.createProcess(PersonalMessageProcess.class);
				Collection<PersonalMessageVO> list = personalMessageProcess.doQueryAnnouncementsByUser(user);
				total = list.size();
				jsonO.put("id", "");
				jsonO.put("total", total);
				PrintWriter writer = ServletActionContext.getResponse().getWriter();
				writer.print(jsonO);
				writer.close();
			} else if (PageWidget.TYPE_VIEW.equals(widget.getType())) {
				String viewid = widget.getActionContent();
				jsonO.put("id", viewid);
				jsonO.put("total", total);
				PrintWriter writer = ServletActionContext.getResponse().getWriter();
				writer.print(jsonO);
				writer.close();
			}else {
				PrintWriter writer = ServletActionContext.getResponse().getWriter();
				writer.print("null");
				writer.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		try {
//			PrintWriter writer = ServletActionContext.getResponse().getWriter();
//			writer.print(total);
//			writer.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	/**
	 * 获取自定义报表的数据内容
	 */
	public void doGetCustomizeReportData(){
		try {
			ParamsTable params = ParamsTable.convertHTTP(ServletActionContext.getRequest());
			WebUser user = getUser();
			String reportId = params.getParameterAsString("id");
			if(!StringUtil.isBlank(reportId)){
				CrossReportProcess reportProcess =(CrossReportProcess) ProcessFactory.createProcess(CrossReportProcess.class);
				CrossReportVO cReport =  (CrossReportVO)reportProcess.doView(reportId);
				if(cReport !=null){
					String reportJson = cReport.getJson();
					if(!StringUtil.isBlank(reportJson)){
						JSONObject cfg = JSONObject.fromObject(reportJson);
						String defaultChart = cfg.getString("defaultChart");//默认显示图表类型
						//生成OpenFlashChart报表数据
//						String data = ChartBuilderFactory.getChartBuilder(defaultChart).bulidOpenFlashChart(cReport, user);
						//生成EChart报表数据
						String datae = ChartBuilderFactory.getChartBuilder(defaultChart).bulidEChart(cReport, user);
						HttpServletResponse response = ServletActionContext.getResponse();
						response.setHeader("ContentType", "text/json");  
			            response.setCharacterEncoding("utf-8");  
			            PrintWriter pw = response.getWriter(); 
//			            pw.write(data);
			            pw.write(datae);
			            pw.flush();  
			            pw.close();
					}
				}
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 显示个人信息
	 * @return
	 * @throws Exception
	 */
	public String doShowMsg() throws Exception {
		ParamsTable params = ParamsTable.convertHTTP(ServletActionContext.getRequest());
		PersonalMessageProcess pmProcess = (PersonalMessageProcess) ProcessFactory.createProcess(PersonalMessageProcess.class);
		
		String msgid = params.getParameterAsString("id");
		String isRead = params.getParameterAsString("read");
		
		if (!StringUtil.isBlank(msgid)) {
			if (!StringUtil.isBlank(isRead)) {
				PersonalMessageVO pmVO = (PersonalMessageVO) pmProcess
						.doView(msgid);
				String allReader = pmProcess.doFindAllReader(pmVO.getSenderId(),pmVO.getReceiverId(),pmVO.getBody().getId());
				if(allReader != null){
					String[] allReaders = allReader.split(";");
					if(allReaders.length > 1){
						pmVO.setIsReadReceiverId(allReaders[0]);
						pmVO.setNoReadReceiverId(allReaders[1]);
					}else if(allReaders.length > 0){
						pmVO.setIsReadReceiverId(allReaders[0]);
					}
				}
				if (isRead.equalsIgnoreCase("false")) {
					pmProcess.doUpdate(pmVO);
				}
				ServletActionContext.getRequest().setAttribute("PersonalMessageVO", pmVO);
				if(PersonalMessageVO.MESSAGE_TYPE_ANNOUNCEMENT.equals(pmVO.getType())){
					return "announcement";
				}
			}

		}

		return INPUT;
	}
	
	public String doShowWorkflowAnalyzer(){
		try {
			ParamsTable params = ParamsTable.convertHTTP(ServletActionContext.getRequest());
			WebUser user = getUser();
			String type =  params.getParameterAsString("type");
			String applicationId = params.getParameterAsString("applicationId");
			String dateRange = params.getParameterAsString("dateRange")!=null? params.getParameterAsString("dateRange") : "TODAY";
			Collection<FlowAnalyzerVO> datas = null;
			if("analyzerActorTimeConsumingTopX".equals(type)){//流程结点耗时-TOP10
				AnalyzerProcessBean process = (AnalyzerProcessBean)ProcessFactory.createRuntimeProcess(AnalyzerProcess.class,applicationId);
				datas = process.doAnalyzerActorTimeConsumingTopX(params,dateRange,10,"", user);
			}else if("flowTimeConsumingAccounting".equals(type)){//流程耗时占比
				AnalyzerProcessBean process = (AnalyzerProcessBean)ProcessFactory.createRuntimeProcess(AnalyzerProcess.class, applicationId);
				datas = process.doAnalyzerFlowTimeConsumingAccounting(params, dateRange, "", user);
			}else if("flowAndNodeTimeConsuming".equals(type)){//流程&节点耗时统计
				AnalyzerProcessBean process = (AnalyzerProcessBean)ProcessFactory.createRuntimeProcess(AnalyzerProcess.class,applicationId);
				datas = process.doAnalyzerFlowAndNodeTimeConsuming(params, dateRange, "", user);
			}else if("flowAccounting".equals(type)){//流程实例占比
				AnalyzerProcessBean process = (AnalyzerProcessBean)ProcessFactory.createRuntimeProcess(AnalyzerProcess.class,applicationId);
				datas = process.doAnalyzerFlowAccounting(params, dateRange, "", user);
			}
			ActionContext.getContext().put("type", type);
			ActionContext.getContext().put("applicationId", applicationId);
			ActionContext.getContext().put("dateRange", dateRange);
			ActionContext.getContext().put("DATA", datas);
			return type;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	

	public PageWidgetProcess getProcess() throws ClassNotFoundException {
		PageWidgetProcess process = (PageWidgetProcess) ProcessFactory
				.createProcess(PageWidgetProcess.class);
		return process;
	}

}
