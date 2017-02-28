package cn.myapps.webservice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.HibernateSQLUtils;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.document.ejb.DocumentProcessBean;
import cn.myapps.core.dynaform.dts.datasource.ejb.DataSourceProcess;
import cn.myapps.core.dynaform.dts.datasource.ejb.DataSourceProcessBean;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormField;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workflow.element.FlowDiagram;
import cn.myapps.core.workflow.element.Node;
import cn.myapps.core.workflow.engine.StateMachine;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiProcess;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;
import cn.myapps.core.workflow.storage.runtime.ejb.NodeRT;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.json.JsonUtil;
import cn.myapps.util.xml.XmlUtil;
import cn.myapps.webservice.fault.DocumentServiceFault;
import cn.myapps.webservice.model.SimpleDocument;
import cn.myapps.webservice.model.SimpleNode;
import cn.myapps.webservice.util.DocumentUtil;

/**
 * 提供文档创建、查询、更新、删除、DQL查询、SQL查询的功能功能接口
 * @author Administrator
 *
 */
public class DocumentService {
	/**
	 * 获取应用使用的宾客
	 * 
	 * @param applicationId
	 *            应用标识
	 * @return 用户
	 * @throws Exception
	 */
	private WebUser getGuest(String applicationId) throws Exception {
		
		ApplicationProcess process = (ApplicationProcess) ProcessFactory.createProcess(ApplicationProcess.class);
		ApplicationVO app = (ApplicationVO) process.doView(applicationId);
		
		UserVO uservo = new UserVO();
		uservo.setId("guest");
		uservo.setName("guest");
		uservo.setLoginno("guest");
		uservo.setDomainid(app !=null? app.getDomainid():"");
		uservo.setApplicationid(applicationId);
		WebUser user = new WebUser(uservo);
		user.setDefaultApplication(applicationId);
		return user;
	}
	
	/**
	 * 禁用表单所有字段的刷新重计算
	 * @param form
	 * @throws DocumentServiceFault
	 */
	private void unCalculateOnRefresh(Form form) throws DocumentServiceFault {
		Iterator<FormField> iter = form.getAllFields().iterator();
		FormField field = null;
		while (iter.hasNext()) {
			field = (FormField) iter.next();
			if(field.isCalculateOnRefresh()){
				field.setCalculateOnRefresh(false);
			}
		}
	}

	/**
	 * 用宾客创建文档
	 * 
	 * @param formName
	 *            表单名称
	 * @param parameters
	 *            表单参数
	 * @param applicationId
	 *            表单所在应用标识
	 * @return  -1:失败 ,0:成功
	 * @throws DocumentServiceFault
	 */
	public int createDocumentByGuest(String formName, Map<String, Object> parameters, String applicationId)
			throws DocumentServiceFault {
		int result = -1;
		try {
			WebUser user = getGuest(applicationId);
			createDocument(formName, parameters, user, applicationId);
			
			result = 0;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentServiceFault(e.getMessage());
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new DocumentServiceFault(e.getMessage());
			}
		}
		return result;
	}
	
	/**
	 * 用宾客创建文档
	 * 
	 * @param formName
	 *            表单名称
	 * @param parameters
	 *            表单参数(Json格式)
	 * @param applicationId
	 *            表单所在应用标识
	 * @return -1:失败 ,0:成功
	 * @throws DocumentServiceFault
	 */
	public int createDocumentByGuest(String formName, String parameters, String applicationId)
			throws DocumentServiceFault {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			if(!StringUtil.isBlank(parameters))
				params = JsonUtil.toMap(parameters);
			return createDocumentByGuest(formName, params, applicationId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentServiceFault(e.getMessage());
		}
	}

	/**
	 * 用宾客更新文档
	 * 
	 * @param documentId
	 *            文档标识
	 * @param parameters
	 *            表单参数
	 * @param applicationId
	 *            表单所在应用标识
	 * @return  -1:失败 ,0:成功
	 * @throws DocumentServiceFault
	 */
	public int updateDocumentByGuest(String documentId, Map<String, Object> parameters, String applicationId)
			throws DocumentServiceFault {
		int result = -1;
		try {
			WebUser user = getGuest(applicationId);
			updateDocument(documentId, parameters, user, applicationId);
			
			result = 0;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentServiceFault(e.getMessage());
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new DocumentServiceFault(e.getMessage());
			}
		}
		return result;
	}
	
	/**
	 * 用宾客更新文档
	 * 
	 * @param documentId
	 *            文档标识
	 * @param parameters
	 *            表单参数(Json格式)
	 * @param applicationId
	 *            表单所在应用标识
	 * @return  -1:失败 ,0:成功
	 * @throws DocumentServiceFault
	 */
	public int updateDocumentByGuest(String documentId, String parameters, String applicationId)
			throws DocumentServiceFault {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			if(!StringUtil.isBlank(parameters))
				params = JsonUtil.toMap(parameters);
			return updateDocumentByGuest(documentId, params, applicationId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentServiceFault(e.getMessage());
		}
	}

	/**
	 * 用域用户创建文档
	 * 
	 * @param formName
	 *            表单名称
	 * @param parameters
	 *            表单参数
	 * @param domainUserId
	 *            域用户标识
	 * @param applicationId
	 *            表单所在应用标识
	 * 
	 * @return -1:失败 ,0:成功
	 * 
	 * @throws DocumentServiceFault
	 * 
	 */
	public int createDocumentByDomainUser(String formName, Map<String, Object> parameters, String domainUserId,
			String applicationId) throws DocumentServiceFault {
		int result = -1;
		try {
			UserVO user = WebServiceUtil.findUserWithValidate(domainUserId);
			createDocument(formName, parameters, new WebUser(user), applicationId);
			
			result = 0;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentServiceFault(e.getMessage());
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new DocumentServiceFault(e.getMessage());
			}
		}
		return result;
	}
	
	/**
	 * 用域用户创建文档
	 * 
	 * @param formName
	 *            表单名称
	 * @param parameters
	 *            表单参数(Json格式)
	 * @param domainUserId
	 *            域用户标识
	 * @param applicationId
	 *            表单所在应用标识
	 * 
	 * @return -1:失败 ,0:成功
	 * 
	 * @throws DocumentServiceFault
	 * 
	 */
	public int createDocumentByDomainUser(String formName, String parameters, String domainUserId,
			String applicationId) throws DocumentServiceFault {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			if(!StringUtil.isBlank(parameters))
				params = JsonUtil.toMap(parameters);
			return createDocumentByDomainUser(formName, params, domainUserId, applicationId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentServiceFault(e.getMessage());
		}
	}
	
	/**
	 * 用域用户创建文档并启动流程
	 * 
	 * @param formName
	 *            表单名称
	 * @param parameters
	 *            表单参数
	 * @param domainUserId
	 *            域用户标识
	 * @param applicationId
	 *            表单所在应用标识
	 * @return -1失败，0成功
	 * @throws DocumentServiceFault
	 */
	public int createDocumentAndStartFlowByDomainUser(String formName, Map<String, Object> parameters, String domainUserId,
			String applicationId) throws DocumentServiceFault {
		int result = -1;
		try {
			UserVO user = WebServiceUtil.findUserWithValidate(domainUserId);
			createDocumentAndStartFlow(formName, parameters, new WebUser(user), applicationId);
			
			result = 0;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentServiceFault(e.getMessage());
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new DocumentServiceFault(e.getMessage());
			}
		}
		return result;
	}
	
	/**
	 * 用域用户创建文档并启动流程
	 * 
	 * @param formName
	 *            表单名称
	 * @param parameters
	 *            表单参数(Json格式)
	 * @param domainUserId
	 *            域用户标识
	 * @param applicationId
	 *            表单所在应用标识
	 * @return -1失败，0成功
	 * @throws DocumentServiceFault
	 */
	public int createDocumentAndStartFlowByDomainUser(String formName, String parameters, String domainUserId,
			String applicationId) throws DocumentServiceFault {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			if(!StringUtil.isBlank(parameters))
				params = JsonUtil.toMap(parameters);
			return createDocumentAndStartFlowByDomainUser(formName, params, domainUserId, applicationId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentServiceFault(e.getMessage());
		}
	}

	/**
	 * 用域用户更新文档
	 * 
	 * @param documentId
	 *            文档标识
	 * @param parameters
	 *            表单参数
	 * @param domainUserId
	 *            域用户标识
	 * @param applicationId
	 *            表单所在应用标识
	 * @return  -1:失败 ,0:成功
	 * @throws DocumentServiceFault
	 */
	public int updateDocumentByDomainUser(String documentId, Map<String, Object> parameters, String domainUserId,
			String applicationId) throws DocumentServiceFault {
		int result = -1;
		try {
			UserVO user = WebServiceUtil.findUserWithValidate(domainUserId);
			updateDocument(documentId, parameters, new WebUser(user), applicationId);
			
			result = 0;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentServiceFault(e.getMessage());
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new DocumentServiceFault(e.getMessage());
			}
		}
		return result;
	}
	
	/**
	 * 用域用户更新文档
	 * 
	 * @param documentId
	 *            文档标识
	 * @param parameters
	 *            表单参数(Json格式)
	 * @param domainUserId
	 *            域用户标识
	 * @param applicationId
	 *            表单所在应用标识
	 * @return  -1:失败 ,0:成功
	 * @throws DocumentServiceFault
	 */
	public int updateDocumentByDomainUser(String documentId, String parameters, String domainUserId,
			String applicationId) throws DocumentServiceFault {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			if(!StringUtil.isBlank(parameters))
				params = JsonUtil.toMap(parameters);
			return updateDocumentByDomainUser(documentId, params, domainUserId, applicationId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentServiceFault(e.getMessage());
		}
	}

	/**
	 * 创建文档，返回文档主键
	 * 
	 * @param formName
	 * @param parameters
	 * @param user
	 * @param applicationId
	 * @return
	 * @throws DocumentServiceFault
	 */
	private String createDocument(String formName, Map<String, Object> parameters, WebUser user, String applicationId)
			throws DocumentServiceFault {
		try {
			FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
			
			WebServiceUtil.validateApplicationById(applicationId);
			
			DocumentProcess docProcess = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,
					applicationId);

			Form form = formProcess.doViewByFormName(formName, applicationId);
			if(form == null){
				throw new DocumentServiceFault("表单[" + formName + "]不存在软件[id="+applicationId+"]下.");
			}
			
			this.unCalculateOnRefresh(form);

			ParamsTable params = new ParamsTable();
			params.putAll(parameters);
			Document doc = form.createDocument(params, user, false);
	
			doc.setIstmp(false);
			docProcess.doCreateOrUpdate(doc, user);
			
			return doc.getId();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentServiceFault(e.getMessage());
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new DocumentServiceFault(e.getMessage());
			}
		}
	}
	
	/**
	 * 用域用户创建文档并启动流程
	 * 
	 * @param formName
	 *            表单名称
	 * @param parameters
	 *            表单参数(Json格式)
	 * @param domainUserId
	 *            域用户标识
	 * @param applicationId
	 *            表单所在应用标识
	 * @return -1失败，0成功
	 * @throws DocumentServiceFault
	 */
	public String createDocumentAndStartFlow(String formName, String parameters, String domainUserId,
			String applicationId) throws DocumentServiceFault {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			if(!StringUtil.isBlank(parameters))
				params = JsonUtil.toMap(parameters);
			UserVO user = WebServiceUtil.findUserWithValidate(domainUserId);
			return createDocumentAndStartFlow(formName, params, new WebUser(user), applicationId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentServiceFault(e.getMessage());
		}
	}
	
	/**
	 * 创建文档，如果有流程则创建流程
	 * 
	 * @param formName
	 * @param parameters
	 * @param user
	 * @param applicationId
	 * @return 文档主键
	 * @throws DocumentServiceFault
	 */
	private String createDocumentAndStartFlow(String formName, Map<String, Object> parameters, WebUser user, String applicationId)
			throws DocumentServiceFault {
		try {
			FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
			
			WebServiceUtil.validateApplicationById(applicationId);
			
			DocumentProcess docProcess = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,
					applicationId);

			Form form = formProcess.doViewByFormName(formName, applicationId);
			if(form == null){
				throw new DocumentServiceFault("表单[" + formName + "]不存在软件[id="+applicationId+"]下.");
			}
			
			this.unCalculateOnRefresh(form);

			ParamsTable params = new ParamsTable();
			params.putAll(parameters);
			Document doc = form.createDocument(params, user, false);
			
			// 设置启动流程相关参数
			this.setParameter(form, doc, user, params, applicationId);
			
			if (doc != null) {
				doc.setIstmp(false);
				docProcess.doStartFlowOrUpdate(doc, params, user);
				return doc.getId();
			}

			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentServiceFault(e.getMessage());
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new DocumentServiceFault(e.getMessage());
			}
		}
	}
	
	/**
	 * 设置启动流程相关参数
	 * @return
	 * @throws Exception 
	 */
	private void setParameter(Form form, Document doc, WebUser user, ParamsTable params, String applicationId) throws Exception{
		BillDefiProcess flowProcess = (BillDefiProcess) ProcessFactory.createProcess(BillDefiProcess.class);
		String flowName = params.getParameterAsString("flowname"); // 需启动的流程名称
		String flowid = null;
		if (!cn.myapps.util.StringUtil.isBlank(flowName)){
			BillDefiVO flowVO = (BillDefiVO) flowProcess.doViewBySubject(flowName, applicationId);
			if (flowVO != null){
				flowid = flowVO.getId();
			}
		}else{
			flowid = form.getOnActionFlow();
		}
		params.setParameter("_flowid", flowid);
	}

	private void updateDocument(String documentId, Map<String, Object> parameters, WebUser user, String applicationId)
			throws DocumentServiceFault {
		try {
			FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
			
			WebServiceUtil.validateApplicationById(applicationId);
			
			DocumentProcess docProcess = new DocumentProcessBean(applicationId);

			Document doc = (Document) docProcess.doView(documentId);
			if(doc == null){
				throw new DocumentServiceFault("文档[id=" + documentId + "]不存在软件[id="+applicationId+"]下.");
			}
			Form form = (Form) formProcess.doView(doc.getFormid());
			if(form == null){
				throw new DocumentServiceFault("文档[id=" + documentId + "]对应的表单[id=" + doc.getFormid() + "]不存在.");
			}
			
			this.unCalculateOnRefresh(form);

			ParamsTable params = new ParamsTable();
			params.putAll(parameters);

			doc = form.createDocument(doc, params, false, user);
			docProcess.doUpdate(doc);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentServiceFault(e.getMessage());
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new DocumentServiceFault(e.getMessage());
			}
		}
	}

	/**
	 * 删除文档
	 * 
	 * @param documentId
	 *            文档标识
	 * @param applicationId
	 *            应用标识
	 * @return  -1:失败 ,0:成功
	 * @throws DocumentServiceFault
	 */
	public int removeDocument(String documentId, String applicationId) throws DocumentServiceFault {
		int result = -1;
		try {
			// WebUser user = getGuest(applicationId);
			WebServiceUtil.validateApplicationById(applicationId);
			DocumentProcess docProcess = new DocumentProcessBean(applicationId);
			Document doc = (Document) docProcess.doView(documentId);
			if(doc != null){
				docProcess.doRemove(documentId);
			}
			
			result = 0;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentServiceFault(e.getMessage());
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new DocumentServiceFault(e.getMessage());
			}
		}
		return result;
	}

	/**
	 * 删除一条没有启动流程或者流程当前处理人为传入的user用户的文档信息 返回结果代码(-1:失败；0：成功)
	 * 
	 * @param documentId
	 *            文档id
	 * @param applicationId
	 *            软件id
	 * @return  -1:失败 ,0:成功
	 * @throws DocumentServiceFault
	 */
	public int removeDocumentByUser(String documentId, String applicationId, String userId) throws DocumentServiceFault {
		int result = -1;
		try {
			WebServiceUtil.validateApplicationById(applicationId);
			DocumentProcess docProcess = new DocumentProcessBean(applicationId);
			Document doc = (Document) docProcess.doView(documentId);
			if(doc != null){
				if(StringUtil.isBlank(doc.getStateid()) || 
						(!StringUtil.isBlank(doc.getAuditorList()) && doc.getAuditorList().indexOf(userId) > 0)){
					docProcess.doRemove(documentId);
					result = 0;
				}
			}
		} catch (Exception e) {
			throw new DocumentServiceFault(e.getMessage());
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new DocumentServiceFault(e.getMessage());
			}
		}
		return result;
	}
		
	/**
	 * 宾客通过参数查询出数据集
	 * 
	 * @param formName
	 *            表单名
	 * @param parameters
	 *            参数
	 * @param applicationId
	 *            应用标识
	 * @return SimpleDocument对象集合
	 * @throws DocumentServiceFault
	 */
	public Collection<SimpleDocument> searchDocumentsByFilter(String formName, Map<String, Object> parameters,
			String applicationId) throws DocumentServiceFault {
		try {
			return searchDocumentsByFilter(formName, parameters, applicationId, getGuest(applicationId));
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new DocumentServiceFault(e.getMessage());
			}
		}
	}
	
	/**
	 * 宾客通过参数查询出数据集
	 * 
	 * @param formName
	 *            表单名
	 * @param parameters
	 *            参数(Json格式)
	 * @param applicationId
	 *            应用标识
	 * @return SimpleDocument对象集合
	 * @throws DocumentServiceFault
	 */
	public Collection<SimpleDocument> searchDocumentsByFilter(String formName, String parameters,
			String applicationId) throws DocumentServiceFault {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			if(!StringUtil.isBlank(parameters))
				params = JsonUtil.toMap(parameters);
			return searchDocumentsByFilter(formName, params, applicationId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentServiceFault(e.getMessage());
		}
	}

	private Collection<SimpleDocument> searchDocumentsByFilter(String formName, Map<String, Object> parameters,
			String applicationId, WebUser webUser) throws DocumentServiceFault {
		try {
			WebServiceUtil.validateApplicationById(applicationId);
			DocumentProcess docProcess = new DocumentProcessBean(applicationId);
			FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
			Form form = formProcess.doViewByFormName(formName, applicationId);
			if(form == null){
				throw new DocumentServiceFault("表单[" + formName + "]不存在软件[id="+applicationId+"]下.");
			}
			
			String dql = "$formname='" + formName + "'";
			
			ParamsTable params = new ParamsTable();
			params.putAll(parameters);
			if(params != null){
				HibernateSQLUtils sqlUtil = new HibernateSQLUtils();
				String whereClause = sqlUtil.createWhere(params);
				if (whereClause != null && whereClause.trim().length() > 0) {
					dql += " AND " + whereClause;
				}
			}

			String _currpage = params.getParameterAsString("_currpage");
			String _pagelines = params.getParameterAsString("_pagelines");

			int page = (_currpage != null && _currpage.length() > 0) ? Integer.parseInt(_currpage) : 1;
			int lines = (_pagelines != null && _pagelines.length() > 0) ? Integer.parseInt(_pagelines)
					: Integer.MAX_VALUE;

			DataPackage<Document> dataPackage = docProcess.queryByDQLPage(dql, params, page, lines, webUser.getDomainid());
			return DocumentUtil.convertToSimpleDatas(dataPackage);

			// return (Collection<SimpleDocument>)dataPackage.getDatas();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentServiceFault(e.getMessage());
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new DocumentServiceFault(e.getMessage());
			}
		}
	}

	/**
	 * 域用户根据过滤的参数查询数据集
	 * 
	 * @param formName
	 *            表单名
	 * @param parameters
	 *            参数
	 * @param applicationId
	 *            应用标识
	 * @param domainUserId
	 *            域用户
	 * @return SimpleDocument对象集合
	 * @throws DocumentServiceFault
	 */
	public Collection<SimpleDocument> searchDocumentsByFilter(String formName, Map<String, Object> parameters,
			String applicationId, String domainUserId) throws DocumentServiceFault {
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			UserVO user = (UserVO) userProcess.doView(domainUserId);
			if (user != null) {
				return searchDocumentsByFilter(formName, parameters, applicationId, new WebUser(user));
			}
			return new ArrayList<SimpleDocument>();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentServiceFault(e.getMessage());
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new DocumentServiceFault(e.getMessage());
			}
		}
	}
	
	/**
	 * 域用户根据过滤的参数查询数据集
	 * 
	 * @param formName
	 *            表单名
	 * @param parameters
	 *            参数(Json格式)
	 * @param applicationId
	 *            应用标识
	 * @param domainUserId
	 *            域用户
	 * @return SimpleDocument对象集合
	 * @throws DocumentServiceFault
	 */
	public Collection<SimpleDocument> searchDocumentsByFilter(String formName, String parameters,
			String applicationId, String domainUserId) throws DocumentServiceFault {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			if(!StringUtil.isBlank(parameters))
				params = JsonUtil.toMap(parameters);
			return searchDocumentsByFilter(formName, params, applicationId, domainUserId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentServiceFault(e.getMessage());
		}
	}

	/**
	 * 域用户根据过滤的参数查询一条文档数据
	 * @param formName
	 *            表单名
	 * @param parameters
	 *            参数
	 * @param applicationId
	 *            应用标识
	 * @param domainUserId
	 *            域用户标识
	 * @return 简单的文档对象
	 * @throws DocumentServiceFault
	 */
	public SimpleDocument searchDocumentByFilter(String formName, Map<String, Object> parameters, String applicationId,
			String domainUserId) throws DocumentServiceFault {
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			UserVO user = (UserVO) userProcess.doView(domainUserId);
			if (user != null) {
				Collection<?> docList = searchDocumentsByFilter(formName, parameters, applicationId, new WebUser(user));
				if (docList != null && !docList.isEmpty()) {
					return (SimpleDocument) docList.iterator().next();
				}
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentServiceFault(e.getMessage());
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new DocumentServiceFault(e.getMessage());
			}
		}
	}
	
	/**
	 * 域用户根据过滤的参数查询一条文档数据
	 * @param formName
	 *            表单名
	 * @param parameters
	 *            参数(Json格式)
	 * @param applicationId
	 *            应用标识
	 * @param domainUserId
	 *            域用户标识
	 * @return 简单的文档对象
	 * @throws DocumentServiceFault
	 */
	public SimpleDocument searchDocumentByFilter(String formName, String parameters, String applicationId,
			String domainUserId) throws DocumentServiceFault {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			if(!StringUtil.isBlank(parameters))
				params = JsonUtil.toMap(parameters);
			return searchDocumentByFilter(formName, params, applicationId, domainUserId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentServiceFault(e.getMessage());
		}
	}

	/**
	 * 宾客通过参数查询出数据集,返回XML
	 * @param formName 表单名
	 * @param parameters 参数
	 * @param applicationId 应用标识
	 * @return 返回XML
	 * @throws DocumentServiceFault
	 */
	public String searchDocumentsByFilterFormat2XML(String formName, Map<String, Object> parameters,
			String applicationId) throws DocumentServiceFault {
		Collection<SimpleDocument> datas = searchDocumentsByFilter(formName, parameters, applicationId);
		return XmlUtil.toXml(datas);
	}
	
	/**
	 * 宾客通过参数查询出数据集,返回XML
	 * @param formName 表单名
	 * @param parameters 参数(Json格式)
	 * @param applicationId 应用标识
	 * @return 返回XML
	 * @throws DocumentServiceFault
	 */
	public String searchDocumentsByFilterFormat2XML(String formName, String parameters,
			String applicationId) throws DocumentServiceFault {
		Collection<SimpleDocument> datas = searchDocumentsByFilter(formName, parameters, applicationId);
		return XmlUtil.toXml(datas);
	}
	
	/**
	 * 域用户通过参数查询出数据集,返回XML
	 * @param formName 表单名
	 * @param parameters 参数
	 * @param applicationId 应用标识
	 * @param domainUserId 域用户
	 * @return 返回XML
	 * @throws DocumentServiceFault
	 */
	public String searchDocumentsByFilterFormat2XML(String formName, Map<String, Object> parameters,
			String applicationId, String domainUserId) throws DocumentServiceFault {
		Collection<SimpleDocument> datas = searchDocumentsByFilter(formName, parameters, applicationId, domainUserId);
		return XmlUtil.toXml(datas);
	}
	
	/**
	 * 域用户通过参数查询出数据集,返回XML
	 * @param formName 表单名
	 * @param parameters 参数(Json格式)
	 * @param applicationId 应用标识
	 * @param domainUserId 域用户
	 * @return 返回XML
	 * @throws DocumentServiceFault
	 */
	public String searchDocumentsByFilterFormat2XML(String formName, String parameters,
			String applicationId, String domainUserId) throws DocumentServiceFault {
		Collection<SimpleDocument> datas = searchDocumentsByFilter(formName, parameters, applicationId, domainUserId);
		return XmlUtil.toXml(datas);
	}
	
	/**
	 * 域用户根据传入过滤参数查询单条文档，返回XML
	 * @param formName 表单名
	 * @param parameters 参数
	 * @param applicationId 应用标识
	 * @param domainUserId 域用户
	 * @return 返回XML
	 * @throws DocumentServiceFault
	 */
	public String searchSimpleDocumentByFilterFormat2XML(String formName, Map<String, Object> parameters,
			String applicationId, String domainUserId) throws DocumentServiceFault {
		SimpleDocument data = searchDocumentByFilter(formName, parameters, applicationId, domainUserId);
		return XmlUtil.toXml(data);
	}
	
	/**
	 * 域用户根据传入过滤参数查询单条文档，返回XML
	 * @param formName 表单名
	 * @param parameters 参数(Json格式)
	 * @param applicationId 应用标识
	 * @param domainUserId 域用户
	 * @return 返回XML
	 * @throws DocumentServiceFault
	 */
	public String searchSimpleDocumentByFilterFormat2XML(String formName, String parameters,
			String applicationId, String domainUserId) throws DocumentServiceFault {
		SimpleDocument data = searchDocumentByFilter(formName, parameters, applicationId, domainUserId);
		return XmlUtil.toXml(data);
	}
	
	/**
	 * 宾客通过参数查询出数据集,返回Json
	 * @param formName 表单名
	 * @param parameters 参数
	 * @param applicationId 应用标识
	 * @return 返回Json
	 * @throws DocumentServiceFault
	 */
	public String searchDocumentsByFilterFormat2Json(String formName, Map<String, Object> parameters,
			String applicationId) throws DocumentServiceFault {
		Collection<SimpleDocument> datas = searchDocumentsByFilter(formName, parameters, applicationId);
		return JsonUtil.collection2Json(datas);
	}
	
	/**
	 * 宾客通过参数查询出数据集,返回Json
	 * @param formName 表单名
	 * @param parameters 参数(Json格式)
	 * @param applicationId 应用标识
	 * @return 返回Json
	 * @throws DocumentServiceFault
	 */
	public String searchDocumentsByFilterFormat2Json(String formName, String parameters,
			String applicationId) throws DocumentServiceFault {
		Collection<SimpleDocument> datas = searchDocumentsByFilter(formName, parameters, applicationId);
		return JsonUtil.collection2Json(datas);
	}
	
	/**
	 * 域用户通过参数查询出数据集,返回Json
	 * @param formName 表单名
	 * @param parameters 参数
	 * @param applicationId 应用标识
	 * @param domainUserId 域用户
	 * @return 返回Json
	 * @throws DocumentServiceFault
	 */
	public String searchDocumentsByFilterFormat2Json(String formName, Map<String, Object> parameters,
			String applicationId, String domainUserId) throws DocumentServiceFault {
		Collection<SimpleDocument> datas = searchDocumentsByFilter(formName, parameters, applicationId, domainUserId);
		return JsonUtil.collection2Json(datas);
	}
	
	/**
	 * 域用户通过参数查询出数据集,返回Json
	 * @param formName 表单名
	 * @param parameters 参数(Json格式)
	 * @param applicationId 应用标识
	 * @param domainUserId 域用户
	 * @return 返回Json
	 * @throws DocumentServiceFault
	 */
	public String searchDocumentsByFilterFormat2Json(String formName, String parameters,
			String applicationId, String domainUserId) throws DocumentServiceFault {
		Collection<SimpleDocument> datas = searchDocumentsByFilter(formName, parameters, applicationId, domainUserId);
		return JsonUtil.collection2Json(datas);
	}
	
	/**
	 * 域用户根据传入过滤参数查询单条文档，返回Json
	 * @param formName 表单名
	 * @param parameters 参数
	 * @param applicationId 应用标识
	 * @param domainUserId 域用户
	 * @return 返回Json
	 * @throws DocumentServiceFault
	 */
	public String searchSimpleDocumentByFilterFormat2Json(String formName, Map<String, Object> parameters,
			String applicationId, String domainUserId) throws DocumentServiceFault {
		SimpleDocument data = searchDocumentByFilter(formName, parameters, applicationId, domainUserId);
		return JsonUtil.toJson(data);
	}
	
	/**
	 * 域用户根据传入过滤参数查询单条文档，返回Json
	 * @param formName 表单名
	 * @param parameters 参数(Json格式)
	 * @param applicationId 应用标识
	 * @param domainUserId 域用户
	 * @return 返回Json
	 * @throws DocumentServiceFault
	 */
	public String searchSimpleDocumentByFilterFormat2Json(String formName, String parameters,
			String applicationId, String domainUserId) throws DocumentServiceFault {
		SimpleDocument data = searchDocumentByFilter(formName, parameters, applicationId, domainUserId);
		return JsonUtil.toJson(data);
	}
	
/***********************************************BySQL ********************************************************/
	private Collection<SimpleDocument> searchDocumentsBySQL(String sql,String applicationId, WebUser webUser)
				throws DocumentServiceFault {
		try {
			WebServiceUtil.validateApplicationById(applicationId);
			DocumentProcess docProcess = new DocumentProcessBean(applicationId);
			DataPackage<Document> dataPackage = docProcess.queryBySQL(sql, webUser.getDomainid());
			return DocumentUtil.convertToSimpleDatas(dataPackage);

		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentServiceFault(e.getMessage());
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new DocumentServiceFault(e.getMessage());
			}
		}
	}
	
	/**
	 * 通过sql语句查询出数据集
	 * @param sql
	 * @param applicationId 应用标识
	 * @return SimpleDocument对象集合
	 * @throws DocumentServiceFault
	 */
	public Collection<SimpleDocument> searchDocumentsBySQL(String sql, String applicationId) 
				throws DocumentServiceFault {
		try {
			return searchDocumentsBySQL(sql, applicationId, getGuest(applicationId));
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentServiceFault(e.getMessage());
		}
	}
	
	/**
	 * 传入sql语句查询数据集
	 * @param sql
	 * @param applicationId 应用标识
	 * @param domainUserId 域用户
	 * @return SimpleDocument对象集合
	 * @throws DocumentServiceFault
	 */
	public Collection<SimpleDocument> searchDocumentsBySQL(String sql, String applicationId, String domainUserId) 
				throws DocumentServiceFault {
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			UserVO user = (UserVO) userProcess.doView(domainUserId);
			if (user != null) {
				return searchDocumentsBySQL(sql, applicationId, new WebUser(user));
			}
			return new ArrayList<SimpleDocument>();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentServiceFault(e.getMessage());
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new DocumentServiceFault(e.getMessage());
			}
		}
	}
	
	/**
	 * 传入sql语句查询单条文档
	 * @param sql
	 * @param applicationId 应用标识
	 * @param domainUserId 域用户
	 * @return SimpleDocument对象
	 * @throws DocumentServiceFault
	 */
	public SimpleDocument searchDocumentBySQL(String sql, String applicationId,String domainUserId) 
				throws DocumentServiceFault {
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			UserVO user = (UserVO) userProcess.doView(domainUserId);
			if (user != null) {
				Collection<?> docList = searchDocumentsBySQL(sql, applicationId, new WebUser(user));
				if (docList != null && !docList.isEmpty()) {
					return (SimpleDocument) docList.iterator().next();
				}
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentServiceFault(e.getMessage());
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new DocumentServiceFault(e.getMessage());
			}
		}
	}
	
	/**
	 * 通过sql语句查询出数据集,返回XML
	 * @param sql
	 * @param applicationId 应用标识
	 * @return 返回XML
	 * @throws DocumentServiceFault
	 */
	public String searchDocumentsBySQLFormat2XML(String sql, String applicationId)
				throws DocumentServiceFault {
		Collection<SimpleDocument> datas = searchDocumentsBySQL(sql, applicationId);
		return XmlUtil.toXml(datas);
	}
	
	/**
	 * 通过sql语句查询出数据集,返回XML
	 * @param sql
	 * @param applicationId 应用标识
	 * @param domainUserId 域用户
	 * @return 返回XML
	 * @throws DocumentServiceFault
	 */
	public String searchDocumentsBySQLFormat2XML(String sql, String applicationId,String domainUserId)
				throws DocumentServiceFault {
		Collection<SimpleDocument> datas = searchDocumentsBySQL(sql, applicationId, domainUserId);
		return XmlUtil.toXml(datas);
	}
	
	/**
	 * 传入过滤参数查询单条文档，返回XML
	 * @param sql
	 * @param applicationId 应用标识
	 * @param domainUserId 域用户
	 * @return 返回XML
	 * @throws DocumentServiceFault
	 */
	public String searchSimpleDocumentBySQLFormat2XML(String sql, String applicationId,String domainUserId)
				throws DocumentServiceFault {
		SimpleDocument data = searchDocumentBySQL(sql, applicationId, domainUserId);
		return XmlUtil.toXml(data);
	}
	
	/**
	 * 通过sql语句查询出数据集,返回Json
	 * @param sql
	 * @param applicationId 应用标识
	 * @return 返回Json
	 * @throws DocumentServiceFault
	 */
	public String searchDocumentsBySQLFormat2Json(String sql, String applicationId)
				throws DocumentServiceFault {
		Collection<SimpleDocument> datas = searchDocumentsBySQL(sql, applicationId);
		return JsonUtil.collection2Json(datas);
	}
	
	/**
	 * 通过sql语句查询出数据集,返回Json
	 * @param sql
	 * @param applicationId 应用标识
	 * @param domainUserId 域用户
	 * @return 返回Json
	 * @throws DocumentServiceFault
	 */
	public String searchDocumentsBySQLFormat2Json(String sql, String applicationId,String domainUserId)
				throws DocumentServiceFault {
		Collection<SimpleDocument> datas = searchDocumentsBySQL(sql, applicationId, domainUserId);
		return JsonUtil.collection2Json(datas);
	}
	
	/**
	 * 传入sql语句查询单条文档，返回Json
	 * @param sql
	 * @param applicationId 应用标识
	 * @param domainUserId 域用户
	 * @return 返回Json
	 * @throws DocumentServiceFault
	 */
	public String searchSimpleDocumentBySQLFormat2Json(String sql, String applicationId, String domainUserId) 
				throws DocumentServiceFault {
		SimpleDocument data = searchDocumentBySQL(sql, applicationId, domainUserId);
		return JsonUtil.toJson(data);
	}
/********************************************** End BySQL *********************************************************/
	
/********************************************** ByDQL *********************************************************/
	private Collection<SimpleDocument> searchDocumentsByDQL(String dql,String applicationId, WebUser webUser) 
			throws DocumentServiceFault {
		try {
			WebServiceUtil.validateApplicationById(applicationId);
			DocumentProcess docProcess = new DocumentProcessBean(applicationId);
			DataPackage<Document> dataPackage = docProcess.queryByDQL(dql,webUser.getDomainid());
			return DocumentUtil.convertToSimpleDatas(dataPackage);

		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentServiceFault(e.getMessage());
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new DocumentServiceFault(e.getMessage());
			}
		}
	}
	
	/**
	* 通过dql语句查询出数据集
	* @param dql
	* @param applicationId 应用标识
	* @return SimpleDocument对象集合
	* @throws DocumentServiceFault
	*/
	public Collection<SimpleDocument> searchDocumentsByDQL(String dql,String applicationId) 
			throws DocumentServiceFault {
		try {
			return searchDocumentsByDQL(dql, applicationId,
					getGuest(applicationId));
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new DocumentServiceFault(e.getMessage());
			}
		}
	}
	
	/**
	* 传入dql语句查询数据集
	* @param dql
	* @param applicationId 应用标识
	* @param domainUserId 域用户
	* @return SimpleDocument对象集合
	* @throws DocumentServiceFault
	*/
	public Collection<SimpleDocument> searchDocumentsByDQL(String dql,String applicationId, String domainUserId)
			throws DocumentServiceFault {
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory
					.createProcess(UserProcess.class);
			UserVO user = (UserVO) userProcess.doView(domainUserId);
			if (user != null) {
				return searchDocumentsByDQL(dql, applicationId, new WebUser(
						user));
			}
			return new ArrayList<SimpleDocument>();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentServiceFault(e.getMessage());
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new DocumentServiceFault(e.getMessage());
			}
		}
	}
	
	/**
	* 传入dql语句查询单条文档
	* @param dql
	* @param applicationId 应用标识
	* @param domainUserId 域用户
	* @return 简单的文档对象
	* @throws DocumentServiceFault
	*/
	public SimpleDocument searchDocumentByDQL(String dql, String applicationId,
			String domainUserId) throws DocumentServiceFault {
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory
					.createProcess(UserProcess.class);
			UserVO user = (UserVO) userProcess.doView(domainUserId);
			if (user != null) {
				Collection<?> docList = searchDocumentsByDQL(dql,
						applicationId, new WebUser(user));
				if (docList != null && !docList.isEmpty()) {
					return (SimpleDocument) docList.iterator().next();
				}
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentServiceFault(e.getMessage());
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new DocumentServiceFault(e.getMessage());
			}
		}
	}
	
	/**
	* 通过dql语句查询出数据集,返回XML
	* @param dql
	* @param applicationId 应用标识
	* @return XML
	* @throws DocumentServiceFault
	*/
	public String searchDocumentsByDQLFormat2XML(String dql,String applicationId) 
			throws DocumentServiceFault {
		Collection<SimpleDocument> datas = searchDocumentsByDQL(dql,applicationId);
		return XmlUtil.toXml(datas);
	}
	
	/**
	* 通过dql语句查询出数据集,返回XML
	* @param dql
	* @param applicationId 应用标识
	* @param domainUserId 域用户
	* @return XML
	* @throws DocumentServiceFault
	*/
	public String searchDocumentsByDQLFormat2XML(String dql,String applicationId, String domainUserId)
			throws DocumentServiceFault {
		Collection<SimpleDocument> datas = searchDocumentsByDQL(dql,applicationId, domainUserId);
		return XmlUtil.toXml(datas);
	}
	
	/**
	* 传入过滤参数查询单条文档，返回XML
	* @param dql
	* @param applicationId 应用标识
	* @param domainUserId 域用户
	* @return 返回XML
	* @throws DocumentServiceFault
	*/
	public String searchSimpleDocumentByDQLFormat2XML(String dql,String applicationId, String domainUserId)
			throws DocumentServiceFault {
		SimpleDocument data = searchDocumentByDQL(dql, applicationId,domainUserId);
		return XmlUtil.toXml(data);
	}
		
	/**
	* 通过dql语句查询出数据集,返回Json
	* @param dql
	* @param applicationId 应用标识
	* @return Json
	* @throws DocumentServiceFault
	*/
	public String searchDocumentsByDQLFormat2Json(String dql,String applicationId) 
			throws DocumentServiceFault {
		Collection<SimpleDocument> datas = searchDocumentsByDQL(dql,applicationId);
		return JsonUtil.collection2Json(datas);
	}
		
	/**
	* 通过dql语句查询出数据集,返回Json
	* @param dql
	* @param applicationId 应用标识
	* @param domainUserId 域用户
	* @return Json
	* @throws DocumentServiceFault
	*/
	public String searchDocumentsByDQLFormat2Json(String dql,String applicationId, String domainUserId)
			throws DocumentServiceFault {
		Collection<SimpleDocument> datas = searchDocumentsByDQL(dql,applicationId, domainUserId);
		return JsonUtil.collection2Json(datas);
	}

	/**
	* 传入dql语句查询单条文档，返回Json
	* @param dql
	* @param applicationId 应用标识
	* @param domainUserId 域用户
	* @return 返回Json
	* @throws DocumentServiceFault
	*/
	public String searchSimpleDocumentByDQLFormat2Json(String dql, String applicationId, String domainUserId) 
			throws DocumentServiceFault {
		SimpleDocument data = searchDocumentByDQL(dql, applicationId, domainUserId);
		return JsonUtil.toJson(data);
	}
	
	/**
	 * 根据传递的sql语句向目标数据源查询数据，返回结果集
	 * @param sql
	 * @param dataSourceName
	 * @param applicationId
	 * @param domainUserId
	 * @return
	 * @throws DocumentServiceFault
	 */
	public Collection<?> doQuery(String sql,String dataSourceName,String applicationId,String domainUserId) throws DocumentServiceFault{
		DataSourceProcess process = new DataSourceProcessBean();
		
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory
					.createProcess(UserProcess.class);
			UserVO user = (UserVO) userProcess.doView(domainUserId);
			if (user != null) {
				return process.queryDataSourceSQL(dataSourceName, sql, applicationId);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentServiceFault(e.getMessage());
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new DocumentServiceFault(e.getMessage());
			}
		}
	}
	
	/**
	 * 根据传递的sql语句向目标数据源更新数据，返回结果代码
	 * @param sql
	 * @param dataSourceName
	 * @param applicationId
	 * @param domainUserId
	 * @return
	 * @throws DocumentServiceFault
	 */
	public int doUpdate(String sql,String dataSourceName,String applicationId,String domainUserId) throws DocumentServiceFault{
		DataSourceProcess process = new DataSourceProcessBean();
		int result = 0;
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory
					.createProcess(UserProcess.class);
			UserVO user = (UserVO) userProcess.doView(domainUserId);
			if (user != null) {
				process.createOrUpdate(dataSourceName, sql, applicationId);
				result = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentServiceFault(e.getMessage());
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new DocumentServiceFault(e.getMessage());
			}
		}
		return result;
	}
	
	
	/**
	 * 根据当前节点id，文档id，软件id来获取下一节点id
	 * @param currNodeId	当前节点id
	 * @param docId		文档id
	 * @param applicationid		软件id
	 * @return String[] 返回流程下一节点id
	 * @throws Exception 
	 * @author Alvin
	 */
	public String[] getNextNodeIdByCurrNode(String currNodeId, String docId, String applicationId) throws Exception{
		
		Document doc = null;
		try {
			DocumentProcess documentProcess = (DocumentProcess)ProcessFactory.createRuntimeProcess(DocumentProcess.class, applicationId);
			doc = (Document) documentProcess.doView(docId);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		String[] nodeStrings =null ;
		//state需要流程提交了才能有值
		if (doc.getState() != null) {
			BillDefiVO flowVO = doc.getState().getFlowVO();
			FlowDiagram fd =flowVO.toFlowDiagram();
			
			Node currnode = (Node) fd.getElementByID(currNodeId);
			Collection<Node> nextNodeList = fd.getNextNodeList(currnode.id,doc,null,null);
			
			nodeStrings = new String[nextNodeList.size()];
			
			int i=0 ;
			//用String数组来存放下一节点id
			for(Iterator it = nextNodeList.iterator(); it.hasNext();)
			{
				nodeStrings[i] = ((Node)it.next()).id;
				i++;
			}
		} 
		
		return nodeStrings;
	}
	
	/**
	 * 根据文档id，用户id，软件id来获取下一节点id
	 * @param docId		文档id
	 * @param userid	用户id
	 * @param applicationid		软件id
	 * @return String[] 返回流程下一节点id
	 * @throws Exception 
	 * @author Alvin
	 */
	public String[] getNextNodeId(String docId,String userId,String applicationId) throws Exception{
		
		WebUser user = null;
		Document doc = null;
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			user = (WebUser) userProcess.getWebUserInstance(userId);
			
			DocumentProcess documentProcess = (DocumentProcess)ProcessFactory.createRuntimeProcess(DocumentProcess.class, applicationId);
			doc = (Document) documentProcess.doView(docId);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		String[] nodeStrings =null ;
		//state需要流程提交了才能有值
		if (doc.getState() != null) {
			BillDefiVO flowVO = doc.getState().getFlowVO();
			FlowDiagram fd =flowVO.toFlowDiagram();
			
			NodeRT nodert = StateMachine.getCurrUserNodeRT(doc, user);
			String currNodeId = nodert.getNodeid();
			
			Node currnode = (Node) fd.getElementByID(currNodeId);
			Collection<Node> nextNodeList = fd.getNextNodeList(currnode.id,doc,null,user);
			
			nodeStrings = new String[nextNodeList.size()];
			
			int i=0 ;
			//用String数组来存放下一节点id
			for(Iterator it = nextNodeList.iterator(); it.hasNext();)
			{
				nodeStrings[i] = ((Node)it.next()).id;
				i++;
			}
		} 
		
		return nodeStrings;
	}
	
	
	/**
	 * 根据文档id，用户id，软件id来获取下一节点id
	 * @param docId		文档id
	 * @param userid	用户id
	 * @param applicationid		软件id
	 * @return Collection<SimpleNode> 返回流程下一节点id
	 * @throws Exception 
	 * @author Alvin
	 */
	public Collection<SimpleNode> getNextNode(String docId,String userId,String applicationId) throws Exception{
		
		WebUser user = null;
		Document doc = null;
		Collection<SimpleNode> datas = new ArrayList<SimpleNode>();
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			user = (WebUser) userProcess.getWebUserInstance(userId);
			
			DocumentProcess documentProcess = (DocumentProcess)ProcessFactory.createRuntimeProcess(DocumentProcess.class, applicationId);
			doc = (Document) documentProcess.doView(docId);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		//state需要流程提交了才能有值
		if (doc.getState() != null) {
			BillDefiVO flowVO = doc.getState().getFlowVO();
			FlowDiagram fd =flowVO.toFlowDiagram();
			
			NodeRT nodert = StateMachine.getCurrUserNodeRT(doc, user);
			String currNodeId = nodert.getNodeid();
			
			Node currnode = (Node) fd.getElementByID(currNodeId);
			Collection<Node> nextNodeList = fd.getNextNodeList(currnode.id,doc,null,user);
			
			int i=0 ;
			Node node = null;
			SimpleNode simpleNode = null;
			
			//用simpleNode来存放下一节点id
			for(Iterator it = nextNodeList.iterator(); it.hasNext();)
			{
				simpleNode = new SimpleNode();
				node = (Node)it.next();
				simpleNode.setId(node.id);
				simpleNode.setName(node.name);
				datas.add(simpleNode);
			}
		} 
		
		return datas;
	}
	
	/**
	 * 根据当前节点id，文档id，软件id来获取下一节点id
	 * @param currNodeId	当前节点id
	 * @param docId		文档id
	 * @param applicationid		软件id
	 * @return Collection<SimpleNode> 返回流程下一节点id
	 * @throws Exception 
	 * @author Alvin
	 */
	public Collection<SimpleNode> getNextNodeByCurrNode(String currNodeId, String docId, String applicationId) throws Exception{
		
		Document doc = null;
		Collection<SimpleNode> datas = new ArrayList<SimpleNode>();
		try {
			DocumentProcess documentProcess = (DocumentProcess)ProcessFactory.createRuntimeProcess(DocumentProcess.class, applicationId);
			doc = (Document) documentProcess.doView(docId);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		//state需要流程提交了才能有值
		if (doc.getState() != null) {
			BillDefiVO flowVO = doc.getState().getFlowVO();
			FlowDiagram fd =flowVO.toFlowDiagram();
			
			Node currnode = (Node) fd.getElementByID(currNodeId);
			Collection<Node> nextNodeList = fd.getNextNodeList(currnode.id,doc,null,null);
			
			int i=0 ;
			Node node = null;
			SimpleNode simpleNode = null;
			
			//用simpleNode来存放下一节点id
			for(Iterator it = nextNodeList.iterator(); it.hasNext();)
			{
				simpleNode = new SimpleNode();
				node = (Node)it.next();
				simpleNode.setId(node.id);
				simpleNode.setName(node.name);
				datas.add(simpleNode);
			}
		} 
		return datas;
	}
	
	/**
	 * 根据当前节点id，文档id，软件id来获取下一节点id
	 * @param currNodeId	当前节点id
	 * @param docId		文档id
	 * @param applicationid		软件id
	 * @return String 返回流程下一节点id（JSON格式）
	 * @throws Exception 
	 * @author Alvin
	 */
	public String getNextNodeJSONByCurrNode(String currNodeId, String docId, String applicationId) throws Exception{
		
		Document doc = null;
		Collection<SimpleNode> datas = new ArrayList<SimpleNode>();
		try {
			DocumentProcess documentProcess = (DocumentProcess)ProcessFactory.createRuntimeProcess(DocumentProcess.class, applicationId);
			doc = (Document) documentProcess.doView(docId);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		//state需要流程提交了才能有值
		if (doc.getState() != null) {
			BillDefiVO flowVO = doc.getState().getFlowVO();
			FlowDiagram fd =flowVO.toFlowDiagram();
			
			Node currnode = (Node) fd.getElementByID(currNodeId);
			Collection<Node> nextNodeList = fd.getNextNodeList(currnode.id,doc,null,null);
			
			int i=0 ;
			Node node = null;
			SimpleNode simpleNode = null;
			
			//用simpleNode来存放下一节点id
			for(Iterator it = nextNodeList.iterator(); it.hasNext();)
			{
				simpleNode = new SimpleNode();
				node = (Node)it.next();
				simpleNode.setId(node.id);
				simpleNode.setName(node.name);
				datas.add(simpleNode);
			}
		} 
		return JsonUtil.collection2Json(datas);
	}
	
	/**
	 * 根据文档id，用户id，软件id来获取下一节点id
	 * @param docId		文档id
	 * @param userid	用户id
	 * @param applicationid		软件id
	 * @return String 返回流程下一节点id（JSON格式）
	 * @throws Exception 
	 * @author Alvin
	 */
	public String getNextNodeJSON(String docId, String userId, String applicationId) throws Exception{
		
		WebUser user = null;
		Document doc = null;
		Collection<SimpleNode> datas = new ArrayList<SimpleNode>();
		try {
			DocumentProcess documentProcess = (DocumentProcess)ProcessFactory.createRuntimeProcess(DocumentProcess.class, applicationId);
			doc = (Document) documentProcess.doView(docId);
		
			UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			user = (WebUser) userProcess.getWebUserInstance(userId);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		//state需要流程提交了才能有值
		if (doc.getState() != null) {
			BillDefiVO flowVO = doc.getState().getFlowVO();
			FlowDiagram fd =flowVO.toFlowDiagram();
			
			NodeRT nodert = StateMachine.getCurrUserNodeRT(doc, user);
			String currNodeId = nodert.getNodeid();
			
			Node currnode = (Node) fd.getElementByID(currNodeId);
			Collection<Node> nextNodeList = fd.getNextNodeList(currnode.id,doc,null,user);
			
			int i=0 ;
			Node node = null;
			SimpleNode simpleNode = null;
			
			//用simpleNode来存放下一节点id
			for(Iterator it = nextNodeList.iterator(); it.hasNext();)
			{
				simpleNode = new SimpleNode();
				node = (Node)it.next();
				simpleNode.setId(node.id);
				simpleNode.setName(node.name);
				datas.add(simpleNode);
			}
		} 
		return JsonUtil.collection2Json(datas);
	}
	
	
/********************************************** End ByDQL *********************************************************/

	public static void main(String[] args){
		try {
			/*
			DocumentService ds = new DocumentService();
			String formName = "基本控件";
			String applicationId = "11de-ef9e-c010eee1-860c-e1cadb714510";
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("编辑", "1002guest");
			parameters.put("单选框", "kimi and 1002 域用户创建文档");
			ds.createDocumentByGuest(formName, parameters, applicationId);
			*/
			
//			DocumentService ds = new DocumentService();
//			ds.searchNextNodeList("11e4-81cc-f89b5be4-a8a9-45b21ff8a97f", "11e4-7f56-44f06e28-80eb-bde4458aa7ab");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
