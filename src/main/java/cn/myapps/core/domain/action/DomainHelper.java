package cn.myapps.core.domain.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import com.opensymphony.xwork2.ActionContext;

import cn.myapps.constans.Environment;
import cn.myapps.constans.Web;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.util.ProcessFactory;

/**
 * @see cn.myapps.core.domain.action.DomainHelper class
 * @author Chris
 * @since JDK1.4
 */
public class DomainHelper {

	public BaseUser user = null;

	public int _page;

	public int _line;

	public static DomainVO getDomainVO(BaseUser user) throws Exception {
		if (user == null)
			return null;
		String domainId = user.getDomainid();
		if (domainId == null)
			return null;
		DomainProcess dp = (DomainProcess) ProcessFactory
				.createProcess(DomainProcess.class);
		return (DomainVO) dp.doView(domainId);
	}

	public Collection<DomainVO> queryDomains() throws Exception {
		DomainProcess dp = (DomainProcess) ProcessFactory
				.createProcess(DomainProcess.class);
		return dp.queryDomains(user.getId(), 1, 5);
	}

	public BaseUser getUser() {
		return user;
	}

	public void setUser(BaseUser user) {
		this.user = user;
	}

	public int get_page() {
		return _page;
	}

	public void set_page(int _page) {
		this._page = _page;
	}

	public int get_line() {
		return _line;
	}

	public void set_line(int _line) {
		this._line = _line;
	}

	public static DomainVO getDomainVO(String domainid) throws Exception {
		DomainProcess dp = (DomainProcess) ProcessFactory
				.createProcess(DomainProcess.class);
		return (DomainVO) dp.doView(domainid);
	}

	/**
	 * 返回当前所有的皮肤类型
	 * 
	 * @return 皮肤类型的哈希图
	 * @throws Exception
	 */
	public Map<String, String> querySkinTypes(WebUser user) throws Exception {
		Map<String, String> skins = new HashMap<String, String>();

		Environment ev = Environment.getInstance();
		
		String equipment = "";
		if(user !=null){
			switch (user.getEquipment()) {
			case WebUser.EQUIPMENT_PC:
				equipment = "pc";
				break;
			case WebUser.EQUIPMENT_PAD:
				equipment = "pad";
				break;
			case WebUser.EQUIPMENT_PHONE:
				equipment = "phone";
				break;
			default:
				equipment = "pc";
				break;
			}
		}
		synchronized (ev) {
			String contentPath = "";
			if (ev != null) {
				contentPath = ev.getRealPath("") + "/portal";
				File dir = new File(contentPath);
				File[] files = dir.listFiles();
				if (files != null && files.length > 0) {
					for (int i = 0; i < files.length; i++) {
						if (files[i].isDirectory()) {// 判断是否是目录文件
							if (!files[i].getName().equals("share")
									&& !files[i].getName().equals("dispatch")
									&& files[i].getName().indexOf(".") == -1){
								
								if(user !=null){
									File cf = new File(contentPath+"/"+files[i].getName()+"/config.ini");
									if(cf.exists()){
										Properties config = new Properties();
										InputStream stream = new FileInputStream(cf);
										config.load(stream);
										stream.close();
										if("false".equals(config.getProperty(equipment))){
											continue;
										}
									}
								}
								
								skins.put(files[i].getName(),
										"{*[" + files[i].getName() + "]*}");
							}
						} else {
							continue;
						}
					}
				} else {
					skins.put("default", "default");
				}
			}
			return skins;

		}
	}

	/**
	 * 获取所有的企业域
	 * 
	 * @return
	 */
	public Map<String, String> getAllDomain() {
		Map<String, String> domainMap = new HashMap<String, String>();
		Collection<DomainVO> domains;
		try {
			DomainProcess dp = (DomainProcess) ProcessFactory
					.createProcess(DomainProcess.class);
			domains = dp.getAllDomain();
			if (domains == null) {
				domains = new ArrayList<DomainVO>();
			}
			domainMap.put("", "{*[Select]*}");
			Iterator<DomainVO> it = domains.iterator();
			while (it.hasNext()) {
				DomainVO domain = it.next();
				domainMap.put(domain.getId(), domain.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return domainMap;
	}

	/**
	 * 获取已激活的企业域集合
	 * 
	 * @return
	 */
	public Collection<DomainVO> getDomainByStatus() throws Exception {
		Collection<DomainVO> col = new ArrayList<DomainVO>();
		try {
			DomainProcess dp = (DomainProcess) ProcessFactory
					.createProcess(DomainProcess.class);
			col = dp.getDomainByStatus(1);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return col;
	}

	/**
	 * 根据条件获取企业域集合(后台监控在线用户调用)
	 * 
	 * @return
	 */
	public Collection<DomainVO> getDomains() throws Exception {
		Collection<DomainVO> col = new ArrayList<DomainVO>();
		try {
			Map<String, Object> session = ActionContext.getContext()
					.getSession();
			WebUser user = (WebUser) session.get(Web.SESSION_ATTRIBUTE_USER);
			DomainProcess dp = (DomainProcess) ProcessFactory
					.createProcess(DomainProcess.class);
			if (user.isSuperAdmin()) {
				DomainVO domain = new DomainVO();
				domain.setId("");
				domain.setName("{*[ALL]*}");
				col.add(domain);
				col.addAll(dp.getDomainByStatus(1));
			} else if (user.isDomainAdmin()) {
				DomainVO domain = new DomainVO();
				domain.setId("");
				domain.setName("{*[ALL]*}");
				col.add(domain);
				col.addAll(dp.queryDomainsByStatusAndUser(1, user.getId()));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return col;
	}
}