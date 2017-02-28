package cn.myapps.core.privilege.operation.action;

import java.util.Iterator;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.BaseAction;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.privilege.operation.ejb.OperationProcess;
import cn.myapps.core.privilege.operation.ejb.OperationVO;
import cn.myapps.init.InitOperationInfo;
import cn.myapps.util.ProcessFactory;

/**
 * @SuppressWarnings 不支持泛型
 * @author Administrator
 *
 */
@SuppressWarnings("unchecked")
public class OperationAction extends BaseAction {

	public OperationAction() throws Exception {
		super(ProcessFactory.createProcess(OperationProcess.class),
				new OperationVO());
	}

	private static final long serialVersionUID = 1L;

	/**
	 * 删除全部操作
	 */
	public String doDelete() {
		try{
		this.getParams().removeParameter("_pagelines");
		DataPackage<OperationVO> datas = ((OperationProcess)process).doQuery(this.getParams());
		if(datas.rowCount>0){
			for (Iterator<OperationVO> iter = datas.datas.iterator(); iter.hasNext();) {
				OperationVO operationVO = iter.next();
				((OperationProcess)process).doRemove(operationVO.getId());
			}
		}
		addActionMessage("{*[delete.successful]*}");
		return  SUCCESS;
		}catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}
	
	/**
	 * 撤销删除
	 * @return
	 */
	public String doUndo() {
		try{
			InitOperationInfo initOperation = new InitOperationInfo();
			initOperation.run();
			addActionMessage("{*[Undo]*}{*[delete.successful]*}");
			return  SUCCESS;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}
}
