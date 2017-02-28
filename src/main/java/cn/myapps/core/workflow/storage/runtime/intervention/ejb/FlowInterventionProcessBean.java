package cn.myapps.core.workflow.storage.runtime.intervention.ejb;

import java.util.Date;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.base.ejb.AbstractRunTimeProcessBean;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgProcess;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRT;
import cn.myapps.core.workflow.storage.runtime.intervention.dao.FlowInterventionDAO;
import cn.myapps.km.util.StringUtil;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.RuntimeDaoManager;

/**
 * @author Happy
 * 
 */
public class FlowInterventionProcessBean extends
		AbstractRunTimeProcessBean<FlowInterventionVO> implements
		FlowInterventionProcess {

	public FlowInterventionProcessBean(String applicationId) {
		super(applicationId);
	}

	@Override
	protected IRuntimeDAO getDAO() throws Exception {
		RuntimeDaoManager runtimeDao = new RuntimeDaoManager();
		FlowInterventionDAO flowInterventionDAO = (FlowInterventionDAO) runtimeDao
				.getFlowInterventionDAO(getConnection(), getApplicationId());
		return flowInterventionDAO;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3862446754753478202L;

	public void doCreateByDocument(Document doc, WebUser user) throws Exception {
		if (doc.getStateid() != null && doc.getStateid().length() > 0) {
			FlowInterventionVO vo = new FlowInterventionVO();
			vo.setId(doc.getId());
			SummaryCfgProcess summaryCfgProcess = (SummaryCfgProcess) ProcessFactory.createProcess(SummaryCfgProcess.class);
			SummaryCfgVO summaryCfg = (SummaryCfgVO) summaryCfgProcess.doViewByFormIdAndScope(doc.getFormid(), SummaryCfgVO.SCOPE_PENDING);
			if (summaryCfg != null) {
				vo.setSummary(summaryCfg.toSummay(doc,user));
			}else{
				vo.setSummary("");
			}
			vo.setInitiator(user.getName());
			vo.setInitiatorId(user.getId());
			vo.setInitiatorDeptId(user.getDefaultDepartment());
			DepartmentVO dept = user.getDepartmentById(user.getDefaultDepartment());
			vo.setInitiatorDept(dept!=null? dept.getName():"");
			vo.setLastAuditor(user.getName());
			vo.setApplicationid(doc.getApplicationid());
			vo.setFlowId(doc.getState().getFlowid());
			vo.setDocId(doc.getId());
			vo.setDomainid(doc.getDomainid());
			vo.setFirstProcessTime(doc.getAuditdate());
			vo.setLastProcessTime(new Date());
			vo.setVersion(doc.getVersion());
			vo.setFlowName(doc.getState().getFlowName());
			vo.setStateLabel(doc.getStateLabel());
			vo.setFormId(doc.getFormid());
			
			FlowStateRT state = doc.getState();
			if(state!=null && (state.isComplete() || state.isTerminated())){
				vo.setStatus(FlowInterventionVO.STATUS_COMPLETED);
			}else{
				vo.setStatus(FlowInterventionVO.STATUS_PENDING);
			}
			vo.setAuditorNames(doc.getAuditorNames());
			vo.setAuditorList(doc.getAuditorList());
			vo.setLastFlowOperation(doc.getLastFlowOperation());

			this.getDAO().create(vo);
		}
	}

	public void doUpdateByDocument(Document doc, WebUser user) throws Exception {
		if (doc.getFlowid() != null && doc.getFlowid().length() > 0) {
			FlowInterventionVO vo = (FlowInterventionVO) getDAO().find(
					doc.getId());
			if (vo != null) {
				SummaryCfgProcess summaryCfgProcess = (SummaryCfgProcess) ProcessFactory.createProcess(SummaryCfgProcess.class);
				SummaryCfgVO summaryCfg = (SummaryCfgVO) summaryCfgProcess.doViewByFormIdAndScope(doc.getFormid(), SummaryCfgVO.SCOPE_PENDING);
				if (summaryCfg != null) {
					vo.setSummary(summaryCfg.toSummay(doc,user));
				}else{
					vo.setSummary("");
				}
				vo.setLastAuditor(user.getName());
				vo.setLastProcessTime(new Date());
				vo.setStateLabel(doc.getStateLabel());
				vo.setVersion(doc.getVersion());
				if(doc.getState().isComplete() || doc.getState().isTerminated()){
					vo.setStatus(FlowInterventionVO.STATUS_COMPLETED);
				}else{
					vo.setStatus(FlowInterventionVO.STATUS_PENDING);
				}
				vo.setAuditorNames(doc.getAuditorNames());
				vo.setAuditorList(doc.getAuditorList());
				vo.setLastFlowOperation(doc.getLastFlowOperation());
				
				//TODO 判断对象为空时，从document中获取Initiator
				if(StringUtil.isBlank(vo.getInitiatorId())){
					vo.setInitiatorId(doc.getInitiator());
				}
				
				this.getDAO().update(vo);
			}else{
				doCreateByDocument(doc, user);
			}
		}
	}

	@Override
	public DataPackage<FlowInterventionVO> doQuery(ParamsTable params,
			WebUser user) throws Exception {
		if (getApplicationId() != null && getApplicationId().trim().length()>0) {
			return ((FlowInterventionDAO) this.getDAO()).queryByFilter(params,
					user);
		}
		return new DataPackage<FlowInterventionVO>();
	}

}
