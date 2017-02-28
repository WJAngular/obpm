package cn.myapps.core.usergroup.action;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.usergroup.ejb.UserGroupProcess;
import cn.myapps.core.usergroup.ejb.UserGroupVO;
import cn.myapps.util.ProcessFactory;

public class UserGroupHelper {
	
	/**
	 * 返回用户的用户组信息
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getAllGroupByUser(String userid) throws Exception{
		Map<String, String> options = new LinkedHashMap<String, String>();
		
		UserGroupProcess process = (UserGroupProcess) ProcessFactory.createProcess(UserGroupProcess.class);
		DataPackage<UserGroupVO> userGroups = process.getUserGroupsByUser(userid);
		for(Iterator<UserGroupVO> it = userGroups.datas.iterator(); it.hasNext();){
			UserGroupVO userGroup = it.next();
			options.put(userGroup.getId(), userGroup.getName());
		}
		
		return options;
	}
}
