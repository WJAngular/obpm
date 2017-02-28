package cn.myapps.core.email.attachment.action;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.BaseAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.ejb.IDesignTimeProcess;
import cn.myapps.constans.Environment;
import cn.myapps.core.email.attachment.ejb.Attachment;
import cn.myapps.core.email.attachment.ejb.AttachmentProcess;
import cn.myapps.core.email.email.action.EmailHelper;
import cn.myapps.core.email.folder.ejb.EmailFolder;
import cn.myapps.core.email.folder.ejb.EmailFolderProcess;
import cn.myapps.core.email.util.EmailConfig;
import cn.myapps.core.email.util.EmailProcessUtil;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.http.ResponseUtil;


public class AttachmentAction extends BaseAction<Attachment> {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(AttachmentAction.class);
	
	public static final String ATTACHMENT_DIR = EmailConfig.getString("attachment.dir", "/attachment");
	public static final long ATTACHMENT_MAX_SIZE = EmailConfig.getInteger("attachment.max.size", 50 * 1000 * 1024); // 50M
	public static final long ATTACHMENT_MAX_COUNT = EmailConfig.getInteger("attachment.max.count", 3); // 5
	
	/**
	 * 默认构造方法
	 * @SuppressWarnings 工厂方法不支持泛型
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public AttachmentAction() throws Exception {
		super(ProcessFactory.createProcess(AttachmentProcess.class), new Attachment());
	}
	
	@Override
	public String doSave() {
		return super.doSave();
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
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		ResponseUtil.setTextToResponse(response, text);
		return null;
	}
	
	public String doDownload() {
		try {
			setCurrentAttachmentProcess();
			ParamsTable params = getParams();
			String folderid = params.getParameterAsString("folderid");
			EmailFolderProcess folderProcess = getEmailFolderProcess();
			EmailFolder folder = folderProcess.getEmailFolderById(folderid);
			String emailid = params.getParameterAsString("emailid");
			String fileName = params.getParameterAsString("filename");
			//fileName = fileName != null ? URLEncoder.encode(fileName, "ISO-8859-1") : null;
			if (folder != null && !StringUtil.isBlank(emailid)
					&& !StringUtil.isBlank(fileName)) {
				//fileName = URLDecoder.decode(fileName, "UTF-8");
				EmailHelper helper = new EmailHelper();
				fileName = helper.decoder(fileName);
				Attachment attachment = ((AttachmentProcess)process).getAttachment(emailid, folder, fileName);
				if (attachment != null) {
					HttpServletResponse response = ServletActionContext.getResponse();
					String encoding = EmailConfig.getString("charset", "UTF-8");
					// application/octet-stream  application/x-download
					response.setContentType("application/octet-stream; charset=" + encoding);
					response.setHeader("Content-Disposition", "attachment;filename=\"" + URLEncoder.encode(fileName, encoding) + "\"");
					if (attachment.getContent() instanceof ByteArrayOutputStream) {
						ByteArrayOutputStream bos = (ByteArrayOutputStream) attachment.getContent();
						bos.writeTo(response.getOutputStream());
					} else {
						File file = new File(attachment.getFileAllPath());
						if (file.exists()) {
							setDownloadFile(response, file);
						} else {
							throw new OBPMValidateException(getMultiLanguage("core.email.attachment.nofound"));
						}
					}
					return null;
				}
			}
		} catch (OBPMValidateException e) {
			log.warn(e);
			addFieldError("", e.getValidateMessage());
		}catch (Exception e) {
			log.warn(e);
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		}
		ServletActionContext.getRequest().setAttribute(ERROR, getMultiLanguage("core.email.attachment.dowonload.error"));
		return ERROR;
	}
	
	private void setDownloadFile(HttpServletResponse response, File file) throws Exception {
		OutputStream os = null;
		BufferedInputStream reader = null;
		try {
			String encoding = Environment.getInstance().getEncoding();
			response.setContentType("application/x-download; charset=" + encoding + "");
			response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(file.getName(), encoding));
			os = response.getOutputStream();
			reader = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[4096];
			int i = -1;
			while ((i = reader.read(buffer)) != -1) {
				os.write(buffer, 0, i);
			}
			os.flush();
		} catch (IOException e) {
			// maybe user cancelled download
			//throw new Exception(getMultiLanguage("core.email.attachment.dowonload.error"));
			this.setRuntimeException(new OBPMRuntimeException("{*[OBPMRuntimeException]*}",e));
			e.printStackTrace();
		} finally {
			if (os != null) {
				reader.close();
			}
			if ( reader != null) {
				reader.close();
			}
		}
	}
	
	/**
	 * 设置当前业务处理Bean
	 * @SuppressWarnings 工厂方法不支持泛型
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void setCurrentAttachmentProcess() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		this.process = (IDesignTimeProcess<Attachment>) EmailProcessUtil.createProcess(AttachmentProcess.class, request);
	}
	
	private EmailFolderProcess getEmailFolderProcess() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		return (EmailFolderProcess) EmailProcessUtil.createProcess(EmailFolderProcess.class, request);
	}
	
	public String doUpload() {
		
		return SUCCESS;
	}
	
	public String doViewWordFile() {
		
		return SUCCESS;
	}
	
}
