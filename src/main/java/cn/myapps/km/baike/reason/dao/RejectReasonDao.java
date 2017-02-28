package cn.myapps.km.baike.reason.dao;

import java.util.Collection;

import cn.myapps.km.baike.reason.ejb.RejectReason;
import cn.myapps.km.base.dao.NRuntimeDAO;
/**
 * @author Able
 * ReasonDao 抽象接口
 * 
 */
public interface RejectReasonDao extends NRuntimeDAO{

	/**
	 * 查询年通过了的词条
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public Collection<RejectReason> queryAllReason(String contentId) throws Exception;

} 
