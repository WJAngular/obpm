package cn.myapps.km.baike.history.ejb;

import java.util.Date;
import cn.myapps.km.baike.history.dao.HistoryDao;
import cn.myapps.km.baike.user.ejb.BUser;
import cn.myapps.km.base.dao.BDaoManager;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.base.ejb.AbstractRunTimeProcessBean;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.org.ejb.NUser;
import cn.myapps.util.sequence.Sequence;

/**
 * @author Allen
 * process层，实现业务
 */
public class HistoryProcessBean extends AbstractRunTimeProcessBean<History> implements HistoryProcess{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -160006938248239439L;
	
	@Override
	protected NRuntimeDAO getDAO() throws Exception {
		NRuntimeDAO dao =  BDaoManager.getHistoryDAO(getConnection());
		return dao;
	}
	
	/**
	 * 当前用户创建词条
	 * @param vo
	 * @param user
	 * @throws Exception
	 */
	public void doCreate(NObject vo, NUser user) throws Exception {
		if (vo instanceof History) {
			try {
				if (vo.getId() == null || vo.getId().trim().length() == 0) {
					vo.setId(Sequence.getSequence());
				}
				History history = (History)vo;
				history.setAuthor(new BUser(user));
				history.setReadTime(new Date());
				history.setEntryId(history.getEntryId());
				history.setEntryName(history.getEntryName());
				beginTransaction();
				getDAO().create(history);
				commitTransaction();
			} catch (Exception e) {
				rollbackTransaction();
				throw e;
			}
		}
	}
	
	/**
	 * 通过entryId查找记录
	 */
	public History queryByEntryId(String entryId,String userId) throws Exception{
		return ((HistoryDao)getDAO()).queryByEntryId(entryId,userId);
	}
	/**
	 * 通过userId查找记录
	 */
	public DataPackage<History> queryHistoryByUserId(int page, int lines,String userId) throws Exception{
		return ((HistoryDao)getDAO()).queryHistoryByUserId(page, lines, userId);
	}
}