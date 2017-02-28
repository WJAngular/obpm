package cn.myapps.core.dynaform.form.ejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.table.constants.MobileConstant;
import cn.myapps.core.table.constants.MobileConstant2;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.HtmlEncoder;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.json.JsonUtil;

public class TabNormal implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6274054997243800672L;

	protected final static String TAB_CONTENT = "content_";

	protected final static String TAB_HREF = "href_";

	protected final static String TAB_LI = "li_";

	protected final static String IS_LOADED = "isloaded";

	protected TabField field;

	public TabNormal(TabField field) {
		this.field = field;
	}

	public String getName() {
		return "tab_" + field.getId();
	}

	public boolean isCalculateOnRefresh() {
		return true;
	}

	public ValidateMessage validate(IRunner runner, Document doc)
			throws Exception {
		return null;
	}

	/**
	 * 获取模板描述页签
	 * 
	 * @return 模板描述页签
	 * @roseuid 41E7917A033F
	 */
	public String toTemplate() {
		StringBuffer template = new StringBuffer();
		template.append("<IMG id=" + field.getId() + "");
		template
				.append("src=\"/obpm/core/dynaform/form/formeditor/buttonimage/v1/tag.gif\"");
		template
				.append("className=\"cn.myapps.core.dynaform.form.ejb.TabField\"");
		template.append("type=\"tabfield\"");
		String[] exculdes = new String[] { "form", "fields" };
		template.append("relStr= \""
				+ JsonUtil.collection2Json(getTabs(), exculdes) + "\"");
		template.append("\\>");
		return template.toString();
	}

	/**
	 * 
	 * Form模版的TagField(页签)内容结合Document中的ITEM存放的值,返回重定义后的html，
	 * 
	 * @param doc
	 *            文档对象
	 * @see cn.myapps.base.action.ParamsTable#params
	 * @see cn.myapps.core.macro.runner.AbstractRunner#run(String, String)
	 * @return 重定义后的html为Form模版的TabField(页签)内容结合Document中的ITEM存放的值,
	 * 
	 */
	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser,int displayType)
			throws Exception {
		StringBuffer html = new StringBuffer();
		Tab selectedTab = getSelectedTab(runner);
		html.append("<div moduleType='TabNormal'");
		String tabId = selectedTab != null ? selectedTab.getFormId() : "";
		html.append(" tabId='" + tabId + "'>");
		appendTabsHtml(html, runner);
		appendTabContentsHtml(html, doc, runner, webUser, selectedTab,displayType);
		html.append("</div>");

		return html.toString();
	}

	/**
	 * 获取tab组件的集合
	 * 
	 * @return tab组件的集合
	 * @uml.property name="tabs"
	 */
	public Collection<Tab> getTabs() {
		return field.getTabs();
	}

	/**
	 * 获取tab组件的集合,设置tab点击事件并输出的页面
	 * 
	 * @param html
	 *            字符串
	 * @param tabs
	 *            tab组件的集合
	 * @param runner
	 *            AbstractRunner(执行脚本的接口类)
	 * @return
	 * @throws Exception
	 */
	public void appendTabsHtml(StringBuffer html, IRunner runner)
			throws Exception {
		// div
		html.append("<div style='display' id='title'");
		html.append(" fieldId='" + field.getId() + "'>");
		for (Iterator<Tab> iterator = getTabs().iterator(); iterator.hasNext();) {
			Tab tab = (Tab) iterator.next();
			// <li>
			html.append("<div formId='").append(tab.getFormId()).append("'");
			html.append(" isHidden=" + tab.isHidden(this, runner));
			html.append(" isRefreshOnChanged=" + tab.isRefreshOnChanged());
			html.append(" name='" + tab.getName() + "'>");
			html.append("</div>");
		}
		html.append("</div>");
	}

	/**
	 * 根据名称获取Tab
	 * 
	 * @param tabName
	 *            页签名称
	 * @return
	 */
	protected Tab getTabByName(String tabName) {
		for (Iterator<Tab> iterator = getTabs().iterator(); iterator.hasNext();) {
			Tab tab = (Tab) iterator.next();
			if (tab.getName().equals(tabName)) {
				return tab;
			}
		}
		return null;
	}

	/**
	 * 获取选中的页签
	 * 
	 * @param runner
	 * @return
	 * @throws Exception
	 */
	public Tab getSelectedTab(IRunner runner) throws Exception {
		Object result = runner.run(field.getScriptLable("SelectedScript"),
				StringUtil.dencodeHTML(field.getSelectedScript()));
		if (result != null && result instanceof String) {
			String tabName = (String) result;
			Tab tab = getTabByName(tabName);
			if (!tab.isHidden(this, runner)) {
				return tab;
			}
		}

		return getFirstShowTab(runner);
	}

	/**
	 * 获取第一个显示的页签
	 * 
	 * @param runner
	 * @return
	 * @throws Exception
	 */
	protected Tab getFirstShowTab(IRunner runner) throws Exception {
		for (Iterator<Tab> iterator = getTabs().iterator(); iterator.hasNext();) {
			Tab tab = (Tab) iterator.next();
			if (!tab.isHidden(this, runner)) {
				return tab;
			}
		}

		return null;
	}

	/**
	 * 初始化TAB的内容,除第一个选中的Tab外，其他都默认隐藏 每点击一个tab自动刷新文档内容
	 * 
	 * @param html
	 *            html 字符串
	 * @param tabs
	 *            tab组件的集合
	 * @param doc
	 *            document(文档对象)
	 * @param runner
	 *            AbstractRunner(执行脚本的接口类)
	 * @param webUser
	 *            webUser
	 * @param selectedIndex
	 *            初始化显示tab
	 */
	public void appendTabContentsHtml(StringBuffer html, Document doc,
			IRunner runner, WebUser webUser, Tab selectedTab,int displayType) {
		html.append("<DIV id='tabcontainer' class='tabcontainer'>");
		try {
			for (Iterator<Tab> iterator = getTabs().iterator(); iterator
					.hasNext();) {
				Tab tab = (Tab) iterator.next();
				html.append("<div formId='").append(tab.getFormId())
						.append("'");
				
				int _displayType = displayType ;
				boolean isHidden = false ;
				boolean isReadOnly = false ;
				if(displayType == PermissionType.HIDDEN 
						 || (isHidden = tab.isHidden(this, runner))){
					_displayType = PermissionType.HIDDEN ;
				}else if(displayType == PermissionType.READONLY 
						 || (isReadOnly = tab.isReadOnly(this, runner))){
					_displayType = PermissionType.READONLY;
				}
				html.append(" isHidden=" + isHidden + ">");
				//html.append(" isReadOnly=" + isReadOnly + ">");
				
				// 只加载选中的Tab
				if (selectedTab != null
						&& tab.getFormId().equals(selectedTab.getFormId())) {
					if (!field.get_form().equals(tab.getForm())) {
						html.append(tab.toHtml(doc, runner, webUser, false,_displayType));
					}
				} else {
					html.append(tab.toHtml(doc, runner, webUser, true,_displayType));
				}
				html.append("</div>");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		html.append("</DIV>");

	}

	public void appendTabContentsPrintHtml(StringBuffer html, IRunner runner,
			Collection<Tab> tabs, Document doc, WebUser webUser) {
		int index = 0;
		try {
			html.append("<DIV>");
			for (Iterator<Tab> iterator = tabs.iterator(); iterator.hasNext();) {
				Tab tab = (Tab) iterator.next();
				StringBuffer suffix = new StringBuffer();
				suffix.append("Tab.").append(tab.getName());
				suffix.append(".hiddenPrintScript");

				if (field.runBooleanScript(runner, suffix.toString(), tab
						.getHiddenPrintScript())) {
					continue;
				}
				html.append("<div id=\"" + field.getId() + index + "\">");
				// 防止死循环,避免Tab Field包含了所在的Form而造成死循环
				if (!field.get_form().getId().equals(tab.getFormId())) {
					html.append(tab.toPrintHtml(doc, runner, webUser));
				}
				html.append("</div>");
				index++;
			}

			html.append("</DIV>");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void appendTabContentsPdfHtml(StringBuffer html, IRunner runner,
			Collection<Tab> tabs, Document doc, WebUser webUser) {
		int index = 0;
		try {
			html.append("<DIV>");
			for (Iterator<Tab> iterator = tabs.iterator(); iterator.hasNext();) {
				Tab tab = (Tab) iterator.next();
				html.append("<div id=\"" + field.getId() + index + "\">");
				// 防止死循环,避免Tab Field包含了所在的Form而造成死循环
				if (!field.get_form().getId().equals(tab.getFormId())) {
					html.append(tab.toPdfHtml(doc, runner, webUser));
				}
				html.append("</div>");
				index++;
			}

			html.append("</DIV>");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取所有页签表单
	 * 
	 * @return 页签表单
	 * @throws Exception
	 */
	public Collection<Form> getForms() throws Exception {
		Collection<Form> rtn = new ArrayList<Form>();
		for (Iterator<Tab> iterator = getTabs().iterator(); iterator.hasNext();) {
			Tab tab = (Tab) iterator.next();
			Form form = tab.getForm();
			if (form != null) {
				rtn.add(form);
			}
		}
		return rtn;
	}

	/**
	 * 根据打印时对应TabField的显示类型不同,默认为MODIFY,返回的结果字符串不同.
	 * 若Document不为空且打印时对应TabField的显示类型不为HIDDEN,
	 * <p>
	 * 并根据Form模版的TabField组件内容结合Document中的ITEM存放的值,返回重定义后的打印html文本
	 * 通过强化HTML标签及语法，表达TabField的布局、属性、事件、样式、等。
	 * 
	 * @param doc
	 *            Document(文档对象)
	 * @param runner
	 *            动态脚本执行器
	 * @param params
	 *            参数
	 * @param user
	 *            webuser
	 * @SuppressWarnings JsonUtil.toCollection 方法返回对象集类型不定
	 * @see cn.myapps.core.macro.runner.AbstractRunner#run(String, String)
	 * @return 打印重定义后的打印html为Form模版的文本框组件内容结合Document中的ITEM存放的值
	 * @throws Exception
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String toPrintHtmlTxt(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		StringBuffer html = new StringBuffer();

		try {
			Collection<Tab> tabs = (Collection) JsonUtil.toCollection(field
					.getRelStr(), Tab.class);
			html.append("<DIV id=\"" + field.getId() + "\">");
			html.append("</DIV>");
			appendTabContentsPrintHtml(html, runner, tabs, doc, webUser);
			return html.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public String toPdfHtmlTxt(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		StringBuffer html = new StringBuffer();

		try {
			Collection<Tab> tabs = (Collection) JsonUtil.toCollection(field
					.getRelStr(), Tab.class);
			html.append("<DIV id=\"" + field.getId() + "\">");
			html.append("</DIV>");
			appendTabContentsPdfHtml(html, runner, tabs, doc, webUser);
			return html.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取页签刷新脚本
	 * 
	 * @param runner
	 *            动态脚本执行器
	 * @param doc
	 *            文档对象
	 * @param webUser
	 *            webUser
	 * @return 页签刷新脚本
	 */
	public String getRefreshScript(IRunner runner, Document doc, WebUser webUser,int displayType)
			throws Exception {
		StringBuffer buffer = new StringBuffer();
		// 参数中获取选中的tabId
		String selectedId = "";// doc.get_params().getParameterAsString("_tabid");
		// 修正一个页面有多个选项卡的问题
		if (!StringUtil
				.isBlank(doc.get_params().getParameterAsString("_tabid"))) {
			String[] selectedIds = doc.get_params().getParameterAsString(
					"_tabid").split(";");
			for (int i = 0; i < selectedIds.length; i++) {
				String[] _ids = selectedIds[i].split("#");
				if (_ids[0].equals(field.getId())) {
					selectedId = _ids[1];
					break;
				}
			}
		} else {
			selectedId = doc.get_params().getParameterAsString(getName());
		}

		boolean isloaded = doc.get_params().getParameterAsBoolean(
				getName() + "_" + IS_LOADED);

		boolean isOneSelected = false; // 有一个tab被选中
		Tab firstShowTab = null; // 第一个显示的tab

		Collection<Object> hiddenList = new ArrayList<Object>();
		Collection<Object> showList = new ArrayList<Object>();
		for (Iterator<Tab> iterator = getTabs().iterator(); iterator.hasNext();) {
			Tab tab = (Tab) iterator.next();
			boolean isHidden = false;
			if (tab.isCalculateOnRefresh()) {
				isHidden = tab.isHidden(this, runner);
				if("view".equals(tab.getType())){
					buffer.append(tab.toHtml4Refresh(doc, runner, webUser,false,displayType));
				}else if("form".equals(tab.getType())){
					// 已加载仍需要重计算
					Collection<FormField> fields = tab.getFields();
					for (Iterator<FormField> iter = fields.iterator(); iter
							.hasNext();) {
						FormField field = (FormField) iter.next();
						// 重新刷新字段显示脚本
						if(field.isCalculateOnRefresh()){
							if (field instanceof TabField) {
								buffer.append(field.getRefreshScript(runner, doc,
										webUser));
							} else {
								buffer.append(field.getRefreshScript(runner, doc,
										webUser, isHidden));
							}
						}
					}
				}
				
				if (isHidden) {
					hiddenList.add("'" + tab.getFormId() + "'");
				} else {
					showList.add("'" + tab.getFormId() + "'");

					if (firstShowTab == null) {
						firstShowTab = tab;
					}

					if (!isloaded) { // 设置为已加载 (控制全刷新、或按需刷新)
						buffer.append("document.getElementById('");
						buffer.append(TAB_CONTENT).append(tab.getFormId())
								.append("').isloaded = true;\n;");
					}

					if (tab.getFormId().equals(selectedId)) {// 刷新选中的Tab
						isOneSelected = true;
					}
				}
			}
		}

		// 如果当前选中的tab已隐藏，则显示第一个不隐藏的tab
		if (!isOneSelected && firstShowTab != null) {
			doc.get_params().removeParameter("_tabid");
			doc.get_params().setParameter(getName(), firstShowTab.getFormId());
			return getRefreshScript(runner, doc, webUser,displayType);
		}

		// 显示隐藏Tab脚本
		if (!showList.isEmpty()) {
			buffer.append("ddtabmenu.showMenus('").append(field.getId())
					.append("', " + showList + ");\n");
		}

		// 选中页签脚本
		if (!StringUtil.isBlank(selectedId)) {
			buffer.append("ddtabmenu.showsubmenuById('").append(field.getId())
					.append("', '");
			buffer.append(selectedId);
			buffer.append("');\n");
		}

		if (!hiddenList.isEmpty()) {
			buffer.append("ddtabmenu.hideMenus('").append(field.getId())
					.append("', " + hiddenList + ");\n");
		}

		return buffer.toString();
	}

	public boolean isRender(String destVal, String origVal) {
		return true;
	}

	/**
	 * 用于为手机平台XML串生成
	 * 
	 * @param runner
	 *            动态脚本执行器
	 * @param doc
	 *            文档对象
	 * @param webUser
	 *            webUser
	 * @return 手机平台XML串生成
	 */
	public String toMbXMLText(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		StringBuffer html = new StringBuffer();
		int index = 0;
		html.append("<" + MobileConstant.TAG_TAB + " "
				+ MobileConstant.ATT_NAME + "='" + getName() + "' >");
		// Collection tabs = getTabs();
		for (Iterator<Tab> iterator = getTabs().iterator(); iterator.hasNext();) {
			Tab tab = (Tab) iterator.next();
			StringBuffer suffix = new StringBuffer();
			suffix.append("Tab.").append(tab.getName());
			suffix.append("(").append(tab.getFormId()).append(")");
			suffix.append(".hiddenPrintScript");
			// String temp = tab.getHiddenPrintScript();
			boolean isHidden = field.runBooleanScript(runner,
					suffix.toString(), tab.getHiddenScript());
			String tabXml = tab.toMbXML(doc, runner, webUser, isHidden);
			if (!isHidden && tabXml != null && tabXml.trim().length() > 0) {
				html.append("<" + MobileConstant.TAG_TABLI);
				if (tab.isHidden(this, runner)) {
					html.append(" ").append(MobileConstant.ATT_HIDDEN).append(
							" ='true' ");
				}
				if (tab.isRefreshOnChanged()) {
					html.append(" " + MobileConstant.ATT_REFRESH + "='true' ");
				}
				html.append(" " + MobileConstant.ATT_NAME + " ='");
				html.append(HtmlEncoder.encode(tab.getName()));
				html.append("' >");
				html.append(tabXml);
				html.append("</" + MobileConstant.TAG_TABLI + ">");
			}
			index++;
		}
		html.append("</" + MobileConstant.TAG_TAB + ">");
		index = 0;
		return html.toString();
	}
	
	/**
	 * 用于为手机平台XML串生成
	 * 适用于2.5 sp9版本以后
	 * 
	 * @param runner
	 *            动态脚本执行器
	 * @param doc
	 *            文档对象
	 * @param webUser
	 *            webUser
	 * @return 手机平台XML串生成
	 */
	public String toMbXMLText2(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		StringBuffer html = new StringBuffer();
//		int index = 0;
		html.append("<").append(MobileConstant2.TAG_TABFIELD);
		html.append(" ").append(MobileConstant2.ATT_NAME).append("='").append(getName()).append("'>");
		// Collection tabs = getTabs();
		for (Iterator<Tab> iterator = getTabs().iterator(); iterator.hasNext();) {
			Tab tab = (Tab) iterator.next();
			StringBuffer suffix = new StringBuffer();
			suffix.append("Tab.").append(tab.getName());
			suffix.append("(").append(tab.getFormId()).append(")");
			suffix.append(".hiddenPrintScript");
			// String temp = tab.getHiddenPrintScript();
			boolean isHidden = field.runBooleanScript(runner,
					suffix.toString(), tab.getHiddenScript());
			
			String tabXml = "";
			if(!isHidden){
				if(tab.getType().equals("view")){
					StringBuffer viewsb = new StringBuffer();
					ViewProcess viewProcess = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
					String viewId =tab.getFormId();
					tab.view = (View) viewProcess.doView(viewId);
					if(tab.view != null){
						viewsb.append("<").append(MobileConstant2.TAG_INCLUDEFIELD);
						
						viewsb.append(" ").append(MobileConstant2.ATT_VIEWID).append("='").append(tab.view.getId()).append("'");
						if (tab.isRelate()) {// 是否有父子关系
							viewsb.append(" ").append(MobileConstant2.ATT_RELATE).append("='true'");
							viewsb.append(" ").append(MobileConstant2.ATT_PARENTID).append("='").append(doc.getId()).append("'");
						}
						
						viewsb.append(">");
						viewsb.append("</").append(MobileConstant2.TAG_INCLUDEFIELD).append(">");
						tabXml = viewsb.toString();
					}
				}else if(tab.getType().equals("form")){
					Collection<FormField> fields = tab.getForm().getFields();
					for(Iterator<FormField> itfield = fields.iterator();itfield.hasNext();){
						FormField field = itfield.next();
						tabXml += field.toMbXMLText2(doc, runner, webUser);
					}
				}
			}
			if (!isHidden && tabXml != null && tabXml.trim().length() > 0) {
				html.append("<").append(MobileConstant2.TAG_TABLE);
                if (tab.getName() != null && tab.getName().equals(getSelectedTab(runner).getName())) {
                    html.append(" ").append(MobileConstant2.ATT_SELECTED).append("='true'");
                }
				if (tab.isHidden(this, runner)) {
					html.append(" ").append(MobileConstant2.ATT_HIDDEN).append("='true'");
				}
				if (tab.isRefreshOnChanged()) {
					html.append(" ").append(MobileConstant2.ATT_REFRESH).append("='true'");
				}
				html.append(" ").append(MobileConstant2.ATT_NAME).append("='");
				html.append(HtmlEncoder.encode(tab.getName()));
				html.append("'>");
				html.append(tabXml);
				html.append("</").append(MobileConstant2.TAG_TABLE).append(">");
			}
//			index++;
		}
		html.append("</").append(MobileConstant2.TAG_TABFIELD).append(">");
//		index = 0;
		return html.toString();
	}

	public String getValueMapScript() {
		StringBuffer scriptBuffer = new StringBuffer();
		scriptBuffer.append("valuesMap['" + field.getName()
				+ "'] = ddtabmenu.getSelected('" + field.getId() + "');");
		scriptBuffer.append("valuesMap['" + field.getName() + "_" + IS_LOADED
				+ "'] = ddtabmenu.isloaded('" + field.getId() + "');");

		return scriptBuffer.toString();
	}
}
