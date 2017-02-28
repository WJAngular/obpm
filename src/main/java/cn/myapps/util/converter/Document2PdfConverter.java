package cn.myapps.util.converter;

import java.io.File;
import java.io.FileNotFoundException;
import org.apache.log4j.Logger;

/**
 * PDF文档转换器
 * 
 * @author xiuwei
 * 
 */
public abstract class Document2PdfConverter implements Converter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6650214905685005929L;

	Logger log = Logger.getLogger(getClass());

	public void convert(String source, String destPath, String fileName)
			throws Exception {

		File destDir = new File(destPath);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}

		if (!new File(source).exists()) {
			log.error(String.format("文档[%s]转换PDF失败，因为找不到物理文件%s", fileName,
					source));
			new FileNotFoundException(String.format("文档不存在%s", source));
		}

		String errorFieldPath = destPath
				+ fileName.substring(0, fileName.lastIndexOf(".") + 1) + "err";

		try {

			String format = getFileExtensionName(source.toLowerCase());
			if (format.equals("doc") || format.equals("docx")
					|| format.equals("wps") || format.equals("html")
					|| format.equals("htm") || format.equals("rtf")
					|| format.equals("txt")) {
				word2Pdf(source, destPath, fileName);
			} else if (format.equals("xls") || format.equals("xlsx")
					|| format.equals("et")) {
				excel2Pdf(source, destPath, fileName);
			} else if (format.equals("ppt") || format.equals("pptx")
					|| format.equals("dps") || format.equals("pot")
					|| format.equals("pps")) {
				ppt2Pdf(source, destPath, fileName);
			}

			new File(errorFieldPath).deleteOnExit();
		} catch (Exception e) {
			new File(errorFieldPath).createNewFile();
			log.error(String.format("文档[%s]转换PDF失败，物理文件可能被加密或已经损坏！[%s]",
					fileName, source));
			throw new RuntimeException(String.format(
					"文档[%s]转换PDF失败，物理文件可能被加密或已经损坏![%s]", fileName, source), e);
		}

	}

	public abstract void word2Pdf(String source, String destPath,
			String fileName) throws Exception;

	public abstract void excel2Pdf(String source, String destPath,
			String fileName) throws Exception;

	public abstract void ppt2Pdf(String source, String destPath, String fileName)
			throws Exception;

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.km.util.converter.Converter#init()
	 */
	public void init() throws Exception {
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

}
