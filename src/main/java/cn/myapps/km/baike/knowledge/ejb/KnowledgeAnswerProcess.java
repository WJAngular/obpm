package cn.myapps.km.baike.knowledge.ejb;



import java.util.Collection;

import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.base.ejb.NRunTimeProcess;
import cn.myapps.km.org.ejb.NUser;


/**
 * @author Abel
  */
public interface KnowledgeAnswerProcess  extends NRunTimeProcess<KnowledgeAnswer>{
	
	/**
	 * 当前用户创建答案
	 * @param vo
	 * @param user
	 * @throws Exception
	 */
	public void doCreate(NObject vo, NUser user) throws Exception;
	
	/**
	 * 查询所有的答案
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public Collection<KnowledgeAnswer> queryAnswerById(String questionId) throws Exception;
	
	/**
	 * 采纳答案
	 * @param id
	 * @throws Exception
	 */
	public void doAcceptAnswer(String id, NUser user) throws Exception;
	
	/**
	 * 获取通过的答案
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public KnowledgeAnswer queryAcceptAnswerById(String id) throws Exception;

	}