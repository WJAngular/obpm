package cn.myapps.km.baike.history.dao;

import cn.myapps.km.baike.entry.ejb.Entry;
import cn.myapps.km.baike.history.ejb.History;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.dao.NRuntimeDAO;
/**
 * @author Able
 * ReasonDao 抽象接口
 * 
 */
public interface HistoryDao extends NRuntimeDAO{

	/**
	 * 通过entryId查找浏览记录
	 * @param entryId
	 * @return
	 * @throws Exception
	 */
	public History queryByEntryId(String entryId,String userId) throws Exception;
	/**
	 * 通过userId查找所有浏览记录
	 * @param page
	 * @param lines
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public DataPackage<History> queryHistoryByUserId(int page, int lines,String userId) throws Exception;

	
} 
