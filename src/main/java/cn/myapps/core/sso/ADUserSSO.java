package cn.myapps.core.sso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jcifs.Config;
import jcifs.UniAddress;
import jcifs.http.NtlmSsp;
import jcifs.smb.NtlmChallenge;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbAuthException;
import jcifs.smb.SmbSession;
import jcifs.util.Base64;
import cn.myapps.constans.Web;
import cn.myapps.core.sysconfig.ejb.AuthConfig;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.property.PropertyUtil;

/**
 * @author xiuwei
 * 
 */
public class ADUserSSO implements SSO {

	private String defaultDomain;
	private String domainController;
	private boolean loadBalance;
	private boolean enableBasic;
	private boolean insecureBasic;
	private String realm;

	public Map<String, String> authenticateUser(HttpServletRequest req,
			HttpServletResponse resp) {

		initJcifsConfig();
		NtlmPasswordAuthentication ntlm = null;
		Map<String, String> userInfo = new HashMap<String, String>();
		try {
			ntlm = negotiate(req, resp, false);
			if (ntlm != null) {
				userInfo
						.put(Web.SSO_LOGINACCOUNT_ATTRIBUTE, ntlm.getUsername());
				
				userInfo.put(Web.SSO_DOMAINNAME_ATTRIBUTE, PropertyUtil.get(AuthConfig.AD_LOGIN_DOMAIN));// 获取配置的企业域名称设置进来
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}

		return userInfo;
	}

	public Map<String, String> authenticateUser(PortletRequest req) {
		/** 目前还不支持portlet **/
		throw new RuntimeException(
				"Currently does not support portlet interface");
	}

	/**
	 * 从AD服务器验证、登录并返回登录凭据
	 * 
	 * @param req
	 * @param resp
	 * @param skipAuthentication
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @author xiuwei
	 */
	protected NtlmPasswordAuthentication negotiate(HttpServletRequest req,
			HttpServletResponse resp, boolean skipAuthentication)
			throws IOException, ServletException {
		UniAddress dc;
		String msg;
		NtlmPasswordAuthentication ntlm = null;
		msg = req.getHeader("Authorization");
//		boolean offerBasic = enableBasic && (insecureBasic || req.isSecure());

		if (msg != null
				&& (msg.startsWith("NTLM ") || msg
						.startsWith("Basic "))) {
			if (msg.startsWith("NTLM ")) {
				HttpSession ssn = req.getSession();
				byte[] challenge;

				if (loadBalance) {
					NtlmChallenge chal = (NtlmChallenge) ssn
							.getAttribute("NtlmHttpChal");
					if (chal == null) {
						chal = SmbSession.getChallengeForDomain();
						ssn.setAttribute("NtlmHttpChal", chal);
					}
					dc = chal.dc;
					challenge = chal.challenge;
				} else {
					dc = UniAddress.getByName(domainController, true);
					challenge = SmbSession.getChallenge(dc);
				}

				if ((ntlm = NtlmSsp.authenticate(req, resp, challenge)) == null) {
					return null;
				}
				//negotiation complete, remove the challenge object
				ssn.removeAttribute("NtlmHttpChal");
			} else {
				String auth = new String(Base64.decode(msg.substring(6)),
						"US-ASCII");
				int index = auth.indexOf(':');
				String user = (index != -1) ? auth.substring(0, index) : auth;
				String password = (index != -1) ? auth.substring(index + 1)
						: "";
				index = user.indexOf('\\');
				if (index == -1)
					index = user.indexOf('/');
				String domain = (index != -1) ? user.substring(0, index)
						: defaultDomain;
				user = (index != -1) ? user.substring(index + 1) : user;
				ntlm = new NtlmPasswordAuthentication(domain, user, password);
				dc = UniAddress.getByName(domainController, true);
			}
			try {

				SmbSession.logon(dc, ntlm);

			} catch (SmbAuthException sae) {
				if (sae.getNtStatus() == sae.NT_STATUS_ACCESS_VIOLATION) {
					HttpSession ssn = req.getSession(false);
					if (ssn != null) {
						ssn.removeAttribute("NtlmHttpAuth");
					}
				}
//				resp.setHeader("WWW-Authenticate", "NTLM");
//				if (offerBasic) {
					resp.addHeader("WWW-Authenticate", "Basic realm=\"" + realm
							+ "\"");
//				}
				resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				resp.setContentLength(0); 
				resp.flushBuffer();
				return null;
			}
			req.getSession().setAttribute("NtlmHttpAuth", ntlm);
		} else {
			if (!skipAuthentication) {
				HttpSession ssn = req.getSession(false);
				if (ssn == null
						|| (ntlm = (NtlmPasswordAuthentication) ssn
								.getAttribute("NtlmHttpAuth")) == null) {
//					resp.setHeader("WWW-Authenticate", "NTLM");
//					if (offerBasic) {
						resp.addHeader("WWW-Authenticate", "Basic realm=\""
								+ realm + "\"");
//					}
					resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					resp.setContentLength(0);
					resp.flushBuffer();
					return null;
				}
			}
		}

		return ntlm;
	}
	
	/**
	 * 从AD服务器验证、登录并返回登录凭据
	 * 
	 * @param req
	 * @param resp
	 * @param skipAuthentication
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @author xiuwei
	 */
	
	/*
	 * 
	protected NtlmPasswordAuthentication negotiate(HttpServletRequest req,
			HttpServletResponse resp, boolean skipAuthentication)
			throws IOException, ServletException {
	     UniAddress dc;
	        String msg;
	        NtlmPasswordAuthentication ntlm = null;
	        msg = req.getHeader( "Authorization" );
	        loadBalance = false;
	        enableBasic = true;
	        boolean offerBasic = enableBasic && (insecureBasic || req.isSecure());

	        if( msg != null && (msg.startsWith( "NTLM " ) ||
	                    (offerBasic && msg.startsWith("Basic ")))) {
	            if (msg.startsWith("NTLM ")) {
	                HttpSession ssn = req.getSession();
	                byte[] challenge;

	                if( loadBalance ) {
	                    NtlmChallenge chal = (NtlmChallenge)ssn.getAttribute( "NtlmHttpChal" );
	                    if( chal == null ) {
	                        chal = SmbSession.getChallengeForDomain();
	                        ssn.setAttribute( "NtlmHttpChal", chal );
	                    }
	                    dc = chal.dc;
	                    challenge = chal.challenge;
	                } else {
	                    dc = UniAddress.getByName( domainController, true );
	                    challenge = SmbSession.getChallenge( dc );
	                }

	                if(( ntlm = NtlmSsp.authenticate( req, resp, challenge )) == null ) {
	                    return null;
	                }
	                // negotiation complete, remove the challenge object 
	                ssn.removeAttribute( "NtlmHttpChal" );
	            } else {
	                String auth = new String(Base64.decode(msg.substring(6)),
	                        "US-ASCII");
	                int index = auth.indexOf(':');
	                String user = (index != -1) ? auth.substring(0, index) : auth;
	                String password = (index != -1) ? auth.substring(index + 1) :
	                        "";
	                index = user.indexOf('\\');
	                if (index == -1) index = user.indexOf('/');
	                String domain = (index != -1) ? user.substring(0, index) :
	                        defaultDomain;
	                user = (index != -1) ? user.substring(index + 1) : user;
	                ntlm = new NtlmPasswordAuthentication(domain, user, password);
	                dc = UniAddress.getByName( domainController, true );
	            }
	            try {

	                SmbSession.logon( dc, ntlm );

//	                if( log.level > 2 ) {
//	                    log.println( "NtlmHttpFilter: " + ntlm +
//	                            " successfully authenticated against " + dc );
//	                }
	            } catch( Exception sae ) {
	            	//SmbAuthException sae
	            	
	            	if(sae instanceof SmbAuthException){
	            	if(ntlm !=null && !StringUtil.isBlank(ntlm.getUsername())){//是否自动传递客户端本机用户名
                		String userName = ntlm.getUsername();
                		int index = userName.indexOf('\\');
     	                if (index == -1) index = userName.indexOf('/');
                		String loginno = userName.substring(index+1);
                		
                		try {
							UserProcess process = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
							String domainName = PropertyUtil.get(AuthConfig.AD_LOGIN_DOMAIN);
							UserVO user = process.getUserByLoginnoAndDoaminName(loginno, domainName);
							if(user !=null){
								return new NtlmPasswordAuthentication("", user.getLoginno(), "");
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
                		
                	}
	            	
		                if( ((SmbAuthException)sae).getNtStatus() == SmbAuthException.NT_STATUS_ACCESS_VIOLATION ) {
		                	
		                    HttpSession ssn = req.getSession(false);
		                    if (ssn != null) {
		                        ssn.removeAttribute( "NtlmHttpAuth" );
		                    }
		                }
	            	}
	               // resp.setHeader( "WWW-Authenticate", "NTLM" );
	               // if (offerBasic) {
	                resp.addHeader( "WWW-Authenticate", "Basic realm=\"" +
	                            realm + "\"");
	                //}
	                resp.setStatus( HttpServletResponse.SC_UNAUTHORIZED );
	                resp.setContentLength(0);
	                resp.flushBuffer();
	                return null;
	            }
	            req.getSession().setAttribute( "NtlmHttpAuth", ntlm );
	        } else {
	            if (!skipAuthentication) {
	                HttpSession ssn = req.getSession(false);
	                if (ssn == null || (ntlm = (NtlmPasswordAuthentication)
	                            ssn.getAttribute("NtlmHttpAuth")) == null) {
	                    resp.setHeader( "WWW-Authenticate", "NTLM" );
	                    if (offerBasic) {
	                        resp.addHeader( "WWW-Authenticate", "Basic realm=\"" +
	                                realm + "\"");
	                    }
	                    resp.setStatus( HttpServletResponse.SC_UNAUTHORIZED );
	                    resp.setContentLength(0);
	                    resp.flushBuffer();
	                    return null;
	                }
	            }
	        }

	        return ntlm;
	}
	*/
	

	private void initJcifsConfig() {
		/*
		 * Set jcifs properties we know we want; soTimeout and cachePolicy to
		 * 30min.
		 */
		Config.setProperty("jcifs.smb.client.soTimeout", "1800000");
		Config.setProperty("jcifs.netbios.cachePolicy", "1200");
		/*
		 * The Filter can only work with NTLMv1 as it uses a man-in-the-middle
		 * techinque that NTLMv2 specifically thwarts. A real NTLM Filter would
		 * need to do a NETLOGON RPC that JCIFS will likely never implement
		 * because it requires a lot of extra crypto not used by CIFS.
		 */

		Config.setProperty("jcifs.smb.lmCompatibility", "0");
		Config.setProperty("jcifs.smb.client.useExtendedSecurity", "false");
		// 获取配置的AD域管理员账号和密码
		Config.setProperty("jcifs.smb.client.username", PropertyUtil.get(AuthConfig.AD_ADMIN_LOGINNO));
		Config.setProperty("jcifs.smb.client.password", PropertyUtil.get(AuthConfig.AD_ADMIN_LOGINNOPW));
		
		Config.setProperty("jcifs.util.loglevel", "0");
		Config.setProperty("jcifs.smb.client.soTimeout", "200");
		Config.setProperty("jcifs.smb.client.useExtendedSecurity", "false");
		Config.setProperty("jcifs.http.domainController", PropertyUtil
				.get(Web.SSO_AD_DOMAINCONTROLLER));
		Config.setProperty("jcifs.smb.client.domain", PropertyUtil
				.get(Web.SSO_AD_DEFAULTDOMAIN));

		defaultDomain = Config.getProperty("jcifs.smb.client.domain");
		domainController = Config.getProperty("jcifs.http.domainController");
		if (domainController == null) {
			domainController = defaultDomain;
			loadBalance = Config.getBoolean("jcifs.http.loadBalance", true);
		}
		enableBasic = Boolean.valueOf(
				Config.getProperty("jcifs.http.enableBasic")).booleanValue();
		insecureBasic = Boolean.valueOf(
				Config.getProperty("jcifs.http.insecureBasic")).booleanValue();
		realm = Config.getProperty("jcifs.http.basicRealm");
		if (realm == null)
			realm = "jCIFS";
	}

}
