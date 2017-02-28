package cn.myapps.core.security;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.myapps.constans.Environment;
import cn.myapps.core.security.action.LoginHelper;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.Security;
import cn.myapps.util.property.PropertyUtil;

/**
 * @author Happy
 *
 */
public class CookieAuthValidator {
	
	private static String COOKIE_NAME = null;
	private static boolean COOKIE_AUTO_LOGIN = false;
	private static int EXPIRES = 7*24*60*60*1000;

	static{
		try {
			COOKIE_NAME = Security.encodeToMD5(Environment.getMACAddress());
			PropertyUtil.load("security");
			COOKIE_AUTO_LOGIN = Boolean.parseBoolean(PropertyUtil.get("cfg.security.cookie_auto_login"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	public static WebUser valid(HttpServletRequest request,HttpServletResponse response){
		
		if(!COOKIE_AUTO_LOGIN) return null;
		Cookie cookie = getCookie(request);
		if(cookie == null) return null;
		
		String[] info = Security.decryptPassword(cookie.getValue()).split("\\|");
		if(info.length!=4) return null;
		String domainName = info[0];
		String loginno = info[1];
		String pw = info[2];
		long expires = Long.valueOf(info[3]);
		
		if(System.currentTimeMillis()>expires){
			destory(response);
			return null;
		}
		try {
			UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			UserVO user = userProcess.getUserByLoginnoAndDoaminName(loginno, domainName);
			if(user == null || !user.getLoginpwd().equals(pw)) return null;
			
			return LoginHelper.initWebUser(request, user, user.getDefaultApplication(), domainName);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void destory(HttpServletResponse response){
		if(!COOKIE_AUTO_LOGIN) return;
		Cookie cookie = new Cookie(COOKIE_NAME, "");
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}
	
	public static void addCookie(WebUser user,HttpServletResponse response){
		if(!COOKIE_AUTO_LOGIN) return;
		try {
			String value = user.getDomain().getName()+"|"+user.getLoginno()+"|"+user.getLoginpwd()+"|"+(System.currentTimeMillis()+EXPIRES);
			Cookie cookie = new Cookie(COOKIE_NAME, Security.encryptPassword(value));
			cookie.setPath("/");
			cookie.setMaxAge(EXPIRES);
			response.addCookie(cookie);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static Cookie getCookie(HttpServletRequest request){
		Cookie[] cookies = request.getCookies();
		if(cookies == null) return null;
		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			if(COOKIE_NAME.equals(cookie.getName())){
				return cookie;
			}
		}
		return null;
	}
	
	

}
