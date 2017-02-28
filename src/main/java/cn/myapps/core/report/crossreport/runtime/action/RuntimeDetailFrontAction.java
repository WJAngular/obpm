package cn.myapps.core.report.crossreport.runtime.action;

import cn.myapps.core.report.crossreport.definition.ejb.CrossReportProcess;
import cn.myapps.core.report.crossreport.definition.ejb.CrossReportVO;
import cn.myapps.util.ProcessFactory;


public class RuntimeDetailFrontAction extends RuntimeDetailAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public RuntimeDetailFrontAction() throws ClassNotFoundException {
		super(ProcessFactory.createProcess(CrossReportProcess.class), new CrossReportVO());
	}
}
