package cn.myapps.core.workflow.storage.definition.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;


import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.BaseAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.constans.Web;
import cn.myapps.core.deploy.module.ejb.ModuleProcess;
import cn.myapps.core.deploy.module.ejb.ModuleVO;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.superuser.ejb.SuperUserProcess;
import cn.myapps.core.superuser.ejb.SuperUserVO;
import cn.myapps.core.sysconfig.ejb.CheckoutConfig;
import cn.myapps.core.workflow.element.FlowDiagram;
import cn.myapps.core.workflow.engine.StateMachine;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiProcess;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRT;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRTProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRTProcessBean;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.property.PropertyUtil;

public class BillDefiAction extends BaseAction<BillDefiVO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6022362114846090570L;
	private String _moduleid;
	
	private String checkoutConfig;

	public String get_moduleid() {
		return _moduleid;
	}

	public void set_moduleid(String _moduleid) {
		this._moduleid = _moduleid;
	}

	public String getCheckoutConfig() {
		PropertyUtil.reload("checkout");
		String _checkoutConfig = PropertyUtil.get(CheckoutConfig.INVOCATION);
		return _checkoutConfig;
	}

	public void setCheckoutConfig(String checkoutConfig) {
		this.checkoutConfig = checkoutConfig;
	}
	/**
	 * @SuppressWarnings 工厂方法不支持泛型
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public BillDefiAction() throws ClassNotFoundException {
		super(ProcessFactory.createProcess(BillDefiProcess.class),
				new BillDefiVO());
	}
	
	public String doNew() {
		
		BillDefiVO bl = new BillDefiVO();
		try {
			bl.setAuthorname(getUser().getName());
			bl.setAuthorno(getUser().getId());
			setContent(bl);
		} catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	/**
	 * 复制流程
	 * @return
	 */
	public String doCopy(){
		try {
			BillDefiProcess billDefiProcess = (BillDefiProcess) ProcessFactory.createProcess(BillDefiProcess.class);
			billDefiProcess.doCopyFlow(_selects);
		}catch (OBPMValidateException e) {
			addFieldError("1", "{*[cn.myapps.core.dynaform.view.copy.error]*}:"+e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[cn.myapps.core.dynaform.view.copy.error]*}:"+"{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
		
		this.addActionMessage("{*[cn.myapps.core.dynaform.view.copy.success]*}");
		return SUCCESS;
	}
	
	public String doSave() {
		try {
			BillDefiVO bl = (BillDefiVO) getContent();
			
			if(StringUtil.isBlank(bl.getId())){
				bl.setCheckout(true);
				bl.setCheckoutHandler(getUser().getId());
			}
			ModuleProcess mp = (ModuleProcess) ProcessFactory
					.createProcess(ModuleProcess.class);
			ModuleVO mv = (ModuleVO) mp.doView(_moduleid);
			doSaveValidate(bl);//校验
			Date lastmodify = new Date();
			bl.setLastmodify(lastmodify);
			bl.setModule(mv);

			return super.doSave();
		}  catch (OBPMValidateException e) {
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
			
			BillDefiVO bl = (BillDefiVO) getContent();
			process.doCheckout(bl.getId(), getUser());
			bl.setCheckout(true);
			bl.setCheckoutHandler(getUser().getId());
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
		try {
			BillDefiVO bl = (BillDefiVO) getContent();
			process.doCheckin(bl.getId(), getUser());
			bl.setCheckout(false);
			bl.setCheckoutHandler("");
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
	
	public String doEdit() {
		try {
			PropertyUtil.reload("checkout");
			String _checkoutConfig = PropertyUtil.get(CheckoutConfig.INVOCATION);
			Map<?, ?> params = getContext().getParameters();

			String id = ((String[]) params.get("id"))[0];
			BillDefiVO bi = (BillDefiVO) process.doView(id);
			if(StringUtil.isBlank(bi.getAuthorname())){//兼容旧数据
				bi.setAuthorname(getUser().getName());
				bi.setAuthorno(getUser().getId());
			}
			if(_checkoutConfig.equals("true") && bi.isCheckout() && !bi.getCheckoutHandler().equals(getUser().getId())){
				SuperUserProcess sp = (SuperUserProcess) ProcessFactory.createProcess(SuperUserProcess.class);
				SuperUserVO speruser = (SuperUserVO) sp.doView(bi.getCheckoutHandler());
				addFieldError("", "此流程已经被"+speruser.getName()+"签出，您目前没有修改的权限！");
			}
			setContent(bi);
		}  catch (OBPMValidateException e) {
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
	 * 前台手动调节流程
	 */
	public String doSaveFront(){
		try{
			
			BillDefiVO bl = (BillDefiVO) getContent();
			Date lastmodify = new Date();
			String applicationid = this.getParams().getParameterAsString("application");
			String _docid = this.getParams().getParameterAsString("_docid");
			String _stateId = this.getParams().getParameterAsString("_stateId");
			
			DocumentProcess docProcess = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,applicationid);
			Document document = (Document)docProcess.doView(_docid);
			
			FlowStateRTProcess flowStateRTProcess = new FlowStateRTProcessBean(applicationid);
			FlowStateRT flowStateRT = (FlowStateRT)flowStateRTProcess.doView(_stateId);
			flowStateRT.setFlowName(bl.getSubject());
			flowStateRT.setFlowXML(bl.getFlow());
			flowStateRT.setLastModified(lastmodify);
			flowStateRT.setLastModifierId(bl.getAuthorname());
			if(flowStateRT.getAuditdate()==null)
			flowStateRT.setAuditdate(lastmodify);
			flowStateRTProcess.doUpdate(flowStateRT);
			flowStateRT.setDocument(document);
			StateMachine.updateImage(flowStateRT);
			this.addActionMessage("{*[Save_Success]*}");
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
	 * 前台手动调节流程
	 */
	public String doViewFront(){
		try{
			String applicationid = this.getParams().getParameterAsString("application");
			String _docid = this.getParams().getParameterAsString("_docid");
			String _stateId = this.getParams().getParameterAsString("_stateId");
			DocumentProcess docProcess = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,applicationid);
			Document document = (Document)docProcess.doView(_docid);
			FlowStateRT instance = null;
			if(!StringUtil.isBlank(_stateId)){
				FlowStateRTProcess stateProcess = (FlowStateRTProcess)ProcessFactory.createRuntimeProcess(FlowStateRTProcess.class,applicationid);
				instance = ((FlowStateRT) stateProcess.doView(_stateId));
				this.set_moduleid(instance.getFlowVO().getModule().getId());
			}
			//FlowStateRT instance = document.getState();
			instance.setDocument(document);
			
			setContent(changeFlowImage(instance));
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
	
	//前台手动调整流程的节点已通过的需要改变xml的相应属性
	public BillDefiVO changeFlowImage(FlowStateRT instance) throws Exception{
		FlowDiagram fd = StateMachine.changeFlowState(instance);
		BillDefiVO flowVO = instance.getFlowVO();
		flowVO.setFlow(fd.toXML());
		instance.getDocument().setFlowVO(flowVO);
		//更新流程图
//		StateMachine.updateImage(instance);
		return flowVO;
	}
	
	public void doSaveValidate(BillDefiVO bl) throws Exception{
		// 非法字符串检验
		String invalidChars = getInvalidChars(bl.getSubject());
		if (!StringUtil.isBlank(invalidChars)) {
				throw new OBPMValidateException("{*[workflow.subject.exist.invalidchar]*}: "
						+ invalidChars);
		}
		validate();
	}
	/**
	 * 校验是否有重复的名称
	 */
	public void validate() {
		try {
			if (((BillDefiProcess) process)
					.isSubjectExisted((BillDefiVO) getContent())) {
				BillDefiVO bl = (BillDefiVO)getContent();
				bl.setCheckoutHandler(null);
				addFieldError("1", "{*[workflow.subject.exist]*}");
			}
		}  catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
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
	
	public void getOpinion(){
		ParamsTable params = getParams();
		String flowid = params.getParameterAsString("flowid");
		PrintWriter pw = null;
		String msg = "null";
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setHeader("ContentType", "text/json");
			response.setCharacterEncoding("utf-8");
			pw = response.getWriter();
			BillDefiVO bi = (BillDefiVO) process.doView(flowid);

			if(bi != null && bi.getOpinion()!=null)
				msg=bi.getOpinion();
		} catch (IOException e) {
			msg = "null";
			e.printStackTrace();
		} catch (Exception e1) {
			msg = "null";
			e1.printStackTrace();
		}finally{
			pw.write(msg.toString()); // 把内容放入response中
			pw.flush();
			pw.close();
		}
	}
	
	public void addOpinion(){
		ParamsTable params = getParams();
		String flowid = params.getParameterAsString("flowid");
		String opinion = params.getParameterAsString("value");
		PrintWriter pw = null;
		String msg = "null";
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setHeader("ContentType", "text/json");
			response.setCharacterEncoding("utf-8");
			pw = response.getWriter();
			((BillDefiProcess)process).updateOpinion(flowid,opinion);
			msg="success";
		}catch (IOException e) {
			msg = "null";
			e.printStackTrace();
		} catch (Exception e1) {
			msg = "null";
			e1.printStackTrace();
		}finally{
			pw.write(msg.toString()); // 把内容放入response中
			pw.flush();
			pw.close();
		}
	}
	
	public void deleteOpinion(){
		ParamsTable params = getParams();
		String flowid = params.getParameterAsString("flowid");
		String opinionid = params.getParameterAsString("opinionid");
		PrintWriter pw = null;
		String msg = "null";
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setHeader("ContentType", "text/json");
			response.setCharacterEncoding("utf-8");
			pw = response.getWriter();
			((BillDefiProcess)process).removeOpinion(flowid,opinionid);
			msg="success";
		}catch (IOException e) {
			msg = "null";
			e.printStackTrace();
		} catch (Exception e1) {
			msg = "null";
			e1.printStackTrace();
		}finally{
			pw.write(msg.toString()); // 把内容放入response中
			pw.flush();
			pw.close();
		}
	}
}
