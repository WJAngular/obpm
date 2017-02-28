package cn.myapps.core.style.repository.ejb;

import java.util.Collection;

import org.apache.commons.beanutils.PropertyUtils;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.dynaform.form.action.ImpropriateException;
import cn.myapps.core.style.repository.dao.StyleRepositoryDAO;

public class StyleRepositoryProcessBean extends AbstractDesignTimeProcessBean<StyleRepositoryVO> implements
		StyleRepositoryProcess {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5090348941297364352L;

	/**
	 * @SuppressWarnings 不支持泛型getDefaultDAO返回的process不确定
	 */
	@SuppressWarnings("unchecked")
	protected IDesignTimeDAO<StyleRepositoryVO> getDAO() throws Exception {
		IDesignTimeDAO dao = DAOFactory.getDefaultDAO(StyleRepositoryVO.class.getName());
		return dao;
	}

	/**
	 * 根据样式库名以及应用标识查找,返回样式库对象StyleRepositoryVO
	 * 
	 * @param name
	 *            样式库名
	 * @param application
	 *            应用标识
	 * @return 样式库对象StyleRepositoryVO
	 * @throws Exception
	 */
	public StyleRepositoryVO getRepositoryByName(String name, String application) throws Exception {
		return ((StyleRepositoryDAO) getDAO()).getRepositoryByName(name, application);

	}

	/**
	 * 根据模块(module)主键(id)以及应用标识来查询,获取相应样式库(StyleRepository)集合.
	 * 
	 * @param moduleid
	 *            模块主键
	 * @param application
	 *            应用标识
	 * @return 样式库(StyleRepository)集合
	 * @throws Exception
	 */
	public Collection<StyleRepositoryVO> getStyleRepositoryByModule(String moduleid, String application)
			throws Exception {
		return ((StyleRepositoryDAO) getDAO()).getStyleRepositoryByModule(moduleid, application);
	}

	/**
	 * 根据应用标识查找,返回相应样式库(StyleRepository)集合.
	 * 
	 * @param application
	 *            应用标识
	 * @return 样式库(StyleRepository)集合
	 * @throws Exception
	 */
	public Collection<StyleRepositoryVO> getStyleRepositoryByApplication(String applicationid) throws Exception {
		return ((StyleRepositoryDAO) getDAO()).getStyleRepositoryByApplication(applicationid);
	}

	/**
	 * 更新样式库(StyleRepository). 如果样式库版本不一致,会抛出异常.提示"Already having been
	 * impropriate by others , can not Save".
	 * 
	 * @param vo
	 *            StyleRepository对象
	 */
	public void doUpdate(ValueObject vo) throws Exception {
		try {
			PersistenceUtils.beginTransaction();
			ValueObject po = getDAO().find(vo.getId());
			if (po !=null &&((StyleRepositoryVO) vo).getVersion() != ((StyleRepositoryVO) po).getVersion())
				throw new OBPMValidateException("{*[core.util.cannotsave]*}",new ImpropriateException("{*[core.util.cannotsave]*}"));
			((StyleRepositoryVO) vo).setVersion(((StyleRepositoryVO) vo).getVersion() + 1);
			if (po != null) {
				PropertyUtils.copyProperties(po, vo);
				getDAO().update(po);
			} else {
				getDAO().update(vo);
			}

			PersistenceUtils.commitTransaction();
		} catch (ImpropriateException e) {
			PersistenceUtils.rollbackTransaction();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			PersistenceUtils.rollbackTransaction();
		}
	}

	/**
	 * 通过样式库主键查询,返回样式库对象实例.
	 * 
	 * @param pk
	 *            样式库主键
	 * @return 样式库值对象.
	 */
	public ValueObject doView(String pk) throws Exception {
		return super.doView(pk);
	}

	/**
	 * 根据样式名，判断名称是否唯一
	 * 
	 * @param name
	 * @param application
	 * @return 存在返回true,否则返回false
	 * @throws Exception
	 */
	public boolean isStyleNameExist(String id, String name, String application) throws Exception {
		return ((StyleRepositoryDAO) getDAO()).isStyleNameExist(id, name, application);
	}
}
