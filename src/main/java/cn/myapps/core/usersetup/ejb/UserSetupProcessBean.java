package cn.myapps.core.usersetup.ejb;

import java.util.HashMap;
import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.usersetup.dao.UserSetupDAO;

public class UserSetupProcessBean extends AbstractDesignTimeProcessBean<UserSetupVO> implements UserSetupProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2326039651240289683L;
	public final static HashMap<String, WebUser> _cache = new HashMap<String, WebUser>();


	public UserSetupVO getUserSetupByUserId(String uId) throws Exception {
		return (UserSetupVO) this.getDAO().find(uId);
	}
	
	//@SuppressWarnings("unchecked")
	protected IDesignTimeDAO<UserSetupVO> getDAO() throws Exception {
		return (UserSetupDAO) DAOFactory.getDefaultDAO(UserSetupVO.class.getName());
	}
}
