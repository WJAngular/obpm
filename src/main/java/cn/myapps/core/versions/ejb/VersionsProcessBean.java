package cn.myapps.core.versions.ejb;

import java.util.Collection;

import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.versions.dao.VersionsDAO;
import cn.myapps.util.sequence.Sequence;

public class VersionsProcessBean extends
		AbstractDesignTimeProcessBean<VersionsVO> implements VersionsProcess{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected IDesignTimeDAO<VersionsVO> getDAO() throws Exception {
		return (VersionsDAO) DAOFactory.getDefaultDAO(VersionsVO.class
				.getName());
	}

	public Collection<VersionsVO> queryByVersionAndType(String name, String number,
			int type) throws Exception {
		return ((VersionsDAO)getDAO()).queryByVersionAndType(name,number, type);
	}

	public void doRecordVersions(VersionsVO vo) throws Exception {
		if(vo != null){
			Collection<VersionsVO> cols = queryByVersionAndType(vo.getVersion_name(), vo.getVersion_number(), vo.getType());
			if(cols == null || cols.isEmpty()){
				try{
					PersistenceUtils.beginTransaction();
					vo.setId(Sequence.getSequence());
					getDAO().create(vo);
					PersistenceUtils.commitTransaction();
				} catch (Exception e) {
					e.printStackTrace();
					PersistenceUtils.rollbackTransaction();
				}
			}
		}
	}
	
	public Collection<VersionsVO> queryByType(int type) throws Exception {
		return ((VersionsDAO)getDAO()).queryByType(type);
	}

	public VersionsVO findCurrVersionByType(int type)
			throws Exception {
		VersionsVO vo = null;
		Collection<VersionsVO> cols = this.queryByType(type);
		if(cols != null && !cols.isEmpty()){
			vo = cols.iterator().next();
		}
		return vo;
	}

}
