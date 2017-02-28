package cn.myapps.mobile2.homepage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.core.dynaform.pending.ejb.PendingVO;
import cn.myapps.core.homepage.action.HomePageHelper;
import cn.myapps.core.personalmessage.action.PersonalMessageHelper;
import cn.myapps.core.personalmessage.ejb.PersonalMessageVO;
import cn.myapps.core.table.constants.MobileConstant2;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserDefined;
import cn.myapps.core.widget.action.PageWidgetHelper;
import cn.myapps.core.widget.ejb.PageWidget;
import cn.myapps.util.StringUtil;

public class MbMessageXMLBuilder {

	/**
	 * 手机端最新接口 获取首页widget以及站内信息
	 * 
	 * @param user
	 * @param params
	 * @return
	 * @throws JSONException
	 * @throws Exception
	 */
	public static String toMessageXML(WebUser user, ParamsTable params) throws JSONException {
		try {
			StringBuffer sb = new StringBuffer();
			PersonalMessageHelper pmh = new PersonalMessageHelper();
			DataPackage<PersonalMessageVO> datas = MbMessageHelper.getMessageList(user, params);
			if (datas != null && datas.rowCount > 0) {
				sb.append("<").append("MESSAGEDATA");
				sb.append(" ").append("SIZE").append("='").append(datas.rowCount).append("'");
				sb.append(">");

				for (PersonalMessageVO messagvo : datas.datas) {
					StringBuffer messageSB = new StringBuffer();
					messageSB.append("<").append(MobileConstant2.TAG_MESSAGE).append(" ");
					messageSB.append(MobileConstant2.ATT_ID).append("='").append(messagvo.getId()).append("'")
							.append(" ");
					if (StringUtil.isBlank(messagvo.getSenderId())) {
						messageSB.append(MobileConstant2.ATT_SENDERID).append("='").append("系统").append("'")
								.append(" ");
					} else {
						messageSB.append(MobileConstant2.ATT_SENDERID).append("='")
								.append(pmh.findUserName(messagvo.getSenderId())).append("'").append(" ");
					}
					messageSB.append(MobileConstant2.ATT_ISREAD).append("='").append(messagvo.isRead()).append("'")
							.append(" ");
					messageSB
							.append(MobileConstant2.ATT_CONTENT)
							.append("='")
							.append(MbHomePageHelper.replacSpecial(MbMessageHelper.replaceHTML(messagvo.getBody()
									.getContent()))).append("'>");
					messageSB.append(MbHomePageHelper.replacSpecial(MbMessageHelper.replaceHTML(messagvo.getBody()
							.getTitle())));
					messageSB.append("</").append(MobileConstant2.TAG_MESSAGE).append(">");
					sb.append(messageSB);
				}

				sb.append("</").append("MESSAGEDATA").append(">");
			}

			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
