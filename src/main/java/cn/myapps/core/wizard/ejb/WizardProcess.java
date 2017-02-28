package cn.myapps.core.wizard.ejb;

import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.IDesignTimeProcess;
import cn.myapps.core.user.action.WebUser;

/**
 * WizardProcess class.
 * 
 * @author zhuxuehong ,Sam
 * @since JDK1.4
 */
public interface WizardProcess extends IDesignTimeProcess<WizardVO> {

	/**
	 * 当确认时，程序创建所有的表单，视图，菜单，流程
	 * 
	 * @param vo
	 *            向导VO
	 * @param user
	 *            当前在线用户
	 * @param applicationid
	 *            当前应用ID
	 * @param contextBasePath
	 *            当前应用的相对路径
	 * @throws Exception
	 */
	public abstract void confirm(ValueObject vo, WebUser user, String applicationid, String contextBasePath)
			throws Exception;
}
