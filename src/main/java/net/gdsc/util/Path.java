
package net.gdsc.util;

import javax.servlet.ServletContext;

import org.apache.struts2.util.ServletContextAware;

/**
 * web路径
 * 
 * @author 吴景
 * @since 1.0
 * @version 2016-05-30 吴景
 */
public class Path implements ServletContextAware {
    
    /** physicalPath */
    private static String physicalPath;
    
    /** contextPath */
    private static String contextPath;
    
    @Override
    public void setServletContext(ServletContext servletContext) {
        
        physicalPath = servletContext.getRealPath("/");
        contextPath = servletContext.getContextPath();
        
        int iLastSlash = contextPath.lastIndexOf('/');
        if (iLastSlash != -1 && iLastSlash == contextPath.length() - 1) {
            contextPath = contextPath.substring(0, iLastSlash);
        }
    }
    
    /**
     * FIXME 方法注释信息(此标记由Eclipse自动生成,请填写注释信息删除此标记)
     * 
     * @return 相对路径
     */
    public static String getContextPath() {
        return contextPath;
    }
    
    /**
     * FIXME 方法注释信息(此标记由Eclipse自动生成,请填写注释信息删除此标记)
     * 
     * @return 物理路径
     */
    public static String getPhysicalPath() {
        return physicalPath;
    }
}
