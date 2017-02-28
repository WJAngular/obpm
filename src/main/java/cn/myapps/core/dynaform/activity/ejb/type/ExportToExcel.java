package cn.myapps.core.dynaform.activity.ejb.type;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.action.AbstractRunTimeAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.constans.Environment;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityParent;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.property.DefaultProperty;

public class ExportToExcel extends ActivityType {

	public ExportToExcel(Activity act) {
		super(act);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6577567526874137106L;

	public String getOnClickFunction() {
		String filename = act.getExcelName();
		
		if(StringUtil.isBlank(filename)){
			try {
				if(act.getParent() instanceof View){
					View view = (View) act.getParent();
					filename = StringUtil.isBlank(view.getDescription())?view.getName():view.getDescription();
				}else{
					filename = StringUtil.isBlank(act.getParent().getName())?act.getId():act.getParent().getName();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
		boolean expSub = act.isExpSub();
		//return "ev_expToExl('" + act.getId() + "', '" + filename + "', " + expSub + ")";
		return "Activity.doExecute('"+act.getId()+"',"+act.getType()+",{fileName:'"+filename+"',expSub:"+act.isExpSub()+",target:this})";
		
	}

	public String getDefaultClass() {
		return VIEW_BUTTON_CLASS;
	}

	public String getButtonId() {
		return VIEW_BUTTON_ID;
	}

	public String getAfterAction() {
		//return VIEW_NAMESPACE + "/displayView.action";
		return null;
	}

	public String getBackAction() {
		//return VIEW_NAMESPACE + "/displayView.action";
		return null;
	}

	public String getBeforeAction() {
		//return act.getExcelName() != null ? VIEW_NAMESPACE + "/expDocToExcel.action?filename=" + act.getExcelName() + "&isExpSub=" + act.isExpSub()  : VIEW_NAMESPACE + "/expDocToExcel.action?filename=" + act.getId() + "&isExpSub=" + act.isExpSub();
		return null;
	}

	public String getDefaultOnClass() {
		return DOCUMENT_BUTTON_ON_CLASS;
	}
	
	public String doExecute(AbstractRunTimeAction action, Document doc,WebUser user, ParamsTable params) throws Exception {
		return null;
	}
	
	
	public String doProcess(AbstractRunTimeAction action, Document doc,
			WebUser user, ParamsTable params) throws Exception {
		try {
			HttpServletRequest request = action.getRequest();
			HttpServletResponse response = action.getResponse();
			
			ParamsTable parasm = ParamsTable.convertHTTP(request);
			String viewid = parasm.getParameterAsString("_viewid");
			ViewProcess process = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
			View view = (View) process.doView(viewid);
			changeOrderBy(parasm, view);
			
			String fileName = process.expDocToExcel(viewid, user, parasm);
			
			String webPath = DefaultProperty.getProperty("REPORT_PATH");
			String fileWebPath = webPath + "/" + fileName;
			doFileDownload(request, response, fileWebPath);
			
			//Excel导出执行运行后脚本
			 
			String _activityid = parasm.getParameterAsString("_activityid");
			if (!StringUtil.isBlank(viewid)) {
				ViewProcess viewProcess = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
				ActivityParent actParent = (ActivityParent) viewProcess.doView(viewid);
				if (actParent != null && _activityid != null && _activityid.trim().length() > 0) {
					Activity act = actParent.findActivity(_activityid);
					
					// 运行后脚本
					if (!StringUtil.isBlank(act.getAfterActionScript())) {
						IRunner runner = JavaScriptFactory.getInstance(parasm.getSessionid(), actParent.getApplicationid()); 
						runner.initBSFManager(new Document(), parasm, user, new java.util.ArrayList<ValidateMessage>());
						StringBuffer label = new StringBuffer();
						label.append("Activity Action(").append(act.getId()).append(")." + act.getName()).append("afterActionScript");
						runner.run(label.toString(), act.getAfterActionScript());
					}
				}
			}
		} catch (Exception e) {
			action.setRuntimeException(new OBPMRuntimeException(e.getMessage(),e));
			e.printStackTrace();
		}
		return null;
	}
	
	private void changeOrderBy(ParamsTable params, View view) {
		if (params.getParameter("_sortCol") == null || params.getParameter("_sortCol").equals("")) {
			setOrder(params, view.getDefaultOrderFieldArr());
		} else {
			String[] colFields = new String[1];
			String fieldName = view.getFormFieldNameByColsName(params.getParameterAsString("_sortCol"));
			String sortStatus = params.getParameterAsString("_sortStatus");
			colFields[0] = fieldName + " " + sortStatus;
			setOrder(params, colFields);
		}
	}
	
	private void setOrder(ParamsTable params, String[] orderFields) {
		params.setParameter("_sortCol", orderFields);
	}
	
	/**
	 * 文件下载response设置
	 * 
	 * @return
	 * @throws IOException
	 */
	private void doFileDownload(HttpServletRequest request, HttpServletResponse response, String fileName) throws IOException {
		// String fileName = (String)ServletActionContext.getRequest().getAttribute("downloadFileName");

		Environment env = Environment.getInstance();
		String realFileName = env.getRealPath(fileName);
		File file = new File(realFileName);
		if (file.exists()) {
			try {
				setResponse(request, response, file);
			} catch (IOException e) {
				throw e;
			}
		}
	}
	
	/**
	 * 设置响应头
	 * 
	 * @param file
	 *            文件
	 * @throws IOException
	 */
	private void setResponse(HttpServletRequest request, HttpServletResponse response, File file) throws IOException {
		OutputStream os = null;
		BufferedInputStream reader = null;
		try {
			String encoding = Environment.getInstance().getEncoding();
			String agent = request.getHeader("USER-AGENT");
			if(null != agent && -1 != agent.indexOf("Firefox")){
				response.setContentType("application/x-download; charset=" + encoding + "");
				response.setHeader("Content-Disposition", "attachment;filename=\"" + MimeUtility.encodeText(file.getName(), encoding, "B") + "\"");
			}else{
				response.setContentType("application/x-download; charset=" + encoding + "");
				response.setHeader("Content-Disposition", "attachment;filename=\"" + java.net.URLEncoder.encode(file.getName(), encoding) + "\"");
			}
			os = response.getOutputStream();

			reader = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[4096];
			int i = -1;
			while ((i = reader.read(buffer)) != -1) {
				os.write(buffer, 0, i);
			}
			os.flush();
		} catch (IOException e) {
			throw e;
		} finally {
			if (os != null) {
				reader.close();
			}
			if ( reader != null) {
				reader.close();
			}
		}
	}

	@Override
	public ValueObject doMbExecte(WebUser user, ParamsTable params)
			throws Exception {
		return null;
	}

}
