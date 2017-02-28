package cn.myapps.qm.answer.ejb;

import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.qm.base.ejb.BaseProcess;

public interface AnswerProcess extends BaseProcess<AnswerVO> {
	public DataPackage<AnswerVO> doViewForQuestionnaire(String pk) throws Exception;
	
	public AnswerVO doSave(ValueObject vo) throws Exception ;
	
	/**
	 * 查询根据条件查找实例集合
	 * @param params
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public DataPackage<AnswerVO> doQueryByFilter(String s_title,int page,int lines,WebUser user) throws Exception;

	/**
	 * 根据问卷的ID查询答卷的ID
	 * @param questionnaireID
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public String answerIDByQuestionnaireID(String questionnaireID) throws Exception;
	
	/**
	 * 根据问卷的ID和相对应的用户查询答卷的ID
	 * @param questionnaireID
	 * @param user
	 * @return
	 * @throws Exception
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
	 * 获取填空题报表数据
	 * @param questionnaireId
	 * 问卷ID
	 * @param user
	 * 用户对象
	 * @return
	 * 返回问卷答案对象
	 * @throws Exception
	 */
	public DataPackage<AnswerVO> findInputReport(String questionnaireId,String q_Id,int page,int lines) throws Exception;
	
}
