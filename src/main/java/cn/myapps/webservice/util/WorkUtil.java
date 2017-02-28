package cn.myapps.webservice.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.dynaform.work.ejb.WorkVO;
import cn.myapps.core.workflow.storage.runtime.ejb.Circulator;
import cn.myapps.util.ObjectUtil;
import cn.myapps.webservice.model.SimpleCirculator;
import cn.myapps.webservice.model.SimpleWork;

/**
 * WorkService工具类
 * @author ivan
 *
 */
public class WorkUtil {
	
	/**
	 * 转换DataPackage中的WorkVO为SimpleWork
	 * 
	 * @param dataPackage
	 * @return Collection
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Collection<SimpleWork> convertToSimpleDatas (DataPackage<WorkVO> dataPackage)
			throws IllegalAccessException, InvocationTargetException {
		Collection<SimpleWork> datas = new ArrayList<SimpleWork>();
		if(dataPackage != null)
			for (Iterator<WorkVO> iterator = dataPackage.datas.iterator(); iterator.hasNext();) {
				WorkVO workVO = (WorkVO) iterator.next();
				SimpleWork sWork = new SimpleWork();
				ObjectUtil.copyProperties(sWork, workVO);
				datas.add(sWork);
			}
		return datas;
	}
	
	/**
	 * 转换DataPackage中的Circulator为SimpleCirculator
	 * 
	 * @param dataPackage
	 * @return Collection
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Collection<SimpleCirculator> convertToSimpleDatas4Cc (DataPackage<Circulator> dataPackage)
			throws IllegalAccessException, InvocationTargetException {
		Collection<SimpleCirculator> datas = new ArrayList<SimpleCirculator>();
		if(dataPackage != null)
			for (Iterator<Circulator> iterator = dataPackage.datas.iterator(); iterator.hasNext();) {
				Circulator vo = (Circulator) iterator.next();
				SimpleCirculator svo = new SimpleCirculator();
				ObjectUtil.copyProperties(svo, vo);
				datas.add(svo);
			}
		return datas;
	}
}
