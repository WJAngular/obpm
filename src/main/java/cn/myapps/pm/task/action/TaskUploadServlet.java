package cn.myapps.pm.task.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.python.modules.newmodule;

import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.pm.task.dao.MySqlTaskDAO;
import cn.myapps.pm.task.ejb.TaskProcessBean;
import cn.myapps.pm.util.Sequence;
import cn.myapps.util.json.JsonUtil;

public class TaskUploadServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1507451476583038609L;

	// 通过doget请求处理
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			processRequest(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 通过dopost请求处理
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			processRequest(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// NDirProcess dirProcess = new NDirProcessBean();
		// String nFileId = null;
		// String nDirId = null;
		try {
			DiskFileItemFactory fac = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(fac);
			upload.setHeaderEncoding("utf-8");
			List fileList = null;
			try {
				fileList = upload.parseRequest(request);
			} catch (FileUploadException ex) {
				return;
			}

			Iterator<FileItem> iter = fileList.iterator();
			String taskid = null;
			while (iter.hasNext()) {
				FileItem item = iter.next();
				if (item.isFormField()) {
					String fieldName = item.getFieldName();
					if (fieldName.equalsIgnoreCase("TaskId")) {
						taskid = item.getString("UTF-8");
					}
				}

			}
			// if(/*nFileId == null ||*/ nDirId ==null){
			// response.setStatus(500);
			// return;
			// }
			// NDir dir = (NDir)dirProcess.doView(nDirId);
			// if(dir ==null) {
			// throw new Exception("找不到上传目录");
			// }

			String realPath = this.getServletConfig().getServletContext()
					.getRealPath("");// 服务器目录的绝对路径
			String relativePath = "\\task\\" + taskid; // 目录的相对路径
			String savePath = realPath + relativePath; // 存放此文件的绝对路径

			File f1 = new File(savePath);
			if (!f1.exists()) {
				f1.mkdirs();
			}

			Iterator<FileItem> it = fileList.iterator();

			Map<String, String> resultInfo = new HashMap<String, String>();
			Map<Object, Object> fileMap = new HashMap<Object, Object>();
			Map<Object, Object> fileMap2 = new HashMap<Object, Object>();
			int i = 1;

			while (it.hasNext()) {
				FileItem item = it.next();
				if (!item.isFormField()) {
					String id = Sequence.getSequence();
					String extName = "";
					String name = item.getName();
					long size = item.getSize();
					resultInfo.put("id", id);
					resultInfo.put("name", name);
					resultInfo.put("size", String.valueOf(size));
					resultInfo.put("taskid", taskid);
					String type = item.getContentType();
					if (name == null || name.trim().equals("")) {
						continue;
					}
					// 扩展名格式：
					int index = name.lastIndexOf(".");
					resultInfo.put("title", name.substring(0, index));
					extName = name.substring(index);
					// 生成文件名：
					File saveFile = new File(savePath + File.separator + id
							+ extName);
					resultInfo.put("url", relativePath + File.separator + id
							+ extName);

					fileMap.put("id", id);
					fileMap.put("name", name);
					fileMap.put("size", size);
					fileMap2.put(id, fileMap);
					i++;
					try {
						item.write(saveFile); // 文件上传到服务器
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			String attachjson = JsonUtil.toJson(fileMap2);
			//
			try {

				TaskProcessBean process = new TaskProcessBean();
				if (process.doView(taskid) != null) {
					process.addAttachment(taskid, attachjson);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			PrintWriter writer = encodehead(request, response);
			String result = JsonUtil.toJson(resultInfo);
			writer.print(result);
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

	/**
	 * Ajax辅助方法 获取 PrintWriter
	 * 
	 * @return
	 * @throws IOException
	 * @throws IOException
	 *             request.setCharacterEncoding("utf-8");
	 *             response.setContentType("text/html; charset=utf-8");
	 */
	private PrintWriter encodehead(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		return response.getWriter();
	}

	public static void main(String[] args) {

	}
}
