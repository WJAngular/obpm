package cn.myapps.core.deploy.module.action;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.hibernate.Session;

import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.base.dao.SessionSignal;
import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.deploy.module.ejb.ModuleProcess;
import cn.myapps.core.deploy.module.ejb.ModuleVO;
import cn.myapps.core.deploy.module.util.ListAllElement;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.web.DWRHtmlUtils;

public class ModuleHelper {

	public String createSuperiorOptionFunc(String selectFieldName, String applicationId, String excludeNodeId,
			String def, String application) throws Exception {

		ModuleProcess mp = getProcess();

		Collection<ModuleVO> dc = mp.doSimpleQuery(null, application);
		Map<String, String> dm = mp.deepSearchModuleTree(dc, applicationId, null, excludeNodeId, 0);
		return DWRHtmlUtils.createOptions(dm, selectFieldName, def);
	}

	public void deleteModuleWithSub(ModuleProcess moduleProcess, ModuleVO startNode) throws Exception {
		Collection<ModuleVO> modules = moduleProcess.getUnderModuleList(startNode.getId(), Integer.MAX_VALUE);
		deleteModule(moduleProcess, modules, startNode);
	}

	private void deleteModule(ModuleProcess moduleProcess, Collection<ModuleVO> colls, ModuleVO startNode) throws Exception {
		if (colls.size() > 0) {
			Iterator<ModuleVO> iter = colls.iterator();
			while (iter.hasNext()) {
				ModuleVO vo = iter.next();

				if (startNode == null) {
					if (vo.getSuperior() == null) {
						deleteModule(moduleProcess, colls, vo);
					}
				} else {
					if (vo.getSuperior() != null && vo.getSuperior().getId().equals(startNode.getId())) {
						deleteModule(moduleProcess, colls, vo);
					}
				}
			}
		}
		if (startNode != null) {
			moduleProcess.deleteModule(startNode.getId(), startNode.getApplicationid());
//			moduleProcess.doRemove(startNode.getId());
		}
	}

	public Collection<ModuleVO> get_moduleList(String application) throws Exception {
		ModuleProcess mp = getProcess();
		return mp.doSimpleQuery(null, application);
	}

	public String getMoudleByName(String pk) throws Exception {
		String name = "";
		ModuleProcess mp = getProcess();
		ModuleVO vo = (ModuleVO) mp.doView(pk);
		if (vo != null) {
			name = vo.getName();
		}
		return name;
	}

	public ModuleProcess getProcess() throws Exception {
		return (ModuleProcess) ProcessFactory.createProcess(ModuleProcess.class);
	}

	// public String createQueryOption(String selectFieldName,
	// String applicationId, String moduleid, String def, String application)
	// throws Exception {
	//
	// QueryProcess qp = (QueryProcess) ProcessFactory
	// .createProcess(QueryProcess.class);
	// Collection qc = null;
	// if (moduleid == null || moduleid.trim().length() == 0) {
	// qc = qp.get_queryByAppId(applicationId, application);
	// } else {
	// qc = qp.get_queryStringList(applicationId, moduleid, application);
	// }
	//
	// Map temp = new LinkedHashMap();
	// temp.put("", "none");
	// for (Iterator iter = qc.iterator(); iter.hasNext();) {
	// Query element = (Query) iter.next();
	// temp.put(element.getId(), element.getName());
	// }
	// return DWRHtmlUtils.createOptions(temp, selectFieldName, def);
	// }

	public Map<String, String> getModuleSel(String applicationid) throws Exception {
		return getModuleSelected(applicationid, "");
	}

	public Map<String, String> getModuleSelected(String applicationid, String currentModuleId) throws Exception {
		// //PersistenceUtils.getSessionSignal().sessionSignal++;
		ApplicationProcess ap = (ApplicationProcess) ProcessFactory.createProcess(ApplicationProcess.class);
		ApplicationVO av = (ApplicationVO) ap.doView(applicationid);

		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("", "{*[Select]*}");
		if (applicationid != null && !applicationid.equals("none") && !applicationid.equals(""))
			map = deepSearchModuleTree(av.getModules(), applicationid, null, null, 0);
		// //PersistenceUtils.getSessionSignal().sessionSignal--;
		PersistenceUtils.closeSession();

		// 除去当前模块
		if (!StringUtil.isBlank(currentModuleId)) {
			map.remove(currentModuleId);
		}

		return map;
	}

	public Map<String, String> deepSearchModuleTree(Collection<ModuleVO> cols, String applicationId,
			ModuleVO startNode, String excludeNodeId, int deep) throws Exception {
		Map<String, String> list = new LinkedHashMap<String, String>();
		list.put("", "{*[Select]*}");
		if (applicationId == null || applicationId.equals(""))
			return list;

		String prefix = "|------------------------------------------------";
		if (startNode != null) {
			list.put(startNode.getId(), prefix.substring(0, deep * 2) + startNode.getName());
		}

		Iterator<ModuleVO> iter = cols.iterator();
		while (iter.hasNext()) {
			ModuleVO vo = iter.next();

			if (applicationId == null || vo.getApplication() == null
					|| !applicationId.equals(vo.getApplication().getId())) {
				continue;
			}

			if (startNode == null) {
				if (vo.getSuperior() == null) {
					if (vo.getId() != null && !vo.getId().equals(excludeNodeId)) {
						Map<String, String> tmp = deepSearchModuleTree(cols, applicationId, vo, excludeNodeId, deep + 1);
						list.putAll(tmp);
					}
				}
			} else {
				if (vo.getSuperior() != null && vo.getSuperior().getId().equals(startNode.getId())) {
					if (vo.getId() != null && !vo.getId().equals(excludeNodeId)) {
						Map<String, String> tmp = deepSearchModuleTree(cols, applicationId, vo, excludeNodeId, deep + 1);
						list.putAll(tmp);
					}
				}
			}
		}
		return list;
	}

	public static String listAllElement(ModuleVO module, String applicationid) throws Exception {
		ListAllElement list = new ListAllElement();
		return list.listAndGetStr(module, applicationid);
	}
	
	public static String renameModule (Collection<ModuleVO> modules, ModuleVO module, int num, boolean isself) throws Exception {
		for(Iterator<ModuleVO> iter = modules.iterator(); iter
			.hasNext();){
			ModuleVO orgM = iter.next();
			if(orgM != null){
				//模块处于同一等级才有rename操作
				if(orgM.getSuperior() == null && module.getSuperior() == null){//case 1
					if(isself && orgM.getName().equals(module.getName()) &&
							!orgM.getId().equals(module.getId())){
						num ++;
						renameModule(modules, module, num, false);
					}else if(orgM.getName().equals(module.getName() + "("+num+")") &&
							!orgM.getId().equals(module.getId())){
						num ++;
						renameModule(modules, module, num, false);
					}
				}else if(orgM.getSuperior() != null && 
						orgM.getSuperior().equals(module.getSuperior())){//case 2
					if(isself && orgM.getName().equals(module.getName()) &&
							!orgM.getId().equals(module.getId())){
						num ++;
						renameModule(modules, module, num, false);
					}else if(orgM.getName().equals(module.getName() + "("+num+")") &&
							!orgM.getId().equals(module.getId())){
						num ++;
						renameModule(modules, module, num, false);
					}
				}
			}
		}
		if(num > 0){
			return module.getName() + "("+num+")";
		}
		return module.getName();
	}

}
