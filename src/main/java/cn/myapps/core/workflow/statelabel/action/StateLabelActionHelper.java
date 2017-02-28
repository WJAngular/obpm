package cn.myapps.core.workflow.statelabel.action;

import java.util.Collection;

import cn.myapps.core.workflow.statelabel.ejb.StateLabel;
import cn.myapps.core.workflow.statelabel.ejb.StateLabelProcess;
import cn.myapps.util.ProcessFactory;

public class StateLabelActionHelper {
	public Collection<StateLabel> getStateList(String application)
			throws Exception {
		StateLabelProcess cp = (StateLabelProcess) ProcessFactory
				.createProcess((StateLabelProcess.class));
		Collection<StateLabel> rtn = cp.doQueryState(application);
		return rtn;
	}
}
