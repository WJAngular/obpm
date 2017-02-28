package cn.myapps.core.dynaform.work.action;


import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.constans.Web;
import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.dynaform.document.action.DocumentAction;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.work.ejb.WorkVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.engine.StateMachine;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiProcess;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;
import cn.myapps.km.util.StringUtil;
import cn.myapps.util.ProcessFactory;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author Happy
 * 
 */
public class WorkAction extends DocumentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6517074447281803556L;

	private DataPackage<WorkVO> workDatas;

//	private String isWorkManager;
	
	private String _flowId;

	/**
	 * 下一个节点数组
	 */
	private String[] _nextids;

	/**
	 * 当前节点id
	 */
	private String _currid;

	private String _flowType;

	private String _attitude;
	

	/*
	 * 用户所选择地提交对象{node0:u1;u2,node1:u1;u3}
	 */
	private String submitTo;

	private String _moduleId;

	public String get_moduleId() {
		return _moduleId;
	}

	public void set_moduleId(String moduleId) {
		_moduleId = moduleId;
	}

	public String getSubmitTo() {
		return submitTo;
	}

	public void setSubmitTo(String submitTo) {
		this.submitTo = submitTo;
	}

	public String get_attitude() {
		return _attitude;
	}

	public void set_attitude(String _attitude) {
		this._attitude = _attitude;
	}

	public String get_flowType() {
		return _flowType;
	}

	public void set_flowType(String type) {
		_flowType = type;
	}

	public String get_currid() {
		return _currid;
	}

	public void set_currid(String _currid) {
		this._currid = _currid;
	}

	public String[] get_nextids() {
		return _nextids;
	}

	public void set_nextids(String[] _nextids) {
		this._nextids = _nextids;
	}

	/**
	 * Get the DataPackage with WorkVO
	 * 
	 * @return
	 */
	public DataPackage<WorkVO> getWorkDatas() {
		return workDatas;
	}

	/**
	 * @param workDatas
	 */
	public void setWorkDatas(DataPackage<WorkVO> workDatas) {
		this.workDatas = workDatas;
	}
/*
 * 
	public String getIsWorkManager() {
		try{
			if(isWorkManager ==null || isWorkManager.trim().length()<=0){
				for (Iterator<RoleVO> iter = getUser().getRoles().iterator(); iter
				.hasNext();) {
					if (RoleVO.WORK_MANAGER_TRUE.equals(iter.next().getIsWorkManager())) {
						return RoleVO.WORK_MANAGER_TRUE;
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return isWorkManager;
	}

	public void setIsWorkManager(String isWorkManager) {
		this.isWorkManager = isWorkManager;
	}
*/	
	

	public String get_flowId() {
		return _flowId;
	}

	public void set_flowId(String flowId) {
		_flowId = flowId;
	}

	public WorkAction() throws ClassNotFoundException {
		super();
	}

	/**
	 * 查询出符合条件的工作列表
	 * 
	 * @return
	 */
	public String doWorkList() {

		try {
			this.setWorkDatas(((DocumentProcess) getProcess()).queryWorks(
					getParams(), getUser()));
			return SUCCESS;
		}  catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}

	}

	/**
	 * 流程提交
	 * 
	 * @return SUCCESS or INPUT
	 * @throws Exception
	 */
	public String doFlow() throws Exception {
		WebUser user = this.getUser();

		if (user.getStatus() == 1) {
			try {
				ParamsTable params = getParams();
				Document doc = (Document) getProcess().doView(
						params.getParameterAsString("_docid"));
				((DocumentProcess) getProcess()).doFlow(doc, params,
						get_currid(), get_nextids(), get_flowType(),
						get_attitude(), user);
				getProcess().doUpdate(doc);
				set_attitude("");// 将remarks清空
			}catch (OBPMValidateException e) {
				this.addFieldError("System Error", e.getValidateMessage());
				return INPUT;
			}catch (Exception e) {
				this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
				e.printStackTrace();
				return INPUT;
			}
			this.setWorkDatas(((DocumentProcess) getProcess()).queryWorks(
					getParams(), getUser()));// 刷新列表
			return SUCCESS;
		} else {
			this.addFieldError("System Error", "{*[core.user.noeffectived]*}");
			return INPUT;
		}
	}

	public String doCommissionedWork() throws Exception {
		try {
			ParamsTable params = getParams();
			Document doc = (Document) getProcess().doView(
					params.getParameterAsString("_docid"));
			StateMachine.commissionedWork(doc, params, getUser());
		} catch (OBPMValidateException e) {
			this.addFieldError("System Error", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
		this.setWorkDatas(((DocumentProcess) getProcess()).queryWorks(
				getParams(), getUser()));// 刷新列表
		return SUCCESS;
	}

	public String doRemoveWork() throws Exception {
		try {
			((DocumentProcess)getProcess()).doRemove(getParams().getParameterAsArray("_docid"));
		} catch (OBPMValidateException e) {
			this.addFieldError("System Error", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
		
		return doGetPendingList();
	}
	
	/**
	 * 获取待办任务列表
	 * @return
	 */
	public String doGetPendingList() {
		try {
		
			ParamsTable params = getParams();
			
			String flowId = params.getParameterAsString("_flowId");
			String subject = params.getParameterAsString("_subject");
			boolean isFlowAgent = params.getParameterAsBoolean("isFlowAgent");
			
			//分页数据
			String _pagelines = null;
			String _currpage = params.getParameterAsString("_currpage") ;
			Cookie[] cookies = ServletActionContext.getRequest().getCookies();
			for(Cookie cookie : cookies){
				if(Web.FILELIST_PAGELINE.equals(cookie.getName())){
					_pagelines = cookie.getValue();
				}
			}
			_pagelines = params.getParameterAsString("_pagelines");
			int currpage = (_currpage != null && _currpage.length() > 0 && !"null".equals(_currpage)) ? Integer
					.parseInt(_currpage) : 1;
			//boolean showWorkFlowProxy = (_showWorkFlowProxy != null && _showWorkFlowProxy.equals("true"));
			int pagelines = (_pagelines != null && _pagelines.length() > 0 && !"null".equals(_pagelines)) ? Integer
					.parseInt(_pagelines) : 10;  //待办列表应该按照topX的方式获取，而无需翻页，还可以继续优化 --Jarod

			this.setWorkDatas(((DocumentProcess) getProcess()).doQueryProcessingWorks(getUser(), flowId, subject, currpage, pagelines,isFlowAgent));
			return SUCCESS;
		}  catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}

	}
	
	/**
	 * 获取经办任务列表
	 * @return
	 */
	public String doGetProcessedList() {
		try {
			ParamsTable params = getParams();
			String flowId = params.getParameterAsString("_flowId");
			String subject = params.getParameterAsString("_subject");
			
			//分页数据
			String _pagelines = null;
			String _currpage = params.getParameterAsString("_currpage") ;
			Cookie[] cookies = ServletActionContext.getRequest().getCookies();
			for(Cookie cookie : cookies){
				if(Web.FILELIST_PAGELINE.equals(cookie.getName())){
					_pagelines = cookie.getValue();
				}
			}

			if(params.getParameterAsString("_pagelines") != null){
				_pagelines = params.getParameterAsString("_pagelines");
			}
			int currpage = (_currpage != null && _currpage.length() > 0 && !"null".equals(_currpage)) ? Integer
					.parseInt(_currpage) : 1;
			//boolean showWorkFlowProxy = (_showWorkFlowProxy != null && _showWorkFlowProxy.equals("true"));
			int pagelines = (_pagelines != null && _pagelines.length() > 0 && !"null".equals(_pagelines)) ? Integer
					.parseInt(_pagelines) : 10;  //待办列表应该按照topX的方式获取，而无需翻页，还可以继续优化 --Jarod


			this.setWorkDatas(((DocumentProcess) getProcess()).doQueryProcessedRunningWorks(getUser(), flowId, subject, currpage, pagelines));
			return SUCCESS;
		}  catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}

	}
	
	/**
	 * 获取历史任务列表
	 * @return
	 */
	public String doGetHistoryList() {
		try {
			ParamsTable params = getParams();
			String _currpage = params.getParameterAsString("_currpage");
			String _pagelines = params.getParameterAsString("_pagelines");
			String flowId = params.getParameterAsString("_flowId");
			String subject = params.getParameterAsString("_subject");
			Boolean myInitiatedList = true;   //  isMyInitiatedList  为 我创办的 ，默认显示自身创办的
			if(!"1".equals(params.getParameterAsString("_isMyWorkFlow"))){
				myInitiatedList = false;      // notMyInitiatedList 为 我经办的， 前台传空值
			}

			int currpage = (_currpage != null && _currpage.length() > 0) ? Integer
					.parseInt(_currpage) : 1;
			int pagelines = (_pagelines != null && _pagelines.length() > 0) ? Integer
					.parseInt(_pagelines) : 200;
			
			this.setWorkDatas(((DocumentProcess) getProcess()).doQueryProcessedCompletedWorks(getUser(), flowId, subject,myInitiatedList, currpage, pagelines));
			return SUCCESS;
		}  catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}
	/**
	 * 获取所有历史任务列表
	 * @return
	 */
	public String doGetAllHistoryList() {
		try {
			ParamsTable params = getParams();
			String _currpage = params.getParameterAsString("_currpage");
			String _pagelines = params.getParameterAsString("_pagelines");
			String flowId = params.getParameterAsString("_flowId");
			String subject = params.getParameterAsString("_subject");

			int currpage = (_currpage != null && _currpage.length() > 0) ? Integer
					.parseInt(_currpage) : 1;
			int pagelines = (_pagelines != null && _pagelines.length() > 0) ? Integer
					.parseInt(_pagelines) : 200;
			
			this.setWorkDatas(((DocumentProcess) getProcess()).doQueryAllProcessedCompletedWorks(getUser(), flowId, subject, currpage, pagelines));
			return SUCCESS;
		}  catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}
	
	/**
	 * 根据软件id获取软件的流程列表
	 */
	public void  getFlowListByApplication(){
		try {
			ParamsTable params = getParams();
			String applicationId = params.getParameterAsString("applicationId");
			
			JSONArray list = new JSONArray();
			BillDefiProcess bp = (BillDefiProcess) ProcessFactory.createProcess(BillDefiProcess.class);

			//添加排序条件
			ParamsTable _params = new ParamsTable();
			_params.setParameter("_orderby", "subject");
			_params.setParameter("_desc", "asc");
			Collection<BillDefiVO> col = bp.doSimpleQuery(_params, applicationId);

			Iterator<BillDefiVO> it = col.iterator();
			while (it.hasNext()) {
				BillDefiVO bv = (BillDefiVO) it.next();
				JSONObject item = new JSONObject();
				item.put("id", bv.getId());
				item.put("name", bv.getSubject());
				list.add(item);
			}
			
			HttpServletResponse response =   ServletActionContext.getResponse();
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().print(list.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * Widget获取待办任务列表
	 * @return
	 */
	public String doGetWidgetPendingList() {
		try {
			
			ParamsTable params = getParams();
			String _currpage = params.getParameterAsString("_currpage");
			String flowId = params.getParameterAsString("_flowId");
			String subject = params.getParameterAsString("_subject");
			boolean isFlowAgent = params.getParameterAsBoolean("isFlowAgent");
			
			int currpage = (_currpage != null && _currpage.length() > 0) ? Integer.parseInt(_currpage) : 1;
			int pagelines = 200;
			
			WebUser user = getUser();
			Set<String> applicationIds = user.getApplicationIds();
			DataPackage<WorkVO> works = new DataPackage<WorkVO>();
			int rowCount = 0;
		
			if(applicationIds!=null || applicationIds.size() != 0){
				for (String applicationId : applicationIds) {
					ApplicationProcess process = (ApplicationProcess)ProcessFactory.createProcess(ApplicationProcess.class);
					ApplicationVO applicationVO = (ApplicationVO) process.doView(applicationId);
					if(applicationVO == null)
						continue;
					if (!applicationVO.isActivated())
						continue;
					DocumentProcess documentProcess = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,applicationVO.getId());
					DataPackage<WorkVO> tmp_works = documentProcess.doQueryProcessingWorks(getUser(), flowId, subject, currpage, pagelines,isFlowAgent);
				    
				    if(tmp_works == null || tmp_works.getDatas().size() == 0 )
				    	continue;
				    
				    rowCount = works.getRowCount() + tmp_works.getRowCount();
				    works.getDatas().addAll(tmp_works.getDatas());
				}
			 }
			    works.setRowCount(rowCount);
			    works.setLinesPerPage(pagelines);
			    works.setPageNo(1);
			    
			    this.setWorkDatas(works);
			    return SUCCESS;
		}  catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}

	}
	
	/**
	 * Widget获取待办任务列表
	 * @return
	 */
	public String doGetWidgetProcessedList() {

		try {
			ParamsTable params = getParams();
			String _currpage = params.getParameterAsString("_currpage");
			String flowId = params.getParameterAsString("_flowId");
			String subject = params.getParameterAsString("_subject");

			int currpage = (_currpage != null && _currpage.length() > 0) ? Integer.parseInt(_currpage) : 1;
			int pagelines = 200;

			WebUser user = getUser();
			Set<String> applicationIds = user.getApplicationIds();
			DataPackage<WorkVO> works = new DataPackage<WorkVO>();
			int rowCount = 0;

			if(applicationIds!=null || applicationIds.size() != 0){
				for (String applicationId : applicationIds) {
					ApplicationProcess process = (ApplicationProcess) ProcessFactory
							.createProcess(ApplicationProcess.class);
					ApplicationVO applicationVO = (ApplicationVO) process
							.doView(applicationId);
					if (applicationVO == null)
						continue;
					if (!applicationVO.isActivated())
						continue;
					DocumentProcess documentProcess = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,applicationVO.getId());
					DataPackage<WorkVO> tmp_works = documentProcess.doQueryProcessedRunningWorks(getUser(), flowId, subject, currpage, pagelines);

					if (tmp_works == null || tmp_works.getDatas().size() == 0)
						continue;

					rowCount = works.getRowCount() + tmp_works.getRowCount();
					works.getDatas().addAll(tmp_works.getDatas());

				}
			}
			
			works.setRowCount(rowCount);
			works.setLinesPerPage(pagelines);
			works.setPageNo(1);
			this.setWorkDatas(works);
			return SUCCESS;
		} catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return INPUT;
		} catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException(
					"{*[OBPMRuntimeException]*}", e));
			e.printStackTrace();
			return INPUT;
		}

	}

}
