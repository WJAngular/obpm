package cn.myapps.core.dynaform.view.action;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.constans.Web;
import cn.myapps.core.deploy.module.ejb.ModuleProcess;
import cn.myapps.core.deploy.module.ejb.ModuleVO;
import cn.myapps.core.dynaform.view.ejb.Column;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.dynaform.view.ejb.type.CalendarType;
import cn.myapps.core.dynaform.view.ejb.type.GanttType;
import cn.myapps.core.dynaform.view.ejb.type.MapType;
import cn.myapps.core.dynaform.view.ejb.type.TreeType;
import cn.myapps.core.superuser.ejb.SuperUserProcess;
import cn.myapps.core.superuser.ejb.SuperUserVO;
import cn.myapps.core.sysconfig.ejb.CheckoutConfig;
import cn.myapps.core.user.ejb.PreviewUser;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.property.PropertyUtil;
import cn.myapps.util.sequence.Sequence;


public class ViewDesignTimeAction extends ViewAction {
	
	private static final Logger LOG = Logger.getLogger(ViewDesignTimeAction.class);
	
	private static final long serialVersionUID = 3307137490213767068L;

	private String checkoutConfig;
	
	private String linkName;
	
	private String _isPreview;
	
	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}
	
	public String get_isPreview() {
		return _isPreview;
	}

	public void set_isPreview(String isPreview) {
		_isPreview = isPreview;
	}
	
	public ViewDesignTimeAction() throws ClassNotFoundException {
		super();
	}

	public String getCheckoutConfig() {
		PropertyUtil.reload("checkout");
		String checkoutConfig = PropertyUtil.get(CheckoutConfig.INVOCATION);
		return checkoutConfig;
	}

	public void setCheckoutConfig(String checkoutConfig) {
		this.checkoutConfig = checkoutConfig;
	}
	
	/**
	 * 显示视图view数据列表(后台调用)
	 * 
	 * @return result.
	 * @throws Exception
	 */
	public String doPreView() throws Exception {
		try {
			PreviewUser user = new PreviewUser(new UserVO());
			String skinType = getParams().getParameterAsString("_skinType");
			if(!StringUtil.isBlank(skinType)){
				ServletActionContext.getRequest().setAttribute("_skinType", skinType);
				user.setSkinType(skinType);
			}
			ServletActionContext.getRequest().getSession().setAttribute(Web.SESSION_ATTRIBUTE_PREVIEW_USER, user);
			set_isPreview("true");
			return doPreDisplayView();
			//displayByView((View) getContent());
		}catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}

		//return SUCCESS;
	}
	
	/**
	 * 预览
	 * 
	 * @return
	 */
	public String doPreDialogView() {
		try {
			return getSuccessResult((View) getContent(), DO_DIALOG_VIEW);
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
	 * 预览
	 */
	public String doPreDisplayView() throws Exception {
		try {
			return getSuccessResult(view, DO_DISPLAY_VIEW);
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
	 * 显示列表
	 * 
	 * @return
	 */
	public String list() {
		// 避免了干扰查询参数
		ParamsTable params = this.getParams();
		getParams().removeParameter("content.auth_role");
		getParams().removeParameter("content.auth_user");
		getParams().removeParameter("content.auth_fields");
		int lines = 10;
		Cookie[] cookies = ServletActionContext.getRequest().getCookies();
		for(Cookie cookie : cookies){
			if(Web.FILELIST_PAGELINE.equals(cookie.getName())){
				lines = Integer.parseInt(cookie.getValue());
			}
			   cookie.getName();
			   cookie.getValue();
		}
		params.removeParameter("_pagelines");
		params.setParameter("_pagelines", lines);
		return super.doList();	
	}
	
	/**
	 * 复制视图
	 * @return
	 */
	public String doCopy(){
		try {
			ViewProcess viewProcess = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
			viewProcess.doCopyView(_selects);
		} catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[cn.myapps.core.dynaform.view.copy.error]*}",e));
			e.printStackTrace();
			return INPUT;
		}
		
		this.addActionMessage("{*[cn.myapps.core.dynaform.view.copy.success]*}");
		return SUCCESS;
	}
	
	/**
	 * 编辑
	 */
	public String doView() {
		String rtn ="";
		try{
			PropertyUtil.reload("checkout");
			String _checkoutConfig = PropertyUtil.get(CheckoutConfig.INVOCATION);
			String id = getParams().getParameterAsString("id");
			rtn = super.doView();
			View view = (View) getContent();
			if(_checkoutConfig.equals("true") && view.isCheckout() && !view.getCheckoutHandler().equals(getUser().getId())){
				SuperUserProcess sp = (SuperUserProcess) ProcessFactory.createProcess(SuperUserProcess.class);
				SuperUserVO speruser = (SuperUserVO) sp.doView(view.getCheckoutHandler());
				addFieldError("", "此视图已经被"+speruser.getName()+"签出，您目前没有修改的权限！");
//				addFieldError("", "{*[core.dynaform.form.message.warning.be.checkedout.by.other.developers]*}");
			}
			if (view.getLink() != null) {
				setLinkName(view.getLink().getName());
			}
			set_viewid(id);
		}catch(OBPMValidateException e){
			addFieldError("1", e.getValidateMessage());
		}catch(Exception e){
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}

		return rtn;
	}
	
	/**
	 * 保存view对象
	 * 
	 * @return 返回一个字符串,如果处理成功返回"SUCCESS",
	 */
	public String doSave() {
		try {
			View view = (View) (this.getContent());
			if(StringUtil.isBlank(view.getId())){
				view.setCheckout(true);
				view.setCheckoutHandler(getUser().getId());
			}
			set_viewid(view.getId());
			String name = view.getName();
			ParamsTable params = this.getParams();
			params.setParameter("s_name", name);
			//定义地图视图设置默认地址的缩放级别
			if(!StringUtil.isBlank(view.getCountry()) && !view.getCountry().equals("请选择")){
				view.setLevel("4");
			}
			if(!StringUtil.isBlank(view.getProvince()) && !view.getProvince().equals("请选择")){
				view.setLevel("8");
			}
			if(!StringUtil.isBlank(view.getCity()) && !view.getCity().equals("请选择")){
				view.setLevel("12");
			}
			if(!StringUtil.isBlank(view.getTown()) && !view.getTown().equals("请选择")){
				view.setLevel("14");
			}

			doValidate(view); // 校验

			if (StringUtil.isBlank(view.getId())) {
				view.setId(Sequence.getSequence());
				view.setSortId(Sequence.getTimeSequence());
				ModuleProcess mp = (ModuleProcess) ProcessFactory.createProcess(ModuleProcess.class);
				ModuleVO module = (ModuleVO) mp.doView(getParams().getParameterAsString("s_module"));
				view.setModule(module);
			}

			view.setLastmodifytime(new Date());
			view.setDescription(_resourcedesc);
           
			setContent(view);
			if(view.getRelatedForm()==null){
				addFieldError("[{*[Errors]*}]:  ", "{*[core.dynaform.form.action.FormHelper.noticeCreateFrom]*}");
				return INPUT;
			}
			return super.doSave();
		} catch (OBPMValidateException e) {
			LOG.error("doSave", e);
			addFieldError("[{*[Errors]*}]:  ", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			LOG.error("doSave", e);
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}
	
	/**
	 * 签出
	 * @return
	 * @throws Exception
	 */
	public String doCheckout() throws Exception {
		try {
			View view = (View) (this.getContent());
			view.setDescription(_resourcedesc);
			process.doCheckout(view.getId(), getUser());
			view.setCheckout(true);
			view.setCheckoutHandler(getUser().getId());
			setContent(view);
			this.addActionMessage("{*[core.dynaform.form.success.checkout]*}");
			return SUCCESS;
		} catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}
	
	/**
	 * 签入
	 * @return
	 * @throws Exception
	 */
	public String doCheckin() throws Exception {
		try {
			View view = (View) (this.getContent());
			view.setDescription(_resourcedesc);
			process.doCheckin(view.getId(), getUser());
			view.setCheckout(false);
			view.setCheckoutHandler("");
			setContent(view);
			this.addActionMessage("{*[core.dynaform.form.success.checkin]*}");
			return SUCCESS;
		}  catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}
	
	/**
	 * 对视图进行校验
	 * 
	 * @param view
	 * @throws Exception
	 */
	private void doValidate(View view) throws Exception {
		String name = "";
		ParamsTable params = this.getParams();
		if (view != null) {
			set_viewid(view.getId());
			name = view.getName();
			params.setParameter("s_name", name);
		
			// 检验重名
			View tempView = ((ViewProcess) process).getViewByName(name, application);
			if (tempView != null) {
				if (view.getId() == null || view.getId().trim().length() <= 0) {// 判断新建不能重名
					throw new OBPMValidateException("{*[viewExist]*}");
				} else if (view.getViewType() == tempView.getViewType()
						&& !view.getId().trim().equalsIgnoreCase(tempView.getId())) {// 修改不能重名
					throw new OBPMValidateException("{*[viewExist]*}");
				}
			}
			
			//当视图打开类型为网格视图时,不应有操作列
			Set<Column> columns = view.getColumns();
			Iterator<Column> colIterator = columns.iterator();
			while(colIterator.hasNext()){
				int tempOpenType = view.getOpenType();
				Column col = colIterator.next();
				String tempType = col.getType();
				if(Column.COLUMN_TYPE_OPERATE.equals(tempType) || Column.COLUMN_TYPE_LOGO.equals(tempType)){
					if(tempOpenType == View.OPEN_TYPE_GRID){
						throw new OBPMValidateException("{*[core.view.grid.operate]*}");
					}
				}
			}
		
		
		}
		
		// 非法字符串检验
		String invalidChars = getInvalidChars(name);
		if (!StringUtil.isBlank(invalidChars)) {
			throw new OBPMValidateException("{*[core.view.name.exist.invalidchar]*}: "
					+ invalidChars);
		}

		Map<String, Column> columnMapping = view.getViewTypeImpl().getColumnMapping();
		StringBuffer errors = new StringBuffer();
		if (view.getViewType() == View.VIEW_TYPE_GANTT) {// 检验甘特视图映射
			for (int i = 0; i < GanttType.DEFAULT_KEY_FIELDS.length; i++) {
				String fieldCode = GanttType.DEFAULT_KEY_FIELDS[i];
				if (!columnMapping.containsKey(fieldCode)) {
					errors.append(GanttType.DEFAULT_FIELDS.get(fieldCode)).append(",");
				}
			}
		} else if (view.getViewType() == View.VIEW_TYPE_TREE) {// 检验树形视图映射
			for (int i = 0; i < TreeType.DEFAULT_KEY_FIELDS.length; i++) {
				String fieldCode = TreeType.DEFAULT_KEY_FIELDS[i];
				if (!columnMapping.containsKey(fieldCode)) {
					errors.append(TreeType.DEFAULT_FIELDS.get(fieldCode)).append(",");
				}
			}
		} else if (view.getViewType() == View.VIEW_TYPE_MAP) {// 校验地图视图映射
			StringBuffer mapError = new StringBuffer();
			StringBuffer fieldError = new StringBuffer();
			if(!columnMapping.containsKey( MapType.DEFAULT_KEY_FIELDS[0])){
				mapError.append(MapType.DEFAULT_FIELDS.get(MapType.DEFAULT_KEY_FIELDS[0]));
				for (int i = 0; i < MapType.MAP_KEY_FIELDS.length; i++) {
					String fieldCode = MapType.MAP_KEY_FIELDS[i];
					if (!columnMapping.containsKey(fieldCode)) {
						fieldError.append(MapType.MAP_FIELDS.get(fieldCode)).append(",");
					}
				}
			}
			if(!StringUtil.isBlank(mapError.toString()) && !StringUtil.isBlank(fieldError.toString())){
				errors.append(mapError.toString()).append(" {*[OR]*} ").append(fieldError.toString());
			}
//			String isMapStr = params.getParameterAsString("isMap");
//			boolean isMap = params.getParameterAsBoolean("isMap");
//			if(isMap == true){//表单字段是地图控件
//				for (int i = 0; i < MapType.DEFAULT_KEY_FIELDS.length; i++) {
//					String fieldCode = MapType.DEFAULT_KEY_FIELDS[i];
//					if (!columnMapping.containsKey(fieldCode)) {
//						errors.append(MapType.DEFAULT_FIELDS.get(fieldCode)).append(",");
//					}
//				}
//			}else if(isMap == false && isMapStr != null){//表单字段非地图控件
//				for (int i = 0; i < MapType.MAP_KEY_FIELDS.length; i++) {
//					String fieldCode = MapType.MAP_KEY_FIELDS[i];
//					if (!columnMapping.containsKey(fieldCode)) {
//						errors.append(MapType.MAP_FIELDS.get(fieldCode)).append(",");
//					}
//				}
//			}
		} else if (view.getViewType() == View.VIEW_TYPE_CALENDAR) {// 校验日历视图映射
			for (int i = 0; i < CalendarType.DEFAULT_KEY_FIELDS.length; i++) {
				String fieldCode = CalendarType.DEFAULT_KEY_FIELDS[i];
				if (!columnMapping.containsKey(fieldCode)) {
					errors.append(CalendarType.DEFAULT_FIELDS.get(fieldCode)).append(",");
				}
			}
		}

		if (errors.length() > 0) {
			errors.deleteCharAt(errors.lastIndexOf(","));
			throw new OBPMValidateException("(" + errors.toString() + "){*[require.mapping]*}");
		}
	}

	private String getInvalidChars(String name) {
		String[] p = { "﹉", "＃", "＠", "＆", "＊", "※", "§", "〃", "№", "〓", "○",
				"●", "△", "▲", "◎", "☆", "★", "◇", "◆", "■", "□", "▼", "▽",
				"㊣", "℅", "ˉ", "￣", "＿", "﹍", "﹊", "﹎", "﹋", "﹌", "﹟", "﹠",
				"﹡", "♀", "♂", "?", "⊙", "↑", "↓", "←", "→", "↖", "↗", "↙",
				"↘", "┄", "—", "︴", "﹏", "（", "）", "︵", "︶", "｛", "｝", "︷",
				"︸", "〔", "〕", "︹", "︺", "【", "】", "︻", "︼", "《", "》", "︽",
				"︾", "〈", "〉", "︿", "﹀", "「", "」", "﹁", "﹂", "『", "』", "﹃",
				"﹄", "﹙", "﹚", "﹛", "﹜", "﹝", "﹞", "\"", "〝", "〞", "ˋ",
				"ˊ", "≈", "≡", "≠", "＝", "≤", "≥", "＜", "＞", "≮", "≯", "∷",
				"±", "＋", "－", "×", "÷", "／", "∫", "∮", "∝", "∧", "∨", "∞",
				"∑", "∏", "∪", "∩", "∈", "∵", "∴", "⊥", "∥", "∠", "⌒", "⊙",
				"≌", "∽", "√", "≦", "≧", "≒", "≡", "﹢", "﹣", "﹤", "﹥", "﹦",
				"～", "∟", "⊿", "∥", "㏒", "㏑", "∣", "｜", "︱", "︳", "|", "／",
				"＼", "∕", "﹨", "¥", "€", "￥", "£", "®", "™", "©", "，", "、",
				"。", "．", "；", "：", "？", "！", "︰", "…", "‥", "′", "‵", "々",
				"～", "‖", "ˇ", "ˉ", "﹐", "﹑", "﹒", "·", "﹔", "﹕", "﹖", "﹗",
				"-", "&", "*", "#", "`", "~", "+", "=", "(", ")", "^", "%",
				"$", "@", ";", ",", ":", "'", "\\", "/", ".", ">", "<",
				"?", "!", "[", "]", "{", "}" };
		for (int i = 0; i < p.length; i++) {
			if (name != null && name.contains(p[i])) {			
				return p[i];
			}
		}
		return "";
	}

}
