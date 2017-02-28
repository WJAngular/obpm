package cn.myapps.core.shortmessage.received.ejb;

import java.util.Collection;
import java.util.Date;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DAOFactory;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.AbstractDesignTimeProcessBean;
import cn.myapps.core.permission.ejb.PermissionPackage;
import cn.myapps.core.shortmessage.received.dao.ReceivedMessageDAO;

public class ReceivedMessageProcessBean extends AbstractDesignTimeProcessBean<ReceivedMessageVO>
		implements ReceivedMessageProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1754608461011239235L;

	/**
	 * 创建待发短信
	 * 
	 * @param vo
	 *            短信内容
	 */
	public void doCreate(ValueObject vo) throws Exception {
		ReceivedMessageVO receiverVO = (ReceivedMessageVO) vo;
		receiverVO.setCreated(new Date());
		super.doCreate(receiverVO);
		PermissionPackage.clearCache();
	}

	/**
	 * 根据Document ID 查询回复记录
	 * @param docid 文档ID
	 * @see cn.myapps.base.dao.DataPackage
	 * @return　返回回复记录结果 
	 * @throws Exception
	 */
	public DataPackage<ReceivedMessageVO> queryByDocId(String docid) throws Exception {
		ParamsTable params = new ParamsTable();
		params.setParameter("s_docid", docid);
		return getDAO().query(params);
	}
	
	/**
	 * 根据Document ID 查询回复记录
	 * @param parentid 关联的发送记录ID
	 * @see cn.myapps.base.dao.DataPackage
	 * @return　返回回复记录结果 
	 * @throws Exception
	 */
	public DataPackage<ReceivedMessageVO> queryByParent(String id) throws Exception{
		ParamsTable params = new ParamsTable();
		params.setParameter("s_parent", id);
		return getDAO().query(params);
	}

	protected IDesignTimeDAO<ReceivedMessageVO> getDAO() throws Exception {
		return (ReceivedMessageDAO)DAOFactory.getDefaultDAO(ReceivedMessageVO.class.getName());
	}
	
	/**
	 * 查询出未读回复记录
	 * @return 返回未读回复记录集
	 * @throws Exception
	 */
	public Collection<ReceivedMessageVO> doQueryUnReadMessage() throws Exception{
		return ((ReceivedMessageDAO)getDAO()).queryUnReadMessage();
	}

}
