package cn.myapps.desktop.personal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.jivesoftware.smack.packet.IQ.Type;

import cn.myapps.base.action.BaseAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.constans.Web;
import cn.myapps.core.deploy.application.action.ApplicationHelper;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.personalmessage.action.PersonalMessageHelper;
import cn.myapps.core.personalmessage.ejb.MessageBody;
import cn.myapps.core.personalmessage.ejb.MessageBodyProcess;
import cn.myapps.core.personalmessage.ejb.PersonalMessageProcess;
import cn.myapps.core.personalmessage.ejb.PersonalMessageVO;
import cn.myapps.core.shortmessage.submission.action.SubmitMessageHelper;
import cn.myapps.core.shortmessage.submission.ejb.SubmitMessageProcess;
import cn.myapps.core.shortmessage.submission.ejb.SubmitMessageVO;
import cn.myapps.core.sysconfig.ejb.LoginConfig;
import cn.myapps.core.table.constants.MobileConstant;
import cn.myapps.core.user.action.UserUtil;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workcalendar.calendar.action.CalendarHelper;
import cn.myapps.core.workflow.notification.ejb.sendmode.SMSModeProxy;
import cn.myapps.core.xmpp.XMPPSender;
import cn.myapps.core.xmpp.notification.SiteMessageIQ;
import cn.myapps.util.DateUtil;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.Security;
import cn.myapps.util.StringUtil;
import cn.myapps.util.property.PropertyUtil;
import cn.myapps.util.sequence.Sequence;


/**
 * 
 * @author Tom
 * @SuppressWarnings DPersonalAction内容涉及多个实体，不支持泛型
 */
@SuppressWarnings("unchecked")
public class DPersonalAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(DPersonalAction.class);
	private UserUtil userutil = new UserUtil();

	public DPersonalAction() {
		super(null, null);
	}

	private String operation;
	private String username;
	private String msgType = "in";
	private UserVO uservo = new UserVO();
	private SubmitMessageVO sms = new SubmitMessageVO();
	private PersonalMessageVO msg = new PersonalMessageVO();

	public String doEdit() {
		try {
			String toXml = "";
			if (getOperation().equals("user")) {
				toXml = getUserXml();
			} else if (getOperation().equals("message")) {
				toXml = getMessageXml();
			} else if (getOperation().equals("sms")) {
				toXml = getSmsXml();
			}
			ServletActionContext.getRequest().setAttribute("toXml", toXml);
			// System.out.println("toXml--> " + toXml);
		} catch (Exception e) {
			addFieldError("SystemError", e.toString());
			LOG.warn(e);
			return ERROR;
		}
		return SUCCESS;
	}

	private String getSmsXml() throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			SubmitMessageProcess process = (SubmitMessageProcess) ProcessFactory
					.createProcess(SubmitMessageProcess.class);
			sms = (SubmitMessageVO) process.doView(sms.getId());
			if (sms == null) {
				sms = new SubmitMessageVO();
			}
			sb.append("<" + MobileConstant.TAG_SMSPANEL + " "
					+ MobileConstant.ATT_READONLY + "='" + sms.getSubmission()
					+ "'>");

			sb.append(
					"<" + MobileConstant.TAG_HIDDENFIELD + " "
							+ MobileConstant.ATT_NAME + "='sms.id' "
							+ MobileConstant.ATT_VALUE + "='"
							+ getString(sms.getId()) + "'>").append(
					"</" + MobileConstant.TAG_HIDDENFIELD + ">");
			sb.append(
					"<" + MobileConstant.TAG_TEXTFIELD + " "
							+ MobileConstant.ATT_NAME + "='sms.receiver' "
							+ MobileConstant.ATT_VALUE + "='"
							+ getString(getSmsReceiver()) + "'>").append(
					"</" + MobileConstant.TAG_TEXTFIELD + ">");
			sb.append(
					"<" + MobileConstant.TAG_RADIOFIELD + " "
							+ MobileConstant.ATT_NAME + "='sms.needReply' "
							+ MobileConstant.ATT_VALUE + "='"
							+ sms.isNeedReply() + "'>").append(
					"</" + MobileConstant.TAG_RADIOFIELD + ">");
			sb.append(
					"<" + MobileConstant.TAG_RADIOFIELD + " "
							+ MobileConstant.ATT_NAME + "='sms.mass' "
							+ MobileConstant.ATT_VALUE + "='" + sms.isMass()
							+ "'>").append(
					"</" + MobileConstant.TAG_RADIOFIELD + ">");
			sb.append(
					"<" + MobileConstant.TAG_TEXTFIELD + " "
							+ MobileConstant.ATT_NAME + "='sms.title' "
							+ MobileConstant.ATT_VALUE + "='"
							+ getString(sms.getTitle()) + "'>").append(
					"</" + MobileConstant.TAG_TEXTFIELD + ">");
			sb.append("<" + MobileConstant.TAG_SELECTFIELD + " "
					+ MobileConstant.ATT_NAME + "='_smgType' "
					+ MobileConstant.ATT_VALUE + "='" + get_msgType() + "'>");
			SubmitMessageHelper sh = new SubmitMessageHelper();
			Map<String, String> map = sh.getContentTypes();
			for (Iterator<Entry<String, String>> it = map.entrySet().iterator(); it
					.hasNext();) {
				Map.Entry<String, String> entry = it.next();
				String key = entry.getKey();
				String value = entry.getValue();
				sb.append(
						"<" + MobileConstant.TAG_OPTION + " "
								+ MobileConstant.ATT_VALUE + "='" + key + "'>")
						.append(value).append(
								"</" + MobileConstant.TAG_OPTION + ">");
			}
			sb.append("</" + MobileConstant.TAG_SELECTFIELD + ">");
			sb.append(
					"<" + MobileConstant.TAG_TEXTAREAFIELD + " "
							+ MobileConstant.ATT_NAME + "='sms.content' "
							+ MobileConstant.ATT_VALUE + "='"
							+ getString(sms.getContent()) + "'>").append(
					"</" + MobileConstant.TAG_TEXTAREAFIELD + ">");
			sb.append("</" + MobileConstant.TAG_SMSPANEL + ">");

		} catch (Exception e) {
			LOG.warn(e.toString());
			throw new Exception("查看失败");
		}
		return sb.toString();
	}

	private String getSmsReceiver() {
		if (StringUtil.isBlank(sms.getReceiver())) {
			if (!StringUtil.isBlank(uservo.getId())) {
				try {
					UserProcess process = (UserProcess) ProcessFactory
							.createProcess(UserProcess.class);
					uservo = (UserVO) process.doView(uservo.getId());
					if (uservo != null) {
						return getString(uservo.getTelephone());
					}
				} catch (Exception e) {
					LOG.warn(e);
					return "";
				}
			} else {
				return "";
			}
		}
		return sms.getReceiver();
	}

	private String getString(String str) {
		return str == null ? "" : str;
	}

	private String getSmsListXml() {
		StringBuffer sb = new StringBuffer();
		sb.append(
				"<" + MobileConstant.TAG_HIDDENFIELD + " "
						+ MobileConstant.ATT_NAME + "='operation'>").append(
				"sms");
		sb.append("</" + MobileConstant.TAG_HIDDENFIELD + ">");
		sb.append("<" + MobileConstant.TAG_TABLE + ">");

		// sb.append("<" + MobileConstant.TAG_ACTION + " " +
		// MobileConstant.ATT_NAME + "='{*[New]*}' " + MobileConstant.ATT_TYPE +
		// "='" + MobileConstant.BUTTON_NEW + "'>").append("</" +
		// MobileConstant.TAG_ACTION + ">");
		// sb.append("<" + MobileConstant.TAG_ACTION + " " +
		// MobileConstant.ATT_NAME + "='{*[Delete]*}' " +
		// MobileConstant.ATT_TYPE + "='" + MobileConstant.BUTTON_DELETE +
		// "'>").append("</" + MobileConstant.TAG_ACTION + ">");
		// sb.append("<" + MobileConstant.TAG_ACTION + " " +
		// MobileConstant.ATT_NAME + "='{*[Cancel]*}' " +
		// MobileConstant.ATT_TYPE + "='" + MobileConstant.BUTTON_CANCEL +
		// "'>").append("</" + MobileConstant.TAG_ACTION + ">");

		sb.append("<" + MobileConstant.TAG_TH + ">");
		sb.append("<" + MobileConstant.TAG_TD + ">").append("{*[Content]*}")
				.append("</" + MobileConstant.TAG_TD + ">");
		sb.append("<" + MobileConstant.TAG_TD + ">").append("{*[Receiver]*}")
				.append("</" + MobileConstant.TAG_TD + ">");
		sb.append("<" + MobileConstant.TAG_TD + ">").append(
				"{*[Send]*}{*[Date]*}").append(
				"</" + MobileConstant.TAG_TD + ">");
		sb.append("<" + MobileConstant.TAG_TD + ">").append("{*[Status]*}")
				.append("</" + MobileConstant.TAG_TD + ">");
		sb.append("</" + MobileConstant.TAG_TH + ">");

		int currPage = 1, total = 1;

		try {
			SubmitMessageProcess process = (SubmitMessageProcess) ProcessFactory
					.createProcess(SubmitMessageProcess.class);
			DataPackage<SubmitMessageVO> datas = process.list(getUser(),
					getParams());
			if (datas == null) {
				datas = new DataPackage<SubmitMessageVO>();
			}
			setDatas(datas);
			for (Iterator<SubmitMessageVO> it = datas.getDatas().iterator(); it
					.hasNext();) {
				SubmitMessageVO vo = (SubmitMessageVO) it.next();
				sb.append("<" + MobileConstant.TAG_TR + " "
						+ MobileConstant.ATT_ID + "='" + vo.getId() + "'>");
				sb.append("<" + MobileConstant.TAG_TD + ">").append(
						getString2Table(vo.getContent()) + "").append(
						"</" + MobileConstant.TAG_TD + ">");
				sb.append("<" + MobileConstant.TAG_TD + ">").append(
						getString2Table(vo.getReceiver())).append(
						"</" + MobileConstant.TAG_TD + ">");
				sb.append("<" + MobileConstant.TAG_TD + ">").append(
						getString2Table(DateUtil.getDateTimeStr(vo
								.getSendDate()))).append(
						"</" + MobileConstant.TAG_TD + ">");
				String str = "";
				if (vo.getSubmission()) {
					str = "{*[cn.myapps.core.desktop.personal.sent]*}";
				} else if (vo.isFailure()) {
					str = "{*[cn.myapps.core.desktop.personal.failure]*}";
				} else if (vo.isDraft()) {
					str = "{*[Draft]*}";
				} else {
					str = "{*[Invalid]*}";
				}
				sb.append("<" + MobileConstant.TAG_TD + ">").append(
						getString2Table(str)).append(
						"</" + MobileConstant.TAG_TD + ">");
				sb.append("</" + MobileConstant.TAG_TR + ">");
			}
			currPage = getDatas().pageNo;
			total = getDatas().getPageCount();
		} catch (Exception e) {
			LOG.warn(e);
		}

		sb.append(
				"<" + MobileConstant.TAG_PAGE + " " + MobileConstant.ATT_TOTAL
						+ "='" + total + "' " + MobileConstant.ATT_CURRPAGE
						+ "='" + currPage + "'>").append(
				"</" + MobileConstant.TAG_PAGE + ">");

		sb.append("</" + MobileConstant.TAG_TABLE + ">");
		return sb.toString();
	}

	private String getMessageListXml() throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			ParamsTable params = getParams();
			params.removeParameter("msg_type");
			params.removeParameter("_selects");
			params.setParameter("_orderby", "sendDate");
			params.setParameter("_desc", "desc");
			WebUser webUser = getUser();
			PersonalMessageProcess process = (PersonalMessageProcess) ProcessFactory
					.createProcess(PersonalMessageProcess.class);
			if (webUser != null) {
				String temp = "in";
				if (getMsgType().equals("out")) {
					temp = "out";
					setDatas(process.doOutbox(webUser.getId(), params));
				} else if (getMsgType().equals("trash")) {
					temp = "trash";
					setDatas(process.doTrash(webUser.getId(), params));
				} else { // in
					setDatas(process.doInbox(webUser.getId(), params));
				}
				sb.append(
						"<" + MobileConstant.TAG_HIDDENFIELD + " "
								+ MobileConstant.ATT_NAME + "='msg_type'>")
						.append(temp);
				sb.append("</" + MobileConstant.TAG_HIDDENFIELD + ">");
			}
			sb.append(
					"<" + MobileConstant.TAG_HIDDENFIELD + " "
							+ MobileConstant.ATT_NAME + "='operation'>")
					.append("message");
			sb.append("</" + MobileConstant.TAG_HIDDENFIELD + ">");
			sb.append("<" + MobileConstant.TAG_TABLE + ">");

			sb.append("<" + MobileConstant.TAG_TH + ">");
			sb.append("<" + MobileConstant.TAG_TD + ">").append("{*[Title]*}")
					.append("</" + MobileConstant.TAG_TD + ">");
			sb.append("<" + MobileConstant.TAG_TD + ">").append(
					"{*[Receiver]*}")
					.append("</" + MobileConstant.TAG_TD + ">");
			sb.append("<" + MobileConstant.TAG_TD + ">").append("{*[Sender]*}")
					.append("</" + MobileConstant.TAG_TD + ">");
			sb.append("<" + MobileConstant.TAG_TD + ">").append(
					"{*[SendDate]*}")
					.append("</" + MobileConstant.TAG_TD + ">");
			sb.append("</" + MobileConstant.TAG_TH + ">");

			int currPage = 1, total = 1;
			PersonalMessageHelper ph = new PersonalMessageHelper();
			for (Iterator<PersonalMessageVO> it = getDatas().getDatas()
					.iterator(); it.hasNext();) {
				PersonalMessageVO vo = (PersonalMessageVO) it.next();
				if (vo.getBody() == null) {
					continue;
				}

				sb.append("<" + MobileConstant.TAG_TR + " "
						+ MobileConstant.ATT_ID + "='" + vo.getId() + "' "
						+ MobileConstant.ATT_ISNEW + "='" + vo.isRead() + "'>");
				if (!getMsgType().equals("out")) {
					if (vo.isRead()) {
						sb.append("<" + MobileConstant.TAG_TD + ">")
								.append(
										getString2Table("已读 "
												+ vo.getBody().getTitle())
												+ "").append(
										"</" + MobileConstant.TAG_TD + ">");
					} else {
						sb.append("<" + MobileConstant.TAG_TD + ">")
								.append(
										getString2Table("未读 "
												+ vo.getBody().getTitle())
												+ "").append(
										"</" + MobileConstant.TAG_TD + ">");
					}
				} else {
					sb.append("<" + MobileConstant.TAG_TD + ">").append(
							getString2Table(vo.getBody().getTitle()) + "")
							.append("</" + MobileConstant.TAG_TD + ">");
				}
				sb.append("<" + MobileConstant.TAG_TD + ">").append(
						getString2Table(ph.findUserNamesByMsgIds(vo
								.getReceiverId()))).append(
						"</" + MobileConstant.TAG_TD + ">");
				sb.append("<" + MobileConstant.TAG_TD + ">").append(
						getString2Table(ph.findUserNameById(vo.getSenderId())))
						.append("</" + MobileConstant.TAG_TD + ">");
				sb.append("<" + MobileConstant.TAG_TD + ">").append(
						getString2Table(DateUtil.getDateTimeStr(vo
								.getSendDate()))).append(
						"</" + MobileConstant.TAG_TD + ">");

				sb.append("<" + MobileConstant.TAG_TD + ">").append(
						getString2Table("")).append(
						"</" + MobileConstant.TAG_TD + ">");
				sb.append("</" + MobileConstant.TAG_TR + ">");
			}
			currPage = getDatas().pageNo;
			total = getDatas().getPageCount();

			sb.append(
					"<" + MobileConstant.TAG_PAGE + " "
							+ MobileConstant.ATT_TOTAL + "='" + total + "' "
							+ MobileConstant.ATT_CURRPAGE + "='" + currPage
							+ "'>")
					.append("</" + MobileConstant.TAG_PAGE + ">");

			sb.append("</" + MobileConstant.TAG_TABLE + ">");
		} catch (Exception e) {
			LOG.warn(e.toString());
			throw new Exception("获取站内短信失败");
		}
		return sb.toString();
	}

	private String getMessageXml() throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			PersonalMessageProcess process = (PersonalMessageProcess) ProcessFactory
					.createProcess(PersonalMessageProcess.class);
			msg = (PersonalMessageVO) process.doView(msg.getId());
			if (msg == null) {
				msg = new PersonalMessageVO();
				msg.setBody(new MessageBody());
			} else {
				if (!msg.isRead()) {
					msg.setRead(true);
					process.doUpdate(msg);
				}
			}

			sb.append("<" + MobileConstant.TAG_MESSAGEPANEL + " "
					+ MobileConstant.ATT_READONLY + "='"
					+ !StringUtil.isBlank(msg.getId()) + "'>");

			sb.append(
					"<" + MobileConstant.TAG_HIDDENFIELD + " "
							+ MobileConstant.ATT_NAME + "='msg.id' "
							+ MobileConstant.ATT_VALUE + "='"
							+ getString(msg.getId()) + "'>").append(
					"</" + MobileConstant.TAG_HIDDENFIELD + ">");
			sb.append(
					"<" + MobileConstant.TAG_TEXTFIELD + " "
							+ MobileConstant.ATT_NAME + "='receiver' "
							+ MobileConstant.ATT_VALUE + "='"
							+ getString(getReceiver()) + "'>").append(
					"</" + MobileConstant.TAG_TEXTFIELD + ">");
			sb.append(
					"<" + MobileConstant.TAG_TEXTFIELD + " "
							+ MobileConstant.ATT_NAME + "='msg.body.title' "
							+ MobileConstant.ATT_VALUE + "='"
							+ getString(msg.getBody().getTitle()) + "'>")
					.append("</" + MobileConstant.TAG_TEXTFIELD + ">");

			sb.append(
					"<" + MobileConstant.TAG_TEXTAREAFIELD + " "
							+ MobileConstant.ATT_NAME + "='msg.body.content' "
							+ MobileConstant.ATT_VALUE + "='"
							+ getString(msg.getBody().getContent()) + "'>")
					.append("</" + MobileConstant.TAG_TEXTAREAFIELD + ">");
			sb.append("</" + MobileConstant.TAG_MESSAGEPANEL + ">");

		} catch (Exception e) {
			LOG.warn(e.toString());
			throw new Exception("查看失败");
		}
		return sb.toString();
	}

	private String getUserXml() throws Exception {
		StringBuffer sb = new StringBuffer();
		// WebUser user = getUser();
		UserProcess up = (UserProcess) ProcessFactory
				.createProcess(UserProcess.class);
		if (StringUtil.isBlank(uservo.getId())) {
			uservo = (UserVO) up.doView(getUser().getId());
		} else {
			uservo = (UserVO) up.doView(uservo.getId());
		}
		sb.append("<" + MobileConstant.TAG_USERPANEL + " "
				+ MobileConstant.ATT_OPTION + "='user' "
				+ MobileConstant.ATT_ID + "='" + uservo.getId() + "'>");

		sb.append(
				"<" + MobileConstant.TAG_TEXTFIELD + " "
						+ MobileConstant.ATT_NAME + "='uservo.name' "
						+ MobileConstant.ATT_VALUE + "='" + uservo.getName()
						+ "'>").append(
				"</" + MobileConstant.TAG_TEXTFIELD + ">");
		sb.append(
				"<" + MobileConstant.TAG_TEXTFIELD + " "
						+ MobileConstant.ATT_NAME + "='uservo.loginno' "
						+ MobileConstant.ATT_VALUE + "='" + uservo.getLoginno()
						+ "'>").append(
				"</" + MobileConstant.TAG_TEXTFIELD + ">");
		sb.append(
				"<" + MobileConstant.TAG_TEXTFIELD + " "
						+ MobileConstant.ATT_NAME + "='_password' "
						+ MobileConstant.ATT_VALUE + "='" + get_password()
						+ "'>").append(
				"</" + MobileConstant.TAG_TEXTFIELD + ">");
		sb.append(
				"<" + MobileConstant.TAG_TEXTFIELD + " "
						+ MobileConstant.ATT_NAME + "='uservo.email' "
						+ MobileConstant.ATT_VALUE + "='" + uservo.getEmail()
						+ "'>").append(
				"</" + MobileConstant.TAG_TEXTFIELD + ">");
		sb.append(
				"<" + MobileConstant.TAG_TEXTFIELD + " "
						+ MobileConstant.ATT_NAME + "='uservo.telephone' "
						+ MobileConstant.ATT_VALUE + "='"
						+ uservo.getTelephone() + "'>").append(
				"</" + MobileConstant.TAG_TEXTFIELD + ">");
		sb.append("<" + MobileConstant.TAG_SELECTFIELD + " "
				+ MobileConstant.ATT_NAME + "='uservo.calendarType' "
				+ MobileConstant.ATT_VALUE + "='" + uservo.getCalendarType()
				+ "'>");

		CalendarHelper ch = new CalendarHelper();
		ch.setDomain(uservo.getDomainid());
		Map<String, String> map = ch.getWorkCalendars();
		for (Iterator<Entry<String, String>> it = map.entrySet().iterator(); it
				.hasNext();) {
			Map.Entry<String, String> entry = it.next();
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(
					"<" + MobileConstant.TAG_OPTION + " "
							+ MobileConstant.ATT_VALUE + "='" + key + "'>")
					.append(value).append(
							"</" + MobileConstant.TAG_OPTION + ">");
		}
		sb.append("</" + MobileConstant.TAG_SELECTFIELD + ">");

		sb.append("<" + MobileConstant.TAG_SELECTFIELD + " "
				+ MobileConstant.ATT_NAME + "='_proxyUser' "
				+ MobileConstant.ATT_VALUE + "='" + get_proxyUser() + "'>");
		Collection<UserVO> list = getAllUsers();
		for (Iterator<UserVO> it = list.iterator(); it.hasNext();) {
			UserVO vo = (UserVO) it.next();
			sb.append(
					"<" + MobileConstant.TAG_OPTION + " "
							+ MobileConstant.ATT_VALUE + "='" + vo.getId()
							+ "'>").append(vo.getLoginno()).append(
					"</" + MobileConstant.TAG_OPTION + ">");
		}
		sb.append("</" + MobileConstant.TAG_SELECTFIELD + ">");

		sb.append("</" + MobileConstant.TAG_USERPANEL + ">");
		return sb.toString();
	}

	private String getUserListXml() {
		StringBuffer sb = new StringBuffer();
		sb.append(
				"<" + MobileConstant.TAG_HIDDENFIELD + " "
						+ MobileConstant.ATT_NAME + "='user'>").append("sms");
		sb.append("</" + MobileConstant.TAG_HIDDENFIELD + ">");
		sb.append("<" + MobileConstant.TAG_TABLE + ">");

		sb.append("<" + MobileConstant.TAG_TH + ">");
		sb.append("<" + MobileConstant.TAG_TD + ">").append("{*[Name]*}")
				.append("</" + MobileConstant.TAG_TD + ">");
		// sb.append("<" + MobileConstant.TAG_TD +
		// ">").append("{*[Account]*}").append("</" + MobileConstant.TAG_TD +
		// ">");
		sb.append("<" + MobileConstant.TAG_TD + ">").append("{*[Email]*}")
				.append("</" + MobileConstant.TAG_TD + ">");
		sb.append("<" + MobileConstant.TAG_TD + ">").append("{*[Mobile]*}")
				.append("</" + MobileConstant.TAG_TD + ">");
		sb.append("</" + MobileConstant.TAG_TH + ">");

		int currPage = 1, total = 1;

		try {
			UserProcess process = (UserProcess) ProcessFactory
					.createProcess(UserProcess.class);
			ParamsTable params = new ParamsTable();
			WebUser user = getUser();
			params = getParams();
			params.setParameter("sm_name", username);
			params.setParameter("t_domainid", user.getDomainid());
			setDatas(process.doQuery(params));
			if (getDatas().datas != null) {
				for (Iterator<UserVO> it = getDatas().datas.iterator(); it
						.hasNext();) {
					UserVO vo = (UserVO) it.next();
					sb.append("<" + MobileConstant.TAG_TR + " "
							+ MobileConstant.ATT_ID + "='" + vo.getId() + "'>");
					sb.append("<" + MobileConstant.TAG_TD + ">").append(
							getString2Table(vo.getName())).append(
							"</" + MobileConstant.TAG_TD + ">");
					// sb.append("<" + MobileConstant.TAG_TD +
					// ">").append(getString2Table(vo.getLoginno())).append("</"
					// + MobileConstant.TAG_TD + ">");
					sb.append("<" + MobileConstant.TAG_TD + ">").append(
							getString2Table(vo.getEmail())).append(
							"</" + MobileConstant.TAG_TD + ">");
					sb.append("<" + MobileConstant.TAG_TD + ">").append(
							getString2Table(vo.getTelephone())).append(
							"</" + MobileConstant.TAG_TD + ">");
					sb.append("</" + MobileConstant.TAG_TR + ">");
				}
				currPage = getDatas().pageNo;
				total = getDatas().getPageCount();
			}
		} catch (Exception e) {
			LOG.warn(e);
		}

		sb.append(
				"<" + MobileConstant.TAG_PAGE + " " + MobileConstant.ATT_TOTAL
						+ "='" + total + "' " + MobileConstant.ATT_CURRPAGE
						+ "='" + currPage + "'>").append(
				"</" + MobileConstant.TAG_PAGE + ">");

		sb.append("</" + MobileConstant.TAG_TABLE + ">");
		return sb.toString();
	}

	public UserVO getUservo() {
		return uservo;
	}

	public void setUservo(UserVO uservo) {
		this.uservo = uservo;
	}

	public String doSave() {
		String toXml = "";
		try {
			if (getOperation().equals("user")) {
				savePersonal();
				toXml = getUserXml();
			} else if (getOperation().equals("message")) {
				saveMsg();
				// toXml = getMessageXml();
			} else if (getOperation().equals("sms")) {
				saveSms();
				toXml = "<" + MobileConstant.TAG_VIEW + " "
						+ MobileConstant.ATT_OPTION + "='" + "sms'>" + getSmsListXml();; 
			}
			ServletActionContext.getRequest().setAttribute("toXml", toXml);
			// System.out.println("toXml--> " + toXml);
		} catch (Exception e) {
			if (getOperation().equals("user")) {
				if(this.getFieldErrors() != null && this.getFieldErrors().size()>0){
					  List<String> SystemError = this.getFieldErrors().get("1");
					  String result = "";
					  if(SystemError != null && SystemError.size()>0){
						  for(String str :SystemError){
							  result += str+" ";
						  }
					  }
					  if(result.length() >0){
						  toXml =result;
					  }
				}
			} else if (getOperation().equals("message")) {

			} else if (getOperation().equals("sms")) {
				toXml = sms.getId();
			}
			addFieldError("SystemError",  toXml);
			LOG.warn(e);
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * 保存个人信息
	 * 
	 * @return 成功处理返回"SUCCESS",否则提示失败
	 * @throws Exception
	 */
	private void savePersonal() throws Exception {
		// set_password(_password);
//		PropertyUtil.reload("sso");
//		String loginFailTimesString = PropertyUtil.get(LoginConfig.LOGIN_FAIL_TIMES);
//		int loginPasswordErrortimes = Integer.parseInt(loginFailTimesString);
//		validateUser();
//		UserProcess up = (UserProcess) ProcessFactory
//				.createProcess(UserProcess.class);
//		set_proxyUser(_proxyUser);
//		WebUser webUser = getUser();
//		up.doPersonalUpdate(uservo);
//		UserVO uv = up.login(uservo.getLoginno(), uservo.getLoginpwd(), webUser
//				.getDomain().getName(),loginPasswordErrortimes);
//
//		webUser.setName(uservo.getName());
//		webUser.setLoginno(uservo.getLoginno());
//		webUser.setLoginpwd(uv.getLoginpwd());
//		webUser.setEmail(uservo.getEmail());
//		webUser.setTelephone(uservo.getTelephone());
//		webUser.setCalendarType(uservo.getCalendarType());
		
		this.uservo = setPasswordArray(uservo);
		
		PropertyUtil.reload("passwordLegal");
		String passwordLength = PropertyUtil.get(LoginConfig.LOGIN_PASSWORD_LENGTH);
		if (passwordLength != null && !passwordLength.trim().equals("")) {
			int length = Integer.parseInt(passwordLength);
			if(uservo.getLoginpwd().length()<length){                         //判断密码长度
				this.addFieldError("1", "{*[PasswordLengthCanNotLow]*}"+length);
			}
		}
		String legal = PropertyUtil.get(LoginConfig.LOGIN_PASSWORD_LEGAL);
		if (legal.equals("1") || legal == "1") {
			Pattern p = Pattern
					.compile("[a-zA-Z0-9]*[a-zA-Z]+[0-9]+[a-zA-Z0-9]*");
			Pattern p1 = Pattern
					.compile("[a-zA-Z0-9]*[0-9]+[a-zA-Z]+[a-zA-Z0-9]*");
			Matcher matcher = p.matcher(uservo.getLoginpwd());
			Matcher matcher1 = p1.matcher(uservo.getLoginpwd());
			boolean result = matcher.matches();
			boolean result1 = matcher1.matches();
			if ((!result) && (!result1)) {
				this.addFieldError("1", "{*[PasswordLegal]*}");
			}
		}
		
		UserProcess up = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
		up.doPersonalUpdate(this.uservo);
		
		WebUser webUser = new WebUser(uservo);
		
		getSession().setAttribute(getWebUserSessionKey(), webUser);

		// this.addActionMessage("{*[Save_Success]*}");
	}
	
	public UserVO setPasswordArray(UserVO user) throws Exception {

		PropertyUtil.reload("passwordLegal");
		String passwordArrayLengthString = PropertyUtil
				.get(LoginConfig.LOGIN_PASSWORD_UPADTE_TIMES);
		int passwordArrayLength = Integer.parseInt(passwordArrayLengthString);

		UserProcess userProcess = (UserProcess) ProcessFactory
				.createProcess(UserProcess.class);
		UserVO po = (UserVO) userProcess.doView(user.getId());
		String oldPasswordArray = "";
		oldPasswordArray = po.getPasswordArray();

		String pwda[] = null;
		int oldpasswordArrayLength = 0;
		if (oldPasswordArray != null) {
			pwda = oldPasswordArray.split(",");
			oldpasswordArrayLength = oldPasswordArray.split(",").length;
		}

		String passwordArray = null;
		passwordArray = user.getPasswordArray();
		String loginpwd = user.getLoginpwd();

		if (!loginpwd.trim().equals(decrypt(po.getLoginpwd()))
				&& !loginpwd.trim().equals(Web.DEFAULT_SHOWPASSWORD)) {
			if (oldPasswordArray != null) {
				for (int i = 0; i < oldpasswordArrayLength; i++) {
					if (loginpwd.equals(decrypt(pwda[i]))) {
						this.addFieldError("1", "{*[ModifyPasswordNotSame]*}"
								+ passwordArrayLengthString);
						throw new Exception("e1");
					}
				}
				passwordArray = oldPasswordArray + "," + encrypt(loginpwd);
			} else {
				passwordArray = po.getLoginpwd() + ","
						+ encrypt(user.getLoginpwd());
			}
			po.setLastModifyPasswordTime(new Date());

		} else {
			passwordArray = po.getPasswordArray();
			po.setLastModifyPasswordTime(po.getLastModifyPasswordTime());
		}
		if (oldpasswordArrayLength + 1 > passwordArrayLength) {
			int i1 = passwordArray.split(",").length;
			String passwordArraytmp = "";
			for (int i = i1 - passwordArrayLength - 1; i < i1; i++) {
				passwordArraytmp += "," + passwordArray.split(",")[i];
			}
			passwordArray = passwordArraytmp.substring(1);
		}
		po.setPasswordArray(passwordArray);
		po.setName(user.getName());
		po.setLoginpwd(user.getLoginpwd());
		po.setEmail(user.getEmail());
		po.setTelephone(user.getTelephone());
		po.setCalendarType(user.getCalendarType());
		po.setProxyUser(user.getProxyUser());
		
		return po;
	}
	
	/**
	 * 新的密码加密机制
	 * 
	 * @param s
	 * @return
	 * @throws Exception
	 */
	private String encrypt(final String s) throws Exception {
		return Security.encryptPassword(s);
	}
	
	/**
	 * 新的密码解密机制
	 * 
	 * @param s
	 * @return
	 * @throws Exception
	 */
	private String decrypt(final String s) {
		return Security.decryptPassword(s);
	}
	

	private void validateUser() throws Exception {
		if (StringUtil.isBlank(uservo.getName())) {
			throw new Exception("{*[page.name.notexist]*}!");
		}
		if (StringUtil.isBlank(uservo.getLoginno())) {
			throw new Exception("{*[page.user.account.illegal]*}!");
		}
		if (StringUtil.isBlank(uservo.getLoginpwd())) {
			throw new Exception("{*[page.user.loginpwd.illegal]*}!");
		}
	}
	
	//发送站内短信
	private void saveMsg() throws Exception {
		validateMsg();
		WebUser user = getUser();
		ParamsTable param = getParams();

	    String receiverids = param.getParameterAsString("ids");
		String ids = Sequence.getSequence();
		String title = param.getParameterAsString("msg.body.title");
		String content = param.getParameterAsString("msg.body.content");
		String receiver = param.getParameterAsString("receiver");
		String domainid = param.getParameterAsString("domainid");
		MessageBody body = new MessageBody();
		body.setContent(content);
		body.setTitle(title);
		body.setId(ids);
		doCreateMessageBody(body);
		String receiverid="";
		String[] a = receiver.split(";");
		if(receiverids == ""){
			for(int i = 0;i<a.length ; i++){
				String users = a[i];
				String id = userutil.queryUserIdsByName(users, user.getDomainid());
				if(id.equals("") || id == null){
					throw new Exception("没有找到"+"'"+users+"'"+"发件人");
				}
				if(receiverid == ""){
					receiverid = id;
				}else{
					receiverid += ","+ id;
				}
				
			}
		}else{
			receiverid = receiverids.replaceAll(";", ",");
		}
		String[] rId = receiverid.split(",");
		
		PersonalMessageVO pm = new PersonalMessageVO();
		pm.setSenderId(user.getId());
		PersonalMessageProcess process = (PersonalMessageProcess) ProcessFactory
				.createProcess(PersonalMessageProcess.class);
		pm.setReceiverId(receiverid);
		pm.setDomainid(domainid);
		pm.setBody(body);
		pm.setType("0");
		pm.setOwnerId(pm.getSenderId());
		pm.setRead(true);
		pm.setOutbox(true);
		Collection<PersonalMessageVO> receiverList = new ArrayList<PersonalMessageVO>();
		receiverList = process.doSaveMorePersonalMessageVO(rId,pm);
				// 如果参数中附有sendMsg.by.xmpp参数,则使用XMPP发送站内短信
		String sendMsgByXMPP = param.getParameterAsString("sendMsg.by.xmpp");
		if (receiverList != null && "true".equalsIgnoreCase(sendMsgByXMPP)) {
			sendMoreMessageByXMPP(receiverList,rId);
		}
	}
	

	/**
	 * 使用xmpp发送站内短信给接收者
	 * 
	 * @throws Exception
	 * 
	
	private void sendMessageByXMPP(PersonalMessageVO pmVO) throws Exception {
		UserProcess process = (UserProcess) ProcessFactory
				.createProcess(UserProcess.class);

		WebUser user = getUser();
		ParamsTable param = getParams();
		String title = param.getParameterAsString("msg.body.title");
		String content = param.getParameterAsString("msg.body.content");
		String to = param.getParameterAsString("receiver");
		String receiverid = param.getParameterAsString("receiver_id");

		UserVO userVO = (UserVO) process.doView(receiverid);
		if (userVO != null) {
			ParamsTable param_user = new ParamsTable();
			param_user.setParameter("username", to);

			SiteMessageIQ siteMessageIQ = new SiteMessageIQ();
			siteMessageIQ.setId(pmVO.getId());
			siteMessageIQ.setTitle(title);
			siteMessageIQ.setContent(content);
			siteMessageIQ.setDomain(userVO.getDomain());
			siteMessageIQ.setSender(user); // 设置发送者
			siteMessageIQ.getReceivers().add(userVO); // 添加接收者
			siteMessageIQ.setType(Type.SET);

			XMPPSender sender = XMPPSender.getInstance();
			// if (!siteMessageIQSender.isConnected()) {
			// siteMessageIQSender.connect();
			// }
			sender.processNotification(siteMessageIQ);
		}
	}
	 * 
	 */
	
	/**
	 * 使用xmpp发送站内短信给接收者
	 * 
	 * @throws Exception
	 */
	private void sendMoreMessageByXMPP(Collection<PersonalMessageVO> pmVO ,String[] ids) throws Exception {
		UserProcess process = (UserProcess) ProcessFactory
				.createProcess(UserProcess.class);

		WebUser user = getUser();
		ParamsTable param = getParams();
		String title = param.getParameterAsString("msg.body.title");
		String content = param.getParameterAsString("msg.body.content");
		String to = param.getParameterAsString("receiver");
		PersonalMessageVO pp = pmVO.iterator().next();
		
		if (ids != null) {
			ParamsTable param_user = new ParamsTable();
			param_user.setParameter("username", to);

			SiteMessageIQ siteMessageIQ = new SiteMessageIQ();
			siteMessageIQ.setId(pp.getId());
			siteMessageIQ.setTitle(title);
			siteMessageIQ.setContent(content);
			siteMessageIQ.setDomain(user.getDomain());
			siteMessageIQ.setSender(user); // 设置发送者
			for(int i = 0;i<ids.length;i++){
				UserVO userVO = (UserVO) process.doView(ids[i]);
				siteMessageIQ.getReceivers().add(userVO); // 添加接收者
			}
			siteMessageIQ.setType(Type.SET);

			XMPPSender sender = XMPPSender.getInstance();
			// if (!siteMessageIQSender.isConnected()) {
			// siteMessageIQSender.connect();
			// }
			sender.processNotification(siteMessageIQ);
		}
	}
	private void validateMsg() throws Exception {
		if (msg == null) {
			throw new Exception("保存失败!");
		}
	}

	private void saveSms() throws Exception {
		validateSms();
		WebUser uservo = getUser();
		
		Collection<ApplicationVO> apps = new ApplicationHelper().getListByWebUser(uservo);
		if(apps != null){
			for (Iterator<ApplicationVO> it = apps.iterator(); it.hasNext();) {
				ApplicationVO app = it.next();
				sms.setApplicationid(app.getApplicationid());
				break;
			}
		}
		if (uservo != null && uservo.getTelephone() != null
				&& sms.getSender() == null) {
			sms.setSender(uservo.getTelephone());
		}
		if (!sms.getSubmission()) {
			SMSModeProxy sender = new SMSModeProxy(uservo);
			String receiver = sms.getReceiver();
			String sendtel = uservo.getTelephone();
			if (sendtel != null && sendtel.trim().length() > 0) {
				if (receiver != null) {
					set_msgType(getParams().getParameterAsString("_smgType"));
					sms.setSender(sendtel);
//					sms.setApplicationid(getApplication());
					sms.setDomainid(uservo.getDomainid());
					if(sms.getId() ==null) sms.setId("");
					sender.send(sms);
					sms = new SubmitMessageVO();
					if (sms.isFailure()) {
						throw new Exception("{*[cn.myapps.core.desktop.personal.failure]*}");
					}
				} else {
					throw new Exception("{*[core.shortmessage.norecvlist]*}");
				}
			} else {
				throw new Exception("{*[core.shortmessage.nosender]*}");
			}
		}
	}

	private void validateSms() throws Exception {
		if (StringUtil.isBlank(sms.getReceiver())) {
			throw new Exception("{*[core.shortmessage.norecvlist]*}!");
		}
		if (StringUtil.isBlank(sms.getContent())) {
			throw new Exception("{*[page.content.notexist]*}!");
		}
	}

	public String doList() {
		try {
			String toXml = "<" + MobileConstant.TAG_VIEW + " "
					+ MobileConstant.ATT_OPTION + "='";
			if (getOperation().equals("user")) {
				toXml += "user'>" + getUserListXml();
			} else if (getOperation().equals("message")) {
				toXml += "message'>" + getMessageListXml();
			} else if (getOperation().equals("sms")) {
				toXml += "sms'>" + getSmsListXml();
			} else {
				toXml += "'>";
			}
			toXml += "</" + MobileConstant.TAG_VIEW + ">";
			ServletActionContext.getRequest().setAttribute("toXml", toXml);
			// System.out.println("toXml--> " + toXml);
		} catch (Exception e) {
			addFieldError("SystemError", e.toString());
			LOG.warn(e);
			return ERROR;
		}
		return SUCCESS;
	}

	public String doDelete() {
		try {
			if (getOperation().equals("user")) {

			} else if (getOperation().equals("message")) {
				if (_selects == null || _selects.length == 0) {
					// throw new Exception();
					return doList();
				}
				PersonalMessageProcess process = (PersonalMessageProcess) ProcessFactory
						.createProcess(PersonalMessageProcess.class);
				if (getMsgType().equals("trash")) {
					process.doRemove(_selects);
				} else {
					process.doSendToTrash(_selects);
				}
			} else if (getOperation().equals("sms")) {
				if (_selects == null || _selects.length == 0) {
					// throw new Exception();
					return doList();
				}
				SubmitMessageProcess process = (SubmitMessageProcess) ProcessFactory
						.createProcess(SubmitMessageProcess.class);
				process.doRemove(_selects);
			}
		} catch (Exception e) {
			LOG.warn(e);
			addFieldError("SystemError", "{*[Delete]*}{*[page.obj.dofailure]*}");
			return ERROR;
		}
		return doList();
	}

	public String getOperation() {
		if (operation == null)
			operation = "";
		return operation.split(",")[0].trim();
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public HttpSession getSession() {
		return ServletActionContext.getRequest().getSession();
	}

	public String getWebUserSessionKey() {
		return Web.SESSION_ATTRIBUTE_FRONT_USER;
	}

	private String _password = "";

	private String _proxyUser = "";

	public String get_password() {
		return Web.DEFAULT_SHOWPASSWORD;
	}

	public void set_password(String password) {
		_password = password;
		// if (!Web.DEFAULT_SHOWPASSWORD.equals(_password)) {
		uservo.setLoginpwd(_password);
		// }
	}

	public String get_proxyUser() {
		if (uservo.getProxyUser() != null) {
			_proxyUser = uservo.getProxyUser().getId();
		}
		return _proxyUser;
	}

	public void set_proxyUser(String proxyUser) throws Exception {
		_proxyUser = proxyUser;
		if (!StringUtil.isBlank(proxyUser)) {
			UserProcess up = (UserProcess) ProcessFactory
					.createProcess(UserProcess.class);
			UserVO proxyUserVO = (UserVO) up.doView(proxyUser);
			if (proxyUserVO != null) {
				uservo.setProxyUser(proxyUserVO);
			}
		}
	}

	/**
	 * 获取所有用户
	 * 
	 * @return 用户列表
	 * @throws Exception
	 */
	public Collection<UserVO> getAllUsers() throws Exception {
		UserProcess up = (UserProcess) ProcessFactory
				.createProcess(UserProcess.class);
		ParamsTable params = this.getParams();
		String domainid = params.getParameterAsString("domain");
		if (StringUtil.isBlank(domainid)) {
			domainid = uservo.getDomainid();
		}
		Collection<UserVO> userList = new ArrayList<UserVO>();
		UserVO none = new UserVO();
		none.setId("");
		none.setLoginno("{*[none]*}");
		none.setName("{*[none]*}");
		userList.add(none);
		userList.addAll(up.queryByDomain(domainid));
		return userList;
	}

	public SubmitMessageVO getSms() {
		return sms;
	}

	public void setSms(SubmitMessageVO sms) {
		this.sms = sms;
	}

	// private String _smgType = "0";

	public String get_msgType() {
		return sms.getContentType() + "";
	}

	public void set_msgType(String type) {
		if (type != null) {
			sms.setContentType(Integer.parseInt(type.trim()));
		}
	}

	private String getString2Table(String str) {
		if (StringUtil.isBlank(str)) {
			return " ";
		}
		return str;
	}

	@Override
	public void set_selects(String[] selects) {
		if (selects != null && selects.length == 1) {
			_selects = selects[0].split(";");
		} else {
			super.set_selects(selects);
		}
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		if (StringUtil.isBlank(msgType))
			msgType = "in";
		this.msgType = msgType;
	}

	// private String receiver;

	public String getReceiver() {
		if (msg == null)
			return "";
		PersonalMessageHelper helper = new PersonalMessageHelper();
		try {
			return helper.findUserNamesByMsgIds(msg.getReceiverId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	// public void setReceiver(String receiver) {
	// this.receiver = receiver;
	// }

	public PersonalMessageVO getMsg() {
		return msg;
	}

	public void setMsg(PersonalMessageVO msg) {
		this.msg = msg;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	private void doCreateMessageBody(MessageBody body) throws Exception {
		if (body != null) {
			MessageBodyProcess bodyProcess = (MessageBodyProcess) ProcessFactory
					.createProcess(MessageBodyProcess.class);
			bodyProcess.doCreate(body);
		}
	}
}
