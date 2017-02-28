package cn.myapps.core.workflow.storage.runtime.intervention.ejb;

import cn.myapps.base.ejb.IRunTimeProcess;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.user.action.WebUser;

/**
 * @author Happy
 *
 */
public interface FlowInterventionProcess extends IRunTimeProcess<FlowInterventionVO>{

	/**
	 * 根据文档创建记录
	 * @param doc
	 */
	public void doCreateByDocument(Document doc,WebUser user) throws Exception;
	
	public void doUpdateByDocument(Document doc,WebUser user) throws Exception;
	
}
