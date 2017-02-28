package cn.myapps.qm.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.myapps.qm.answer.ejb.AnswerVO;
import cn.myapps.qm.questionnaire.ejb.QuestionnaireVO;
import cn.myapps.util.StringUtil;

public class CensusUtil {
	
	public static int totalScore(QuestionnaireVO vo) throws Exception {
		int score = 0;
		String json = vo.getContent();
		JSONArray array = new JSONArray(json);
		for (int b = 0; b < array.length(); b++) {
			JSONObject object = array.getJSONObject(b);
			String type = object.getString("type");
			if(type.indexOf("test") >=0){
				int code = object.getInt("code");
				score += code;
			}else if("codematrix".equals(type)){
				String left = object.getString("leftLabel");
				String[] lefts = left.split("\n");
				JSONArray options = object.getJSONArray("options");
				int heightCode = 0;
				for(int i= 0;i<options.length();i++){
					JSONObject option = options.getJSONObject(i);
					boolean isNull = option.getBoolean("isNull");
					if(!isNull){
						int code = option.getInt("code");
						if(heightCode < code){
							heightCode = code;
						}
					}
				}
				score += heightCode * (lefts.length);
			}else if("coderadio".equals(type)){
				JSONArray options = object.getJSONArray("options");
				int heightCode = 0;
				for(int i= 0;i<options.length();i++){
					JSONObject option = options.getJSONObject(i);
					boolean isNull = option.getBoolean("isNull");
					if(!isNull){
						int code = option.getInt("code");
						if(heightCode < code){
							heightCode = code;
						}
					}
				}
				score += heightCode;
			}else if("codecheck".equals(type)){
				JSONArray options = object.getJSONArray("options");
				int heightCode = 0;
				for(int i= 0;i<options.length();i++){
					JSONObject option = options.getJSONObject(i);
					boolean isNull = option.getBoolean("isNull");
					if(!isNull){
						int code = option.getInt("code");
						heightCode += code;
					}
				}
				score += heightCode;
			}
		}
		return score;
	}
	
	public static int currentScore(AnswerVO vo) throws Exception {
		int score = 0;
		String json = vo.getAnswer();
		JSONArray answers = new JSONArray(json);
		for (int b = 0; b < answers.length(); b++) {
			JSONObject object = answers.getJSONObject(b);
			String type = object.getString("type");
			if(type.indexOf("test") >=0 || type.indexOf("code") >=0){
				int code = object.getInt("code");
				if("testradio".equals(type)){
					JSONArray options = object.getJSONArray("options");
					for(int i=0;i<options.length();i++){
						JSONObject option = options.getJSONObject(i);
						boolean isAnswer = option.getBoolean("isAnswer");
						if(isAnswer){
							score += code;
							break;
						}
					}
				}else if("testcheck".equals(type)){
					JSONArray options = object.getJSONArray("options");
					int rightNumber = 0;
					int errorNumber = 0;
					for (int i = 0; i < options.length(); i++) {
						JSONObject option = options.getJSONObject(i);
						boolean isAnswer = option.getBoolean("isAnswer");
						if (isAnswer) {
							rightNumber++;
						}else{
							errorNumber++;
						}
					}
					String right = object.getString("right");
					String rights[] = right.split("&&");
					if(rightNumber == rights.length && errorNumber==0){
						score += code;
					}
				}else if("testinput".equals(type)){
					String standardanswer = object.getString("standardanswer");
					String value = object.getString("value");
					if(standardanswer.equals(value)){
						score += code;
					}
				}else if("coderadio".equals(type) || "codecheck".equals(type) || "codematrix".equals(type)){
					score += code;
				}
			}
		}
		return score;
	}
	
	public static String voteNumber(Collection<AnswerVO> datas,String holder_id) throws Exception{
		
		JSONArray newAnswer = new JSONArray();
		
		for (Iterator<AnswerVO> iter = datas.iterator(); iter.hasNext();) {
			AnswerVO vo = iter.next();
			String json = vo.getAnswer();
			JSONArray answers = new JSONArray(json);
			for (int b = 0; b < answers.length(); b++) {
				JSONObject object = answers.getJSONObject(b);
				String id = object.getString("id");
				if(id.equals(holder_id)){
					JSONArray options = object.getJSONArray("options");
					for(int i=0;i<options.length();i++){
						JSONObject option = options.getJSONObject(i);
						newAnswer.put(option);
					}
					break;
				}
			}
		}
		return newAnswer.toString();
	}
	
	/**
	 * 统计答案结果
	 * 
	 * @param datas
	 *            答案集合
	 * @return
	 */
	public static String createCensus(Collection<AnswerVO> datas) throws Exception {

		Map<String, JSONArray> map = new LinkedHashMap<String, JSONArray>();

		JSONArray main = new JSONArray();

		if (datas != null) {
			for (Iterator<AnswerVO> iter = datas.iterator(); iter.hasNext();) {
				AnswerVO vo = iter.next();
				String json = vo.getAnswer();
				JSONArray answers = new JSONArray(json);
				for (int b = 0; b < answers.length(); b++) {
					JSONObject object = answers.getJSONObject(b);
					String id = object.getString("id");
					String type = object.getString("type");
					if ("radio".equals(type) || "check".equals(type) || "voteradio".equals(type)
							|| "votecheck".equals(type) || "coderadio".equals(type) || "codecheck".equals(type)
							|| "testradio".equals(type) || "testcheck".equals(type) || "matrixradio".equals(type) 
							|| "matrixcheck".equals(type) || "codematrix".equals(type)){
						JSONArray options = object.getJSONArray("options");
						JSONArray array = map.get(id);
						if (array == null) {
							array = new JSONArray();
							map.put(id, array);
						}

						for (int c = 0; c < options.length(); c++) {
							array.put(options.getJSONObject(c));
						}
					}
				}
			}
			for (Iterator<String> iter = map.keySet().iterator(); iter.hasNext();) {
				String id = iter.next();
				JSONArray array = map.get(id);

				JSONObject object = new JSONObject();
				object.put("id", id);
				object.put("data", array);
				main.put(object);
			}
		}

		return main.toString();
	}
	
	/**
	 * 统计问卷数据
	 * 
	 * @param datas
	 *            答案集合
	 * @return
	 */
	public static String reportForm(Collection<AnswerVO> datas) throws Exception {

		Map<String, JSONObject> map = new LinkedHashMap<String, JSONObject>();

		JSONArray main = new JSONArray();

		if(datas==null||datas.isEmpty())
			return main.toString();
		
		String content = ((ArrayList<AnswerVO>)datas).get(0).getContent();
		JSONArray contents = new JSONArray(content);
		for(int c = 0; c < contents.length(); ++c){
			JSONObject object = contents.getJSONObject(c);
			String type = object.getString("type");
			if ("radio".equals(type) || "check".equals(type) || "voteradio".equals(type) || "votecheck".equals(type)
					|| "coderadio".equals(type) || "codecheck".equals(type) || "testradio".equals(type)
					|| "testcheck".equals(type) ) {
				JSONArray array = object.getJSONArray("options");
				JSONObject data = new JSONObject();
				data.put("type", type);
				data.put("data", array);
				String id = object.getString("id");
				map.put(id, data);
			}
			if( "matrixradio".equals(type) || "matrixcheck".equals(type) || "codematrix".equals(type)){
				String[] leftLabel = object.getString("leftLabel").split("\n");
				JSONArray options = object.getJSONArray("options");
  
				JSONObject legend = new JSONObject();
				JSONArray xAxis = new JSONArray();
				JSONArray series = new JSONArray();
				
				legend.put("data",leftLabel);
				
				for(int o = 0; o<options.length();++o){
					xAxis.put(options.getJSONObject(o).get("name"));
				}
				
				for(String label:leftLabel){
					JSONObject serieses = new JSONObject();
					serieses.put("name", label);
					JSONArray data = new JSONArray();
					for(int d = 0; d < options.length(); ++d){
						data.put(options.get(d));
					}
					serieses.put("data", data);
					series.put(serieses);
				}
				
				JSONObject data = new JSONObject();
				data.put("type", type);
				data.put("legend", legend);
				data.put("series", series);
				data.put("xAxis", xAxis);
				
				String id = object.getString("id");
				map.put(id, data);
			}
		}
		
		for (AnswerVO answerVO : datas) {
			String answerStr = answerVO.getAnswer();
			String userName = answerVO.getUserName();
			String userDepartment = answerVO.getUserDepartment();
			JSONArray answerJson = new JSONArray(answerStr);
			
			for (int a = 0; a < answerJson.length(); ++a) {
				JSONObject object = answerJson.getJSONObject(a);
				String type = object.getString("type");
				if ("radio".equals(type) || "check".equals(type) || "voteradio".equals(type) || "votecheck".equals(type)
						|| "coderadio".equals(type) || "codecheck".equals(type) || "testradio".equals(type)
						|| "testcheck".equals(type)) {
					String id = object.getString("id");
					JSONArray options = object.getJSONArray("options");

					JSONObject data = map.get(id);

					for (int c = 0; c < options.length(); c++) {
						JSONObject answer = options.getJSONObject(c);
						answer.put("userName", userName);
						answer.put("userDepartment", userDepartment);
						data.getJSONArray("data").put(answer);
					}
				}
				if( "matrixradio".equals(type) || "matrixcheck".equals(type) || "codematrix".equals(type)){
					String id = object.getString("id");
					JSONArray options = object.getJSONArray("options");
					JSONObject data = map.get(id);
					for(int o = 0;o<options.length();++o){
						JSONObject option = options.getJSONObject(o);
						String value = option.optString("value");
						String name_value = option.optString("name");
						int end = name_value.lastIndexOf(value);
						String name = name_value.substring(0, end - 1);
						JSONArray series = data.getJSONArray("series");
						for(int s = 0; s<series.length();++s){
							JSONObject seriesArray = series.getJSONObject(s);
							if(!name.equalsIgnoreCase(seriesArray.optString("name")))
								continue;
							
							JSONObject valueObject = new JSONObject();
							valueObject.put("name", value);
							valueObject.put("userName", userName);
							valueObject.put("userDepartment", userDepartment);
							seriesArray.getJSONArray("data").put(valueObject);
						}
					}
				}
			}
		}
		for (Iterator<String> iter = map.keySet().iterator(); iter.hasNext();) {
			String id = iter.next();
			JSONObject data = map.get(id);
			String type = data.getString("type");
			if ("radio".equals(type) || "check".equals(type) || "voteradio".equals(type) || "votecheck".equals(type)
					|| "coderadio".equals(type) || "codecheck".equals(type) || "testradio".equals(type)
					|| "testcheck".equals(type)) {
				JSONArray array = data.getJSONArray("data");
				Map<String, Integer> valueMap = new LinkedHashMap<String, Integer>();
				Map<String, JSONArray> nameMap = new LinkedHashMap<String, JSONArray>();
				for (int i = 0; i < array.length(); ++i) {
					JSONObject value = array.getJSONObject(i);
					String name = value.getString("name");
					Integer count = valueMap.get(name);
					valueMap.put(name, count == null ? 0 : ++count);
					String userName = value.optString("userName");
					String userDepartment = value.optString("userDepartment");
					JSONObject userInfo = new JSONObject();
					userInfo.put("name", userName);
					userInfo.put("department", userDepartment);
					JSONArray nameArray = nameMap.get(name);
					nameArray = nameArray == null ? new JSONArray() : nameArray;
					if(!StringUtil.isBlank(userName))
						nameArray.put(userInfo);
					nameMap.put(name, nameArray);
				}
				
				JSONObject object = new JSONObject();
				object.put("id", id);
				object.put("type", type); 
				object.put("data", valueMap);
				object.put("userName", nameMap);
				main.put(object);
			}
			if( "matrixradio".equals(type) || "matrixcheck".equals(type) || "codematrix".equals(type)){
				JSONObject legend = data.getJSONObject("legend");
				JSONArray xAxis = data.getJSONArray("xAxis");
				JSONArray series = data.getJSONArray("series");
				for(int s = 0 ;s<series.length();++s){
					JSONArray value = series.getJSONObject(s).getJSONArray("data");
					Map<String, Integer> valueMap = new LinkedHashMap<String, Integer>();
					Map<String, JSONArray> nameMap = new LinkedHashMap<String, JSONArray>();
					for(int v= 0;v<value.length();++v){
						JSONObject countValue = value.getJSONObject(v);
						String name = countValue.getString("name");
						Integer count = valueMap.get(name);
						valueMap.put(name, count == null ? 0 : ++count);
						String userName = countValue.optString("userName");
						String userDepartment = countValue.optString("userDepartment");
						JSONObject userInfo = new JSONObject();
						userInfo.put("name", userName);
						userInfo.put("department", userDepartment);
						JSONArray nameArray = nameMap.get(name);
						nameArray = nameArray == null ? new JSONArray() : nameArray;
						if(!StringUtil.isBlank(userName))
							nameArray.put(userInfo);
						nameMap.put(name, nameArray);
					}
					series.getJSONObject(s).remove("data");
					series.getJSONObject(s).put("data", valueMap.values());
					series.getJSONObject(s).remove("userName");
					series.getJSONObject(s).put("userName", nameMap.values());
				}
				JSONObject object = new JSONObject();
				object.put("id", id);
				object.put("type", type);
				object.put("series", series);
				object.put("legend", legend);
				object.put("xAxis", xAxis);
				main.put(object);
			}
		}
		return main.toString();
	}
	
	/**
	 * 统计问卷数据
	 * 
	 * @param datas
	 *            答案集合
	 * @return
	 */
	public static Collection<AnswerVO> inputReport(Collection<AnswerVO> datas,String q_Id) throws Exception {
		Collection<AnswerVO> data = new ArrayList<AnswerVO>();
		for(AnswerVO answerVO:datas){
			JSONArray answerArr = new JSONArray(answerVO.getAnswer());
			JSONArray answerData = new JSONArray();
			for(int a = 0; a<answerArr.length();++a){
				JSONObject answerJson = answerArr.getJSONObject(a);
				if(!q_Id.equalsIgnoreCase(answerJson.optString("id")))
					continue;
				answerData.put(answerJson);
			}
			answerVO.setAnswer(answerData.toString());
			data.add(answerVO);
		}
		return data;
	}
}
