package cn.myapps.attendance.rule.ejb;

import cn.myapps.attendance.base.dao.BaseDAO;
import cn.myapps.attendance.base.dao.DaoManager;
import cn.myapps.attendance.base.ejb.AbstractBaseProcessBean;
import cn.myapps.attendance.rule.dao.RuleDAO;
import cn.myapps.core.user.action.WebUser;


public class RuleProcessBean extends AbstractBaseProcessBean<Rule>
		implements RuleProcess {


	@Override
	public BaseDAO getDAO() throws Exception {
		return DaoManager.getRuleDAO(getConnection());
	}

	public Rule getRuleByUser(WebUser user) throws Exception {
		Rule rule= null;
		//1.优先按用户获取考勤规则
		rule = (Rule) ((RuleDAO)getDAO()).findBySQL("SELECT * FROM AM_RULE WHERE ORGANIZATION_TYPE=2 AND DOMAIN_ID='"+user.getDomainid()+"' AND ORGANIZATIONS like '%"+user.getId()+"%'");
		if(rule==null){
			//2.按用户默认部门获取考勤规则
			rule = (Rule) ((RuleDAO)getDAO()).findBySQL("SELECT * FROM AM_RULE WHERE ORGANIZATION_TYPE=1 AND DOMAIN_ID='"+user.getDomainid()+"' AND ORGANIZATIONS like '%"+user.getDefaultDepartment()+"%'");
		}
		if(rule ==null){
			//3.按用户所属公司获取考勤规则
			//rule = (Rule) ((RuleDAO)getDAO()).findBySQL("SELECT * FROM AM_RULE WHERE ORGANIZATION_TYPE=0 AND DOMAIN_ID='"+user.getDomainid()+"'");
		}
		
		return rule;
	}


}
