package cn.myapps.km.baike.knowledge.ejb;


import java.util.Collection;
import java.util.Date;

import cn.myapps.km.baike.entry.dao.EntryDao;
import cn.myapps.km.baike.entry.ejb.Entry;
import cn.myapps.km.baike.knowledge.dao.KnowledgeQuestionDAO;
import cn.myapps.km.baike.search.BKSearchEngine;
import cn.myapps.km.baike.user.ejb.BUser;
import cn.myapps.km.base.dao.BDaoManager;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.base.ejb.AbstractRunTimeProcessBean;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.org.ejb.NUser;
import cn.myapps.km.util.Sequence;



/**
 * @author Abel
 * 悬赏问题ejb层的实现类
 */
public class KnowledgeQuestionProcessBean extends AbstractRunTimeProcessBean<KnowledgeQuestion> implements KnowledgeQuestionProcess{

	
	private static final long serialVersionUID = 2788287724926400503L;
	
	
	/**
	 * 当前用户创建词条版本
	 * @param vo
	 * @param user
	 * @throws Exception
	 */
	public void doCreate(NObject vo,NUser user, String indexRealPath) throws Exception {
		KnowledgeQuestion question = (KnowledgeQuestion)vo;
		if (question.getId() == null || question.getId().trim().length() == 0) {
			question.setId(Sequence.getSequence());
		}
		question.setCreated(new Date());
		question.setAuthor(new BUser(user));
		question.setTitle(question.getTitle());
		question.setContent(question.getContent());
		question.setCategoryId(question.getCategoryId());
		question.setPoint(question.getPoint());
		super.doCreate(question);
		BKSearchEngine.createQuestionIndex(question.getTitle(), question, indexRealPath, true);
		}
	

	public DataPackage<KnowledgeQuestion> doQueryAllQuestion(int page, int lines)
			throws Exception {
		return (DataPackage<KnowledgeQuestion>) ((KnowledgeQuestionDAO)getDAO()).queryAllQuestion(page, lines);
	}

/*	*//**
	 * 查询问题
	 * @param queryString
	 * @param indexPath
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public Collection<KnowledgeQuestion> doQuery(String queryString, String indexPath, int page, int lines) throws Exception {
		return BKSearchEngine.searchQuestion(queryString, indexPath,  lines, page);
	}
	
	/**
	 * 通过问题标题查找
	 * @param name
	 * 			词条名称
	 * @return
	 * @throws Exception
	 */
	public KnowledgeQuestion doFindByName(String title) throws Exception {
		return ((KnowledgeQuestionDAO)getDAO()).findByName(title);
	}
	
	/**
	 *所有问题个数 
	 */
	public int getQuestionCounts() throws Exception{
		return ((KnowledgeQuestionDAO)getDAO()).getQuestionCounts();
	}
	
	/**
	 *所有已解决问题个数 
	 */
	public int getAcceptAnswerCounts() throws Exception{
		return ((KnowledgeQuestionDAO)getDAO()).getAcceptAnswerCounts();
	}
	@Override
	protected NRuntimeDAO getDAO() throws Exception {
		// TODO Auto-generated method stub
		return BDaoManager.getKnowledgeQuestionDAO(getConnection());
	}

}
