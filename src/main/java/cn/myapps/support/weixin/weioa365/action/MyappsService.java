package cn.myapps.support.weixin.weioa365.action;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;















import com.opensymphony.xwork2.ActionSupport;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.constans.Environment;
import cn.myapps.constans.Web;
import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.fieldextends.ejb.FieldExtendsProcess;
import cn.myapps.core.fieldextends.ejb.FieldExtendsVO;
import cn.myapps.core.role.ejb.RoleProcess;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.core.user.ejb.UserDepartmentSet;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserRoleSet;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workcalendar.calendar.ejb.CalendarProcess;
import cn.myapps.core.workcalendar.calendar.ejb.CalendarVO;
import cn.myapps.km.util.StringUtil;
import cn.myapps.util.ProcessFactory;

public class MyappsService extends ActionSupport {
	
	private static final long serialVersionUID = -1021941166716325951L;
	/**
	 * 微OA365反向代理模式外网访问地址
	 */
	private static final String weioa365_addr = "https://yun.weioa365.com/{site_id}";
	
	protected static final String ACTION_RESULT_KEY = "status";
	protected static final int ACTION_RESULT_VALUE_SUCCESS = 1;
	protected static final int ACTION_RESULT_VALUE_FAULT = 0;
	
	protected static final String ACTION_MESSAGE_KEY = "message";
	protected static final String ACTION_DATA_KEY = "data";
	
	
	protected ParamsTable params;
	
	//将会被Struts2序列化为JSON字符串的对象,用来作为控制层返回结果的容器
	protected Map<String, Object> dataMap = new HashMap<String, Object>();
	
	
	
	
	
	
	/**
	 * 获取企业域下的所有部门集合
	 * @return
	 */
	public String getDepartments(){
		try {
			Collection<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			ParamsTable params = getParams();
			String siteId = params.getParameterAsString("site_id");
			String domainId = params.getParameterAsString("domain_id");
			
			DepartmentProcess process = (DepartmentProcess)ProcessFactory.createProcess(DepartmentProcess.class);
			Collection<DepartmentVO> depts = new ArrayList<DepartmentVO>();
			depts.addAll(process.doQueryByHQL("from "+DepartmentVO.class.getName()+" vo where vo.domain.id='"+domainId+"'  order by vo.level asc,vo.orderByNo", 1, Integer.MAX_VALUE));
			for (Iterator<DepartmentVO> iterator = depts.iterator(); iterator.hasNext();) {
				DepartmentVO dept = iterator.next();
				list.add(department2Map(dept));
			}
			
			addActionResult(true, "", list);
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String updateDepartment(){
		try {
			DepartmentProcess process = (DepartmentProcess)ProcessFactory.createProcess(DepartmentProcess.class);
			ParamsTable params = getParams();
			String siteId = params.getParameterAsString("site_id");
			String domainId = params.getParameterAsString("domain_id");
			
			String postDate = getPostDataAsString();
			
			if(!StringUtil.isBlank(postDate)){
				JSONObject dept = JSONObject.fromObject(postDate);
					String id = dept.getString("id");
					DepartmentVO vo = (DepartmentVO) process.doView(id);
					if(vo !=null){
						vo.setName(dept.getString("name"));
						vo.setWeixinDeptId(dept.getString("weixinDeptId"));
						String superior = dept.getString("superior");
						if(!StringUtil.isBlank(superior)){
							DepartmentVO superiorVO = (DepartmentVO) process.doView(superior);
							vo.setSuperior(superiorVO);
						}
						process.doUpdate(vo);
					}
				
			}
			addActionResult(true, "", "success");
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String createDepartment(){
		try {
			DepartmentProcess process = (DepartmentProcess)ProcessFactory.createProcess(DepartmentProcess.class);
			DomainProcess domainProcess = (DomainProcess) ProcessFactory.createProcess(DomainProcess.class);
			
			ParamsTable params = getParams();
			String siteId = params.getParameterAsString("site_id");
			String domainId = params.getParameterAsString("domain_id");
			
			DomainVO domain = (DomainVO) domainProcess.doView(domainId);
			
			String postDate = getPostDataAsString();
			
			if(!StringUtil.isBlank(postDate)){
				JSONObject dept = JSONObject.fromObject(postDate);
					String id = dept.getString("id");
					DepartmentVO d = new DepartmentVO();
					d.setId(id);
					d.setName(dept.getString("name"));
					d.setLevel(dept.getInt("level"));
					d.setDomain(domain);
					d.setDomainid(domainId);
					d.setWeixinDeptId(dept.getString("weixinDeptId"));
					String superior = dept.getString("superior");
					if(!StringUtil.isBlank(superior)){
						DepartmentVO superiorVO = (DepartmentVO) process.doView(superior);
						d.setSuperior(superiorVO);
					}
						process.doCreate(d);
						
					}
			addActionResult(true, "", "success");
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String updateDepartments(){
		try {
			DepartmentProcess process = (DepartmentProcess)ProcessFactory.createProcess(DepartmentProcess.class);
			ParamsTable params = getParams();
			String siteId = params.getParameterAsString("site_id");
			String domainId = params.getParameterAsString("domain_id");
			
			String postDate = getPostDataAsString();
			
			if(!StringUtil.isBlank(postDate)){
				JSONArray arr = JSONArray.fromObject(postDate);
				for (Iterator<JSONObject> iterator = arr.iterator(); iterator.hasNext();) {
					JSONObject dept = (JSONObject) iterator.next();
					String id = dept.getString("id");
					DepartmentVO vo = (DepartmentVO) process.doView(id);
					if(vo ==null) continue;
					vo.setName(dept.getString("name"));
					vo.setWeixinDeptId(dept.getString("weixinDeptId"));
					String superior = dept.getString("superior");
					if(!StringUtil.isBlank(superior)){
						DepartmentVO superiorVO = (DepartmentVO) process.doView(superior);
						vo.setSuperior(superiorVO);
					}
					process.doUpdate(vo);
				}
				
			}
			addActionResult(true, "", "success");
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	
	public String getDepartmentsByLevel(){
		try {
			Collection<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			ParamsTable params = getParams();
			String siteId = params.getParameterAsString("site_id");
			String domainId = params.getParameterAsString("domain_id");
			int level = params.getParameterAsInteger("level");
			
			DepartmentProcess process = (DepartmentProcess)ProcessFactory.createProcess(DepartmentProcess.class);
			Collection<DepartmentVO> depts = new ArrayList<DepartmentVO>();
			depts.addAll(process.getDepartmentByLevel(level, "", domainId));
			for (Iterator<DepartmentVO> iterator = depts.iterator(); iterator.hasNext();) {
				DepartmentVO dept = iterator.next();
				list.add(department2Map(dept));
			}
			
			addActionResult(true, "", list);
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String getDepartmentsByLevelAndName(){
		try {
			Collection<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			ParamsTable params = getParams();
			String siteId = params.getParameterAsString("site_id");
			String domainId = params.getParameterAsString("domain_id");
			String postDate = getPostDataAsString();
			if(!StringUtil.isBlank(postDate)){
				JSONObject jo = JSONObject.fromObject(postDate);
				Map<String, Object> map = (Map<String, Object>) JSONObject.toBean(jo, Map.class);
				params.putAll(map);
			}
			
			int level = Integer.valueOf(params.getParameterAsString("level"));
			String name = params.getParameterAsString("name");
			
			DepartmentProcess process = (DepartmentProcess)ProcessFactory.createProcess(DepartmentProcess.class);
			Collection<DepartmentVO> depts = new ArrayList<DepartmentVO>();
			depts.add(process.getDepartmentByNameAndLevel(name, level, domainId));
			for (Iterator<DepartmentVO> iterator = depts.iterator(); iterator.hasNext();) {
				DepartmentVO dept = iterator.next();
				list.add(department2Map(dept));
			}
			
			addActionResult(true, "", list);
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String getDepartmentsByParams(){
		try {
			Collection<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			ParamsTable params = getParams();
			String siteId = params.getParameterAsString("site_id");
			String domainId = params.getParameterAsString("domain_id");
			
			String postDate = getPostDataAsString();
			if(!StringUtil.isBlank(postDate)){
				JSONObject jo = JSONObject.fromObject(postDate);
				Map<String, Object> map = (Map<String, Object>) JSONObject.toBean(jo, Map.class);
				params.putAll(map);
			}
			
			params.setParameter("t_domain.id", domainId);
			DepartmentProcess process = (DepartmentProcess)ProcessFactory.createProcess(DepartmentProcess.class);
			Collection<DepartmentVO> depts = new ArrayList<DepartmentVO>();
			depts.addAll(process.doSimpleQuery(params));
			for (Iterator<DepartmentVO> iterator = depts.iterator(); iterator.hasNext();) {
				DepartmentVO dept = iterator.next();
				list.add(department2Map(dept));
			}
			addActionResult(true, "", list);
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	
	public String getUsersByParams(){
		try {
			Collection<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			ParamsTable params = getParams();
			String siteId = params.getParameterAsString("site_id");
			String domainId = params.getParameterAsString("domain_id");
			params.setParameter("domain", domainId);
			String postDate = getPostDataAsString();
			if(!StringUtil.isBlank(postDate)){
				JSONObject jo = JSONObject.fromObject(postDate);
				Map<String, Object> map = (Map<String, Object>) JSONObject.toBean(jo, Map.class);
				params.putAll(map);
			}
			
			UserProcess process = (UserProcess)ProcessFactory.createProcess(UserProcess.class);
			Collection<UserVO> users = new ArrayList<UserVO>();
			users.addAll(process.doSimpleQuery(params));
			for (Iterator<UserVO> iterator = users.iterator(); iterator.hasNext();) {
				UserVO u = iterator.next();
				list.add(user2Map(u));
			}
			addActionResult(true, "", list);
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String updateUser(){
		try {
			UserProcess process = (UserProcess)ProcessFactory.createProcess(UserProcess.class);
			ParamsTable params = getParams();
			String siteId = params.getParameterAsString("site_id");
			String domainId = params.getParameterAsString("domain_id");
			
			String postDate = getPostDataAsString();
			
			if(!StringUtil.isBlank(postDate)){
				JSONObject user = JSONObject.fromObject(postDate);
				String id = user.getString("id");
				UserVO vo = (UserVO) process.doView(id);
				if(vo !=null){
					vo.setName(user.getString("name"));
					vo.setEmail(user.getString("email"));
					vo.setTelephone(user.getString("telephone"));
					vo.setAvatar(user.getString("avatar"));
					
					//更新拓展字段
					if(!StringUtils.isBlank(user.getString("extattr"))){
						
						FieldExtendsProcess fieldExtendsProcess = (FieldExtendsProcess) ProcessFactory.createProcess(FieldExtendsProcess.class);
						List<FieldExtendsVO> fieldExtendses = fieldExtendsProcess.queryFieldExtendsByTable(domainId, FieldExtendsVO.TABLE_USER);
						List<FieldExtendsVO> fes = new ArrayList<FieldExtendsVO>();
						for(int i=0;i<fieldExtendses.size();i++){
							FieldExtendsVO fevo = fieldExtendses.get(i);
							if(fevo != null && fevo.getEnabel()){
								fes.add(fevo);
							}
						}
						
						if(user.containsKey("extattr") && !fes.isEmpty()){
							JSONObject extattr =  JSONObject.fromObject(user.getString("extattr"));
							JSONArray attrs = extattr.getJSONArray("attrs");
							for (Iterator<FieldExtendsVO> iterator3 = fes.iterator(); iterator3.hasNext();) {
								FieldExtendsVO fe = iterator3.next();
								String name = fe.getLabel();
								for (Iterator<JSONObject> iterator4 = attrs.iterator(); iterator4.hasNext();) {
									JSONObject attr = iterator4.next();
									if(name.equals(attr.getString("name"))){
										String fieldName = fe.getName();
										fieldName = fieldName.replaceFirst("f","F");
										Method method = UserVO.class.getMethod("set" + fieldName,String.class);
										method.invoke(vo,attr.getString("value"));
										break;
									}
								}
							}
						}
					}
					process.doUpdate(vo);
				}
				
			}
			addActionResult(true, "", "success");
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String createUser(){
		try {
			UserProcess process = (UserProcess)ProcessFactory.createProcess(UserProcess.class);
			DepartmentProcess deptprocess = (DepartmentProcess)ProcessFactory.createProcess(DepartmentProcess.class);
			DomainProcess domainProcess = (DomainProcess) ProcessFactory.createProcess(DomainProcess.class);
			CalendarProcess calendarProcess = (CalendarProcess)ProcessFactory.createProcess(CalendarProcess.class);
			
		
			ParamsTable params = getParams();
			String siteId = params.getParameterAsString("site_id");
			String domainId = params.getParameterAsString("domain_id");
			
			DomainVO domain = (DomainVO) domainProcess.doView(domainId);
			List<CalendarVO> calendars = (List<CalendarVO>) calendarProcess.doQueryByHQL("from "+CalendarVO.class.getName()+" vo where vo.name='Standard_Calendar' and vo.domainid='"+domain.getId()+"'", 1, 1);
			CalendarVO calendar = calendars.isEmpty()? null : calendars.get(0);
			
			
			FieldExtendsProcess fieldExtendsProcess = (FieldExtendsProcess) ProcessFactory.createProcess(FieldExtendsProcess.class);
			List<FieldExtendsVO> fieldExtendses = fieldExtendsProcess.queryFieldExtendsByTable(domain.getId(), FieldExtendsVO.TABLE_USER);
			List<FieldExtendsVO> fes = new ArrayList<FieldExtendsVO>();
			
			for(int i=0;i<fieldExtendses.size();i++){
				FieldExtendsVO fevo = fieldExtendses.get(i);
				if(fevo != null && fevo.getEnabel()){
					fes.add(fevo);
				}
			}
			
			String postDate = getPostDataAsString();
			
			if(!StringUtil.isBlank(postDate)){
				JSONObject u = JSONObject.fromObject(postDate);
				String id = u.getString("id");
				UserVO user = new UserVO();
				user.setId(id);
				user.setName(u.getString("name"));
				user.setTelephone(u.getString("telephone"));
				user.setEmail(u.getString("email"));
				user.setAvatar(u.getString("avatar"));
				user.setLoginno(u.getString("loginno"));
				user.setLoginpwd(u.getString("loginpwd"));
				user.setDomainid(domainId);
				user.setDimission(UserVO.ONJOB);
				String deptId = u.getString("defaultDepartment");
				if(!StringUtil.isBlank(deptId)){
					Collection<UserDepartmentSet> depts = user.getUserDepartmentSets();
					UserDepartmentSet set = new UserDepartmentSet(id, deptId);
					depts.add(set);
					user.setUserDepartmentSets(depts);
					user.setDefaultDepartment(deptId);
				}
				
				//设置拓展字段
				if(u.containsKey("extattr")){
					JSONObject extattr = u.getJSONObject("extattr");
					if(extattr != null){
						if(!fes.isEmpty()){
							JSONArray attrs = extattr.getJSONArray("attrs");
							for (Iterator<FieldExtendsVO> iterator3 = fes.iterator(); iterator3
									.hasNext();) {
								FieldExtendsVO fe = iterator3.next();
								String name = fe.getLabel();
								for (Iterator<JSONObject> iterator4 = attrs.iterator(); iterator4
										.hasNext();) {
									JSONObject attr = iterator4.next();
									if(name.equals(attr.getString("name"))){
										String fieldName = fe.getName();
										fieldName = fieldName.replaceFirst("f","F");
										Method method = UserVO.class.getMethod("set" + fieldName,String.class);
										method.invoke(user,attr.getString("value"));
										break;
									}
								}
							}
						}
					}
			}
				
				//设置默认角色
				RoleProcess rp = (RoleProcess) ProcessFactory.createProcess(RoleProcess.class);
				Collection<ApplicationVO> apps = domain.getApplications();
				
				for(Iterator<ApplicationVO> app_its = apps.iterator(); app_its.hasNext();){
					ApplicationVO app = app_its.next();
					if(app.isActivated()){
						//获取软件下系统默认角色
						Collection<RoleVO> roles = rp.getDefaultRolesByApplication(app.getApplicationid());
						if(roles != null && roles.size() > 0 ){
							for(Iterator<RoleVO> it = roles.iterator(); it.hasNext();){
								RoleVO role = it.next();
								UserRoleSet set = new UserRoleSet(user.getId(), role.getId());
								user.getUserRoleSets().add(set);
							}
						}
					}
				}
				
				user.setCalendarType(calendar !=null? calendar.getId() : null);
				process.doCreate(user);
					
				}
			addActionResult(true, "", "success");
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	
	/**
	 * 获取默认套件应用菜单
	 * @return
	 */
	public String getDefaultAppMenus(){
		try {
			String redirect_uri = null;
			ParamsTable params = getParams();
			String siteId = params.getParameterAsString("site_id");
			String domainId = params.getParameterAsString("domain_id");
			Map<String,JSONArray> agentMemus = new LinkedHashMap<String, JSONArray>();
			
			JSONArray txlMenu = new JSONArray();
			JSONObject txlItem1 = new JSONObject();
			txlItem1.put("name", "通讯录");
			txlItem1.put("type", "view");
			txlItem1.put("url","https://open.weixin.qq.com/connect/oauth2/authorize?appid={appid}&redirect_uri={client_addr}/contacts/index.jsp?SET_COOKIE_SITEID={site_id}&response_type=code&scope=snsapi_base&state={domainId}#wechat_redirect"); 
			txlMenu.add(txlItem1);
			
			JSONObject txlItem2 = new JSONObject();
			txlItem2.put("name", "常用");
			txlItem2.put("type", "view");
			txlItem2.put("url","https://open.weixin.qq.com/connect/oauth2/authorize?appid={appid}&redirect_uri={client_addr}/contacts/index.jsp?action=favoriteContacts&SET_COOKIE_SITEID={site_id}&response_type=code&scope=snsapi_base&state={domainId}#wechat_redirect"); 
			txlMenu.add(txlItem2);
			
//			txlMenu.put("通讯录", "https://open.weixin.qq.com/connect/oauth2/authorize?appid={appid}&redirect_uri={client_addr}/contacts/index.jsp?SET_COOKIE_SITEID={site_id}&response_type=code&scope=snsapi_base&state={domainId}#wechat_redirect");
//			txlMenu.put("常用", "https://open.weixin.qq.com/connect/oauth2/authorize?appid={appid}&redirect_uri={client_addr}/contacts/index.jsp?action=favoriteContacts&SET_COOKIE_SITEID={site_id}&response_type=code&scope=snsapi_base&state={domainId}#wechat_redirect");
			agentMemus.put("2", txlMenu);
			
			
			JSONArray kqMenu = new JSONArray();
			JSONObject kqItem1 = new JSONObject();
			kqItem1.put("name", "签到");
			kqItem1.put("type", "view");
			kqItem1.put("url","https://open.weixin.qq.com/connect/oauth2/authorize?appid={appid}&redirect_uri={client_addr}/attendance/sign.jsp?action=signin&SET_COOKIE_SITEID={site_id}&response_type=code&scope=snsapi_base&state={domainId}#wechat_redirect");
			kqMenu.add(kqItem1);
			
			JSONObject kqItem2 = new JSONObject();
			kqItem2.put("name", "签退");
			kqItem2.put("type", "view");
			kqItem2.put("url","https://open.weixin.qq.com/connect/oauth2/authorize?appid={appid}&redirect_uri={client_addr}/attendance/sign.jsp?action=signout&SET_COOKIE_SITEID={site_id}&response_type=code&scope=snsapi_base&state={domainId}#wechat_redirect");
			kqMenu.add(kqItem2);
			
			JSONObject kqItem3 = new JSONObject();
			kqItem3.put("name", "考勤记录");
			kqItem3.put("type", "view");
			kqItem3.put("url","https://open.weixin.qq.com/connect/oauth2/authorize?appid={appid}&redirect_uri={client_addr}/attendance/record.jsp?SET_COOKIE_SITEID={site_id}&response_type=code&scope=snsapi_base&state={domainId}#wechat_redirect");
			kqMenu.add(kqItem3);
			
//			Map<String, String> kqMenu = new LinkedHashMap<String, String>();
//			kqMenu.put("签到", "https://open.weixin.qq.com/connect/oauth2/authorize?appid={appid}&redirect_uri={client_addr}/attendance/sign.jsp?action=signin&SET_COOKIE_SITEID={site_id}&response_type=code&scope=snsapi_base&state={domainId}#wechat_redirect");
//			kqMenu.put("签退", "https://open.weixin.qq.com/connect/oauth2/authorize?appid={appid}&redirect_uri={client_addr}/attendance/sign.jsp?action=signout&SET_COOKIE_SITEID={site_id}&response_type=code&scope=snsapi_base&state={domainId}#wechat_redirect");
//			kqMenu.put("考勤记录", "https://open.weixin.qq.com/connect/oauth2/authorize?appid={appid}&redirect_uri={client_addr}/attendance/record.jsp?SET_COOKIE_SITEID={site_id}&response_type=code&scope=snsapi_base&state={domainId}#wechat_redirect");
			agentMemus.put("4", kqMenu);
			
			JSONArray pmMenu = new JSONArray();
			JSONObject pmItem1 = new JSONObject();
			pmItem1.put("name", "新建任务");
			pmItem1.put("type", "view");
			pmItem1.put("url","https://open.weixin.qq.com/connect/oauth2/authorize?appid={appid}&redirect_uri={client_addr}/pm/wap/index.jsp?action=createTask&SET_COOKIE_SITEID={site_id}&response_type=code&scope=snsapi_base&state={domainId}#wechat_redirect");
			pmMenu.add(pmItem1);
			
			JSONObject pmItem2 = new JSONObject();
			pmItem2.put("name", "任务中心");
			pmItem2.put("type", "view");
			pmItem2.put("url","https://open.weixin.qq.com/connect/oauth2/authorize?appid={appid}&redirect_uri={client_addr}/pm/wap/index.jsp?action=taskList&SET_COOKIE_SITEID={site_id}&response_type=code&scope=snsapi_base&state={domainId}#wechat_redirect");
			pmMenu.add(pmItem2);
			
			JSONObject pmItem3 = new JSONObject();
			pmItem3.put("name", "项目");
			pmItem3.put("type", "view");
			pmItem3.put("url","https://open.weixin.qq.com/connect/oauth2/authorize?appid={appid}&redirect_uri={client_addr}/pm/wap/index.jsp?action=project&SET_COOKIE_SITEID={site_id}&response_type=code&scope=snsapi_base&state={domainId}#wechat_redirect");
			pmMenu.add(pmItem3);
			
			agentMemus.put("3", pmMenu);
			
			JSONArray erpMenu = new JSONArray();
			JSONObject erpItem1 = new JSONObject();
			erpItem1.put("name", "报表");
			erpItem1.put("type", "view");
			erpItem1.put("url", "https://open.weixin.qq.com/connect/oauth2/authorize?appid={appid}&redirect_uri={client_addr}/extendedReport/erpReport/index.jsp?SET_COOKIE_SITEID={site_id}&response_type=code&scope=snsapi_base&state={domainId}#wechat_redirect");
			erpMenu.add(erpItem1);
			
			JSONObject erpItem2 = new JSONObject();
			erpItem2.put("name", "数据查询");
			erpItem2.put("type", "view");
			erpItem2.put("url", "https://open.weixin.qq.com/connect/oauth2/authorize?appid={appid}&redirect_uri={client_addr}/extendedReport/erpSearch/searchDept.jsp?SET_COOKIE_SITEID={site_id}&response_type=code&scope=snsapi_base&state={domainId}#wechat_redirect");
			erpMenu.add(erpItem2);
			
			JSONObject erpItem3 = new JSONObject();
			erpItem3.put("name", "订单");
			
			JSONArray erpItem3_subMenus = new JSONArray();
			JSONObject erpItem3_subItem1 =  new JSONObject();
			erpItem3_subItem1.put("name", "采购订单");
			erpItem3_subItem1.put("type", "view");
			erpItem3_subItem1.put("url","https://open.weixin.qq.com/connect/oauth2/authorize?appid={appid}&redirect_uri={client_addr}/extendedReport/erpOrder/orderPurchase.jsp?SET_COOKIE_SITEID={site_id}&response_type=code&scope=snsapi_base&state={domainId}#wechat_redirect");
			erpItem3_subMenus.add(erpItem3_subItem1);
			JSONObject erpItem3_subItem2 =  new JSONObject();
			erpItem3_subItem2.put("name", "销售订单");
			erpItem3_subItem2.put("type", "view");
			erpItem3_subItem2.put("url","https://open.weixin.qq.com/connect/oauth2/authorize?appid={appid}&redirect_uri={client_addr}/extendedReport/erpOrder/orderSale.jsp?SET_COOKIE_SITEID={site_id}&response_type=code&scope=snsapi_base&state={domainId}#wechat_redirect");
			erpItem3_subMenus.add(erpItem3_subItem2);
			
			erpItem3.put("sub_button", erpItem3_subMenus);
			erpMenu.add(erpItem3);
			
			agentMemus.put("5", erpMenu);
			
			
			JSONArray oaMenu = new JSONArray();
			JSONObject oaItem1 = new JSONObject();
			oaItem1.put("name", "主页");
			oaItem1.put("type", "view");
			oaItem1.put("url","https://open.weixin.qq.com/connect/oauth2/authorize?appid={appid}&redirect_uri={client_addr}/portal/share/index.jsp?application=11de-f053-df18d577-aeb6-19a7865cfdb6&SET_COOKIE_SITEID={site_id}&response_type=code&scope=snsapi_base&state={domainId}#wechat_redirect");
			oaMenu.add(oaItem1);
			
			JSONObject oaItem2 = new JSONObject();
			oaItem2.put("name", "流程");
			oaItem2.put("type", "view");
			redirect_uri = URLEncoder.encode((weioa365_addr+"/portal/share/index.jsp?application=11de-f053-df18d577-aeb6-19a7865cfdb6&action=flowCenter&SET_COOKIE_SITEID={site_id}").replace("{site_id}", Environment.getMACAddress()),"utf-8");
			oaItem2.put("url","https://open.weixin.qq.com/connect/oauth2/authorize?appid={appid}&redirect_uri="+redirect_uri+"&response_type=code&scope=snsapi_base&state={domainId}#wechat_redirect");
			oaMenu.add(oaItem2);
			
			JSONObject oaItem3 = new JSONObject();
			oaItem3.put("name", "菜单");
			oaItem3.put("type", "view");
			redirect_uri = URLEncoder.encode((weioa365_addr+"/portal/share/index.jsp?application=11de-f053-df18d577-aeb6-19a7865cfdb6&action=menu&SET_COOKIE_SITEID={site_id}").replace("{site_id}", Environment.getMACAddress()),"utf-8");
			oaItem3.put("url","https://open.weixin.qq.com/connect/oauth2/authorize?appid={appid}&redirect_uri="+redirect_uri+"&response_type=code&scope=snsapi_base&state={domainId}#wechat_redirect");
			oaMenu.add(oaItem3);
			
			agentMemus.put("1", oaMenu);
			
			JSONArray crmMenu = new JSONArray();
			JSONObject crmItem1 = new JSONObject();
			crmItem1.put("name", "主页");
			crmItem1.put("type", "view");
			crmItem1.put("url","https://open.weixin.qq.com/connect/oauth2/authorize?appid={appid}&redirect_uri={client_addr}/portal/share/index.jsp?application=11e6-429d-dd7a3284-86a2-074015f7cc96&SET_COOKIE_SITEID={site_id}&response_type=code&scope=snsapi_base&state={domainId}#wechat_redirect");
			crmMenu.add(crmItem1);
			
			JSONObject crmItem2 = new JSONObject();
			crmItem2.put("name", "流程");
			crmItem2.put("type", "view");
			redirect_uri = URLEncoder.encode((weioa365_addr+"/portal/share/index.jsp?application=11e6-429d-dd7a3284-86a2-074015f7cc96&action=flowCenter&SET_COOKIE_SITEID={site_id}").replace("{site_id}", Environment.getMACAddress()),"utf-8");
			crmItem2.put("url","https://open.weixin.qq.com/connect/oauth2/authorize?appid={appid}&redirect_uri="+redirect_uri+"&response_type=code&scope=snsapi_base&state={domainId}#wechat_redirect");
			crmMenu.add(crmItem2);
			
			JSONObject crmItem3 = new JSONObject();
			crmItem3.put("name", "菜单");
			crmItem3.put("type", "view");
			redirect_uri = URLEncoder.encode((weioa365_addr+"/portal/share/index.jsp?application=11e6-429d-dd7a3284-86a2-074015f7cc96&action=menu&SET_COOKIE_SITEID={site_id}").replace("{site_id}", Environment.getMACAddress()),"utf-8");
			crmItem3.put("url","https://open.weixin.qq.com/connect/oauth2/authorize?appid={appid}&redirect_uri="+redirect_uri+"&response_type=code&scope=snsapi_base&state={domainId}#wechat_redirect");
			crmMenu.add(crmItem3);
			
			agentMemus.put("6", crmMenu);
			
			JSONArray xmglMenu = new JSONArray();
			JSONObject xmglItem1 = new JSONObject();
			xmglItem1.put("name", "新建日报");
			xmglItem1.put("type", "view");
			redirect_uri = URLEncoder.encode((weioa365_addr+"/portal/dynaform/document/newWithPermission.action?_formid=11e6-27bc-7bd880cf-a1cc-0ff3b6c87f18&_isJump=1&application=11e6-2d1c-908d1c9b-abde-35dbfa83876a&_resourceid=11e6-3119-2a152176-bd0b-3f8085c95eef&SET_COOKIE_SITEID={site_id}").replace("{site_id}", Environment.getMACAddress()),"utf-8");
			xmglItem1.put("url","https://open.weixin.qq.com/connect/oauth2/authorize?appid={appid}&redirect_uri="+redirect_uri+"&response_type=code&scope=snsapi_base&state={domainId}#wechat_redirect");
			xmglMenu.add(xmglItem1);
			
			JSONObject xmglItem2 = new JSONObject();
			xmglItem2.put("name", "日报查询");
			xmglItem2.put("type", "view");
			redirect_uri = URLEncoder.encode((weioa365_addr+"/portal/dynaform/view/displayViewWithPermission.action?_viewid=11e6-27c6-8b8229f7-a1cc-0ff3b6c87f18&clearTemp=true&application=11e6-2d1c-908d1c9b-abde-35dbfa83876a&_resourceid=11e6-27c9-0464e6f5-a1cc-0ff3b6c87f18&SET_COOKIE_SITEID={site_id}").replace("{site_id}", Environment.getMACAddress()),"utf-8");
			xmglItem2.put("url","https://open.weixin.qq.com/connect/oauth2/authorize?appid={appid}&redirect_uri="+redirect_uri+"&response_type=code&scope=snsapi_base&state={domainId}#wechat_redirect");
			xmglMenu.add(xmglItem2);
			
			JSONObject xmglItem3 = new JSONObject();
			xmglItem3.put("name", "我的项目");
			xmglItem3.put("type", "view");
			redirect_uri = URLEncoder.encode((weioa365_addr+"/portal/dynaform/view/displayViewWithPermission.action?_viewid=11e6-27bf-1f641c86-a1cc-0ff3b6c87f18&clearTemp=true&application=11e6-2d1c-908d1c9b-abde-35dbfa83876a&_resourceid=11e6-27c8-bf70c5a1-a1cc-0ff3b6c87f18&SET_COOKIE_SITEID={site_id}").replace("{site_id}", Environment.getMACAddress()),"utf-8");
			xmglItem3.put("url","https://open.weixin.qq.com/connect/oauth2/authorize?appid={appid}&redirect_uri="+redirect_uri+"&response_type=code&scope=snsapi_base&state={domainId}#wechat_redirect");
			xmglMenu.add(xmglItem3);
			
			agentMemus.put("11", xmglMenu);
			
			JSONArray qmMenu = new JSONArray();
			JSONObject qmItem1 = new JSONObject();
			qmItem1.put("name", "待填写");
			qmItem1.put("type", "view");
			redirect_uri = URLEncoder.encode((weioa365_addr+"/qm/wap/pendlist.jsp?SET_COOKIE_SITEID={site_id}").replace("{site_id}", Environment.getMACAddress()),"utf-8");
			qmItem1.put("url","https://open.weixin.qq.com/connect/oauth2/authorize?appid={appid}&redirect_uri="+redirect_uri+"&response_type=code&scope=snsapi_base&state={domainId}#wechat_redirect");
			qmMenu.add(qmItem1);
			
			JSONObject qmItem2 = new JSONObject();
			qmItem2.put("name", "问卷中心");
			qmItem2.put("type", "view");
			redirect_uri = URLEncoder.encode((weioa365_addr+"/qm/wap/center.jsp?SET_COOKIE_SITEID={site_id}").replace("{site_id}", Environment.getMACAddress()),"utf-8");
			qmItem2.put("url","https://open.weixin.qq.com/connect/oauth2/authorize?appid={appid}&redirect_uri="+redirect_uri+"&response_type=code&scope=snsapi_base&state={domainId}#wechat_redirect");
			qmMenu.add(qmItem2);
			
			agentMemus.put("12", qmMenu);
			
			addActionResult(true, "", agentMemus);
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	
	public String updateWeixinProxyType(){
		try {
			DomainProcess process = (DomainProcess)ProcessFactory.createProcess(DomainProcess.class);
			ParamsTable params = getParams();
			String siteId = params.getParameterAsString("site_id");
			String domainId = params.getParameterAsString("domain_id");
			String weixinProxyType = params.getParameterAsString("weixinProxyType");
			
			DomainVO vo = (DomainVO) process.doView(domainId);
			if(vo!=null){
				vo.setWeixinProxyType(weixinProxyType);
				process.doUpdate(vo);
			}
			addActionResult(true, "", "success");
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String upload(){
		InputStream is = null;
		FileOutputStream os = null;
		try {
			ParamsTable params = getParams();
			String fileName = params.getParameterAsString("fileName");
			String base64File = getPostDataAsString();
			if(!StringUtils.isBlank(base64File)){
				byte[] data = new sun.misc.BASE64Decoder().decodeBuffer(base64File);
				os = new FileOutputStream(fileName);
				os.write(data);
				os.close();
				
				if(fileName.toLowerCase().endsWith(".amr")){
					changeToMp3(fileName, fileName.replace(".amr", ".mp3"));
				}
			}
			addActionResult(true, "", "success");
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 把amr格式的语音转换成MP3
	 * @Title: changeToMp3 
	 * @Description: (把amr格式的语音转换成MP3) 
	 * @author  pll
	 * @param @param sourcePath amr格式文件路径
	 * @param @param targetPath 存放mp3格式文件路径 
	 * @return void 返回类型 
	 * @throws
	 */
	private void changeToMp3(String sourcePath, String targetPath) {
		File source = new File(sourcePath);  
		File target = new File(targetPath);  
		AudioAttributes audio = new AudioAttributes();  
		Encoder encoder = new Encoder();  

		audio.setCodec("libmp3lame");  
		EncodingAttributes attrs = new EncodingAttributes();  
		attrs.setFormat("mp3");  
		attrs.setAudioAttributes(audio);  
		try {  
			encoder.encode(source, target, attrs);  
		} catch (IllegalArgumentException e) {  
		} catch (InputFormatException e) {  
		} catch (EncoderException e) {  
		}  
	}
	
	private Map<String, Object> department2Map(DepartmentVO dept){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", dept.getId());
		map.put("name", dept.getName());
		map.put("superior", dept.getSuperior()!=null? dept.getSuperior().getId():"");
		map.put("level", dept.getLevel());
		map.put("weixinDeptId",StringUtil.isBlank(dept.getWeixinDeptId())? "":dept.getWeixinDeptId());
		
		return map;
	}
	
	private Map<String, Object> user2Map(UserVO u) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", u.getId());
		map.put("name", u.getName());
		map.put("loginno", u.getLoginno());
		map.put("telephone", StringUtil.isBlank(u.getTelephone())? "":u.getTelephone());
		map.put("email", StringUtil.isBlank(u.getEmail())? "":u.getEmail());
		map.put("defaultDepartment", StringUtil.isBlank(u.getDefaultDepartment())? "":u.getDefaultDepartment());
		map.put("superior", u.getSuperior()!=null? u.getSuperior().getId():"");
		map.put("avatar", StringUtil.isBlank(u.getAvatar())? "":u.getAvatar());
		map.put("level", u.getLevel());
		map.put("dimission", u.getDimission());
		map.put("status", u.getStatus());
		
		
		//拓展字段
		FieldExtendsProcess fieldExtendsProcess = (FieldExtendsProcess) ProcessFactory.createProcess(FieldExtendsProcess.class);
		List<FieldExtendsVO> fieldExtendses = fieldExtendsProcess.queryFieldExtendsByTable(u.getDomain().getId(), FieldExtendsVO.TABLE_USER);
		List<FieldExtendsVO> fes = new ArrayList<FieldExtendsVO>();
		for(int i=0;i<fieldExtendses.size();i++){
			FieldExtendsVO fevo = fieldExtendses.get(i);
			if(fevo != null && fevo.getEnabel()){
				fes.add(fevo);
			}
		}
		//添加拓展字段值
		JSONObject extattr = new JSONObject();
		if(!fes.isEmpty()){
			JSONArray attrs = new JSONArray() ;
			JSONObject attr ;
			for (Iterator<FieldExtendsVO> _iter = fes.iterator(); _iter.hasNext();) {
				FieldExtendsVO fe = _iter.next();
				String label = fe.getLabel();
				String fieldName = fe.getName();
				String _fieldName = fieldName.replaceFirst("f","F");
				Method method = UserVO.class.getMethod("get" + _fieldName,null);
				Object value = method.invoke(u,null);
				if(value != null ){
					attr = new JSONObject() ;
					attr.put("name", label);
					attr.put("value", value.toString());
					attrs.add(attr);
				}
			}
			extattr.put("attrs", attrs);
		}
		if(extattr!=null && extattr.getJSONArray("attrs").size()>0){  // 校验是否真的存在拓展字段
			map.put("extattr", extattr.toString());
		}
		return map;
	}
	
	private String getPostDataAsString(){
		StringBuffer info=new java.lang.StringBuffer();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
		    InputStream in=request.getInputStream();
		    BufferedInputStream buf=new BufferedInputStream(in);
		    byte[] buffer=new byte[1024]; 
		    int iRead;
		    while((iRead=buf.read(buffer))!=-1)   
		    {
		     info.append(new String(buffer,0,iRead,"UTF-8"));
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return info.toString();
	}
	
	
	/**
	 * 获取企业域下的所有用户集合
	 * @return
	 */
	public String getUsers(){
		try {
			Collection<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			ParamsTable params = getParams();
			String siteId = params.getParameterAsString("site_id");
			String domainId = params.getParameterAsString("domain_id");
			
			UserProcess userPorcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			List<UserVO> user = (List<UserVO>)userPorcess.doQueryByHQL("from "+UserVO.class.getName()+" where domainid='"+domainId +"' and status=1 and dimission=1 order by orderByNo");
			for (Iterator<UserVO> iterator = user.iterator(); iterator.hasNext();) {
				UserVO u = iterator.next();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", u.getId());
				map.put("name", u.getName());
				map.put("superior", u.getSuperior().getId());
				map.put("level", u.getLevel());
				list.add(map);
			}
			
			addActionResult(true, "", list);
		} catch (Exception e) {
			addActionResult(false, e.getMessage(), null);
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	
	
	
	
	
	public ParamsTable getParams() {
//		if (params == null) {
		
		ParamsTable params = ParamsTable.convertHTTP(ServletActionContext.getRequest());

		params.setSessionid(ServletActionContext.getRequest().getSession().getId());

		if (params.getParameter("_pagelines") == null){
			params.setParameter("_pagelines", Web.DEFAULT_LINES_PER_PAGE);
		}
//		}

		return params;
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
