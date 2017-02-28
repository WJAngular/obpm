package cn.myapps.qm.notification;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cn.myapps.constans.Environment;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.qm.answer.ejb.AnswerVO;
import cn.myapps.qm.questionnaire.ejb.QuestionnaireProcess;
import cn.myapps.qm.questionnaire.ejb.QuestionnaireProcessBean;
import cn.myapps.qm.questionnaire.ejb.QuestionnaireVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.notification.NotificationUtil;

/**
 * qm微信端消息提醒实现类
 * @author ahan
 *
 */
public class QuestionnaireNotificationServiceImpl implements QuestionnaireNotificationService{

	/**
	 * 微OA365反向代理模式外网访问地址
	 */
	private static final String weioa365_addr = "https://yun.weioa365.com/{site_id}";
	private static final String qm_app_id = "12";
	
	QuestionnaireProcess questionnaireProcess = new QuestionnaireProcessBean();

	@Override
	public void completeAnswer(AnswerVO answer) throws Exception {
		DomainVO domain = getDomain(answer.getDomainid());
		if(domain==null) return;
		if(DomainVO.WEIXIN_PROXY_TYPE_NONE.equals(domain.getWeixinProxyType())) return;

		QuestionnaireVO questionnaire = (QuestionnaireVO) questionnaireProcess.doView(answer.getQuestionnaire_id());
		
		UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
		UserVO userVO = (UserVO) userProcess.doView(questionnaire.getCreator());
		
		String touser = userVO.getLoginno();
		String title = "[提醒]"+questionnaire.getTitle();
		
		String desc = "您发布的问卷所有参与人都已完成填写。";
		String url = getShowUrl(questionnaire.getId(),domain);
		NotificationUtil.sendRichWeixinMessage(touser,title , desc, url, "", domain.getId(), qm_app_id);
	}
	
	@Override
	public void completeQuestionnaire(QuestionnaireVO questionnaire) throws Exception {
		DomainVO domain = getDomain(questionnaire.getDomainid());
		if(domain==null) return;
		if(DomainVO.WEIXIN_PROXY_TYPE_NONE.equals(domain.getWeixinProxyType())) return;
		
		UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
		UserVO userVO = null;
		String actorIds = questionnaire.getActorIds();
		StringBuffer actorIdsBuffer = new StringBuffer();
		for(String actorId: actorIds.split(";")){
			if(StringUtil.isBlank(actorId))
				continue;
			userVO = (UserVO) userProcess.doView(actorId);
			if(userVO.getLoginno()!=null)
			actorIdsBuffer.append(userVO.getLoginno()).append("|");
		}
		String touser_ = actorIdsBuffer.toString();
		String touser = touser_.substring(0,touser_.length()-1);

		String title = questionnaire.getTitle();
		String content = "您填写的问卷 [" + title + "] 已成功回收，感谢您的积极参与！";
		NotificationUtil.sendTextWeixinMessage(touser, content, domain.getId(), qm_app_id);
	}

	@Override
	public void publish(QuestionnaireVO questionnaire) throws Exception {
		DomainVO domain = getDomain(questionnaire.getDomainid());
		if(domain==null) return;
		if(DomainVO.WEIXIN_PROXY_TYPE_NONE.equals(domain.getWeixinProxyType())) return;
		
		UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
		UserVO userVO = null;
		String actorIds = questionnaire.getActorIds();
		StringBuffer actorIdsBuffer = new StringBuffer();
		for(String actorId: actorIds.split(";")){
			if(StringUtil.isBlank(actorId))
				continue;
			userVO = (UserVO) userProcess.doView(actorId);
			if(userVO.getLoginno()!=null)
			actorIdsBuffer.append(userVO.getLoginno()).append("|");
		}
		
		String touser_ = actorIdsBuffer.toString();
		String touser = touser_.substring(0,touser_.length()-1);
		
		String title = "[待办]"+questionnaire.getTitle();
		String desc = questionnaire.getCreatorName()+"邀请你参加问卷调查。";
		String url = getWriteUrl(questionnaire.getId(),domain);
		NotificationUtil.sendRichWeixinMessage(touser,title , desc, url, "", domain.getId(), qm_app_id);
	}
	
	/**
	 * 获取待填写问卷详情URL地址
	 * @param questionnaireId 问卷Id
	 * @param user 当前用户
	 * @param domain 当前企业域
	 * @return
	 */
	private String getWriteUrl(String questionnaireId,DomainVO domain){
		StringBuilder url = new StringBuilder();
		if(DomainVO.WEIXIN_PROXY_TYPE_LOCAL.equals(domain.getWeixinProxyType())){//本地绑定模式
			url.append(domain.getServerHost())
			.append("/qm/wap/center.jsp?active=qm-write&id=")
			.append(questionnaireId);
			
		}else if(DomainVO.WEIXIN_PROXY_TYPE_CLOUD.equals(domain.getWeixinProxyType())){//云端代理模式
			try {
				String redirect_uri = URLEncoder.encode(weioa365_addr.replace("{site_id}", Environment.getMACAddress())+"/qm/wap/center.jsp?active=qm-write&id="+questionnaireId,"utf-8");
				url.append("https://open.weixin.qq.com/connect/oauth2/authorize?appid=")
				.append("(appid)")
				.append("&redirect_uri=").append(redirect_uri)
				.append("&response_type=code&scope=snsapi_base&state=").append(domain.getId())
				.append("#wechat_redirect");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return url.toString();
	}
	
	/**
	 * 获取问卷详情URL地址
	 * @param questionnaireId 问卷Id
	 * @param user 当前用户
	 * @param domain 当前企业域
	 * @return
	 */
	private String getShowUrl(String questionnaireId,DomainVO domain){
		StringBuilder url = new StringBuilder();
		if(DomainVO.WEIXIN_PROXY_TYPE_LOCAL.equals(domain.getWeixinProxyType())){//本地绑定模式
			url.append(domain.getServerHost())
			.append("/qm/wap/center.jsp?active=qm-show&id=")
			.append(questionnaireId);
			
		}else if(DomainVO.WEIXIN_PROXY_TYPE_CLOUD.equals(domain.getWeixinProxyType())){//云端代理模式
			try {
				String redirect_uri = URLEncoder.encode(weioa365_addr.replace("{site_id}", Environment.getMACAddress())+"/qm/wap/center.jsp?active=qm-show&id="+questionnaireId,"utf-8");
				url.append("https://open.weixin.qq.com/connect/oauth2/authorize?appid=")
				.append("(appid)")
				.append("&redirect_uri=").append(redirect_uri)
				.append("&response_type=code&scope=snsapi_base&state=").append(domain.getId())
				.append("#wechat_redirect");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return url.toString();
	}

	/**
	 * 获取企业域
	 * @param domainId
	 * @return
	 */
	private DomainVO getDomain(String domainId){
		try {
			DomainProcess domainProcess = (DomainProcess) ProcessFactory.createProcess(DomainProcess.class);
			
			DomainVO domain  = (DomainVO) domainProcess.doView(domainId);
			
			return domain;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}

}
