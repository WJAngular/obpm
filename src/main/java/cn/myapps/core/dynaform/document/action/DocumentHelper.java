package cn.myapps.core.dynaform.document.action;

import java.io.File;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;


import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.constans.Environment;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiProcess;
import cn.myapps.core.workflow.storage.definition.ejb.BillDefiVO;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowHistory;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRT;
import cn.myapps.core.workflow.storage.runtime.ejb.FlowStateRTProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHIS;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHISProcess;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.cache.MemoryCacheUtil;
import cn.myapps.util.property.DefaultProperty;

public class DocumentHelper {
	/**
	 * 根据文档主键,返回相应文档
	 * 
	 * @param id
	 *            文档主键
	 * @return 文档
	 * @throws Exception
	 */

	public static Document getDocumentById(String id, String applicationid) throws Exception {
		DocumentProcess dp = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,applicationid);
		return (Document) dp.doView(id);
	}
	
	public static Document getDocumentByFlow(String id, String flowid) throws Exception {
		BillDefiProcess billDefiProcess = (BillDefiProcess) ProcessFactory.createProcess(BillDefiProcess.class);
		BillDefiVO flowVO = (BillDefiVO) billDefiProcess.doView(flowid);
		
		DocumentProcess dp = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class,flowVO.getApplicationid());
		return (Document) dp.doView(id);
	}
	
	public static String toHistoryHtml(HttpServletRequest request) throws Exception{
		String docid = request.getParameter("_docid");
		String flowStateId = request.getParameter("flowStateId");
		String applicationId = request.getParameter("application");
		if (!StringUtil.isBlank(flowStateId) && !StringUtil.isBlank(applicationId) && !StringUtil.isBlank(docid)) {
			FlowStateRTProcess process = (FlowStateRTProcess) ProcessFactory.createRuntimeProcess(FlowStateRTProcess.class, applicationId);
			FlowStateRT instance = (FlowStateRT) process.doView(flowStateId);
			if(instance ==null) return null;
			RelationHISProcess hisProcess = (RelationHISProcess) ProcessFactory.createRuntimeProcess(RelationHISProcess.class, applicationId);
			Collection<RelationHIS> colls = hisProcess.doAllQueryByDocIdAndFlowStateId(docid, flowStateId);
			FlowHistory his = new FlowHistory();
			his.addAllHis(colls);

			return his.toTextHtml();
		}
		return "";
	}
	
	/**
	 * 重新构建文档
	 * @param doc
	 * @param params
	 * @param user
	 * @return
	 */
	public static Document rebuildDocument(Document doc, ParamsTable params,WebUser user) {
		String formid = params.getParameterAsString("_formid");
		try {
			if(!StringUtil.isBlank(params.getParameterAsString("_refreshDocument")) && !StringUtil.isBlank(doc.getId()) && !StringUtil.isBlank(formid)){
				doc = (Document) MemoryCacheUtil.getFromPrivateSpace(doc.getId(), user);
				FormProcess formPross = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
				Form form = (Form) formPross.doView(formid);
				doc = form.createDocument(doc, params, user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	/**查询word文件是否存在(1.表示文件存在,0:表示文件不存在)
	 * @param params
	 * @return
	 */
	public static String isWordFileExist(ParamsTable params) {
		String _docid[] = params.getParameterAsArray("filename");
		if (_docid != null) {
			String dir = DefaultProperty.getProperty("WEB_DOCPATH");
			String realPath = Environment.getInstance().getRealPath(dir);
			int index = _docid[0].indexOf("_");
			if(index >0){
				_docid[0] = _docid[0].substring(0, index);
			}
			File file = new File(realPath + File.separator + _docid[0]);
			boolean exist = file.exists();
			if (exist) {// 1.文件存在,0不存在
				return "1";
			} else {
				return "0";
			}
		}
		return "0";
	}
	
	/**
	 * 遍历word文件
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static String listWordFiles(String path, String domainid) throws Exception {
		//2016.8.24---与旧数据兼容，保留默认目录下的word文件作为所有企业域的文件
		String dir = DefaultProperty.getProperty(path);
		String realPath = Environment.getInstance().getRealPath(dir);
		File files = new File(realPath);
		if (!files.exists()) {
			if (!files.mkdirs())
				throw new OBPMValidateException("Folder create failure");
		}
		StringBuffer options = new StringBuffer();
		for (File file:files.listFiles()) {
			//若文件为目录，则跳过。
			if(file.isDirectory())
				continue;
			String fileName = file.getName();
			String filePath = dir + fileName;
			options.append("<option value=\"" + filePath + "\" title=\"" + fileName + "\">" + fileName
					+ "</option>\n");
		}
		//------------------与旧数据兼容代码段结束-----------------------
		
		//2016.8.24---新增各企业域目录下的word文件
		String domainDir = dir + domainid + "/";
		String domainRealPath = Environment.getInstance().getRealPath(domainDir);
		File domainFiles = new File(domainRealPath);
		if (!domainFiles.exists()) {
			if (!domainFiles.mkdirs())
				throw new OBPMValidateException("Folder create failure");
		}
		for (File domainFile:domainFiles.listFiles()) {
			//若文件为目录，则跳过。
			if(domainFile.isDirectory())
				continue;
			String fileName = domainFile.getName();
			String filePath = domainDir + fileName;
			options.append("<option value=\"" + filePath + "\" title=\"" + fileName + "\">" + fileName
					+ "</option>\n");
		}
		
		return options.toString();
	}
}
