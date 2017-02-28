package cn.myapps.core.dynaform.printer.action;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.BaseAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.constans.Web;
import cn.myapps.core.deploy.module.ejb.ModuleProcess;
import cn.myapps.core.deploy.module.ejb.ModuleVO;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.dynaform.printer.ejb.Printer;
import cn.myapps.core.dynaform.printer.ejb.PrinterProcess;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.superuser.ejb.SuperUserProcess;
import cn.myapps.core.superuser.ejb.SuperUserVO;
import cn.myapps.core.sysconfig.ejb.CheckoutConfig;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.property.PropertyUtil;


/**
 * @author Happy
 * 
 */
public class PrinterAction extends BaseAction<Printer> {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(PrinterAction.class);

	private static final long serialVersionUID = -5834805743250764407L;
	private String fieldList;
	private String subViewList;
	private String _formid;
	private String _flowid;
	private String _docid;
	private String reportData;
	private String checkoutConfig;

	public String getFieldList() {
		return fieldList;
	}

	public void setFieldList(String fieldList) {
		this.fieldList = fieldList;
	}

	public String getSubViewList() {
		return subViewList;
	}

	public void setSubViewList(String subViewList) {
		this.subViewList = subViewList;
	}

	public String get_formid() {
		return _formid;
	}

	public void set_formid(String formid) {
		_formid = formid;
	}

	public String get_flowid() {
		return _flowid;
	}

	public void set_flowid(String flowid) {
		_flowid = flowid;
	}

	public String get_docid() {
		return _docid;
	}

	public void set_docid(String docid) {
		_docid = docid;
	}

	public String getCheckoutConfig() {
		PropertyUtil.reload("checkout");
		String _checkoutConfig = PropertyUtil.get(CheckoutConfig.INVOCATION);
		return _checkoutConfig;
	}

	public void setCheckoutConfig(String checkoutConfig) {
		this.checkoutConfig = checkoutConfig;
	}
	
	public String get_moduleid() {
		Printer printer = (Printer) getContent();
		if (printer.getModule() != null) {
			return printer.getModule().getId();
		}
		return null;
	}

	public void set_moduleid(String moduleid) {
		Printer printer = (Printer) getContent();
		if (moduleid != null) {
			ModuleProcess mp;
			try {
				mp = (ModuleProcess) ProcessFactory.createProcess((ModuleProcess.class));
				ModuleVO module = (ModuleVO) mp.doView(moduleid);
				printer.setModule(module);
			} catch (OBPMValidateException e) {
				addFieldError("1", e.getValidateMessage());
			}catch (Exception e) {
				this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
				e.printStackTrace();
			}
		}
	}
	public String doEdit() {
		try {
			PropertyUtil.reload("checkout");
			String _checkoutConfig = PropertyUtil.get(CheckoutConfig.INVOCATION);
			Map<?, ?> params = getContext().getParameters();

			String id = ((String[]) params.get("id"))[0];
			Printer printer = (Printer) process.doView(id);
			if(_checkoutConfig.equals("true") && printer.isCheckout() && !printer.getCheckoutHandler().equals(super.getUser().getId())){
				SuperUserProcess sp = (SuperUserProcess) ProcessFactory.createProcess(SuperUserProcess.class);
				SuperUserVO speruser = (SuperUserVO) sp.doView(printer.getCheckoutHandler());
				addFieldError("", "{*[cn.myapps.core.dynaform.printer.add_field_error_first]*}"+speruser.getName()+"{*[cn.myapps.core.dynaform.printer.add_field_error_second]*}");
			}
			setContent(printer);
		} catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}


		return SUCCESS;
	}

	/**
	 * 返回web 用户对象
	 * 
	 * @SuppressWarnings webwork 不支持泛型
	 * @return web user.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public WebUser getFrontUser() throws Exception {
		Map<String, Object> session = getContext().getSession();
		WebUser user = null;
		if (session == null || session.get(Web.SESSION_ATTRIBUTE_FRONT_USER) == null) {
			UserVO vo = new UserVO();
			vo.getId();
			vo.setName("GUEST");
			vo.setLoginno("guest");
			vo.setLoginpwd("");
			vo.setRoles(null);
			vo.setEmail("");
			// vo.setLanguageType(1);
			user = new WebUser(vo);
		} else {
			user = (WebUser) session.get(Web.SESSION_ATTRIBUTE_FRONT_USER);
		}
		return user;
	}

	public void setReportData(String reportData) {
		this.reportData = reportData;
	}

	public String getReportData() {
		return reportData;
	}

	/**
	 * @SuppressWarnings 工厂方法不支持泛型
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public PrinterAction() throws ClassNotFoundException {
		super(ProcessFactory.createProcess(PrinterProcess.class), new Printer());
	}

	
	public String doSave() {
	 try {
		 Printer printer = (Printer) getContent();
		 if(StringUtil.isBlank(printer.getId())){
			 printer.setCheckout(true);
			 printer.setCheckoutHandler(getUser().getId());
			}
		 
		 ParamsTable params = new ParamsTable();
		 params.setParameter("t_name", printer.getName());
		 Collection<Printer> collection = process.doSimpleQuery(params);
		 if(collection.size()>0){
			 for (Iterator<Printer> iterator = collection.iterator(); iterator.hasNext();) {
				Printer printer2 = (Printer) iterator.next();
				if(printer2.getModule().getId().equals(printer.getModule().getId()) && !printer2.getId().equals(printer.getId())){
					addFieldError("1","{*[core.form.exist]*}");
					return INPUT;
				}
			}
		 }
	 }catch (OBPMValidateException e) {
		 addFieldError("1", e.getValidateMessage());
		 return INPUT;
	 }catch (Exception e) {
		 this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
		 e.printStackTrace();
		 return INPUT;
	 }
	 	return super.doSave();
	}
	
	
	/**
	 * 签入
	 * @return
	 * @throws Exception
	 */
	public String doCheckin() throws Exception {
		try {
			Printer printer = (Printer) (this.getContent());
			process.doCheckin(printer.getId(), getUser());
			printer.setCheckout(false);
			printer.setCheckoutHandler("");
			this.addActionMessage("{*[core.dynaform.form.success.checkin]*}");
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
	 * 签出
	 * @return
	 * @throws Exception
	 */
	public String doCheckout() throws Exception {
		try {
			Printer printer = (Printer) (this.getContent());
			process.doCheckout(printer.getId(), getUser());
			printer.setCheckout(true);
			printer.setCheckoutHandler(super.getUser().getId());
			this.addActionMessage("{*[core.dynaform.form.success.checkout]*}");
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
	public String doGetFields() {
		PrinterProcess printerProcess = (PrinterProcess) process;
		this.fieldList = printerProcess.getFields(get_formid());
		return SUCCESS;
	}

	public String doGetSubViews() {
		cn.myapps.core.macro.runner.IRunner runner = null;
		PrinterProcess printerProcess = (PrinterProcess) process;
		try {
			FormProcess fp = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
			Form form = (Form) fp.doView(get_formid());
			if (form != null) {
				runner = JavaScriptFactory.getInstance(ServletActionContext.getRequest().getSession().getId(), form
						.getApplicationid());
			}
		} catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		this.subViewList = printerProcess.getSubViews(get_formid(), runner);
		return SUCCESS;
	}

	public String doPrint() {
		ParamsTable params = getParams();
		try {
			this.reportData = ((PrinterProcess) process).getReportData(this.getContent().getId(), this.get_formid(),
					this.get_docid(), this.get_flowid(), this.getFrontUser(), params);
		} catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String doList() {
		ParamsTable params = getParams();
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
	
}
