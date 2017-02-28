package cn.myapps.util.converter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.apache.log4j.Logger;

/**
 * pdf文档转换为SWF的转换器
 * 
 * @author xiuwei
 * 
 */
public class PDF2SWF implements Converter {
	/**
	 * 
	 */
	private static final long serialVersionUID = -977509009832395455L;
	
	Logger log = Logger.getLogger(getClass());

	public void init() throws Exception {
		// TODO Auto-generated method stub

	}

	public void convert(String source, String destPath, String fileName)
			throws Exception {

		File destDir = new File(destPath);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		
		if(!new File(source).exists()){
			log.error(String.format("文档[%s]转换SWF失败，因为找不到物理文件%s", fileName,source));
			new FileNotFoundException(String.format("文档不存在%s", source));
		}
		String errorFieldPath = destPath+fileName.substring(0, fileName.lastIndexOf(".")+1)+"err";

		String format = getFileExtensionName(source.toLowerCase());
		try {
			if (format.equals("pdf")) {
				pdf2swf(source, destPath, fileName);
			} else if (format.equals("jpg") || format.equals("jpeg")) {
				image2swf(source, destPath, fileName, "jpeg2swf.exe");
			} else if (format.equals("png")) {
				image2swf(source, destPath, fileName, "png2swf.exe");
			} else if (format.equals("gif")) {
				gif2swf(source, destPath, fileName);
			}
			new File(errorFieldPath).deleteOnExit();
		} catch (Exception e) {
			new File(errorFieldPath).createNewFile();
			log.error(String.format("文档[%s]转换SWF失败，物理文件可能已经损坏[%s]", fileName,source));
			throw new RuntimeException(String.format("文档[%s]转换SWF失败，物理文件可能已经损坏[%s]", fileName,source),e);
		}
	}

	public void pdf2swf(String source, String destPath, String fileName)
			throws Exception {
		// 目标路径不存在则建立目标路径
		File dest = new File(destPath);
		if (!dest.exists())
			dest.mkdirs();

		// 源文件不存在则返回
		File s = new File(source);
		if (!s.exists())
			return;

		// 调用pdf2swf命令进行转换
		// String command = "C:/Program Files/SWFTools/pdf2swf.exe" + " -o " +
		// destPath + "/" + fileName +
		// "  <span style='color: #ff0000;'>languagedir=D:/xpdf/xpdf-chinese-simplified</span> -s flashversion=9 "
		// + sourcePath + "";
		String command = "pdf2swf.exe -t \"" + source + "\" -T 9 -f -o \""
				+ destPath + "/" + fileName + "\" -qq -s "
				+ " languagedir=C:\\xpdf\\xpdf-chinese-simplified ";
		
		//System.out.println("文件大小："+s.length());
		//if(s.length()>2*1024*1024){
			command = command+" -G -s poly2bitmap";//只对文件中的图形转成点阵
		//}
		Process pro = Runtime.getRuntime().exec(command);

		StreamGobbler err = new StreamGobbler(pro.getErrorStream(), "ERROR");
		StreamGobbler out = new StreamGobbler(pro.getInputStream(), "OUTPUT");
		err.start();
		out.start();
		
		try {
			pro.waitFor();
		} catch (InterruptedException e) {
			throw e;
		} finally {
			// pro.exitValue();
		}

	}

	public void image2swf(String source, String destPath, String fileName,
			String command) throws Exception {
		// 目标路径不存在则建立目标路径
		File dest = new File(destPath);
		if (!dest.exists())
			dest.mkdirs();

		// 源文件不存在则返回
		File s = new File(source);
		if (!s.exists())
			return;

		// 调用pdf2swf命令进行转换
		command = command + " -o " + destPath + "/" + fileName
				+ " -T 9 -q 100 " + source + "";
		Process pro = Runtime.getRuntime().exec(command);

		StreamGobbler err = new StreamGobbler(pro.getErrorStream(), "ERROR");
		StreamGobbler out = new StreamGobbler(pro.getInputStream(), "OUTPUT");
		err.start();
		out.start();

		try {
			pro.waitFor();
		} catch (InterruptedException e) {
			throw e;
		} finally {
			pro.exitValue();
		}
	}

	public void gif2swf(String source, String destPath, String fileName)
			throws Exception {
		// 目标路径不存在则建立目标路径
		File dest = new File(destPath);
		if (!dest.exists())
			dest.mkdirs();

		// 源文件不存在则返回
		File s = new File(source);
		if (!s.exists())
			return;

		// 调用pdf2swf命令进行转换
		String command = "gif2swf.exe -o " + destPath + "/" + fileName + " "
				+ source + "";
		Process pro = Runtime.getRuntime().exec(command);

		StreamGobbler err = new StreamGobbler(pro.getErrorStream(), "ERROR");
		StreamGobbler out = new StreamGobbler(pro.getInputStream(), "OUTPUT");
		err.start();
		out.start();

		try {
			pro.waitFor();
		} catch (InterruptedException e) {
			throw e;
		} finally {
			pro.exitValue();
		}
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param fileName
	 * @return
	 */
	public String getFileExtensionName(String fileName) {
		String extension = "";
		if (fileName != null) {
			int dotIndex = fileName.lastIndexOf(".");
			if (dotIndex != -1) {
				extension = fileName.substring(dotIndex + 1);
			}
		}
		return extension;
	}

	public static void main(String[] args) throws Exception {
		// new PDF2SWF().convert("E:\\demo\\2.png", "E:\\demo\\", "2.swf");
		// new PDF2SWF().convert("E:\\demo\\1.jpg", "E:\\demo\\", "1.swf");
		// String pdf = "11e2-c8f3-ae1efa11-b626-7dd70bae066f.pdf";
		String pdf = "11e2-c8f5-23b5b65a-b626-7dd70bae066f.pdf";

		String source = "D:\\JSPTPDKM\\MyApps-2.5.sp6.13557\\bin\\apache-tomcat-6.0.14\\webapps\\obpm\\ndisk\\11e2-b620-b0bb9bdb-9f53-1df94607cbed\\"
				+ pdf;
		String destDir = "D:\\JSPTPDKM\\MyApps-2.5.sp6.13557\\bin\\apache-tomcat-6.0.14\\webapps\\obpm\\ndisk\\11e2-b620b0bb9bdb-9f53-1df94607cbed\\swf\\";
		String fileName = "11e2-c8f3-468733ba-b626-7dd70bae066f.swf";
		new PDF2SWF().pdf2swf(source, destDir, fileName);
	}

}

class StreamGobbler extends Thread {
	InputStream is;
	String type;
	OutputStream os;

	StreamGobbler(InputStream is, String type) {
		this(is, type, null);
	}

	StreamGobbler(InputStream is, String type, OutputStream redirect) {
		this.is = is;
		this.type = type;
		this.os = redirect;
	}

	public void run() {
		InputStreamReader isr = null;  
        BufferedReader br = null;  
        PrintWriter pw = null;  
		try {
			if (os != null)
				pw = new PrintWriter(os);
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				if (pw != null)
					pw.println(line);
				System.out.println(type + ">" + line);
			}
			if (pw != null)
				pw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}finally{
			if(pw !=null) pw.close();
			if(br !=null)
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if(isr !=null)
				try {
					isr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
}