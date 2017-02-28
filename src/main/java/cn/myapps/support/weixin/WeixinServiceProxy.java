package cn.myapps.support.weixin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.util.CLoader;

/**
 * 微信服务接口代理
 * @author Happy
 *
 */
public class WeixinServiceProxy {
	
	/**
	 * 同步企业域组织结构到企业微信
	 * @param domain
	 * @throws Exception
	 */
	public static void synch2Weixin(DomainVO domain)  throws Exception{
		invoke("synch2Weixin", new Object[]{domain});
	}
	
	/**
	 * 从企业微信上同步组织结构到企业域
	 * @param domain
	 * @throws Exception
	 */
	public static void synchFromWeixin(DomainVO domain)  throws Exception{
		invoke("synchFromWeixin", new Object[]{domain});
	}
	
	/**
	 * 获取access_token
	 * @param corpid 企业Id
	 * @param corpsecret 管理组的凭证密钥
	 * @return
	 * @throws WeixinHandleException
	 */
	public static String getAccessToken(String corpid, String corpsecret) throws Exception{
		return (String) invoke("getAccessToken", new Object[]{corpid,corpsecret});
	}
	
	/**
	 * 微信oauth2登录验证
	 * @param request
	 * @param response
	 * @param chain
	 * @throws Exception
	 */
	public static void auth(HttpServletRequest request, HttpServletResponse response,FilterChain chain) throws Exception{
		//invoke("auth", new Object[]{request,response,chain});
		Class<?> ownerClass = CLoader.initWeixinService();
	    Class<?>[] argsClass = new Class[]{HttpServletRequest.class,HttpServletResponse.class,FilterChain.class};
	 
	 
	    Method method = ownerClass.getMethod("auth", argsClass);
	    method.invoke(null, new Object[]{request,response,chain});
	}
	
	/**
	 * 发生消息
	 * @param title 标题
	 * @param discription 描述
	 * @param touser 接收用户账号
	 * @param doc 文档对象
	 * @return
	 * @throws Exception
	 */
	public static boolean send(String title,String description,String touser,Document doc) throws Exception {
		return (Boolean) invoke("send", new Object[]{title,description,touser,doc});
	}
	
	/**
	 * 验证微信回调URL
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	public static void validWeixinUrl(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		Class<?> ownerClass = CLoader.initWeixinService();
	    Class<?>[] argsClass = new Class[]{HttpServletRequest.class,HttpServletResponse.class};
	 
	 
	    Method method = ownerClass.getMethod("validWeixinUrl", argsClass);
	    method.invoke(null, new Object[]{request,response});
	}
	
	/**
	 * 处理微信事件回调业务
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	public static void handelWeixinCallbackEvent(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Class<?> ownerClass = CLoader.initWeixinService();
	    Class<?>[] argsClass = new Class[]{HttpServletRequest.class,HttpServletResponse.class};
	 
	 
	    Method method = ownerClass.getMethod("handelWeixinCallbackEvent", argsClass);
	    method.invoke(null, new Object[]{request,response});
	}
	
	/**
	 * 清空微信配置信息缓存
	 * @throws Exception
	 */
	public static void cleanWeixinSecretCache() throws Exception{
		Class<?> ownerClass = CLoader.initWeixinService();
		Method method = ownerClass.getMethod("cleanWeixinSecretCache", new Class[]{});
		method.invoke(null, new Object[]{});
	}
	
	/**
	 * 发送图文消息
	 * @param touser
	 * 		用户账号列表（消息接收者，多个接收者用‘|’分隔）。特殊情况：指定为@all，则向关注该企业应用的全部成员发送
	 * @param title
	 * 		消息标题
	 * @param description
	 * 		消息描述
	 * @param url
	 * 		消息url
	 * @param picUrl
	 * 		图片url
	 * @param domainId
	 * 		企业域id
	 * @param applicationid
	 * 		发送目标软件id（null 表示发送到企业小助手）
	 * @return
	 * @throws Exception
	 */
	public static boolean sendRichTextMessage(String touser,String title,String description,String url,String picUrl,String domainId,String applicationid)  throws Exception{
		return (Boolean) invoke("sendRichTextMessage", new Object[]{touser,title,description,url,picUrl,domainId,applicationid});
	}
	
	/**
	 * 发送文本消息
	 * @param touser
	 * 		用户账号列表（消息接收者，多个接收者用‘|’分隔）。特殊情况：指定为@all，则向关注该企业应用的全部成员发送
	 * @param content
	 * 		消息内容
	 * @param domainId
	 * 		企业域id
	 * @param applicationid
	 * 		发送目标软件id（null 表示发送到企业小助手）
	 * @return
	 * @throws Exception
	 */
	public static boolean sendTextMessage(String touser,String content,String domainId,String applicationid)  throws Exception{
		return (Boolean) invoke("sendTextMessage", new Object[]{touser,content,domainId,applicationid});
	}
	
	/**
	 * 发送微信消息
	 * @param json
	 * 		微信消息(JSON格式 参考微信平台API文档)
	 * @param domainId
	 * 		企业域id
	 * @return
	 * @throws Exception
	 */
	public static boolean sendMessage(String json,String domainId)  throws Exception{
		return (Boolean) invoke("sendMessage", new Object[]{json,domainId});
	}
	
	/**
	 * 创建微信用户
	 * @param weixinCorpID
	 * 		企业号id
	 * @param weixinCorpSecret
	 * 		企业号开发凭据
	 * @param name
	 * 		用户名
	 * @param userid
	 * 		账号
	 * @param mobile
	 * 		手机
	 * @param email
	 * 		邮件
	 * @param dept
	 * 		部门id
	 * @return
	 * @throws Exception
	 */
	public static boolean createUser(String domainId,String name,String userid,String mobile,String email,Integer[] dept)  throws Exception{
		return (Boolean) invoke("createUser", new Object[]{domainId,name,userid,mobile,email,dept});
	}
	
	public static boolean updateUser(String domainId,String name,String userid,String mobile,String email,Integer[] dept)  throws Exception{
		return (Boolean) invoke("updateUser", new Object[]{domainId,name,userid,mobile,email,dept});
	}
	
	public static boolean deleteUser(String domainId,String userid)  throws Exception{
		return (Boolean) invoke("deleteUser", new Object[]{domainId,userid});
	}
	
	/**
	 * @param domainId
	 * @param name
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public static int createDepartment(String domainId,String name,int parentId)  throws Exception{
		return (Integer) invoke("createDepartment", new Object[]{domainId,name,parentId});
	}
	
	public static void updateDepartment(String domainId,int id,String name,int parentId)  throws Exception{
		invoke("updateDepartment", new Object[]{domainId,id,name,parentId});
	}
	
	public static void deleteDepartment(String domainId,String id)  throws Exception{
		invoke("deleteDepartment", new Object[]{domainId,id});
	}
	
	/**
	 * 删除部门下的所有用户
	 * @param weixinCorpID
	 * @param weixinCorpSecret
	 * @param deptid
	 * @throws Exception
	 */
	public static void clearDeptUsers(String weixinCorpID,String weixinCorpSecret,String deptid)  throws Exception{
		invoke("clearDeptUsers", new Object[]{weixinCorpID,weixinCorpSecret,deptid});
	}
	
	
	/**
	 * @param url
	 * @param domainid 
	 * @return
	 */
	public static Map<String, String> getJsapiConfig(String url, String domainid) throws Exception{
		return (Map<String, String>) invoke("getJsapiConfig", new Object[]{url,domainid});
	}
	
	
	/**
	 * 下载微信多媒体资源文件
	 * @param domainId
	 * 		企业域id
	 * @param media_id
	 * 		媒体资源id
	 * @param fileName
	 * 		下载到本地服务器的完整文件名
	 * @throws Exception
	 */
	public static void downloadMedia(String domainId,String media_id,String fileName) throws Exception{
		invoke("downloadMedia", new Object[]{domainId,media_id,fileName});
	}
	
	/**
	 * 获取企业号id
	 * @param site_id
	 * 		客户端唯一标示
	 * @param domainId
	 * 		企业域id
	 * @return
	 * @throws WeixinHandleException
	 */
	public static String getCorpId(String site_id,String domainId) throws Exception {
		return (String) invoke("getCorpId", new Object[]{site_id,domainId});
	}

	
	private static Object invoke(String methodName, Object[] args) throws Exception {
		try{
			Class<?> ownerClass = CLoader.initWeixinService();
		    Class<?>[] argsClass = new Class[args.length];
		 
		    for (int i = 0, j = args.length; i < j; i++) {
		        argsClass[i] = args[i].getClass();
		    }
		 
		    Method method = ownerClass.getMethod(methodName, argsClass);
		    return method.invoke(null, args);
		}catch (InvocationTargetException e) {
			Throwable t = e.getTargetException();
			if(t instanceof OBPMValidateException){
				throw (OBPMValidateException)t;
			}else{
				throw new OBPMValidateException(t.getLocalizedMessage(),t);
			}
		}catch (Exception e) {
			throw e;
		}
	}

	
	

}
