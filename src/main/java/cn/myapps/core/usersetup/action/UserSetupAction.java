package cn.myapps.core.usersetup.action;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.BaseAction;
import cn.myapps.constans.Web;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.usersetup.ejb.UserSetupProcess;
import cn.myapps.core.usersetup.ejb.UserSetupVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

public class UserSetupAction extends BaseAction<UserSetupVO> {

	/**
	 * @SuppressWarnings 工厂方法不支持泛型
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public UserSetupAction() throws ClassNotFoundException {
		super(ProcessFactory.createProcess(UserSetupProcess.class), new UserSetupVO());
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5040193129783611761L;

	public String doUserSetup(){
		StringBuffer html=new StringBuffer();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpSession session = ServletActionContext.getRequest().getSession();
		try {
			UserSetupVO userSetup=(UserSetupVO) this.getContent();
			UserVO user= (UserVO) ProcessFactory.createProcess(UserProcess.class).doView(userSetup.getId());
			if(user!=null){
				UserSetupVO olduserSetup=(UserSetupVO) process.doView(userSetup.getId());
				if(userSetup.getUserSkin()!=null && !userSetup.getUserSkin().equals("")){
					olduserSetup.setUserSkin(userSetup.getUserSkin());
					session.setAttribute("SKINTYPE", userSetup.getUserSkin());
				}
				if(userSetup.getUserStyle()!=null && !userSetup.getUserStyle().equals("")){
					olduserSetup.setUserStyle(userSetup.getUserStyle());
					session.setAttribute("USERSTYLE", userSetup.getUserStyle());
				}
				olduserSetup.setUser(user);
				process.doUpdate(olduserSetup);
				
				//更新webuser的usersetup属性
				WebUser webUser=(WebUser) session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
				webUser.setUserSetup(olduserSetup);
				session.setAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER, webUser);
			}
			
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(html.toString());
		} catch (ClassNotFoundException e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		} catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return html.toString();
	}
	/**
	 * mainFrame更改皮肤设置
	 * @return success
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public String doUserSetupOnMainFrame() throws ClassNotFoundException, Exception{
		String userSetupId = getParams().getParameterAsString("userSetupId");
		String userSkin = getParams().getParameterAsString("userSkin");
		UserSetupVO userSetup = (UserSetupVO) process.doView(userSetupId);
		if(userSetup != null){
			userSetup.setUserSkin(userSkin);
			process.doUpdate(userSetup);
		}
		HttpSession session = ServletActionContext.getRequest().getSession();
		WebUser webUser = (WebUser) session.getAttribute("FRONT_USER");
		

		if (!StringUtil.isBlank(userSkin)) {
			String oldSkin = (String) session.getAttribute(Web.SKIN_TYPE);
			if (!StringUtil.isBlank(oldSkin)) {
				if(!oldSkin.equals(userSkin)){
					session.setAttribute(Web.SKIN_TYPE, userSkin);
					if(webUser.getUserSetup() != null){
						webUser.getUserSetup().setUserSkin(userSkin);
					}
					return "switchskin";
				}
			}else{
				session.setAttribute(Web.SKIN_TYPE, userSkin);
				if(webUser.getUserSetup() != null){
					webUser.getUserSetup().setUserSkin(userSkin);
				}
				return "switchskin";
			}
		}
		return "switchskin";
		
	}
}
