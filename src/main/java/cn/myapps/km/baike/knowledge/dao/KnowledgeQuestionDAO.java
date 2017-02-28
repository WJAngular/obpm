package cn.myapps.km.baike.knowledge.dao;

import cn.myapps.km.baike.knowledge.ejb.KnowledgeQuestion;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.dao.NRuntimeDAO;

/**
 * 
 * @author Abel
 *词条内容DAO层接口
 */
public interface KnowledgeQuestionDAO extends NRuntimeDAO{	

	/**
	 * 查询所有问题
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public DataPackage<KnowledgeQuestion> queryAllQuestion(int page, int lines) throws Exception;


	
	/**
	 * 查询问题
	 * @param questionId
	 * @throws Exception
	 */
	public void queryQuestionById(String id) throws Exception;
	
	/**
	 * 通过问题标题查询问题
	 * @param questionId
	 * @throws Exception
	 */
	public KnowledgeQuestion findByName(String title) throws Exception;
	
	/**
	 *所有问题个数 
	 */
	public int getQuestionCounts() throws Exception;
	
	/**
	 *所有已解决问题个数 
	 */
	public int getAcceptAnswerCounts() throws Exception;
}
