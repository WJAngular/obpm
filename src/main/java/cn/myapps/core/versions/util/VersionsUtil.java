package cn.myapps.core.versions.util;

import java.util.Date;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.log4j.Logger;

import cn.myapps.constans.Environment;
import cn.myapps.core.versions.ejb.VersionsProcess;
import cn.myapps.core.versions.ejb.VersionsVO;
import cn.myapps.util.DateUtil;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

public class VersionsUtil {
	
	private final static Logger LOG = Logger.getLogger("VersionsUtil");

	public static void doRecord(){
		try {
			Environment evt = Environment.getInstance();
			String applicationRealPath = evt.getApplicationRealPath();
			if(!StringUtil.isBlank(applicationRealPath)){
				String path = "";
				if(applicationRealPath.endsWith("/") || applicationRealPath.endsWith("\\")){
					path = applicationRealPath+ "WEB-INF/timestamp.txt";
				}else {
					path = applicationRealPath+ "/WEB-INF/timestamp.txt";
				}
				File file = new File(path);
				if(file.exists()){
					execute(file);
				}
			}
			
		} catch (Exception e) {
			LOG.error("记录版本发现异常！");
			e.printStackTrace();
		}
	}
	
	private static void execute (File file) throws Exception {
		VersionsProcess vp = (VersionsProcess)ProcessFactory.createProcess(VersionsProcess.class);
		VersionsVO vo = new VersionsVO();
		BufferedReader breader = null;
		FileInputStream fileIn = new FileInputStream(file);
		Reader reader = new InputStreamReader(fileIn);
		breader = new BufferedReader(reader);
		String line;
		while ((line = breader.readLine()) != null
				&& line.trim().length() > 0) {
			int index = line.indexOf(":");
			String value = line.substring(
					index + 1, line.length())
					.trim();
			if(line.trim().startsWith("打包日期")){
				Date date = new Date();
				try {
					date = DateUtil.parseDateTime(compatibleFormat(value));
				} catch (Exception e) {
					LOG.info("打包日期转换异常！The convert date is：" + value);
				}
				vo.setUpgrade_date(date);
			}else if (line.trim().startsWith("Last Changed Rev")){
				vo.setVersion_number(value);
			}else if (line.trim().startsWith("打包版本")){
				vo.setVersion_name(value);
			}
		}
		vo.setType(VersionsVO.TYPE_SOURCECODE_UPGRADE);
		vp.doRecordVersions(vo);
	}
	
	private static String compatibleFormat(String s) throws Exception {
		if(!StringUtil.isBlank(s)){
			if(s.indexOf(" ") > 0){
				if(s.split(":").length == 1){
					s += ":00:00";
				}else if(s.split(":").length == 2){
					s += ":00";
				}
			}else{
				s += " 00:00:00";
			}
		}
		return s;
	}
	
	public static String toHTMLText(){
		StringBuilder html = new StringBuilder();
		try {
			VersionsProcess vp = (VersionsProcess)ProcessFactory.createProcess(VersionsProcess.class);
			VersionsVO vo = vp.findCurrVersionByType(VersionsVO.TYPE_SOURCECODE_UPGRADE);
			if(vo != null){
				html.append("<font color=\"red\">{*[cn.myapps.core.versions.curr_info]*}：");
				html.append(vo.getVersion_name()).append(" ").append(vo.getVersion_number());
				html.append("</font>");
			}else{
				html.append("<font color=\"red\">{*[cn.myapps.core.versions.not.find]*}!</font>");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return html.toString();
	}
	
}
