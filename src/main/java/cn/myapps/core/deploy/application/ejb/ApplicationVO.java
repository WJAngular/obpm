//Source file: C:\\Java\\workspace\\MyApps\\src\\cn\\myapps\\core\\deploy\\application\\ejb\\ApplicationVO.java

package cn.myapps.core.deploy.application.ejb;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.jfree.util.Log;

import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.deploy.module.ejb.ModuleProcess;
import cn.myapps.core.deploy.module.ejb.ModuleVO;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.dynaform.dts.datasource.ejb.DataSource;
import cn.myapps.core.dynaform.dts.datasource.ejb.DataSourceProcess;
import cn.myapps.core.superuser.ejb.SuperUserVO;
import cn.myapps.util.ProcessFactory;

/**
 * 注册的应用
 * 
 * @hibernate.class table="T_APPLICATION" lazy = "false"
 */
public class ApplicationVO extends ValueObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @uml.property name="id"
	 */
	private String id;

	/**
	 * @uml.property name="name"
	 */
	private String name;

	/**
	 * @uml.property name="ispublished"
	 */
	//private boolean ispublished;

	/**
	 * @uml.property name="description"
	 */
	private String description;

	/**
	 * @uml.property name="isdefaultsite"
	 */
	//private boolean isdefaultsite;
	
	/**
	 * @uml.property name="logourl"
	 */
	private String logourl;

	/**
	 * @uml.property name="owners"
	 */
	public Collection<SuperUserVO> owners;

	private Collection<ModuleVO> modules;

	/**
	 * @uml.property name="domainName"
	 */
	private String domainName;

	private Collection<DomainVO> domains;
	/**
	 * @uml.property name="type"
	 */
	private String type;
	/**
	 * 数据源ID
	 */
	private String datasourceid;
	
	
	/**
	 * 是否激活
	 */
	private boolean activated;
	
	/**
	 * 微信应用id
	 */
	private String weixinAgentId;
	
	private Collection<DomainApplicationSet> domainApplicationSet;
	
	
	/**
	 * 是否被激活
	 * @return
	 */
	public boolean isActivated() {
		return activated;
	}

	/**
	 * 设置是否激活
	 * @param activated
	 */
	public void setActivated(boolean activated) {
		this.activated = activated;
	}
	
	/**
	 * 获取应用图标
	 * 
	 * @return 应用图标
	 */
	public String getLogourl() {
		return logourl;
	}

	/**
	 * 设置应用图标
	 * 
	 * @param logourl
	 *            应用图标
	 */
	public void setLogourl(String logourl) {
		this.logourl = logourl;
	}

	/**
	 * 设置应用的类型
	 * 
	 * @hibernate column="type"
	 * @return 应用的类型
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置应用的类型
	 * 
	 * @param type
	 *            类型(移动商务,财务管理,市场管理,人力资源,客户管理,软件开发)
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 创建日期
	 */
	private Date createDate;

	/**
	 * 获取应用关联域的集合
	 * 
	 * @return 域的集合
	 */
	public Collection<DomainVO> getDomains() {
		if (domains == null)
			domains = new HashSet<DomainVO>();
		return domains;
	}

	/**
	 * 设置应用关联域的集合
	 * 
	 * @param domains
	 *            域的集合
	 */
	public void setDomains(Collection<DomainVO> domains) {
		this.domains = domains;
	}

	/**
	 * 获取企业域名称
	 * 
	 * @hibernate.property column="DOMAINNAME"
	 * @uml.property name="domainName"
	 */
	public String getDomainName() {
		return domainName;
	}

	/**
	 * 设置企业域名称
	 * 
	 * @param domainName
	 *            企业域名称
	 * @uml.property name="domainName"
	 */
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	/**
	 * 获取关联模块集合
	 * 
	 * @return 模块集合
	 * @hibernate.collection-one-to-many 
	 *                                   class="cn.myapps.core.deploy.module.ejb.ModuleVO"
	 * @hibernate.collection-key column="APPLICATION"
	 * @hibernate.set name="modules" table="T_MODULE" inverse="true"
	 *                sort="unsorted" order-by="ID"
	 * @uml.property name="modules"
	 */

	public Collection<ModuleVO> getModules() {
		if(isLazyLoad() && modules == null){
			try {
				ModuleProcess process = (ModuleProcess) ProcessFactory.createProcess(ModuleProcess.class);
				modules = process.getModuleByApplication(id);
				if(modules == null){
					return new HashSet<ModuleVO>();
				}else{
					return modules;
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return modules;
	}

	/**
	 * 设置关联模块集合
	 * 
	 * @param modules
	 *            模块集合
	 * @uml.property name="modules"
	 */
	public void setModules(Collection<ModuleVO> modules) {
		this.modules = modules;
	}

	/**
	 * 获取企业域标识
	 * 
	 * @hibernate.id column="ID" generator-class="assigned"
	 * @uml.property name="id"
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置企业域标识
	 * 
	 * @param id
	 *            企业域标识
	 * @uml.property name="id"
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取企业域的描述
	 * 
	 * @return 企业域的描述
	 * @hibernate.property column="DESCRIPTION"
	 * @uml.property name="description"
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 设置企业域的描述
	 * 
	 * @return 企业域的描述
	 * @param description
	 *            企业域的描述
	 * @uml.property name="description"
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 获取默认位置
	 * 
	 * @return 默认位置
	 * @hibernate.property column="ISDEFAULTSITE"
	 * @uml.property name="isdefaultsite"
	 */
//	public boolean isIsdefaultsite() {
//		return isdefaultsite;
//	}

	/**
	 * 设置是否使是默认位置
	 * 
	 * @param isdefaultsite
	 *            默认位置
	 * @uml.property name="isdefaultsite"
	 */
//	public void setIsdefaultsite(boolean isdefaultsite) {
//		this.isdefaultsite = isdefaultsite;
//	}

	/**
	 * 获取是否发布
	 * 
	 * @return 是否发布
	 * @hibernate.property column="ISPUBLISHED"
	 * @uml.property name="ispublished"
	 */
//	public boolean isIspublished() {
//		return ispublished;
//	}

	/**
	 * 设置是否发布
	 * 
	 * @param ispublished
	 *            是否发布
	 * @uml.property name="ispublished"
	 */
//	public void setIspublished(boolean ispublished) {
//		this.ispublished = ispublished;
//	}

	/**
	 * 获取软件名称
	 * 
	 * @return 软件名称
	 * @hibernate.property column="NAME"
	 * @uml.property name="name"
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置软件名称
	 * 
	 * @param name
	 *            软件名称
	 * @uml.property name="name"
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取拥有者
	 * 
	 * @return 拥有者
	 */
	public Collection<SuperUserVO> getOwners() {
		if (owners == null)
			owners = new HashSet<SuperUserVO>();
		return owners;
	}

	/**
	 * 设置拥有者
	 * 
	 * @param owners
	 *            拥有者
	 */
	public void setOwners(Collection<SuperUserVO> owners) {
		this.owners = owners;
	}

	public String getDatasourceid() {
		return datasourceid;
	}

	public void setDatasourceid(String datasourceid) {
		this.datasourceid = datasourceid;
	}

	public boolean equals(Object obj) {
		// if (obj instanceof ApplicationVO) {
		// return ((ApplicationVO) obj).getId().equals(this.getId());
		// }
		// return false;
		if (this == null)
			return false;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ApplicationVO vo = (ApplicationVO) obj;
		if (id == null) {
			return false;
		} else if (!this.id.equals(vo.id)) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		if (this.getId() != null) {
			return this.getId().hashCode();
		}
		return super.hashCode();
	}

	//
	// /**
	// * 获取阿里软件标识
	// *
	// * @return 阿里软件标识
	// */
	// public String getSipAppkey() {
	// return sipAppkey;
	// }
	//
	// /**
	// * 获取阿里软件验证串
	// *
	// * @return 阿里软件验证串
	// */
	// public String getSipAppsecret() {
	// return sipAppsecret;
	// }
	//
	// /**
	// * 设置阿里软件标识
	// *
	// * @param sipAppkey
	// * 阿里软件标识
	// */
	// public void setSipAppkey(String sipAppkey) {
	// this.sipAppkey = sipAppkey;
	// }
	//
	// /**
	// * 设置阿里软件验证串
	// *
	// * @param sipAppsecret
	// * 阿里软件验证串
	// */
	// public void setSipAppsecret(String sipAppsecret) {
	// this.sipAppsecret = sipAppsecret;
	// }

	/**
	 * 覆盖Object的比较方法
	 * 
	 * @param other
	 *            应用标识
	 * @return
	 */
	public boolean equals(ApplicationVO other) {
		if (other == null)
			return false;
		return this.id.equals(other.getId());
	}

	/**
	 * 测试数据源连接是否正确
	 * 
	 * @return (true：正确 false:失败)
	 */
	public boolean testDB() {
		try {
			DataSource ds = getDataSourceDefine();
			Connection conn = ds.getConnection();
			conn.close();

			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public Connection getConnection() throws Exception {
		DataSource ds = getDataSourceDefine();
		return ds.getConnection();
	}

	/**
	 * 获取创建日期
	 * 
	 * @return 创建日期
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * 设置创建日期
	 * 
	 * @param createDate
	 *            创建日期
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getWeixinAgentId() {
		return weixinAgentId;
	}

	public void setWeixinAgentId(String weixinAgentId) {
		this.weixinAgentId = weixinAgentId;
	}
	
	public Collection<DomainApplicationSet> getDomainApplicationSet() {
		if (domainApplicationSet == null) {
			try {
				//ApplicationProcess process = (ApplicationProcess)ProcessFactory.createProcess(ApplicationProcess.class);
				//domainApplicationSet = process.queryDomainApplicationSet(id);
				domainApplicationSet = new HashSet<DomainApplicationSet>();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return domainApplicationSet;
	}
	public void setDomainApplicationSet(
			Collection<DomainApplicationSet> domainApplicationSet) {
		this.domainApplicationSet = domainApplicationSet;
	}

	public DataSource getDataSourceDefine() {
		try {
			DataSourceProcess dsProcess = (DataSourceProcess) ProcessFactory.createProcess(DataSourceProcess.class);
			DataSource dataSource = (DataSource) dsProcess.doView(getDatasourceid());

			return dataSource;
		} catch (Exception e) {
			Log.warn(e.getMessage());
		}
		return new DataSource();
	}
}
