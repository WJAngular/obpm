package cn.myapps.webservice.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorHIS;
import cn.myapps.core.workflow.storage.runtime.ejb.RelationHIS;
import cn.myapps.util.ObjectUtil;
import cn.myapps.webservice.model.SimpleActorHIS;
import cn.myapps.webservice.model.SimpleRelationHIS;

/**
 * FlowHistoryService工具类
 * @author ivan
 *
 */
public class FlowHistoryUtil {
	
	/**
	 * Convert Collection<RelationHIS> to Collection<SimpleRelationHIS>
	 * <br />把RelationHIS集合转换为SimpleRelationHIS集合
	 * @param data
	 * 			Collection<RelationHIS>
	 * @return 
	 * 			Collection<SimpleRelationHIS>
	 * @throws Exception
	 */
	public static Collection<SimpleRelationHIS> convertToSimpleDatas(Collection<RelationHIS> data)throws Exception{
		if(data == null)
			return null;
		Collection<SimpleRelationHIS> simpleData = new ArrayList<SimpleRelationHIS>();
		for (Iterator<RelationHIS> iterator = data.iterator(); iterator.hasNext();) {
			RelationHIS relationHIS = iterator.next();
			SimpleRelationHIS simpleRelationHIS = convertToSimpleRelationHIS(relationHIS);
			if(simpleRelationHIS != null)
				simpleData.add(simpleRelationHIS);
		}
		return simpleData;
	}
	
	/**
	 * Convert RelationHIS to SimpleRelationHIS
	 * <br />把RelationHIS转换为SimpleRelationHIS
	 * @param relationHIS
	 * 			RelationHIS
	 * @return
	 * 			SimpleRelationHIS
	 * @throws Exception
	 */
	public static SimpleRelationHIS convertToSimpleRelationHIS(RelationHIS relationHIS) throws Exception{
		if(relationHIS == null)
			return null;
		SimpleRelationHIS simpleRelationHIS = new SimpleRelationHIS();
		ObjectUtil.copyProperties(simpleRelationHIS, relationHIS);
		simpleRelationHIS.setActorhiss(convertToSimpleActorDatas(relationHIS.getActorhiss()));
		return simpleRelationHIS;
	}
	
	/**
	 * Convert Collection<ActorHIS> to Collection<SimpleActorHIS>
	 * <br />把ActorHIS集合转换为SimpleActorHIS集合
	 * @param actorhis
	 * 			Collection<ActorHIS>
	 * @return
	 * 			Collection<SimpleActorHIS>
	 * @throws Exception
	 */
	public static Collection<SimpleActorHIS> convertToSimpleActorDatas(Collection<ActorHIS> actorhis)throws Exception{
		if(actorhis == null)
			return null;
		Collection<SimpleActorHIS> sActorhis = new ArrayList<SimpleActorHIS>();
		for (Iterator<ActorHIS> iterator = actorhis.iterator(); iterator.hasNext();) {
			ActorHIS actor = (ActorHIS) iterator.next();
			SimpleActorHIS sActor = convertToSimpleActorHIS(actor);
			if(sActor != null)
				sActorhis.add(sActor);
		}
		return sActorhis;
	}
	
	/**
	 * Convert ActorHIS to SimpleActorHIS
	 * <br />把ActorHIS转换为SimpleActorHIS
	 * @param actorhis
	 * 			ActorHIS
	 * @return
	 * 			SimpleActorHIS
	 * @throws Exception
	 */
	public static SimpleActorHIS convertToSimpleActorHIS(ActorHIS actorhis)throws Exception{
		if(actorhis == null)
			return null;
		SimpleActorHIS sActorhis = new SimpleActorHIS();
		ObjectUtil.copyProperties(sActorhis, actorhis);
		Collection<String> userList = new ArrayList<String>();
		if(actorhis.getAllUser() != null)
			for (Iterator<UserVO> iterator = actorhis.getAllUser().iterator(); iterator.hasNext();) {
				UserVO user = (UserVO) iterator.next();
				if(user != null)
					userList.add(user.getId());
			}
		sActorhis.setAlluserid(userList);
		return sActorhis;
	}
}
