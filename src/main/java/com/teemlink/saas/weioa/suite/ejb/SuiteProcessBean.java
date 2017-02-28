package com.teemlink.saas.weioa.suite.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;

import cn.myapps.attendance.util.ConnectionManager;
import cn.myapps.base.action.ParamsTable;

public class SuiteProcessBean implements SuiteProcess {
	
	

	public Collection<Suite> getAllSuiteApps(ParamsTable params, String domainId)
			throws Exception {
		//1.获取所有微信套件应用
		Collection<Suite> list = new ArrayList<Suite>();
		
		Suite txl = new Suite();
		txl.setAgentid("");
		txl.setAppid("2");
		txl.setStatus(Suite.NO_AUTH);
		txl.setName("通讯录");
		txl.setDescription("");
		txl.setLogourl("../resource/images/app-2.png");
		txl.setDomainid(domainId);
		
		Suite pm = new Suite();
		pm.setAgentid("");
		pm.setAppid("3");
		pm.setStatus(Suite.NO_AUTH);
		pm.setName("微任务");
		pm.setDescription("");
		pm.setLogourl("../resource/images/app-3.png");
		pm.setDomainid(domainId);
		
		Suite kq = new Suite();
		kq.setAgentid("");
		kq.setAppid("4");
		kq.setStatus(Suite.NO_AUTH);
		kq.setName("微信考勤");
		kq.setDescription("");
		kq.setLogourl("../resource/images/app-4.png");
		kq.setDomainid(domainId);
		
		Suite crm = new Suite();
		crm.setAgentid("");
		crm.setAppid("5");
		crm.setStatus(Suite.NO_AUTH);
		crm.setName("移动CRM");
		crm.setDescription("");
		crm.setLogourl("../resource/images/app-5.png");
		crm.setDomainid(domainId);
		
		Suite oa = new Suite();
		oa.setAgentid("");
		oa.setAppid("1");
		oa.setStatus(Suite.NO_AUTH);
		oa.setName("OA办公");
		oa.setDescription("");
		oa.setDomainid(domainId);
		oa.setLogourl("../resource/images/app-1.png");
		
		//2.查询应用授权表。更新套件应用状态
		Connection conn = null;
		try {
			conn = ConnectionManager.getConnection();
			PreparedStatement stmt = null;

			String sql = "select * from saas_suite_auth_old where domain_id=?";

				stmt = conn.prepareStatement(sql);
				stmt.setString(1, domainId);

				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					String auth_info = rs.getString("auth_info");
					if(!StringUtils.isBlank(auth_info)){
						JSONObject json = JSONObject.fromObject(auth_info);
						JSONArray agents = json.getJSONObject("auth_info").getJSONArray("agent");
						for (Iterator<JSONObject> iterator = agents.iterator(); iterator
								.hasNext();) {
							JSONObject agent = iterator.next();
							String appid = agent.getString("appid");
							if("2".equals(appid)){
								txl.setStatus(Suite.HAS_AUTH);
							}else if("4".equals(appid)){
								kq.setStatus(Suite.HAS_AUTH);
							}
						}
					}
					
				}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(conn !=null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		}
		
		
		
		list.add(oa);
		list.add(txl);
		list.add(kq);
		list.add(pm);
		list.add(crm);
		
		return list;
	}

	public Collection<Suite> getMySuiteApps(ParamsTable params, String domainId)
			throws Exception {
		Collection<Suite> list = new ArrayList<Suite>();
		Connection conn = null;
		try {
			conn = ConnectionManager.getConnection();
			PreparedStatement stmt = null;

			String sql = "select * from saas_suite_auth_old where domain_id=?";

				stmt = conn.prepareStatement(sql);
				stmt.setString(1, domainId);

				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					String auth_info = rs.getString("auth_info");
					if(!StringUtils.isBlank(auth_info)){
						JSONObject json = JSONObject.fromObject(auth_info);
						if(json.containsKey("auth_info")){
							JSONArray agents = json.getJSONObject("auth_info").getJSONArray("agent");
							for (Iterator<JSONObject> iterator = agents.iterator(); iterator
									.hasNext();) {
								JSONObject agent = iterator.next();
								Suite s = new Suite();
								s.setAgentid(agent.getString("agentid"));
								s.setAppid(agent.getString("appid"));
								s.setStatus(Suite.HAS_AUTH);
								s.setName(agent.getString("name"));
								s.setLogourl("../resource/images/app-"+s.getAppid()+".png");
								s.setDomainid(domainId);
								list.add(s);
							}
						}
					}
					
				}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(conn !=null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		}
		return list;
	}

}
