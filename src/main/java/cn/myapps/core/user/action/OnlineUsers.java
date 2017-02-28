package cn.myapps.core.user.action;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.opensymphony.xwork2.ActionContext;


import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.constans.Web;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

public class OnlineUsers {
	private static Map<String, WebUser> _users = new HashMap<String, WebUser>();

	@SuppressWarnings("unchecked")
	public static synchronized void add(String key, WebUser user) {
		if (key == null || user == null) {
			return;
		}
		
		if(_users.size()>0){
			List<String> delList = new ArrayList<String>();
			Iterator<Entry<String, WebUser>> iter = _users.entrySet().iterator();
			while(iter.hasNext()){
				Map.Entry entry=(Entry) iter.next();
				String thekey=(String) entry.getKey();
				WebUser theUser=(WebUser) entry.getValue();
				if(!thekey.equals(key) && theUser.getId().equals(user.getId())){
					//重复登录时，删掉旧的,先保存要删除的key,避免发生异常
					delList.add(thekey);
				}
			}
			
			if(delList.size() > 0){
				for(Iterator it = delList.iterator(); it.hasNext();){
					String delKey = (String) it.next();
					_users.remove(delKey);
				}
			}
		}
		
		_users.put(key, user);
	}

	public static synchronized void remove(String key) {
		if (key == null) {
			return;
		}
		_users.remove(key);

	}

	public static int getUsersCount() {
		return (_users != null ? _users.size() : 0);
	}

	public static DataPackage<WebUser> doQuery(ParamsTable params) {
		DataPackage<WebUser> datas = new DataPackage<WebUser>();
		//按企业域查询
		if(!StringUtil.isBlank(params.getParameterAsString("sm_domainid")))
			return doQueryByDomain(params,params.getParameterAsString("sm_domainid"));
		ArrayList<WebUser> result = new ArrayList<WebUser>(_users.values());

		datas.rowCount = result.size();
		int page, lines;
		try {
			page = Integer.parseInt(params.getParameterAsString("_currpage"));
		} catch (Exception ex) {
			page = 1;
		}
		try {
			lines = Integer.parseInt(params.getParameterAsString("_pagelines"));
		} catch (Exception ex) {
			lines = Integer.MAX_VALUE;
		}
		datas.pageNo = page;
		datas.linesPerPage = lines;
		//排序
		if(!StringUtil.isBlank(params.getParameterAsString("_orderby")))
			sort(result,params);
		// 分页
		try {
			datas.setDatas(result.subList((page - 1) * lines,
					(datas.rowCount > (page) * lines ? (page) * lines
							: datas.rowCount)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datas;
	}
	
	public static DataPackage<WebUser> doQueryByDomain(ParamsTable params,String domain) {
		DataPackage<WebUser> datas = new DataPackage<WebUser>();
		ArrayList<WebUser> result = new ArrayList<WebUser>();
		for (Iterator<WebUser> iterator = _users.values().iterator(); iterator.hasNext();) {
			WebUser webUser = (WebUser) iterator.next();
			if(webUser.getDomainid().equals(domain)) result.add(webUser);
		}
		

		datas.rowCount = result.size();
		int page, lines;
		try {
			page = Integer.parseInt(params.getParameterAsString("_currpage"));
		} catch (Exception ex) {
			page = 1;
		}
		try {
			lines = Integer.parseInt(params.getParameterAsString("_pagelines"));
		} catch (Exception ex) {
			lines = Integer.MAX_VALUE;
		}
		datas.pageNo = page;
		datas.linesPerPage = lines;
		//排序
		if(!StringUtil.isBlank(params.getParameterAsString("_orderby")))
			sort(result,params);
		// 分页
		try {
			datas.setDatas(result.subList((page - 1) * lines,
					(datas.rowCount > (page) * lines ? (page) * lines
							: datas.rowCount)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datas;
	}
	
	/**
	 * 企业域管理员身份登录查看当前在线用户(后台监控调用)
	 * @param params
	 * @return
	 */
	public static DataPackage<WebUser> doQueryForDomainAdmin(ParamsTable params) {
		DataPackage<WebUser> datas = new DataPackage<WebUser>();
		ArrayList<WebUser> result = new ArrayList<WebUser>();
		//按企业域查询
		if(!StringUtil.isBlank(params.getParameterAsString("sm_domainid")))
			return doQueryByDomain(params,params.getParameterAsString("sm_domainid"));
		
		Map<?, ?> session = ActionContext.getContext().getSession();
		WebUser user = (WebUser) session.get(Web.SESSION_ATTRIBUTE_USER);
		//domainList存放当前DomainAdmin的所有已激活的企业域
		ArrayList<String> domainList = new ArrayList<String>();
		try {
			DomainProcess dp = (DomainProcess) ProcessFactory.createProcess(DomainProcess.class);
			if(user.isDomainAdmin()){
				Collection<DomainVO> domainCol = dp.queryDomainsByStatusAndUser(1, user.getId());
				for(Iterator<DomainVO> it = domainCol.iterator();it.hasNext();){
					DomainVO vo = it.next();
					domainList.add(vo.getId());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (Iterator<WebUser> iterator = _users.values().iterator(); iterator.hasNext();) {
			WebUser webUser = (WebUser) iterator.next();
			if(domainList.contains(webUser.getDomainid())) result.add(webUser);
		}
		
		datas.rowCount = result.size();
		int page, lines;
		try {
			page = Integer.parseInt(params.getParameterAsString("_currpage"));
		} catch (Exception ex) {
			page = 1;
		}
		try {
			lines = Integer.parseInt(params.getParameterAsString("_pagelines"));
		} catch (Exception ex) {
			lines = Integer.MAX_VALUE;
		}
		datas.pageNo = page;
		datas.linesPerPage = lines;
		//排序
		if(!StringUtil.isBlank(params.getParameterAsString("_orderby")))
			sort(result,params);
		// 分页
		try {
			datas.setDatas(result.subList((page - 1) * lines,
					(datas.rowCount > (page) * lines ? (page) * lines
							: datas.rowCount)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datas;
	}
	
	protected static void sort(List<WebUser> datas, final ParamsTable params) {
		Collections.sort(datas, new Comparator<WebUser>() {
			public int compare(WebUser w1, WebUser w2) {
				Object value1 = null;
				Object value2 = null;
				String orderBy = params.getParameterAsString("_orderby");
				String fieldName = "";
				String orderStatus = "";
				if(!StringUtil.isBlank(orderBy)){
					if(orderBy.indexOf(" ") > 0){
						String [] array = orderBy.split(" ");
						fieldName = array[0];
						orderStatus = array[1];
					}else{
						fieldName = orderBy;
						orderStatus = "asc";
					}
				}
				if (!StringUtil.isBlank(orderBy)) {
					Class class1 = w1.getClass();
					Class class2 = w2.getClass();
					while (class1 != null && value1 == null) {
						try {
							Field field = class1.getDeclaredField(fieldName);
							field.setAccessible(true);
							value1 = field.get(w1);
							field.setAccessible(false);
							class1 = class1.getSuperclass();
						} catch (Exception e) {
							class1 = class1.getSuperclass();
						}
					}
					while (class2 != null && value2 == null) {
						try {
							Field field = class2.getDeclaredField(fieldName);
							field.setAccessible(true);
							value2 = field.get(w2);
							field.setAccessible(false);
							class2 = class2.getSuperclass();
						} catch (Exception e) {
							class2 = class2.getSuperclass();
						}
					}
				}
				if (value1 instanceof Number && value2 instanceof Number) {
					double n1 = ((Number) value1).doubleValue();
					double n2 = ((Number) value2).doubleValue();
					if (n1 < n2)
						return "desc".equalsIgnoreCase(orderStatus) ? 1 : -1;
					if (n1 > n2)
						return "desc".equalsIgnoreCase(orderStatus) ? -1 : 1;

				} else if (value1 instanceof Date && value2 instanceof Date) {
					Date da1 = (Date) value1;
					Date da2 = (Date) value2;
					if (da1.compareTo(da2) < 0)
						return "desc".equalsIgnoreCase(orderStatus) ? 1 : -1;
					if (da1.compareTo(da2) > 0)
						return "desc".equalsIgnoreCase(orderStatus) ? -1 : 1;
				} else if (value1 instanceof String && value2 instanceof String) {
					String s1 = (String) value1;
					String s2 = (String) value2;
					if (s1.compareTo(s2) < 0)
						return "desc".equalsIgnoreCase(orderStatus) ? 1 : -1;
					if (s1.compareTo(s2) > 0)
						return "desc".equalsIgnoreCase(orderStatus) ? -1 : 1;
				}
				return 0;
			}
		});
	}

}
