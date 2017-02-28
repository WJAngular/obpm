package cn.myapps.core.deploy.application.action;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.constans.Web;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.property.MultiLanguageProperty;

public class ApplicationRunTimeAction extends ApplicationAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1781227401370916693L;

	public ApplicationRunTimeAction() throws ClassNotFoundException {
		super();
	}
	
	/**
	 * @SuppressWarnings 工厂方法无法使用泛型
	 * @return
	 */
	public String doChange() {
		try {
			WebUser webUser = getUser();
			String userid = webUser.getId();
			
			UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			// 更新默认应用
			userProcess.doUpdateDefaultApplication(userid, getId());
			//webUser.setApplicationid(getId());
			webUser.setDefaultApplication(getId());

			MultiLanguageProperty.load(getId(), false);
		}  catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}

		return SUCCESS;
	}

	public String getWebUserSessionKey() {
		return Web.SESSION_ATTRIBUTE_FRONT_USER;
	}
}
