package cn.myapps.core.datamap.definition.action;

import java.util.Map;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.BaseAction;
import cn.myapps.constans.Web;
import cn.myapps.core.datamap.definition.ejb.DataMapCfgProcess;
import cn.myapps.core.datamap.definition.ejb.DataMapCfgVO;
import cn.myapps.core.deploy.module.ejb.ModuleProcess;
import cn.myapps.core.deploy.module.ejb.ModuleVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;

/**
 * @author Happy
 *
 */
public class DataMapCfgAction extends BaseAction<DataMapCfgVO> {
	private static final long serialVersionUID = -3453088712323446750L;
	
	
	private String _datamapCfgId;
	private String _culeValue;
	private String _culeValue2;
	private String applicationId;
	private String domainId;
	
	private String _result;
	
	

	public String get_datamapCfgId() {
		return _datamapCfgId;
	}

	public void set_datamapCfgId(String datamapCfgId) {
		_datamapCfgId = datamapCfgId;
	}

	public String get_culeValue() {
		return _culeValue;
	}

	public void set_culeValue(String culeValue) {
		_culeValue = culeValue;
	}
	
	public String get_culeValue2() {
		return _culeValue2;
	}

	public void set_culeValue2(String _culeValue2) {
		this._culeValue2 = _culeValue2;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}
	
	

	public String get_result() {
		return _result;
	}

	@SuppressWarnings("unchecked")
	public DataMapCfgAction() throws ClassNotFoundException {
		super(ProcessFactory.createProcess(DataMapCfgProcess.class), new DataMapCfgVO());
	}
	
	public String get_moduleid() {
		DataMapCfgVO vo = (DataMapCfgVO) getContent();
		if (vo.getModule() != null) {
			return vo.getModule().getId();
		}
		return null;
	}

	public void set_moduleid(String moduleid) {
		DataMapCfgVO vo = (DataMapCfgVO) getContent();
		if (moduleid != null) {
			ModuleProcess mp;
			try {
				mp = (ModuleProcess) ProcessFactory.createProcess((ModuleProcess.class));
				ModuleVO module = (ModuleVO) mp.doView(moduleid);
				vo.setModule(module);
			}catch (OBPMValidateException e) {
				this.addFieldError("", e.getValidateMessage());
			}catch (Exception e) {
				this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
				e.printStackTrace();
			}
		}
	}
	
	public WebUser getWebUser(){
		Map session = getContext().getSession();
		WebUser user = (WebUser) session.get(Web.SESSION_ATTRIBUTE_FRONT_USER);
		
		return user;
	}
	
	public String doGetClueFields() {
		try {
			this._result = ((DataMapCfgProcess)process).getClueFields(_datamapCfgId, getParams(),  getWebUser(), applicationId);
		}catch (OBPMValidateException e) {
			this.addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String doGetSummaryFields() {
		
		try {
			this._result = ((DataMapCfgProcess)process).getSummaryFields(_datamapCfgId, _culeValue,_culeValue2, applicationId, domainId);
		} catch (OBPMValidateException e) {
			this.addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		
		return SUCCESS;
	}


}
