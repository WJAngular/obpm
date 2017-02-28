package cn.myapps.support.weixin.action;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.constans.Web;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.km.util.StringUtil;
import cn.myapps.pm.task.ejb.TaskProcessBean;
import cn.myapps.support.weixin.WeixinServiceProxy;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.Security;
import cn.myapps.util.json.JsonUtil;

public class WeixinServiceAction extends ActionSupport {
	
	private static final long serialVersionUID = -1021941166716325951L;
	
	protected static final String ACTION_RESULT_KEY = "status";
	protected static final int ACTION_RESULT_VALUE_SUCCESS = 1;
	protected static final int ACTION_RESULT_VALUE_FAULT = 0;
	
	protected static final String ACTION_MESSAGE_KEY = "message";
	protected static final String ACTION_DATA_KEY = "data";
	
	
	protected ParamsTable params;
	
	//将会被Struts2序列化为JSON字符串的对象,用来作为控制层返回结果的容器
	protected Map<String, Object> dataMap = new HashMap<String, Object>();
	
	
	
	
	
	
	public String getJsapiConfig(){
		try {
			WebUser user = getUser();
			ParamsTable params = getParams();
			String url = params.getParameterAsString("_url");
			Map<String, String> config = WeixinServiceProxy.getJsapiConfig(url ,user.getDomainid());
			addActionResult(true, "", config);
		} catch (Exception e) {
			e.printStackTrace();
			addActionResult(false, e.getMessage(), null);
		}
		
		return SUCCESS;
	}
	
	public String doUpload(){
		try {
			System.err.print("jsapi 回调--》doUpload");
			WebUser user = getUser();
			ParamsTable params = getParams();
			String serverId = params.getParameterAsString("serverId");
			String folder  = params.getParameterAsString("folder");
			String fileType = params.getParameterAsString("fileType");
			folder = StringUtil.isBlank(folder)? "photo" :folder;
			fileType = StringUtil.isBlank(fileType)? "jpg" :fileType;
			//System.out.println("serverId--->"+serverId);
			String path = "/uploads/"+folder+"/"+UUID.randomUUID().toString()+"."+fileType;
			String fileName = ServletActionContext.getRequest().getRealPath(path);
			File dir = new File( ServletActionContext.getRequest().getRealPath("/uploads/"+folder));
			if(!dir.exists()){
				dir.mkdirs();
			}
			WeixinServiceProxy.downloadMedia(user.getDomainid(), serverId, fileName);
			//System.out.println("fileName--->"+fileName);
			
			addActionResult(true, "", path);
		} catch (Exception e) {
			
			e.printStackTrace();
			addActionResult(false, e.getMessage(), null);
		}
		
		return SUCCESS;
	}
	
	public String doPMUpload(){
		try {
			WebUser user = getUser();
			ParamsTable params = getParams();
			String serverId = params.getParameterAsString("serverId");
			String taskid = params.getParameterAsString("taskid");
			String folder  = params.getParameterAsString("folder");
			String fileType = params.getParameterAsString("fileType");
			String time = params.getParameterAsString("time");
			folder = StringUtil.isBlank(folder)? "photo" :folder;
			fileType = StringUtil.isBlank(fileType)? "jpg" :fileType;
			//System.out.println("serverId--->"+serverId);
			String path = "/uploads/"+folder+"/"+UUID.randomUUID().toString()+"."+fileType;
			String fileName = ServletActionContext.getRequest().getRealPath(path);
			File dir = new File( ServletActionContext.getRequest().getRealPath("/uploads/"+folder));
			if(!dir.exists()){
				dir.mkdirs();
			}
			WeixinServiceProxy.downloadMedia(user.getDomainid(), serverId, fileName);
			
			fileName = fileName.replace(".amr", ".mp3");
			
			File f2 = new File(fileName);
			Map<String, String> resultInfo = new HashMap<String, String>();
			Map<Object, Object> fileMap = new HashMap<Object, Object>();
			Map<Object, Object> fileMap2 = new HashMap<Object, Object>();
			String name = f2.getName();
			String id = UUID.randomUUID().toString();
			long size = f2.length();
			resultInfo.put("id", id);
			resultInfo.put("name", name);
			resultInfo.put("size", String.valueOf(size));
			resultInfo.put("taskid", taskid);
			// 生成文件名：
			resultInfo.put("url",path);

			fileMap.put("id", id);
			fileMap.put("name", name);
			fileMap.put("time", time);
			fileMap2.put(id, fileMap);

//			String attachjson = JsonUtil.toJson(fileMap2);
			//
			try {
				
//				TaskProcessBean process = new TaskProcessBean();
//				process.addAttachment(taskid, attachjson);
			} catch (Exception e) {
				e.printStackTrace();
			}
//			String result = JsonUtil.toJson(resultInfo);
//			System.out.println("attachjson-->" + attachjson);

			addActionResult(true, "", resultInfo);

			//System.out.println("fileName--->"+fileName);
		} catch (Exception e) {
			
			e.printStackTrace();
			addActionResult(false, e.getMessage(), null);
		}
		
		return SUCCESS;
	}
	
	
	
	
	
	
	
	
	
	public ParamsTable getParams() {
		if (params == null) {
			params = ParamsTable.convertHTTP(ServletActionContext.getRequest());

			params.setSessionid(ServletActionContext.getRequest().getSession().getId());

			if (params.getParameter("_pagelines") == null){
				params.setParameter("_pagelines", Web.DEFAULT_LINES_PER_PAGE);
			}
		}

		return params;
	}
	
	public WebUser getUser(){
		Map<String,Object> session = ActionContext.getContext().getSession();

		WebUser user = null;

		if (session == null || session.get(Web.SESSION_ATTRIBUTE_FRONT_USER) == null){
			if(isFromWeixin()){
				return getUserFromCookie();
			}else
				return null;
		}
		else{
			user = (WebUser) session.get(Web.SESSION_ATTRIBUTE_FRONT_USER);
		}

		return user;
	}
	
	/**
     * 是否来自微信
     * @param request
     * @return
     */
    private boolean isFromWeixin() {
    	HttpServletRequest request = ServletActionContext.getRequest();
    	String userAgent = request.getHeader("user-agent");
    	if(userAgent.contains("MicroMessenger")) return true;
    	return false;
    }
    
    private WebUser getUserFromCookie(){
    	HttpServletRequest request = ServletActionContext.getRequest();
    	Cookie[] cookies = request.getCookies();
		
		if(cookies == null) return null;
 		
		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			if("WEB_USER_COOKIE_KEY".equals(cookie.getName())){
				String value = Security.decryptPassword(cookie.getValue());
				if(value !=null){
					JSONObject jo = JSONObject.fromObject(value);
					String loginno = jo.getString("loginNo");
					if(!StringUtil.isBlank(loginno)){
						UserProcess process;
						try {
							process = (UserProcess)ProcessFactory.createProcess(UserProcess.class);
							UserVO user = (UserVO)process.login(loginno);
							if(user !=null){
								WebUser webUser = new WebUser(user);
								request.getSession().setAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER, webUser);
								return webUser;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
				}
				
			}
		}
    	
    	return null;
    }
	
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

}
