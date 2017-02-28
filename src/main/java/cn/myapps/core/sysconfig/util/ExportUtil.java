package cn.myapps.core.sysconfig.util;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import cn.myapps.core.sysconfig.ejb.AuthConfig;
import cn.myapps.core.sysconfig.ejb.CheckoutConfig;
import cn.myapps.core.sysconfig.ejb.EmailConfig;
import cn.myapps.core.sysconfig.ejb.ImConfig;
import cn.myapps.core.sysconfig.ejb.LdapConfig;
import cn.myapps.core.sysconfig.ejb.LoginConfig;
import cn.myapps.core.sysconfig.ejb.SysConfigProcess;
import cn.myapps.core.sysconfig.ejb.SysConfigProcessBean;

public class ExportUtil {

	
	public static String getFile() {
		URL url = ExportUtil.class.getClassLoader().getResource("sysConfig.xml");
		if(url != null)
			return url.getFile();
		return null;
	}
	
	public static void export() throws Exception {
		SysConfigProcess process = new SysConfigProcessBean();
		AuthConfig authConfig = process.getAuthConfig();
		LdapConfig ldapConfig = process.getLdapConfig();
		EmailConfig emailConfig = process.getEmailConfig();
		ImConfig imConfig = process.getImConfig();
		CheckoutConfig checkoutConfig = process.getCheckoutConfig();
		LoginConfig loginConfig = process.getLoginConfig();
		
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("sysConfig");
		initDocument(root, authConfig);
		initDocument(root, ldapConfig);
		initDocument(root, emailConfig);
		initDocument(root,imConfig);
		initDocument(root,checkoutConfig);
		initDocument(root,loginConfig);
		OutputFormat format = OutputFormat.createPrettyPrint();
		String ssoFile = SysConfigProcessBean.class.getClassLoader().getResource("sso.properties").getFile();
		String sysConfigFile = ssoFile.replaceAll("sso[.]properties","sysConfig.xml");
		File file = new File(sysConfigFile);
		if (!file.exists()) {
			if (!file.createNewFile()) {
				System.out.println("create xml file failed!");
			}
		}
		XMLWriter writer = new XMLWriter(new FileWriter(file), format);
		writer.write(document);
		writer.close();
	}

	private static void initDocument(final Element root, final Object config)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		if (config != null && root != null) {
			Element e_perant = root.addElement(config.getClass().getName());
			Class<?> clazz = config.getClass();
			Field[] fields = clazz.getDeclaredFields();
			if (fields != null) {
				for (int i = 0; i < fields.length; i++) {
					StringBuffer getter = new StringBuffer("get");
					if (Modifier.isPrivate(fields[i].getModifiers())
							&& !Modifier.isStatic(fields[i].getModifiers())
							&& !Modifier.isFinal(fields[i].getModifiers())) {
						String fieldName = fields[i].getName();
						if (fieldName != null) {
							if (fieldName.length() > 0) {
								String first = fieldName.substring(0, 1)
										.toUpperCase();
								getter.append(first);
							}
							if (fieldName.length() > 1) {
								getter.append(fieldName.substring(1));
							}
						}
						Method method_get = clazz.getMethod(getter.toString(),
								new Class<?>[] {});
						String value = String.valueOf(method_get.invoke(config,new Object[] {}));
						Element e_child = e_perant.addElement(fieldName);// .addAttribute("value",
						// value)
						e_child.setText(value != null ? value : "");
					}
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		ExportUtil.export();
	}
}
