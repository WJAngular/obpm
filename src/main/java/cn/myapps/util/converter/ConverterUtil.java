package cn.myapps.util.converter;

import java.io.File;

import cn.myapps.km.disk.ejb.NFile;
import cn.myapps.km.util.Config;
import cn.myapps.km.util.FileUtils;


/**
 * 文件转换工具
 * @author xiuwei
 *
 */
public class ConverterUtil {
	
	private static Document2PdfConverter document2PdfConverter = null;
	
	
	/**
	 * 生成SWF文件
	 * @param file
	 * @param source
	 * @param type
	 * @throws Exception
	 */
	public static void createSWF(String uuid,String source,String type) throws Exception {
		try {
			int pos = source.lastIndexOf(uuid+".");
			if (pos <0) return;
			
			if(!new File(source).exists()) return;
			
			String destPath = source.substring(0, pos-1)+File.separator+FileUtils.SWF_PATH+File.separator;//目标文件目录
			String fileName = "";//目标文件名
			if(NFile.TYPE_PDF.equalsIgnoreCase(type) || NFile.TYPE_JPEG.equalsIgnoreCase(type) || NFile.TYPE_JPG.equalsIgnoreCase(type) || NFile.TYPE_PNG.equalsIgnoreCase(type) || NFile.TYPE_GIF.equalsIgnoreCase(type)){
				fileName = uuid+"."+NFile.TYPE_SWF;
				ConverterUtil.toSwf(source, destPath, fileName);
			}else {
				fileName = uuid+"."+NFile.TYPE_PDF;
				ConverterUtil.document2pdf(source, destPath, fileName);
				source = destPath+uuid+"."+NFile.TYPE_PDF;
				
				File srcFile = new File(source);
				if (srcFile.exists() && srcFile.isFile()) {
					fileName = uuid+"."+NFile.TYPE_SWF;
					ConverterUtil.toSwf(source, destPath, fileName);
				}
				//删除临时生成的pdf文件
				/**
				File s = new File(source);
				if(s.exists()){
					s.delete();
				}
				**/
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 将可阅读文档转换为PDF文档
	 * @param source
	 * @param destPath
	 * @param fileName
	 * @throws Exception
	 */
	public static void document2pdf(String source, String destPath,String fileName) throws Exception{
		Converter converter = getDocument2PdfConverter();
		converter.init();
		converter.convert(source, destPath, fileName);
	}
	
	/**
	 * 将文档转换为SWF文件
	 * @param source
	 * 		源文件地址
	 * @param destPath
	 * 		目标文件目录
	 * @param fileName
	 * 		目标文件名
	 * @param params
	 * 		参数
	 * @return
	 * @throws Exception
	 */
	public static void toSwf(String source, String destPath,String fileName) throws Exception{
		try {
			Converter converter = new PDF2SWF();
			converter.init();
			converter.convert(source, destPath, fileName);
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	
	public static Document2PdfConverter getDocument2PdfConverter() {
		if(document2PdfConverter ==null){
			synchronized (ConverterUtil.class) {
				if(document2PdfConverter ==null) {
					if(Config.getWpsVersion()==8){
						document2PdfConverter = new V8Document2PdfConverter();
					}else if(Config.getWpsVersion()==9){
						document2PdfConverter = new V9Document2PdfConverter();
					}
				}
			}
		}
		return document2PdfConverter;
	}

}
