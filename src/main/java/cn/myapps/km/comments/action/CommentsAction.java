package cn.myapps.km.comments.action;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;


import cn.myapps.km.base.action.AbstractRunTimeAction;
import cn.myapps.km.base.ejb.NRunTimeProcess;
import cn.myapps.km.comments.ejb.Comments;
import cn.myapps.km.comments.ejb.CommentsProcess;
import cn.myapps.km.comments.ejb.CommentsProcessBean;
import cn.myapps.km.org.ejb.NUser;

public class CommentsAction extends AbstractRunTimeAction<Comments>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5833429093904365690L;
	
	/**
	 * 评价
	 */
	private String _evaluate;
	
	public String get_evaluate() {
		return _evaluate;
	}

	public void set_evaluate(String evaluate) {
		_evaluate = evaluate;
	}

	public CommentsAction() {
		super();
		content = new Comments();
	}




	@Override
	public NRunTimeProcess<Comments> getProcess() {
		// TODO Auto-generated method stub
		return new CommentsProcessBean();
	}

	public String doSave() throws Exception{
		CommentsProcess commentsProcess=(CommentsProcess)getProcess();
		Comments comments=(Comments) (this.getContent());
		NUser user = getUser();
		comments.setUserId(user.getId());
		comments.setUserName(user.getName());
		comments.setAssessmentDate(new Date());
		if("good".equals(_evaluate)){
			comments.setGood(true);
		}else{
			comments.setBad(false);
		}
		
		try {
			ActionContext ctx = ActionContext.getContext();        
			HttpServletRequest request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);        
			HttpServletResponse response = (HttpServletResponse)ctx.get(ServletActionContext.HTTP_RESPONSE);
			commentsProcess.doCreate(comments,request,response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

}
