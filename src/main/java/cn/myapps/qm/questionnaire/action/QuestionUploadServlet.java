package cn.myapps.qm.questionnaire.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
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

import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.pm.util.Sequence;
import cn.myapps.util.DateUtil;
import cn.myapps.util.json.JsonUtil;
import cn.myapps.util.property.DefaultProperty;

public class QuestionUploadServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -1507451476583038609L;

    // 通过doget请求处理
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	try {
	    processRequest(request, response);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    // 通过dopost请求处理
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	try {
	    processRequest(request, response);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

	try {
	    DiskFileItemFactory fac = new DiskFileItemFactory();
	    ServletFileUpload upload = new ServletFileUpload(fac);
	    upload.setHeaderEncoding("UTF-8");
	    Map<String, FileItem> map = new HashMap<String, FileItem>();
	    try {
		List<FileItem> fileList = upload.parseRequest(request);
		for (FileItem item : fileList) {
		    map.put(item.getFieldName(), item);
		}
	    } catch (FileUploadException ex) {
		return;
	    }

	    String uploadPath = DefaultProperty.getProperty("myapps.qm.upload.path","/uploads/qm/");
	    
	    // 服务器目录的绝对路径
	    String realPath = this.getServletConfig().getServletContext().getRealPath("");
	    
	     // 目录的相对路径
	    String relativePath =uploadPath + DateUtil.getCurDateStr("yyyy");
	    
	     // 存放此文件的绝对路径
	    String savePath = realPath + relativePath;

	    File f1 = new File(savePath);
	    if (!f1.exists()) {
		f1.mkdirs();
	    }

	    Map<String, String> resultInfo = new HashMap<String, String>();

	    FileItem fileData = map.get("file");
	    String id = Sequence.getSequence();
	    String fileName = fileData.getName();
	    long size = fileData.getSize();
	    resultInfo.put("name", fileName);
	    resultInfo.put("size", String.valueOf(size));
	    
	    // 扩展名格式：
	    int index = fileName.lastIndexOf(".");

	    String extName  = fileName.substring(index);
	    
	    // 生成文件名：
	    File saveFile = new File(savePath + "/" + id + extName);
	    resultInfo.put("url", relativePath + "/" + id + extName);

	    try {
		// 文件上传到服务器
		fileData.write(saveFile); 
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
    private PrintWriter encodehead(HttpServletRequest request, HttpServletResponse response) throws IOException {
	request.setCharacterEncoding("utf-8");
	response.setContentType("text/html; charset=utf-8");
	return response.getWriter();
    }

}
