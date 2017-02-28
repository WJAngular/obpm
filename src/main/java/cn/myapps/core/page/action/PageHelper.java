package cn.myapps.core.page.action;

import java.util.Collection;
import java.util.Iterator;

import cn.myapps.base.action.BaseHelper;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.page.ejb.Page;
import cn.myapps.core.page.ejb.PageProcess;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;

public class PageHelper extends BaseHelper<Page> {
	public PageHelper() throws ClassNotFoundException {
		super((PageProcess) ProcessFactory.createProcess(PageProcess.class));
	}

	public static Page getDefaultPage(String application) throws Exception {
		PageProcess pp = (PageProcess) ProcessFactory.createProcess(PageProcess.class);
		return pp.getDefaultPage(application);
	}

	public static boolean hasDefaultHomePage(WebUser user, String application) throws Exception {
		PageProcess pp = (PageProcess) ProcessFactory.createProcess(PageProcess.class);
		Collection<Page> pages = pp.getPagesByApplication(application);
		if (pages == null || pages.size() == 0)
			return false;
		String rolelist = user.getRolelist(application);
		String[] roles = rolelist.split(",");
		if (roles == null || roles.length <= 0)
			return false;
		for (Iterator<Page> iterator = pages.iterator(); iterator.hasNext();) {
			Page page = iterator.next();
			if (!page.isDefHomePage())
				continue;
			String pageRoles = page.getRoles();
			if (pageRoles == null || pageRoles.length() <= 0)
				return true;
			for (int i = 0; i < roles.length; i++) {
				if (pageRoles.indexOf(roles[i]) >= 0)
					return true;
			}
		}
		return false;
	}

	public String toHtml(WebUser user, ParamsTable params) throws Exception {
		StringBuffer buff = new StringBuffer();
		PageProcess pp = (PageProcess) ProcessFactory.createProcess(PageProcess.class);
		String application = params.getParameterAsString("application");
		Collection<Page> pages = pp.getPagesByApplication(application);
		String rolelist = user.getRolelist();
		rolelist = rolelist.replaceAll("'", "");
		String[] roles = rolelist.split(",");
		for (Iterator<Page> iterator = pages.iterator(); iterator.hasNext();) {
			Page page = iterator.next();
			if (!page.isDefHomePage())
				continue;
			String pageRoles = page.getRoles();
			if (pageRoles == null || pageRoles.length() <= 0) {
				buff.append(page.toHtml(params, user));
				return buff.toString();
			}
			for (int i = 0; i < roles.length; i++) {
				if (pageRoles.indexOf(roles[i]) >= 0) {
					buff.append(page.toHtml(params, user));
					return buff.toString();
				}
			}
		}
		return buff.toString();
	}
}
