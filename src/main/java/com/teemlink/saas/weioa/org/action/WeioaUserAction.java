package com.teemlink.saas.weioa.org.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.email.email.action.EmailUserHelper;
import cn.myapps.core.sysconfig.ejb.KmConfig;
import cn.myapps.core.sysconfig.ejb.LoginConfig;
import cn.myapps.core.user.action.UserAction;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.km.disk.ejb.NDiskProcess;
import cn.myapps.km.disk.ejb.NDiskProcessBean;
import cn.myapps.km.org.ejb.NUserRoleSetProcess;
import cn.myapps.km.org.ejb.NUserRoleSetProcessBean;
import cn.myapps.support.weixin.WeixinServiceProxy;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.property.PropertyUtil;
public class WeioaUserAction extends UserAction {

	protected static final String ACTION_RESULT_KEY = "status";
	protected static final int ACTION_RESULT_VALUE_SUCCESS = 1;
	protected static final int ACTION_RESULT_VALUE_FAULT = 0;
	
	protected static final String ACTION_MESSAGE_KEY = "message";
	protected static final String ACTION_DATA_KEY = "data";
	//将会被Struts2序列化为JSON字符串的对象,用来作为控制层返回结果的容器
	protected Map<String, Object> dataMap = new HashMap<String, Object>();
	/**
     * Struts2序列化指定属性时，必须有该属性的getter方法
     * @return
     */
    public Map<String, Object> getDataMap() {
        return dataMap;
    }
    /**
     * 添加Action处理结果
     * @param isSuccess
     * 		是否成功处理
     * @param message
     * 		返回消息
     * @param data
     * 		返回数据
     */
    public void addActionResult(boolean isSuccess,String message,Object data){
    	dataMap.put(ACTION_RESULT_KEY, isSuccess?ACTION_RESULT_VALUE_SUCCESS : ACTION_RESULT_VALUE_FAULT );
    	dataMap.put(ACTION_MESSAGE_KEY, message);
    	if(data != null){
    		dataMap.put(ACTION_DATA_KEY, data);
    	}
    }
    
	public WeioaUserAction() throws Exception {
		super();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1543331093496221500L;
	
	private String result;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String doSave(){
		try {
			UserVO user = (UserVO) getContent();
			user.setEmail(user.getLoginno());
			// user.setUserRoleSets(get_Roles());
			if(user.getLoginno().equals("admin")){
				this.addFieldError("1", "{*[NotCanUseadminAsLoginno]*}");
				throw new OBPMValidateException("{*[NotCanUseadminAsLoginno]*}");
			}
			if(user.getProxyUser()!=null && user.getStartProxyTime()!=null && user.getEndProxyTime()==null){
				this.addFieldError("1", "{*[cn.myapps.core.user.tip.proxyenddate]*}");
				throw new OBPMValidateException("{*[cn.myapps.core.user.tip.proxyenddate]*}");
			}
			
			if(user.getProxyUser()!=null && user.getStartProxyTime()==null && user.getEndProxyTime()!=null){
				this.addFieldError("1", "{*[cn.myapps.core.user.tip.proxystartdate]*}");
				throw new OBPMValidateException("{*[cn.myapps.core.user.tip.proxystartdate]*}");
			}
			
			if(user.getProxyUser()!=null && startProxyTime!=null &&user.getStartProxyTime().getTime()>user.getEndProxyTime().getTime()){
				this.addFieldError("1", "{*[page.core.calendar.overoftime]*}");
				throw new OBPMValidateException("{*[page.core.calendar.overoftime]*}");
			}
			
			if(user.getProxyUser()!=null && endProxyTime!=null &&user.getEndProxyTime().getTime()<(new Date()).getTime()){
				this.addFieldError("1", "{*[cn.myapps.core.user.tip.proxyenddate_currenttime]*}");
				throw new OBPMValidateException("{*[cn.myapps.core.user.tip.proxyenddate_currenttime]*}");
			}
			PropertyUtil.reload("passwordLegal");
			String passwordLength = PropertyUtil.get(LoginConfig.LOGIN_PASSWORD_LENGTH);
			
			if (passwordLength != null && !passwordLength.trim().equals("")) {
				int length = Integer.parseInt(passwordLength);
				if(user.getLoginpwd().length()<length){                         //判断密码长度
					this.addFieldError("1", "{*[PasswordLengthCanNotLow]*}"+length);
					throw new OBPMValidateException("{*[NotCanUseadminAsLoginno]*}");
				}
			}
			
			String legal = PropertyUtil.get(LoginConfig.LOGIN_PASSWORD_LEGAL);
			if(legal.equals("1")||legal=="1"){                                //判断是否开启密码规则
				Pattern p = Pattern.compile("[a-zA-Z0-9]*[a-zA-Z]+[0-9]+[a-zA-Z0-9]*");
				Pattern p1 = Pattern.compile("[a-zA-Z0-9]*[0-9]+[a-zA-Z]+[a-zA-Z0-9]*");
				 Matcher matcher = p.matcher(user.getLoginpwd());
				 Matcher matcher1 = p1.matcher(user.getLoginpwd());
				 boolean result = matcher.matches();
				 boolean result1 = matcher1.matches();
				 if((!result)&&(!result1)){
				this.addFieldError("1", "{*[PasswordLegal]*}");
				 }
			}
			set_departmentids(null);

			// 保存角色
			set_rolesids(null);
			
			if (process.doView(user.getId()) == null) {
				//微信同步新建用户
				createWeixinUser(user);
				process.doCreate(user);
			} else {
				try{
				user = this.setPasswordArray(user);
				}catch(Exception e){
					if(e.getMessage().equals("e1")){
					}
				}
				//微信同步更新用户
				updateWeixinUser(user);
				process.doUpdate(user);
			}
			setContent(user);
			
			this.addActionMessage("{*[Save_Success]*}");
			
			// 关联或创建邮件用户
			HttpServletRequest request = ServletActionContext.getRequest();
			EmailUserHelper.checkAndCreateEmailUser(user, request);
			
			//级联创建KM个人网盘
			PropertyUtil.reload("km");
			if(PropertyUtil.get(KmConfig.ENABLE).equals("true")){
				NDiskProcess ndiskProcess = new NDiskProcessBean();
				ndiskProcess.createByUser(user.getId(), user.getDomainid());
				
				//保存KM角色
				NUserRoleSetProcess nUserRoleSetProcess = new NUserRoleSetProcessBean();
				nUserRoleSetProcess.doUpdateUserRoleSet(user.getId(), getParams().getParameterAsArray("_kmRoleSelectItem"));
			}
			
		}  catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
		}catch (Exception e) {
			addFieldError("1", "服务端未成功响应，请稍后再试！");
			e.printStackTrace();
		}
		if(this.hasFieldErrors()){
			addActionResult(false, "error", this.getFieldErrors());
		}else{
			addActionResult(true, "success", "");
		}
		
		setResult(JSONObject.fromObject(dataMap).toString());
		
		return SUCCESS;
		
	}
	
	public String doDelete() {
		try {
			UserProcess process = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			for (int i = 0; i < _selects.length; i++) {
				UserVO user = (UserVO) process.doView(_selects[i]);
				if(user !=null){
					deleteWeixinUser(user);
				}
			}
			super.doDelete();
		} catch (Exception e) {
			addActionResult(true, "error", e.getMessage());
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		
		
		return SUCCESS;
	}
	
	/**
	 * 在微信端同步创建用户
	 * @param user
	 * @throws Exception
	 */
	private void createWeixinUser(UserVO user) throws Exception {
		try {
			/**
			List<String> deptids = new ArrayList<String>();
			for (Iterator<DepartmentVO> iterator = user.getDepartments().iterator(); iterator.hasNext();) {
				DepartmentVO dept = iterator.next();
				String id = dept.getWeixinDeptId()!=null? dept.getWeixinDeptId():"1";
				if(!deptids.contains(id)){
					deptids.add(id);
				}
			}
			Integer[] dept = new Integer[deptids.size()];
			int j = 0;
			for (Iterator<String> iterator = deptids.iterator(); iterator.hasNext();) {
				dept[j]= Integer.valueOf(iterator.next());
				j++;
			}
			**/
			DepartmentProcess process = (DepartmentProcess) ProcessFactory.createProcess(DepartmentProcess.class);
			DepartmentVO d = (DepartmentVO) process.doView(user.getDefaultDepartment());
			
			Integer[] dept = new Integer[1];
			dept[0] = d.getWeixinDeptId() !=null? Integer.valueOf(d.getWeixinDeptId()) : 1;
			
			WeixinServiceProxy.createUser(user.getDomainid(), user.getName(), user.getLoginno(), user.getTelephone(), user.getEmail(), dept);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 在微信端同步更新用户
	 * @param user
	 * @throws Exception
	 */
	private void updateWeixinUser(UserVO user) throws Exception {
		try {
			/**
			List<String> deptids = new ArrayList<String>();
			for (Iterator<DepartmentVO> iterator = user.getDepartments().iterator(); iterator.hasNext();) {
				DepartmentVO dept = iterator.next();
				String id = dept.getWeixinDeptId()!=null? dept.getWeixinDeptId():"1";
				if(!deptids.contains(id)){
					deptids.add(id);
				}
			}
			int dept[] = new int[deptids.size()];
			String[] deptidArray = (String[]) deptids.toArray();
			for (int i = 0; i < deptidArray.length; i++) {
				dept[i] = Integer.valueOf(deptidArray[i]);
			}
			**/
			DepartmentProcess process = (DepartmentProcess) ProcessFactory.createProcess(DepartmentProcess.class);
			DepartmentVO d = (DepartmentVO) process.doView(user.getDefaultDepartment());
			
			Integer[] dept = new Integer[1];
			dept[0] = d.getWeixinDeptId() !=null? Integer.valueOf(d.getWeixinDeptId()) : 1;
			
			WeixinServiceProxy.updateUser(user.getDomainid(), user.getName(), user.getLoginno(), user.getTelephone(), user.getEmail(), dept);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 在微信端同步删除用户
	 * @param user
	 * @throws Exception
	 */
	private void deleteWeixinUser(UserVO user) throws Exception {
		try {
			WeixinServiceProxy.deleteUser(user.getDomainid(), user.getLoginno());
		} catch (Exception e) {
			throw e;
		}
	}

	public String doResult(){
		ParamsTable params= getParams();
		result = params.getParameterAsString("result");
		return SUCCESS;
	}
}
