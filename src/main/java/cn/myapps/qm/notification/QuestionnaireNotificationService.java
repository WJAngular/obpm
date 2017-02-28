package cn.myapps.qm.notification;

import cn.myapps.qm.answer.ejb.AnswerVO;
import cn.myapps.qm.questionnaire.ejb.QuestionnaireVO;

/**
 * qm微信端消息提醒接口
 * @author ahan
 *
 */
public interface QuestionnaireNotificationService {


	/**
	 * 完成问卷时，若所有发布内的用户都已完成答卷，则向发布人推送消息。
	 * @param questionnaire 问卷
	 * @param user 当前操作人
	 * @throws Exception 
	 */
	public void completeAnswer(AnswerVO answer) throws Exception;
	
	/**
	 * 完成问卷时，若所有发布内的用户都已完成答卷，则向发布人推送消息。
	 * @param questionnaire 问卷
	 * @param user 当前操作人
	 * @throws Exception 
	 */
	public void completeQuestionnaire(QuestionnaireVO questionnaire) throws Exception;
	
	/**
	 * 发布问卷时，向在发布范围内的所有用户推送消息。
	 * @param questionnaire 问卷
	 * @param user 当前操作人
	 * @throws Exception
	 */
	public void publish(QuestionnaireVO questionnaire) throws Exception;
	
	
	
}
