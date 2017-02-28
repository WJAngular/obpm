package cn.myapps.core.widget.action;

import cn.myapps.core.widget.ejb.PageWidget;
import cn.myapps.core.widget.ejb.PageWidgetProcess;
import cn.myapps.util.ProcessFactory;

public class PageWidgetHelper {
	public PageWidget getWidget(String id) {

		try {
			PageWidgetProcess process = (PageWidgetProcess) ProcessFactory
					.createProcess(PageWidgetProcess.class);
			return (PageWidget) process.doView(id);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
