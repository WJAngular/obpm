package cn.myapps.qm.answer.dao;

import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.qm.answer.ejb.AnswerVO;
import cn.myapps.qm.base.dao.BaseDAO;

public interface AnswerDAO extends BaseDAO {
	public DataPackage<AnswerVO> query(WebUser user) throws Exception;
	
	public DataPackage<AnswerVO> queryForQuestionnaire(String id) throws Exception;
	
	/**
	 * 
	 * 个人答卷查询
	 * @param user
	 * @param s_title
	 * @return
	 * @throws Exception
	 */
	public DataPackage<AnswerVO> queryByFilter(String s_title,int page,int lines,WebUser user) throws Exception;

	/**
	 * 通过questionnaireID 找相对应的 answerID
	 */
	public String answerIDByQuestionnaireID(String questionnaireID) throws Exception;
	
	/**
	 * 通过questionnaireID 找相对应的 answerID
	 */
	public String answerIDByQuestionnaireID(String questionnaireID,WebUser user) throws Exception;
	
	/**
	 * 获取某用户的问卷答案
	 * @param questionnaireId
	 * 问卷ID
	 * @param user
	 * 用户对象
	 * @return
	 * 返回问卷答案对象
	 * @throws Exception
	 */
	public AnswerVO findByQuestionnaireIdAndUserId(String questionnaireId, WebUser user) throws Exception;
	
	/**
	 * 
	 * 个人答卷查询
	 * @param user
	 * @param s_title
	 * @return
	 * @throws Exception
	 */
	public DataPackage<AnswerVO> findInputReport(String questionnaireId,int page,int lines) throws Exception;
	
}
