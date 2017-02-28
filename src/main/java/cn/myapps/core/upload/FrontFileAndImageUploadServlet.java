package cn.myapps.core.upload;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.upload.ejb.UploadInfo;
import cn.myapps.core.upload.ejb.UploadProcess;
import cn.myapps.core.upload.ejb.UploadVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.converter.ConverterBundle;
import cn.myapps.util.converter.ConverterRunner;

public class FrontFileAndImageUploadServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8751596990930951691L;
	private static final Logger Log = Logger
			.getLogger(FrontFileAndImageUploadServlet.class);

	private String path;// 上传文件保存的路径
	private String fileSaveMode;// 文件保存模式
	private String fieldid;// 表单字段
	private String allowedTypes;// 允许上传的类型
	private String applicationid;// 上传限制大小
	private String uploadContentType;// 上传文件类型
	private File saveFile;// 上传时生成文件
	private String uploadFileName;// 上传文件的名称
	private String uuid = "";// 保存到数据库中编号
	private UploadInfo uploadInfo1;
	String[] excludeTypes = new String[] {};// 系统定义的限制上传文件类型

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String _excludeTypes = config.getInitParameter("excludeType");
		if (!StringUtil.isBlank(_excludeTypes)) {
			excludeTypes = _excludeTypes.split("\\|");
		}
	}

	/**
	 * 文件上传请求调用的方法
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws Exception
	 */
	public void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			// 获取upload.jsp传来参数
			String data = request.getParameter("data");
			// 分割分别获得对应数据
			String[] dataArry = data.split(",");

			// 赋值相应参数
			path = dataArry[0].split(":")[1].equals("null") ? "" : dataArry[0]
					.split(":")[1];
			fileSaveMode = dataArry[1].split(":")[1].equals("null") ? ""
					: dataArry[1].split(":")[1];
			fieldid = dataArry[2].split(":")[1].equals("null") ? ""
					: dataArry[2].split(":")[1];
			allowedTypes = dataArry[3].split(":")[1].equals("null") ? ""
					: dataArry[3].split(":")[1];
			applicationid = dataArry[4].split(":")[1].equals("null") ? ""
					: dataArry[4].split(":")[1];
			String userid = ((WebUser) request.getSession().getAttribute(
					"FRONT_USER")) != null ? ((WebUser) request.getSession()
					.getAttribute("FRONT_USER")).getId() : null;

			if (path.indexOf("/") != -1) {
				path = path.substring(1);
			}
			String actionType = request.getParameter("actionType");
			// 获得文件保存的真实路径
			String savePath = this.getServletConfig().getServletContext()
					.getRealPath("");
			// 拼接好文件要保存的真实文件夹
			savePath += getUploadInfo().getFileDir();

			// 加上以当前年份命名的文件夹
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			savePath += File.separator + year;

			// 生成文件
			File f1 = new File(savePath);
			// 如果该文件夹不存在则创建
			if (!f1.exists()) {
				if (!f1.mkdirs()) {
					Log.warn("Failed to create folder (" + savePath + ")");
					throw new IOException("Failed to create folder ("
							+ savePath + ")");
				}
			}
			// 磁盘文件个项工厂
			DiskFileItemFactory fac = new DiskFileItemFactory();
			// servlet文件上传
			ServletFileUpload upload = new ServletFileUpload(fac);
			// 设置上传编码
			upload.setHeaderEncoding("utf-8");
			List<?> fileList = null;// 文件列表
			try {
				request.setCharacterEncoding("UTF-8");
				fileList = upload.parseRequest(request);// 获得列表
			} catch (FileUploadException ex) {
				// ex.printStackTrace();
				throw ex;
			}
			Iterator<?> it = fileList.iterator();
			String fileName = "";// 文件名
			String extName = "";// 文件扩展名
			long size = 0;// 图片大小
			// 解决中文乱码返回网络路径
			response.setContentType("text/html;charset=UTF-8");
			int fileCount = 0;
			JSONArray responseInfo = new JSONArray();
			while (it.hasNext()) {
				FileItem item = (FileItem) it.next();
				if (!item.isFormField()) {
					fileCount++;
					if (item.getName().indexOf(".") > -1) {
						fileName = filter(item.getName().substring(0,
								item.getName().lastIndexOf(".")));// 获得文件名
						extName = item.getName().substring(
								item.getName().lastIndexOf("."));
					} else {
						fileName = filter(item.getName());// 获得文件名
						extName = "";
					}

					/**
					 * 把上传文档名字中的特殊的字符替换成"_"
					 * 
					 * @author Alvin
					 * @time 2014-12-22
					 */
					fileName = fileName.replace("#", "_").replace(",", "_")
							.replace("'", "_").replace(";", "_")
							.replace("%", "_").replace("&", "_")
							.replace("+", "%2B");

					fileName = URLDecoder.decode(fileName, "UTF-8");

					if (fileName.indexOf("/") >= 0
							|| fileName.indexOf("\\") >= 0) {// 防止非法上传文件到任意目录
						throw new OBPMValidateException("上传的文件名称不合法！");
					}
					if (isNotLegalFileExt(extName)) {
						throw new OBPMValidateException("上传的文件类型不合法！");
					}
					size = item.getSize();// 获得图片大小
					if (item.getName() == null
							|| item.getName().trim().equals("")
							|| item.getName().trim().equals("null")) {
						continue;
					}
					// 扩展名格式：
					if (item.getName().lastIndexOf(".") >= 0) {
						uploadFileName = fileName + extName;
						uploadContentType = allowedTypes
								+ "/"
								+ item.getName().substring(
										item.getName().lastIndexOf(".") + 1);
					}
					uploadInfo1 = getUploadInfo();
					try {
						uuid = UUID.randomUUID().toString();
						saveFile = new File(uploadInfo1.getFileRealDir() + year
								+ File.separator + uuid + extName);
						item.write(saveFile);
						if (applicationid != null && !applicationid.equals("")) {
							UploadProcess uploadProcess = (UploadProcess) ProcessFactory
									.createRuntimeProcess(UploadProcess.class,
											applicationid);
							UploadVO uploadVO = new UploadVO();
							uploadVO.setId(uuid);
							uploadVO.setName(uploadInfo1.getFileName());
							uploadVO.setFieldid(fieldid);
							uploadVO.setType(extName);
							uploadVO.setSize(size);
							uploadVO.setModifyDate(new SimpleDateFormat(
									"yyyy-MM-dd").format(new Date()));
							uploadVO.setUserid(userid);
							uploadVO.setPath(uploadInfo1.getFileDir() + year
									+ "/" + uuid + extName);
							uploadVO.setFolderPath(uploadInfo1.getFileDir());
							uploadProcess.doCreate(uploadVO);

							if (!"excelImport".equals(actionType)) {// excel导入时，不执行swf转换
								// 生成SWF文件
								createSWF(extName.substring(1).toLowerCase(),
										saveFile.getPath(), uuid);
							}

						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					JSONObject responseInfoItem = new JSONObject();
					responseInfoItem.put("name", uploadInfo1.getFileSaveName());
					responseInfoItem.put("path", uploadInfo1.getFileDir()
							+ year + "/" + uuid + extName);
					responseInfo.add(responseInfoItem);
				}

			}
			if (fileCount > 1) {
				response.getWriter().print(responseInfo.toString());
			} else {
				response.getWriter().print(
						uploadInfo1.getFileSaveName() + ","
								+ uploadInfo1.getFileDir() + year + "/" + uuid
								+ extName);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().print(e.getMessage() + "," + "ERROR");
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 生成SWF文件
	 * 
	 * @param fileType
	 *            文件扩展名
	 * @param path
	 *            文件路径
	 * @param uuid
	 *            id
	 */
	private void createSWF(String fileType,String path ,String uuid){
		
		if ("doc".equals(fileType) || "docx".equals(fileType) || "xls".equals(fileType)
			|| "xlsx".equals(fileType) || "pdf".equals(fileType)
			|| "txt".equals(fileType) || "rtf".equals(fileType)
			|| "et".equals(fileType) || "ppt".equals(fileType)
			|| "pptx".equals(fileType)
			|| "dps".equals(fileType) || "pot".equals(fileType)
			|| "pps".equals(fileType) || "wps".equals(fileType)
			|| "html".equals(fileType) || "htm".equals(fileType)) {
			
			ConverterBundle bundle = new ConverterBundle(fileType, path, uuid);
			ConverterRunner runner = ConverterRunner.getInstance();
			runner.addBundle(bundle);
			runner.convert();
		}
	}

	// 过滤 '_'
	protected String filter(String str) {
		StringBuffer sb = new StringBuffer();
		sb.append(str);
		if (sb.indexOf("_") != -1) {
			sb.deleteCharAt(sb.indexOf("_"));
			filter(sb.toString());
		}
		return sb.toString();
	}

	// doget请求
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			processRequest(request, response);
		} catch (FileUploadException fe) {
			Log.debug(fe.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// dopost请求
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			processRequest(request, response);
		} catch (FileUploadException fe) {
			Log.debug(fe.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 返回UploadInfo实例
	 * 
	 * @return
	 */
	public UploadInfo getUploadInfo() {
		// 设置属性
		UploadInfo uploadInfo = new UploadInfo();
		uploadInfo.setAllowedTypes(allowedTypes);
		uploadInfo.setContentType(uploadContentType);
		uploadInfo.setFileName(uploadFileName);
		uploadInfo.setFileSaveMode(fileSaveMode);
		try {
			uploadInfo.setPath(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uploadInfo;
	}

	/**
	 * 是否为不合法文件类型
	 * 
	 * @param fileName
	 * @throws Exception
	 */
	private boolean isNotLegalFileExt(String fileExt) throws Exception {
		if(StringUtil.isBlank(fileExt))
			return true;
		String fileType = fileExt.substring(1);
		for (int i = 0; i < excludeTypes.length; i++) {
			String excludeType = excludeTypes[i];
			if (StringUtil.isBlank(excludeType))
				continue;
			if (fileType.equalsIgnoreCase(excludeType))
				return true;
		}
		return false;
	}

}
