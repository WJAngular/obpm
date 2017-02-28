package cn.myapps.core.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.AbstractRunTimeAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.ejb.IRunTimeProcess;
import cn.myapps.constans.Environment;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.superuser.ejb.SuperUserProcess;
import cn.myapps.core.superuser.ejb.SuperUserVO;
import cn.myapps.core.upload.ejb.UploadInfo;
import cn.myapps.core.upload.ejb.UploadProcess;
import cn.myapps.core.upload.ejb.UploadProcessBean;
import cn.myapps.core.upload.ejb.UploadVO;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.property.DefaultProperty;
import cn.myapps.util.sequence.Sequence;
import flex.messaging.util.URLDecoder;

//public class UploadAction extends ActionSupport implements Action {
public class UploadAction extends AbstractRunTimeAction<UploadVO> {

	private String applicationid;

	private static final long serialVersionUID = 6865609097678926341L;

	private static final Logger Log = Logger.getLogger(UploadAction.class);

	private File upload;

	private String uploadContentType;

	private String uploadFileName;

	private String path;

	private String id;

	private String viewid;

	private String uploadList_;

	private String fieldValue;

	private String newUploadFileName;

	private String webPath;

	private String allowedTypes;

	private String[] fileName;

	private double perc;

	private String layer;

	private String fileSaveMode = UploadInfo.FILE_SAVE_MODE_SYSTEM;

	private File[] file;

	private String fileFullName;

	public String getFileFullName() {
		return fileFullName;
	}

	public void setFileFullName(String fileFullName) {
		this.fileFullName = fileFullName;
	}

	public String getUploadList_() {
		return uploadList_;
	}

	public void setUploadList_(String uploadList_) {
		this.uploadList_ = uploadList_;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	private int maximumSize = Integer.MAX_VALUE;

	private static final String IMAGE_TYPES[] = { "image/png", "image/gif",
			"image/jpeg", "image/pjpeg", "image/bmp", "application/pdf" };

	public double getPerc() {
		return perc;
	}

	public void setPerc(double perc) {
		this.perc = perc;
	}

	public String getLayer() {
		return layer;
	}

	public void setLayer(String layer) {
		this.layer = layer;
	}

	public String getViewid() {
		return viewid;
	}

	public void setViewid(String viewid) {
		this.viewid = viewid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getMaximumSize() {
		return maximumSize;
	}

	public void setMaximumSize(int maximumSize) {
		this.maximumSize = maximumSize;
	}

	public String getAllowedTypes() {
		return allowedTypes;
	}

	public void setAllowedTypes(String allowedTypes) {
		this.allowedTypes = allowedTypes;
	}

	public String getWebPath() {
		return webPath;
	}

	public void setWebPath(String webPath) {
		this.webPath = webPath;
	}

	public String getPath() throws Exception {
		return this.path;
	}

	public void setPath(String path) throws Exception {
		this.path = path;
	}

	public String getNewUploadFileName() {
		return newUploadFileName;
	}

	public void setNewUploadFileName(String newUploadFileName) {
		this.newUploadFileName = newUploadFileName;
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public File[] getFile() {
		return file;
	}

	public void setFile(File[] file) {
		this.file = file;
	}

	public void setFileName(String[] fileName) {
		this.fileName = fileName;
	}

	public String[] getFileName() {
		return fileName;
	}

	public UploadInfo getUploadInfo() {
		// 设置属性
		UploadInfo uploadInfo = new UploadInfo();
		uploadInfo.setAllowedTypes(getAllowedTypes());
		uploadInfo.setContentType(getUploadContentType());
		uploadInfo.setFileName(getUploadFileName());
		uploadInfo.setFileSaveMode(getFileSaveMode());
		uploadInfo.setMaximumSize(getMaximumSize());
		try {
			uploadInfo.setPath(getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return uploadInfo;
	}

	public String getFileSaveMode() {
		return fileSaveMode;
	}

	public void setFileSaveMode(String fileSaveMode) {
		this.fileSaveMode = fileSaveMode;
	}

	public IRunTimeProcess<UploadVO> getProcess() {
		return new UploadProcessBean(applicationid);
	}

	public String getApplicationid() {
		return applicationid;
	}

	public void setApplicationid(String applicationid) {
		this.applicationid = applicationid;
	}

	/**
	 * 上传文件校验 1.校验上传文件是否存在 2.校验上传文件类型是否合法 3.校验上传文件大小是否合法
	 */
	public void validate() {
		Map<?, ?> map = ActionContext.getContext().getParameters();
		file = (File[]) map.get("EDITFILE");
		String[] wordForms = (String[]) map.get("EDITFILEFileName");
		// 通过是上传什么文件word不做验证
		if (file == null && wordForms == null) {
			uploadfileValidate();
		}
	}

	public void uploadfileValidate() {
		if (upload == null) {
			addFieldError("", "{*[upload.file.empty]*}");
		} else {
			if (!isAllowedType()) {
				addFieldError("",
						"{*[core.upload.notallow]*}GIF/PNG/JPG/BMP/PDF");
			}

			if (isTooLarge(upload)) {
				addFieldError("", "{*[core.upload.toolarge]*}:\""
						+ uploadFileName + "\",{*[MaximumSize]*}("
						+ (getMaximumSize() / 1024) + "KB)");
			}
		}
	}

	private boolean isAllowedType() {
		if (!StringUtil.isBlank(allowedTypes)) {
			if (allowedTypes.equalsIgnoreCase("image")) {
				for (int i = 0; i < IMAGE_TYPES.length; i++) {
					// String type = getUploadContentType();
					if (uploadContentType.equalsIgnoreCase(IMAGE_TYPES[i])) {
						return true;
					}
				}
			}
		} else {
			return true;
		}

		return false;
	}

	private boolean isTooLarge(File file) {
		if (file.length() > getMaximumSize()) {
			return true;
		}
		return false;
	}

	public String execute() throws Exception {

		return NONE;
	}

	public String getPercs() throws Exception {
		try {
			HttpSession session = ServletActionContext.getRequest()
					.getSession();
			HttpServletResponse response = ServletActionContext.getResponse();
			PrintWriter writer = null;
			writer = response.getWriter();
			int perc = ((Integer) session.getAttribute("perc")) != null ? ((Integer) session
					.getAttribute("perc")).intValue() : 0;
			writer.println(perc);
			if (perc == 100) {
				session.setAttribute("perc", Integer.valueOf(0));
			}
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}

	public String clearPerc() {
		HttpSession session = ServletActionContext.getRequest().getSession();
		session.setAttribute("perc", Integer.valueOf(0));

		return SUCCESS;
	}

	/**
	 * 删除列表中所有文件
	 * 
	 * @return
	 * @throws Exception
	 */
	public String doDelete() {
		HttpServletRequest request = ServletActionContext.getRequest();
		ParamsTable params = ParamsTable.convertHTTP(request);
		String docid = params.getParameterAsString("_docid");
		try {
			if (!StringUtil.isBlank(fileFullName)) {
				fileFullName = URLDecoder.decode(fileFullName, "UTF-8");
				UploadProcess uploadProcess = (UploadProcess) ProcessFactory
						.createRuntimeProcess(UploadProcess.class,
								this.applicationid);
				String[] fileFullNameArry = fileFullName.split(";");
				for (int i = 0; i < fileFullNameArry.length; i++) {
					String fileFullName = fileFullNameArry[i];
					UploadVO uploadVO = null;
					if (!StringUtil.isBlank(fileFullName)
							&& fileFullName.indexOf("_/uploads") > 0) {
						String fileId = fileFullName.substring(0,
								fileFullName.indexOf("_/uploads"));
						uploadVO = (UploadVO) uploadProcess.doView(fileId);
					} else {
						uploadVO = (UploadVO) uploadProcess.findByColumnName1(
								"PATH", fileFullName);
					}

					if (uploadVO != null) {
						String fileRealPath = getEnvironment().getRealPath(
								uploadVO.getPath());
						File file = new File(fileRealPath);
						if (file.exists()) {
							if (!file.delete()) {
								Log.warn("File(" + fileRealPath
										+ ") delete failed");
								throw new OBPMValidateException("File("
										+ fileRealPath + ") delete failed");
							}
						}
						uploadProcess.doRemove(uploadVO.getId());

						// 删除上传文件时，更新文档
						if (!StringUtil.isBlank(applicationid)) {
							DocumentProcess docProcess = (DocumentProcess) ProcessFactory
									.createRuntimeProcess(
											DocumentProcess.class,
											applicationid);
							if (!StringUtil.isBlank(docid)) {
								Document doc = (Document) docProcess
										.doView(docid);
								if (doc != null) {
									doc = rebuildDocument(doc, params);
									docProcess.doUpdate(doc, false, false);
								}
							}
						}

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			addFieldError("", e.getMessage());
			return NONE;
		}
		return NONE;
	}

	/**
	 * 删除一个文件
	 * 
	 * @return
	 * @throws Exception
	 */
	public String doDeleteOne() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		ParamsTable params = ParamsTable.convertHTTP(request);
		String docid = params.getParameterAsString("_docid");
		if (!StringUtil.isBlank(fileFullName)) {
			fileFullName = URLDecoder.decode(fileFullName, "UTF-8");
			UploadProcess uploadProcess = (UploadProcess) ProcessFactory
					.createRuntimeProcess(UploadProcess.class,
							this.applicationid);
			UploadVO uploadVO = null;
			if (!StringUtil.isBlank(fileFullName)
					&& fileFullName.indexOf("_/uploads") > 0) {
				String fileId = fileFullName.substring(0,
						fileFullName.indexOf("_/uploads"));
				uploadVO = (UploadVO) uploadProcess.doView(fileId);
			} else {
				uploadVO = (UploadVO) uploadProcess.findByColumnName1("PATH",
						fileFullName);
			}
			if (uploadVO != null) {
				String fileRealPath = getEnvironment().getRealPath(
						uploadVO.getPath());
				File file = new File(fileRealPath);
				if (file.exists()) {
					if (!file.delete()) {
						Log.warn("File(" + fileRealPath + ") delete failed");
						throw new OBPMValidateException("File(" + fileRealPath
								+ ") delete failed");
					}
				}
				uploadProcess.doRemove(uploadVO.getId());

				// 删除上传文件时，更新文档
				if (!StringUtil.isBlank(applicationid)) {
					DocumentProcess docProcess = (DocumentProcess) ProcessFactory
							.createRuntimeProcess(DocumentProcess.class,
									applicationid);
					if (!StringUtil.isBlank(docid)) {
						Document doc = (Document) docProcess.doView(docid);
						if (doc != null) {
							doc = rebuildDocument(doc, params);
							docProcess.doUpdate(doc, false, false);
						}
					}
				}

			}
		}
		return NONE;
	}

	/**
	 * 删除一个文件
	 * 
	 * @return
	 * @throws Exception
	 */
	public String doFileManagerDelete() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		ParamsTable params = ParamsTable.convertHTTP(request);
		String docid = params.getParameterAsString("_docid");
		// 删除上传文件时，更新文档
		if (!StringUtil.isBlank(applicationid)) {
			DocumentProcess docProcess = (DocumentProcess) ProcessFactory
					.createRuntimeProcess(DocumentProcess.class, applicationid);
			if (!StringUtil.isBlank(docid)) {
				Document doc = (Document) docProcess.doView(docid);
				if (doc != null) {
					doc = rebuildDocument(doc, params);
					docProcess.doUpdate(doc, false, false);
				}
			}
		}
		return NONE;
	}

	/**
	 * 获得文件信息
	 * 
	 * @return
	 */
	public String doFileInfor() {
		if (!StringUtil.isBlank(fileFullName)) {
			try {
				fileFullName = URLDecoder.decode(fileFullName, "UTF-8");
				UploadProcess uploadProcess = (UploadProcess) ProcessFactory
						.createRuntimeProcess(UploadProcess.class,
								this.applicationid);
				SuperUserProcess superUserProcess = (SuperUserProcess) ProcessFactory
						.createProcess(SuperUserProcess.class);
				UserProcess userProcess = (UserProcess) ProcessFactory
						.createProcess(UserProcess.class);
				UploadVO uploadVO = (UploadVO) uploadProcess.findByColumnName1(
						"PATH", fileFullName);
				if (uploadVO == null) {
					uploadVO = (UploadVO) uploadProcess.findByColumnName1("ID",
							fileFullName);
				}

				if (uploadVO != null) {
					ActionContext ctx = ActionContext.getContext();
					HttpServletResponse response = (HttpServletResponse) ctx
							.get(ServletActionContext.HTTP_RESPONSE);
					StringBuffer sb = new StringBuffer();
					if (uploadVO.getSize() != 0) {
						double size = 0;
						java.text.DecimalFormat format = new DecimalFormat(
								"##.##");
						if (uploadVO.getSize() < 1024) {
							size = uploadVO.getSize();
							sb.append("{*[Size]*}:").append(size)
									.append(" B; ");
						} else if (uploadVO.getSize() < (1024 * 1024)
								&& uploadVO.getSize() >= 1024) {
							size = (double) (uploadVO.getSize() / 1024);
							sb.append("{*[Size]*}:")
									.append(format.format(size))
									.append(" KB; ");
						} else {
							size = (double) (uploadVO.getSize() / (1024f * 1024));
							sb.append("{*[Size]*}:")
									.append(format.format(size)).append(" M; ");
						}
					}
					if (uploadVO.getType() != null) {
						sb.append("{*[Type]*}:").append(uploadVO.getType())
								.append("; ");
					}
					if (uploadVO.getModifyDate() != null) {
						sb.append("{*[Upload]*}{*[Date]*}:")
								.append(uploadVO.getModifyDate()).append("; ");
					}
					if (uploadVO.getUserid() != null) {
						if (superUserProcess.doView(uploadVO.getUserid()) != null) {
							sb.append("{*[Upload]*}{*[Personal]*}:")
									.append(((SuperUserVO) superUserProcess
											.doView(uploadVO.getUserid()))
											.getName()).append("; ");
						} else if (userProcess.doView(uploadVO.getUserid()) != null) {
							sb.append("{*[Upload]*}{*[Personal]*}:")
									.append(((UserVO) userProcess
											.doView(uploadVO.getUserid()))
											.getName()).append("; ");
						}
					}
					response.setContentType("text/html;charset=UTF-8");
					response.getWriter().print(sb.toString());
					response.getWriter().close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				return NONE;
			}
		}
		return NONE;
	}

	/**
	 * 删除数据库中的图片
	 * 
	 * @return
	 * @throws Exception
	 */
	public String deleteToDataBaseFile() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		ParamsTable params = ParamsTable.convertHTTP(request);
		String docid = params.getParameterAsString("_docid");
		String applicationid = params.getParameterAsString("applicationid");
		// ParamsTable params = this.getParams();
		if (!StringUtil.isBlank(fileFullName)) {
			fileFullName = URLDecoder.decode(fileFullName, "UTF-8");
			UploadProcess uploadProcess = (UploadProcess) ProcessFactory
					.createRuntimeProcess(UploadProcess.class,
							this.applicationid);
			try {
				uploadProcess.beginTransaction();
				if (fileFullName != null && !fileFullName.equals("")) {
					String[] fileFullNameArray = fileFullName.split(";");
					if (fileFullNameArray.length > 0) {
						for (int i = 0; i < fileFullNameArray.length; i++) {
							uploadProcess.doRemove(fileFullNameArray[i]
									.split("_")[0]);
						}
					} else {
						uploadProcess.doRemove(fileFullName.split("_")[0]);
					}
				}

				// 删除上传文件时，更新文档
				if (!StringUtil.isBlank(applicationid)) {
					DocumentProcess docProcess = (DocumentProcess) ProcessFactory
							.createRuntimeProcess(DocumentProcess.class,
									applicationid);
					if (!StringUtil.isBlank(docid)) {
						Document doc = (Document) docProcess.doView(docid);
						if (doc != null) {
							doc = rebuildDocument(doc, params);
							docProcess.doUpdate(doc, false, false);
						}
					}
				}
				uploadProcess.commitTransaction();
			} catch (Exception e) {
				uploadProcess.rollbackTransaction();
				e.printStackTrace();
			}
		}
		return NONE;
	}

	/**
	 * 重新构建文档
	 * 
	 * @param doc
	 * @param params
	 * @return
	 */
	protected Document rebuildDocument(Document doc, ParamsTable params) {
		String formid = params.getParameterAsString("_formid");
		try {
			if (!StringUtil.isBlank(doc.getId()) && !StringUtil.isBlank(formid)) {
				FormProcess formPross = (FormProcess) ProcessFactory
						.createProcess(FormProcess.class);
				Form form = (Form) formPross.doView(formid);
				doc = form.createDocument(doc, params, getUser());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}

	public Environment getEnvironment() {
		Environment evt = (Environment) ActionContext.getContext()
				.getApplication().get(Environment.class.getName());

		return evt;
	}

	public String doUploadFile() {
		ParamsTable params = this.getParams();
		int type = Integer.parseInt(params.getParameterAsString("type"));
		String[] path = { "SECSIGN_PATH", "REDHEAD_DOCPATH", "TEMPLATE_DOCPATH" };
		String[] extensions = { "esp", "doc,docx", "doc.docx" };
		Boolean isse = params.getParameterAsBoolean("isse");

		try {
			String dir = DefaultProperty.getProperty(path[type]) + getUser().getDomainid() + "/";
			String savePath = getEnvironment().getRealPath(dir);
			File file = new File(savePath);
			if(!file.exists()){
				if (!file.mkdirs())
					throw new OBPMValidateException("Folder create failure");
			}
			File uploadFile = null;

			if (isse) {
				uploadFile = new File(savePath
						+ params.getParameterAsString("filename"));
			} else {
				String extension = uploadFileName.substring(uploadFileName
						.lastIndexOf(".") + 1);
				if (extensions[type].indexOf(extension.toLowerCase()) != -1) {
					uploadFile = new File(savePath + uploadFileName);
				} else {
					this.addActionMessage("{*[core.upload.invalid_File_Format]*}!");
					return INPUT;
				}
			}

			if (uploadFile.exists()) {
				if (!uploadFile.delete()) {
					throw new OBPMValidateException("旧文件删除失败!");
				}
			}

			if (upload == null) {
				throw new OBPMValidateException("文件大小不合法！");
			}

			if (!upload.renameTo(uploadFile)) {
				throw new OBPMValidateException("文件保存失败!");
			}

			if (isse) {
				this.addActionMessage("保存成功");
				return "saveSignSuccess";
			}
			this.addActionMessage("{*[Upload_File]*}{*[Success]*}!");
			return SUCCESS;

		} catch (Exception e) {
			this.addActionMessage("{*[Upload_File]*}{*[Fail]*}!");
			e.printStackTrace();
			return INPUT;
		}
	}

	public String dosaveWord() {
		FileInputStream input = null;
		FileOutputStream outputStream = null;
		ParamsTable params = getParams();
		try {
			String[] wordForms = (String[]) ActionContext.getContext()
					.getParameters().get("EDITFILEFileName");// 默认文件名
			String filename = wordForms[0];
			File[] files = getFile();
			String dir = DefaultProperty.getProperty("WEB_DOCPATH");
			String _path = params.getParameterAsString("_path");
			if (!StringUtil.isBlank(_path)) {
				String contentPath = ServletActionContext.getRequest()
						.getContextPath();
				if (_path.startsWith(contentPath)) {
					dir = _path.substring(contentPath.length(),
							_path.lastIndexOf("/") + 1);
				}
			}
			StringBuffer savePath = new StringBuffer(getEnvironment()
					.getRealPath(dir));

			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					if (filename == null) {
						filename = Sequence.getSequence();
					}

					String suffix = "";
					if (!(new File(savePath.toString()).isDirectory())) {
						if (!new File(savePath.toString()).mkdirs()) {
							Log.warn("Failed to create folder ("
									+ savePath.toString() + ")");
							throw new OBPMValidateException(
									"Failed to create folder ("
											+ savePath.toString() + ")");
						}
					}
					input = new FileInputStream(file);
					File docfile = new File(savePath + filename + suffix);
					if (docfile.exists()) {
						// File bak = new File(savePath + filename + "_bak"
						// + suffix);
						// if (bak.exists())
						// forceDelete(bak);
						forceDelete(docfile);
					}
					savePath.append(filename).append(suffix);
					outputStream = new FileOutputStream(savePath.toString(),
							true);

					byte[] buffer = new byte[1024];
					int len;
					while ((len = input.read(buffer)) > 0) {
						outputStream.write(buffer, 0, len);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (input != null)
					input.close();
				if (outputStream != null)
					outputStream.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}

	public boolean forceDelete(File f) {
		boolean result = false;
		int tryCount = 0;
		while (!result && tryCount++ < 10) {
			// System.gc();
			result = f.delete();
		}
		return result;
	}

	public boolean forceRename(File f, File newFile) {
		boolean result = false;
		int tryCount = 0;
		while (!result && tryCount++ < 10) {
			// System.gc();
			result = f.renameTo(newFile);
		}
		return result;
	}
}
