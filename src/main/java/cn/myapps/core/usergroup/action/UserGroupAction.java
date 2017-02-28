package cn.myapps.core.usergroup.action;

import java.util.Iterator;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.BaseAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.constans.Environment;
import cn.myapps.constans.Web;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.usergroup.ejb.UserGroupProcess;
import cn.myapps.core.usergroup.ejb.UserGroupSetProcess;
import cn.myapps.core.usergroup.ejb.UserGroupVO;
import cn.myapps.util.ProcessFactory;

public class UserGroupAction extends BaseAction<UserGroupVO>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7651456037544497113L;

	@SuppressWarnings("unchecked")
	public UserGroupAction() throws Exception{
		super(ProcessFactory.createProcess(UserGroupProcess.class), new UserGroupVO());
	}

	public String doSave(){
		try {
			UserGroupVO vo = (UserGroupVO) getContent();
			ParamsTable params = getParams();
			vo.setOwnerId(getUser().getId());
			vo.setDomainid(params.getParameterAsString("domain"));
			
			UserGroupProcess userGroupProcess = (UserGroupProcess) this.process;
			userGroupProcess.doCreateOrUpdate(vo);
		} catch (OBPMValidateException e) {
			this.addFieldError("1", "{*[cn.myapps.core.usergroup.create.fail]*}:"+e.getValidateMessage());
			e.printStackTrace();
			return INPUT;
		} catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[cn.myapps.core.usergroup.create.fail]*}:"+"{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
		this.addActionMessage("{*[cn.myapps.core.usergroup.create.success]*}");
		return SUCCESS;
	}
	
	public String doSaveNew(){
		try {		
			String rtn = doSave();
			setContent(new UserGroupVO());
			return rtn;
		} catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[cn.myapps.core.usergroup.create.fail]*}:"+"{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}
	
	public String doRename() {
		try {
			ParamsTable params = getParams();
			UserGroupProcess userGroupProcess = (UserGroupProcess) this.process;
			UserGroupVO vo = (UserGroupVO) userGroupProcess.doView(params
					.getParameterAsString("renameId"));
			vo.setName(params.getParameterAsString("rename"));
			userGroupProcess.doCreateOrUpdate(vo);
		} catch (OBPMValidateException e) {
			this.addFieldError("1", "{*[cn.myapps.core.usergroup.rename.fail]*}"+e.getValidateMessage());
			e.printStackTrace();
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[cn.myapps.core.usergroup.rename.fail]*}"+"{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
		this.addActionMessage("{*[cn.myapps.core.usergroup.rename.success]*}");
		return SUCCESS;
	}

	
	public String doView(){
		try {
			WebUser user = this.getUser();
			this.setDatas(((UserGroupProcess)process).getUserGroupsByUser(user.getId()));
		}  catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	public String doAddToGroup() {
		try {
			ParamsTable params = this.getParams();
			UserGroupSetProcess userGroupSetProcess = (UserGroupSetProcess) ProcessFactory.createProcess(UserGroupSetProcess.class);
			userGroupSetProcess.addUserToGroup(params.getParameterAsString("userids").split(";"), params.getParameterAsString("usergroupid"));
		} catch (OBPMValidateException e) {
			this.addFieldError("1", "{*[cn.myapps.core.usergroup.add.fail]*}:"+e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[cn.myapps.core.usergroup.add.fail]*}:"+"{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		
		this.addActionMessage("{*[cn.myapps.core.usergroup.add.success]*}");
		return SUCCESS;
	}
	
	public String  doRemove(){
		try {
			
			ParamsTable params = this.getParams();
			UserGroupSetProcess userGroupSetProcess = (UserGroupSetProcess) ProcessFactory.createProcess(UserGroupSetProcess.class);
			userGroupSetProcess.deleteByUser(params.getParameterAsString("_selects").split(";"), params.getParameterAsString("userGroupId"));
		} catch (OBPMValidateException e) {
			this.addFieldError("1", "{*[cn.myapps.core.usergroup.remove.fail]*}"+e.getValidateMessage());
			e.printStackTrace();
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[cn.myapps.core.usergroup.remove.fail]*}"+"{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		
		this.addActionMessage("{*[cn.myapps.core.usergroup.remove.success]*}");
		return SUCCESS;
	}
	
	public String  doRemoveGroup(){
		try {
			
			ParamsTable params = this.getParams();
			String userGroupId = params.getParameterAsString("delid");
			UserGroupProcess userGroupProcess = (UserGroupProcess) ProcessFactory.createProcess(UserGroupProcess.class);
			userGroupProcess.deleteGroup(userGroupId);
		} catch (OBPMValidateException e) {
			this.addFieldError("1", "{*[cn.myapps.core.usergroup.remove.fail]*}"+e.getValidateMessage());
			e.printStackTrace();
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[cn.myapps.core.usergroup.remove.fail]*}"+"{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		
		this.addActionMessage("{*[cn.myapps.core.usergroup.remove.success]*}");
		return SUCCESS;
	}
	
	public String doContancts(){
		StringBuffer html = new StringBuffer();
		try {
			WebUser user = this.getUser();
			DataPackage<UserGroupVO> userGroups = ((UserGroupProcess)process).getUserGroupsByUser(user.getId());
			html.append("<div id='all' class='list_div' title='{*[cn.myapps.core.usergroup.allcontact]*}' onclick='getUserListByContancts(jQuery(this))'>");
			html.append("<img id='img_" + "all" + "' class='selectImg_right' src='"
					+ Environment.getInstance().getContextPath() + "/resource/images/right_2.gif'/>");
			html.append("{*[cn.myapps.core.usergroup.allcontact]*}");
			html.append("</div>");
			for (Iterator<UserGroupVO> iter = userGroups.datas.iterator(); iter.hasNext();) {
				UserGroupVO tempUserGroup = (UserGroupVO) iter.next();
				html.append("<div id='" + tempUserGroup.getId() + "' class='list_div' title='" + tempUserGroup.getName()
						+ "' onclick='getUserListByContancts(jQuery(this))'>");
				html.append("<img id='img_" + tempUserGroup.getId() + "' class='selectImg_right' src='"
						+ Environment.getInstance().getContextPath() + "/resource/images/right_2.gif'/>");
				html.append(tempUserGroup.getName());
				html.append("</div>");
			}

			ServletActionContext.getRequest().setAttribute("HTML", html.toString());
		} catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public WebUser getUser() throws Exception {
		Map session = getContext().getSession();

		WebUser user = (WebUser) session.get(getWebUserSessionKey());

		if (user == null) {
			user = getAnonymousUser();
		}

		return user;
	}

	public String getWebUserSessionKey() {
		return Web.SESSION_ATTRIBUTE_FRONT_USER;
	}
}
