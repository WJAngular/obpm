package cn.myapps.attendance.rule.ejb;

import cn.myapps.attendance.base.ejb.BaseProcess;
import cn.myapps.core.user.action.WebUser;

public interface RuleProcess extends BaseProcess<Rule> {
	
	/**
	 * 根据用户获得考勤规则
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Rule getRuleByUser(WebUser user) throws Exception;

}
