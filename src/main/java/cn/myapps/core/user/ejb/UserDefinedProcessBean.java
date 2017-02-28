package cn.myapps.core.user.ejb;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.dao.UserDefinedDAO;
import cn.myapps.util.StringUtil;
import cn.myapps.util.sequence.Sequence;

public class UserDefinedProcessBean extends AbstractDesignTimeProcessBean<UserDefined> implements UserDefinedProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1210735331986765651L;

	@Override
	protected IDesignTimeDAO<UserDefined> getDAO() throws Exception {
		return (UserDefinedDAO) DAOFactory.getDefaultDAO(UserDefined.class.getName());
	}


	public void doUserDefinedUpdate(ValueObject vo) throws Exception {
		
	}

	public Collection<UserDefined> doViewByApplication(String applicationId) throws Exception {
		return ((UserDefinedDAO) getDAO()).findByApplication(applicationId);
	}

	public int doViewCountByName(String name, String applicationid)
			throws Exception {
		return ((UserDefinedDAO) getDAO()).queryCountByName(name, applicationid);  
	}

	
	public DataPackage<UserDefined> getDatapackage(String hql, ParamsTable params) throws Exception {
		String _pageLines = params.getParameterAsString("_pagelines");
		String _currPage = params.getParameterAsString("_currpage");
		int pageLines = 10;
		int currPage = 1;
		if(!StringUtil.isBlank(_pageLines)){
			try{
				pageLines = Integer.parseInt(_pageLines);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		if(!StringUtil.isBlank(_currPage)){
			try{
				currPage = Integer.parseInt(_currPage);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		return ((UserDefinedDAO) getDAO()).getDatapackage(hql, params, currPage, pageLines);
	}
	
	public void doCreate(ValueObject vo) throws Exception {
//		UserDefined tmp = null;
//		tmp = ((UserDefinedDAO) getDAO()).login(((UserDefined) vo).getName());
//		if (tmp != null) {
////			throw new ExistNameException("{*[core.form.exist]*}");
//		}
		try {
			PersistenceUtils.beginTransaction();
			if (vo.getId() == null || vo.getId().trim().length() == 0) {
				vo.setId(Sequence.getSequence());
			}

			if (vo.getSortId() == null || vo.getSortId().trim().length() == 0) {
				vo.setSortId(Sequence.getTimeSequence());
			}

			getDAO().create(vo);
			PersistenceUtils.commitTransaction();
		} catch (Exception e) {
			PersistenceUtils.rollbackTransaction();
			e.printStackTrace();
			throw e;
		}
	}
	
	public UserDefined findMyCustomUserDefined(WebUser user) throws Exception{
		
		return null;
	}
	
	/**
	 * 获取用户自定义的首页
	 * @param user
	 * 		当前登录用户
	 * @param applicationId
	 * 		软件id
	 * @return
	 * @throws Exception
	 */
	public UserDefined doFindMyCustomUserDefined(WebUser user) throws Exception{
		return ((UserDefinedDAO) getDAO()).findMyCustomUserDefined(user);
	}
	
	/**
	 * 根据传入的软件id获取软件下的所有已发布的首页集合
	 * @param applicationId
	 * 		软件id
	 * @return
	 * @throws Exception
	 */
	public Collection<UserDefined> doQueryPublishedUserDefinedsByApplication(String applicationId) throws Exception{
		return ((UserDefinedDAO) getDAO()).queryPublishedUserDefinedsByApplication(applicationId);
	}

}
