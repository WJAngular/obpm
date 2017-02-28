package cn.myapps.core.links.action;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.myapps.base.action.BaseHelper;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.ejb.IDesignTimeProcess;
import cn.myapps.core.dynaform.dts.excelimport.config.ejb.IMPMappingConfigProcess;
import cn.myapps.core.dynaform.dts.excelimport.config.ejb.IMPMappingConfigVO;
import cn.myapps.core.links.ejb.LinkProcess;
import cn.myapps.core.links.ejb.LinkVO;
import cn.myapps.util.ProcessFactory;

public class LinkHelper extends BaseHelper<LinkVO> {

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(LinkHelper.class);

	public LinkHelper(IDesignTimeProcess<LinkVO> process) {
		super(process);
	}

	/**
	 * @SuppressWarnings 工厂方法不支持泛型
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public LinkHelper() throws ClassNotFoundException {
		super(ProcessFactory.createProcess(LinkProcess.class));
	}

	public Map<String, String> get_ExcelImportCfgList(String application) throws Exception {
		IMPMappingConfigProcess ep = (IMPMappingConfigProcess) ProcessFactory
				.createProcess(IMPMappingConfigProcess.class);
		Collection<IMPMappingConfigVO> col = ep.getAllMappingConfig(application);
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		Iterator<IMPMappingConfigVO> it = col.iterator();
		while (it.hasNext()) {
			IMPMappingConfigVO em = it.next();
			map.put(em.getId(), em.getName());
		}
		return map;
	}
	
	public Map<String, String> get_AllLinkType()throws Exception {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("", "{*[Select]*}");
		map.put(LinkVO.LinkType.FORM.getCode(), "{*[Form]*}");
		map.put(LinkVO.LinkType.VIEW.getCode(), "{*[View]*}");
		map.put(LinkVO.LinkType.REPORT.getCode(), "{*[cn.myapps.core.resource.report]*}");
		map.put(LinkVO.LinkType.CUSTOMIZE_REPORT.getCode(), "{*[cn.myapps.core.resource.customize_report]*}");
		map.put(LinkVO.LinkType.RUNQIAN_REPORT.getCode(), "{*[cn.myapps.core.resource.raq_report]*}");
		map.put(LinkVO.LinkType.EXCELIMPORT.getCode(), "{*[cn.myapps.core.resource.excel_import]*}");
		map.put(LinkVO.LinkType.MANUAL_INTERNAL.getCode(), "{*[cn.myapps.core.resource.customize_links_internal]*}");
		map.put(LinkVO.LinkType.MANUAL_EXTERNAL.getCode(), "{*[cn.myapps.core.resource.customize_links_external]*}");
		map.put(LinkVO.LinkType.SCRIPT.getCode(), "{*[cn.myapps.core.resource.script_links]*}");
		map.put(LinkVO.LinkType.EMAIL.getCode(), "{*[cn.myapps.core.resource.email_links]*}");
		map.put(LinkVO.LinkType.BBS.getCode(), "{*[Forum.Links]*}");
		map.put(LinkVO.LinkType.NetworkDisk.getCode(), "{*[cn.myapps.core.resource.network_disk_links]*}");
		return map;
	}
	
	public Map<String, String> get_MbLinkType()throws Exception {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("", "{*[Select]*}");
		map.put(LinkVO.LinkType.FORM.getCode(), "{*[Form]*}");
		map.put(LinkVO.LinkType.VIEW.getCode(), "{*[View]*}");
		return map;
	}

	public String buildActionUrl(String pk, ParamsTable params) throws Exception {
		LinkVO link = (LinkVO) process.doView(pk);
		int type = Integer.parseInt(link.getType());
		String actionContent = link.getActionContent();
		String contextPath = params.getContextPath();
		StringBuffer url = new StringBuffer();
		switch (type) {
		case 0:
			url.append(contextPath).append("/portal/dynaform/document/new.action?_formid=").append(actionContent)
					.append("&_isJump=1");
			break;
		case 1:
			url.append(contextPath).append("/portal/dynaform/view/displayView.action?_viewid=").append(actionContent)
					.append("&clearTemp=true");
			break;
		case 2:
			url.append(contextPath).append("/portal/report/crossreport/runtime/runreport.action?reportId=").append(
					actionContent);
			break;
		case 3:
			url.append(contextPath).append("/portal/share/dynaform/dts/excelimport/importbyid.jsp?id=").append(
					actionContent).append("&applicationid="+link.getApplicationid());
			break;
		case 5:
			url.append(actionContent);
			break;

		}
		return url.toString();

	}

	public static void main(String[] args) {
		String a = "03";
		int b = Integer.parseInt(a);
		System.out.println(b);
	}

}
