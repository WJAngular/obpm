package cn.myapps.core.datamap.runtime.ejb;

import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.IRunTimeProcess;

public interface DataMapTemplateProcess extends
		IRunTimeProcess<DataMapTemplate> {
	
	public void doCreateOrUpdate(ValueObject vo) throws Exception;

}
