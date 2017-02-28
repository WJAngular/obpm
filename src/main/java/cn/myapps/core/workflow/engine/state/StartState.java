package cn.myapps.core.workflow.engine.state;

import cn.myapps.core.workflow.FlowState;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.core.workflow.engine.State;

public class StartState extends AbstractState implements State {

	public StartState(Node node) {
		super(node);
	}

	public int toInt() {
		return FlowState.START;
	}
}
