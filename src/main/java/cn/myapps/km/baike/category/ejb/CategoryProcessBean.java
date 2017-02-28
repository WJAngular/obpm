package cn.myapps.km.baike.category.ejb;

import java.io.IOException;
import java.util.Collection;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;

import cn.myapps.km.baike.category.dao.CategoryDAO;
import cn.myapps.km.base.dao.BDaoManager;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.dao.NRuntimeDAO;
import cn.myapps.km.base.ejb.AbstractRunTimeProcessBean;

/**
 * 
 * @author jodg
 *
 */
public class CategoryProcessBean extends AbstractRunTimeProcessBean<Category> implements CategoryProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3714596104260224508L;
	
	/*
	 * 查询所有词条分类
	 * @return
	 * @throws Exception
	 */
	public Collection<Category> doQuery() throws Exception {
		return ((CategoryDAO)getDAO()).query();
	}
	
	/**
	 * 通过父类ID和域ID获取子类集合
	 * @param parentId
	 * 				父类ID
	 * @param domainId
	 * 				域ID
	 * @return
	 * @throws Exception
	 */
	public Collection<Category> doQuerySubCategory(String parentId,
			String domainId) throws Exception {
		return ((CategoryDAO)getDAO()).querySubCategory(parentId, domainId);
	}

	
	/**
	 * 获取词条的分类
	 * @param entryId
	 * 				词条ID
	 * @return
	 * @throws Exception
	 */
	public Category doFindByEntryId(String entryId) throws Exception {
		return ((CategoryDAO)getDAO()).findByEntryId(entryId);
	}
	
	/**
	 * 分页获取词条分类
	 * @param entryId
	 * 				词条ID
	 * @param page
	 * 				页码
	 * @param lines
	 * 				每页显示记录数量
	 * @return
	 * @throws Exception
	 */
	public DataPackage<Category> doQueryByEntryId(String entryId, int page,
			int lines) throws Exception {
		return ((CategoryDAO)getDAO()).queryCategoryByEntryId(entryId, page, lines);
	}
	
	/**
	 * 获取词条分类集合
	 * @param entryId
	 * 				词条ID
	 * @return
	 * @throws Exception
	 */
	public Collection<Category> doQueryByEntryId(String entryId)
			throws Exception {
		return ((CategoryDAO)getDAO()).queryCategoryByEntryId(entryId);
	}
	
	@Override
	protected NRuntimeDAO getDAO() throws Exception {
		// TODO Auto-generated method stub
		return BDaoManager.getCategoryDAO(getConnection());
	}

	public static void main(String[] args) throws HttpException, IOException {
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod("http://www.baidu.com");
		client.executeMethod(method);
		System.out.println(method.getStatusCode());
		System.out.println(HttpStatus.SC_OK);
		 System.out.println(method.getStatusLine());

	        //打印结果页面

	        String response =new String(method.getResponseBodyAsString().getBytes("8859_1"));

	       //打印返回的信息

	        System.out.println(response);

	        method.releaseConnection();
	} 
	

}
