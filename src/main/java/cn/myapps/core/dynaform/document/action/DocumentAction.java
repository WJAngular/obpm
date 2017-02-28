package cn.myapps.core.dynaform.document.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.jfree.util.Log;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.Preparable;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.AbstractRunTimeAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.IRunTimeProcess;
import cn.myapps.constans.Environment;
import cn.myapps.constans.Web;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormField;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.permission.ejb.PermissionProcess;
import cn.myapps.core.privilege.operation.ejb.OperationVO;
import cn.myapps.core.privilege.res.ejb.ResVO;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.core.scheduler.ejb.Job;
import cn.myapps.core.scheduler.ejb.ManualNodeTimingApprovalJob;
import cn.myapps.core.scheduler.ejb.SchedulerFactory;
import cn.myapps.core.scheduler.ejb.TriggerVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.PreviewUser;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workflow.FlowType;
import cn.myapps.core.workflow.element.FlowDiagram;
import cn.myapps.core.workflow.element.ManualNode;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.core.workflow.engine.StateMachineHelper;
import cn.myapps.core.workflow.notification.ejb.NotificationConstant;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorHIS;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorRT;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRT;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRT;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRTProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHIS;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHISProcess;
import cn.myapps.util.CreateProcessException;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.cache.MemoryCacheUtil;
import cn.myapps.util.property.DefaultProperty;
import cn.myapps.util.sequence.Sequence;

/**
 * @author Marky
 */
public class DocumentAction extends AbstractRunTimeAction<Document> implements Preparable {
	private static final Logger log = Logger.getLogger(DocumentAction.class);

	private static final long serialVersionUID = 1L;

	private Collection<FormField> _fieldSelect;

	private String _flowid;

	private String _formid;
	
	private String _viewid;

	private String parentid;

	private ValueObject content;
	
	private String _isPreview;
	
	private String _targetNode;

	public String get_viewid() {
		return _viewid;
	}

	public void set_viewid(String viewid) {
		_viewid = viewid;
	}
	
	/**
	 * 返回父文档主键.
	 * 
	 * @return 父文档主键
	 * @uml.property name="parentid"
	 */
	public String getParentid() {
		return parentid;
	}

	/**
	 * 设置父文档主键.
	 * 
	 * @param parentid
	 *            父文档主键
	 * @throws Exception
	 * @uml.property name="parentid"
	 */
	public void setParentid(String parentid) throws Exception {
		this.parentid = parentid;
	}

	/**
	 * 返回文档主键.
	 * 
	 * @return 文档主键
	 * @throws Exception
	 */
	public String get_docid() throws Exception {
		ValueObject doc = getContent();
		if (doc != null)
			return doc.getId();
		else
			return null;
	}

	/**
	 * 设置文档主键
	 * 
	 * @param _docid
	 *            文档主键
	 * @throws Exception
	 */
	public void set_docid(String _docid) throws Exception {
		getContent().setId(_docid);
	}

	public String get_isPreview() {
		return _isPreview;
	}

	public void set_isPreview(String isPreview) {
		_isPreview = isPreview;
	}

	public String get_targetNode() {
		return _targetNode;
	}
	
	public void set_targetNode(String targetNode){
		this._targetNode = targetNode;
	}

	/**
	 * 返回文档值对象
	 * 
	 * @return 文档值对象
	 * @uml.property name="content"
	 */
	public ValueObject getContent() {
		if (content == null) {
			content = new Document();
		}
		return content;
	}

	/**
	 * DocumentAction构造函数
	 * 
	 * @throws ClassNotFoundException
	 */
	public DocumentAction() throws ClassNotFoundException {
	}

	/**
	 * 根据文档主键，查找文档.
	 * 
	 * @SuppressWarnings webwork 不支持泛型
	 * @return SUCCESS
	 * @throws Exception
	 */
	public String doView() throws Exception {
		
		try {
			if(!validateWithPermission()){
				return "forbid";
			}
			Map<String, String> request = (Map<String, String>) ActionContext.getContext().get("request");
			ParamsTable params = getParams();
			WebUser user = this.getUser();
			Document doc = ((DocumentProcess)getProcess()).doEdit(params, user, getApplication());
			setContent(doc);
			request.put("signatureExist", params.getParameterAsString("signatureExist"));
			NodeRT nodert = (NodeRT) ServletActionContext.getRequest().getAttribute("_targetNodeRT");
			if(nodert!=null){
				this._targetNode = nodert.getNodeid();
			}
			MemoryCacheUtil.putToPrivateSpace(doc.getId(), doc, user);
		} catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
			return INPUT;
		}catch(OBPMRuntimeException ee){
			this.setRuntimeException(ee);
			ee.printStackTrace();
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
		return SUCCESS;
	}

	public boolean validateWithPermission() throws Exception{
		if(StringUtil.isBlank(this._formid) || "null".equals(this._formid)){
			throw new OBPMRuntimeException("文档的表单ID丢失,如采用sql过滤模式,麻烦检查sql语句,添加formid字段!");
		}
		WebUser user = getUser();
		Collection<RoleVO> roles = user.getRoles();
		
		boolean allow = true;
		FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		PermissionProcess process = (PermissionProcess) ProcessFactory.createProcess(PermissionProcess.class);
		
		Form form = (Form)formProcess.doView(_formid);
		
		if(!Form.PERMISSION_TYPE_PUBLIC.equals(form.getPermissionType())){
			allow = process.check(roles, this._formid, this._formid, OperationVO.FORM_VIEW_ALLOW_OPEN, ResVO.FORM_TYPE, application);
		}
		
		if(!allow){
			return false;
		}
		
		return true;
	}

	/**
	 * 新建一个Document.
	 * 
	 * @return result. 处理成功返回"SUCCESS"
	 * @throws Exception
	 */
	public String doNew() {
		try {
			WebUser user = getUser();
			Collection<RoleVO> roles = user.getRoles();
			
			boolean allow = true;
			FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
			PermissionProcess process = (PermissionProcess) ProcessFactory.createProcess(PermissionProcess.class);
			
			Form form = (Form)formProcess.doView(_formid);
			
			if(!Form.PERMISSION_TYPE_PUBLIC.equals(form.getPermissionType())){
				allow = process.check(roles, this._formid, this._formid, OperationVO.FORM_VIEW_ALLOW_OPEN, ResVO.FORM_TYPE, application);
			}
			
			if(!allow){
				return "forbid";
			}
			
			DocumentProcess proxy = (DocumentProcess) getProcess();
			if (form != null) {
				Document newDoc = proxy.doNewWithFlowPermission(form, user, getParams());
				setContent(newDoc);
				// 放入上下文中
				ServletActionContext.getRequest().setAttribute("content.id", newDoc.getId());
				MemoryCacheUtil.putToPrivateSpace(newDoc.getId(), newDoc, user);
			}
			return SUCCESS;
		} catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
			return INPUT;
		}
		catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
			return INPUT;
		}
	}

	public String doPreview() {
		try {
			FormProcess formPross = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
			PreviewUser user = new PreviewUser(new UserVO());
			String skinType = getParams().getParameterAsString("_skinType");
			if(!StringUtil.isBlank(skinType)){
				ServletActionContext.getRequest().setAttribute("_skinType", skinType);
				user.setSkinType(skinType);
			}
			ServletActionContext.getRequest().getSession().setAttribute(Web.SESSION_ATTRIBUTE_PREVIEW_USER, user);
			set_isPreview("true");
			Form form = (Form) formPross.doView(get_formid());
			if (form != null) {
				Document newDoc = form.createDocument(getParams(), user);
				setContent(newDoc);
			}
		} catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}

		return SUCCESS;
	}

	/*
	 * 启动流程前参数准备
	 */
	public void doBeforeStartWorkFlow() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			ParamsTable params = ParamsTable.convertHTTP(request);
			ServletActionContext.getRequest().getSession().setAttribute("params", params);
		} catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
	}

	/**
	 * 校验当前用户是否可以保存文档.
	 * 根据当前Document是否有子Document并且是否可以编辑,若有子Document并且可以编辑,返回true,
	 * 此时可以保存当前Document. 并根据Document id 、 flow(流程)id 与当前用户作为参数条件来判断.
	 */
	public void validate() {
		Document doc = (Document) getContent();

		try {
			final ParamsTable params = getParams();
			doc.setDomainid(getDomain());
			doc = DocumentHelper.rebuildDocument(doc, params,getUser());
			final WebUser webUser = getUser();
			String _flowType = params.getParameterAsString("_flowType");
			boolean isWithoutValidate = params.getParameterAsBoolean("isWithoutValidate");
			if(isWithoutValidate) return;
			if ("retracement".equals(_flowType)) {
				_flowType = "85";
			}
			DocumentProcess proxy = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,getApplication());
			if ((_flowType != null && _flowType.equals(FlowType.RUNNING2RUNNING_RETRACEMENT)) ||  proxy.isDocSaveUser(doc, params, webUser)) {
				Collection<ValidateMessage> errors = proxy.doValidate(doc, params, webUser);
				if (errors != null && errors.size() > 0) {
					for (Iterator<ValidateMessage> iter = errors.iterator(); iter.hasNext();) {
						ValidateMessage err = (ValidateMessage) iter.next();
						this.addFieldError(err.getFieldname(), err.getErrmessage());
					}
				}
			} else {
				addFieldError("isDocSaveUser", "{*[core.document.cannotsave]*}");
			}
		} catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		setContent(doc);
	}

	/**
	 * 返回相关表单主键
	 * 
	 * @return 表单主键
	 * @uml.property name="formid"
	 */
	public String get_formid() {
		return _formid;
	}

	/**
	 * 设置相关表单主键
	 * 
	 * @param formid
	 * @uml.property name="formid"
	 */
	public void set_formid(String _formid) {
		this._formid = _formid;
	}

	/**
	 * 返回相应流程主键.
	 * 
	 * @return 流程主键
	 */
	public String get_flowid() {
		return _flowid;
	}

	/**
	 * 设置相应流程
	 * 
	 * @param _flowid
	 *            流程主键
	 */
	public void set_flowid(String _flowid) {
		String[] flowids = null;
		if (_flowid.indexOf(",") != -1) { // 有多个parmeter时只获取一个
			flowids = _flowid.trim().split(",");
			_flowid = flowids[0];
		}
		this._flowid = _flowid;
	}


	/**
	 * 返回查询的字段
	 * 
	 * @return 查询的字段
	 * @uml.property name="_fieldSelect"
	 */
	public Collection<FormField> get_fieldSelect() {
		return _fieldSelect;
	}

	/**
	 * 设置查询的字段
	 * 
	 * @param select
	 * @uml.property name="_fieldSelect"
	 */
	public void set_fieldSelect(Collection<FormField> select) {
		_fieldSelect = select;
	}

	/**
	 * 设置Document对象
	 * 
	 * @param content
	 *            Document对象
	 * @uml.property name="content"
	 */
	public void setContent(ValueObject content) {
		this.content = content;
	}

	/**
	 * 返回存放Document id 数组
	 * 
	 * @return 存放Document id 数组
	 * @uml.property name="_selects"
	 */
	public String[] get_selects() {
		return _selects;
	}

	/**
	 * 设置存放Document id 数组
	 * 
	 * @param selects
	 * @uml.property name="_selects"
	 */
	public void set_selects(String[] selects) {
		this._selects = selects;
	}

	/**
	 * 删除地图Document
	 * 
	 * @return SUCCESS OR ERROR
	 * @throws Exception
	 */
	public String doDeleteMap() {
		try {
			if (_selects != null) {
				DocumentProcess proxy = (DocumentProcess) getProcess();
				proxy.doRemove(_selects);
			}
		} catch (OBPMValidateException e) {
			log.error("doDelete", e);
			addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			log.error("doDelete", e);
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}

		return SUCCESS;
	}
	
	public String doDelete() {
		try {
			if (_selects != null) {
				DocumentProcess proxy = (DocumentProcess) getProcess();
				proxy.doRemove(_selects);
			}
		} catch (Exception e) {
			log.error("doDelete", e);
			addFieldError("", e.getMessage());
		}

		return SUCCESS;
	}

	private DocumentProcess createDocumentProcess(String applicationid) {
		try {
			DocumentProcess process = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,
					applicationid);
			return process;
		} catch (CreateProcessException e) {
			Log.error(e);
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 执行保存或提交流程等相关操作,必须加入此拦截以确保数据完整
	 */
	public void prepare() throws Exception {
		WebUser user = getUser();
		FormProcess formPross = (FormProcess) ProcessFactory.createProcess(FormProcess.class);

		DocumentProcess proxy = (DocumentProcess) getProcess();
		// 先与po合并
		Document mo = proxy.mergePO((Document) getContent(), user);
		if (_formid != null) {
			// 重新计算
			Document content = ((Form) formPross.doView(_formid)).recalculateDocument(mo, getParams(), user);
			content.setAuditorList(getParams().getParameterAsString("content.auditorList"));
			setContent(content);
		}
	}

	public IRunTimeProcess<Document> getProcess() {
		return createDocumentProcess(getApplication());
	}

	
	public String doNewWord() {
		try {
			ParamsTable params = getParams();
			String formname = params.getParameterAsString("formname");
			if(formname != null && !formname.equals("")){
				ServletActionContext.getRequest().setAttribute("formname", formname);
			}
			ServletActionContext.getRequest().setAttribute("isFile", DocumentHelper.isWordFileExist(params));
			ServletActionContext.getRequest().setAttribute("RHList", DocumentHelper.listWordFiles("REDHEAD_DOCPATH",getUser().getDomainid()));
			ServletActionContext.getRequest().setAttribute("SECList", DocumentHelper.listWordFiles("SECSIGN_PATH",getUser().getDomainid()));
			ServletActionContext.getRequest().setAttribute("TList", DocumentHelper.listWordFiles("TEMPLATE_DOCPATH",getUser().getDomainid()));
		} catch (OBPMValidateException e) {
			this.addFieldError("1", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String doSecEdit() {
		try {
			String dir = DefaultProperty.getProperty("SECSIGN_PATH");
			String realPath = Environment.getInstance().getRealPath(dir);
			File files = new File(realPath);
			if (!files.exists()) {
				if (!files.mkdir())
					throw new OBPMValidateException("Folder create failure");
			}
			List<File> secFiles = new ArrayList<File>();
			for(File file:files.listFiles()){
				if(!file.isDirectory())
					secFiles.add(file);
			}
			String domainDir = DefaultProperty.getProperty("SECSIGN_PATH") + getUser().getDomainid() + "/";
			String realDomainPath = Environment.getInstance().getRealPath(domainDir);
			File domainFiles = new File(realDomainPath);
			if (!domainFiles.exists()) {
				if (!domainFiles.mkdir())
					throw new OBPMValidateException("Folder create failure");
			}
			List<File> secDomainFiles = new ArrayList<File>();
			for(File file:domainFiles.listFiles()){
				if(!file.isDirectory())
					secDomainFiles.add(file);
			}
			ServletActionContext.getRequest().setAttribute("secFiles", secFiles);
			ServletActionContext.getRequest().setAttribute("secPath", dir);
			ServletActionContext.getRequest().setAttribute("secDomainFiles", secDomainFiles);
			ServletActionContext.getRequest().setAttribute("secDomainPath", domainDir);
		} catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	/**
	 * 开始执行动作,用于word组件
	 * 
	 * @return
	 */
	public String doStart() {
		try {
			ParamsTable params = getParams();
			String formname = params.getParameterAsString("formname");
			if(formname != null && !formname.equals("")){
				ServletActionContext.getRequest().setAttribute("formname", formname);
			}
			ServletActionContext.getRequest().setAttribute("isFile", DocumentHelper.isWordFileExist(params));
			ServletActionContext.getRequest().setAttribute("RHList", DocumentHelper.listWordFiles("REDHEAD_DOCPATH",getUser().getDomainid()));
			ServletActionContext.getRequest().setAttribute("SECList", DocumentHelper.listWordFiles("SECSIGN_PATH",getUser().getDomainid()));
			ServletActionContext.getRequest().setAttribute("TList", DocumentHelper.listWordFiles("TEMPLATE_DOCPATH",getUser().getDomainid()));
		} catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String doViewWordFile() {
		try {
		} catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String doSelectByFlow() throws Exception {
		String flowid = getParams().getParameterAsString("flowid");
		String docid = getParams().getParameterAsString("docid");
		String nodeid = getParams().getParameterAsString("nodeid");
		String id = getParams().getParameterAsString("id");
		int type = Integer.parseInt(getParams().getParameterAsString("type"));
		WebUser webUser = (WebUser) getParams().getHttpRequest().getSession().getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
		String html = StateMachineHelper.getPrincipalString(docid, webUser, nodeid, getParams().getHttpRequest(), flowid, type, id);
//		HttpServletResponse response = ServletActionContext.getResponse();
//		response.setContentType("text/html;charset=UTF-8");
//		response.getWriter().write(html);
//		response.flushBuffer();
		
		ServletActionContext.getRequest().setAttribute("HTML", html.toString());
		return SUCCESS;
	}
	
	public String doBattchApprove() {
		String limistStrList = (String) ServletActionContext.getRequest().getAttribute("_approveLimit");
		if(StringUtil.isBlank(limistStrList)){
			limistStrList = getParams().getParameterAsString("_approveLimit");
		}
		Collection<String> limistList = new ArrayList<String>();

		if (!StringUtil.isBlank(limistStrList)) {
			limistList = Arrays.asList(limistStrList.split(","));
		}

		try {
			DocumentProcess proxy = (DocumentProcess) getProcess();

			proxy.doBatchApprove(get_selects(), getUser(), getEnvironment(), getParams(), limistList);
		}catch (OBPMValidateException e) {
			addFieldError("1", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}

		return SUCCESS;
	}
	
	public String doFlowHandup() {
		try {
			ParamsTable params = getParams();
			WebUser user = getUser();
			String applicationid = params.getParameterAsString("application");
			DocumentProcess docProcess = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, applicationid);
			Document doc = (Document) docProcess.doView(params.getParameterAsString("_docid"));
			Date actionTime = new Date();
			String nodertId = params.getParameterAsString("nodertId");
			NodeRTProcess nodeRTProcess = (NodeRTProcess) ProcessFactory.createRuntimeProcess(NodeRTProcess.class, applicationid);
			RelationHISProcess relationHISProcess = (RelationHISProcess) ProcessFactory.createRuntimeProcess(RelationHISProcess.class, applicationid);
			NodeRT nodeRT = (NodeRT) nodeRTProcess.doView(nodertId);
			if(nodeRT != null){
				nodeRT.setState(1);
				nodeRT.setLastProcessTime(new Date());
				nodeRT.setActorrts(nodeRT.getActorrts());
				nodeRTProcess.doUpdate(nodeRT);
				
				//加入流程历史
				BillDefiVO flowVO = doc.getState().getFlowVO();
				// 获取当前结点
				Node currnode = null;
				FlowDiagram fd = null;
				if (flowVO != null) {
					fd = flowVO.toFlowDiagram();
				}
				String currnodeid = nodeRT.getNodeid();
				if (currnodeid != null) {
					currnode = (Node) fd.getElementByID(currnodeid);
				}
				FlowStateRT instance = doc.getState(params.getParameterAsString("_currid"));
				RelationHIS rhis = new RelationHIS();
				rhis.setId(Sequence.getSequence());
				rhis.setFlowStateId(instance.getId());
				rhis.setFlowid(instance.getFlowid());
				rhis.setFlowname(instance.getFlowVO().getSubject());
				rhis.setDocid(instance.getDocid());
				rhis.setStartnodeid(currnode.id);
				rhis.setStartnodename(currnode.name);
				rhis.setEndnodeid(currnode.id);
				rhis.setEndnodename(currnode.name);
				rhis.setIspassed(false);
				rhis.setActiontime(actionTime);
				rhis.setAttitude(params.getParameterAsString("_attitude"));
				rhis.setAuditor(user.getId());
				ActorHIS actorHIS =null;
				if(user.getEmployer()!=null){
					actorHIS = new ActorHIS((new WebUser(user.getEmployer())));
					actorHIS.setAgentid(user.getId());
					actorHIS.setAgentname(user.getName());
				}else{
					actorHIS = new ActorHIS(user);
				}
				actorHIS.setAttitude(params.getParameterAsString("_attitude"));
				actorHIS.setSignature(params.getParameterAsString("_signature"));
				actorHIS.setProcesstime(actionTime);
				rhis.getActorhiss().add(actorHIS);
				rhis.setFlowOperation(FlowType.RUNNING2RUNNING_HANDUP);
				rhis.setReminderCount(0);
				relationHISProcess.doCreate(rhis);
				
				//如果是有审批时限设置,取消之前任务
				if(((ManualNode)currnode).isLimited && nodeRT.getDeadline() !=null){
					/*String key = instance.getDocid() + "_" + instance.getId()+ "_" + currnode.id;
					Timer timer = AutoAuditJobManager.runningTimerMap.get(key);
					timer.cancel();
					AutoAuditJobManager.runningTimerMap.remove(key);*/
					String token = TriggerVO.generateManualNodeTimingApprovalJobToken(doc.getId(), doc.getStateid(), nodeRT.getId());
					SchedulerFactory.getScheduler().cancelTrigger(token);
				}
			}
		} catch (OBPMValidateException e) {
			this.addFieldError("1", "{*[cn.myapps.core.workflow.suspend.fail]*}"+e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[cn.myapps.core.workflow.suspend.fail]*}",e));
			e.printStackTrace();
			return INPUT;
		}
		this.addActionMessage("{*[cn.myapps.core.workflow.suspend.success]*}");
		return SUCCESS;
	}
	
	public String doFlowRecover(){
		try {
			ParamsTable params = getParams();
			WebUser user = getUser();
			String applicationid = params.getParameterAsString("application");
			DocumentProcess docProcess = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, applicationid);
			Document doc = (Document) docProcess.doView(params.getParameterAsString("_docid"));
			Date actionTime = new Date();
			String nodertId = params.getParameterAsString("nodertId");
			NodeRTProcess nodeRTProcess = (NodeRTProcess) ProcessFactory.createRuntimeProcess(NodeRTProcess.class, applicationid);
			RelationHISProcess relationHISProcess = (RelationHISProcess) ProcessFactory.createRuntimeProcess(RelationHISProcess.class, applicationid);
			NodeRT nodeRT = (NodeRT) nodeRTProcess.doView(nodertId);
			if(nodeRT != null){
				BillDefiVO flowVO = doc.getState().getFlowVO();
				// 获取当前结点
				Node currnode = null;
				FlowDiagram fd = null;
				if (flowVO != null) {
					fd = flowVO.toFlowDiagram();
				}
				String currnodeid = nodeRT.getNodeid();
				if (currnodeid != null) {
					currnode = (Node) fd.getElementByID(currnodeid);
				}
				
				nodeRT.setState(0);
				Date lastProcessTime = new Date();
				long time = lastProcessTime.getTime() - nodeRT.getLastProcessTime().getTime();
				//更改审批时限
				if(((ManualNode)currnode).isLimited && nodeRT.getDeadline() !=null){
					if(nodeRT.getDeadline() != null){
						Date deadline = new Date(nodeRT.getDeadline().getTime() + time);
						nodeRT.setDeadline(deadline);
					}
				}
				//更改过期通知时间
				Collection<ActorRT> actorRTs = nodeRT.getActorrts();
				Map<String, Object> strategyMap = ((ManualNode)currnode).getNotificationStrategyMap();
				if(strategyMap.containsKey(NotificationConstant.STRTAGERY_OVERDUE)){
					for(Iterator<ActorRT> it = actorRTs.iterator(); it.hasNext();){
						ActorRT actorRT = it.next();
						if(actorRT.getDeadline() != null){
							Date deadline = new Date(actorRT.getDeadline().getTime() + time);
							actorRT.setDeadline(deadline);
						}
					}
				}
				nodeRT.setActorrts(actorRTs);
				nodeRT.setLastProcessTime(lastProcessTime);
				nodeRTProcess.doUpdate(nodeRT);
				
				//加入流程历史
				FlowStateRT instance = doc.getState(params.getParameterAsString("_currid"));
				RelationHIS rhis = new RelationHIS();
				rhis.setId(Sequence.getSequence());
				rhis.setFlowStateId(instance.getId());
				rhis.setFlowid(instance.getFlowid());
				rhis.setFlowname(instance.getFlowVO().getSubject());
				rhis.setDocid(instance.getDocid());
				rhis.setStartnodeid(currnode.id);
				rhis.setStartnodename(currnode.name);
				rhis.setEndnodeid(currnode.id);
				rhis.setEndnodename(currnode.name);
				rhis.setIspassed(false);
				rhis.setActiontime(actionTime);
				rhis.setAttitude(params.getParameterAsString("_attitude"));
				rhis.setAuditor(user.getId());
				ActorHIS actorHIS =null;
				if(user.getEmployer()!=null){
					actorHIS = new ActorHIS((new WebUser(user.getEmployer())));
					actorHIS.setAgentid(user.getId());
					actorHIS.setAgentname(user.getName());
				}else{
					actorHIS = new ActorHIS(user);
				}
				actorHIS.setAttitude(params.getParameterAsString("_attitude"));
				actorHIS.setSignature(params.getParameterAsString("_signature"));
				actorHIS.setProcesstime(actionTime);
				rhis.getActorhiss().add(actorHIS);
				rhis.setFlowOperation(FlowType.RUNNING2RUNNING_RECOVER);
				rhis.setReminderCount(0);
				relationHISProcess.doCreate(rhis);
				
				//调整deadline后重新加入任务
				if(((ManualNode)currnode).isLimited && nodeRT.getDeadline() !=null){
					/*
					SuperUserProcess superUserProcess = (SuperUserProcess) ProcessFactory.createProcess(SuperUserProcess.class);

					WebUser admin = new WebUser(superUserProcess.getDefaultAdmin()); // 系统用户
					admin.setDomainid(instance.getDocument().getId());
					admin.setName("system");
					AutoAuditJobManager.addJob(new TimelimitAutoAuditJob(instance.getDocument(),instance, nodeRT,currnode.id, admin));// 添加Job
					AutoAuditJobManager.startJob(instance, currnode.id);*/
					Job job = new ManualNodeTimingApprovalJob(instance.getDocid(),instance.getId(),nodeRT.getId(),instance.getApplicationid(),null);
					TriggerVO trigger = new TriggerVO(job, nodeRT.getDeadline().getTime());
					SchedulerFactory.getScheduler().addTrigger(trigger);
				}
			}
		} catch (OBPMValidateException e) {
			this.addFieldError("1", "{*[cn.myapps.core.workflow.recover.fail]*}"+e.getValidateMessage());
			return INPUT;
		}catch (Exception e) {
			e.printStackTrace();
			this.setRuntimeException(new OBPMRuntimeException("{*[cn.myapps.core.workflow.recover.fail]*}",e));
			return INPUT;
		}
		this.addActionMessage("{*[cn.myapps.core.workflow.recover.success]*}");
		return SUCCESS;
	}
	
	/**
	 * 流程催办
	 * @return
	 */
	public String doFlowReminder(){
		
		try {
			ParamsTable params = getParams();
			WebUser user = getUser();
			String applicationid = params.getParameterAsString("application");
			String stateid = params.getParameterAsString("content.stateid");
			String[] nodertIds = params.getParameterAsArray("_nodertIds");
			String reminderContent = params.getParameterAsString("_reminderContent");
			DocumentProcess docProcess = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, applicationid);
			Document doc = (Document) docProcess.doView(params.getParameterAsString("_docid"));
			doc.setState(stateid);
			
			docProcess.doFlowReminder(doc, nodertIds, reminderContent, params, user);
			
		} catch (Exception e) {
			e.printStackTrace();
			this.setRuntimeException(new OBPMRuntimeException("流程催办失败!",e));
			return INPUT;
		}
		this.addActionMessage("成功催办！");
		return SUCCESS;
	}
	
	public String doFileDownload(){
		OutputStream os = null;
		BufferedInputStream reader = null;
		try {
			ParamsTable params = this.getParams();
			String encoding = "utf-8";
			String filename = params.getParameterAsString("filename");
			filename = URLDecoder.decode(filename.replace("%20", "%2B"), encoding);
			String filepath = params.getParameterAsString("filepath");
			filepath = URLDecoder.decode(filepath, encoding);
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			String realPath = request.getSession().getServletContext().getRealPath("/");
			File file = new File(realPath + filepath);
			if(!file.exists()){
				return "找不到指定文件";
			}
			
			String agent = request.getHeader("USER-AGENT");
			
			response.setContentType("application/octet-stream;charset=ISO8859-1");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + java.net.URLEncoder.encode(filename, encoding) + "\"");
			if(null != agent && -1 != agent.indexOf("Firefox")){
				response.setHeader("Content-Disposition", "attachment;filename=\"" + MimeUtility.encodeText(filename, encoding, "B") + "\"");
			}
			/*
			else{
				if(address.indexOf("pdf") == -1){
					response.setContentType("application/octet-stream");
//					java.net.URLEncoder.encode(filename, encoding)	2015-1-20
					response.setHeader("Content-Disposition", "attachment;filename=\"" + java.net.URLEncoder.encode(filename, encoding).replace("%2B", " ") + "\"");
				}
			}
			*/
			os = response.getOutputStream();

			reader = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[4096];
			int i = -1;
			while ((i = reader.read(buffer)) != -1) {
				os.write(buffer, 0, i);
			}
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return "下载失败";
		} finally {
			try{
				if (os != null) {
					reader.close();
				}
				if ( reader != null) {
					reader.close();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return NONE;
	}
	
	/**
	 * 文档编辑页面保持与服务端通讯，用于刷新WebUser的tmpspace缓存中文档的有效状态
	 */
	public void doConnect(){
		try {
			String docId = getParams().getParameterAsString("id");
			WebUser user = getUser();
			
			Document doc = (Document) user.getFromTmpspace(docId);
			
			if(doc !=null){
				//System.out.println("最后刷新时间："+user.getEffectiveDocumentFromTmpspace(docId).getRefreshTime());
				user.putToTmpspace(docId, doc);
			}
			return;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Environment getEnvironment() {
		String ctxPath = ServletActionContext.getRequest().getContextPath();
		Environment evt = Environment.getInstance();
		evt.setContextPath(ctxPath);
		return evt;
	}
}
