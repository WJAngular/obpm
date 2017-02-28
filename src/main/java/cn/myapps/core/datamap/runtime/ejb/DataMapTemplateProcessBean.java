package cn.myapps.core.datamap.runtime.ejb;

import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.AbstractRunTimeProcessBean;
import cn.myapps.core.datamap.runtime.dao.DataMapTemplateDAO;
import cn.myapps.util.RuntimeDaoManager;
import cn.myapps.util.StringUtil;

public class DataMapTemplateProcessBean extends
		AbstractRunTimeProcessBean<DataMapTemplate> implements
		DataMapTemplateProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9124807078853090241L;

	public DataMapTemplateProcessBean(String applicationId) {
		super(applicationId);
	}

	protected IRuntimeDAO getDAO() throws Exception {
		RuntimeDaoManager runtimeDao = new RuntimeDaoManager();
		DataMapTemplateDAO dao = (DataMapTemplateDAO) runtimeDao
				.getDataMapTemplateDAO(getConnection(), getApplicationId());
		return dao;
	}

	public void doCreateOrUpdate(ValueObject vo) throws Exception {
		DataMapTemplate template = (DataMapTemplate)vo;
		String id = template.getDatamapCfgId()+"_"+template.getCuleField();
		if(!StringUtil.isBlank(template.getCuleField2())){
			id += "_"+template.getCuleField2();
		}
		template.setId(id);
		
		if(doView(template.getId()) != null){
			doUpdate(template);
		}else{
			doCreate(template);
		}
		
	}

}
