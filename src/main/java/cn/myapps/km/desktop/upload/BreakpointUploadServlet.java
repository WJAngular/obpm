package cn.myapps.km.desktop.upload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.km.disk.ejb.NDir;
import cn.myapps.km.disk.ejb.NDirProcess;
import cn.myapps.km.disk.ejb.NDirProcessBean;
import cn.myapps.km.util.FileUtils;
import cn.myapps.km.util.Sequence;
import cn.myapps.util.StringUtil;

/**
 * 断点续传
 * 
 * @author Happy
 * 
 */
public class BreakpointUploadServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4275252610690933584L;
	private static final int UPLOAD_ERROR_SERVER = -200;
	private static final int UPLOAD_SUCCESS = 200;
	private static final int UPLOAD_ERROR_NETWORK_DISCONNECT = 101;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String action = req.getParameter("action");
		if ("queryUploadSize".equals(action)) {
			try {
				doQueryUploadSize(req, resp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			doUpload(req, resp);
		} catch (Exception e) {
		}
	}

	public void doUpload(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			int state = 0;
			try {

				NDirProcess dirProcess = new NDirProcessBean();
				String nDirId = null;
				long offset = 0L;
				long fileLength = 0L;
				String nFileId = "";

				DiskFileItemFactory fac = new DiskFileItemFactory();
				BreakpointServletFileUpload upload = new BreakpointServletFileUpload(
						fac);
				upload.setHeaderEncoding("utf-8");
				List fileList = null;
				try {
					fileList = upload.parseRequest(request);
					if (upload.isDisconnect()) {// 客户端传输过程中断了连接
						state = UPLOAD_ERROR_NETWORK_DISCONNECT;
					}
				} catch (FileUploadException ex) {
					state = -100;
					throw ex;
				}

				Iterator<FileItem> iter = fileList.iterator();
				while (iter.hasNext()) {
					FileItem item = iter.next();
					if (item.isFormField()) {
						String fieldName = item.getFieldName();
						if (fieldName.equalsIgnoreCase("nFileId")) {
							nFileId = item.getString("UTF-8");
						} else if (fieldName.equalsIgnoreCase("nDirId")) {
							nDirId = item.getString("UTF-8");
						} else if (fieldName.equalsIgnoreCase("offset")) {
							offset = Long.valueOf(item.getString("UTF-8"));
						} else if (fieldName.equalsIgnoreCase("fileLength")) {
							fileLength = Long.valueOf(item.getString("UTF-8"));
						}
					}

				}
				if (/* nFileId == null || */nDirId == null) {
					response.setStatus(500);
					return;
				}
				NDir dir = (NDir) dirProcess.doView(nDirId);
				if (dir == null) {
					throw new Exception("找不到上传目录");
				}

				String realPath = this.getServletConfig().getServletContext()
						.getRealPath("")
						+ File.separator + FileUtils.uploadFolder;// 服务器目录的绝对路径
				String relativePath = dir.getPath();// 目录的相对路径
				String savePath = realPath + relativePath; // 存放此文件的绝对路径

				File f1 = new File(savePath);
				if (!f1.exists()) {
					f1.mkdirs();
				}

				Iterator<FileItem> it = fileList.iterator();

				while (it.hasNext()) {
					FileItem item = it.next();
					if (!item.isFormField()) {
						String extName = "";
						String name = item.getName();
						if (name == null || name.trim().equals("")) {
							continue;
						}
						// 扩展名格式：
						int index = name.lastIndexOf(".");
						extName = name.substring(index);

						// 临时文件名
						String tempPath = savePath + File.separator + nFileId
								+ ".tmp";

						if (offset == 0) {
							File temp = new File(tempPath);
							if (temp.exists()) {
								temp.delete();
							}
							temp.createNewFile();
							temp = null;
						}

						RandomAccessFile raf = null;
						InputStream in = null;
						try {
							raf = new RandomAccessFile(tempPath, "rw");

							if (offset > 0) {
								raf.setLength(offset);
								raf.seek(offset);
							}

							byte[] b = new byte[1024];
							in = item.getInputStream();

							int nRead = 0;
							while ((nRead = in.read(b)) > 0) {
								raf.write(b, 0, nRead);
							}
							raf.close();

							// 文件完整上传后 修改临时文件名
							File temp = new File(tempPath);
							if (temp.exists() && temp.length() == fileLength) {
								temp.renameTo(new File(savePath
										+ File.separator + nFileId + extName));
								state = UPLOAD_SUCCESS;
							}
						} catch (Exception ee) {
							state = UPLOAD_ERROR_SERVER;
							throw ee;
						} finally {
							if (raf != null) {
								try {
									raf.close();
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							}
							if (in != null) {
								try {
									in.close();
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			PrintWriter writer = getPrintWriter(request, response);
			writer.print(state);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void doQueryUploadSize(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String nDiskId = request.getParameter("nDiskId");
		String nDirId = request.getParameter("nDirId");
		String nFileId = request.getParameter("nFileId");

		if (!StringUtil.isBlank(nDirId) && !StringUtil.isBlank(nFileId)) {
			NDirProcess dirProcess = new NDirProcessBean();
			NDir dir = (NDir) dirProcess.doView(nDirId);
			if (dir == null) {
				response.setStatus(500);
				throw new Exception("找不到上传目录");
			}
			String realPath = this.getServletConfig().getServletContext()
					.getRealPath("")
					+ File.separator + FileUtils.uploadFolder;// 服务器目录的绝对路径
			String relativePath = dir.getPath();// 目录的相对路径
			String savePath = realPath + relativePath; // 存放此文件的绝对路径
			String tempPath = savePath + File.separator + nFileId + ".tmp";

			File file = new File(tempPath);

			if (file.exists()) {
				response.getWriter().print(file.length());
			} else {
				response.getWriter().print(0);
			}

		}

	}

	private PrintWriter getPrintWriter(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		return response.getWriter();
	}

}
