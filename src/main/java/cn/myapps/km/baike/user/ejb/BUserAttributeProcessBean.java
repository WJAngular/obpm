package cn.myapps.km.baike.user.ejb;

import java.util.Collection;
import java.util.Map;

import cn.myapps.km.baike.entry.dao.EntryDao;
import cn.myapps.km.baike.user.dao.BUserAttributeDAO;
import cn.myapps.km.base.dao.BDaoManager;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.base.ejb.AbstractRunTimeProcessBean;
import cn.myapps.km.org.ejb.NUser;

/**
 * 
 * @author dragon
 * 掉用DAO层，实现业务
 */
public class BUserAttributeProcessBean extends  AbstractRunTimeProcessBean<BUserAttribute> implements BUserAttributeProcess{
	private static final long serialVersionUID = -979509233585133828L;
	
	/**
	 * 根据积分排序分页
	 */
	public DataPackage<BUser> getScoreboard(int page, int lines)
			throws Exception {
		return ((BUserAttributeDAO)getDAO()).getScoreboard(page, lines);
	}
	
	
	/**
	 * 通过用户ID查找用户属性
	 * @param userId
	 * 				用户ID
	 * @return
	 * @throws Exception
	 */
	public BUserAttribute findByUserId(String userId) throws Exception {
		return ((BUserAttributeDAO)getDAO()).findByUserId(userId);
	}
	
	/**
	 * 通过用户ID查找用户
	 * @param userId
	 * 				用户ID
	 * @return
	 * @throws Exception
	 */
	public BUser findBUserById(String userId) throws Exception {
		return ((BUserAttributeDAO)getDAO()).findBUserById(userId);
	}
	/**
	 * 用户创建的词条版本数
	 */
	public long doCountCreateEntryContent(String author) throws Exception{
		return ((BUserAttributeDAO)getDAO()).countCreateEntryContent(author);
	}
	
	/**
	 * 用户创建的词条数
	 */
	public long doCountCreateEntry(String author) throws Exception{
		return ((BUserAttributeDAO)getDAO()).countCreateEntry(author);
	}
	/**
	 * 用户创建的通过的词条
	 */
	public long doCountPassedEntry(String author) throws Exception{
		return ((BUserAttributeDAO)getDAO()).countPassedEntry(author);
	}

	
	@Override
	protected NRuntimeDAO getDAO() throws Exception {
		NRuntimeDAO dao =  BDaoManager.getBUserAttributeDAO(getConnection());
		return dao;
	}
	
	/**
	 * 增加用户积分
	 */
	public void addPoint(NUser nUser, int point) throws Exception {
		((BUserAttributeDAO)getDAO()).addPoint(nUser.getId(), point);
	}
	public Collection<BUser> doFindContributer(String entryId) throws Exception{
		return  ((BUserAttributeDAO)getDAO()).doFindContributer(entryId);
	}
}
