package cn.myapps.km.baike.user.ejb;

import javax.servlet.http.HttpServletRequest;

import cn.myapps.core.sysconfig.ejb.KmConfig;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.km.org.ejb.NRole;
import cn.myapps.km.org.ejb.NUser;
import cn.myapps.util.property.PropertyUtil;

/**
 * 
 * @author dragon
 * 用户基本信息实体类
 * 继承平台用户
 *
 */
public class BUser extends NUser{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6520656036035726979L;
	
	public static final String SESSION_ATTRIBUTE_FRONT_USER = "BAIKE_FRONT_USER";//session key
	
	private BUserAttribute attribute;
	
	
	public BUserAttribute getAttribute() {
		return attribute;
	}


	public void setAttribute(BUserAttribute attribute) {
		this.attribute = attribute;
	}


	public BUser(BaseUser vo) throws Exception {
		super(vo);
		// TODO Auto-generated constructor stub
	}
	
	public boolean isPublicDiskAdmin(){
		boolean flag = false;
		try {
			flag = new BUserEntrySetProcessBean().isPublicDiskAdmin(getId(), NRole.LEVEL_DOMAIN_ADMIN);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	public static void login(HttpServletRequest request,WebUser webUser) throws Exception {
		try {
			PropertyUtil.reload("km");
			if(PropertyUtil.get(KmConfig.ENABLE).equals("true")){
				BUser buser = new BUser(webUser);
				buser.getKmRoles();
				request.getSession().setAttribute(BUser.SESSION_ATTRIBUTE_FRONT_USER, buser);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	
}
