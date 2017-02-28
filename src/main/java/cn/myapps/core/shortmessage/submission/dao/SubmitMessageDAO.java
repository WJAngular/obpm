package cn.myapps.core.shortmessage.submission.dao;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.shortmessage.submission.ejb.SubmitMessageVO;
import cn.myapps.core.user.action.WebUser;

public interface SubmitMessageDAO extends IDesignTimeDAO<SubmitMessageVO> {

	public SubmitMessageVO getMessageByReplyCode(String replyCode, String recvtel)
			throws Exception;
	
	public abstract DataPackage<SubmitMessageVO> list(WebUser user,ParamsTable params)throws Exception;

}