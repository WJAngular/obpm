package cn.myapps.core.sysconfig.ejb;

import java.io.Serializable;

public class LoginConfig implements Serializable {
	private static final long serialVersionUID = -5978553466014687626L;
	
	public static final String LOGIN_PASSWORD_LENGTH = "ao.login.password.length";
	public static final String LOGIN_PASSWORD_LEGAL = "ao.login.password.legal";
	public static final String LOGIN_PASSWORD_UPADTE_TIMES = "ao.login.password.update.before.maxtimes";
	public static final String LOGIN_FAIL_TIMES = "ao.login.fail.maxtimes";
	public static final String LOGIN_PASSWOR_MAXAGE = "ao.login.password.maxage";
	public static final String LOGIN_UPDATE_NOTICE = "ao.login.password.update.notice";
	public static final String LOGIN_NOTICE_METHOD = "ao.login.notice.method";

	public static final String LOGIN_NOTICE_AUTHOR = "ao.login.notice.author";
	public static final String LOGIN_NOTICE_CONTENT ="ao.login.notice.content";
    
    private String length;
    private String legal;
    private String maxUpdateTimes;
    private String failLoginTimes;
    private String maxAge;
    private String noticeTime;
    private String noticeMethod;

    private String noticeAuthor;
    private String noticeContent;
    
    
    
    
	public String getNoticeContent() {
		return noticeContent;
	}
	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}
	
	public String getNoticeAuthor() {
		return noticeAuthor;
	}
	public void setNoticeAuthor(String noticeAuthor) {
		this.noticeAuthor = noticeAuthor;
	}
	public String getNoticeMethod() {
		return noticeMethod;
	}
	public void setNoticeMethod(String noticeMethod) {
		this.noticeMethod = noticeMethod;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getLegal() {
		return legal;
	}
	public void setLegal(String legal) {
		this.legal = legal;
	}
	public String getMaxUpdateTimes() {
		return maxUpdateTimes;
	}
	public void setMaxUpdateTimes(String maxUpdateTimes) {
		this.maxUpdateTimes = maxUpdateTimes;
	}
	public String getFailLoginTimes() {
		return failLoginTimes;
	}
	public void setFailLoginTimes(String failLoginTimes) {
		this.failLoginTimes = failLoginTimes;
	}
	public String getMaxAge() {
		return maxAge;
	}
	public void setMaxAge(String maxAge) {
		this.maxAge = maxAge;
	}
	public String getNoticeTime() {
		return noticeTime;
	}
	public void setNoticeTime(String noticeTime) {
		this.noticeTime = noticeTime;
	}
    
}
