package cn.myapps.km.org.ejb;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import cn.myapps.core.sysconfig.ejb.KmConfig;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.util.property.PropertyUtil;


/**网盘用户
 * @author xiuwei
 *
 */
public class NUser extends WebUser{
	
	private Collection<NRole> kmRoles;
	
	public Collection<NRole> getKmRoles() {
		if(kmRoles ==null){
			try {
				kmRoles = new NRoleProcessBean().doQueryRolesByUser(id);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return kmRoles;
	}
	
	public boolean isPublicDiskAdmin(){
		boolean flag = false;
		
		for(Iterator<NRole> it = this.getKmRoles().iterator(); it.hasNext();){
			NRole role = it.next();
			if(role.getLevel() == NRole.LEVEL_DOMAIN_ADMIN){
				flag = true;
				break;
			}
		}
		
		return flag;
	}
	
	/**
	 * 是否属于km管理员(部门文档管理员|企业文档管理员)
	 * @return
	 */
	public boolean isKmAdmin(){
		boolean flag = false;
		for(Iterator<NRole> it = this.getKmRoles().iterator(); it.hasNext();){
			NRole role = it.next();
			if(role.getLevel() >= NRole.LEVEL_DEPT_ADMIN){
				flag = true;
				break;
			}
		}
		
		return flag;
	}

	public void setKmRoles(Collection<NRole> kmRoles) {
		this.kmRoles = kmRoles;
	}

	public NUser(BaseUser vo) throws Exception {
		super(vo);
		if (vo instanceof WebUser) {
			this.setId(vo.getId());
			this.setName(vo.getName());
			this.setDepartments(((WebUser) vo).getDepartments());
			this.setRoles(((WebUser) vo).getRoles());
			this.setDeptlist(((WebUser) vo).getDepartments());
			this.setRolelist(((WebUser) vo).getRoles());
			this.setDomainid(vo.getDomainid());// 隶属域
			this.setCalendarType(((WebUser) vo).getCalendarType());
			this.setTelephone(vo.getTelephone());
			this.setSuperior(vo.getSuperior());
			this.setRemarks(((WebUser) vo).getRemarks());
			// this.setApplicationid(vo.getDefaultApplication());
			this.setDomainUser(((WebUser) vo).getDomainUser());

			this.setUserSetup(((WebUser) vo).getUserSetup());
			this.setPublicKey(vo.getPublicKey());
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1804631177084152454L;
	
	public static final String SESSION_ATTRIBUTE_FRONT_USER = "KM_FRONT_USER";//session key
	
	/**
	 * 主键
	 */
	private String id;

	/**
	 * 用户姓名
	 */
	private String name;

	/**
	 * 用户登陆名，UserID
	 */
	private String loginno;

	private String loginpwd;

	private String serverAddr;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLoginno() {
		return loginno;
	}

	public void setLoginno(String loginno) {
		this.loginno = loginno;
	}

	public String getLoginpwd() {
		return loginpwd;
	}

	public void setLoginpwd(String loginpwd) {
		this.loginpwd = loginpwd;
	}

	public static void login(HttpServletRequest request,WebUser webUser) throws Exception {
		try {
			PropertyUtil.reload("km");
			if(PropertyUtil.get(KmConfig.ENABLE).equals("true")){
				NUser user = new NUser(webUser);
				user.getKmRoles();
				String contextPath = request.getContextPath();
				//WebUser携带的serverAddr最后字符为'/'  contextPath 最开始字符为'/' 去重考虑
				user.setServerAddr(webUser.getServerAddr()+ (contextPath==null || contextPath.trim().length()<=0?"" : contextPath.substring(1)));
				request.getSession().setAttribute(NUser.SESSION_ATTRIBUTE_FRONT_USER, user);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	public String getServerAddr() {
		return serverAddr;
	}

	public void setServerAddr(String serverAddr) {
		this.serverAddr = serverAddr;
	}
	

}
