package cn.myapps.webservice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.json.JsonUtil;
import cn.myapps.util.xml.XmlUtil;
import cn.myapps.webservice.fault.DepartmentServiceFault;
import cn.myapps.webservice.model.SimpleDepartment;
import cn.myapps.webservice.util.DepartmentUtil;

/**
 * 提供部门增删改查功能接口
 * @author Administrator
 *
 */
public class DepartmentService {

	/**
	 * 传入SimpleDepartment对象创建一个部门
	 * @param dep SimpleDepartment对象
	 * @return -1:失败 ,0:成功
	 * @throws DepartmentServiceFault
	 */
	public int createDepartment(SimpleDepartment dep)
			throws DepartmentServiceFault {
		int result = -1;
		try {
			DepartmentProcess process = (DepartmentProcess) ProcessFactory
					.createProcess(DepartmentProcess.class);

			if (dep == null || StringUtil.isBlank(dep.getDomainName())
					|| StringUtil.isBlank(dep.getName())) {
				throw new NullPointerException("部门对象为空或部门的domainName为空或部门的name为空.");
			}

			DomainVO domain = WebServiceUtil
					.validateDomain(dep.getDomainName());

			DepartmentVO vo = new DepartmentVO();

			DepartmentUtil.convert(vo, dep);

			DepartmentVO temp = (DepartmentVO) process.doViewByName(dep.getName(), domain
					.getId());

			if (temp != null) {
				if (dep.getName().equalsIgnoreCase(temp.getName())
						&& vo.getSuperior().getId().equals(
								temp.getSuperior().getId())) {
					throw new Exception("该部门" + temp.getName() + "已经存在.");
				}
			}
			process.doCreate(vo);
			
			result = 0;
		} catch (Exception e) {
			throw new DepartmentServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new DepartmentServiceFault(e.getMessage());
			}
		}
		return result;
	}

	/**
	 * 传入部门对象,更新一个部门
	 * @param dep SimpleDepartment对象
	 * @return  -1:失败 ,0:成功
	 * @throws DepartmentServiceFault
	 */
	public int updateDepartment(SimpleDepartment dep)
			throws DepartmentServiceFault {
		int result = -1;
		try {
			DepartmentProcess process = (DepartmentProcess) ProcessFactory
					.createProcess(DepartmentProcess.class);
			if (dep == null || StringUtil.isBlank(dep.getId())) {
				throw new NullPointerException("部门对象为空或部门的ID为空.");
			}

			DepartmentVO vo = (DepartmentVO) process.doView(dep.getId());
			if (vo == null)
				throw new Exception("该部门" + dep.getId() + "不存在.");
			
			if(dep.getDomainName() == null)
				dep.setDomainName(vo.getDomain().getName());
			
			DomainVO domain = WebServiceUtil
					.validateDomain(dep.getDomainName());

			if (!vo.getName().equals(dep.getName())) {
				DepartmentVO temp = (DepartmentVO) process.doViewByName(dep.getName(), domain
						.getId());
				if (temp != null) {
					throw new Exception("该部门名称" + temp.getName() + "已存在！");
				}
			}

			boolean setDefSuperior = true;//是否需要设置默认上级部门
			if(dep.getSuperiorName() == null && vo.getSuperior() == null){
				setDefSuperior = false;
			}
			DepartmentUtil.convert(vo, dep);
			if(!setDefSuperior){
				vo.setSuperior(null);
				vo.setLevel(0);
			}
			process.doUpdate(vo);

			result = 0;
		} catch (Exception e) {
			throw new DepartmentServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new DepartmentServiceFault(e.getMessage());
			}
		}
		return result;
	}

	/**
	 * 根据主键获取一个SimpleDepartment
	 * @param pk
	 * 			主键
	 * @return SimpleDepartment对象
	 * @throws DepartmentServiceFault
	 */
	public SimpleDepartment getDepartment(String pk)
			throws DepartmentServiceFault {
		SimpleDepartment dep = null;
		try {
			DepartmentProcess process = (DepartmentProcess) ProcessFactory
					.createProcess(DepartmentProcess.class);
			if (StringUtil.isBlank(pk)) {
				throw new NullPointerException("主键为空.");
			}
			DepartmentVO vo = (DepartmentVO) process.doView(pk);
			if (vo != null) {
				dep = new SimpleDepartment();
				DepartmentUtil.convert(dep, vo);
			}
		} catch (Exception e) {
			throw new DepartmentServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new DepartmentServiceFault(e.getMessage());
			}
		}
		return dep;
	}

	/**
	 * 根据主键删除一个部门
	 * @param pk
	 * 			主键
	 * @return -1:失败 ,0:成功
	 * @throws DepartmentServiceFault
	 */
	public int deleteDepartment(String pk) throws DepartmentServiceFault {
		int result = -1;
		try {
			DepartmentProcess process = (DepartmentProcess) ProcessFactory
					.createProcess(DepartmentProcess.class);
			if (StringUtil.isBlank(pk)) {
				throw new NullPointerException("主键为空.");
			}
			process.doRemove(pk);
			result = 0;
		} catch (Exception e) {
			throw new DepartmentServiceFault(e.getMessage());
		}
		return result;
	}

	/**
	 * 根据主键删除部门
	 * 
	 * @param pks
	 *            主键数组
	 * @return -1:失败 ,0:成功
	 * @throws DepartmentServiceFault
	 */
	public int deleteDepartment(String[] pks) throws DepartmentServiceFault {
		int result = -1;
		try {
			if(pks != null){
				for(int i = 0; i<pks.length; i++){
					deleteDepartment(pks[i]);
				}
			}
			result = 0;
		} catch (Exception e) {
			throw new DepartmentServiceFault(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 设置当前部门的上级部门
	 * 
	 * @param dep
	 *            -当前部门
	 * @param superDep
	 *            -上级部门
	 * @return  -1:失败 ,0:成功
	 * @throws DepartmentServiceFault
	 */
	public int upateSuperior(SimpleDepartment dep, SimpleDepartment superDep)
			throws DepartmentServiceFault {
		int result = -1;
		try {

			DepartmentProcess process = (DepartmentProcess) ProcessFactory
					.createProcess(DepartmentProcess.class);
			if (dep == null || StringUtil.isBlank(dep.getId())
					|| superDep == null || StringUtil.isBlank(superDep.getId())) {
				throw new NullPointerException("对象为空或对象的ID为空.");
			}

			DomainVO domain = WebServiceUtil
					.validateDomain(dep.getDomainName());
			DomainVO superiorDomain = WebServiceUtil.validateDomain(superDep
					.getDomainName());

			if (!domain.getId().equals(superiorDomain.getId()))
				throw new Exception("两个部门不在同一个域.");

			DepartmentVO vo = (DepartmentVO) process.doViewByName(
					dep.getName(), domain.getId());
			if (vo == null)
				throw new Exception("当前部门" + dep.getName() + "不存在.");
			DepartmentVO sp = (DepartmentVO) process.doViewByName(superDep
					.getName(), superiorDomain.getId());
			if (sp == null)
				throw new Exception("上级部门" + superDep.getName() + "不存在.");

			vo.setSuperior(sp);
			vo.setLevel(sp.getLevel() + 1);
			PersistenceUtils.currentSession().clear();//清除session
			process.doUpdate(vo);

			result = 0;
		} catch (Exception e) {
			throw new DepartmentServiceFault(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 传入企业域名称获取企业域下所有部门的集合
	 * @param domainName 企业域名
	 * @return SimpleDepartment对象集合
	 * @throws DepartmentServiceFault
	 */
	public Collection<SimpleDepartment> getDepartmentsByDomainName(String domainName)
			throws DepartmentServiceFault {
		Collection<SimpleDepartment> cols = new ArrayList<SimpleDepartment>();
		try {
			DepartmentProcess process = (DepartmentProcess) ProcessFactory
					.createProcess(DepartmentProcess.class);
			
			DomainVO domain = WebServiceUtil.validateDomain(domainName);
			
			Collection<DepartmentVO> vos = process.queryByDomain(domain.getId());
			for(Iterator<DepartmentVO> it = vos.iterator(); it.hasNext();){
				SimpleDepartment dep = new SimpleDepartment();
				DepartmentUtil.convert(dep, it.next());
				cols.add(dep);
			}
			
		} catch (Exception e) {
			throw new DepartmentServiceFault(e.getMessage());
		}finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				throw new DepartmentServiceFault(e.getMessage());
			}
		}
		return cols;
	}

	/**
	 * 传入所有属性值创建一个部门
	 * @param id 部门id
	 * @param name 部门名称
	 * @param code 部门code
	 * @param superiorName 上级部门名称
	 * @param domainName 企业域名
	 * @return  -1:失败 ,0:成功
	 * @throws DepartmentServiceFault
	 */
	public int createDepartment(String id,String name, String code, String superiorName, String domainName) 
			throws DepartmentServiceFault{
		int result = -1;
		try {
			SimpleDepartment dep = new SimpleDepartment();
			dep.setId(id);
			dep.setName(name);
			dep.setCode(code);
			dep.setSuperiorName(superiorName);
			dep.setDomainName(domainName);
			result = createDepartment(dep);
		} catch (Exception e) {
			throw new DepartmentServiceFault(e.getMessage());
		}
		
		return result;
		
	}
	
	/**
	 * 传入键值对更新一个部门
	 * @param attributes 部门Map，key为属性名，value为属性值。如：{id=1,name=产品部}
	 * @return  -1:失败 ,0:成功
	 * @throws DepartmentServiceFault
	 */
	public int updateDepartment(Map<String,String> attributes)
			throws DepartmentServiceFault {
		int result = -1;
		try {
			if(attributes == null)
				throw new DepartmentServiceFault("部门Map不能为空.");
			SimpleDepartment dep = new SimpleDepartment();
			dep.setId(attributes.get("id"));
			dep.setName(attributes.get("name"));
			dep.setCode(attributes.get("code"));
			dep.setSuperiorName(attributes.get("superiorName"));
			dep.setDomainName(attributes.get("domainName"));
			result = updateDepartment(dep);
		} catch (Exception e) {
			throw new DepartmentServiceFault(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 传入Json格式字符串创建一个部门
	 * @param jsonStr
	 * @return  -1:失败 ,0:成功
	 * @throws DepartmentServiceFault
	 */
	public int createDepartmentFromJson(String jsonStr)
			throws DepartmentServiceFault {
		int result = -1;
		try {
			if(StringUtil.isBlank(jsonStr))
				throw new DepartmentServiceFault("Parameter 'jsonStr' can not be null.");
			SimpleDepartment dep = (SimpleDepartment) JsonUtil.toBean(jsonStr, SimpleDepartment.class);
			result = createDepartment(dep);
		} catch (Exception e) {
			throw new DepartmentServiceFault(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 传入Json格式字符串更新一个部门
	 * @param jsonStr
	 * @return  -1:失败 ,0:成功
	 * @throws DepartmentServiceFault
	 */
	public int updateDepartmentFromJson(String jsonStr)
			throws DepartmentServiceFault {
		int result = -1;
		try {
			if(StringUtil.isBlank(jsonStr))
				throw new DepartmentServiceFault("Parameter 'jsonStr' can not be null.");
			SimpleDepartment dep = (SimpleDepartment) JsonUtil.toBean(jsonStr, SimpleDepartment.class);
			result = updateDepartment(dep);
		} catch (Exception e) {
			throw new DepartmentServiceFault(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 传入XML格式字符串创建一个部门
	 * @param xmlStr
	 * @return  -1:失败 ,0:成功
	 * @throws DepartmentServiceFault
	 */
	public int createDepartmentFromXML(String xmlStr)
			throws DepartmentServiceFault {
		int result = -1;
		try {
			if(StringUtil.isBlank(xmlStr))
				throw new DepartmentServiceFault("Parameter 'xmlStr' can not be null.");
			SimpleDepartment dep = (SimpleDepartment) XmlUtil.toOjbect(xmlStr);
			result = createDepartment(dep);
		} catch (Exception e) {
			throw new DepartmentServiceFault(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 传入XML格式字符串更新一个部门
	 * @param xmlStr
	 * @return  -1:失败 ,0:成功
	 * @throws DepartmentServiceFault
	 */
	public int updateDepartmentFromXML(String xmlStr)
			throws DepartmentServiceFault {
		int result = -1;
		try {
			if(StringUtil.isBlank(xmlStr))
				throw new DepartmentServiceFault("Parameter 'xmlStr' can not be null.");
			SimpleDepartment dep = (SimpleDepartment) XmlUtil.toOjbect(xmlStr);
			result = updateDepartment(dep);
		} catch (Exception e) {
			throw new DepartmentServiceFault(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 传入主键返回部门对象格式化成JSON的字符串
	 * @param pk
	 * 			主键
	 * @return JSON字符串
	 * @throws DepartmentServiceFault
	 */
	public String findDepartmentFormat2Json(String pk)
			throws DepartmentServiceFault {
		SimpleDepartment dep = getDepartment(pk);
		return JsonUtil.toJson(dep);
	}
	
	/**
	 * 传入主键返回部门对象格式化成XML的字符串
	 * @param pk
	 * 			主键
	 * @return XML字符串
	 * @throws DepartmentServiceFault
	 */
	public String findDepartmentFormat2XML(String pk)
			throws DepartmentServiceFault {
		SimpleDepartment dep = getDepartment(pk);
		return XmlUtil.toXml(dep);
	}
	
	/**
	 * 传入企业域名称获取企业域下所有部门的集合格式化成JSON的字符串
	 * @param domainName 企业域名
	 * @return JSON字符串
	 * @throws DepartmentServiceFault
	 */
	public String getDepartmentsByDomainNameFormat2Json (String domainName)
			throws DepartmentServiceFault {
		Collection<SimpleDepartment> cols = getDepartmentsByDomainName(domainName);
		return JsonUtil.collection2Json(cols);
	}
	
	/**
	 * 传入企业域名称获取企业域下所有部门的集合格式化成XML的字符串
	 * @param domainName 企业域名
	 * @return XML字符串
	 * @throws DepartmentServiceFault
	 */
	public String getDepartmentsByDomainNameFormat2XML (String domainName)
			throws DepartmentServiceFault {
		Collection<SimpleDepartment> cols = getDepartmentsByDomainName(domainName);
		return XmlUtil.toXml(cols);
	}
	
}
