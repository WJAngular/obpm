package cn.myapps.core.superuser.dao;

import java.util.Collection;

import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.superuser.ejb.SuperUserVO;

public interface SuperUserDAO extends IDesignTimeDAO<SuperUserVO> {

	public abstract SuperUserVO login(String loginno) throws Exception;

	public boolean isEmpty() throws Exception;

	public abstract Collection<SuperUserVO> getDatasByDomain(String domain) throws Exception;

	public abstract Collection<SuperUserVO> getDatasByType(int userType) throws Exception;

	public abstract Collection<SuperUserVO> queryHasMail() throws Exception;

	/**
	 * 根据loginno获取管理员
	 * 
	 * @return 用户的Email集合
	 * 
	 */
	public SuperUserVO findByLoginno(String loginno) throws Exception;
}
