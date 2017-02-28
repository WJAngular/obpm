package cn.myapps.core.datamap.runtime.action;

import javax.servlet.http.HttpServletRequest;

import cn.myapps.core.datamap.runtime.ejb.DataMapTemplate;
import cn.myapps.core.datamap.runtime.ejb.DataMapTemplateProcess;
import cn.myapps.util.ProcessFactory;

public class DataMapTemplateUtil {
	
	public String getDataMapContent(String datamapCfgId,String culeField,String applicationId, HttpServletRequest request) throws Exception {
		DataMapTemplateProcess process = (DataMapTemplateProcess) ProcessFactory.createRuntimeProcess(DataMapTemplateProcess.class, applicationId);
		DataMapTemplate template = (DataMapTemplate)process.doView(datamapCfgId+"_"+culeField);
		if(template !=null){
			return template.getContent();
		}
		return null;
	}

}
