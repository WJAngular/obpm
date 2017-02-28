package cn.myapps.core.workflow.analyzer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import cn.myapps.base.action.AbstractRunTimeAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.ejb.IRunTimeProcess;
import cn.myapps.core.user.action.WebUser;

public class AnalyzerAction extends AbstractRunTimeAction<FlowAnalyzerVO> {

	private static final long serialVersionUID = 4326321048256096919L;
	protected static final String ACTION_RESULT_KEY = "status";
	protected static final int ACTION_RESULT_VALUE_SUCCESS = 1;
	protected static final int ACTION_RESULT_VALUE_FAULT = 0;
	
	protected static final String ACTION_MESSAGE_KEY = "message";
	protected static final String ACTION_DATA_KEY = "data";

	private String dateRange;

	private String showMode;
	
	private Map<String, Object> dataMap = new HashMap<String, Object>();
	
	public String getShowMode() {
		return showMode;
	}

	public void setShowMode(String showMode) {
		this.showMode = showMode;
	}

	public String getDateRange() {
		return dateRange;
	}

	public void setDateRange(String dateRange) {
		this.dateRange = dateRange;
	}

	@Override
	public IRunTimeProcess<FlowAnalyzerVO> getProcess() {
		return new AnalyzerProcessBean(application);
	}

	/**
	 * * 处理人耗时统计，TOP-X
	 * 
	 * @return
	 * @throws Exception
	 */
	public String doAnalyzerActorTimeConsumingTopX() throws Exception {
		ParamsTable params = getParams();
		WebUser user = this.getUser();
		Collection<FlowAnalyzerVO> datas = ((AnalyzerProcessBean) getProcess())
				.doAnalyzerActorTimeConsumingTopX(params, this.getDateRange(),
						10, this.getShowMode(), user);

		getContext().put("DATA", datas);
		return SUCCESS;
	}

	/**
	 * * 流程实例占比统计OK
	 * 
	 * @return
	 * @throws Exception
	 */
	public String doAnalyzerFlowAccounting() throws Exception {
		ParamsTable params = getParams();
		WebUser user = this.getUser();
		Collection<FlowAnalyzerVO> datas = ((AnalyzerProcessBean) getProcess())
				.doAnalyzerFlowAccounting(params, this.getDateRange(),
						this.getShowMode(), user);

		getContext().put("DATA", datas);
		return SUCCESS;
	}

	/**
	 * 流程耗时占比
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String doAnalyzerFlowTimeConsumingAccounting() throws Exception {
		ParamsTable params = getParams();
		WebUser user = this.getUser();
		Collection<FlowAnalyzerVO> datas = ((AnalyzerProcessBean) getProcess())
				.doAnalyzerFlowTimeConsumingAccounting(params,
						this.getDateRange(), this.getShowMode(), user);

		getContext().put("DATA", datas);
		return SUCCESS;
	}

	/**
	 * 流程&节点耗时统计
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String doAnalyzerFlowAndNodeTimeConsuming() throws Exception {
		ParamsTable params = getParams();
		WebUser user = this.getUser();
		Collection<FlowAnalyzerVO> datas = ((AnalyzerProcessBean) getProcess())
				.doAnalyzerFlowAndNodeTimeConsuming(params,
						this.getDateRange(), this.getShowMode(), user);

		getContext().put("DATA", datas);
		return SUCCESS;
	}
	
	public String doConsuming() throws Exception {
		ParamsTable params = getParams();
		WebUser user = this.getUser();
		
		Collection<HashMap> datas;
		try {
			datas = (Collection<HashMap>) ((AnalyzerProcessBean) getProcess())
					.getConsuming(params, this.getDateRange(), this.getShowMode(), user);
			addActionResult(true, "", datas);
		} catch (Exception e) {
			e.printStackTrace();
			addActionResult(false, e.getLocalizedMessage(), null);
		}
		return SUCCESS;
	}
	
	public String doNode() throws Exception {
		ParamsTable params = getParams();
		WebUser user = this.getUser();
		
		Collection<HashMap> datas;
		try {
			datas = (Collection<HashMap>) ((AnalyzerProcessBean) getProcess())
					.getNode(params, this.getDateRange(), this.getShowMode(), user);
			addActionResult(true, "", datas);
		} catch (Exception e) {
			e.printStackTrace();
			addActionResult(false, e.getLocalizedMessage(), null);
		}
		return SUCCESS;
	}
	
	public String doNames() throws Exception {
		ParamsTable params = getParams();
		WebUser user = this.getUser();
		
		Collection<HashMap> datas;
		try {
			datas = (Collection<HashMap>) ((AnalyzerProcessBean) getProcess())
					.getNames(params, this.getShowMode(), user);
			addActionResult(true, "", datas);
		} catch (Exception e) {
			e.printStackTrace();
			addActionResult(false, e.getLocalizedMessage(), null);
		}
		return SUCCESS;
	}
	
	public String doDetailed() throws Exception {
		ParamsTable params = getParams();
		WebUser user = this.getUser();
		
		Collection<HashMap> datas;
		try {
			datas = (Collection<HashMap>) ((AnalyzerProcessBean) getProcess())
					.getDetailed(params, this.getShowMode(), user);
			addActionResult(true, "", datas);
		} catch (Exception e) {
			e.printStackTrace();
			addActionResult(false, e.getLocalizedMessage(), null);
		}
		return SUCCESS;
	}
	
	/**
     * Struts2序列化指定属性时，必须有该属性的getter方法
     * @return
     */
    public Map<String, Object> getDataMap() {
        return dataMap;
    }
    
    /**
     * 添加Action处理结果
     * @param isSuccess
     * 		是否成功处理
     * @param message
     * 		返回消息
     * @param data
     * 		返回数据
     */
    public void addActionResult(boolean isSuccess,String message,Object data){
    	dataMap.put(ACTION_RESULT_KEY, isSuccess?ACTION_RESULT_VALUE_SUCCESS : ACTION_RESULT_VALUE_FAULT );
    	dataMap.put(ACTION_MESSAGE_KEY, message);
    	if(data != null){
    		dataMap.put(ACTION_DATA_KEY, data);
    	}
    }

}
