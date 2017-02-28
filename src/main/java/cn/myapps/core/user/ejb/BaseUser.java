package cn.myapps.core.user.ejb;

import java.io.Serializable;
import java.util.Date;

import net.sf.json.JSONObject;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.util.HtmlEncoder;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

public class BaseUser extends ValueObject implements Serializable, Cloneable {

	/**
	 *   
	 */
	private static final long serialVersionUID = 6368860852731448860L;

	public static final int NORMAL_DOMAIN = 0x000000;

	public static final int UPGRADE_DOMAIN = 0x000001;

	public static final int ADVANCED_DOMAIN = 0x000010;

	public static final int VIP_DOMAIN = 0x000100;

	public static final int SUPER_DOMAIN = 0x001000;
	
	public static final String PERMISSION_TYPE_PRIVATE = "private";
	public static final String PERMISSION_TYPE_PUBLIC = "public";

	/**
	 * 主键
	 */
	private String id;

	/**
	 * 用户姓名
	 */
	private String name;

	/**
	 * 用户姓名拼音首字母
	 */
	private String nameLetter;

	/**
	 * 用户登陆名，UserID
	 */
	private String loginno;

	/**
	 * 密码，通过ＭＤ５加密后然后进行移位运算。
	 */
	private String loginpwd;

	/**
	 * 用户电子邮件
	 */
	private String email;

	/**
	 * 工作日历种类
	 */
	protected String calendarType;

	/**
	 * 状态，表示是否生效。
	 */
	private int status = 1;

	private String domainid;

	private String telephone;

	/**
	 * 域管理员
	 */
	private boolean domainAdmin;
	/**
	 * 超级管理员
	 */
	private boolean superAdmin;
	/**
	 * 开发人员
	 */
	private boolean developer;
	/**
	 * 默认应用
	 */
	protected String defaultApplication;
	/**
	 * 默认部门
	 */
	protected String defaultDepartment;
	/**
	 * 域管理员等级： 0x000000:普通域管理员 0x000001:升级域管理员 0x000010:超级域管理员 0x000100:VIP域管理员
	 */
	private int domainPermission = 0;

	/**
	 * 上级
	 */
	protected UserVO superior;

	private Date startProxyTime;

	private Date endProxyTime;
	/**
	 * 密码策略
	 */
	private Date lastModifyPasswordTime;

	private String passwordArray;

	private int lockFlag = 1;

	/**
	 * USBKEY公钥的模
	 */
	private String publicKey;
	
	private DomainVO domain;
	
	/**
	 * 用户头像（json）
	 */
	private String avatar;
	
	/**
	 * 常用联系人
	 */
	private String favoriteContacts;
	
	/**
	 * 是否为接口人
	 */
	private boolean liaisonOfficer = false;
	
	/**
	 * 授权方式
	 */
	private String permissionType = PERMISSION_TYPE_PUBLIC;
	
	/**
	 * 手机号码2
	 */
	private String telephone2;

	/**
	 * 是否公开手机号码
	 */
	private boolean telephonePublic = true;

	/**
	 * 是否公开手机号码2
	 */
	private boolean telephonePublic2 = true;

	/**
	 * 是否公开电子邮箱
	 */
	private boolean emailPublic = true;

	/**
	 * 是否允许查看非公开信息
	 */
	private boolean userInfoPublic = true;
	
	public String getTelephone2() {
		return telephone2;
	}

	public void setTelephone2(String telephone2) {
		this.telephone2 = telephone2;
	}

	public boolean isTelephonePublic() {
		return telephonePublic;
	}

	public void setTelephonePublic(boolean telephonePublic) {
		this.telephonePublic = telephonePublic;
	}

	public boolean isTelephonePublic2() {
		return telephonePublic2;
	}

	public void setTelephonePublic2(boolean telephonePublic2) {
		this.telephonePublic2 = telephonePublic2;
	}

	public boolean isEmailPublic() {
		return emailPublic;
	}

	public void setEmailPublic(boolean emailPublic) {
		this.emailPublic = emailPublic;
	}

	public boolean isUserInfoPublic() {
		return userInfoPublic;
	}

	public void setUserInfoPublic(boolean userInfoPublic) {
		this.userInfoPublic = userInfoPublic;
	}

	public boolean isLiaisonOfficer() {
		return liaisonOfficer;
	}

	public void setLiaisonOfficer(boolean liaisonOfficer) {
		this.liaisonOfficer = liaisonOfficer;
	}

	public String getPermissionType() {
		return permissionType;
	}

	public void setPermissionType(String permissionType) {
		this.permissionType = permissionType;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	public String getAvatarUri(){
		if(!StringUtil.isBlank(this.avatar)){
			return JSONObject.fromObject(avatar).getString("url");
		}
		
		return null;
	}

	public String getFavoriteContacts() {
		return favoriteContacts;
	}

	public void setFavoriteContacts(String favoriteContacts) {
		this.favoriteContacts = favoriteContacts;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	/**
	 * @hibernate.property column="DEFAULTAPPLICATION"
	 */

	public String getDefaultApplication() {
		return defaultApplication;
	}

	public Date getLastModifyPasswordTime() {
		return lastModifyPasswordTime;
	}

	public void setLastModifyPasswordTime(Date lastModifyPasswordTime) {
		this.lastModifyPasswordTime = lastModifyPasswordTime;
	}

	public String getPasswordArray() {
		return passwordArray;
	}

	public void setPasswordArray(String passwordArray) {
		this.passwordArray = passwordArray;
	}

	public int getLockFlag() {
		return lockFlag;
	}

	public void setLockFlag(int lockFlag) {
		this.lockFlag = lockFlag;
	}

	public void setDefaultApplication(String defaultApplication) {
		this.defaultApplication = defaultApplication;
	}

	/**
	 * @hibernate.property column="EMAIL"
	 * @return Returns the email.
	 * @roseuid 44C7A1960255
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            The email to set.
	 * @param email
	 * @roseuid 44C7A1960256
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @hibernate.id column="ID" generator-class="assigned"
	 * @return Returns the id.
	 * @roseuid 44C7A1960260
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 * @param id
	 * @roseuid 44C7A1960269
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @hibernate.property column="LOGINNO"
	 * @return Returns the loginno.
	 * @roseuid 44C7A1960273
	 */
	public String getLoginno() {
		return loginno;
	}

	/**
	 * @param loginno
	 *            The loginno to set.
	 * @param loginno
	 * @roseuid 44C7A1960274
	 */
	public void setLoginno(String loginno) {
		this.loginno = loginno;
	}

	/**
	 * @hibernate.property column="LOGINPWD"
	 * @return Returns the loginpwd.
	 * @roseuid 44C7A196027E
	 */
	public String getLoginpwd() {
		return loginpwd;
	}

	/**
	 * @param loginpwd
	 *            The loginpwd to set.
	 * @param loginpwd
	 * @roseuid 44C7A1960287
	 */
	public void setLoginpwd(String loginpwd) {
		this.loginpwd = loginpwd;
	}

	/**
	 * @hibernate.property column="NAME"
	 * @return Returns the name.
	 * @roseuid 44C7A1960291
	 */
	public String getName() {
//		if (name != null)
//			this.name = name.toLowerCase();
		return HtmlEncoder.encode(name);
	}

	/**
	 * @param name
	 *            The name to set.
	 * @param name
	 * @roseuid 44C7A196029B
	 */
	public void setName(String name) {
//		if (name != null)
//			this.name = name.toLowerCase();
//		else
//			this.name = null;
		this.name = name;
	}

	public String getNameLetter() {
		return nameLetter;
	}

	public void setNameLetter(String nameLetter) {
		this.nameLetter = nameLetter;
	}

	/**
	 * @hibernate.property column="STATUS"
	 * @return Returns the status.
	 * @roseuid 44C7A19602C3
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            The status to set.
	 * @param status
	 * @roseuid 44C7A19602CD
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isActive() {
		return this.status == 1;
	}

	/**
	 * @hibernate.property column="LANGUAGETYPE"
	 */
	// public int getLanguageType() {
	// return languageType;
	// }
	/**
	 * @param languageType
	 *            The languageType to set.
	 * @param languageType
	 * 
	 */
	// public void setLanguageType(int languageType) {
	// this.languageType = languageType;
	// }
	/**
	 * @hibernate.property column="DOMAINID"
	 * @return
	 */
	public String getDomainid() {
		return domainid;
	}

	public void setDomainid(String domainid) {
		this.domainid = domainid;
	}

	
	public DomainVO getDomain() throws Exception {
		if(domain != null) return domain;
		try {
			DomainProcess domainProcess = (DomainProcess) ProcessFactory
					.createProcess(DomainProcess.class);
			if (!StringUtil.isBlank(this.getDomainid())) {
				domain = (DomainVO) domainProcess.doView(this.getDomainid());
				return domain;
			}
			if (domain == null) {
				throw new OBPMValidateException("No domain found for user: "
						+ this.getLoginno());
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}

	/**
	 * 是否管理员
	 * 
	 * @return
	 */
	public boolean isAdmin() {
		return isDomainAdmin() || isSuperAdmin();
	}

	public boolean isDomainAdmin() {
		return domainAdmin;
	}

	public void setDomainAdmin(boolean domainAdmin) {
		this.domainAdmin = domainAdmin;
	}

	public boolean isSuperAdmin() {
		return superAdmin;
	}

	public void setSuperAdmin(boolean superAdmin) {
		this.superAdmin = superAdmin;
	}

	public boolean isDeveloper() {
		return developer;
	}

	public void setDeveloper(boolean developer) {
		this.developer = developer;
	}

	public void copyFrom(BaseUser another) throws Exception {
		this.id = another.getId();
		// this.calendarType = another.getCalendarType();
		this.developer = another.isDeveloper();
		this.domainAdmin = another.isDomainAdmin();
		this.domainid = another.getDomainid();
		this.email = another.getEmail();
		// this.languageType = another.getLanguageType();
		this.loginno = another.getLoginno();
		this.loginpwd = another.getLoginpwd();
		this.name = another.getName();
		this.status = another.getStatus();
		this.superAdmin = another.isSuperAdmin();
	}

	public boolean equals(Object object) {
		if (object == null)
			return false;
		//if (object.getClass() != this.getClass())
		//	return false;
		BaseUser user = (BaseUser) object;
		return this.id.equals(user.getId());
	}

	public int hashCode() {
		return super.hashCode();
	}

	public int getDomainPermission() {
		return domainPermission;
	}

	public void setDomainPermission(int domainPermission) {
		this.domainPermission = domainPermission;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * @hibernate.property column="CALENDAR"
	 * @return
	 */
	public String getCalendarType() {
		return calendarType;
	}

	public void setCalendarType(String calendarType) {
		this.calendarType = calendarType;
	}

	public UserVO getSuperior() {
		return superior;
	}

	public void setSuperior(UserVO superior) {
		this.superior = superior;
	}

	public String getDefaultDepartment() {
		return defaultDepartment;
	}

	public void setDefaultDepartment(String defaultDepartment) {
		this.defaultDepartment = defaultDepartment;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public Date getStartProxyTime() {
		return startProxyTime;
	}

	public void setStartProxyTime(Date startProxyTime) {
		this.startProxyTime = startProxyTime;
	}

	public Date getEndProxyTime() {
		return endProxyTime;
	}

	public void setEndProxyTime(Date endProxyTime) {
		this.endProxyTime = endProxyTime;
	}

}
