package cn.myapps.km.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import cn.myapps.km.disk.ejb.NFile;


/**
 * 物理文件操作工具类
 * @author xiuwei
 *
 */
public class FileUtils {
	
	public static final String uploadFolder = "ndisk";
	public static final String SWF_PATH = "swf";
	
	/**
	 * 创建文件夹
	 * @throws Exception 
	 */
	public static void createFolder(String path) throws Exception {
		try {
			File file = new File(path);
			if(!file.exists()){
				file.mkdir();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 删除文件夹
	 * @throws Exception 
	 */
	public static void delFolder(String path) throws Exception{
		try {
			delAllFile(path);
			File file = new File(path);
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 删除所有文件,包括文件夹
	 * @throws Exception 
	 */
	public static void delAllFile(String path) throws Exception{
		try {
			File file = new File(path);
			if(!file.exists()){
				return;
			}
			if(!file.isDirectory()){
				return;
			}
			
			String[] tempList = file.list();
			File temp = null;
			for(int i=0; i<tempList.length; i++){
				if(path.endsWith(File.separator)){
					temp = new File(path + tempList[i]);
				}else {
					temp = new File(path + File.separator + tempList[i]);
				}
				
				if(temp.isFile()){
					temp.delete();
				}
				if(temp.isDirectory()){
					delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
					delFolder(path + "/" + tempList[i]); //再删除空文件夹
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 转存时文件复制
	 * @param newPath 转存到路径
	 * @param oldPath 原路径
	 */
	public static void copyFile(String newPath,String oldPath) throws Exception{
		try{
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath); 
			if (oldfile.exists()) { //文件存在时 
				InputStream inStream = new FileInputStream(oldPath); //读入原文件 
				FileOutputStream fs = new FileOutputStream(newPath); 
				byte[] buffer = new byte[1444]; 
				while ( (byteread = inStream.read(buffer)) != -1) { 
					bytesum += byteread; //字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
			inStream.close();
			}
		}catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();
			throw e;
		}
	}
	
	public static String FormetFileSize(long fileS) {//转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }
	
	/**
	 * 文件下载response设置
	 * 
	 * @return
	 * @throws IOException
	 */
	public static void doFileDownload(HttpServletRequest request,HttpServletResponse response, NFile nFile) throws IOException {
		// String fileName = (String)ServletActionContext.getRequest().getAttribute("downloadFileName");

		String realFileName = "";
		String realPath = request.getSession().getServletContext().getRealPath("")+File.separator+FileUtils.uploadFolder;
		realFileName = realPath+nFile.getUrl();
		File file = new File(realFileName);
		if (file.exists()) {
			try {
				setResponse(response, file,nFile);
			} catch (IOException e) {
				e.printStackTrace();
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
	public static void setResponse(HttpServletResponse response, File file,NFile nFile) throws IOException {
		OutputStream os = null;
		BufferedInputStream reader = null;
		try {
			String encoding = "utf-8";
			HttpServletRequest request = ServletActionContext.getRequest();
			String agent = request.getHeader("USER-AGENT");
//			String address = file.toString();
			if(null != agent && -1 != agent.indexOf("Firefox")){
				response.setContentType("application/x-download; charset=" + encoding + "");
				response.setHeader("Content-Disposition", "attachment;filename=\"" + MimeUtility.encodeText(nFile.getName(), encoding, "B") + "\"");
			}else{
				response.setContentType("application/x-download; charset=" + encoding + "");
				response.setHeader("Content-Disposition", "attachment;filename=\"" + java.net.URLEncoder.encode(nFile.getName(), encoding) + "\"");
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
