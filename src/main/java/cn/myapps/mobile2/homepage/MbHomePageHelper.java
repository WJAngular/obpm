package cn.myapps.mobile2.homepage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import cn.myapps.base.action.BaseAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.constans.Web;
import cn.myapps.core.dynaform.pending.ejb.PendingProcess;
import cn.myapps.core.dynaform.pending.ejb.PendingProcessBean;
import cn.myapps.core.dynaform.pending.ejb.PendingVO;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgProcess;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgVO;
import cn.myapps.core.personalmessage.ejb.MessageBody;
import cn.myapps.core.personalmessage.ejb.PersonalMessageProcess;
import cn.myapps.core.personalmessage.ejb.PersonalMessageVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.widget.ejb.PageWidget;
import cn.myapps.core.widget.ejb.PageWidgetProcess;
import cn.myapps.core.workflow.storage.runtime.ejb.ActorRT;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

public class MbHomePageHelper {
	
	public static String refreshWidget(String id,WebUser user){
		try {
			PageWidget widget = getWidgetWithId(id);
			if(widget != null){
				int size = getSummaryCount(widget, user, new ParamsTable());
				JSONObject object = new JSONObject();
				object.put("TYPE", "widget");
				object.put("ID", widget.getId());
				object.put("SIZE", size);
				return object.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 获取widget快捷图标
	 */
	public static ArrayList<PageWidget> getShortCutWidget(Collection<PageWidget> widgetlist) {
		ArrayList<PageWidget> shortCut = new ArrayList<PageWidget>();
		ArrayList<PageWidget> widgets = new ArrayList<PageWidget>();
		if (widgetlist != null) {
			for (PageWidget pageWidget : widgetlist) {
				if (pageWidget.getIconShow()) {
					widgets.add(pageWidget);
					continue;
				}
				if (pageWidget.getType().equals(PageWidget.TYPE_SYSTEM_ANNOUNCEMENT)) {
					shortCut.add(pageWidget);
					continue;
				}
			}
		}
		shortCut.addAll(widgets);
		return shortCut;
	}

	/**
	 * 获取非快捷图标摘要
	 */
	public static Map<String, PageWidget> getWidgetMap(Collection<PageWidget> widgetlist, ArrayList<PageWidget> shortCut) {

		widgetlist.removeAll(shortCut);
		Map<String, PageWidget> result = new LinkedHashMap<String, PageWidget>();

		if (widgetlist != null) {
			for (PageWidget pageWidget : widgetlist) {
				result.put(pageWidget.getId(), pageWidget);
			}
		}
		return result;
	}

	/**
	 * 获取站内短信XML
	 * 
	 * @param data
	 *            DataPackage<PersonalMessageVO>
	 * @param personalMore
	 *            boolean
	 * @return
	 * @throws Exception
	 */
	public static Collection<PersonalMessageVO> doGetPersonalMessage(WebUser user) throws Exception {
		try {
			Collection<PersonalMessageVO> personalMessages = null;
			PersonalMessageProcess personalMensageProcess = (PersonalMessageProcess) ProcessFactory
					.createProcess(PersonalMessageProcess.class);
			String userid = user.getId();
			ParamsTable params = new ParamsTable();
			params.setParameter("_pagelines", String.valueOf(Integer.MAX_VALUE));
			params.setParameter("_orderby", "sendDate");
			params.setParameter("_desc", "desc");
			DataPackage<PersonalMessageVO> data = personalMensageProcess.doInbox(userid, params);
			if (data.rowCount > 0) {
				personalMessages = data.datas;
			}
			return personalMessages;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 旧接口 已弃用 
	 * 获取未读站内短信
	 * 
	 * @param user
	 *            当前用户
	 * @return
	 * @throws Exception
	 * 
	 * 
	 */
	@Deprecated
	public static Collection<PersonalMessageVO> doGetNoReadMessage(WebUser user) throws Exception {
		try {
			Collection<PersonalMessageVO> personalMessages = null;
			PersonalMessageProcess personalMensageProcess = (PersonalMessageProcess) ProcessFactory
					.createProcess(PersonalMessageProcess.class);
			String userid = user.getId();
			ParamsTable params = new ParamsTable();
			params.setParameter("_pagelines", 99);
			params.setParameter("_orderby", "sendDate");
			params.setParameter("_desc", "desc");
			params.setParameter("_isRead", false);
			DataPackage<PersonalMessageVO> data = personalMensageProcess.doInbox(userid, params);
			if (data.rowCount > 0) {
				personalMessages = data.datas;
			}
			return personalMessages;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 旧接口  已弃用
	 * 获取已读站内短信
	 * 
	 * @param user
	 *            获取当前用户
	 * @return
	 * @throws Exception
	 * 
	 */
	@Deprecated
	public static Collection<PersonalMessageVO> doGetReadMessage(WebUser user) throws Exception {
		try {
			Collection<PersonalMessageVO> personalMessages = null;
			PersonalMessageProcess personalMensageProcess = (PersonalMessageProcess) ProcessFactory
					.createProcess(PersonalMessageProcess.class);
			String userid = user.getId();
			ParamsTable params = new ParamsTable();
			params.setParameter("_pagelines", 99);
			params.setParameter("_orderby", "sendDate");
			params.setParameter("_desc", "desc");
			params.setParameter("_isRead", true);
			DataPackage<PersonalMessageVO> data = personalMensageProcess.doInbox(userid, params);
			if (data.rowCount > 0) {
				personalMessages = data.datas;
			}
			return personalMessages;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
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

	public static PageWidget getWidgetWithId(String id) {
		try {
			PageWidgetProcess pageWidgetProcess = (PageWidgetProcess) ProcessFactory
					.createProcess(PageWidgetProcess.class);
			PageWidget pageWidget = (PageWidget) pageWidgetProcess.doView(id);
			return pageWidget;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取摘要待办
	 * 
	 * @param widget
	 *            摘要工具
	 * @param user
	 *            用户
	 * @param params
	 *            参数 _currpage当前页 _pagelines行数
	 * @return
	 * @throws Exception
	 */
	public static Collection<PendingVO> getSummaryPendVO(PageWidget widget, WebUser user, ParamsTable params)
			throws Exception {
		try {
			PersistenceUtils.beginTransaction();
			String applicationId = widget.getApplicationid();
			SummaryCfgProcess summaryCfgProcess = (SummaryCfgProcess) ProcessFactory
					.createProcess(SummaryCfgProcess.class);
			
			String summaryCfgId = widget.getActionContent();
			SummaryCfgVO summaryCfg = (SummaryCfgVO) summaryCfgProcess.doView(summaryCfgId);

			if (summaryCfg != null) {
				params.setParameter("formid", summaryCfg.getFormId());
				PendingProcess process = new PendingProcessBean(applicationId);
				DataPackage<PendingVO> result = process.doQueryByFilter(params, user);
				return result.getDatas();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			PersistenceUtils.closeSessionAndConnection();
		}
		return null;
	}

	/**
	 * 获取摘要总数
	 * 
	 * @param widget
	 *            摘要工具
	 * @param user
	 *            用户
	 * @param params
	 *            参数
	 * @return
	 * @throws Exception
	 */
	public static int getSummaryCount(PageWidget widget, WebUser user, ParamsTable params) throws Exception {
		try {
			PersistenceUtils.beginTransaction();

			String applicationId = widget.getApplicationid();
			SummaryCfgProcess summaryCfgProcess = (SummaryCfgProcess) ProcessFactory
					.createProcess(SummaryCfgProcess.class);

			String summaryCfgId = widget.getActionContent();
			SummaryCfgVO summaryCfg = (SummaryCfgVO) summaryCfgProcess.doView(summaryCfgId);

			if (summaryCfg != null) {
				params.setParameter("formid", summaryCfg.getFormId());
				PendingProcess process = new PendingProcessBean(applicationId);
				DataPackage<PendingVO> result = process.doQueryByFilter(params, user);
				if (result != null) {
					return result.rowCount;
				} else {
					return 0;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			PersistenceUtils.closeSessionAndConnection();
		}
		return 0;
	}

	/**
	 * 获取通知公告总数
	 * 
	 * @param widget
	 *            摘要工具
	 * @param user
	 *            用户
	 * @param params
	 *            参数
	 * @return
	 * @throws Exception
	 */
	public static int getAnnouncementsCout(WebUser user) throws Exception {
		try {
			PersistenceUtils.beginTransaction();

			PersonalMessageProcess personalMessageProcess = (PersonalMessageProcess) ProcessFactory
					.createProcess(PersonalMessageProcess.class);
			Collection<PersonalMessageVO> list = personalMessageProcess.doQueryAnnouncementsByUser(user);
			if (list != null) {
				return list.size();
			} else {
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			PersistenceUtils.closeSessionAndConnection();
		}
		return 0;
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
		String noHTMLString = htmlString.replaceAll("</?[^>]+>", "");
		noHTMLString = noHTMLString.replaceAll("\\&nbsp;", " ");
		return noHTMLString;
	}

	/**
	 * 校验是否已读
	 * 
	 * @param vo
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public static String checkRead(PendingVO vo, WebUser user) throws Exception {
		try {
			Collection<ActorRT> actors = vo.getState().getActors();
			for (Iterator<ActorRT> iter = actors.iterator(); iter.hasNext();) {
				ActorRT actor = (ActorRT) iter.next();
				if (user.getId().equals(actor.getActorid()) && actor.getIsread()) {
					return "true";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return "false";
	}

	/**
	 * 检查字符串是否包含特殊符号：<br>
	 * 
	 * @param value
	 * @return
	 */
	public static String replacSpecial(String value) {
		if (value != null) {
			value = value.replace("&", "&amp;");
			value = value.replace(">", "&gt;");
			value = value.replace("<", "&lt;");
			value = value.replace("'", "&apos;");
			value = value.replace("\"", "&quot;");
		}
		return value;
	}
}
