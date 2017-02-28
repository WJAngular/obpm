package cn.myapps.core.sysconfig.ejb;

public interface SysConfigProcess {
	
	public void save(AuthConfig authConfig, LdapConfig ldapConfig, EmailConfig emailConfig,ImConfig imConfig,CheckoutConfig checkoutConfig,LoginConfig loginConfig,KmConfig kmConfig) throws Exception;
	public KmConfig getKmConfig() throws Exception;
	public AuthConfig getAuthConfig() throws Exception;
	public LdapConfig getLdapConfig() throws Exception;
	public EmailConfig getEmailConfig() throws Exception;
	public ImConfig getImConfig() throws Exception;
	public CheckoutConfig getCheckoutConfig() throws Exception;
	public LoginConfig getLoginConfig() throws Exception;
}
