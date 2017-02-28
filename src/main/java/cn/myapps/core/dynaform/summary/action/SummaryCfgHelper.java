package cn.myapps.core.dynaform.summary.action;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.collections.map.HashedMap;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgProcess;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.web.DWRHtmlUtils;

/**
 * @author Happy
 * 
 */
public class SummaryCfgHelper {

	public static final TreeMap<String, String> subjectList = new TreeMap<String, String>();

	static {
		subjectList.put("1", "{*[Default]*}");
		subjectList.put("2", "{*[Style_one]*}");
		subjectList.put("3", "{*[Style_two]*}");
		subjectList.put("4", "{*[Style_three]*}");
		subjectList.put("5", "{*[Style_four]*}");
	}

	public static TreeMap<String, String> getStyleList() {
		return subjectList;
	}

	public String createSummaryOptions(String selectFieldName,
			String application, String def) throws Exception {
		SummaryCfgProcess mp = (SummaryCfgProcess) ProcessFactory
				.createProcess(SummaryCfgProcess.class);
		ParamsTable params = new ParamsTable();
		params.setParameter("i_scope", SummaryCfgVO.SCOPE_NOTIFY);
		Collection<SummaryCfgVO> summaryCfgs = mp.doSimpleQuery(params, application);
		Map<String, String> map = new HashMap<String, String>();
		map.put("", "{*[Select]*}");
		if (summaryCfgs != null && summaryCfgs.size() > 0) {
			for (Iterator<SummaryCfgVO> iterator = summaryCfgs.iterator(); iterator
					.hasNext();) {
				SummaryCfgVO vo = (SummaryCfgVO) iterator.next();
				map.put(vo.getId(), vo.getTitle());

			}
		}
		return DWRHtmlUtils.createOptions(map, selectFieldName, def);

	}

	/**
	 * 获得当前软件下的摘要
	 * @param applicationid
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getAllSummaryByApplication(String applicationid)
			throws Exception {
		SummaryCfgProcess mp = (SummaryCfgProcess) ProcessFactory
				.createProcess(SummaryCfgProcess.class);
		Collection<SummaryCfgVO> summaryCfgs = mp.doSimpleQuery(null, applicationid);
		Map<String, String> map = new HashMap<String, String>();
		map.put("", "{*[Select]*}");
		if (summaryCfgs != null && summaryCfgs.size() > 0) {
			for (Iterator<SummaryCfgVO> iterator = summaryCfgs.iterator(); iterator
					.hasNext();) {
				SummaryCfgVO vo = (SummaryCfgVO) iterator.next();
				if(vo.getScope()!=1){
				map.put(vo.getId(), vo.getTitle());
				}
			}
		}
		return map;
	}
	
	public Map<String, String> getSummaryByFormIdAndScope(String formId, int scope) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		try {
			SummaryCfgProcess mp = (SummaryCfgProcess) ProcessFactory.createProcess(SummaryCfgProcess.class);
			SummaryCfgVO vo = mp.doViewByFormIdAndScope(formId, scope);
			map.put("", "{*[Select]*}");
			if(vo != null){
				map.put(vo.getId(), vo.getTitle());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return map;
	}

}
