package cn.myapps.qm.answer.ejb;

import java.util.Collection;
import java.util.Date;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.qm.answer.dao.AnswerDAO;
import cn.myapps.qm.base.dao.BaseDAO;
import cn.myapps.qm.base.dao.DaoManager;
import cn.myapps.qm.base.ejb.AbstractBaseProcessBean;
import cn.myapps.qm.notification.QuestionnaireNotificationService;
import cn.myapps.qm.notification.QuestionnaireNotificationServiceImpl;
import cn.myapps.qm.questionnaire.ejb.QuestionnaireProcess;
import cn.myapps.qm.questionnaire.ejb.QuestionnaireProcessBean;
import cn.myapps.qm.questionnaire.ejb.QuestionnaireVO;
import cn.myapps.qm.util.CensusUtil;
import cn.myapps.util.StringUtil;
import cn.myapps.util.sequence.Sequence;

public class AnswerProcessBean extends AbstractBaseProcessBean<AnswerVO> implements AnswerProcess {
	
	public DataPackage<AnswerVO> doQuery(ParamsTable params, WebUser user) throws Exception {
		 DataPackage<AnswerVO> datas = ((AnswerDAO)getDAO()).query(user);
		return datas;
	}

	public Collection<AnswerVO> doSimpleQuery(ParamsTable params, WebUser user) throws Exception {
		return null;
	}

	@Override
	public BaseDAO getDAO() throws Exception {
		return DaoManager.getAnswer(getConnection());
	}

	public ValueObject doUpdate(ValueObject vo) throws Exception {
		return ((AnswerDAO)getDAO()).update(vo);
	}

	public void doRemove(String pk) throws Exception {
		((AnswerDAO)getDAO()).delete(pk);
	}
	
	public DataPackage<AnswerVO> doViewForQuestionnaire(String pk) throws Exception{
		DataPackage<AnswerVO> datas = ((AnswerDAO)getDAO()).queryForQuestionnaire(pk);
		return datas;
	}

	public AnswerVO doSave(ValueObject vo) throws Exception {
		try{
			QuestionnaireProcess questionnaireProcess = new QuestionnaireProcessBean();
			AnswerVO answerVO = ((AnswerVO)vo);
			beginTransaction();
			int score = CensusUtil.currentScore(answerVO);
			answerVO.setAccount(score);
			if(StringUtil.isBlank(answerVO.getId())){
				answerVO.setId(Sequence.getSequence());
				answerVO.setDate(new Date());
				answerVO = (AnswerVO) super.doCreate(answerVO);
			}else{
				answerVO = (AnswerVO) doUpdate(answerVO);
			}
			questionnaireProcess.addPaticipate(answerVO.getQuestionnaire_id());
			commitTransaction();
			
			//发送消息
			QuestionnaireNotificationService questionnaireNotificationService = new QuestionnaireNotificationServiceImpl();
			QuestionnaireVO questionnaireVO = (QuestionnaireVO) questionnaireProcess.doView(answerVO.getQuestionnaire_id());
			if(questionnaireVO.getParticipantTotal()>=questionnaireVO.getAnswerTotal())
				questionnaireNotificationService.completeAnswer(answerVO);
		}catch(Exception e){
			rollbackTransaction();
			e.printStackTrace();
		}
		return (AnswerVO) vo;
	}
	
	/**
	 * 查询根据条件查找实例集合
	 * @param params
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public DataPackage<AnswerVO> doQueryByFilter(String s_title,int page,int lines,WebUser user) throws Exception{
		return ((AnswerDAO)getDAO()).queryByFilter(s_title,page,lines,user);
	}
	
	public String answerIDByQuestionnaireID(String questionnaireID) throws Exception{
		return ((AnswerDAO)getDAO()).answerIDByQuestionnaireID(questionnaireID);
	}
	public String answerIDByQuestionnaireID(String questionnaireID, WebUser user) throws Exception{
		return ((AnswerDAO)getDAO()).answerIDByQuestionnaireID(questionnaireID,user);
	}

	@Override
	public AnswerVO findByQuestionnaireIdAndUserId(String questionnaireId, WebUser user)
			throws Exception {
		return ((AnswerDAO)getDAO()).findByQuestionnaireIdAndUserId(questionnaireId, user);
	}

	@Override
	public DataPackage<AnswerVO> findInputReport(String questionnaireId,
			String q_Id, int page, int lines) throws Exception {
		DataPackage<AnswerVO> answerVO = ((AnswerDAO)getDAO()).findInputReport(questionnaireId,page,lines);
		Collection<AnswerVO>  data = CensusUtil.inputReport(answerVO.datas,q_Id);
		answerVO.datas=data;
		return answerVO;
	}
	
	
}
