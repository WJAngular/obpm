package cn.myapps.core.helper.dao;

import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.helper.ejb.HelperVO;

public interface HelperDAO extends IDesignTimeDAO<HelperVO>{
	public HelperVO getHelperByName(String urlname, String application) throws Exception;

}
