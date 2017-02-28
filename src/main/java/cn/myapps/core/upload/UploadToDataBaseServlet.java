package cn.myapps.core.upload;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.upload.ejb.UploadProcess;
import cn.myapps.core.upload.ejb.UploadVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

/**
 * Servlet implementation class UploadToDataBaseServlet
 */
public class UploadToDataBaseServlet extends HttpServlet {

	private static final long serialVersionUID = -5553779872001072240L;

	// doget请求
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			processRequest(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// dopost请求
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			processRequest(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 文件上传请求调用的方法
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void processRequest(HttpServletRequest request,
			HttpServletResponse response)

	throws Exception {

		try {
			request.setCharacterEncoding("utf-8");
			String uuid = "";
			String fieldidtemp = URLDecoder.decode(
					request.getParameter("fieldid"), "UTF-8");
			String[] strs = fieldidtemp.split(";");
			String allowedTypes = strs[0];
			String fieldId = strs[1];
			String applicationid = strs[2];
			String userid = strs.length == 4 ? strs[3] : "";
			if (StringUtil.isBlank(userid))
				userid = ((WebUser) request.getSession().getAttribute(
						"FRONT_USER")) != null ? ((WebUser) request
						.getSession().getAttribute("FRONT_USER")).getId()
						: null;

			String name = "";// 文件名+文件扩展名

			// 磁盘文件个项工厂
			DiskFileItemFactory fac = new DiskFileItemFactory();
			// servlet文件上传
			ServletFileUpload upload = new ServletFileUpload(fac);
			// 设置上传编码
			upload.setHeaderEncoding("utf-8");
			List<?> fileList = null;// 文件列表
			try {
				fileList = upload.parseRequest(request);// 获得列表
			} catch (FileUploadException ex) {
				ex.printStackTrace();
				throw ex;
			}
			Iterator<?> it = fileList.iterator();
			int i = 0;
			String extName = "";// 文件扩展名
			long size = 0;// 图片大小
			while (it.hasNext()) {
				i++;
				FileItem item = (FileItem) it.next();
				if (!item.isFormField()) {
					name = filter(item.getName());// 获得文件名和扩展名
					name = URLDecoder.decode(name, "UTF-8");
					extName = item.getName().substring(
							item.getName().lastIndexOf("."));
					size = item.getSize();// 获得图片大小
					if (item.getName() == null
							|| item.getName().trim().equals("")
							|| item.getName().trim().equals("null")) {
						continue;
					}
					try {
						UploadProcess uploadProcess = (UploadProcess) ProcessFactory
								.createRuntimeProcess(UploadProcess.class,
										applicationid);
						InputStream in = item.getInputStream();
						if (!StringUtil.isBlank(allowedTypes)
								&& allowedTypes.equals("image")) {
							Collection<UploadVO> datas = uploadProcess
									.findByColumnName("FIELDID", fieldId);
							if (datas.size() > 0) {
								for (Iterator<UploadVO> ite = datas.iterator(); ite
										.hasNext();) {
									UploadVO uploadVO = (UploadVO) ite.next();
									uuid = uploadVO.getId();
									uploadVO.setName(name);
									uploadVO.setImgBinary(in);
									uploadVO.setModifyDate(new SimpleDateFormat(
											"yyyy-MM-dd").format(new Date()));
									uploadProcess.doUpdate(uploadVO);
								}
							} else {
								uuid = UUID.randomUUID().toString();
								UploadVO uploadVO = new UploadVO();
								uploadVO.setId(uuid);
								uploadVO.setName(name);
								uploadVO.setImgBinary(in);
								uploadVO.setFieldid(fieldId);
								uploadVO.setUserid(userid);
								uploadVO.setType(extName);
								uploadVO.setSize(size);
								uploadVO.setModifyDate(new SimpleDateFormat(
										"yyyy-MM-dd").format(new Date()));
								uploadProcess.doCreate(uploadVO);
							}
						} else {
							uuid = UUID.randomUUID().toString();
							UploadVO uploadVO = new UploadVO();
							uploadVO.setId(uuid);
							uploadVO.setName(name);
							uploadVO.setImgBinary(in);
							uploadVO.setFieldid(fieldId);
							uploadVO.setUserid(userid);
							uploadVO.setType(extName);
							uploadVO.setSize(size);
							uploadVO.setModifyDate(new SimpleDateFormat(
									"yyyy-MM-dd").format(new Date()));
							uploadProcess.doCreate(uploadVO);
						}
						if (in != null) {
							in.close();
						}
					} catch (Exception e) {
						response.setContentType("text/html;charset=UTF-8");
						response.getWriter().print(
								"Error:" + name + ";" + e.toString());
						e.printStackTrace();
						return;
					}
				}
			}

			// 解决中文乱码返回网络路径
			response.setContentType("text/html;charset=UTF-8");
			// System.out.println(uuid+"_"+name);
			response.getWriter().print(uuid + "_" + name);
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

}
