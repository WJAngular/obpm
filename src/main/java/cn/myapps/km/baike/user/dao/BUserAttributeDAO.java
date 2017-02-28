package cn.myapps.km.baike.user.dao;
import java.util.Collection;
import cn.myapps.km.baike.user.ejb.BUser;
import cn.myapps.km.baike.user.ejb.BUserAttribute;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.dao.NRuntimeDAO;
/**
 * 
 * @author dragon
 * DAO层接口
 *
 */
public interface BUserAttributeDAO extends NRuntimeDAO{
	/**
	 * 获取积分排行榜
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public DataPackage<BUser> getScoreboard(int page, int lines) throws Exception;
	
	/**
	 * 根据用户ID查找用户属性
	 * @param userId
	 * 				用户ID
	 * @return
	 * @throws Exception
	 */
	public BUserAttribute findByUserId(String userId) throws Exception;
	
	
	/**
	 * 根据用户ID查找用户属性
	 * @param userId
	 * 				用户ID
	 * @return
	 * @throws Exception
	 */
	public BUser findBUserById(String userId) throws Exception;
	
	/**
	 * 获取用户创建词条版本数
	 * @param author
	 * @return
	 * @throws Exctption
	 */
	public long countCreateEntryContent(String author) throws Exception;
	
	/**
	 * 获取用户创建词条数
	 * @param author
	 * @return
	 * @throws Exctption
	 */
	public long countCreateEntry(String author) throws Exception;
	
	/**
	 * 获取用户创建的通过的词条数
	 * @param author
	 * @return
	 * @throws Exctption
	 */
	public long countPassedEntry(String author) throws Exception;
	
	/**
	 * 增加用户积分
	 * @param pk
	 * 			用户ID
	 * @param point
	 * 			增加积分
	 * @throws Expception
	 */
	public void addPoint(String userId, int point) throws Exception;
	
	/**
	 * 查找贡献者
	 * @throws Exception
	 */
	public  Collection<BUser> doFindContributer(String entryId) throws Exception;
	
	/**
	 * 查找所有用户
	 * @throws Exception
	 */
	public  Collection<BUserAttribute> doFindAllUser() throws Exception;
}
