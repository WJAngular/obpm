package cn.myapps.webservice.util;

import cn.myapps.core.department.ejb.DepartmentProcess;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.webservice.WebServiceUtil;
import cn.myapps.webservice.fault.DepartmentServiceFault;
import cn.myapps.webservice.model.SimpleDepartment;

/**
 * DepartmentService工具类
 * @author ivan
 *
 */
public class DepartmentUtil {
	/**
	 * Convert DepartmentVO to SimpleDepartment
	 * 
	 * @param dep
	 *            SimpleDepartment
	 * @param vo
	 *            DepartmentVO
	 * @return SimpleDepartment
	 * @throws DepartmentServiceFault
	 */
	public static SimpleDepartment convert(SimpleDepartment dep, DepartmentVO vo)
			throws DepartmentServiceFault {
		try {
			dep.setId(vo.getId());
			dep.setName(vo.getName());
			dep.setCode(vo.getCode());
			dep.setLevel(vo.getLevel());
			dep.setDomainName(vo.getDomain().getName());
			dep.setSuperiorName(vo.getSuperior() != null ? vo.getSuperior().getName() : null);
			return dep;
		} catch (Exception e) {
			throw new DepartmentServiceFault(e.getMessage());
		}
	}
	
	/**
	 * Convert SimpleDepartment to DepartmentVO
	 * 
	 * @param vo
	 *            DepartmentVO
	 * @param dep
	 *            SimpleDepartment
	 * @return DepartmentVO
	 * @throws DepartmentServiceFault
	 */
	public static DepartmentVO convert(DepartmentVO vo, SimpleDepartment dep)
			throws DepartmentServiceFault {
		try {
			DomainVO domain = WebServiceUtil
				.validateDomain(dep.getDomainName());
			DepartmentProcess process = (DepartmentProcess) ProcessFactory
				.createProcess(DepartmentProcess.class);
			if (dep.getSuperiorName() == null) {
				if (vo.getSuperior() == null) {
					// 默认设置根部门为上级部门
					DepartmentVO superior = (DepartmentVO) process
							.getRootDepartmentByApplication(null, domain.getId());
					vo.setSuperior(superior);
				}
			} else {
				DepartmentVO superior = (DepartmentVO) process.doViewByName(dep
						.getSuperiorName(), domain.getId());
				if (superior == null) {
					// 上级部门不存在数据库
					throw new DepartmentServiceFault("上级部门["
							+ dep.getSuperiorName() + "]不存在企业域["
							+ domain.getName() + "]下.");
				}
				vo.setSuperior(superior);
			}
			vo.setId(dep.getId() != null ? dep.getId() : vo.getId());
			vo.setName(dep.getName() != null ? dep.getName() : vo.getName());
			vo.setCode(dep.getCode() != null ? dep.getCode() : vo.getCode());
			vo.setLevel(vo.getSuperior() != null ? vo.getSuperior()
							.getLevel() + 1 : 0);// 根据上级设置level
			vo.setDomain(domain);
			vo.setDomainid(domain.getId());

			return vo;
		} catch (Exception e) {
			throw new DepartmentServiceFault(e.getMessage());
		}
	}
}
