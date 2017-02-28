package cn.myapps.km.baike.knowledge.dao;

import java.util.Collection;

import cn.myapps.km.baike.content.ejb.EntryContent;
import cn.myapps.km.baike.knowledge.ejb.KnowledgeAnswer;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.dao.NRuntimeDAO;

/**
 * 
 * @author Abel
 *词条内容DAO层接口
 */
public interface KnowledgeAnswerDAO extends NRuntimeDAO{	

	public Collection<KnowledgeAnswer> queryAnswerById(String questionId) throws Exception;
	
	/**
	 * 采纳答案
	 * @param id
	 * @throws Exception
	 */
	public void acceptAnswer(String id) throws Exception;
	
	/**
	 * 通过Id和状态查询通过的词条
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public KnowledgeAnswer queryAcceptAnswerById(String id) throws Exception;
	
	
	
}
