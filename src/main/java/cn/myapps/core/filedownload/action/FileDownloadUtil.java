package cn.myapps.core.filedownload.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;


import cn.myapps.constans.Environment;

public class FileDownloadUtil {
	/**
	 * 文件下载response设置
	 * 
	 * @return
	 * @throws IOException
	 */
	public static void doFileDownload(HttpServletResponse response, String fileName) throws IOException {
		// String fileName = (String)ServletActionContext.getRequest().getAttribute("downloadFileName");

		Environment env = Environment.getInstance();
		String realFileName = env.getRealPath(fileName);
		File file = new File(realFileName);
		if (file.exists()) {
			try {
				setResponse(response, file);
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
	public static void setResponse(HttpServletResponse response, File file) throws IOException {
		OutputStream os = null;
		BufferedInputStream reader = null;
		try {
			String encoding = Environment.getInstance().getEncoding();
			HttpServletRequest request = ServletActionContext.getRequest();
			String agent = request.getHeader("USER-AGENT");
			String address = file.toString();
			if(null != agent && -1 != agent.indexOf("Firefox")){
				response.setContentType("application/x-download; charset=" + encoding + "");
				response.setHeader("Content-Disposition", "attachment;filename=\"" + MimeUtility.encodeText(file.getName(), encoding, "B") + "\"");
			}else{
				if(address.indexOf("pdf") == -1){
					response.setContentType("application/x-download; charset=" + encoding + "");
					response.setHeader("Content-Disposition", "attachment;filename=\"" + java.net.URLEncoder.encode(file.getName(), encoding) + "\"");
				}
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
}
