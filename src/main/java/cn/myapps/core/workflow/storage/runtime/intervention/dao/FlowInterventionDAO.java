package cn.myapps.core.workflow.storage.runtime.intervention.dao;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.storage.runtime.intervention.ejb.FlowInterventionVO;

/**
 * @author Happy
 *
 */
public interface FlowInterventionDAO extends IRuntimeDAO {
	
	public DataPackage<FlowInterventionVO> queryByFilter(ParamsTable params,WebUser user) throws Exception;
	
	/**
	 * 拼装时间格式  用于处理不同数据库
	 * @param time
	 * 		默认时间格式是  yyyy-mm-dd
	 * @return
	 * @throws Exception
	 */
	public String assemblyTime(String time) throws Exception;
	

}
