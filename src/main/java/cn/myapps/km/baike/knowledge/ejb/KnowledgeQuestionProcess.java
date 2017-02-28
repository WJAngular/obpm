package cn.myapps.km.baike.knowledge.ejb;



import java.util.Collection;

import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.base.ejb.NRunTimeProcess;
import cn.myapps.km.org.ejb.NUser;


/**
 * @author Abel
  */
public interface KnowledgeQuestionProcess  extends NRunTimeProcess<KnowledgeQuestion>{
	
	/**
	 * 当前用户创建问题
	 * @param vo
	 * @param user
	 * @throws Exception
	 */
	public void doCreate(NObject vo, NUser user, String indexRealPath) throws Exception;
	
	/**
	 * 查询所有问题
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public DataPackage<KnowledgeQuestion> doQueryAllQuestion(int page, int lines) throws Exception;
	
	/**
	 * 查询问题
	 * @param queryString
	 * @param indexPath
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public Collection<KnowledgeQuestion> doQuery(String queryString, String indexPath, int page, int lines) throws Exception;

	/**
	 * 通过问题标题查找
	 * @param name
	 * 			词条名称
	 * @return
	 * @throws Exception
	 */
	public KnowledgeQuestion doFindByName(String title) throws Exception;
	
	/**
	 *所有问题个数 
	 */
	public int getQuestionCounts() throws Exception;
	
	/**
	 *所有已解决问题个数 
	 */
	public int getAcceptAnswerCounts() throws Exception;
}