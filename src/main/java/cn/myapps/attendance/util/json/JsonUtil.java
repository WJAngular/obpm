package cn.myapps.attendance.util.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import cn.myapps.pm.task.ejb.Log;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;

/**
 * Json与Java Bean转换工具类
 * 
 * <p>为PM的JSON数据增加日期格式转换、属性过滤<p/>
 * @author Happy
 */
public class JsonUtil {
	
	private static JsonConfig jsonConfig = new JsonConfig();  
	
	static{
		jsonConfig.registerJsonValueProcessor(Date.class,  
	            new JsonDateValueProcessor());
		JSONUtils.getMorpherRegistry().registerMorpher(  
                new DateMorpher(new String[] { "yyyy-MM-dd HH:mm" })); 
	}
	
	/**
	 * JSON字符串转Java Bean的集合
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static Collection<?> toCollection(String json,Class<?> clazz){
		JSONArray jsonArray = JSONArray.fromObject(json, jsonConfig);
		return JSONArray.toCollection(jsonArray, clazz);
	}
	
	/**
	 * Java Bean集合转JSON字符串
	 * @param list
	 * @return
	 */
	public static String list2JSON(Collection<?> list){
		JSONArray jsonArray = JSONArray.fromObject(list, jsonConfig);
		
		return jsonArray.toString();
		
	}
	
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		Collection<Log> logs = new ArrayList<Log>();
		Log log = new Log();
		log.setOperationType(0);
		log.setOperationDate(new Date());
		log.setSummary("文档编写");
		log.setUserId("12312asdasd");
		log.setUserName("happy");
		
		logs.add(log);
		
		String json = list2JSON(logs);
		System.out.println(json);
		
		Collection<Log> list = (Collection<Log>) toCollection(json, Log.class);
		for(Log l : list){
			System.out.println(l.getOperationDate());
		}
	}
    

}
