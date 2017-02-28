package cn.myapps.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import cn.myapps.constans.Environment;
import cn.myapps.core.dynaform.dts.datasource.ejb.DataSourceProcess;
import cn.myapps.util.ProcessFactory;

public class FileUtil {

	public static String writeFileByDS(String fileWebName, String dataSourceName, String sql, String applicationId)
			throws Exception {
		DataSourceProcess dsProcess = (DataSourceProcess) ProcessFactory.createProcess(DataSourceProcess.class);

		try {
			Environment env = Environment.getInstance();
			Map<?, ?> map = dsProcess.findDataSourceSQL(dataSourceName, sql, applicationId);
			for (Iterator<?> iterator = map.entrySet().iterator(); iterator.hasNext();) {
				Entry<?, ?> entry = (Entry<?, ?>) iterator.next();
				if (entry.getValue() instanceof Blob) {
					Blob blob = (Blob) entry.getValue();
					String fileTypes = FileOperate.getFileType(blob.getBinaryStream());

					String[] types = fileTypes.split("/");
					if (types.length > 0) {
						StringBuffer fileNames = new StringBuffer();
						for (int i = 0; i < types.length; i++) {
							FileOperate.writeFile(env.getRealPath(fileWebName + "." + types[i]), blob.getBinaryStream());
							fileNames.append(env.getContextPath() + fileWebName + "." + types[i] + ";");
						}
						//fileNames = fileNames.substring(0, fileNames.toString().lastIndexOf(";"));
						return fileNames.substring(0, fileNames.toString().lastIndexOf(";"));
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}
	
	/**
	 * 转存时文件复制
	 * @param newPath 转存到路径
	 * @param oldPath 原路径
	 */
	public static void copyFile(String newPath,String oldPath) throws Exception{
		InputStream inStream = null;
		FileOutputStream fs = null;
		try{
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath); 
			if (oldfile.exists()) { //文件存在时 
				inStream = new FileInputStream(oldPath); //读入原文件 
				fs = new FileOutputStream(newPath); 
				byte[] buffer = new byte[1444]; 
				while ( (byteread = inStream.read(buffer)) != -1) { 
					bytesum += byteread; //字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
			}
		}catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();
			throw e;
		}finally {
			if(inStream != null){
				inStream.close();
			}
			if(fs != null){
				fs.close();
			}
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
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
