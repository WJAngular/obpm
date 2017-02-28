package cn.myapps.km.baike.knowledge.ejb;

import java.util.Collection;
import java.util.Date;

import cn.myapps.km.baike.knowledge.dao.KnowledgeAnswerDAO;
import cn.myapps.km.baike.knowledge.dao.KnowledgeQuestionDAO;
import cn.myapps.km.baike.user.dao.BUserAttributeDAO;
import cn.myapps.km.baike.user.ejb.BUser;
import cn.myapps.km.baike.user.ejb.BUserAttribute;
import cn.myapps.km.base.dao.BDaoManager;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.base.ejb.AbstractRunTimeProcessBean;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.org.ejb.NUser;
import cn.myapps.km.util.Sequence;



/**
 * @author Abel
 * 知识悬赏答案ejb层的实现类
 */
public class KnowledgeAnswerProcessBean extends AbstractRunTimeProcessBean<KnowledgeAnswer> implements KnowledgeAnswerProcess{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6491783765947097291L;

	/**
	 * 当前用户创建答案
	 * @param vo
	 * @param user
	 * @throws Exception
	 */
	public void doCreate(NObject vo, NUser user) throws Exception {
			KnowledgeAnswer knowledge = (KnowledgeAnswer)vo;
			if (knowledge.getId() == null || knowledge.getId().trim().length() == 0) {
				knowledge.setId(Sequence.getSequence());
			}
				knowledge.setQuestionId(knowledge.getQuestionId());
				knowledge.setContent(knowledge.getContent());
				knowledge.setAuthor(new BUser(user));
				knowledge.setSubmitTime(new Date());
				knowledge.setState(KnowledgeAnswer.STATE_UNACCEPT);
				super.doCreate(knowledge);
		}
	

	@Override
	protected NRuntimeDAO getDAO() throws Exception {
		// TODO Auto-generated method stub
		return BDaoManager.getKnowledgeAnswerDAO(getConnection());
	}

	/**
	 * 通过Id查询答案
	 */
	public Collection<KnowledgeAnswer> queryAnswerById(String questionId)
			throws Exception {
		return (Collection<KnowledgeAnswer>) ((KnowledgeAnswerDAO)getDAO()).queryAnswerById(questionId);
	}

	
	/**
	 * 采纳答案
	 * @param id
	 * @throws Exception
	 */
	public void doAcceptAnswer(String id, NUser user) throws Exception{
		 ((KnowledgeAnswerDAO)getDAO()).acceptAnswer(id);
		 KnowledgeAnswer answer=(KnowledgeAnswer) new KnowledgeAnswerProcessBean().doView(id);
		 KnowledgeQuestion question=(KnowledgeQuestion) new KnowledgeQuestionProcessBean().doView(answer.getQuestionId());
		 
		try{
			BUserAttributeDAO attributeDao = (BUserAttributeDAO)BDaoManager.getBUserAttributeDAO(getConnection());
			BUserAttribute attribute = attributeDao.findByUserId(user.getId());
			beginTransaction();
			if (attribute == null) {
				attribute = new BUserAttribute();
				attribute.setUserId(user.getId());
				attribute.setId(Sequence.getSequence());
				attribute.setIntegral(question.getPoint());
				attributeDao.create(attribute);
			} else {
				attributeDao.addPoint(user.getId(),question.getPoint() );
			}
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			throw e;
		}
	}
	
	/**
	 * 获取通过的答案
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public KnowledgeAnswer queryAcceptAnswerById(String id) throws Exception{
		return  ((KnowledgeAnswerDAO)getDAO()).queryAcceptAnswerById(id);
	}


	
}
