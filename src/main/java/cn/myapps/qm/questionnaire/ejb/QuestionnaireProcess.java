package cn.myapps.qm.questionnaire.ejb;

import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.qm.base.ejb.BaseProcess;

public interface QuestionnaireProcess extends BaseProcess<QuestionnaireVO> {

	public DataPackage<QuestionnaireVO> doQuery(WebUser user) throws Exception;

	public QuestionnaireVO doShowResult(String pk, WebUser user)
			throws Exception;

	public DataPackage<QuestionnaireVO> doQueryByPublish(WebUser user)
			throws Exception;

	public DataPackage<QuestionnaireVO> doQueryByPublishDone(WebUser user)
			throws Exception;

	public QuestionnaireVO doNew() throws Exception;

	/**
	 * 查询根据条件查找实例集合
	 * 
	 * @param params
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public DataPackage<QuestionnaireVO> doQueryByFilter(String s_title,
			int page, int lines, WebUser user) throws Exception;

	/**
	 * 查询根据条件查找我发布的问卷实例集合
	 * @param title
	 *            需要查找的标题
	 * @param status
	 *            问卷的状态
	 * @param page
	 *            当前页数
	 * @param lines
	 *            每页的条数
	 * @param user
	 *            查询的用户
	 * @return
	 * @throws Exception
	 */
	public DataPackage<QuestionnaireVO> doQueryMyPublish(String title,
			int status, int page, int lines, WebUser user) throws Exception;

	/**
	 * 查询根据条件查找我参与的问卷实例集合
	 * @param title
	 *            需要查找的标题
	 * @param status
	 *            问卷的状态
	 * @param page
	 *            当前页数
	 * @param lines
	 *            每页的条数
	 * @param user
	 *            查询的用户
	 * @return
	 * @throws Exception
	 */
	public DataPackage<QuestionnaireVO> doQueryMyPartake(String title,
			int status, int page, int lines, WebUser user) throws Exception;

	/**
	 * 获取投票题的统计结果
	 * 
	 * @param pk
	 *            问卷id
	 * @param user
	 */
	public String doShowVoteNumber(String pk, String holder_id, WebUser user)
			throws Exception;

	/**
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public ValueObject doSave(ValueObject vo) throws Exception;

	/**
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public ValueObject doReBuild(ValueObject vo) throws Exception;

	/**
	 * 提交答卷，使对应的已填写问卷人数加1
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean addPaticipate(String id) throws Exception;
	
	/**
	 * 获取问卷报表需要的统计数据
	 * @param id
	 * 		问卷的id
	 * @return
	 * @throws Exception
	 */
	public QuestionnaireVO showReportForm(String id) throws Exception;
}
