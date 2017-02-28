package cn.myapps.core.resource.action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.BaseAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.links.ejb.LinkProcess;
import cn.myapps.core.resource.ejb.ResourceProcess;
import cn.myapps.core.resource.ejb.ResourceType;
import cn.myapps.core.resource.ejb.ResourceVO;
import cn.myapps.core.superuser.ejb.SuperUserProcess;
import cn.myapps.core.user.action.OnlineUsers;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.xmpp.XMPPSender;
import cn.myapps.core.xmpp.notification.MenuNotification;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;


/**
 * Resouce Action using the web-work framework.
 */
public class ResourceAction extends BaseAction<ResourceVO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7354536469938737579L;

	private String _parent;

	private String _link;

	/**
	 * 视图id
	 */
	protected String _viewid;

	/**
	 * 表单id
	 */
	protected String _formid;

	// public String getLinkId() {
	// return (((ResourceVO) getContent()) != null && ((ResourceVO)
	// getContent()).getLink() != null) ? ((ResourceVO) getContent())
	// .getLink().getId()
	// : null;
	// }
	//
	// public void setLinkId(String linkId) throws Exception {
	// if (!StringUtil.isBlank(linkId)) {
	// LinkProcess linkProcess = (LinkProcess)
	// ProcessFactory.createProcess(LinkProcess.class);
	// LinkVO link = (LinkVO) linkProcess.doView(linkId);
	// ((ResourceVO) getContent()).setLink(link);
	// }
	// }

	private String _linkType;
	
	public String get_linkType() {
		return _linkType;
	}

	public void set_linkType(String linkType) {
		_linkType = linkType;
	}

	public String get_link() {
		return this._link;
	}

	public void set_link(String link) {
		this._link = link;
	}

	public String get_dest() {
		return _dest;
	}

	public void set_dest(String _dest) {
		this._dest = _dest;
	}

	public String get_parent() {
		return _parent;
	}

	public void set_parent(String _parent) {
		this._parent = _parent;
	}

	private String _dest;

	public String get_viewid() {// 获取视图id
		return _viewid;
	}

	public void set_viewid(String viewid) {// 设置视图id
		_viewid = viewid;
	}

	public String get_formid() {// 获取表单id
		return _formid;
	}

	public void set_formid(String formid) {// 设置表单id
		_formid = formid;
	}

	/**
	 * @SuppressWarnings 工厂方法不支持泛型
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ResourceAction() throws Exception {
		super(ProcessFactory.createProcess(ResourceProcess.class),
				new ResourceVO());
	}

	/**
	 * The navitesup action
	 * 
	 * @return "sucess".
	 * @throws Exception
	 */
	public String doNavigatesup() throws Exception {
		return SUCCESS;
	}

	/**
	 * Retrieve the top menus.
	 * 
	 * @return Returns the top menus collection.
	 * @throws Exception
	 */
	public Collection<ResourceVO> get_topmenus(String application,
			String domain, ParamsTable params) throws Exception {
		
		return getSubMenus(null, application, domain, params);
	}

	public String getHtmlOfSubMenus() throws UnsupportedEncodingException {
		String resourceid = this.getParams().getParameterAsString("menusid");
		int deep = Integer.parseInt(this.getParams().getParameterAsString(
				"deep"));
		String domain = this.getParams().getParameterAsString("domainid");
		ResourceHelper rh;
		String html = "";
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			rh = new ResourceHelper();
			try {
				html = rh.getHtml(rh
						.searchSubResource(resourceid, deep, domain));
				response.setContentType("text/html;charset=UTF-8");
				response.getWriter().write(html);
			}  catch (OBPMValidateException e) {
				addFieldError("", e.getValidateMessage());
			}catch (Exception e) {
				this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
				e.printStackTrace();
			}
			return html;
		} catch (ClassNotFoundException e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public Collection<ResourceVO> get_topmenus(String application, String domain)
			throws Exception {
		ParamsTable params = new ParamsTable();
		params.setParameter("xi_type", ResourceType.RESOURCE_TYPE_MOBILE);
		params.setParameter("xi_showType", ResourceVO.SHOW_TYPE_FLOW_CENTER);
		return get_topmenus(application, domain, params);
	}
	
	public Collection<ResourceVO> get_topmenus4Mobile(String application, String domain)
			throws Exception {
		ParamsTable params = new ParamsTable();
		params.setParameter("xi_type", ResourceType.RESOURCE_TYPE_HTML);
		params.setParameter("xi_showType", ResourceVO.SHOW_TYPE_FLOW_CENTER);
		return get_topmenus(application, domain, params);
	}
	
	public Collection<ResourceVO> get_topmenus4FlowCenter(String application, String domain)
			throws Exception {
		ParamsTable params = new ParamsTable();
		params.setParameter("xi_type", ResourceType.RESOURCE_TYPE_MOBILE);
		params.setParameter("xi_showType", ResourceVO.SHOW_TYPE_MENU);
		return get_topmenus(application, domain, params);
	}

	/**
	 * Retrieve the sub menus (get the parent menu id from the web paramenter).
	 * 
	 * @return The sub menus collection.
	 * @throws Exception
	 */
	public Collection<ResourceVO> get_submenus(String application,
			String domain, ParamsTable params) throws Exception {
		Collection<ResourceVO> menus = null;
		try {
			String _pid = ServletActionContext.getRequest().getParameter(
					"_parent");
			ResourceVO parent = (ResourceVO) process.doView(_pid);

			// if (parent != null) {
			menus = getSubMenus(parent, application, domain, params);
			// parent.setSuperiorid("-1");
			// menus.add(parent);
		} catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}

		// }
		return menus;
	}

	/**
	 * Retrieve the sub menus
	 * 
	 * @param startNode
	 *            The parent menu.
	 * @return The sub menus under the parment menu.
	 * @throws Exception
	 */
	public Collection<ResourceVO> getSubMenus(ResourceVO startNode,
			String application) throws Exception {
		ArrayList<ResourceVO> list = new ArrayList<ResourceVO>();
		ParamsTable params = new ParamsTable();
		params.setParameter("_orderby", "orderno");
		params.setParameter("application", getApplication());
		Collection<ResourceVO> cols = process
				.doSimpleQuery(params, application);

		Iterator<ResourceVO> iter = cols.iterator();
		while (iter.hasNext()) {
			ResourceVO vo = (ResourceVO) iter.next();
			if (startNode == null) {
				if (vo.getSuperior() == null) {
					list.add(vo);
				}
			} else {
				if (vo.getSuperior() != null) {
					ResourceVO superior = vo.getSuperior();
					while (superior != null) {
						if (superior.getId().equals(startNode.getId())) {
							list.add(vo);
							break;
						}
						superior = superior.getSuperior();
					}
				}
			}
		}

		return list;
	}

	public String toCopyResource() throws Exception {
		return SUCCESS;
	}

	public String copyResource() {
		try {
			String menus = this.getParams().getParameterAsString("menus");
			if (menus != null && !menus.equals("")) {
				this._selects = menus.split(";");
			}
			((ResourceProcess) process).copyResources(this.getApplication(),
					this._selects, this.get_dest());
			this.addActionMessage("{*[cn.myapps.core.resource.copy_mobilemenu_success]*}");
			return SUCCESS;

		} catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}

	}

	public Collection<ResourceVO> getSubMenus(String startNodeid,
			String application, String domain) throws Exception {
		ArrayList<ResourceVO> list = new ArrayList<ResourceVO>();
		ParamsTable params = new ParamsTable();
		params.setParameter("_orderby", "orderno");
		params.setParameter("application", getApplication());
		Collection<ResourceVO> cols = ((ResourceProcess) process)
				.doSimpleQuery(params, application);

		Iterator<ResourceVO> iter = cols.iterator();
		while (iter.hasNext()) {
			ResourceVO vo = (ResourceVO) iter.next();
			if (startNodeid == null) {
				if (vo.getSuperior() == null
						) {
					//多语言标签，有则替代前台菜单描述显示
					replaceDescription(vo);
					list.add(vo);
				}
			} else {
				if (vo.getSuperior() != null) {
					ResourceVO superior = vo.getSuperior();
					while (superior != null) {
						if (superior.getId().equals(startNodeid)) {
							//多语言标签，有则替代前台菜单描述显示
							replaceDescription(vo);
							list.add(vo);
							break;
						}
						superior = superior.getSuperior();
					}
				}
			}
		}

		return list;
	}
	
	public Collection<ResourceVO> getSubMenus(ResourceVO startNode,
			String application, String domain, ParamsTable params)
			throws Exception {
		
		ArrayList<ResourceVO> list = new ArrayList<ResourceVO>();

		params.setParameter("_orderby", "orderno");
		if(application.equals("")||application==null){
			return null;
		}
		params.setParameter("application", application);
		Collection<ResourceVO> cols = ((ResourceProcess) process)
				.doSimpleQuery(params, application);
		if(application==null||application.equals("")){
			return null;
		}

		Iterator<ResourceVO> iter = cols.iterator();
		while (iter.hasNext()) {
			ResourceVO vo = (ResourceVO) iter.next();
			if (startNode == null) {
				if (vo.getSuperior() == null
						) {
					//多语言标签，有则替代前台菜单描述显示
					replaceDescription(vo);
					list.add(vo);
				}
			} else {
				if (vo.getSuperior() != null) {
					ResourceVO superior = vo.getSuperior();
					while (superior != null) {
						if (superior.getId().equals(startNode.getId())) {
							//多语言标签，有则替代前台菜单描述显示
							replaceDescription(vo);
							list.add(vo);
							break;
						}
						superior = superior.getSuperior();
					}
				}
			}
		}

		return list;
	}

	public String get_superiorid() {
		ResourceVO resource = (ResourceVO) getContent();
		if (resource.getSuperior() != null) {
			return resource.getSuperior().getId();
		} else {
			return null;
		}
	}

	public void set_superiorid(String _superiorid) {
		ResourceVO content = (ResourceVO) this.getContent();

		try {
			ResourceVO superior = (ResourceVO) process.doView(_superiorid);
			content.setSuperior(superior);
		} catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
	}

	/*
	 * 
	 */
	public Map<String, String> get_menus(String application) throws Exception {
		Collection<ResourceVO> dc = process.doSimpleQuery(new ParamsTable(),
				application);
		Map<String, String> dm = ((ResourceProcess) process)
				.deepSearchMenuTree(dc, null, getContent().getId(), 0);

		return dm;
	}

	public Map<String, String> get_menusExculdeMobile(String application)
			throws Exception {
		ParamsTable params = new ParamsTable();
		params.setParameter("xi_type", ResourceType.RESOURCE_TYPE_MOBILE);

		Collection<ResourceVO> dc = process.doSimpleQuery(params, application);

		Map<String, String> dm = ((ResourceProcess) process)
				.deepSearchMenuTree(dc, null, getContent().getId(), 0);

		return dm;
	}

	public Map<String, String> get_menusOfMobile(String application)
			throws Exception {
		ParamsTable params = new ParamsTable();
		params.setParameter("t_type", ResourceType.RESOURCE_TYPE_MOBILE);

		Collection<ResourceVO> dc = process.doSimpleQuery(params, application);
		Map<String, String> dm = ((ResourceProcess) process)
				.deepSearchMenuTree(dc, null, getContent().getId(), 0);

		return dm;
	}

	/**
	 * 
	 */
	@Override
	public String doSave() {
		try {
			ResourceVO vo = (ResourceVO) getContent();
			vo.setLinkName(vo.getDescription());
			ResourceVO compare = queryResourceByDes(vo.getDescription(), vo
					.getSuperior(), vo.getApplicationid());
			if (compare != null) {
				if (!(compare.getId().equals(vo.getId()) && compare
						.getDescription().equals(vo.getDescription()))) {
					throw new OBPMValidateException("{*[page.description.error]*}");
				}
			}
			if (StringUtil.isBlank(vo.getId())) {
				process.doCreate(getContent());
			} else {
				process.doUpdate(getContent());
			}
			/**
			 * 增加了xmpp消息发送,提醒obpm-spark客户端更新
			 */
			sendNotification();
			this.addActionMessage("{*[Save_Success]*}");
			return SUCCESS;

		}  catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}

	/** 保存并新建 */
	public String doSaveAndNew() {
		try {
			ResourceVO vo = (ResourceVO) getContent();
			vo.setLinkName(vo.getDescription());
			ResourceVO compare = queryResourceByDes(vo.getDescription(), vo
					.getSuperior(), vo.getApplicationid());
			if (compare != null) {
				if (!(compare.getId().equals(vo.getId()) && compare
						.getDescription().equals(vo.getDescription()))) {
					throw new OBPMValidateException("{*[page.description.error]*}");
				}
			}
			if (StringUtil.isBlank(vo.getId())) {
				process.doCreate(getContent());
			} else {
				process.doUpdate(getContent());
			}
			this.addActionMessage("{*[Save_Success]*}");
			setContent(new ResourceVO());
			/**
			 * 增加了xmpp消息发送,提醒obpm-spark客户端更新
			 */
			sendNotification();
			return SUCCESS;

		} catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}

	/**
	 * 根据菜单描述和菜单上级查找菜单
	 * 
	 * @param description
	 * @param superior
	 * @param application
	 * @return
	 */
	private ResourceVO queryResourceByDes(String description,
			ResourceVO superior, String application) {
		try {
			ResourceProcess process = (ResourceProcess) ProcessFactory
					.createProcess(ResourceProcess.class);
			ParamsTable params = new ParamsTable();
			params.setParameter("s_description", description);
			Collection<ResourceVO> ress = null;
			if (superior != null) {
				params.setParameter("s_superior", superior.getId());
				ress = process.doSimpleQuery(params, application);
			} else {
				ress = get_topmenus(application, getUser().getDomainid(),
						params);
			}
			if (ress != null && !ress.isEmpty()) {
				return (ResourceVO) ress.iterator().next();
			}
		} catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return null;
	}

	public String delete() {
		boolean flag = false;
		String menus = this.getParams().getParameterAsString("menus");
		if (menus != null && !menus.equals("")) {
			this._selects = menus.split(";");
		}
		try {
			ResourceProcess _process = (ResourceProcess) ProcessFactory.createProcess(ResourceProcess.class);
//			LinkProcess linkProcess = (LinkProcess) ProcessFactory.createProcess(LinkProcess.class);
			for (int i = 0; i < this.get_selects().length; i++) {
				
				ResourceVO res = (ResourceVO) _process.doView(get_selects()[i]);
				_process.doCreateOrUpdate(res);
				//当菜单链接没被其他资源引用时,删除掉对应的链接
				getParams().setParameter("s_superior", this.get_selects()[i]);
				if (((ResourceProcess) process).doQuery(getParams()).rowCount > 0) {
					flag = true;
					break;
				}
			}
		} catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
		if (!flag) {
			String rtn = super.doDelete();
			/**
			 * 增加了xmpp的消息发送,此消息将发送到obpm-spark的各个客户端
			 */
			sendNotification();
			return rtn;
		} else {
			addFieldError("1", "{*[core.resource.hasuser]*}");
			return INPUT;
		}
	}

	/**
	 * xmpp消息发送,菜单的增删改将触发此动作
	 * 
	 * 通知所有的obpm-spark客户端更新菜单
	 * 
	 * @author keezzm
	 * @date 2011-08-17
	 * @last modified by keezzm
	 */
	private void sendNotification() {
		try {
			// 发送XMPP信息
			MenuNotification notification = MenuNotification
					.newInstance(MenuNotification.ACTION_UPDATE);
			SuperUserProcess superUserProcess = (SuperUserProcess) ProcessFactory
					.createProcess(SuperUserProcess.class);
			/**
			 * 默认发送者为admin
			 */
			notification.setSender(superUserProcess.getDefaultAdmin());
			/**
			 * 添加接收者,为所有在线用户
			 */
			DataPackage<WebUser> dataPackage = OnlineUsers
					.doQuery(new ParamsTable());
			Collection<WebUser> users = dataPackage.getDatas();
			for (Iterator<WebUser> iterator = users.iterator(); iterator
					.hasNext();) {
				WebUser webUser = iterator.next();
				notification.addReceiver(webUser);
			}
			XMPPSender.getInstance().processNotification(notification);
		} catch (OBPMValidateException e) {
			LOG.warn("XMPP Notification Error", e);
			addFieldError("1", e.getValidateMessage());
		} catch (Exception e) {
			LOG.warn("XMPP Notification Error", e);
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
	}

	/**
	 * 对保存menu操作进行校验 校验输入的menu名称是否为空 校验输入的名称是否存在
	 */
	private boolean validateDoCreateMenu() {
		ResourceVO rVO = (ResourceVO) this.getContent();
		if (rVO.getDescription() == null
				|| rVO.getDescription().trim().equals("")) {
			this.addFieldError("1", "{*[page.name.notexist]*}");
			return false;// 校验不通过
		}
		try {
			ResourceVO from_rVO = (ResourceVO) this.getContent();
			ResourceProcess rp = (ResourceProcess) ProcessFactory
					.createProcess(ResourceProcess.class);
			// 获取要创建的菜单的同一级的所有菜单
			Iterator<ResourceVO> menusInSuperior = rp.doGetDatasByParent(
					from_rVO.getSuperior().getId()).iterator();
			while (menusInSuperior.hasNext()) {
				// 判断同一级菜单下有没有重名
				if (((ResourceVO) menusInSuperior.next()).getDescription()
						.equals(from_rVO.getDescription())) {
					this.addFieldError("*", "{*[menu.name.exist]*}");
					return false;
				}
			}
		}  catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return true;// 校验通过
	}

	/**
	 * 在视图中创建Menu
	 * 
	 * @last modified by keezzm
	 */
	public String doCreateMenuByView() {
		if (!validateDoCreateMenu())
			return INPUT;// 校验menu名称是否为空
		try {
			ViewProcess vp = (ViewProcess) ProcessFactory
					.createProcess(ViewProcess.class);
			View v = (View) vp.doView(_viewid);
			if (v == null)
				return INPUT;

			// 设置Menu相关属性
			ResourceVO from_rVO = (ResourceVO) this.getContent();
			ResourceProcess rp = (ResourceProcess) ProcessFactory
					.createProcess(ResourceProcess.class);
			ResourceVO spMenu = (ResourceVO) rp.doView(from_rVO.getSuperior()
					.getId());

			ResourceVO rVO = new ResourceVO();
			rVO.setSuperior(spMenu);// 上级superior
			rVO.setActionContent(_viewid);
			rVO.setApplicationid(v.getApplicationid());// 设置链接(link)应用id
			rVO.setLinkName(v.getName());
			rVO.setLinkType(ResourceVO.LinkType.VIEW.getCode());// 连接(link)视图类型
			rVO.setQueryString("[]");
			rVO.setModuleid(v.getModule().getId());
			rVO.setDescription(from_rVO.getDescription());// 菜单名
			rVO.setApplicationid(v.getApplicationid());
			rVO.setApplication(v.getApplicationid());//
			if (spMenu != null) {
				rVO.setType(spMenu.getType());// 菜单类型为上级菜单的类型
			} else {
				rVO.setType(ResourceType.RESOURCE_TYPE_MENU);// 否则为默认菜单类型
			}
			// 创建menu(保存到数据库)
			rp.doCreateMenu(rVO);
			/**
			 * 增加了xmpp消息发送,提醒obpm-spark客户端更新
			 */
			sendNotification();
			return SUCCESS;
		}  catch (OBPMValidateException e) {
			addFieldError("menuCreateError", "{*[cn.myapps.core.resource.create_fail]*}:"+e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}

	/**
	 * 在表单中创建Menu
	 * 
	 * @last modified by keezzm
	 * @return
	 */
	public String doCreateMenuByForm() {
		if (!validateDoCreateMenu())
			return INPUT;// 校验菜单名是否为空
		try {
			FormProcess fp = (FormProcess) ProcessFactory
					.createProcess(FormProcess.class);
			Form f = (Form) fp.doView(this._formid);
			if (f == null){
				this.addFieldError("*", "{*[页面已失效，请重新打开！]*}");
				return INPUT;
			}

			// 设置Menu相关属性
			ResourceVO from_rVO = (ResourceVO) this.getContent();
			ResourceProcess rp = (ResourceProcess) ProcessFactory
					.createProcess(ResourceProcess.class);
			// 获取上级菜单
			ResourceVO spMenu = (ResourceVO) rp.doView(from_rVO.getSuperior()
					.getId());

			ResourceVO rVO = new ResourceVO();
			rVO.setSuperior(spMenu);// 上级
			rVO.setActionContent(this._formid);
			rVO.setLinkName(f.getName());
			rVO.setApplicationid(f.getApplicationid());// 设置链接(link)应用id
			rVO.setLinkType(ResourceVO.LinkType.FORM.getCode());// 连接(link)表单类型
			rVO.setQueryString("[]");
			rVO.setModuleid(f.getModule().getId());
			rVO.setDescription(from_rVO.getDescription());// 菜单名
			rVO.setApplicationid(f.getApplicationid());
			rVO.setApplication(f.getApplicationid());//
			if (spMenu != null) {
				rVO.setType(spMenu.getType());// 菜单类型为上级菜单的类型
			} else {
				rVO.setType(ResourceType.RESOURCE_TYPE_MENU);// 否则为默认菜单类型
			}
			// 创建菜单（保存到数据库）
			rp.doCreateMenu(rVO);
			/**
			 * 增加了xmpp消息发送,提醒obpm-spark客户端更新
			 */
			sendNotification();
			return SUCCESS;
		} catch (OBPMValidateException e) {
			addFieldError("menuCreateError", "{*[Create]*}{*[Fail]*}"+e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}
	
	/**
	 * 菜单新增多语言标签，有则替代前台菜单描述显示
	 * @param vo
	 */
	public static void replaceDescription(ResourceVO vo){
		if(!StringUtil.isBlank(vo.getMultiLanguageLabel())){
			vo.setDescription("{*[" + vo.getMultiLanguageLabel() + "]*}");
		}
	}

}
