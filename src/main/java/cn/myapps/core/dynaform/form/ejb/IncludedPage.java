package cn.myapps.core.dynaform.form.ejb;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.ejb.IDesignTimeProcess;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.page.ejb.Page;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserDefined;
import cn.myapps.core.user.ejb.UserDefinedProcess;
import cn.myapps.util.Debug;
import cn.myapps.util.ProcessFactory;

/**
 * 引入一个视图组件, 组件以iframe的形式输出
 * 
 * @author nicholas
 */
public class IncludedPage extends IncludedElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1517041188945769061L;

	public UserDefined page;

	/**
	 * 构造方法
	 * 
	 * @param field
	 */
	public IncludedPage(IncludeField field) {
		super(field);
	}

	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception {
		try {
			UserDefinedProcess pp = (UserDefinedProcess) ProcessFactory.createProcess(UserDefinedProcess.class);
			page = (UserDefined) pp.doView(getValueObjectId(runner));
			if (page != null) {
				return page.toHtml(doc, new ParamsTable(), webUser);
			}
		} catch (Exception e) {
			Debug.println(e.getMessage());
			e.printStackTrace();
		}

		return "";
	}

	public String toPrintHtmlTxt(Document doc, IRunner runner, WebUser webUser) throws Exception {
		return "";
	}

	/**
	 * @SuppressWarnings 工厂方法不支持泛型
	 */
	@SuppressWarnings("unchecked")
	public IDesignTimeProcess getProcess() throws Exception {
		return ProcessFactory.createProcess(UserDefinedProcess.class);
	}

	public String toXMLTxt(Document doc, IRunner runner, WebUser webUser) throws Exception {

		try {
			UserDefinedProcess pp = (UserDefinedProcess) ProcessFactory.createProcess(UserDefinedProcess.class);
			UserDefined homepage = (UserDefined) pp.doView(getValueObjectId(runner));
			Page page = homepage.initPage(homepage);
			if (page != null) {
				return page.toXMLCalctext(doc, runner, webUser, false);
			}
		} catch (Exception e) {
			Debug.println(e.getMessage());
			e.printStackTrace();
		}

		return "";
	}
	
	public String toXMLTxt2(Document doc, IRunner runner, WebUser webUser) throws Exception {

		try {
			UserDefinedProcess pp = (UserDefinedProcess) ProcessFactory.createProcess(UserDefinedProcess.class);
			UserDefined homepage = (UserDefined) pp.doView(getValueObjectId(runner));
			Page page = homepage.initPage(homepage);
			if (page != null) {
				return page.toXMLCalctext(doc, runner, webUser, false);
			}
		} catch (Exception e) {
			Debug.println(e.getMessage());
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * 获取组件名
	 * 
	 * @return 组件名
	 */
	public String getName() {
		if (page != null) {
			return page.getName();
		}
		return "";
	}

	public String toPdfHtmlTxt(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		return null;
	}
}
