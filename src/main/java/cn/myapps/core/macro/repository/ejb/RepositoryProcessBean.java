package cn.myapps.core.macro.repository.ejb;

import java.util.Collection;

import org.apache.commons.beanutils.PropertyUtils;

import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.dynaform.form.action.ImpropriateException;
import cn.myapps.core.macro.repository.dao.RepositoryDAO;

public class RepositoryProcessBean extends AbstractDesignTimeProcessBean<RepositoryVO> implements
		RepositoryProcess {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8429701819151113696L;

	/**
	 * @SuppressWarnings getDefaultDAO得到的process不定
	 */
	@SuppressWarnings("unchecked")
	protected IDesignTimeDAO<RepositoryVO> getDAO() throws Exception {
		IDesignTimeDAO<RepositoryVO> dao = (IDesignTimeDAO<RepositoryVO>) DAOFactory.getDefaultDAO(RepositoryVO.class.getName());
		return dao;
	}
	
	public RepositoryVO getRepositoryByName(String name,String application) throws Exception
	{
		return ((RepositoryDAO)getDAO()).getRepositoryByName(name, application);
	
}
	public void doUpdate(ValueObject vo) throws Exception {
		try {
			PersistenceUtils.beginTransaction();
			ValueObject po = getDAO().find(vo.getId());
			if(po != null && ((RepositoryVO)vo).getVersion()!=((RepositoryVO)po).getVersion())
				throw new ImpropriateException("{*[core.util.cannotsave]*}");
			((RepositoryVO)vo).setVersion(((RepositoryVO)vo).getVersion()+1);
			if (po != null) {
				PropertyUtils.copyProperties(po, vo);
				getDAO().update(po);
			} else {
				getDAO().update(vo);
			}

			PersistenceUtils.commitTransaction();
		}catch(ImpropriateException e)
		{
			PersistenceUtils.rollbackTransaction();
			throw e;
		}
		catch (Exception e) {
			e.printStackTrace();
			PersistenceUtils.rollbackTransaction();
		}
	}

	public boolean isMacroNameExist(String id, String name, String application)
			throws Exception {
		return ((RepositoryDAO)getDAO()).isMacroNameExist(id, name, application);
	}

	public Collection<RepositoryVO> doQueryByApplication(String applicationId)
			throws Exception {
		return ((RepositoryDAO)getDAO()).queryByApplication(applicationId);
	}
}
