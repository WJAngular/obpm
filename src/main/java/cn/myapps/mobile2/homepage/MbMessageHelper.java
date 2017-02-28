package cn.myapps.mobile2.homepage;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import cn.myapps.base.action.BaseAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.constans.Web;
import cn.myapps.core.personalmessage.ejb.MessageBody;
import cn.myapps.core.personalmessage.ejb.PersonalMessageProcess;
import cn.myapps.core.personalmessage.ejb.PersonalMessageVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

public class MbMessageHelper {

	public static String getMessageData(WebUser user) throws Exception {
		try {
			int sysCount = getMessageCount(user, "0");
			boolean sysNoRead = isMessageNoRead(user, "0");
			int personalCount = getMessageCount(user, "1");
			boolean personalNoRead = isMessageNoRead(user, "1");

			JSONObject object = new JSONObject();
			object.put("SYSCOUNT", sysCount);
			object.put("SYSNOREAD", sysNoRead);
			object.put("PERSONALCOUNT", personalCount);
			object.put("PERSONALNOREAD", personalNoRead);
			return object.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 判断是否存在未读短信
	 * 
	 * @param user
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static boolean isMessageNoRead(WebUser user, String type) throws Exception {
		try {
			PersistenceUtils.beginTransaction();
			ParamsTable params = new ParamsTable();
			params.setParameter("messageType", type);
			params.setParameter("_orderby", "sendDate");
			params.setParameter("_desc", "desc");
			params.setParameter("_isRead", false);
			PersonalMessageProcess process = (PersonalMessageProcess) ProcessFactory
					.createProcess(PersonalMessageProcess.class);
			DataPackage<PersonalMessageVO> datas = process.doInbox(user.getId(), params);
			if (datas != null && datas.datas.size() > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			PersistenceUtils.closeSessionAndConnection();
		}
		return false;
	}

	/**
	 * 获取短信数量
	 * 
	 * @param user
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static int getMessageCount(WebUser user, String type) throws Exception {
		try {
			PersistenceUtils.beginTransaction();
			ParamsTable params = new ParamsTable();
			params.setParameter("messageType", type);
			params.setParameter("_orderby", "sendDate");
			params.setParameter("_desc", "desc");
			PersonalMessageProcess process = (PersonalMessageProcess) ProcessFactory
					.createProcess(PersonalMessageProcess.class);
			DataPackage<PersonalMessageVO> datas = process.doInbox(user.getId(), params);
			if (datas != null && datas.datas.size() > 0) {
				return datas.rowCount;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			PersistenceUtils.closeSessionAndConnection();
		}
		return 0;
	}

	public static DataPackage<PersonalMessageVO> getMessageList(WebUser user, ParamsTable params) throws Exception {
		try {
			PersonalMessageProcess process = (PersonalMessageProcess) ProcessFactory
					.createProcess(PersonalMessageProcess.class);
			DataPackage<PersonalMessageVO> datas = process.doInbox(user.getId(), params);
			return datas;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

	/**
	 * 已读功能
	 * 
	 */
	public static String doReadMessage(ParamsTable params) throws Exception {
		try {
			String msgId = (String) params.getParameter("messageid");
			if (msgId == null)
				return null;
			PersonalMessageProcess process = (PersonalMessageProcess) ProcessFactory
					.createProcess(PersonalMessageProcess.class);
			PersonalMessageVO vo = (PersonalMessageVO) process.doView(msgId);
			vo.setRead(true);
			process.doUpdate(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 站内短信已读功能
	 */
	public static String doReplyMessage(ParamsTable params) throws Exception {
		try {
			MessageBody msg_body = new MessageBody();
			String msg_id = params.getParameterAsString("message_id");
			String msg_content = params.getParameterAsString("message_content");
			String msg_title = params.getParameterAsString("message_title");
			PersonalMessageProcess process = (PersonalMessageProcess) ProcessFactory
					.createProcess(PersonalMessageProcess.class);
			PersonalMessageVO pmVO = (PersonalMessageVO) process.doView(msg_id);
			PersonalMessageVO pmR = new PersonalMessageVO();
			WebUser user = (WebUser) BaseAction.getContext().getSession().get(Web.SESSION_ATTRIBUTE_FRONT_USER);
			if (pmVO != null) {
				if (user != null && pmVO.getBody() != null) {
					pmR.setSenderId(user.getId());
					pmR.setDomainid(user.getDomainid());
					msg_body.setTitle("Re" + msg_title);
					msg_body.setContent(msg_content);
					pmR.setBody(msg_body);
					pmR.getBody().setDomainid(user.getDomainid());
					pmR.setReceiverId(pmVO.getSenderId());// 将原来发送者ID变成接收者ID
				}
				if (StringUtil.isBlank(pmVO.getSenderId())) {
					return "{*[could.not.find.sender]*}";
				}

				String[] idArray = pmR.getReceiverId().split(";");// 多个用户用“;”分隔
				((PersonalMessageProcess) process).doCreateByUserIds(idArray, pmR);
				HttpServletRequest request = (HttpServletRequest) BaseAction.getContext().get(
						ServletActionContext.HTTP_REQUEST);
				request.setAttribute("retTag", "success");
				return "SUCCESS";
			} else {
				return "{*[could.not.find.sender]*}";
			}
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	/**
	 * 替换文本中html标签
	 * 
	 * @param htmlString
	 * @return noHTMLString
	 * @author kharry
	 * @throws
	 */
	public static String replaceHTML(String htmlString) {
		String noHTMLString = "";
		if (htmlString != null) {
			noHTMLString = htmlString.replaceAll("</?[^>]+>", "");
			noHTMLString = noHTMLString.replaceAll("\\&nbsp;", " ");

		}
		return noHTMLString;
	}
}
