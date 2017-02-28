package cn.myapps.core.personalmessage.attachment.action;

import cn.myapps.core.email.runtime.mail.ConnectionProfile;
import cn.myapps.util.StringUtil;
import cn.myapps.util.property.PropertyUtil;


public class PersonalMessageConfig {
	
	//private static XmlParser xmlParser;
	private static ConnectionProfile profile;
	
	static {
		//URL xmlUrl = Thread.currentThread().getContextClassLoader().getResource("email-config.xml");
		//xmlParser = new XmlParser(xmlUrl.getFile());
		initConnectionProfile();
	}
	
	private PersonalMessageConfig() {
		
	}
	
	private static void initConnectionProfile() {
		profile = new ConnectionProfile();
		profile.setFetchPort(getString("fetch.server.port", "143"));
		profile.setFetchServer(getString("fetch.server", "127.0.0.1"));
		profile.setFetchSSL(getString("fetch.ssl", "false"));
		profile.setProtocol(getString("fetch.protocol", "imap"));
		profile.setShortName(getString("shortname", "localhost"));
		profile.setSmtpAuthenticated(getString("smtp.authenticated", "false"));
		profile.setSmtpPort(getString("smtp.server.port", "25"));
		profile.setSmtpServer(getString("smtp.server", "127.0.0.1"));
		profile.setSmtpSSL(getString("smtp.ssl", "false"));
		profile.setFolderNameSpace(getString("folder.namespace"));
	}
	
	public static String getString(String name) {
		String result = PropertyUtil.getByPropName("personalmessage", name);
		if (!StringUtil.isBlank(result)) {
			return result.trim();
		}
		return "";
	}
	
	public static String getString(String name, String defaultValue) {
		String result = getString(name);
		if (StringUtil.isBlank(result)) {
			result = defaultValue;
		}
		return result.trim();
	}
	
	public static int getInteger(String name, int defaultInt) {
		try {
			String result = getString(name);
			if (!StringUtil.isBlank(result)) {
				return Integer.parseInt(result);
			}
		} catch (Exception e) {
		}
		return defaultInt;
	}
	
	public static boolean getBoolean(String name, boolean defaultInt) {
		try {
			String result = getString(name);
			if (!StringUtil.isBlank(result)) {
				return Boolean.parseBoolean(result);
			}
		} catch (Exception e) {
		}
		return defaultInt;
	}

	
	public static ConnectionProfile getConnectionProfile() {
		return profile;
	}
	
}
