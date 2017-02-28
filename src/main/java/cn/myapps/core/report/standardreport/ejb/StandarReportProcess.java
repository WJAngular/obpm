package cn.myapps.core.report.standardreport.ejb;

import java.util.Collection;
import java.util.Map;

import cn.myapps.base.ejb.IRunTimeProcess;

public interface StandarReportProcess extends IRunTimeProcess<Object> {

	/**
	 * ���ܲ�ѯ���
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public Collection<Map<String, String>> getSummaryReport(String formId, String startdate,
			String enddate, String[] columnName, String dbmethod) throws Exception;

	/**
	 * 
	 * ��ѯ�ܵı�������
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public int getReportRowsNum(String formId, String startdate,
			String enddate, String[] columnName, String dbmethod) throws Exception;
}
