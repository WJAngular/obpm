package cn.myapps.qm.questionnaire.dao;

import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.qm.base.dao.BaseDAO;
import cn.myapps.qm.questionnaire.ejb.QuestionnaireVO;

public interface QuestionnaireDAO extends BaseDAO {
	public DataPackage<QuestionnaireVO> query(WebUser user) throws Exception;
	
	/**
	 * 获取用户需要填写的发布问卷
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public DataPackage<QuestionnaireVO> queryByPublish(WebUser user) throws Exception;
	
	/**
	 * 
	 * @param toby
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public DataPackage<QuestionnaireVO> queryByPublishDone(WebUser user) throws Exception;

	/**
	 * 根据条件获取实例集合
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public DataPackage<QuestionnaireVO> queryByFilter(String s_title,int page,int lines,WebUser user) throws Exception;

	/**
	 * 添加1位已参加答卷人数
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public boolean addPaticipate(String id) throws Exception;
}
