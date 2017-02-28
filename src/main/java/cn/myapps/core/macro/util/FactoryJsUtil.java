/*
 * Created on 2005-10-27
 *
 *  To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cn.myapps.core.macro.util;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FactoryJsUtil {
  
  public Object createObject(String className) throws Exception {
    Class<?> cls = Class.forName(className);
    Object obj = cls.newInstance();
    return obj;
  }
  

}
