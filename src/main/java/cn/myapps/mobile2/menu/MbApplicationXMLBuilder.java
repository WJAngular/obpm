package cn.myapps.mobile2.menu;

import java.util.Collection;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.deploy.application.action.ApplicationHelper;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.table.constants.MobileConstant2;
import cn.myapps.core.user.action.WebUser;

public class MbApplicationXMLBuilder {
	public static String toMobileXML(WebUser user,ParamsTable params) throws Exception{
		StringBuffer sb = new StringBuffer();
		try{
			ApplicationHelper helper = new ApplicationHelper();
			/**
			 * 根据企业域获取软件集合
			 */
			Collection<ApplicationVO> applicationList = helper.getListByWebUser(user);
			sb.append("<").append(MobileConstant2.TAG_APPLICTIONDATA).append(">");
			if(applicationList != null && !applicationList.isEmpty()){
				for(ApplicationVO appvo:applicationList){
					if(appvo != null && appvo.isActivated()){
						sb.append("<").append(MobileConstant2.TAG_APPLICATION);
						sb.append(" ").append(MobileConstant2.ATT_ID).append("='").append(appvo.getId()).append("'");
						sb.append(" ").append(MobileConstant2.ATT_NAME).append("='").append(appvo.getName()).append("'");
						sb.append(">");
						sb.append("</").append(MobileConstant2.TAG_APPLICATION).append(">");
					}
				}
			}
			sb.append("</").append(MobileConstant2.TAG_APPLICTIONDATA).append(">");
			return sb.toString();
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}
}
