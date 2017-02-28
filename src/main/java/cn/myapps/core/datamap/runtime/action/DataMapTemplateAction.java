package cn.myapps.core.datamap.runtime.action;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.AbstractRunTimeAction;
import cn.myapps.base.ejb.IRunTimeProcess;
import cn.myapps.core.datamap.runtime.ejb.DataMapTemplate;
import cn.myapps.core.datamap.runtime.ejb.DataMapTemplateProcess;
import cn.myapps.core.datamap.runtime.ejb.DataMapTemplateProcessBean;
import cn.myapps.util.StringUtil;

/**
 * @author Happy
 *
 */
public class DataMapTemplateAction extends
		AbstractRunTimeAction<DataMapTemplate> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 918305876666146827L;
	
	/**
	 * 数据地图主键
	 */
	private String _datamapCfgId;
	/**
	 * 线索字段值
	 */
	private String _culeField;
	/**
	 * 线索字段2值
	 */
	private String _culeField2;
	
	/**
	 * 模板内容
	 */
	private String _template;
	
	/**
	 * 模板
	 */
	private DataMapTemplate template = new DataMapTemplate();
	
	private String _result;
	
	

	public String get_datamapCfgId() {
		return _datamapCfgId;
	}



	public void set_datamapCfgId(String datamapCfgId) {
		_datamapCfgId = datamapCfgId;
	}



	public String get_culeField() {
		return _culeField;
	}



	public void set_culeField(String culeField) {
		_culeField = culeField;
	}

	public String get_culeField2() {
		return _culeField2;
	}

	public void set_culeField2(String _culeField2) {
		this._culeField2 = _culeField2;
	}

	public DataMapTemplate getTemplate() {
		return template;
	}

	public void setTemplate(DataMapTemplate template) {
		this.template = template;
	}

	public String get_result() {
		return _result;
	}
	
	public String get_template() {
		return _template;
	}

	public void set_template(String template) {
		_template = template;
	}



	@Override
	public IRunTimeProcess<DataMapTemplate> getProcess() {
			return new DataMapTemplateProcessBean(getApplication());
	}
	
	
	
	public String doGetTemplate() {
		DataMapTemplate vo = null;
		try {
			String templateid = this.get_datamapCfgId()+"_"+this.get_culeField();
			if(!StringUtil.isBlank(this.get_culeField2())){
				templateid += "_"+this.get_culeField2();
			}
			vo = (DataMapTemplate)getProcess().doView(templateid);
		} catch (OBPMValidateException e) {
			this.addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		if(vo != null){
			this._result = vo.getTemplate();
		}
		return SUCCESS;
	}
	
	public String doGetMapContent(){
		DataMapTemplate vo = null;
		try {
			String templateid = this.get_datamapCfgId()+"_"+this.get_culeField();
			if(!StringUtil.isBlank(this.get_culeField2())){
				templateid += "_"+this.get_culeField2();
			}
			vo = (DataMapTemplate)getProcess().doView(templateid);
		} catch (OBPMValidateException e) {
			this.addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		if(vo != null && !StringUtil.isBlank(vo.getTemplate())){
			try {
				this._result = vo.generateDataMapContent();
			}catch (OBPMValidateException e) {
				this.addFieldError("", e.getValidateMessage());
			}catch (Exception e) {
				this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}
	
	public String doSave(){
		DataMapTemplate vo = new DataMapTemplate();
		vo.setDatamapCfgId(this.get_datamapCfgId());
		vo.setTemplate(this.get_template());
		vo.setApplicationid(this.getApplication());
		vo.setDomainid(this.getDomain());
		vo.setCuleField(this.get_culeField());
		vo.setCuleField2(this.get_culeField2());
		try {
			((DataMapTemplateProcess)getProcess()).doCreateOrUpdate(vo);
		} catch (OBPMValidateException e) {
			this.addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String doEdit() {
		DataMapTemplate vo = null;
		try {
			String templateid = this.get_datamapCfgId()+"_"+this.get_culeField();
			if(!StringUtil.isBlank(this.get_culeField2())){
				templateid += "_"+this.get_culeField2();
			}
			vo = (DataMapTemplate) getProcess().doView(templateid);
		} catch (OBPMValidateException e) {
			this.addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		if(vo != null){
			this.setTemplate(vo);
		}
		return SUCCESS;
	}
	


}
