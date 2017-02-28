package cn.myapps.core.datamap.definition.ejb;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.ejb.IDesignTimeProcess;
import cn.myapps.core.user.action.WebUser;

public interface DataMapCfgProcess extends IDesignTimeProcess<DataMapCfgVO> {
	
	/**
	 * 获取索引列
	 * @param datamapCfgId
	 * @return
	 * @throws Exception
	 */
	public String getClueFields(String datamapCfgId,ParamsTable params,WebUser user,String applicationId) throws Exception;
	
	/**
	 * 获取摘要列
	 * @param datamapCfgId
	 * @param clueField
	 * @param clueField2
	 * @return
	 * @throws Exception
	 */
	public String getSummaryFields(String datamapCfgId,String clueValue,String clueField2,String applicationId,String domainId) throws Exception;

}
