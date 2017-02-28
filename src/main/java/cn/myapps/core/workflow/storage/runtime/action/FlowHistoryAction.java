package cn.myapps.core.workflow.storage.runtime.action;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.constans.Web;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorRT;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowHistoryVO;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRT;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRTProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRT;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHISProcess;
import cn.myapps.km.util.StringUtil;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.Security;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class FlowHistoryAction extends ActionSupport {
	
	private static final long serialVersionUID = -1021941166716325952L;
	
	protected static final String ACTION_RESULT_KEY = "status";
	protected static final int ACTION_RESULT_VALUE_SUCCESS = 1;
	protected static final int ACTION_RESULT_VALUE_FAULT = 0;
	
	protected static final String ACTION_MESSAGE_KEY = "message";
	protected static final String ACTION_DATA_KEY = "data";
	
	
	protected ParamsTable params;
	
	//将会被Struts2序列化为JSON字符串的对象,用来作为控制层返回结果的容器
	protected Map<String, Object> dataMap = new HashMap<String, Object>();
	
	
	
	
	
	
	public String getHistory(){
		try {
			WebUser user = getUser();
			ParamsTable params = getParams();
			
			String flowStateId = params.getParameterAsString("_flowStateId");
			String applicationId = params.getParameterAsString("_applicationId");
			
			List<FlowHistoryVO> historys = Collections.EMPTY_LIST;
			Map<String, Object> result = new HashMap<String, Object>();
			
			FlowStateRTProcess flowStateRTProcess = (FlowStateRTProcess) ProcessFactory.createRuntimeProcess(FlowStateRTProcess.class, applicationId);
			RelationHISProcess hisProcess = (RelationHISProcess) ProcessFactory.createRuntimeProcess(RelationHISProcess.class,applicationId);
			
			FlowStateRT flowInstance = (FlowStateRT)flowStateRTProcess.doView(flowStateId);
			if(flowInstance != null){
				
				result.put("isComplete", flowInstance.isComplete());
				historys = (List)hisProcess.getFlowHistorysByFolowStateId(flowStateId);
				result.put("historys", historys);
				//Collections.reverse(historys);
			}
			
			addActionResult(true, "", result);
		} catch (Exception e) {
			e.printStackTrace();
			addActionResult(false, e.getMessage(), null);
		}
		
		return SUCCESS;
	}
	
	public String getFlowInstanceJson(){
		try {
			WebUser user = getUser();
			ParamsTable params = getParams();
			
			String flowStateId = params.getParameterAsString("_flowStateId");
			String applicationId = params.getParameterAsString("_applicationId");
			String curNodeId = params.getParameterAsString("_curNodeId");
			String curNodeName = params.getParameterAsString("_curNodeName");
			
			List<FlowHistoryVO> historys = Collections.EMPTY_LIST;
			Map<String, Object> result = new HashMap<String, Object>();
			
			FlowStateRTProcess flowStateRTProcess = (FlowStateRTProcess) ProcessFactory.createRuntimeProcess(FlowStateRTProcess.class, applicationId);
			RelationHISProcess hisProcess = (RelationHISProcess) ProcessFactory.createRuntimeProcess(RelationHISProcess.class,applicationId);
			
			FlowStateRT flowInstance = (FlowStateRT)flowStateRTProcess.doView(flowStateId);
			
			if(flowInstance != null){
				historys = (List)hisProcess.getFlowHistorysByFolowStateId(flowStateId);
				
				JSONArray flowInstances = new JSONArray(); 
				
				JSONObject hisNodeJson = new JSONObject();
				JSONArray nodeJson = new JSONArray(); 
				//添加该流程节点之前的流程信息
				for(FlowHistoryVO hs : historys){
					JSONObject fis = new JSONObject();
					fis.put("auditorId",hs.getAuditorId() );
					fis.put("auditorName",hs.getAuditorName() );
					fis.put("startNodeName",hs.getStartNodeName() );
					fis.put("processtime",hs.getProcesstime().toString());
					fis.put("currentNode","0");
					fis.put("flowOperation", hs.getFlowOperation());
					fis.put("isComplete", "true");
					nodeJson.add(fis);
				}
				hisNodeJson.put("nodeType", "hisNode");
				hisNodeJson.put("nodeJson", nodeJson);
				flowInstances.add(hisNodeJson);
				
				
				JSONObject curNodeJson = new JSONObject();
				JSONArray  _nodeJson = new JSONArray(); 
				
				if(flowInstance.isComplete()){
					JSONObject fis = new JSONObject();
					fis.put("auditorId","" );
					fis.put("auditorName","");
					fis.put("startNodeName",flowInstance.getStateLabel());
					fis.put("processtime",flowInstance.getLastModified().toString());
					fis.put("currentNode","1");
					fis.put("flowOperation", "");
					fis.put("isComplete", "true");
					_nodeJson.add(fis);
				}else{
				//添加当前结点的流程信息 两种状态：非处理人和处理人
				if(!StringUtil.isBlank(curNodeId) && !StringUtil.isBlank(curNodeName)){  // 处理人状态，显示处理人信息
					JSONObject fis = new JSONObject();
					fis.put("auditorId",user.getId() );
					fis.put("auditorName",user.getName());
					fis.put("startNodeName",curNodeName);
					fis.put("processtime","");
					fis.put("currentNode","1");
					fis.put("flowOperation", "");
					fis.put("isComplete", "false");
					_nodeJson.add(fis);
				}else{                                                       //非处理人状态，显示当前节点所有处理人信息
					Collection<NodeRT> noderts = flowInstance.getNoderts();
					for(NodeRT nrt : noderts){
						Collection<ActorRT> actorrts = nrt.getActorrts();
						for(ActorRT art : actorrts){
							JSONObject fis = new JSONObject();
							fis.put("auditorId",art.getActorid());
							fis.put("auditorName",art.getName());
							fis.put("startNodeName",nrt.getStatelabel());
							fis.put("processtime","");
							fis.put("currentNode","1");
							fis.put("flowOperation", "");
							fis.put("isComplete", "false");
							_nodeJson.add(fis);
						}
					}
				}
				}
				curNodeJson.put("nodeType", "curNode");
				curNodeJson.put("nodeJson", _nodeJson);
				flowInstances.add(curNodeJson);
				
				result.put("historys", flowInstances);
			}
			
			addActionResult(true, "", result);
		} catch (Exception e) {
			e.printStackTrace();
			addActionResult(false, e.getMessage(), null);
		}
		
		return SUCCESS;
	}
	
	public String doUpload(){
		try {
			WebUser user = getUser();
			ParamsTable params = getParams();
			String serverId = params.getParameterAsString("serverId");
			System.out.println("serverId--->"+serverId);
			addActionResult(true, "", "");
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
