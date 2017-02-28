package cn.myapps.mobile2.login;

import cn.myapps.core.table.constants.MobileConstant2;
import cn.myapps.core.user.ejb.UserVO;

public class MbLoginXMLBuilder {
	
	private static String VERSION = "20598";
	
	/**
	 * 手机端登陆接口
	 * @param status
	 * @param errorMessage
	 * @return
	 */
	public static String toMobileXML(boolean status,String errorMessage,UserVO user){
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("<").append(MobileConstant2.TAG_LOGINDATA);
			if(status){
				sb.append(" ").append(MobileConstant2.ATT_STATUS).append("='").append("OK").append("'");
				sb.append(" ").append(MobileConstant2.ATT_VERSION).append("='").append(VERSION).append("'").append(">");
				sb.append("<").append(MobileConstant2.TAG_MESSAGE).append(">");
				sb.append("登录成功");
				sb.append("</").append(MobileConstant2.TAG_MESSAGE).append(">");
				sb.append("<").append("USERINFO");
				sb.append(" ").append("NAME").append("='").append(user.getName()).append("'");
				sb.append(" ").append("TELEPHONE").append("='").append(user.getTelephone()).append("'");
				sb.append(" ").append("EMAIL").append("='").append(user.getEmail()).append("'");
				sb.append("/>");
			}else{
				sb.append(" ").append(MobileConstant2.ATT_STATUS).append("='").append("NO").append("'>");
				sb.append("<").append(MobileConstant2.TAG_MESSAGE).append(">");
				sb.append(errorMessage);
				sb.append("</").append(MobileConstant2.TAG_MESSAGE).append(">");
			}
			sb.append("</").append(MobileConstant2.TAG_LOGINDATA).append(">");
			return sb.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
}
