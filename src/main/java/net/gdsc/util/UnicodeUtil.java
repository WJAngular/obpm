
package net.gdsc.util;

import java.io.UnsupportedEncodingException;

/**
 * 编码转换类
 * 
 * @author 吴景
 * @since 1.0
 * @version 2016-05-30 吴景
 */
public class UnicodeUtil {
    
    /**
     * 将unicode编码转换成GBK
     * 
     * @param param unicode编码字符串
     * @return gbk编码字符串
     */
    public static String unicodeToGbk(String param) {
        if (param == null || "".equals(param)) {
            return param;
        }
        try {
            byte bGBKBytes[] = param.getBytes("GBK");
            return new String(bGBKBytes, "ISO8859_1");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        return null;
    }
}
