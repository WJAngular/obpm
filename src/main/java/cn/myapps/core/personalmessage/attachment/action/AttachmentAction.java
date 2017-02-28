package cn.myapps.core.personalmessage.attachment.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.BaseAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.constans.Environment;

import cn.myapps.core.personalmessage.action.PersonalMessageHelper;
import cn.myapps.core.personalmessage.attachment.ejb.PM_AttachmentVO;
import cn.myapps.core.personalmessage.attachment.ejb.AttachmentProcess;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.http.ResponseUtil;

public class AttachmentAction extends BaseAction<PM_AttachmentVO> {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(AttachmentAction.class);
	
	
	/**
	 * 默认构造方法
	 * @SuppressWarnings 工厂方法不支持泛型
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public AttachmentAction() throws Exception {
		super(ProcessFactory.createProcess(AttachmentProcess.class), new PM_AttachmentVO());
	}
	
	
	@Override
	public String doDelete() {
		HttpServletResponse response = ServletActionContext.getResponse();
		String text = SUCCESS;
		try {
			ParamsTable params = getParams();
			String id = params.getParameterAsString("id");
			//String emailid = params.getParameterAsString("emailid");
			if (StringUtil.isBlank(id)) {
				text = ERROR;
			} else {
				AttachmentProcess aProcess = (AttachmentProcess) ProcessFactory.createProcess(AttachmentProcess.class);
				aProcess.doRemove(id);
			}
		} catch (OBPMValidateException e) {
			log.warn(e);
			text = ERROR;
		}catch (Exception e) {
			log.warn(e);
			text = ERROR;
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		ResponseUtil.setTextToResponse(response, text);
		return null;
	}
	

	public String doDownload(){
		try {
			String attachmentid = ServletActionContext.getRequest().getParameter("attachmentid");
			PersonalMessageHelper pmh = new PersonalMessageHelper();
			File downFile = new File(pmh.findAttachmentById(attachmentid).getPath());
			if(downFile.exists()){
				setResponse(downFile);
				return null;
			}else{
				log.warn("{*[core.email.attachment.nofound]*}");
			}
		}  catch (OBPMValidateException e) {
			addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		ServletActionContext.getRequest().setAttribute(ERROR, getMultiLanguage("core.email.attachment.dowonload.error"));
		return null;
	}
	
	private void setResponse(File file) throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		String encoding = Environment.getInstance().getEncoding();
		response.setContentType("application/x-msdownload; charset=" + encoding
				+ "");
		response.setHeader("Content-Disposition", "attachment;filename=\""
				+ java.net.URLEncoder.encode(file.getName(), encoding) + "\"");
		OutputStream os = response.getOutputStream();

		BufferedInputStream reader = new BufferedInputStream(
				new FileInputStream(file));
		byte[] buffer = new byte[4096];
		int i = -1;
		while ((i = reader.read(buffer)) != -1) {
			os.write(buffer, 0, i);
		}
		if(os != null){
			os.flush();
			os.close();
		}
		if(reader != null){
			reader.close();
		}
	}

	
}
