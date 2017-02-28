package cn.myapps.km.log.action;

import cn.myapps.km.base.action.AbstractRunTimeAction;
import cn.myapps.km.base.action.ParamsTable;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NRunTimeProcess;
import cn.myapps.km.log.ejb.Logs;
import cn.myapps.km.log.ejb.LogsProcess;
import cn.myapps.km.log.ejb.LogsProcessBean;
import cn.myapps.km.org.ejb.NUser;

public class LogsAction extends AbstractRunTimeAction<Logs>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5809202949574916234L;
	
	private String type;
	
	
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public LogsAction(){
		super();
		content = new Logs();
	}

	@Override
	public NRunTimeProcess<Logs> getProcess() {
		// TODO Auto-generated method stub
		return new LogsProcessBean();
	}
	
	public String doSave() throws Exception{
		LogsProcess logsProcess = (LogsProcess)getProcess();
		Logs logs=(Logs)(this.getContent());
		NUser user = getUser();
		logs.setUserId(user.getId());
		logs.setUserName(user.getName());
		//logs.setOperationType(type);
		logsProcess.doCreate(logs);
		return SUCCESS;
	}
	
	public String doQuery() throws Exception{
		LogsProcess logsProcess = (LogsProcess) getProcess();
		ParamsTable params = getParams();
		String  operationtype = params.getParameterAsString("operationtype");
		String filename = params.getParameterAsString("fileName");
	
		NUser user = this.getUser();
		int lines = 20;
		int currPage = params.getParameterAsInteger("_currpage") == null ? 1:params.getParameterAsInteger("_currpage");
		try {
			if(operationtype==""&&filename!=""){
				DataPackage datas = logsProcess. viewByFile(currPage,lines,user.getId(),operationtype,filename);
				setDatas(datas);
				return SUCCESS;
			}
			
			if(operationtype==null||operationtype==""){
				DataPackage datas = logsProcess.doView(currPage, lines, user.getId(),operationtype,filename);
				setDatas(datas);
				return SUCCESS;
				
			}
			if(filename==null||filename==""){
				DataPackage datas = logsProcess. doQuery(currPage,lines,user.getId(),operationtype,filename);
				setDatas(datas);
			}else{
				DataPackage datas = logsProcess. queryByFile(currPage,lines,user.getId(),operationtype,filename);
				setDatas(datas);
			}
			
			
		} catch (Exception e) {
					e.printStackTrace();
				}
		return SUCCESS;
	}
	
	
	public String doManagerQuery() throws Exception{
		LogsProcess logsProcess = (LogsProcess) getProcess();
		ParamsTable params = getParams();
		int lines = 20;
		int currPage = params.getParameterAsInteger("_currpage") == null ? 1:params.getParameterAsInteger("_currpage");
		try {
			DataPackage datas = logsProcess. managerQuery(currPage,lines,params);
			setDatas(datas);
		} catch (Exception e) {
					e.printStackTrace();
				}
		return SUCCESS;
	}
	
}
