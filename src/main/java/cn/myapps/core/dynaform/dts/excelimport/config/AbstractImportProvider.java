package cn.myapps.core.dynaform.dts.excelimport.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.dts.excelimport.AbstractSheet;
import cn.myapps.core.dynaform.dts.excelimport.Column;
import cn.myapps.core.dynaform.dts.excelimport.DetailSheet;
import cn.myapps.core.dynaform.dts.excelimport.ExcelMappingDiagram;
import cn.myapps.core.dynaform.dts.excelimport.LinkageKey;
import cn.myapps.core.dynaform.dts.excelimport.MasterSheet;
import cn.myapps.core.dynaform.form.ejb.CheckboxField;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormField;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.dynaform.form.ejb.RadioField;
import cn.myapps.core.dynaform.form.ejb.SelectAboutField;
import cn.myapps.core.dynaform.form.ejb.SelectField;
import cn.myapps.core.dynaform.form.ejb.SuggestField;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.sequence.Sequence;

/**
 * 抽象的Excel导入服务提供者
 * @author Happy.Lau
 */
public abstract class AbstractImportProvider implements ImportProvider{
	final static String IMPORT_FIELD_ID_PREFIX = Long.toHexString(new Date().getTime());
	public final static Logger log = Logger.getLogger(ImpExcelToDoc.class);
	
	
	private Workbook workBook;

	private ExcelMappingDiagram mappingConfig;
	

	public AbstractImportProvider(Workbook workBook,
			ExcelMappingDiagram mappingConfig) {
		super();
		this.workBook = workBook;
		this.mappingConfig = mappingConfig;
	}
	
	/**
	 * 对表单的字段进行校验
	 * 
	 * @param form
	 * @param doc
	 * @param subParams
	 * @param user
	 * @throws Exception
	 */
	public void validate(Form form, Document doc, ParamsTable subParams, WebUser user) throws Exception {
		// 对文档的字段进行校验
		Collection<ValidateMessage> errs = form.validate(doc, subParams, user, false);
		if (errs != null && errs.size() > 0) {
			StringBuffer fieldErrors = new StringBuffer();

			Iterator<ValidateMessage> iter4 = errs.iterator();
			while (iter4.hasNext()) {
				// ValidateMessage err = (ValidateMessage) iter4.next();
				ValidateMessage err = iter4.next();
				// 字段名称及出错信息
				fieldErrors.append(err.getFieldname() + "(" + err.getErrmessage() + ");");
			}

			fieldErrors.deleteCharAt(fieldErrors.lastIndexOf(";"));

			throw new OBPMValidateException(fieldErrors.toString());
		}
	}

	/**
	 * 获取主键的值映射
	 * 
	 * @param sheet
	 *            配置定义
	 * @param doc
	 *            文档
	 * @return
	 * @throws Exception
	 */
	 Map<String, String> getPrimaryKeyValueMap(AbstractSheet sheet, ParamsTable params) throws Exception {
		Map<String, String> rtn = new HashMap<String, String>();
		Collection<? extends Column> cols = sheet.getColumns();
		for (Iterator<?> iterator = cols.iterator(); iterator.hasNext();) {
			Column clm = (Column) iterator.next();
			if (clm.primaryKey) {
				rtn.put(clm.fieldName, params.getParameterAsString(clm.fieldName));
				break;//性能优化入加入此行代码, 假设excel主键只有一个的情况下可以减少迭代次数  2012-7-6
			}

		}
		return rtn;
	}
	 
	 /**
	 * 获取主键的值映射
	 * 
	 * @param sheet
	 *            配置定义
	 * @param doc
	 *            文档
	 * @return
	 * @throws Exception
	 */
	 Map<String, String> getPrimaryKeyValueMap(AbstractSheet sheet, Document conditionDoc) throws Exception {
		Map<String, String> rtn = new HashMap<String, String>();
		Collection<? extends Column> cols = sheet.getColumns();
		for (Iterator<?> iterator = cols.iterator(); iterator.hasNext();) {
			Column clm = (Column) iterator.next();
			if (clm.primaryKey) {
				rtn.put(clm.fieldName, conditionDoc.getItemValueAsString(clm.fieldName));
				break;//性能优化入加入此行代码, 假设excel主键只有一个的情况下可以减少迭代次数  2012-7-6
			}

		}
		return rtn;
	}

	Form rebuildFormFieldProperty(AbstractSheet sheet, Form form) {
		Iterator<? extends Column> iter = sheet.getColumns().iterator();
		while (iter.hasNext()) {
			Column clm = (Column) iter.next();
			FormField field = form.findFieldByName(clm.fieldName);
			if (field == null) {
				continue;
			}
			String importFieldId = IMPORT_FIELD_ID_PREFIX + "-" + field.getId();
			field.setId(importFieldId);

			// 替换 值脚本
			if (clm.valueScript != null && clm.valueScript.trim().length() > 0) {
				if("//".equals(clm.valueScript)){
					field.setValueScript(null);
				}else{
					field.setValueScript(clm.valueScript);
					}
				
			}
			

			// 替换 校验脚本
			if (clm.validateRule != null && clm.validateRule.trim().length() > 0) {
				if("//".equals(clm.validateRule)){
					field.setValidateRule(null);
				}else{
					field.setValidateRule(clm.validateRule);
				}
			}
			
			if(field instanceof RadioField){
				((RadioField) field).setOptionsScript(null);
			}
			if(field instanceof SelectField){
				((SelectField) field).setOptionsScript(null);
			}
			if(field instanceof CheckboxField){
				((CheckboxField) field).setOptionsScript(null);
			}
			if(field instanceof SuggestField){
				((SuggestField) field).setOptionsScript(null);
			}
			if(field instanceof SelectAboutField){
				((SelectAboutField) field).setOptionsScript(null);
			}

		}
		return form;
	}
	
	Map<String, Object> transExcelValueList2Params(AbstractSheet sheet, Map<?, ?> valueList) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		Iterator<? extends Column> iter = sheet.getColumns().iterator();
		while (iter.hasNext()) {
			Column clm = (Column) iter.next();
			if (clm.name != null && clm.fieldName != null) {
				if (sheet instanceof MasterSheet) {
					map.put(clm.fieldName, valueList.get(clm.name));
				} else if (sheet instanceof DetailSheet) {
					map.put(clm.fieldName, valueList.get(clm.name));
				}
			}
		}

		return map;
	}
	
	public String creatDocument(WebUser user, ParamsTable params, String applicationid) throws Exception {
		String rollback = getMappingConfig().getNeedRollback();
		if(!StringUtil.isBlank(rollback) && rollback.equals(ExcelMappingDiagram.IMPORT_ROLLBACK_ALL)){
			return creatDocument4Rollback(user, params, applicationid);
		}else if(!StringUtil.isBlank(rollback) && rollback.equals(ExcelMappingDiagram.IMPORT_NOT_ROLLBACK_ALL)){
			return creatDocument4NotRollback(user, params, applicationid);
		}else{
			return creatDocument4Rollback(user, params, applicationid);
		}
	}
	
	/**
	 * 发生异常时需要回滚数据
	 * @param user
	 * @param params
	 * @param applicationid
	 * @return
	 * @throws Exception
	 */
	public String creatDocument4Rollback(WebUser user, ParamsTable params, String applicationid) throws Exception {
		// //PersistenceUtils.getSessionSignal().sessionSignal++;
		FormProcess proxy = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		DocumentProcess docproxy = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,applicationid);

		Collection<String> errors = new ArrayList<String>();// 输出到前台的 异常 集合

		int rowCount = getMasterSheetRowCount();

		int row = 0;

		int[] detailErrorCount;

		int i = 0;
		// ArrayList parentDocList = new ArrayList();
		// ArrayList subDocList = new ArrayList();
		String _formName = getMappingConfig().getMasterSheet().formName;
		Form form = (Form) proxy.doViewByFormName(_formName, applicationid);
		if(form == null){
			throw new OBPMValidateException("找不到表单:["+_formName+"],请检查Excel导入配置中MasterSheet的映射设置是否正确！");
		}
		form = rebuildFormFieldProperty(getMappingConfig().getMasterSheet(), form);//12-7-6 性能优化 (Form) form.clone());
		// boolean flag = false; // 标志MasterSheet出错还是detialSheet出错

		String rowError = ""; // 主表单出错信息
		String parentid = (String) params.getParameter("parentid");// 当导入为子表单数据时会使用
		String isRelate = (String) params.getParameter("isRelate");
		
		Collection<LinkageKey> childLinkageKey = getMappingConfig().getLinkageKeys();
		detailErrorCount = new int[childLinkageKey.size()];
		for(int k=0; k<detailErrorCount.length; k++){
			detailErrorCount[k] = 1;
		}
		Map<String,Form> subForms = new HashMap<String, Form>();
		for (Iterator<LinkageKey> iterator = childLinkageKey.iterator(); iterator.hasNext();) {
			LinkageKey key = (LinkageKey) iterator.next();
			String formName = key.getDetailSheet().formName;
			
			Form subform = (Form) proxy.doViewByFormName(formName, applicationid);
			subform = rebuildFormFieldProperty(key.getDetailSheet(), subform);//(Form) subform.clone());
			subForms.put(formName, subform);
		}
		try {
			docproxy.beginTransaction();
			for (i = 1; i < rowCount; i++) {
				rowError = getMappingConfig().getMasterSheet().name + " {*[Row]*}[" + (i + 1) + "]: ";

				long start = System.currentTimeMillis();

				// flag = false;
				ParamsTable parentParams = new ParamsTable(params);

				Map<String, String> values = getMasterSheetRow(i);

				log.debug("Import SETP-1 times->" + (System.currentTimeMillis() - start) + "(ms)");

				if (values == null) {
					continue;
				} else {
					row++;
				}
				Map<String, Object> fieldValues = transExcelValueList2Params(getMappingConfig().getMasterSheet(), values);

				parentParams.putAll(fieldValues);
				
				if(!StringUtil.isBlank(parentid)){
					parentParams.setParameter("parentid", parentid);
				}
				if(!StringUtil.isBlank(isRelate)){
					parentParams.setParameter("isRelate", isRelate);
				}

				log.debug("Import SETP-2 times->" + (System.currentTimeMillis() - start) + "(ms)");

				// 条件文档
				//Document parentDoc = form.createDocument(parentParams, user);
//				Map<String, String> condition = getPrimaryKeyValueMap(getMappingConfig().getMasterSheet(), parentDoc);
//				Map<String, String> condition = getPrimaryKeyValueMap(getMappingConfig().getMasterSheet(), parentParams);

//				boolean isCreate = true;
//
				Document parentDoc = form.createDocument(parentParams, user);
				ParamsTable _params = new ParamsTable();
				_params.putAll(parentDoc.toMap());
				Map<String, String> condition = getPrimaryKeyValueMap(getMappingConfig().getMasterSheet(), _params);
				Document _parentDoc = null;
				if (!condition.isEmpty()) {
					_parentDoc = docproxy.doViewByCondition(form.getName(), condition, user);
					if (_parentDoc != null) {
						parentDoc = form.createDocument(_parentDoc, parentParams, user);
						if (parentid != null && !parentid.equals("")) {
							parentDoc.setParent(parentid);
						}
//						isCreate = false;
					}
				}
				if(_parentDoc ==null){
					parentDoc.set_new(true);
				}
//
//				if (isCreate) { // 是否创建新文档
//					parentDoc = form.createDocument(parentParams, user);
//					if (form.getOnActionFlow() != null) {
//						parentDoc.setFlowid(form.getOnActionFlow());
//						if (parentid != null && !parentid.equals("")) {
//							parentDoc.setParent(parentid);
//						}
//					}
////					docproxy.doCreate(parentDoc,user);重复保存 
//				}

				log.debug("Import SETP-3 times->" + (System.currentTimeMillis() - start) + "(ms)");
				log.debug("Import SETP-4 times->" + (System.currentTimeMillis() - start) + "(ms)");

				log.debug("Import SETP-5 times->" + (System.currentTimeMillis() - start) + "(ms)");

				Iterator<LinkageKey> iter = childLinkageKey.iterator();

				log.debug("Import SETP-6 times->" + (System.currentTimeMillis() - start) + "(ms)");

//				Collection<Document> childs = new HashSet<Document>();没发现有用处 会占用内存空间 2012-7-12
				int j = 0;
				while (iter.hasNext()) {
					// LinkageKey key = (LinkageKey) iter.next();
					LinkageKey key = iter.next();

					String sheetName = key.getDetailSheet().name;
					String columnName = key.getDetailSheetKeyColumn().name;
					String matchValue = (String) values.get(key.getMasterSheetKeyColumn().name);

					Collection<LinkedHashMap<String, String>> detailRows = getDetailSheetRowCollection(sheetName,
							columnName, matchValue);
					int detailRow = 0;

					String formName = key.getDetailSheet().formName;
//					
//					Form subform = (Form) proxy.doViewByFormName(formName, applicationid);
//					subform = rebuildFormFieldProperty(key.getDetailSheet(), (Form) subform.clone());
					Form subform = subForms.get(formName);
					
					
					if(!StringUtil.isBlank(parentDoc.getId())){
						Collection<Document> subDocs = parentDoc.getChilds(formName);
						// 更新则把原有子表单删除
						if (subDocs != null && !subDocs.isEmpty()) {
							for (Iterator<Document> iterator = subDocs.iterator(); iterator.hasNext();) {
								// Document subDoc = (Document) iterator.next();
								Document subDoc = iterator.next();
								docproxy.doRemove(subDoc.getId());
							}
						}
					}

					String subRowError = ""; // 子表单出错信息
					try {
						Iterator<LinkedHashMap<String, String>> iter2 = detailRows.iterator();
						if (iter2.hasNext()) {
							do {
								subRowError = sheetName + " {*[Row]*}[" + (detailErrorCount[j] + 1) + "]: ";
								Map<String, Object> tmp = transExcelValueList2Params(key.getDetailSheet(),
										(LinkedHashMap<String, String>) iter2.next());
								ParamsTable subParams = new ParamsTable(params);
								subParams.setParameter("parentid", parentDoc.getId());
								subParams.putAll(tmp);

								// 此内容参考Form的createDocument
								Document subdoc = new Document();
								subdoc.setId(Sequence.getSequence());
								subdoc.setAuthor(user);
								subdoc.setCreated(new Date());
								subdoc.setIstmp(false);
								subdoc.setLastmodifier(user.getId());
								subdoc.setLastmodified(new Date());
								subdoc.setParent(parentDoc);
								subdoc.set_new(true);

								if (subform.getType() == Form.FORM_TYPE_NORMAL) {
									subdoc.setMappingId(subdoc.getId());
								} else if (subform.getType() == Form.FORM_TYPE_NORMAL_MAPPING) {
									subdoc.setMappingId(Sequence.getUUID());
								}

								subdoc = subform.createDocument(subdoc, subParams, user);

								try {
									validate(subform, subdoc, subParams, user); // 对子文档的正确性进行校验

//									childs.add(subdoc); 没发现有用处 会占用内存空间 2012-7-12
									docproxy.doCreateOrUpdate4ExcelImport(subdoc, user);
								} catch (Exception e) {
									errors.add(subRowError + e.getMessage());
									throw new OBPMValidateException(subRowError + e.getMessage());
								}

								detailErrorCount[j]++;
								detailRow++;
							} while (iter2.hasNext());
						} else if (matchValue == null && detailRow == 0) {
							throw new OBPMValidateException(subRowError + " [" + columnName + " 不能为空]");
						}
					} catch (Throwable t) {
						throw new OBPMValidateException(subRowError + t.getMessage());
					}
					j++;
				}

				log.debug("Import SETP-7 times->" + (System.currentTimeMillis() - start) + "(ms)");

				try {
					// 对主文档的正确性进行校验
					validate(form, parentDoc, parentParams, user);
					// 获取主键映射值
//					docproxy.doStartFlowOrUpdate(parentDoc, parentParams, user);//此方法会查询文档的流程实例等信息 增加复杂度 导致性能下降
					docproxy.doCreateOrUpdate4ExcelImport(parentDoc, user);
					log.info("Import " + getMappingConfig().getMasterSheet().name + " Row " + i + " SUCCESS");
					log.debug("Import SETP-0 times->" + (System.currentTimeMillis() - start) + "(ms)");
				} catch (Exception e) {
					errors.add(rowError + e.getMessage());
					throw new OBPMValidateException(rowError + e.getMessage());
				}
			}
			if (!errors.isEmpty()) {
				StringBuffer es=new StringBuffer(); 
				Object[] err = errors.toArray();
				for(i=0;i<err.length;i++){
					es.append(err[i]);
				}
				throw new OBPMValidateException(es.toString(),new ImpExcelException(errors));
			} 
			
			docproxy.commitTransaction();
			
		} catch (Exception e) {
			docproxy.rollbackTransaction();
			e.printStackTrace();
			throw e;
		} finally {
			// PersistenceUtils.closeSession();
			docproxy.closeConnection();
			PersistenceUtils.currentSession().clear();
		}

		String msg = "";
		if (row == 0 && i == rowCount) {
			msg = "{*[Error]*}[Excel上传文件中主表单无数据]";
			throw new OBPMValidateException(msg);
		} else if (row != 0) {
			msg = "{*[cn.myapps.core.dynaform.dts.excelimport.success.total.imported]*} (" + row + ") {*[Row]*}";
		}

		return msg;

	}

	/**
	 * 发生异常时不回滚数据  200条数据提交一次
	 * @param user
	 * @param params
	 * @param applicationid
	 * @return
	 * @throws Exception
	 */
	public String creatDocument4NotRollback(WebUser user, ParamsTable params, String applicationid) throws Exception {
		//PersistenceUtils.getSessionSignal().sessionSignal++;
		FormProcess proxy = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
		DocumentProcess docproxy = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,applicationid);

		Collection<String> errors = new ArrayList<String>();// 输出到前台的 异常 集合

		int rowCount = getMasterSheetRowCount();

		int row = 0;

		int[] detailErrorCount;

		int i = 0;
		// ArrayList parentDocList = new ArrayList();
		// ArrayList subDocList = new ArrayList();
		String _formName = getMappingConfig().getMasterSheet().formName;
		Form form = (Form) proxy.doViewByFormName(_formName, applicationid);
		if(form == null){
			throw new OBPMValidateException("找不到表单:["+_formName+"],请检查Excel导入配置中MasterSheet的映射设置是否正确！");
		}
		form = rebuildFormFieldProperty(getMappingConfig().getMasterSheet(), form);//12-7-6 性能优化 (Form) form.clone());
		// boolean flag = false; // 标志MasterSheet出错还是detialSheet出错

		String rowError = ""; // 主表单出错信息
		String parentid = (String) params.getParameter("parentid");// 当导入为子表单数据时会使用
		String isRelate = (String) params.getParameter("isRelate");
		
		Collection<LinkageKey> childLinkageKey = getMappingConfig().getLinkageKeys();
		detailErrorCount = new int[childLinkageKey.size()];
		for(int k=0; k<detailErrorCount.length; k++){
			detailErrorCount[k] = 1;
		}
		Map<String,Form> subForms = new HashMap<String, Form>();
		for (Iterator<LinkageKey> iterator = childLinkageKey.iterator(); iterator.hasNext();) {
			LinkageKey key = (LinkageKey) iterator.next();
			String formName = key.getDetailSheet().formName;
			
			Form subform = (Form) proxy.doViewByFormName(formName, applicationid);
			subform = rebuildFormFieldProperty(key.getDetailSheet(), subform);//(Form) subform.clone());
			subForms.put(formName, subform);
		}
		try {
			int count = 0;
			docproxy.beginTransaction();
			for (i = 1; i < rowCount; i++) {
				count++;
				rowError = getMappingConfig().getMasterSheet().name + " {*[Row]*}[" + (i + 1) + "]: ";

				long start = System.currentTimeMillis();

				// flag = false;
				ParamsTable parentParams = new ParamsTable(params);

				Map<String, String> values = getMasterSheetRow(i);

				log.debug("Import SETP-1 times->" + (System.currentTimeMillis() - start) + "(ms)");

				if (values == null) {
					continue;
				} else {
					row++;
				}
				Map<String, Object> fieldValues = transExcelValueList2Params(getMappingConfig().getMasterSheet(), values);

				parentParams.putAll(fieldValues);
				
				if(!StringUtil.isBlank(parentid)){
					parentParams.setParameter("parentid", parentid);
				}
				if(!StringUtil.isBlank(isRelate)){
					parentParams.setParameter("isRelate", isRelate);
				}

				log.debug("Import SETP-2 times->" + (System.currentTimeMillis() - start) + "(ms)");

				// 条件文档
				//Document parentDoc = form.createDocument(parentParams, user);
//				Map<String, String> condition = getPrimaryKeyValueMap(getMappingConfig().getMasterSheet(), parentDoc);
//				Map<String, String> condition = getPrimaryKeyValueMap(getMappingConfig().getMasterSheet(), parentParams);

//				boolean isCreate = true;
//
				Document parentDoc = form.createDocument(parentParams, user);
				ParamsTable _params = new ParamsTable();
				_params.putAll(parentDoc.toMap());
				Map<String, String> condition = getPrimaryKeyValueMap(getMappingConfig().getMasterSheet(), _params);
				Document _parentDoc = null;
				if (!condition.isEmpty()) {
					_parentDoc = docproxy.doViewByCondition(form.getName(), condition, user);
					if (_parentDoc != null) {
						parentDoc = form.createDocument(_parentDoc, parentParams, user);
						if (parentid != null && !parentid.equals("")) {
							parentDoc.setParent(parentid);
						}
//						isCreate = false;
					}
				}
				if(_parentDoc ==null){
					parentDoc.set_new(true);
				}
//
//				if (isCreate) { // 是否创建新文档
//					parentDoc = form.createDocument(parentParams, user);
//					if (form.getOnActionFlow() != null) {
//						parentDoc.setFlowid(form.getOnActionFlow());
//						if (parentid != null && !parentid.equals("")) {
//							parentDoc.setParent(parentid);
//						}
//					}
////					docproxy.doCreate(parentDoc,user);重复保存 
//				}

				log.debug("Import SETP-3 times->" + (System.currentTimeMillis() - start) + "(ms)");
				log.debug("Import SETP-4 times->" + (System.currentTimeMillis() - start) + "(ms)");

				log.debug("Import SETP-5 times->" + (System.currentTimeMillis() - start) + "(ms)");

				Iterator<LinkageKey> iter = childLinkageKey.iterator();

				log.debug("Import SETP-6 times->" + (System.currentTimeMillis() - start) + "(ms)");

//				Collection<Document> childs = new HashSet<Document>();没发现有用处 会占用内存空间 2012-7-12
				int j = 0;
				while (iter.hasNext()) {
					// LinkageKey key = (LinkageKey) iter.next();
					LinkageKey key = iter.next();

					String sheetName = key.getDetailSheet().name;
					String columnName = key.getDetailSheetKeyColumn().name;
					String matchValue = (String) values.get(key.getMasterSheetKeyColumn().name);

					Collection<LinkedHashMap<String, String>> detailRows = getDetailSheetRowCollection(sheetName,
							columnName, matchValue);
					int detailRow = 0;

					String formName = key.getDetailSheet().formName;
//					
//					Form subform = (Form) proxy.doViewByFormName(formName, applicationid);
//					subform = rebuildFormFieldProperty(key.getDetailSheet(), (Form) subform.clone());
					Form subform = subForms.get(formName);
					
					
					if(!StringUtil.isBlank(parentDoc.getId())){
						Collection<Document> subDocs = parentDoc.getChilds(formName);
						// 更新则把原有子表单删除
						if (subDocs != null && !subDocs.isEmpty()) {
							for (Iterator<Document> iterator = subDocs.iterator(); iterator.hasNext();) {
								// Document subDoc = (Document) iterator.next();
								Document subDoc = iterator.next();
								docproxy.doRemove(subDoc.getId());
							}
						}
					}

					String subRowError = ""; // 子表单出错信息
					try {
						Iterator<LinkedHashMap<String, String>> iter2 = detailRows.iterator();
						if (iter2.hasNext()) {
							do {
								subRowError = sheetName + " {*[Row]*}[" + (detailErrorCount[j] + 1) + "]: ";
								Map<String, Object> tmp = transExcelValueList2Params(key.getDetailSheet(),
										(LinkedHashMap<String, String>) iter2.next());
								ParamsTable subParams = new ParamsTable(params);
								subParams.setParameter("parentid", parentDoc.getId());
								subParams.putAll(tmp);

								// 此内容参考Form的createDocument
								Document subdoc = new Document();
								subdoc.setId(Sequence.getSequence());
								subdoc.setAuthor(user);
								subdoc.setCreated(new Date());
								subdoc.setIstmp(false);
								subdoc.setLastmodifier(user.getId());
								subdoc.setLastmodified(new Date());
								subdoc.setParent(parentDoc);
								subdoc.set_new(true);

								if (subform.getType() == Form.FORM_TYPE_NORMAL) {
									subdoc.setMappingId(subdoc.getId());
								} else if (subform.getType() == Form.FORM_TYPE_NORMAL_MAPPING) {
									subdoc.setMappingId(Sequence.getUUID());
								}

								subdoc = subform.createDocument(subdoc, subParams, user);

								try {
									validate(subform, subdoc, subParams, user); // 对子文档的正确性进行校验

//									childs.add(subdoc); 没发现有用处 会占用内存空间 2012-7-12
									docproxy.doCreateOrUpdate4ExcelImport(subdoc, user);
								} catch (Exception e) {
									errors.add(subRowError + e.getMessage());
//									throw new Exception(subRowError + e.getMessage());
								}

								detailErrorCount[j]++;
								detailRow++;
							} while (iter2.hasNext());
						} else if (matchValue == null && detailRow == 0) {
							throw new OBPMValidateException(subRowError + " [" + columnName + " 不能为空]");
						}
					} catch (Throwable t) {
						//throw new Exception(subRowError + t.getMessage());
					}
					j++;
				}

				log.debug("Import SETP-7 times->" + (System.currentTimeMillis() - start) + "(ms)");

				try {
					// 对主文档的正确性进行校验
					validate(form, parentDoc, parentParams, user);
					// 获取主键映射值
//					docproxy.doStartFlowOrUpdate(parentDoc, parentParams, user);//此方法会查询文档的流程实例等信息 增加复杂度 导致性能下降
					docproxy.doCreateOrUpdate4ExcelImport(parentDoc, user);
					log.info("Import " + getMappingConfig().getMasterSheet().name + " Row " + i + " SUCCESS");
					log.debug("Import SETP-0 times->" + (System.currentTimeMillis() - start) + "(ms)");
				} catch (Exception e) {
					errors.add(rowError + e.getMessage());
				}
				
				if(count==500){
					PersistenceUtils.currentSession().flush();
					PersistenceUtils.currentSession().clear();
					docproxy.commitTransaction();
					docproxy.beginTransaction();
					count = 0;
				}
			}
			
			
		}catch(Exception e){
//			docproxy.rollbackTransaction();
			e.printStackTrace();
			throw e;
			
		}try{
			if (!errors.isEmpty()) {
				StringBuffer es=new StringBuffer(); 
				Object[] err = errors.toArray();
				for(i=0;i<err.length;i++){
					es.append(err[i]);
				}
				throw new OBPMValidateException(es.toString(),new ImpExcelException(errors));
			} 			
		} catch (Exception e) {
//			docproxy.rollbackTransaction();
			throw e;
			
		} finally {
			PersistenceUtils.currentSession().flush();
			docproxy.commitTransaction();
			// PersistenceUtils.closeSession();
			docproxy.closeConnection();
			PersistenceUtils.currentSession().clear();
		}

		String msg = "";
		if (row == 0 && i == rowCount) {
			msg = "{*[Error]*}[Excel上传文件中主表单无数据]";
			throw new OBPMValidateException(msg);
		} else if (row != 0) {
			msg = "{*[cn.myapps.core.dynaform.dts.excelimport.success.total.imported]*} (" + row + ") {*[Row]*}";
		}

		return msg;

	}

	public Workbook getWorkBook() {
		return workBook;
	}


	public void setWorkBook(Workbook workBook) {
		this.workBook = workBook;
	}


	public ExcelMappingDiagram getMappingConfig() {
		return mappingConfig;
	}


	public void setMappingConfig(ExcelMappingDiagram mappingConfig) {
		this.mappingConfig = mappingConfig;
	}

	
	
	
}
