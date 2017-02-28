package cn.myapps.core.sysconfig.ejb;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

import cn.myapps.km.base.init.Initiator;
import cn.myapps.km.util.NDataSource;
import cn.myapps.km.util.PersistenceUtils;
import cn.myapps.util.StringUtil;
import cn.myapps.util.property.PropertyUtil;

public class SysConfigProcessBean implements SysConfigProcess {
	
	
	
	public KmConfig getKmConfig() throws Exception {
		KmConfig kmConfig = new KmConfig();
		PropertyUtil.reload("km");
		kmConfig.setEnable((PropertyUtil.get(KmConfig.ENABLE)).equals("true"));
		kmConfig.setDbType(PropertyUtil.get(KmConfig.DB_TYPE));
		kmConfig.setDriverClass(PropertyUtil.get(KmConfig.DERVER_CLASS));
		kmConfig.setUrl(PropertyUtil.get(KmConfig.DERVER_URL));
		kmConfig.setUsername(PropertyUtil.get(KmConfig.USER));
		kmConfig.setPassword(PropertyUtil.get(KmConfig.PASSWORD));
		kmConfig.setPoolsize(PropertyUtil.get(KmConfig.POOL_SIZE));
		kmConfig.setTimeout(PropertyUtil.get(KmConfig.TIME_OUT));
		return kmConfig;
	}

	public AuthConfig getAuthConfig() throws Exception {
		AuthConfig authConfig = new AuthConfig();
		PropertyUtil.reload("sso");
		authConfig
				.setAuthType(PropertyUtil.get(AuthConfig.AUTHENTICATION_TYPE));
		authConfig.setLoginAuth(PropertyUtil
				.get(AuthConfig.LOGIN_AUTHENTICATION));
		authConfig.setSsoAuth(PropertyUtil.get(AuthConfig.SSO_IMLEMENTATION));
		authConfig.setSsoDefaultEmail(PropertyUtil
				.get(AuthConfig.SSO_DEFAULT_EMAIL));
		authConfig.setSsoDefaultPassword(PropertyUtil
				.get(AuthConfig.SSO_DEFAULT_PASSWORD));
		authConfig.setSsoRedirect(PropertyUtil.get(AuthConfig.SSO_REDIRECT));
		authConfig.setSsoLogoutRedirect(PropertyUtil
				.get(AuthConfig.SSO_LOGOUT_REDIRECT));
		authConfig.setCasLoginUrl(PropertyUtil
				.get(AuthConfig.CAS_SERVER_LOGIN_URL));
		authConfig.setCasUrlPrefix(PropertyUtil
				.get(AuthConfig.CAS_SERVER_URL_PREFIX));
		authConfig.setLocalServerName(PropertyUtil
				.get(AuthConfig.LOCAL_SERVER_NAME));
		authConfig.setSsoSaveType(PropertyUtil
				.get(AuthConfig.SSO_INFO_SAVE_TYPE));
		authConfig.setSsoKeyLoginAccount(PropertyUtil
				.get(AuthConfig.SSO_INFO_KEY_LOGINACCOUNT));
		authConfig.setSsoKeyPassword(PropertyUtil
				.get(AuthConfig.SSO_INFO_KEY_PASSWORD));
		authConfig.setSsoKeyDomain(PropertyUtil
				.get(AuthConfig.SSO_INFO_KEY_DOMAINNAME));
		authConfig.setSsoKeyEmail(PropertyUtil
				.get(AuthConfig.SSO_INFO_KEY_EMAIL));
		authConfig.setSsoDataEncryption(PropertyUtil
				.get(AuthConfig.SSO_INFO_DATA_ENCRYPTION));
		authConfig.setAdDefaultDomain(PropertyUtil
				.get(AuthConfig.AD_DEFAULT_DOMAIN));
		authConfig.setAdDomainController(PropertyUtil
				.get(AuthConfig.AD_DOMAIN_CONTROLLER));
		authConfig.setSmsAuthenticate(PropertyUtil
				.get(AuthConfig.SMS_AUTHENTICATE));
		authConfig.setSmsTimeout(PropertyUtil
				.get(AuthConfig.SMS_TIMEOUT));
		authConfig.setSmsContent(PropertyUtil
				.get(AuthConfig.SMS_CONTENT));
		authConfig.setSmsAffectMode(PropertyUtil
				.get(AuthConfig.SMS_AFFECTMODE));
		authConfig.setSmsStartRangeIp(PropertyUtil
				.get(AuthConfig.SMS_STARTRANGEIP));
		authConfig.setSmsEndRangeIp(PropertyUtil
				.get(AuthConfig.SMS_ENDRANGEIP));
		authConfig.setUsbkeyAuthenticate(PropertyUtil
				.get(AuthConfig.UK_AUTHENTICATE));
		authConfig.setUsbkeyAffectMode(PropertyUtil
				.get(AuthConfig.UK_AFFECTMODE));
		authConfig.setUsbkeyRangeIps(PropertyUtil
				.get(AuthConfig.UK_RANGEIPS));
		authConfig.setAdadminloginno(PropertyUtil
				.get(AuthConfig.AD_ADMIN_LOGINNO));
		authConfig.setAdadminloginnopw(PropertyUtil
				.get(AuthConfig.AD_ADMIN_LOGINNOPW));
		authConfig.setAdlogindomain(PropertyUtil
				.get(AuthConfig.AD_LOGIN_DOMAIN));
		return authConfig;
	}

	public EmailConfig getEmailConfig() throws Exception {
		EmailConfig emailConfig = new EmailConfig();
		PropertyUtil.reload("email");
		emailConfig.setSendHost(PropertyUtil.get(EmailConfig.EMAIL_SEND_HOST));
		emailConfig.setSendAddress(PropertyUtil
				.get(EmailConfig.EMAIL_SEND_ADDRESS));
		emailConfig.setSendAccount(PropertyUtil.get(EmailConfig.EMAIL_USER));
		emailConfig.setSendPassword(PropertyUtil
				.get(EmailConfig.EMAIL_SEND_PASSWORD));
		emailConfig
				.setCcAddress(PropertyUtil.get(EmailConfig.EMAIL_CC_ADDRESS));
		emailConfig.setIsUseClient(PropertyUtil
				.get(EmailConfig.USE_EMAIL_CLIENT));
		emailConfig.setIsUseInnerEmail(PropertyUtil
				.get(EmailConfig.USE_INNER_EMAIL));
		emailConfig.setFunctionDomain(PropertyUtil
				.get(EmailConfig.EMAIL_FUNCTION_DOAMIN));
		emailConfig.setTrash(PropertyUtil.get(EmailConfig.EMAIL_TRASH));
		emailConfig.setSender(PropertyUtil.get(EmailConfig.EMAIL_SENDER));
		emailConfig.setDraft(PropertyUtil.get(EmailConfig.EMAIL_DRAFT));
		emailConfig.setRemoved(PropertyUtil.get(EmailConfig.EMAIL_REMOVED));
		emailConfig.setFetchServer(PropertyUtil
				.get(EmailConfig.EMAIL_RECEIVE_SERVER));
		emailConfig.setFetchServerPort(PropertyUtil
				.get(EmailConfig.EMAIL_RECEIVE_SERVER_PORT));
		emailConfig.setFetchProtocol(PropertyUtil
				.get(EmailConfig.EMAIL_RECEIVE_PROTOCOL));
		emailConfig.setFetchssl(PropertyUtil
				.get(EmailConfig.EMAIL_RECEIVE_NEED_CERTIFICATE));
		emailConfig.setSmtpServer(PropertyUtil
				.get(EmailConfig.EMAIL_SEND_SERVER));
		emailConfig.setSmtpServerPort(PropertyUtil
				.get(EmailConfig.EMAIL_SEND_SERVER_PORT));
		emailConfig.setSmtpAuthenticated(PropertyUtil
				.get(EmailConfig.EMAIL_ENABLE_ACCESSOORIES));
		emailConfig.setSmtpssl(PropertyUtil
				.get(EmailConfig.EMAIL_SEND_NEED_CERTIFICATE));
		return emailConfig;
	}

	public LdapConfig getLdapConfig() throws Exception {
		LdapConfig ldapConfig = new LdapConfig();
		PropertyUtil.reload("sso");
		ldapConfig.setUrl(PropertyUtil.get(LdapConfig.LDAP_URL));
		ldapConfig.setBaseDN(PropertyUtil.get(LdapConfig.LDAP_BASEDN));
		ldapConfig.setPooled(PropertyUtil.get(LdapConfig.LDAP_POOLED));
		ldapConfig.setDirStructure(PropertyUtil.get(LdapConfig.DIRSTRUCTURE));
		ldapConfig.setId_(PropertyUtil.get(LdapConfig.ID));
		ldapConfig.setLoginno_(PropertyUtil.get(LdapConfig.LOGINNO));
		ldapConfig.setLoginpwd_(PropertyUtil.get(LdapConfig.LOGINPWD));
		ldapConfig.setName_(PropertyUtil.get(LdapConfig.NAME));
		ldapConfig.setEmail_(PropertyUtil.get(LdapConfig.EMAIL));
		ldapConfig.setTelephone_(PropertyUtil.get(LdapConfig.TELEPHONE));
		ldapConfig.setManager(PropertyUtil.get(LdapConfig.MANAGER));
		ldapConfig.setManagerPassword(PropertyUtil.get(LdapConfig.MANAGERPASSWORD));
		ldapConfig.setUserClass(PropertyUtil.get(LdapConfig.USERCLASS));
		ldapConfig.setDeptClass(PropertyUtil.get(LdapConfig.DEPTCALSS));
		ldapConfig.setEnterDept(PropertyUtil.get(LdapConfig.ENTERDEPT));
		ldapConfig.setDomain(PropertyUtil.get(LdapConfig.DOMAIN));
		return ldapConfig;
	}
	
	/**
	 * 获取IM的配置
	 * @return
	 * @throws Exception
	 */
	public ImConfig getImConfig() throws Exception{
		ImConfig imConfig = new ImConfig();
		PropertyUtil.reload("im");
		imConfig.setOpen(PropertyUtil.get(ImConfig.GKE_API_OPEN));
		imConfig.setIp(PropertyUtil.get(ImConfig.GKE_API_IP));
		imConfig.setPort(PropertyUtil.get(ImConfig.GKE_SERVER_PORT));
		imConfig.setLoginno(PropertyUtil.get(ImConfig.GKE_SERVER_LOGINNO));
		imConfig.setPassword(PropertyUtil.get(ImConfig.GKE_SERVER_PASSWORD));
		return imConfig;
	}
	public LoginConfig getLoginConfig() throws Exception{
		LoginConfig loginConfig = new LoginConfig();
		PropertyUtil.reload("passwordLegal");
		loginConfig.setLength(PropertyUtil.get(LoginConfig.LOGIN_PASSWORD_LENGTH));
		loginConfig.setLegal(PropertyUtil.get(LoginConfig.LOGIN_PASSWORD_LEGAL));
		loginConfig.setMaxUpdateTimes(PropertyUtil.get(LoginConfig.LOGIN_PASSWORD_UPADTE_TIMES));
		loginConfig.setNoticeTime(PropertyUtil.get(LoginConfig.LOGIN_UPDATE_NOTICE));
		loginConfig.setMaxAge(PropertyUtil.get(LoginConfig.LOGIN_PASSWOR_MAXAGE));
		loginConfig.setFailLoginTimes(PropertyUtil.get(LoginConfig.LOGIN_FAIL_TIMES));
		loginConfig.setNoticeMethod(PropertyUtil.get(LoginConfig.LOGIN_NOTICE_METHOD));
		loginConfig.setNoticeAuthor(PropertyUtil.get(LoginConfig.LOGIN_NOTICE_AUTHOR));
		loginConfig.setNoticeContent(PropertyUtil.get(LoginConfig.LOGIN_NOTICE_CONTENT));
		return loginConfig;
		}
	
	public CheckoutConfig getCheckoutConfig() throws Exception {
		CheckoutConfig checkoutConfig = new CheckoutConfig();
		PropertyUtil.reload("checkout");
		checkoutConfig.setInvocation((PropertyUtil.get(CheckoutConfig.INVOCATION)).equals("true"));
		return checkoutConfig;
	}

	public void save(AuthConfig authConfig, LdapConfig ldapConfig,
			EmailConfig emailConfig,ImConfig imConfig,CheckoutConfig checkoutConfig,LoginConfig loginConfig,KmConfig kmConfig) throws IOException, URISyntaxException {
		Properties authConfigProperties = new Properties();
		Properties emailConfigProperties = new Properties();
		/**
		 * IM
		 */
		Properties imConfigProperties = new Properties();
		/**
		 * checkoutConfig
		 */
		Properties checkoutConfigProperties = new Properties();
		
		/**
		 * loginConfig
		 */
		Properties loginConfigConfigProperties = new Properties();
		
		Properties kmConfigConfigProperties = new Properties();
		
		String ssoPath = SysConfigProcessBean.class.getClassLoader().getResource("sso.properties").toURI().getPath();
		String emailPath = SysConfigProcessBean.class.getClassLoader().getResource("email.properties").toURI().getPath();
		/**
		 * IM
		 */
		String imPath = SysConfigProcessBean.class.getClassLoader().getResource("im.properties").toURI().getPath();
		String checkoutPath = SysConfigProcessBean.class.getClassLoader().getResource("checkout.properties").toURI().getPath();
		/**
		 * LoginConfig
		 */
		String passwordLegalPath = SysConfigProcessBean.class.getClassLoader().getResource("passwordLegal.properties").toURI().getPath();
		String kmPath = SysConfigProcessBean.class.getClassLoader().getResource("km.properties").toURI().getPath();
		
		if (authConfig != null && ldapConfig != null && ssoPath != null) {
			// 身份验证设置
			authConfigProperties.setProperty(AuthConfig.AUTHENTICATION_TYPE, authConfig
					.getAuthType() != null ? authConfig.getAuthType() : "");
			authConfigProperties.setProperty(AuthConfig.LOGIN_AUTHENTICATION,
					!StringUtil.isBlank(authConfig.getLoginAuth()) ? authConfig
							.getLoginAuth() : AuthConfig
							.getLoginAuth("default"));
			authConfigProperties.setProperty(AuthConfig.SSO_IMLEMENTATION, authConfig
					.getSsoAuth() != null ? authConfig.getSsoAuth() : "");
			authConfigProperties.setProperty(AuthConfig.SSO_DEFAULT_EMAIL, authConfig
					.getSsoDefaultEmail() != null ? authConfig
					.getSsoDefaultEmail() : "");
			authConfigProperties.setProperty(AuthConfig.SSO_DEFAULT_PASSWORD, authConfig
					.getSsoDefaultPassword() != null ? authConfig
					.getSsoDefaultPassword() : "");
			authConfigProperties.setProperty(AuthConfig.SSO_REDIRECT, authConfig
					.getSsoRedirect() != null ? authConfig.getSsoRedirect()
					: "");
			authConfigProperties.setProperty(AuthConfig.SSO_LOGOUT_REDIRECT, authConfig
					.getSsoLogoutRedirect() != null ? authConfig
					.getSsoLogoutRedirect() : "");
			authConfigProperties.setProperty(AuthConfig.CAS_SERVER_LOGIN_URL, authConfig
					.getCasLoginUrl() != null ? authConfig.getCasLoginUrl()
					: "");
			authConfigProperties.setProperty(AuthConfig.CAS_SERVER_URL_PREFIX,
					authConfig.getCasUrlPrefix() != null ? authConfig
							.getCasUrlPrefix() : "");
			authConfigProperties.setProperty(AuthConfig.LOCAL_SERVER_NAME, authConfig
					.getLocalServerName() != null ? authConfig
					.getLocalServerName() : "");
			authConfigProperties.setProperty(AuthConfig.SSO_INFO_SAVE_TYPE, authConfig
					.getSsoSaveType() != null ? authConfig.getSsoSaveType()
					: "");
			authConfigProperties.setProperty(AuthConfig.SSO_INFO_KEY_LOGINACCOUNT,
					authConfig.getSsoKeyLoginAccount() != null ? authConfig
							.getSsoKeyLoginAccount() : "");
			authConfigProperties.setProperty(AuthConfig.SSO_INFO_KEY_PASSWORD,
					authConfig.getSsoKeyPassword() != null ? authConfig
							.getSsoKeyPassword() : "");
			authConfigProperties.setProperty(AuthConfig.SSO_INFO_KEY_DOMAINNAME,
					authConfig.getSsoKeyDomain() != null ? authConfig
							.getSsoKeyDomain() : "");
			authConfigProperties.setProperty(AuthConfig.SSO_INFO_KEY_EMAIL, authConfig
					.getSsoKeyEmail() != null ? authConfig.getSsoKeyEmail()
					: "");
			authConfigProperties.setProperty(AuthConfig.SSO_INFO_DATA_ENCRYPTION,
					authConfig.getSsoDataEncryption() != null ? authConfig
							.getSsoDataEncryption() : "");
			authConfigProperties.setProperty(AuthConfig.AD_DOMAIN_CONTROLLER,
					authConfig.getAdDomainController() != null ? authConfig
							.getAdDomainController() : "");
			authConfigProperties.setProperty(AuthConfig.AD_DEFAULT_DOMAIN,
					authConfig.getAdDefaultDomain() != null ? authConfig
							.getAdDefaultDomain() : "");
			authConfigProperties.setProperty(AuthConfig.AD_ADMIN_LOGINNO,
					authConfig.getAdadminloginno() != null ? authConfig
							.getAdadminloginno() : "");
			authConfigProperties.setProperty(AuthConfig.AD_ADMIN_LOGINNOPW,
					authConfig.getAdadminloginnopw() != null ? authConfig
							.getAdadminloginnopw() : "");
			authConfigProperties.setProperty(AuthConfig.AD_LOGIN_DOMAIN,
					authConfig.getAdlogindomain() != null ? authConfig
							.getAdlogindomain(): "");
			
			authConfigProperties.setProperty(AuthConfig.SMS_AUTHENTICATE,
					authConfig.getSmsAuthenticate() != null ? authConfig
							.getSmsAuthenticate() : "false");
			authConfigProperties.setProperty(AuthConfig.SMS_TIMEOUT,
					authConfig.getSmsTimeout() != null ? authConfig
							.getSmsTimeout() : "0");
			authConfigProperties.setProperty(AuthConfig.SMS_CONTENT,
					authConfig.getSmsContent() != null ? authConfig
							.getSmsContent() : "");
			authConfigProperties.setProperty(AuthConfig.SMS_AFFECTMODE,
					authConfig.getSmsAffectMode() != null ? authConfig
							.getSmsAffectMode() : "");
			authConfigProperties.setProperty(AuthConfig.SMS_STARTRANGEIP,
					authConfig.getSmsStartRangeIp() != null ? authConfig
							.getSmsStartRangeIp() : "");
			authConfigProperties.setProperty(AuthConfig.SMS_ENDRANGEIP,
					authConfig.getSmsEndRangeIp() != null ? authConfig
							.getSmsEndRangeIp() : "");
			
			authConfigProperties.setProperty(AuthConfig.UK_AUTHENTICATE,
					authConfig.getUsbkeyAuthenticate() != null ? authConfig
							.getUsbkeyAuthenticate() : "false");
			authConfigProperties.setProperty(AuthConfig.UK_AFFECTMODE,
					authConfig.getUsbkeyAffectMode() != null ? authConfig
							.getUsbkeyAffectMode() : "");
			authConfigProperties.setProperty(AuthConfig.UK_RANGEIPS,
					authConfig.getUsbkeyRangeIps() != null ? authConfig
							.getUsbkeyRangeIps() : "");
			
			// ldap的设置
			authConfigProperties.setProperty(LdapConfig.LDAP_URL,
					ldapConfig.getUrl() != null ? ldapConfig.getUrl() : "");
			authConfigProperties.setProperty(LdapConfig.LDAP_BASEDN, ldapConfig
					.getBaseDN() != null ? ldapConfig.getBaseDN() : "");
			authConfigProperties.setProperty(LdapConfig.LDAP_POOLED, ldapConfig
					.getPooled() != null ? ldapConfig.getPooled() : "");
			authConfigProperties.setProperty(LdapConfig.DIRSTRUCTURE, ldapConfig
					.getDirStructure() != null ? ldapConfig.getDirStructure()
					: "");
			authConfigProperties.setProperty(LdapConfig.ID,
					ldapConfig.getId_() != null ? ldapConfig.getId_() : "");
			authConfigProperties.setProperty(LdapConfig.NAME,
					ldapConfig.getName_() != null ? ldapConfig.getName_() : "");
			authConfigProperties.setProperty(LdapConfig.LOGINNO, ldapConfig
					.getLoginno_() != null ? ldapConfig.getLoginno_() : "");
			authConfigProperties.setProperty(LdapConfig.LOGINPWD, ldapConfig
					.getLoginpwd_() != null ? ldapConfig.getLoginpwd_() : "");
			authConfigProperties.setProperty(LdapConfig.EMAIL,
					ldapConfig.getEmail_() != null ? ldapConfig.getEmail_()
							: "");
			authConfigProperties.setProperty(LdapConfig.TELEPHONE, ldapConfig
					.getTelephone_() != null ? ldapConfig.getTelephone_() : "");
			authConfigProperties.setProperty(LdapConfig.MANAGER, ldapConfig
					.getManager() != null ? ldapConfig.getManager() : "");
			authConfigProperties.setProperty(LdapConfig.MANAGERPASSWORD, ldapConfig
					.getManagerPassword() != null ? ldapConfig.getManagerPassword() : "");
			authConfigProperties.setProperty(LdapConfig.USERCLASS, ldapConfig
					.getUserClass() != null ? ldapConfig.getUserClass() : "");
			authConfigProperties.setProperty(LdapConfig.DEPTCALSS, ldapConfig
					.getDeptClass() != null ? ldapConfig.getDeptClass() : "");
			authConfigProperties.setProperty(LdapConfig.ENTERDEPT, ldapConfig
					.getEnterDept() != null ? ldapConfig.getEnterDept() : "");
			authConfigProperties.setProperty(LdapConfig.DOMAIN, ldapConfig
					.getDomain() != null ? ldapConfig.getDomain() : "");
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(ssoPath);
				authConfigProperties
						.store(fos,
								"#####################auth and ldap setting#####################");
				PropertyUtil.reload("sso");
				PropertyUtil.reload("email");
			} catch (IOException e) {
				throw e;
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		if (emailConfig != null && emailPath != null) {
			// email的设置
			emailConfigProperties.setProperty(EmailConfig.EMAIL_SEND_HOST, emailConfig
					.getSendHost() != null ? emailConfig.getSendHost() : "");
			emailConfigProperties.setProperty(EmailConfig.EMAIL_SEND_ADDRESS, emailConfig
					.getSendAddress() != null ? emailConfig.getSendAddress()
					: "");
			emailConfigProperties.setProperty(EmailConfig.EMAIL_USER, emailConfig
					.getSendAccount() != null ? emailConfig.getSendAccount()
					: "");
			emailConfigProperties.setProperty(EmailConfig.EMAIL_SEND_PASSWORD,
					emailConfig.getSendPassword() != null ? emailConfig
							.getSendPassword() : "");
			emailConfigProperties.setProperty(EmailConfig.EMAIL_CC_ADDRESS, emailConfig
					.getCcAddress() != null ? emailConfig.getCcAddress() : "");
			emailConfigProperties.setProperty(EmailConfig.USE_EMAIL_CLIENT, emailConfig
					.getIsUseClient() != null ? emailConfig.getIsUseClient()
					: "");
			emailConfigProperties.setProperty(EmailConfig.USE_INNER_EMAIL, emailConfig
					.getIsUseInnerEmail() != null ? emailConfig
					.getIsUseInnerEmail() : "");
			emailConfigProperties.setProperty(EmailConfig.EMAIL_FUNCTION_DOAMIN,
					emailConfig.getFunctionDomain() != null ? emailConfig
							.getFunctionDomain() : "");
			emailConfigProperties.setProperty(EmailConfig.EMAIL_TRASH, emailConfig
					.getTrash() != null ? emailConfig.getTrash() : "");
			emailConfigProperties.setProperty(EmailConfig.EMAIL_SENDER, emailConfig
					.getSender() != null ? emailConfig.getSender() : "");
			emailConfigProperties.setProperty(EmailConfig.EMAIL_DRAFT, emailConfig
					.getDraft() != null ? emailConfig.getDraft() : "");
			emailConfigProperties.setProperty(EmailConfig.EMAIL_REMOVED, emailConfig
					.getRemoved() != null ? emailConfig.getRemoved() : "");
			emailConfigProperties.setProperty(EmailConfig.EMAIL_RECEIVE_SERVER,
					emailConfig.getFetchServer() != null ? emailConfig
							.getFetchServer() : "");
			emailConfigProperties.setProperty(EmailConfig.EMAIL_RECEIVE_SERVER_PORT,
					emailConfig.getFetchServerPort() != null ? emailConfig
							.getFetchServerPort() : "");
			emailConfigProperties.setProperty(EmailConfig.EMAIL_RECEIVE_PROTOCOL,
					emailConfig.getFetchProtocol() != null ? emailConfig
							.getFetchProtocol() : "");
			emailConfigProperties.setProperty(EmailConfig.EMAIL_RECEIVE_NEED_CERTIFICATE,
					emailConfig.getFetchssl() != null ? emailConfig
							.getFetchssl() : "");
			emailConfigProperties
					.setProperty(EmailConfig.EMAIL_SEND_SERVER, emailConfig
							.getSmtpServer() != null ? emailConfig
							.getSmtpServer() : "");
			emailConfigProperties.setProperty(EmailConfig.EMAIL_SEND_SERVER_PORT,
					emailConfig.getSmtpServerPort() != null ? emailConfig
							.getSmtpServerPort() : "");
			emailConfigProperties.setProperty(EmailConfig.EMAIL_ENABLE_ACCESSOORIES,
					emailConfig.getSmtpAuthenticated() != null ? emailConfig
							.getSmtpAuthenticated() : "");
			emailConfigProperties.setProperty(EmailConfig.EMAIL_SEND_NEED_CERTIFICATE,
					emailConfig.getSmtpssl() != null ? emailConfig.getSmtpssl()
							: "");
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(emailPath);
				emailConfigProperties
						.store(fos,
								"#####################email setting#####################");
			} catch (IOException e) {
				try {
					throw e;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		if (imConfig != null && imPath != null) {
			// IM的设置
			imConfigProperties.setProperty(ImConfig.GKE_API_OPEN, imConfig.getOpen()!=null?imConfig.getOpen():"0");
			imConfigProperties.setProperty(ImConfig.GKE_API_IP, imConfig.getIp() != null ? imConfig.getIp() : "127.0.0.1");
			imConfigProperties.setProperty(ImConfig.GKE_SERVER_PORT, imConfig.getPort() != null ? imConfig.getPort() : "8900");
			imConfigProperties.setProperty(ImConfig.GKE_SERVER_LOGINNO, imConfig.getLoginno() != null ? imConfig.getLoginno() : "admin");
			imConfigProperties.setProperty(ImConfig.GKE_SERVER_PASSWORD, imConfig.getPassword() != null ? imConfig.getPassword() : "admin");
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(imPath);
				imConfigProperties.store(fos,"#####################im setting#####################");
			} catch (IOException e) {
				try {
					throw e;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		if(checkoutPath != null){
			if(checkoutConfig != null){
				if(checkoutConfig.getInvocation()){
					checkoutConfigProperties.setProperty(CheckoutConfig.INVOCATION, "true");
				}else {
					checkoutConfigProperties.setProperty(CheckoutConfig.INVOCATION, "false");
				}
			}else if(checkoutConfig == null){
				checkoutConfigProperties.setProperty(CheckoutConfig.INVOCATION, "false");
			}
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(checkoutPath);
				checkoutConfigProperties.store(fos,"#####################checkout setting#####################");
			} catch (IOException e) {
				try {
					throw e;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		if(loginConfig!=null&&passwordLegalPath!=null){
			//loginConfig设置
			loginConfigConfigProperties.setProperty(LoginConfig.LOGIN_PASSWORD_LENGTH, loginConfig
					.getLength()!=null ? loginConfig.getLength() : "");
			loginConfigConfigProperties.setProperty(LoginConfig.LOGIN_FAIL_TIMES, loginConfig
					.getFailLoginTimes()!=null ? loginConfig.getFailLoginTimes():"");
			loginConfigConfigProperties.setProperty(LoginConfig.LOGIN_PASSWOR_MAXAGE, loginConfig
					.getMaxAge()!=null ? loginConfig.getMaxAge():"");
			loginConfigConfigProperties.setProperty(LoginConfig.LOGIN_PASSWORD_LEGAL, loginConfig.getLegal()
					!=null?loginConfig.getLegal():"");
			loginConfigConfigProperties.setProperty(LoginConfig.LOGIN_PASSWORD_UPADTE_TIMES,loginConfig
					.getMaxUpdateTimes()!=null?loginConfig.getMaxUpdateTimes():"");
			loginConfigConfigProperties.setProperty(LoginConfig.LOGIN_UPDATE_NOTICE,loginConfig
					.getNoticeTime()!=null ? loginConfig.getNoticeTime() : "");
			loginConfigConfigProperties.setProperty(LoginConfig.LOGIN_NOTICE_METHOD, loginConfig
					.getNoticeMethod()!=null ? loginConfig.getNoticeMethod() : "");
			loginConfigConfigProperties.setProperty(LoginConfig.LOGIN_NOTICE_AUTHOR, loginConfig.
					getNoticeAuthor()!= null ?loginConfig.getNoticeAuthor():"");
			loginConfigConfigProperties.setProperty(LoginConfig.LOGIN_NOTICE_CONTENT, loginConfig
					.getNoticeContent()!=null?loginConfig.getNoticeContent():"");
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(passwordLegalPath);
				loginConfigConfigProperties
						.store(fos,
								"#####################loginConfig setting#####################");
			} catch (IOException e) {
				try {
					throw e;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		if (kmConfig != null && kmPath != null) {
			// KM的设置
			kmConfigConfigProperties.setProperty(KmConfig.ENABLE, kmConfig.getEnable()? "true":"false");
			kmConfigConfigProperties.setProperty(KmConfig.DB_TYPE, kmConfig.getDbType());
			kmConfigConfigProperties.setProperty(KmConfig.DERVER_CLASS, kmConfig.getDriverClass());
			kmConfigConfigProperties.setProperty(KmConfig.DERVER_URL, kmConfig.getUrl());
			kmConfigConfigProperties.setProperty(KmConfig.USER, kmConfig.getUsername());
			kmConfigConfigProperties.setProperty(KmConfig.PASSWORD, kmConfig.getPassword());
			kmConfigConfigProperties.setProperty(KmConfig.POOL_SIZE, kmConfig.getPoolsize());
			kmConfigConfigProperties.setProperty(KmConfig.TIME_OUT, kmConfig.getTimeout());
			
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(kmPath);
				kmConfigConfigProperties.store(fos,"#####################km setting#####################");
			} catch (IOException e) {
				try {
					throw e;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		PropertyUtil.reload("sso");
		PropertyUtil.reload("email");
		PropertyUtil.reload("im");
		PropertyUtil.reload("checkout");
		PropertyUtil.reload("passwordLegal");
		PropertyUtil.reload("km");
		//更改KM数据源时，需将ThreadLocal中的旧连接清除
		try {
			PersistenceUtils.closeConnection();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if(kmConfig !=null && kmConfig.getEnable()){
			try {
				NDataSource.reLoadDataSource();
				new Initiator().init();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		cn.myapps.core.email.util.EmailConfig.initEmailConfig();
	}

}
