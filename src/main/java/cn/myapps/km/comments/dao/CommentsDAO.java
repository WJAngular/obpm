package cn.myapps.km.comments.dao;

import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.comments.ejb.Comments;

public interface CommentsDAO extends NRuntimeDAO {
	
	/**
	 * 好评数
	 *
	 */
	public long countByGood(String fileID) throws Exception ;
	
	/**
	 * 差评数
	 *
	 */
	public long countByBad(String fileID) throws Exception ;
	
	public long countBy(String fileId,String userId) throws Exception;
	
	public Comments find(String id) throws Exception;
	
	public void remove(String id) throws Exception;
	
	public DataPackage<Comments> query() throws Exception;
}
