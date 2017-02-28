package cn.myapps.core.workflow.analyzer;

import java.util.Collection;
import java.util.HashMap;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.base.ejb.AbstractRunTimeProcessBean;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.RuntimeDaoManager;

public class AnalyzerProcessBean extends
		AbstractRunTimeProcessBean<FlowAnalyzerVO> {

	public AnalyzerProcessBean(String applicationId) {
		super(applicationId);
	}

	private static final long serialVersionUID = 1110835769966966930L;

	public DataPackage<FlowAnalyzerVO> doQuery(ParamsTable params, WebUser user)
			throws Exception {

		throw new Exception("Method is not implemented.");
	}

	// ////////////////////////////////我是华丽的分割线////////////////////////////////////////

	/**
	 * 流程实例占比统计OK
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Collection<FlowAnalyzerVO> doAnalyzerFlowAccounting(
			ParamsTable params, String dateRangeType, String showMode,
			WebUser user) throws Exception {
		AbstractAnalyzerDAO dao = (AbstractAnalyzerDAO) getDAO();
		Collection<FlowAnalyzerVO> datas = dao.analyzerFlowAccounting(params,
				dateRangeType, showMode, user);

		return datas;
	}

	/**
	 * * 处理人耗时统计，TOP-X
	 * 
	 * @param params
	 * @param top
	 * @return
	 * @throws Exception
	 */
	public Collection<FlowAnalyzerVO> doAnalyzerActorTimeConsumingTopX(
			ParamsTable params, String dateRangeType, int top, String showMode,
			WebUser user) throws Exception {
		AbstractAnalyzerDAO dao = (AbstractAnalyzerDAO) getDAO();
		Collection<FlowAnalyzerVO> datas = dao.analyzerActorTimeConsumingTopX(
				params, dateRangeType, top, showMode, user);

		return datas;
	}

	/**
	 * 流程耗时占比
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Collection<FlowAnalyzerVO> doAnalyzerFlowTimeConsumingAccounting(
			ParamsTable params, String dateRangeType, String showMode,
			WebUser user) throws Exception {
		AbstractAnalyzerDAO dao = (AbstractAnalyzerDAO) getDAO();
		Collection<FlowAnalyzerVO> datas = dao
				.analyzerFlowTimeConsumingAccounting(params, dateRangeType,
						showMode, user);

		return datas;
	}

	/**
	 * * 流程&节点耗时统计
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Collection<FlowAnalyzerVO> doAnalyzerFlowAndNodeTimeConsuming(
			ParamsTable params, String dateRangeType, String showMode,
			WebUser user) throws Exception {
		AbstractAnalyzerDAO dao = (AbstractAnalyzerDAO) getDAO();
		Collection<FlowAnalyzerVO> datas = dao
				.analyzerFlowAndNodeTimeConsuming(params, dateRangeType,
						showMode, user);

		return datas;

	}

	protected IRuntimeDAO getDAO() throws Exception {
		return new RuntimeDaoManager().getFlowMonitorDAO(getConnection(),
				getApplicationId());
	}
	
	public Collection<HashMap> getConsuming(ParamsTable params,String dateRangeType,String showMode,
			WebUser user) throws Exception{
		AbstractAnalyzerDAO dao = (AbstractAnalyzerDAO) getDAO();
		Collection<HashMap> datas = (Collection<HashMap>) dao.queryConsuming(params, dateRangeType, showMode, user);

		return datas;
	}
	
	public Collection<HashMap> getNode(ParamsTable params,String dateRangeType,String showMode,
			WebUser user) throws Exception{
		AbstractAnalyzerDAO dao = (AbstractAnalyzerDAO) getDAO();
		Collection<HashMap> datas = (Collection<HashMap>) dao.queryNode(params, dateRangeType, showMode, user);

		return datas;
	}
	
	public Collection<HashMap> getNames(ParamsTable params,String showMode,
			WebUser user) throws Exception{
		AbstractAnalyzerDAO dao = (AbstractAnalyzerDAO) getDAO();
		Collection<HashMap> datas = (Collection<HashMap>) dao.queryNames(params, showMode, user);

		return datas;
	}
	public Collection<HashMap> getDetailed(ParamsTable params,String showMode,
			WebUser user) throws Exception{
		AbstractAnalyzerDAO dao = (AbstractAnalyzerDAO) getDAO();
		Collection<HashMap> datas = (Collection<HashMap>) dao.queryDetailed(params, showMode, user);

		return datas;
	}
	
}
