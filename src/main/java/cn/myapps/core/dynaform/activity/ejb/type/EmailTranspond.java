package cn.myapps.core.dynaform.activity.ejb.type;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import com.opensymphony.xwork2.Action;

import cn.myapps.base.OBPMRuntimeException;
import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.AbstractRunTimeAction;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgProcess;
import cn.myapps.core.dynaform.summary.ejb.SummaryCfgVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.core.workflow.notification.ejb.sendmode.SMSModeProxy;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.mail.EmailUtil;

public class EmailTranspond extends ActivityType {

	public EmailTranspond(Activity act) {
		super(act);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6026837196484129755L;

	public String getOnClickFunction() {
		return "Activity.doExecute('" + act.getId() + "',"+act.getType()+",{summaryCfg:'" + act.getTranspond() + "'})";
		//return "email_transpond('" + act.getTranspond() + "', '" + act.getId() + "')";
	}

	public String getDefaultClass() {
		return DOCUMENT_BUTTON_CLASS;
	}

	public String getButtonId() {
		return DOCUMENT_BUTTON_ID;
	}

	public String getAfterAction() {
		return ACTIVITY_EMAIL_NAMESPACE + "/success.jsp";
	}

	public String getBackAction() {
		return BASE_ACTION;
	}

	public String getBeforeAction() {
		return BASE_ACTION;
	}

	public String getDefaultOnClass() {
		
		return DOCUMENT_BUTTON_ON_CLASS;
	}
	
	public String doExecute(AbstractRunTimeAction action, Document doc,
			WebUser user, ParamsTable params) throws Exception {
		try {
			int emailSuccessCount = 0;
			int emailFailCount = 0;
			int msmSuccessCount = 0;
			int msmFailCount = 0;
			DocumentProcess proxy = (DocumentProcess) ProcessFactory.createRuntimeProcess(DocumentProcess.class, action.getApplication());
			doc = (Document) proxy.doView(params.getParameterAsString("_docid"));
			SummaryCfgProcess mp = (SummaryCfgProcess) ProcessFactory.createProcess(SummaryCfgProcess.class);
			String transpond = params.getParameterAsString("transpond");
			if(transpond == null || transpond.equals("null") || transpond.equals("")){
				action.addFieldError("1", "{*[core.dynaform.form.activity.transpond.template]*}");
				return Action.SUCCESS;
			}
			SummaryCfgVO vo = (SummaryCfgVO) mp.doView(transpond);
			StringBuffer emailContent = new StringBuffer();
			StringBuffer msmContent= new StringBuffer();
			emailContent.append("[" + vo.getTitle() + "]" + "待办:</br>");
			emailContent.append("详情: " + vo.toSummay(doc, user) + "</br>");
			String handleUrl = params.getParameterAsString("handleUrl");
			if(handleUrl.contains("#")){
				handleUrl = handleUrl.replaceAll("#", "");
			}
			emailContent.append("链接Url: " + handleUrl + "</br>");
			emailContent.append("日期: " + new Date().toString());
			String receiverid = params.getParameterAsString("receiverid");
			List<String> emails = getUserEmailAddress(receiverid);
			List<String> telephones = getUserTelephoneNO(receiverid);
			boolean email = params.getParameterAsBoolean("email");
			if(email){
				EmailUtil eu = new EmailUtil();
				Iterator<String> emailIt = emails.iterator();
				while(emailIt.hasNext()){
					String _email = emailIt.next();
					//eu.sendEmailBySystemUser(email, vo.getTitle(), emailContent.toString());
					boolean success = eu.sendEmailBySystemUserForTranspond(_email, vo.getTitle(), emailContent.toString());
					if(success){
						emailSuccessCount++;
					}else{
						emailFailCount++;
					}
				}
	//			this.addActionMessage("邮件：发送成功:" + emailSuccessCount + "  发送失败:" + emailFailCount);
			}
			
			msmContent.append("[" + vo.getTitle() + "]" + "待办: ");
			msmContent.append(vo.toSummay(doc, user) + "  ");
			msmContent.append("日期: " + new Date().toString());
			boolean msm = params.getParameterAsBoolean("msm");
			if(msm){
				SMSModeProxy sender = new SMSModeProxy(user);
				Iterator<String> telephoneIt = telephones.iterator();
				while(telephoneIt.hasNext()){
					String telephone = telephoneIt.next();
					boolean flag = sender.send("", msmContent.toString(), telephone, null, false);
					if(flag){
						msmSuccessCount++;
					}else{
						msmFailCount++;
					}
				}
	//			this.addActionMessage("短信：发送成功:" + msmSuccessCount + "  发送失败:" + msmFailCount);
			}
			if (email || msm) {
				action.addActionMessage("已发送");
			}
			return Action.SUCCESS;
		} catch (OBPMValidateException e) {
			action.addFieldError("1", e.getValidateMessage());
			return Action.INPUT;
		}
		catch (Exception e) {
			action.setRuntimeException(new OBPMRuntimeException(e.getMessage(),e));
			return Action.INPUT;
		}
	}
	
	private List<String> getUserEmailAddress(String receiverid) throws Exception {
		UserProcess up = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
		List<String> emails = new ArrayList<String>();
		String[] _receiverid = receiverid.split(";");
		for(int i=0; i<_receiverid.length; i++){
			UserVO user = (UserVO) up.doView(_receiverid[i]);
			emails.add(user.getEmail());
		}
		return emails;
	}
	
	private List<String> getUserTelephoneNO(String receiverid) throws Exception {
		UserProcess up = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
		List<String> telephones = new ArrayList<String>();
		String[] _receiverid = receiverid.split(";");
		for(int i=0; i<_receiverid.length; i++){
			UserVO user = (UserVO) up.doView(_receiverid[i]);
			telephones.add(user.getTelephone());
		}
		return telephones;
	}

	@Override
	public ValueObject doMbExecte(WebUser user, ParamsTable params)
			throws Exception {
		return null;
	}
}
