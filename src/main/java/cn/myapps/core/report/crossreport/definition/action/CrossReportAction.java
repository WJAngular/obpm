package cn.myapps.core.report.crossreport.definition.action;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.apache.struts2.ServletActionContext;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.BaseAction;
import cn.myapps.constans.Web;
import cn.myapps.core.report.crossreport.definition.ejb.CrossReportProcess;
import cn.myapps.core.report.crossreport.definition.ejb.CrossReportVO;
import cn.myapps.core.superuser.ejb.SuperUserProcess;
import cn.myapps.core.superuser.ejb.SuperUserVO;
import cn.myapps.core.sysconfig.ejb.CheckoutConfig;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.property.PropertyUtil;

public class CrossReportAction extends BaseAction<CrossReportVO>{
	
	String domain;
	
	private String checkoutConfig;

	public String getCheckoutConfig() {
		PropertyUtil.reload("checkout");
		String _checkoutConfig = PropertyUtil.get(CheckoutConfig.INVOCATION);
		return _checkoutConfig;
	}

	public void setCheckoutConfig(String checkoutConfig) {
		this.checkoutConfig = checkoutConfig;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	/**
	 * @SuppressWarnings 工厂方法不支持泛型
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public CrossReportAction() throws ClassNotFoundException {
		super(ProcessFactory.createProcess(CrossReportProcess.class), new CrossReportVO());
	}
	
	public String doList(){
		try {
			this.validateQueryParams();
			params = getParams();
			int lines = 10;
			Cookie[] cookies = ServletActionContext.getRequest().getCookies();
			for(Cookie cookie : cookies){
				if(Web.FILELIST_PAGELINE.equals(cookie.getName())){
					lines = Integer.parseInt(cookie.getValue());
				}
			    cookie.getName();
			    cookie.getValue();
			}
			int page = params.getParameterAsInteger("_currpage")!=null? params.getParameterAsInteger("_currpage") :1;
			
			datas = ((CrossReportProcess)process).doQueryCrossReportByModule(params.getParameterAsString("module"), params, page, lines);
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
	
	public String doNew()
	{
		CrossReportVO vo = (CrossReportVO) getContent();
		if(vo.getCalculationMethod() == null || vo.getCalculationMethod() == ""){
			vo.setCalculationMethod("SUM");
		}
		return SUCCESS;
	}
	public String doSave() 
	{
		try{
			CrossReportVO vo = (CrossReportVO)this.getContent();
			if(StringUtil.isBlank(vo.getId())){
				vo.setCheckout(true);
				vo.setCheckoutHandler(getUser().getId());
			}
			
			if(!vo.isDisplayRow()){
				vo.setRowCalMethod(null);
			}
			if(!vo.isDisplayCol()){
				vo.setColCalMethod(null);
			}
			if(vo.getDatas()==null){
				vo.setCalculationMethod(null);
			}else if(vo.getDatas().equals("[]")){
				vo.setCalculationMethod(null);
			}
//			vo.setFilters("[]");
			vo.setType("CrossReport");
			return super.doSave();
		 } catch (OBPMValidateException e) {
				addFieldError("1", e.getValidateMessage());
				return INPUT;
			}catch (Exception e) {
				this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
				e.printStackTrace();
				return INPUT;
			}
	}
	
	public String doEdit() {
		try {
			PropertyUtil.reload("checkout");
			String _checkoutConfig = PropertyUtil.get(CheckoutConfig.INVOCATION);
			Map<?, ?> params = getContext().getParameters();

			String id = ((String[]) params.get("id"))[0];
			CrossReportVO vo = (CrossReportVO) process.doView(id);
			if(_checkoutConfig.equals("true") && vo.isCheckout() && !vo.getCheckoutHandler().equals(getUser().getId())){
				SuperUserProcess sp = (SuperUserProcess) ProcessFactory.createProcess(SuperUserProcess.class);
				SuperUserVO speruser = (SuperUserVO) sp.doView(vo.getCheckoutHandler());
				addFieldError("", "此报表已经被"+speruser.getName()+"签出，您目前没有修改的权限！");
			}
			setContent(vo);
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
	 * 签出
	 * @return
	 * @throws Exception
	 */
	public String doCheckout() throws Exception {
		try{
			CrossReportVO vo = (CrossReportVO)this.getContent();
			process.doCheckout(vo.getId(), getUser());
			vo.setCheckout(true);
			vo.setCheckoutHandler(getUser().getId());
			setContent(vo);
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
	
	/**
	 * 签入
	 * @return
	 * @throws Exception
	 */
	public String doCheckin() throws Exception {
		try{
			CrossReportVO vo = (CrossReportVO)this.getContent();
			process.doCheckin(vo.getId(), getUser());
			vo.setCheckout(false);
			vo.setCheckoutHandler("");
			setContent(vo);
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
	
	private static final long serialVersionUID = 1L;
	
	public String getDomain() {
		if (domain != null && domain.trim().length() > 0) {
			return domain;
		} else {
			return (String) getContext().getSession().get(Web.SESSION_ATTRIBUTE_DOMAIN);
		}
	}


	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * 用于叛断是否是选择的列不存在
	 * @param str
	 * @param metadatas
	 * @return
	 
	private boolean isExistColumn(String str,Map<String, String> metadatas)
	{
		Collection<Object> cols = JsonUtil.toCollection(str);
		for (Iterator<Object> iterator = cols.iterator(); iterator.hasNext();) {
			String col = (String) iterator.next();
			if(!metadatas.containsKey(col))
			 return false;
		}
		return true;
	}*/
}
