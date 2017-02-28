package cn.myapps.util.report;

import net.sf.json.JSONObject;
import cn.myapps.core.report.crossreport.definition.ejb.CrossReportVO;
import cn.myapps.core.report.oreport.action.OReportUtil;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.StringUtil;

public class AbstractChartBuilder {

	/**
	 * 获取报表数据集（JSON格式）
	 * 
	 * @param reportVO
	 *            报表对象
	 * @param user
	 *            用户
	 * @return JSON格式字符串
	 * @throws Exception
	 */
	public String getDataSet(CrossReportVO reportVO, WebUser user)
			throws Exception {
		if (reportVO != null) {
			String reportJson = reportVO.getJson();

			if (!StringUtil.isBlank(reportJson)) {
				JSONObject cfg = JSONObject.fromObject(reportJson);
				String viewId = cfg.getString("viewid");
				return OReportUtil.getDataSet(viewId, user, reportJson, "", 1);
			}
		}
		return null;
	}
}
