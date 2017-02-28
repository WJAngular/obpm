package cn.myapps.core.privilege.res.ejb;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.IDesignTimeProcess;
import cn.myapps.core.tree.Node;

public interface ResProcess extends IDesignTimeProcess<ResVO> {

	/**
	 * 获得显示资源树形结构
	 * 
	 * @param params
	 * @param rprocess
	 * @return
	 * @throws Exception
	 */
	public Collection<Node> getResTree(ParamsTable params) throws Exception;

	public ValueObject doViewByName(String name, String application) throws Exception;

}
