package cn.myapps.core.personalmessage.action;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;



import cn.myapps.core.personalmessage.attachment.ejb.AttachmentProcess;
import cn.myapps.core.personalmessage.attachment.ejb.PM_AttachmentVO;
import cn.myapps.core.personalmessage.ejb.PersonalMessageProcess;
import cn.myapps.core.personalmessage.ejb.VoteOptionsProcess;
import cn.myapps.core.personalmessage.ejb.VoteOptionsVO;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

public class PersonalMessageHelper {

	public String createUsers(String domainid, String[] def) throws Exception {

		UserProcess up = (UserProcess) ProcessFactory
				.createProcess(UserProcess.class);
		Collection<UserVO> users = up.queryByDomain(domainid, 1, 10);

		return createDomainUserList(users, def);
	}

	public String createDomainUserList(Collection<UserVO> cols, String[] def) {
		StringBuffer fun = new StringBuffer();
		fun.append("<table width='100%'>");
		fun.append("<tr>");
		fun.append("<td>&nbsp;</td>");
		fun.append("<td>{*[UserName]*}</td><td>{*[Account]*}</td>");
		fun.append("</tr>");
		for (Iterator<UserVO> iter = cols.iterator(); iter.hasNext();) {
			fun.append("<tr>");

			UserVO user = iter.next();
			String checked = "";
			if (def != null) {
				for (int k = 0; k < def.length; k++) {
					if (def[k] != null && def[k].equals(user.getId())) {
						checked = " checked ";
						break;
					}
				}
			}
			fun.append("<td><input name='colids' type='checkbox' value='")
					.append(user.getId()).append("'").append(checked).append(
							" /></td>");
			fun.append("<td class='commFont'>").append(user.getName()).append(
					"</td>");
			fun.append("<td class='commFont'>").append(user.getLoginno())
					.append("</td>");

			fun.append("</tr>");
		}
		fun.append("</table>");
		return fun.toString();
	}

	public String findUserName(String ids) throws Exception {
		if (ids != null) {
			UserProcess up = (UserProcess) ProcessFactory
					.createProcess(UserProcess.class);
			String[] id = ids.split(",");
			StringBuffer names = new StringBuffer();
			for (int i = 0; i < id.length; i++) {
				UserVO user = (UserVO) up.doView(id[i]);
				if (user != null)
					names.append(user.getName()).append(";");
			}
			return names.toString();
		}
		return "";
	}
	
	public String findUserNamesByBodyId(String bodyId) throws Exception {
		try {
			PersonalMessageProcess process = (PersonalMessageProcess) ProcessFactory.createProcess(PersonalMessageProcess.class);
			String[] ids = process.getReceiverUserIdsByMessageBodyId(bodyId);
			if (ids != null && ids.length > 0) {
				UserProcess up = (UserProcess) ProcessFactory
						.createProcess(UserProcess.class);
				StringBuffer names = new StringBuffer();
				for (int i = 0; i < ids.length; i++) {
					UserVO user = (UserVO) up.doView(ids[i]);
					if (user != null) {
						names.append(user.getName()).append(";");
					}
				}
				String namesString = names.toString();
				return namesString.endsWith(";") ? namesString.substring(0, namesString.length()-1) : namesString;
			}
		} catch (Exception e) {
			
		}
		return "";
	}
	
	public String findUserNamesByMsgIds(String ids) throws Exception {
		try {
			
			if(ids != null){
				String[] idStrings = ids.split(",");
				if (idStrings.length > 0) {
					StringBuffer names = new StringBuffer();
					for (int i = 0; i < idStrings.length; i++) {
						names.append(findUserName(idStrings[i]));
					}
					String namesString = names.toString().trim();
					return namesString.endsWith(";") ? namesString.substring(0, namesString.length()-1) : namesString;
				}
			}

		} catch (Exception e) {
			
		}
		return "";
	}
	
	public String findUserNameById(String id) throws Exception {
		if (!StringUtil.isBlank(id)) {
			UserProcess up = (UserProcess) ProcessFactory
					.createProcess(UserProcess.class);
			UserVO user = (UserVO) up.doView(id);
			if (user != null) {
				return user.getName();
			}
		}
		return "";
	}

	public int countMessage(String userId) throws Exception {
		PersonalMessageProcess pmp = (PersonalMessageProcess) ProcessFactory
				.createProcess(PersonalMessageProcess.class);
		return pmp.countNewMessages(userId);
	}
	
	public int countIsReadMessage(String userId) throws Exception {
		PersonalMessageProcess pmp = (PersonalMessageProcess) ProcessFactory
				.createProcess(PersonalMessageProcess.class);
		return pmp.countIsReadMessages(userId);
	}
	
	public String regexHtml(String text) {
		if (!StringUtil.isBlank(text)) {
			text = htmlDecodeEncoder(text);
			Pattern pattern = Pattern.compile("<.+?>", Pattern.DOTALL);
			Matcher matcher = pattern.matcher(text);
			text = matcher.replaceAll(" ");
			text = text.replaceAll("&#160;", " ");
		}
		return text;
	}
	
	public String htmlDecodeEncoder(String content) {
		if (StringUtil.isBlank(content)) {
			return content;
		}
		content = content.replaceAll("&quot;", "\"");
		content = content.replaceAll("&amp;", "&");
		content = content.replaceAll("&lt;", "<");
		content = content.replaceAll("&gt;", ">");
		content = content.replaceAll("&nbsp;", " ");
		return content;
	}
	
	public String findAttachmentsByIds(String attachmentid,boolean outbox) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String contextPath = request.getContextPath();
		if(attachmentid != null){
			String[] ids = attachmentid.split(";");
			if (ids.length > 0) {
				StringBuffer html = new StringBuffer();
				for (int i = 0; i < ids.length; i++) {
					PM_AttachmentVO attachmentvo = findAttachmentById(ids[i]);
					if(attachmentvo != null){
						html.append("<div>"+(i+1)+"：<img src=\"" + contextPath + "/portal/share/email/images/attachment.png\" /><a  onclick=\'downloadAttachment(\""+attachmentvo.getId()+"\")\'");
						html.append(" href=\"#\">");
						html.append(attachmentvo.getRealFileName());
						html.append("&nbsp;&nbsp;&nbsp;("+attachmentvo.getSizeString()+")");
						html.append("</a>");
						if(outbox){
							html.append("<img id='" + attachmentvo.getId() + "' onclick=\"if(confirm('{*[core.email.attachment.delete.confrm]*}？')){deleteAttachment(this,'',this.id);UpdateMsgAfterDeleteAttachment(this.id);}\" src=\"" + contextPath + "/portal/share/email/images/close.gif\" border='0' title='{*[Delete]*}' />");
						}
						html.append("</div>");
					}
				}
				String htmlString = html.toString().trim();
				return htmlString;
			}
		}
		return null;
	}
	
	public  String checkIsExist(String attid){
		try {
			PM_AttachmentVO vo = findAttachmentById(attid);
			if(vo != null){
				File file = new File(vo.getPath());
				if(file.exists()){
					return "true";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "false";
	}
	
	public PM_AttachmentVO findAttachmentById(String id) throws Exception {
		AttachmentProcess ap = (AttachmentProcess) ProcessFactory
		.createProcess(AttachmentProcess.class);
		return (PM_AttachmentVO)ap.getAttachment(id);
	}
	
	public String getVoteOptionsHtml(String radioOrCheckbox,String voteOptionsIds,String checkedOptionsId){
		try {
			if(!StringUtil.isBlank(voteOptionsIds)){
				
				VoteOptionsProcess vop = (VoteOptionsProcess) ProcessFactory
				.createProcess(VoteOptionsProcess.class);
				
				StringBuffer html = new StringBuffer();
				String[] options = voteOptionsIds.split(";");
				//计算总投票数
				int countVotes = 0;
				for(int i = 0;i < options.length; i++){
					if(!StringUtil.isBlank(options[i])){
						try {
							VoteOptionsVO vo = (VoteOptionsVO) vop.findVoteOptionsVOById(options[i]);
							countVotes += vo.getVotes();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				//设置选项类型
				String optionsType = "";
				if(!StringUtil.isBlank(radioOrCheckbox)){
					if(radioOrCheckbox.equalsIgnoreCase("0")){
						optionsType = "radio";
					}else{
						optionsType = "checkbox";
					}
				}
				//拼装html
				for(int i = 0;i < options.length; i++){
					if(!StringUtil.isBlank(options[i])){
						try {
							html.append("<div><input type='"+optionsType+"' disabled='disabled' name='content.checkedOptionsId' value='");
	
							VoteOptionsVO vo = (VoteOptionsVO) vop.findVoteOptionsVOById(options[i]);
							html.append(vo.getId()+"'");
							if(!StringUtil.isBlank(checkedOptionsId)){
								String[] checkedids = checkedOptionsId.split(",");
								for(int j = 0;j < checkedids.length; j++){
									if(vo.getId().equalsIgnoreCase(checkedids[j].trim())){
										html.append(" checked='checked'");
									}
								}
							}
							//计算百分比
							double d = countVotes >0? vo.getVotes()*1.00/countVotes : 0;
							double dl = d*100;
							DecimalFormat format = new DecimalFormat("##.##");
							String value = format.format((Number) dl);
							
							html.append(">"+vo.getValue()+"</input>......................................................"+
									vo.getVotes()+"{*[cn.myapps.core.personalmessage.votes]*}（"+value+"%）</div>");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				return html.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String findDisplayText(String num){
		if(!StringUtil.isBlank(num)){
			String text = "";
			char key = num.charAt(0);
			switch (key) {
			case '0':
				text = "{*[PersonalMessage]*}";
				break;
			case '1':
				text = "{*[Announcement]*}";
				break;
			case '2':
				text = "{*[cn.myapps.core.personalmessage.vote]*}";
				break;
			case '3':
				text = "问卷调查";
				break;
			default:
				text = "{*[cn.myapps.core.personalmessage.vote]*}";
				break;
			}
			return text;
		}
		return null;
	}
}
