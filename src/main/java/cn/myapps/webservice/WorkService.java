package cn.myapps.webservice;

import java.util.ArrayList;
import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.dynaform.work.ejb.WorkVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiProcess;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;
import cn.myapps.core.workflow.storage.runtime.ejb.Circulator;
import cn.myapps.core.workflow.storage.runtime.ejb.CirculatorProcess;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.json.JsonUtil;
import cn.myapps.util.xml.XmlUtil;
import cn.myapps.webservice.fault.WorkServiceFault;
import cn.myapps.webservice.model.SimpleCirculator;
import cn.myapps.webservice.model.SimpleWork;
import cn.myapps.webservice.util.WorkUtil;

/**
 * 提供用户待办、已办、待阅、已阅文档的查询功能接口
 * @author Ivan
 *
 */
public class WorkService {

/****************************************** 待办已办部分 ***************************************************/
	/**
	 * 查询待办、已办数据
	 * 
	 * @param params
	 *            参数集
	 * @param user
	 *            用户Id
	 * @param applicationId
	 *            软件Id
	 * @return
	 * 			DataPackage<WorkVO>
	 */
	private DataPackage<WorkVO> queryWorks(ParamsTable params,
			  WebUser user, String applicationId) throws WorkServiceFault {
		try {
			WebServiceUtil.validateApplicationById(applicationId);
			DocumentProcess docProcess = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,
					applicationId);
			String _currpage = params.getParameterAsString("_currpage");
			String _pagelines = params.getParameterAsString("_pagelines");

			int page = (_currpage != null && _currpage.length() > 0) ? Integer
					.parseInt(_currpage) : 1;
			int lines = (_pagelines != null && _pagelines.length() > 0) ? Integer
					.parseInt(_pagelines) : Integer.MAX_VALUE;
			return docProcess.queryWorkBySQLPage(params, page, lines, user);
		} catch (Exception e) {
			e.printStackTrace();
			throw new WorkServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new WorkServiceFault(e.getMessage());
			}
		}
	}
	
	/**
	 * 查询待办、已办数据,并转换为SimpleWork集合
	 * @param params
	 * @param userId
	 * @param applicationId
	 * @return
	 * @throws WorkServiceFault
	 */
	private Collection<SimpleWork> queryWorksByUser(ParamsTable params, String userId, String applicationId)
			throws WorkServiceFault {
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			UserVO user = (UserVO) userProcess.doView(userId);
			if(user != null){
				return WorkUtil.convertToSimpleDatas(queryWorks(params, new WebUser(user), applicationId));
			}
			return new ArrayList<SimpleWork>();
		} catch (Exception e) {
			e.printStackTrace();
			throw new WorkServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new WorkServiceFault(e.getMessage());
			}
		}
	}
	
	/**
	 * axis自动生成SimpleWork类需要一个返回SimpleWork的public方法
	 * @return
	 */
	public SimpleWork getSimpleWorkInstance(){
		return new SimpleWork();
	}
	
	/**
	 * 传入用户Id、软件Id查询用户的待办文档返回SimpleWork对象集合
	 * 
	 * @param userId
	 *            用户Id
	 * @param applicationId
	 *            软件Id
	 * @return 返回SimpleWork对象集合
	 * @throws WorkServiceFault
	 */
	public Collection<SimpleWork> getPendingWorkByUser(String userId, String applicationId)
			throws WorkServiceFault {
		ParamsTable params = new ParamsTable();
		params.setParameter("_processType", "processing");
		return queryWorksByUser(params, userId, applicationId);
	}
	
	/**
	 * 传入用户Id、软件Id查询用户的待办文档集合，返回xml字符串
	 * 
	 * @param userId
	 *            用户Id
	 * @param applicationId
	 *            软件Id
	 * @return 返回xml字符串
	 * @throws WorkServiceFault
	 */
	public String getPendingWorkByUserFormat2XML(String userId, String applicationId)
			throws WorkServiceFault {
		Collection<SimpleWork> datas = getPendingWorkByUser(userId, applicationId);
		return XmlUtil.toXml(datas);
	}
	
	/**
	 * 传入用户Id、软件Id查询用户的待办文档集合，返回Json字符串
	 * 
	 * @param userId
	 *            用户Id
	 * @param applicationId
	 *            软件Id
	 * @return 返回Json字符串
	 * @throws WorkServiceFault
	 */
	public String getPendingWorkByUserFormat2Json(String userId, String applicationId)
			throws WorkServiceFault {
		Collection<SimpleWork> datas = getPendingWorkByUser(userId, applicationId);
		return JsonUtil.collection2Json(datas);
	}
	
	/**
	 * 传入用户Id、软件Id查询用户的已办文档返回SimpleWork对象集合
	 * 
	 * @param userId
	 *            用户Id
	 * @param applicationId
	 *            软件Id
	 * @return 返回SimpleWork对象集合
	 * @throws WorkServiceFault
	 */
	public Collection<SimpleWork> getProcessedWorkByUser(String userId, String applicationId)
			throws WorkServiceFault {
		ParamsTable params = new ParamsTable();
		params.setParameter("_processType", "processed");
		return queryWorksByUser(params, userId, applicationId);
	}
	
	/**
	 * 传入用户Id、软件Id查询用户的已办文档集合，返回xml字符串
	 * 
	 * @param userId
	 *            用户Id
	 * @param applicationId
	 *            软件Id
	 * @return 返回xml字符串
	 * @throws WorkServiceFault
	 */
	public String getProcessedWorkByUserFormat2XML(String userId,String applicationId)
			throws WorkServiceFault {
		Collection<SimpleWork> datas = getProcessedWorkByUser(userId, applicationId);
		return XmlUtil.toXml(datas);
	}
	
	/**
	 * 传入用户Id、软件Id查询用户的已办文档集合，返回Json字符串
	 * 
	 * @param userId
	 *            用户Id
	 * @param applicationId
	 *            软件Id
	 * @return 返回Json字符串
	 * @throws WorkServiceFault
	 */
	public String getProcessedWorkByUserFormat2Json(String userId,String applicationId)
			throws WorkServiceFault {
		Collection<SimpleWork> datas = getProcessedWorkByUser(userId, applicationId);
		return JsonUtil.collection2Json(datas);
	}
	
	private Form doViewByFormName(String formName, String application)
			throws WorkServiceFault {
		try {
			if(StringUtil.isBlank(formName)){
				throw new WorkServiceFault("formName不能为空");
			}
			FormProcess process = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
			return process.doViewByFormName(formName, application);
		} catch (Exception e) {
			throw new WorkServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new WorkServiceFault(e.getMessage());
			}
		}
	}
	
	private BillDefiVO doViewBySubject(String subject, String applicationId)
			throws WorkServiceFault {
		try {
			if(StringUtil.isBlank(subject)){
				throw new WorkServiceFault("flowName不能为空");
			}
			BillDefiProcess process = (BillDefiProcess) ProcessFactory.createProcess(BillDefiProcess.class);
			return process.doViewBySubject(subject, applicationId);
		} catch (Exception e) {
			throw new WorkServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new WorkServiceFault(e.getMessage());
			}
		}
	}
	
	/**
	 * 传入用户Id、软件Id、表单名称、流程名称、当前页数、一页显示数(为空表示查询所有)查询用户的待办文档返回SimpleWork对象集合
	 * 
	 * @param userId
	 *            用户Id
	 * @param formName
	 *            表单名称
	 * @param flowName
	 *            流程名称
	 * @param applicationId
	 *            软件Id
	 * @param currpage
	 *            当前页数
	 * @param pagelines
	 *            一页显示数
	 * @return 返回SimpleWork对象集合
	 * @throws WorkServiceFault
	 */
	public Collection<SimpleWork> getPendingWorkByUser(String userId,String formName ,String flowName,String applicationId,int currpage,int pagelines)
			throws WorkServiceFault {
		try {
			WebServiceUtil.validateApplicationById(applicationId);
			ParamsTable params = new ParamsTable();
			params.setParameter("_processType", "processing");
			params.setParameter("_currpage", String.valueOf(currpage));
			params.setParameter("_pagelines", String.valueOf(pagelines));
			if(!StringUtil.isBlank(formName)){
				Form form = doViewByFormName(formName, applicationId);
				if(form == null)
					throw new WorkServiceFault("表单[" + formName + "]不存在软件[id="+applicationId+"]下.");
				params.setParameter("_formId", form.getId());
			}
			if(!StringUtil.isBlank(flowName)){
				BillDefiVO flow = doViewBySubject(flowName, applicationId);
				if(flow == null)
					throw new WorkServiceFault("流程[" + flowName + "]不存在软件[id="+applicationId+"]下.");
				params.setParameter("_flowId", flow.getId());
			}
			return queryWorksByUser(params, userId, applicationId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new WorkServiceFault(e.getMessage());
		}
	}
	
	/**
	 * 传入用户Id、软件Id、表单名称、流程名称、当前页数、一页显示数(为空表示查询所有)查询用户的待办文档集合，返回xml
	 * 
	 * @param userId
	 *            用户Id
	 * @param formName
	 *            表单名称
	 * @param flowName
	 *            流程名称
	 * @param applicationId
	 *            软件Id
	 * @param currpage
	 *            当前页数
	 * @param pagelines
	 *            一页显示数
	 * @return 返回xml字符串
	 * @throws WorkServiceFault
	 */
	public String getPendingWorkByUserFormat2XML(String userId,String formName ,String flowName,String applicationId, int currpage,int pagelines)
			throws WorkServiceFault {
		Collection<SimpleWork> datas = getPendingWorkByUser(userId, formName, flowName, applicationId, currpage, pagelines);
		return XmlUtil.toXml(datas);
	}
	
	/**
	 * 传入用户Id、软件Id、表单名称、流程名称、当前页数、一页显示数(为空表示查询所有)查询用户的待办文档集合，返回Json
	 * 
	 * @param userId
	 *            用户Id
	 * @param formName
	 *            表单名称
	 * @param flowName
	 *            流程名称
	 * @param applicationId
	 *            软件Id
	 * @param currpage
	 *            当前页数
	 * @param pagelines
	 *            一页显示数
	 * @return 返回Json字符串
	 * @throws WorkServiceFault
	 */
	public String getPendingWorkByUserFormat2Json(String userId,String formName ,String flowName,String applicationId, int currpage,int pagelines)
			throws WorkServiceFault {
		Collection<SimpleWork> datas = getPendingWorkByUser(userId, formName, flowName, applicationId, currpage, pagelines);
		return JsonUtil.collection2Json(datas);
	}
	
	/**
	 * 传入用户Id、软件Id、表单名称、流程名称、当前页数、一页显示数(为空表示查询所有)查询用户的已办文档返回SimpleWork对象集合
	 * 
	 * @param userId
	 *            用户Id
	 * @param formName
	 *            表单名称
	 * @param flowName
	 *            流程名称
	 * @param applicationId
	 *            软件Id
	 * @param currpage
	 *            当前页数
	 * @param pagelines
	 *            一页显示数
	 * @return 返回SimpleWork对象集合
	 * @throws WorkServiceFault
	 */
	public Collection<SimpleWork> getProcessedWorkByUser(String userId,String formName ,String flowName,String applicationId,int currpage,int pagelines)
			throws WorkServiceFault {
		try {
			WebServiceUtil.validateApplicationById(applicationId);
			ParamsTable params = new ParamsTable();
			params.setParameter("_processType", "processed");
			params.setParameter("_currpage", String.valueOf(currpage));
			params.setParameter("_pagelines", String.valueOf(pagelines));
			if(!StringUtil.isBlank(formName)){
				Form form = doViewByFormName(formName, applicationId);
				if(form == null)
					throw new WorkServiceFault("表单[" + formName + "]不存在软件[id="+applicationId+"]下.");
				params.setParameter("_formId", form.getId());
			}
			if(!StringUtil.isBlank(flowName)){
				BillDefiVO flow = doViewBySubject(flowName, applicationId);
				if(flow == null)
					throw new WorkServiceFault("流程[" + flowName + "]不存在软件[id="+applicationId+"]下.");
				params.setParameter("_flowId", flow.getId());
			}
			return queryWorksByUser(params, userId, applicationId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new WorkServiceFault(e.getMessage());
		}
	}
	
	/**
	 * 传入用户Id、软件Id、表单名称、流程名称、当前页数、一页显示数(为空表示查询所有)查询用户的已办文档集合，返回xml
	 * 
	 * @param userId
	 *            用户Id
	 * @param formName
	 *            表单名称
	 * @param flowName
	 *            流程名称
	 * @param applicationId
	 *            软件Id
	 * @param currpage
	 *            当前页数
	 * @param pagelines
	 *            一页显示数
	 * @return 返回xml字符串
	 * @throws WorkServiceFault
	 */
	public String getProcessedWorkByUserFormat2XML(String userId, String formName ,String flowName,String applicationId, int currpage,int pagelines)
			throws WorkServiceFault {
		Collection<SimpleWork> datas = getProcessedWorkByUser(userId, formName, flowName, applicationId, currpage, pagelines);
		return XmlUtil.toXml(datas); 
	}
	
	/**
	 * 传入用户Id、软件Id、表单名称、流程名称、当前页数、一页显示数(为空表示查询所有)查询用户的已办文档集合，返回Json
	 * 
	 * @param userId
	 *            用户Id
	 * @param formName
	 *            表单名称
	 * @param flowName
	 *            流程名称
	 * @param applicationId
	 *            软件Id
	 * @param currpage
	 *            当前页数
	 * @param pagelines
	 *            一页显示数
	 * @return 返回Json字符串
	 * @throws WorkServiceFault
	 */
	public String getProcessedWorkByUserFormat2Json(String userId, String formName ,String flowName,String applicationId, int currpage,int pagelines)
			throws WorkServiceFault {
		Collection<SimpleWork> datas = getProcessedWorkByUser(userId, formName, flowName, applicationId, currpage, pagelines);
		return JsonUtil.collection2Json(datas);
	}
/****************************************** End 待办已办部分 ***************************************************/
	
	
/****************************************** 待阅已阅部分 ***************************************************/
	/**
	 * 查询待阅、已阅数据
	 * 
	 * @param params
	 *            参数集
	 * @param user
	 *            用户Id
	 * @param applicationId
	 *            软件Id
	 * @return
	 * 			DataPackage<WorkVO>
	 */
	private DataPackage<Circulator> queryWorks4Cc(ParamsTable params,
			  WebUser user, String applicationId) throws WorkServiceFault {
		try {
			WebServiceUtil.validateApplicationById(applicationId);
			CirculatorProcess process = (CirculatorProcess) ProcessFactory.createRuntimeProcess(CirculatorProcess.class,
					applicationId);
			String _currpage = params.getParameterAsString("_currpage");
			String _pagelines = params.getParameterAsString("_pagelines");

			int page = (_currpage != null && _currpage.length() > 0) ? Integer
					.parseInt(_currpage) : 1;
			int lines = (_pagelines != null && _pagelines.length() > 0) ? Integer
					.parseInt(_pagelines) : Integer.MAX_VALUE;
					
			params.setParameter("_currpage", String.valueOf(page));
			params.setParameter("_pagelines", String.valueOf(lines));
			
			return process.getWorksByUser(params, user);
		} catch (Exception e) {
			e.printStackTrace();
			throw new WorkServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new WorkServiceFault(e.getMessage());
			}
		}
	}
	
	/**
	 * 查询待阅、已阅数据,并转换为SimpleCirculator集合
	 * @param params
	 * @param userId
	 * @param applicationId
	 * @return
	 * @throws WorkServiceFault
	 */
	private Collection<SimpleCirculator> queryWorksByUser4Cc(ParamsTable params, String userId, String applicationId)
			throws WorkServiceFault {
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			UserVO user = (UserVO) userProcess.doView(userId);
			if(user != null){
				return WorkUtil.convertToSimpleDatas4Cc(queryWorks4Cc(params, new WebUser(user), applicationId));
			}
			return new ArrayList<SimpleCirculator>();
		} catch (Exception e) {
			e.printStackTrace();
			throw new WorkServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new WorkServiceFault(e.getMessage());
			}
		}
	}

	/**
	 * axis自动生成SimpleCirculator类需要一个返回SimpleCirculator的public方法
	 * @return
	 */
	public SimpleCirculator getSimpleCirculatorInstance(){
		return new SimpleCirculator();
	}
	
	/**
	 * 传入用户Id、软件Id查询用户的待阅文档返回SimpleCirculator对象集合
	 * 
	 * @param userId
	 *            用户Id
	 * @param applicationId
	 *            软件Id
	 * @return 返回SimpleCirculator对象集合
	 * @throws WorkServiceFault
	 */
	public Collection<SimpleCirculator> getPendingInfoByUser(String userId,	String applicationId)
			throws WorkServiceFault {
		ParamsTable params = new ParamsTable();
		params.setParameter("_isRead", "0");
		return queryWorksByUser4Cc(params, userId, applicationId);
	}
	
	/**
	 * 传入用户Id、软件Id查询用户的待阅文档集合，返回xml字符串
	 * 
	 * @param userId
	 *            用户Id
	 * @param applicationId
	 *            软件Id
	 * @return 返回xml字符串
	 * @throws WorkServiceFault
	 */
	public String getPendingInfoByUserFormat2XML(String userId,	String applicationId)
			throws WorkServiceFault {
		Collection<SimpleCirculator> datas = getPendingInfoByUser(userId, applicationId);
		return XmlUtil.toXml(datas);
	}
	
	/**
	 * 传入用户Id、软件Id查询用户的待阅文档集合，返回Json字符串
	 * 
	 * @param userId
	 *            用户Id
	 * @param applicationId
	 *            软件Id
	 * @return 返回Json字符串
	 * @throws WorkServiceFault
	 */
	public String getPendingInfoByUserFormat2Json(String userId, String applicationId)
			throws WorkServiceFault {
		Collection<SimpleCirculator> datas = getPendingInfoByUser(userId, applicationId);
		return JsonUtil.collection2Json(datas);
	}
	
	/**
	 * 传入用户Id、软件Id查询用户的已阅文档返回SimpleCirculator对象集合
	 * 
	 * @param userId
	 *            用户Id
	 * @param applicationId
	 *            软件Id
	 * @return 返回SimpleCirculator对象集合
	 * @throws WorkServiceFault
	 */
	public Collection<SimpleCirculator> getProcessedInfoByUser(String userId, String applicationId)
			throws WorkServiceFault {
		ParamsTable params = new ParamsTable();
		params.setParameter("_isRead", "1");
		return queryWorksByUser4Cc(params, userId, applicationId);
	}
	
	/**
	 * 传入用户Id、软件Id查询用户的已阅文档集合，返回xml字符串
	 * 
	 * @param userId
	 *            用户Id
	 * @param applicationId
	 *            软件Id
	 * @return 返回xml字符串
	 * @throws WorkServiceFault
	 */
	public String getProcessedInfoByUserFormat2XML(String userId,String applicationId)
			throws WorkServiceFault {
		Collection<SimpleCirculator> datas = getProcessedInfoByUser(userId, applicationId);
		return XmlUtil.toXml(datas);
	}
	
	/**
	 * 传入用户Id、软件Id查询用户的已阅文档集合，返回Json字符串
	 * 
	 * @param userId
	 *            用户Id
	 * @param applicationId
	 *            软件Id
	 * @return 返回Json字符串
	 * @throws WorkServiceFault
	 */
	public String getProcessedInfoByUserFormat2Json(String userId,String applicationId)
			throws WorkServiceFault {
		Collection<SimpleCirculator> datas = getProcessedInfoByUser(userId, applicationId);
		return JsonUtil.collection2Json(datas); 
	}
	
	/**
	 * 传入用户Id、软件Id、表单名称、流程名称、当前页数、一页显示数(为空表示查询所有)查询用户的待阅文档返回SimpleCirculator对象集合
	 * 
	 * @param userId
	 *            用户Id
	 * @param formName
	 *            表单名称
	 * @param flowName
	 *            流程名称
	 * @param applicationId
	 *            软件Id
	 * @param currpage
	 *            当前页数
	 * @param pagelines
	 *            一页显示数
	 * @return 返回SimpleCirculator对象集合
	 * @throws WorkServiceFault
	 */
	public Collection<SimpleCirculator> getPendingInfoByUser(String userId,String formName ,String flowName,String applicationId, int currpage,int pagelines)
			throws WorkServiceFault {
		try {
			WebServiceUtil.validateApplicationById(applicationId);
			ParamsTable params = new ParamsTable();
			params.setParameter("_isRead", "0");
			params.setParameter("_currpage", String.valueOf(currpage));
			params.setParameter("_pagelines", String.valueOf(pagelines));
			if(!StringUtil.isBlank(formName)){
				Form form = doViewByFormName(formName, applicationId);
				if(form == null)
					throw new WorkServiceFault("表单[" + formName + "]不存在软件[id="+applicationId+"]下.");
				params.setParameter("_formId", form.getId());
			}
			if(!StringUtil.isBlank(flowName)){
				BillDefiVO flow = doViewBySubject(flowName, applicationId);
				if(flow == null)
					throw new WorkServiceFault("流程[" + flowName + "]不存在软件[id="+applicationId+"]下.");
				params.setParameter("_flowId", flow.getId());
			}
			return queryWorksByUser4Cc(params, userId, applicationId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new WorkServiceFault(e.getMessage());
		}
	}
	
	/**
	 * 传入用户Id、软件Id、表单名称、流程名称、当前页数、一页显示数(为空表示查询所有)查询用户的待阅文档集合，返回xml
	 * 
	 * @param userId
	 *            用户Id
	 * @param formName
	 *            表单名称
	 * @param flowName
	 *            流程名称
	 * @param applicationId
	 *            软件Id
	 * @param currpage
	 *            当前页数
	 * @param pagelines
	 *            一页显示数
	 * @return 返回xml字符串
	 * @throws WorkServiceFault
	 */
	public String getPendingInfoByUserFormat2XML(String userId,String formName ,String flowName,String applicationId, int currpage,int pagelines)
			throws WorkServiceFault {
		Collection<SimpleCirculator> datas = getPendingInfoByUser(userId, formName, flowName, applicationId, currpage, pagelines);
		return XmlUtil.toXml(datas);
	}
	
	/**
	 * 传入用户Id、软件Id、表单名称、流程名称、当前页数、一页显示数(为空表示查询所有)查询用户的待阅文档集合，返回Json
	 * 
	 * @param userId
	 *            用户Id
	 * @param formName
	 *            表单名称
	 * @param flowName
	 *            流程名称
	 * @param applicationId
	 *            软件Id
	 * @param currpage
	 *            当前页数
	 * @param pagelines
	 *            一页显示数
	 * @return 返回Json字符串
	 * @throws WorkServiceFault
	 */
	public String getPendingInfoByUserFormat2Json(String userId,String formName ,String flowName,String applicationId, int currpage,int pagelines)
			throws WorkServiceFault {
		Collection<SimpleCirculator> datas = getPendingInfoByUser(userId, formName, flowName, applicationId, currpage, pagelines);
		return JsonUtil.collection2Json(datas);
	}
	
	/**
	 * 传入用户Id、软件Id、表单名称、流程名称、当前页数、一页显示数(为空表示查询所有)查询用户的已阅文档返回SimpleCirculator对象集合
	 * 
	 * @param userId
	 *            用户Id
	 * @param formName
	 *            表单名称
	 * @param flowName
	 *            流程名称
	 * @param applicationId
	 *            软件Id
	 * @param currpage
	 *            当前页数
	 * @param pagelines
	 *            一页显示数
	 * @return 返回SimpleCirculator对象集合
	 * @throws WorkServiceFault
	 */
	public Collection<SimpleCirculator> getProcessedInfoByUser(String userId,String formName ,String flowName,String applicationId, int currpage,int pagelines)
			throws WorkServiceFault {
		try {
			WebServiceUtil.validateApplicationById(applicationId);
			ParamsTable params = new ParamsTable();
			params.setParameter("_isRead", "1");
			params.setParameter("_currpage", String.valueOf(currpage));
			params.setParameter("_pagelines", String.valueOf(pagelines));
			if(!StringUtil.isBlank(formName)){
				Form form = doViewByFormName(formName, applicationId);
				if(form == null)
					throw new WorkServiceFault("表单[" + formName + "]不存在软件[id="+applicationId+"]下.");
				params.setParameter("_formId", form.getId());
			}
			if(!StringUtil.isBlank(flowName)){
				BillDefiVO flow = doViewBySubject(flowName, applicationId);
				if(flow == null)
					throw new WorkServiceFault("流程[" + flowName + "]不存在软件[id="+applicationId+"]下.");
				params.setParameter("_flowId", flow.getId());
			}
			return queryWorksByUser4Cc(params, userId, applicationId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new WorkServiceFault(e.getMessage());
		}
	}
	
	/**
	 * 传入用户Id、软件Id、表单名称、流程名称、当前页数、一页显示数(为空表示查询所有)查询用户的已阅文档集合，返回xml
	 * 
	 * @param userId
	 *            用户Id
	 * @param formName
	 *            表单名称
	 * @param flowName
	 *            流程名称
	 * @param applicationId
	 *            软件Id
	 * @param currpage
	 *            当前页数
	 * @param pagelines
	 *            一页显示数
	 * @return 返回xml字符串
	 * @throws WorkServiceFault
	 */
	public String getProcessedInfoByUserFormat2XML(String userId, 	String formName ,String flowName,String applicationId, int currpage,int pagelines)
			throws WorkServiceFault {
		Collection<SimpleCirculator> datas = getProcessedInfoByUser(userId, formName, flowName, applicationId, currpage, pagelines);
		return XmlUtil.toXml(datas);
	}
	
	/**
	 * 传入用户Id、软件Id、表单名称、流程名称、当前页数、一页显示数(为空表示查询所有)查询用户的已阅文档集合，返回Json
	 * 
	 * @param userId
	 *            用户Id
	 * @param formName
	 *            表单名称
	 * @param flowName
	 *            流程名称
	 * @param applicationId
	 *            软件Id
	 * @param currpage
	 *            当前页数
	 * @param pagelines
	 *            一页显示数
	 * @return 返回Json字符串
	 * @throws WorkServiceFault
	 */
	public String getProcessedInfoByUserFormat2Json(String userId, 	String formName ,String flowName,String applicationId, int currpage,int pagelines)
			throws WorkServiceFault {
		Collection<SimpleCirculator> datas = getProcessedInfoByUser(userId, formName, flowName, applicationId, currpage, pagelines);
		return JsonUtil.collection2Json(datas);
	}
	
/****************************************** End 待阅已阅部分 ***************************************************/
	
}
