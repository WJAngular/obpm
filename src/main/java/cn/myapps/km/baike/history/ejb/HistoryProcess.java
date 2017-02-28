package cn.myapps.km.baike.history.ejb;


import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.base.ejb.NRunTimeProcess;
import cn.myapps.km.org.ejb.NUser;

/**
 * 
 * @author Allen
 *	entry 的process层的接口
 */
public interface HistoryProcess extends  NRunTimeProcess<History>{
	
	/**
	 * 当前用户创建词条
	 * @param vo
	 * @param user
	 * @throws Exception
	 */
	public void doCreate(NObject vo, NUser user) throws Exception ;
	
	/**
	 * 通过entryId查找浏览记录
	 * @param entryId
	 * @return
	 * @throws Exception
	 */
	public History queryByEntryId(String entryId,String userId) throws Exception;
	
	/**
	 * 通过userId查找记录
	 */
	public DataPackage<History> queryHistoryByUserId(int page, int lines,String userId) throws Exception;		

}
