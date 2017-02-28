package cn.myapps.core.workflow.engine;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRT;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRT;

public interface State {
	public NodeRT process(ParamsTable params, NodeRT origNodeRT, FlowStateRT instance, WebUser user,
			String flowOption) throws Exception;

	public int toInt();

	public Collection<String> getPrincipalIdList(Document doc, ParamsTable params, String domainId, String applicationid,BaseUser auditor)
			throws Exception;
}
