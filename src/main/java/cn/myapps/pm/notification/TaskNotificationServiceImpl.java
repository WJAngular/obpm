package cn.myapps.pm.notification;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import cn.myapps.constans.Environment;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.km.util.StringUtil;
import cn.myapps.pm.project.ejb.Project;
import cn.myapps.pm.project.ejb.ProjectProcess;
import cn.myapps.pm.project.ejb.ProjectProcessBean;
import cn.myapps.pm.task.ejb.Follower;
import cn.myapps.pm.task.ejb.Task;
import cn.myapps.pm.task.ejb.TaskProcess;
import cn.myapps.pm.task.ejb.TaskProcessBean;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.notification.NotificationUtil;

/**
 * PM消息提醒服务
 * @author Seven
 *
 */
public class TaskNotificationServiceImpl implements TaskNotificationService{
	
	/**
	 * 微OA365反向代理模式外网访问地址
	 */
	private static final String weioa365_addr = "https://yun.weioa365.com/{site_id}";
	private static final String pm_app_id = "3";
	
	ProjectProcess projectProcess = new ProjectProcessBean();

	@Override
	public void create(Task task, WebUser user) throws Exception {
		DomainVO domain = getDomain(task.getDomainid());
		if(domain==null) return;
		if(DomainVO.WEIXIN_PROXY_TYPE_NONE.equals(domain.getWeixinProxyType())) return;
	}

	@Override
	public void complate(Task task, WebUser user) throws Exception {
		DomainVO domain = getDomain(task.getDomainid());
		if(domain==null) return;
		if(DomainVO.WEIXIN_PROXY_TYPE_NONE.equals(domain.getWeixinProxyType())) return;
		if(!isNotification(task)) return;
		
		try {
			UserProcess up = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			UserVO tUser = (UserVO) up.doView(task.getCreatorId());
			
			TaskProcess taskProcess_ = new TaskProcessBean();
			Collection<Follower> Followers = taskProcess_.getFollowersByTask(task.getId());
			
			//任务完成：创建者 + 关注者 收到通知 
			StringBuffer touser_ = new StringBuffer();
			touser_.append(tUser.getLoginno()+"|");
			//遍历获取关注人列表
			Iterator<Follower> iterator = Followers.iterator();
			while(iterator.hasNext()){
				tUser = (UserVO) up.doView(iterator.next().getUserId());
				touser_.append(tUser.getLoginno()+"|");
			}
			
			String touser = touser_.toString().substring(0,touser_.toString().length()-1);
			
			String title = "[完成]"+task.getName();
			String desc =task.getExecutor()+"完成了任务！";
			String url = getUrl(task.getId(),user,domain);
			NotificationUtil.sendRichWeixinMessage(touser,title , desc, url, "", task.getDomainid(), pm_app_id);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void updateExecutor(Task task, String old_tasker_id,WebUser user) throws Exception {
		DomainVO domain = getDomain(task.getDomainid());
		if(domain==null) return;
		if(DomainVO.WEIXIN_PROXY_TYPE_NONE.equals(domain.getWeixinProxyType())) return;
		if(!isNotification(task)) return;
		
		try {
			UserProcess up = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			UserVO creator = (UserVO) up.doView(task.getCreatorId());
			UserVO excutor = (UserVO) up.doView(task.getExecutorId());
			//UserVO old_excutor =  (UserVO) up.doView(old_tasker_id);
			//任务执行人更改：新执行者和创建人收到通知
			String touser = creator.getLoginno()+"|"+ excutor.getLoginno();
			String title = "[指派]"+task.getName();
			String desc = user.getName()+"把任务指派给了"+excutor.getName();
			String url = getUrl(task.getId(),user,domain);
			NotificationUtil.sendRichWeixinMessage(touser,title, desc, url,"", task.getDomainid(), pm_app_id);
		} catch (Exception e) {
			throw e;
		}
		
		
	}

	@Override
	public void undo(Task task, WebUser user) throws Exception {
		DomainVO domain = getDomain(task.getDomainid());
		if(domain==null) return;
		if(DomainVO.WEIXIN_PROXY_TYPE_NONE.equals(domain.getWeixinProxyType())) return;
		if(!isNotification(task)) return;
	}
	
	@Override
	public void updateTime(Task task, WebUser user) throws Exception {
		DomainVO domain = getDomain(task.getDomainid());
		if(domain==null) return;
		if(DomainVO.WEIXIN_PROXY_TYPE_NONE.equals(domain.getWeixinProxyType())) return;
		if(!isNotification(task)) return;
		
		try {
			UserProcess up = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			//UserVO creator = (UserVO) up.doView(task.getCreatorId());
			UserVO excutor = (UserVO) up.doView(task.getExecutorId());
			//任务时间更新：执行者收到通知(方案待定)
			String touser = excutor.getLoginno();
			String title = "[更新]"+task.getName();
			String start_date_msg = null ;
			String end_date_msg = null; 
			
			if(StringUtil.isBlank(getDate(task.getCreateDate()))){
				start_date_msg = " 待定 ";
			}else{
				start_date_msg = getDate(task.getCreateDate());
			}
			if(StringUtil.isBlank(getDate(task.getEndDate()))){
				end_date_msg = " 待定 ";
			}else{
				end_date_msg = getDate(task.getEndDate());
			}
			
			String desc = user.getName()+"把任务时间修改为: "+start_date_msg+" —— "+end_date_msg;
			String url = getUrl(task.getId(),user,domain);
			NotificationUtil.sendRichWeixinMessage(touser,title, desc, url,"", task.getDomainid(), pm_app_id);
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	@Override
	public void updateTaskStatus(Task task, String old_Status,String new_Status, WebUser user) throws Exception {
		DomainVO domain = getDomain(task.getDomainid());
		if(domain==null) return;
		if(DomainVO.WEIXIN_PROXY_TYPE_NONE.equals(domain.getWeixinProxyType())) return;
		if(!isNotification(task)) return;
		
		try {
			UserProcess up = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
			UserVO creator = (UserVO) up.doView(task.getCreatorId());
			UserVO excutor = (UserVO) up.doView(task.getExecutorId());
			//任务状态更新：创建者和执行者收到通知
			String touser = creator.getLoginno()+"|"+excutor.getLoginno();
			String title = "[更新]"+task.getName();
			String desc = user.getName()+"把任务状态从 "+old_Status+" 修改为 "+new_Status;
			String url = getUrl(task.getId(),user,domain);
			NotificationUtil.sendRichWeixinMessage(touser,title, desc, url,"", task.getDomainid(), pm_app_id);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 返回日期字符串
	 * @param date 
	 *             转换日期
	 * @return
	 */
	protected String getDate(Date date){
		
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
		String day = null;
		try{
			if(date != null){
				day = sdf.format(date);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return day;
	}

	/**
	 * 获取微信消息任务详情URL地址
	 * @param taskId
	 * @param user
	 * @param domain
	 * @return
	 */
	private String getUrl(String taskId,WebUser user,DomainVO domain){
		StringBuilder url = new StringBuilder();
		
		if(DomainVO.WEIXIN_PROXY_TYPE_LOCAL.equals(domain.getWeixinProxyType())){//本地绑定模式
			url.append(domain.getServerHost())
			.append("/pm/wap/index.jsp?taskid=")
			.append(taskId);
			
		}else if(DomainVO.WEIXIN_PROXY_TYPE_CLOUD.equals(domain.getWeixinProxyType())){//云端代理模式
			try {
				String redirect_uri = URLEncoder.encode(weioa365_addr.replace("{site_id}", Environment.getMACAddress())+"/pm/wap/index.jsp?taskid="+taskId,"utf-8");
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

	/**
	 * 是否启用通知服务
	 * @param task
	 *             
	 * @return
	 * @throws Exception
	 */
	private boolean isNotification(Task task) throws Exception {
		try {
			//TODO  将数据保存到二级缓存中
			Project project  = (Project) projectProcess.doView(task.getProjectId());
			if(project!=null)
			{
				return project.isNotification();
			}	
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	


	
	
}
