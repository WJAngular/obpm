package cn.myapps.km.baike.user.ejb;
import java.util.Collection;

import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NRunTimeProcess;
import cn.myapps.km.org.ejb.NUser;

/**
 * 
 * @author dragon
 *调用DAO层的接口
 */
public interface BUserAttributeProcess extends NRunTimeProcess<BUserAttribute>{
	/**
	 * 根据积分排序分页
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public DataPackage<BUser> getScoreboard(int page, int lines) throws Exception;
	
	
	/**
	 * 通过用户ID查找用户属性
	 * @param userId
	 * 				用户ID
	 * @return
	 * @throws Exception
	 */
	public BUserAttribute findByUserId(String userId) throws Exception;
	
	/**
	 * 通过用户id查找经验百科用户
	 * @param UserId
	 * 				用户ID
	 * @return
	 * @throws Exception
	 */
	public BUser findBUserById(String userId) throws Exception;
	
	/**
	 * 获取用户创建词条版本数
	 * @param author
	 * @return
	 * @throws Exception
	 */
	public long doCountCreateEntryContent(String author) throws Exception;
	

	/**
	 * 获取用户创建词条数
	 * @param author
	 * @return
	 * @throws Exception
	 */
	public long doCountCreateEntry(String author) throws Exception;
	/**
	 * 获取用户创建词条数
	 * @param author
	 * @return
	 * @throws Exception
	 */
	public long doCountPassedEntry(String author) throws Exception;
	
	/**
	 * 增加用户积分
	 * @param pk
	 * 			用户ID
	 * @param point
	 * 			增加积分
	 * @throws Expception
	 */
	public void addPoint(NUser nUser, int point) throws Exception;
	public Collection<BUser> doFindContributer(String entryId) throws Exception;
}
