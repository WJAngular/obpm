package cn.myapps.core.dynaform.dts.el2xml;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.view.ejb.Column;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.xml.XmlUtil;
import cn.myapps.util.xml.converter.ActivityConverter;
import cn.myapps.util.xml.converter.ColumnConverter;
import cn.myapps.util.xml.converter.HibernateCollectionConverter;

import com.thoughtworks.xstream.XStream;

public class ViewElementToXml extends ElementToXml {
	XStream xstream;

	public ViewElementToXml() throws Exception {
		super();
		xstream = new XStream();
		xstream.registerConverter(new HibernateCollectionConverter(xstream
				.getMapper()));
		xstream.registerConverter(new ActivityConverter());
		xstream.registerConverter(new ColumnConverter());
	}

	public void doTransfer() throws Exception {
		ApplicationProcess ap = (ApplicationProcess) ProcessFactory
				.createProcess(ApplicationProcess.class);
		Collection<?> apps = ap.doSimpleQuery(null);
		for (Iterator<?> iterator = apps.iterator(); iterator.hasNext();) {
			ApplicationVO app = (ApplicationVO) iterator.next();
			if (app.testDB()) {
				if (app.getName().equals("CHINA")){
					doTransferByApp(app.getId());
				}
			}
		}
	}

	public void doTransferByApp(String applicationId) throws Exception {
		ViewProcess vp = (ViewProcess) ProcessFactory
				.createProcess(ViewProcess.class);
		ParamsTable params = new ParamsTable();
		params.setParameter("t_applicationid", applicationId);
		Collection<?> viewList = vp.doSimpleQuery(params);
		try {
			for (Iterator<?> iterator = viewList.iterator(); iterator.hasNext();) {
				View view = (View) iterator.next();
				
				{
					// 获取activitys
					TreeSet<Activity> activitySet = new TreeSet<Activity>();
					String sql = "select ID, ORDERNO, NAME, BEFOREACTIONSCRIPT, HIDDENSCRIPT, ONACTIONFORM_ID ONACTIONFORM, ONACTIONVIEW_ID ONACTIONVIEW, TYPE, FORM_ID PARENTFORM, VIEW_ID PARENTVIEW, ONACTIONFLOW_ID ONACTIONFLOW, ICONURL, STATETOSHOW, AFTERACTIONSCRIPT, APPROVELIMIT, SORTID, APPLICATIONID from T_ACTIVITY where VIEW_ID='"
							+ view.getId() + "' order by ORDERNO";
					Collection<?> activitys = getElementsBySQL(sql, Activity.class);
					int orderno = 0;
					for (Iterator<?> iterator2 = activitys.iterator(); iterator2
							.hasNext(); orderno++) {
						Activity activity = (Activity) iterator2.next();
						activity.setOrderno(orderno);
						activitySet.add(activity);
					}
					String activityXml = XmlUtil.toXml(activitySet);
					view.setActivityXML(activityXml);
				}

				{
					// 获取columns
					TreeSet<Column> columnSet = new TreeSet<Column>();
					String sql = "select ID, ORDERNO, VALUESCRIPT, WIDTH, VIEW_ID PARENTVIEW, NAME, CTYPE TYPE, FIELDNAME, FORMID, SORTID, APPLICATIONID from T_COLUMN where VIEW_ID='"
							+ view.getId() + "' order by ORDERNO";
					Collection<?> columns = getElementsBySQL(sql, Column.class);
					int orderno = 0;
					for (Iterator<?> iterator3 = columns.iterator(); iterator3
							.hasNext(); orderno++) {
						Column column = (Column) iterator3.next();
						column.setOrderno(orderno);
						columnSet.add(column);
					}
					String columnXml = XmlUtil.toXml(columnSet);
					view.setColumnXML(columnXml);
				}
				vp.doUpdate(view);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		
		
	}

	public static void main(String[] args) throws Exception {
		new ViewElementToXml().doTransfer();
	}
}
