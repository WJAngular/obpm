package cn.myapps.core.sysconfig.util;

import java.util.Collection;
import java.util.Iterator;

import cn.myapps.core.deploy.application.action.ApplicationHelper;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.cache.EhcacheProvider;
import cn.myapps.util.cache.MyCacheManager;

public class DataManageUtil {
	public String clearCache(){
		try {
			EhcacheProvider cacheProvider = (EhcacheProvider) MyCacheManager.getProviderInstance();
			cacheProvider.clearAll();
			JavaScriptFactory.clear();
		}catch (Exception e) {
			return "fail";
		}
		return "success";
	}
	
	public String clearDatas(String applicationid){
		try {
			if (!StringUtil.isBlank(applicationid)) {
				doClear(applicationid);
			}else{
				//清除所有软件下的冗余数据
				ApplicationHelper appHelper = new ApplicationHelper();
				Collection<ApplicationVO> appCols = appHelper.getAppList();
				for (Iterator<ApplicationVO> it = appCols.iterator(); it
						.hasNext();) {
					ApplicationVO vo = it.next();
					doClear(vo.getId());
				}
			}
		}catch (Exception e) {
			return "fail";
		}
		return "success";
	}
	
	/**
	 * 清除冗余数据
	 * @param applicationid
	 * @throws Exception
	 */
	public void doClear(String applicationid) throws Exception {
		DocumentProcess docProcess = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,
				applicationid);
			docProcess.doClearRedundancyData();
	}
}
