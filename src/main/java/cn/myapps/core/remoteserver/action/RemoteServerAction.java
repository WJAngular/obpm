package cn.myapps.core.remoteserver.action;

import cn.myapps.base.action.BaseAction;
import cn.myapps.core.remoteserver.ejb.RemoteServerProcess;
import cn.myapps.core.remoteserver.ejb.RemoteServerVO;
import cn.myapps.util.ProcessFactory;

public class RemoteServerAction extends BaseAction<RemoteServerVO> {

	/**
	 * @SuppressWarnings 工厂方法不支持泛型
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public RemoteServerAction() throws ClassNotFoundException {
		super(ProcessFactory.createProcess(RemoteServerProcess.class), new RemoteServerVO());
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
