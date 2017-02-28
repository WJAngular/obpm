package cn.myapps.webservice.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.util.ObjectUtil;
import cn.myapps.webservice.model.SimpleDomain;

/**
 * DomainService工具类
 * @author ivan
 *
 */
public class DomainUtil {
	/**
	 * 转换为简单域对象列表
	 * 
	 * @param domainList
	 *            企业域列表
	 * @return
	 */
	public static Collection<SimpleDomain> convertToSimple(Collection<?> domainList) {
		Collection<SimpleDomain> sDomainList = new ArrayList<SimpleDomain>();
		for (Iterator<?> iterator = domainList.iterator(); iterator.hasNext();) {
			DomainVO domain = (DomainVO) iterator.next();
			sDomainList.add(convertToSimple(domain));
		}

		return sDomainList;
	}

	/**
	 * 转换为简单域对象
	 * 
	 * @param domain
	 *            企业域
	 * @return
	 */
	public static SimpleDomain convertToSimple(DomainVO domain) {
		if (domain != null) {
			SimpleDomain sDomain = new SimpleDomain();

			try {
				ObjectUtil.copyProperties(sDomain, domain);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			return sDomain;
		}
		return null;
	}
}
