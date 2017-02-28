// Source file:
// C:\\Java\\workspace\\SmartWeb3\\src\\com\\cyberway\\dynaform\\document\\dao\\DocumentDAO.java

package cn.myapps.core.dynaform.document.dao;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IRuntimeDAO;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.work.ejb.WorkVO;
import cn.myapps.core.user.action.WebUser;

/**
 * 本系统的Document的查询语句运用自定义的DQL语句.DQL语句类似HQL语句.
 * <p>
 * DQL查询语句语法为:$formname=formname(模块表单名)+ 查询条件;
 * 
 * 例1: 查询付款费用模块下payment的Document的一条为广东省广州市的记录.
 * <p>
 * formname="付款费用/payment";条件为w="and 省份='广东省' and 城市='广州'",条件用"and" 连接起来.
 * 此时的DQL语句为$formname=formname+w . 此处的"付款费用"为模块名,"payment"该模块下的表单名.((表单名与动态表名同名)
 * <p>
 * 系统会将上述所得的DQL转为hibernate的HQL, 最后得出的SQL语句为"SELECT Item_省份,item_城市 FROM
 * tlk_payment where item_省份='广东省' and item_城市='广州'".
 * tlk_payment为动态表名(表名规则为前缀"tlk"+表单名). (动态表的字段名为前缀"item_"+表单字段名).
 * <p>
 * 如果查询语句中的字列有Document的属性时.要加上"$"+属性名,如:$id,$formname.
 * 有Document属性字段的DQL:$formname="付款费用/payment and $id='1000' and
 * $childs.id='1111'";id,chinlds为Document的属性名. Document的属性名有如下: ID, PARENT,
 * LASTMODIFIED, FORMNAME, STATE, AUDITDATE, AUTHOR, CREATED, FORMID, ISTMP,
 * FLOWID, VERSIONS, SORTID, APPLICATIONID, STATEINT, STATELABEL ".
 * 
 * <p>
 * 若查询语句中的字列有item字段时直接写item名.如上述的省份,城市.$formname="付款费用/payment and 省份='广东省 and
 * 城市='广州'".省份,城市为ITEM字段.
 * 
 * 此类为Document DAO 接口类.
 * 
 * @author Marky
 * 
 */
public interface DocumentDAO extends IRuntimeDAO {

	public void removeDocumentByField(Form form, String[] fields)
			throws Exception;

	public void removeDocumentByForm(Form form) throws Exception;
	
	public void changeDocumentFormName(Form form) throws Exception;

	/**
	 * 根据父文档主键查询,获取所属父Document的子Document集合
	 * 
	 * @param parentid
	 *            父文档ID(primary key)
	 * @return Document集合
	 * @throws Exception
	 */
	public Collection<Document> queryByParentID(String parentid)
			throws Exception;

	/**
	 * 根据父文档ID(primary key)与子表单名查询，返回所属父Document的子Document集合。
	 * 
	 * @param parentid
	 *            父文档ID(primary key)
	 * @param formName
	 *            子表单名
	 * @return 所属父Document的子Document集合.
	 * @throws Exception
	 */

	public Collection<Document> queryByParentID(String parentid, String formName)
			throws Exception;

	/**
	 * 根据父文档ID(primary key)与子表单名查询，返回所属父Document的子Document集合。
	 * 
	 * @param parentid
	 *            父文档ID(primary key)
	 * @param formName
	 *            子表单名
	 * @param istmp
	 *            是否临时表单可选0|1|null
	 * @return 所属父Document的子Document集合.
	 * @throws Exception
	 */
	public Collection<Document> queryByParentID(String parentid,
			String formName, Boolean istmp) throws Exception;

	/**
	 * 存储Document Execute Create Or Update
	 * 
	 * @param doc
	 *            Document
	 * @throws Exception
	 */
	public void storeDocument(Document doc) throws Exception;

	/**
	 * 根据Document主键查询Document.
	 * 
	 * @param id
	 *            文档主键
	 * @return 文档值对象
	 * @throws Exception
	 */
	public ValueObject find(String id) throws Exception;

	public long getNeedExportDocumentTotal(String dql, Date date,
			String domainid) throws Exception;

	/**
	 * 根据符合DQL语句以及应用标识查询,返回文档的DataPackage .
	 * <p>
	 * DataPackage为一个封装类，此类封装了所得到的文档数据并分页。
	 * 
	 * @see cn.myapps.base.dao.DataPackage#datas
	 * @see cn.myapps.base.dao.DataPackage#getPageNo()
	 * @see cn.myapps.base.dao.DataPackage#getLinesPerPage()
	 * @see cn.myapps.base.dao.DataPackage#getPageCount()
	 * @param dql
	 *            DQL语句
	 * @param application
	 *            应用标识
	 * @retur 文档的DataPackage
	 * @throws Exception
	 */
	public DataPackage<Document> queryByDQL(String dql, String domainid)
			throws Exception;

	/**
	 * 根据符合DQL执行语句,参数表以及应用标识查询,返回文档的DataPackage.
	 * DataPackage为一个封装类，此类封装了所得到的文档数据并分页。
	 * 
	 * @see cn.myapps.base.dao.DataPackage#datas
	 * @see cn.myapps.base.dao.DataPackage#getPageNo()
	 * @see cn.myapps.base.dao.DataPackage#getLinesPerPage()
	 * @see cn.myapps.base.dao.DataPackage#getPageCount()
	 * @param dql
	 *            DQL语句
	 * @param params
	 *            参数表
	 * @param application
	 *            应用标识
	 * @return 文档DataPackage
	 * @see cn.myapps.base.action.ParamsTable#params
	 * @throws Exception
	 */
	public DataPackage<Document> queryByDQL(String dql, ParamsTable params,
			String domainid) throws Exception;

	public DataPackage<Document> queryBySQL(String sql, String domainid)
			throws Exception;

	public DataPackage<Document> queryBySQL(String sql, ParamsTable params,
			String domainid) throws Exception;

	/**
	 * 根据符合DQL语句以及应用标识查询并分页,返回文档的DataPackage.
	 * 
	 * DataPackage为一个封装类，此类封装了所得到的文档数据并分页。
	 * 
	 * @see cn.myapps.base.dao.DataPackage#datas
	 * @see cn.myapps.base.dao.DataPackage#getPageNo()
	 * @see cn.myapps.base.dao.DataPackage#getLinesPerPage()
	 * @see cn.myapps.base.dao.DataPackage#getPageCount()
	 * @param dql
	 *            dql语句
	 * @param page
	 *            当前页码
	 * @param lines
	 *            每页显示行数
	 * @param application
	 *            应用标识
	 * @return 文档的DataPackage
	 * @throws Exception
	 */
	public DataPackage<Document> queryByDQLPage(String dql, int page,
			int lines, String domainid) throws Exception;

	public DataPackage<Document> queryBySQLPage(String sql, int page,
			int lines, String domainid) throws Exception;

	public DataPackage<Document> queryBySQLPage(String sql, ParamsTable params,
			int page, int lines, String domainid) throws Exception;

	/**
	 * 根据符合DQL语句,参数表以及应用标识查询并分页,返回文档的DataPackage.
	 * DataPackage为一个封装类，此类封装了所得到的文档数据并分页。
	 * 
	 * @see cn.myapps.base.dao.DataPackage#datas
	 * @see cn.myapps.base.dao.DataPackage#getPageNo()
	 * @see cn.myapps.base.dao.DataPackage#getLinesPerPage()
	 * @see cn.myapps.base.dao.DataPackage#getPageCount()
	 * @param dql
	 *            dql语句
	 * @param params
	 *            参数
	 * @see cn.myapps.base.action.ParamsTable#params
	 * @param page
	 *            当前页码
	 * @param lines
	 *            每页显示行数
	 * @param application
	 *            应用标识
	 * @return 符合条件的文档的DataPackage
	 * @throws Exception
	 */
	public DataPackage<Document> queryByDQLPage(String dql, ParamsTable params,
			int page, int lines, String domainid) throws Exception;

	// public DataPackage queryByHQL(String hql, String application)
	// throws Exception;

	// public DataPackage queryByHQL(String hql, ParamsTable params,
	// String application) throws Exception;

	// public DataPackage queryByHQLPage(String hql, ParamsTable params, int
	// page,
	// int lines, String application) throws Exception;

	// public DataPackage queryByFormName(String formname, String application)
	// throws Exception;

	// public Collection findDocsByItemAndFormname(String itemName,
	// String itemValue, String formname, String application)
	// throws Exception;

	// public Document findByItemAndFormname(String itemName, String itemValue,
	// String formname, String application) throws Exception;

	// public void store(Document doc, WebUser user) throws Exception;

	// public void removeByItemAndFormname(String itemName, String itemValue,
	// String formname, String application) throws Exception;
	/**
	 * 根据符合DQL语句,最后修改文档日期,以及应用标识查询并分页,返回文档的DataPackage.
	 * 
	 * DataPackage为一个封装类，此类封装了所得到的文档数据并分页。
	 * 
	 * @see cn.myapps.base.dao.DataPackage#datas
	 * @see cn.myapps.base.dao.DataPackage#getPageNo()
	 * @see cn.myapps.base.dao.DataPackage#getLinesPerPage()
	 * @see cn.myapps.base.dao.DataPackage#getPageCount()
	 * @param dql
	 *            dql语句
	 * @param date
	 *            最后修改文档日期
	 * @param page
	 *            当前页码
	 * @param lines
	 *            每页显示行数
	 * @param application
	 *            应用标识
	 * @return 符合条件的文档的DataPackage
	 * @throws Exception
	 */
	public Iterator<Document> queryByDQLAndDocumentLastModifyDate(String dql,
			Date date, int page, int lines, String domainid) throws Exception;

	// public String findFormFullName(String formid, String application)
	// throws Exception;

	// public Collection queryField(String fieldName, String moduleid,
	// String application) throws Exception;
	/**
	 * 根据符合DQL语句,以及应用标识查询并分页,返回文档的集合.
	 * 
	 * @see cn.myapps.base.dao.DataPackage#datas
	 * @param dql
	 *            dql语句
	 * @param pos
	 *            页码
	 * @param size
	 *            每页显示行数
	 * @param application
	 *            应用标识
	 * @return 文档的集合
	 */
	public Collection<Document> queryLimitByDQL(String dql, int pos, int size,
			String domainid) throws Exception;

	/**
	 * 根据DQL语句以及文档某字段名查询,返回此文档此字段总和.
	 * 
	 * @param dql
	 *            dql语句
	 * @param fieldName
	 *            字段名
	 * @return 文档此字段总和
	 * @throws Exception
	 */
	public double sumByDQL(String dql, String fieldName, String domainid)
			throws Exception;

	/**
	 * 根据DQL语句查询,返回此文档总行数..
	 * 
	 * @param dql
	 *            DQL语句
	 * @return 文档总行数
	 * @throws Exception
	 */

	public long countByDQL(String dql, String domainid) throws Exception;

	public long countByProcedure(String dql, String domainid) throws Exception;
	
	/**
	 * 根据符合DQL执行语句以及应用标识查询并分页,返回文档的数据集.
	 * 
	 * @param dql
	 *            DQL语句
	 * @param pos
	 *            页码
	 * @param size
	 *            每页显示行数
	 * @param application
	 *            应用标识
	 * @return 文档的数据集
	 * @throws Exceptio
	 */
	public Iterator<Document> iteratorLimitByDQL(String dql, int pos, int size,
			String domainid) throws Exception;

	/**
	 * 删除此Document
	 * 
	 * @param doc
	 *            Document
	 * @throws Exception
	 */
	public void removeDocument(Document doc) throws Exception;

	/**
	 * 根据用户,更新相应此文档.
	 * 
	 * @param doc
	 *            文档对象
	 * @param user
	 *            webuser
	 * @throws Exception
	 */
	public void updateDocument(Document doc) throws Exception;

	public void createDocument(Document doc) throws Exception;

	public void createDocument(Document doc, int tabelType) throws Exception;

	public int findVersions(String id) throws Exception;

	public double sumBySQL(String sql, String domainid) throws Exception;

	public long countBySQL(String sql, String domainid) throws Exception;
	
	/**
	 * 查询文档的个数,是以标准sql的形式
	 * 
	 * @param sql
	 *            原生sql
	 * @param domainid
	 *            域标识
	 * @param warpSql
	 *            是否需要包装SQL语句（使原生sql兼容不同数据库）
	 * @return
	 * @throws Exception
	 */
	public long countBySQL(String sql, String domainid,boolean warpSql) throws Exception;

	public boolean checkTable(String tableName) throws Exception;

	public Collection<Document> queryModifiedDocuments(Document doc)
			throws Exception;

	public boolean isExist(String id) throws Exception;

	public Document findByDQL(String dql, String domainid) throws Exception;

	public Document findBySQL(String sql, String domainid) throws Exception;

	// For Authority Table testing. UNSTABLE
	public void createAuthDocWithCondition(String formName, String docId,
			Collection<?> condition) throws Exception;

	public Collection<Document> queryBySQL(String sql, int page, int lines,
			String domainid) throws Exception;

	public void createDocumentHead(Document doc) throws Exception;

	/**
	 * 删除Auth表对应记录
	 * 
	 * @param doc
	 *            Document对象
	 * @throws Exception
	 */
	public void removeAuthByDoc(Document doc) throws Exception;

	public DataPackage<WorkVO> queryWorkBySQLPage(String sql,String countSql, int page, int lines, WebUser user) throws Exception;

	public DataPackage<WorkVO> queryWorkBySQLPage(ParamsTable params, int page,
			int lines, WebUser user) throws Exception;

	public void setWorkProperties(WorkVO work, ResultSet rs);

	public DataPackage<Document> queryByProcedure(String procedure,
			ParamsTable params, int page, int lines, String domainid)
			throws Exception;
	
	public String compareAndUpdateItemWord(String filename, String fieldname, String formname, String docid)throws Exception;
	
	/**
	 * 清除冗余数据
	 * @throws Exception
	 */
	public void doClearRedundancyData() throws Exception;
	
	/**
	 * 获取映射表单同步数据时需要的源表单主键集合
	 * @param domainid
	 * @param form
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public Collection<String> queryMappingTablePrimaryKeys(String domainid, Form form,int page, int lines) throws Exception;
	
	/**
	 * 根据参数传入的条件查询待办任务
	 * @param applicationId
	 * 		软件Id
	 * @param userId
	 * 		用户Id
	 * @param flowId
	 * 		流程Id
	 * @param subject
	 * 		待办主题
	 * @param currpage
	 * 		当前页数
	 * @param pagelines
	 * 		每页记录数
	 * @param isFlowAgent
	 * 		是否显示代理的代办
	 * @return
	 * @throws Exception
	 */
	public DataPackage<WorkVO> queryProcessingWorks(String applicationId,WebUser user,String flowId,String subject,int currpage,int pagelines,boolean isFlowAgent) throws Exception;
	
	/**
	 * 根据参数传入的条件查询经办任务
	 * @param applicationId
	 * 		软件Id
	 * @param user
	 * 		用户
	 * @param flowId
	 * 		流程Id
	 * @param subject
	 * 		待办主题
	 * @param currpage
	 * 		当前页数
	 * @param pagelines
	 * 		每页记录数
	 * @return
	 * @throws Exception
	 */
	public DataPackage<WorkVO> queryProcessedRunningWorks(String applicationId,WebUser user,String flowId,String subject,int currpage,int pagelines) throws Exception;
	
	/**
	 * 根据参数传入的条件查询历史任务
	 * @param applicationId
	 * 		软件Id
	 * @param user
	 * 		用户
	 * @param flowId
	 * 		流程Id
	 * @param subject
	 * 		待办主题
	 * @param currpage
	 * 		当前页数
	 * @param pagelines
	 * 		每页记录数
	 * @return
	 * @throws Exception
	 */
	public DataPackage<WorkVO> queryProcessedCompletedWorks(String applicationId,WebUser user,String flowId,String subject,Boolean myInitiatedList,int currpage,int pagelines) throws Exception;
}
