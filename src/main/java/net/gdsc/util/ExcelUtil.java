
package net.gdsc.util;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

/**
 * Excel操作工具类
 * 
 * @author 吴景
 * @since 1.0
 * @version 2014-04-29 吴景
 */
public class ExcelUtil {
    
    /**
     * 获取excel导出模版路径
     * 
     * @return excel模版路径
     */
    public static String getExcelTemplatePath(HttpServletRequest request) {
        return request.getSession().getServletContext().getRealPath("/")+ "gdsc"+File.separator+"resource" + File.separator + "exceltemplate" + File.separator;
    }
    
    /**
     * 获取excel临时文件存储路径
     * 
     * @return excel临时文件存储路径
     */
    public static String getExcelSavePath(HttpServletRequest request) {
        return request.getSession().getServletContext().getRealPath("/")+ "gdsc"+File.separator+"resource" + File.separator + "excelsavepath" + File.separator;
    }
    
}
