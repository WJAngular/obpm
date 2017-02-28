package cn.myapps.core.logger.ejb;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.logger.dao.LogDAO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.StringUtil;

public class LogProcessBean extends AbstractDesignTimeProcessBean<LogVO> implements LogProcess {

	private static final long serialVersionUID = 3998082574597686749L;

	@Override
	protected IDesignTimeDAO<LogVO> getDAO() throws Exception {
		return (LogDAO) DAOFactory.getDefaultDAO(LogVO.class.getName());
	}
	
	@Override
	public DataPackage<LogVO> doQuery(ParamsTable params, WebUser user)
			throws Exception {
		return ((LogDAO)getDAO()).queryLog(params, user);
		//return getDAO().query(params, user);
	}

	public DataPackage<LogVO> getLogsByDomain(ParamsTable params, WebUser user)
			throws Exception {
		String domain = params.getParameterAsString("domain");
		if (StringUtil.isBlank(domain)) {
			throw new OBPMValidateException("domainid is null!");
		}
		return ((LogDAO)getDAO()).queryLog(params, user, domain);
	}
	
	public void deleteLogsByUser(String userid) throws Exception{
		((LogDAO)getDAO()).deleteLogsByUser(userid);
	}
	
}
