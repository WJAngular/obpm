package cn.myapps.core.shortmessage.submission.ejb;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.ejb.IDesignTimeProcess;
import cn.myapps.core.user.action.WebUser;

public interface SubmitMessageProcess extends IDesignTimeProcess<SubmitMessageVO> {

	/**
	 * 根据当前用户提交的参数params查询出符合条件的发送成功的记录
	 * @param user 当前用户
	 * @param params @see cn.myapps.base.action.ParamsTable
	 * @return 返回发送成功的记录信息集合
	 * @throws Exception
	 */
	public abstract DataPackage<SubmitMessageVO> listSubmitMessage(WebUser user,ParamsTable params) throws Exception;
	
	public abstract DataPackage<SubmitMessageVO> list(WebUser user,ParamsTable params)throws Exception;
	
	/**
	 * 根据当前用户提交的参数params查询出符合条件的发送失败的记录
	 * @param user 当前用户
	 * @param params @see cn.myapps.base.action.ParamsTable
	 * @return 返回发送失败的记录信息集合
	 * @throws Exception
	 */
	public abstract DataPackage<SubmitMessageVO> listFailureMessage(WebUser user,ParamsTable params) throws Exception;
	
	/**
	 * 根据回复者电话号码与回复代码查找发送记录
	 * @param replyCode 回复代码
	 * @param recvtel 接收者电话号码
	 * @return 根据回复者电话号码与回复代码查找发送记录
	 * @throws Exception
	 */
	public SubmitMessageVO getMessageByReplyCode(String replyCode, String recvtel) throws Exception;
	
	/**
	 * 判断回复代码是否可用
	 * @param code 回复代码
	 * @param recvtel 接收者电话号码
	 * @return 判断回复代码是否可用，true:可用，否则，不可用。
	 * @throws Exception
	 */
	public boolean unAvailableCode(String code, String recvtel) throws Exception;
}