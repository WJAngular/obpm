package cn.myapps.webservice.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.superuser.ejb.SuperUserVO;
import cn.myapps.webservice.model.SimpleApplication;

/**
 * ApplicationService工具类
 * @author ivan
 *
 */
public class ApplicationUtil {
	/**
	 * 转换为简单软件对象列表
	 * 
	 * @param appList
	 *            软件列表
	 * @return 应用集合
	 */
	public static Collection<SimpleApplication> convertToSimple(
			Collection<ApplicationVO> appList) {
		Collection<SimpleApplication> sAppList = new ArrayList<SimpleApplication>();
		for (Iterator<?> iterator = appList.iterator(); iterator.hasNext();) {
			ApplicationVO app = (ApplicationVO) iterator.next();
			sAppList.add(convertToSimple(app));
		}

		return sAppList;
	}

	/**
	 * 转换为简单软件对象
	 * 
	 * @param app
	 *            软件
	 * @return
	 */
	public static SimpleApplication convertToSimple(ApplicationVO app) {
		if (app != null) {
			SimpleApplication sApp = new SimpleApplication();

			sApp.setId(app.getId());
			sApp.setDescription(app.getDescription());
			sApp.setName(app.getName());
			sApp.setRegisterCount(app.getDomains().size());

			Collection<?> owners = app.getOwners();
			Collection<String> developerNames = new ArrayList<String>();
			for (Iterator<?> iterator = owners.iterator(); iterator.hasNext();) {
				SuperUserVO developer = (SuperUserVO) iterator.next();
				developerNames.add(developer.getName());
			}

			sApp.setDeveloperNames(developerNames);

			return sApp;
		}
		return null;
	}
}
