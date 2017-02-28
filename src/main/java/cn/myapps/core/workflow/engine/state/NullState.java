package cn.myapps.core.workflow.engine.state;

import cn.myapps.core.workflow.element.Node;
import cn.myapps.core.workflow.engine.State;

public class NullState extends AbstractState implements State {

	public NullState(Node node) {
		super(node);
	}

	public int toInt() {
		return Integer.MAX_VALUE;
	}

}
