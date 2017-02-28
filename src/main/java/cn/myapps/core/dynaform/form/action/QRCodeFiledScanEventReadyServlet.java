package cn.myapps.core.dynaform.form.action;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.constans.Environment;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.form.ejb.FormProcess;
import cn.myapps.core.dynaform.form.ejb.QRCodeField;
import cn.myapps.support.weixin.WeixinServiceProxy;
import cn.myapps.util.CreateProcessException;
import cn.myapps.util.ProcessFactory;

/**
 * @author Happy
 *
 */
public class QRCodeFiledScanEventReadyServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5644106523329927589L;
	/**
	 * 微OA365反向代理模式外网访问地址
	 */
	private static final String weioa365_addr = "https://yun.weioa365.com/{site_id}";
	

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String domainId = req.getParameter("domainId");
		String formId = req.getParameter("formId");
		String docId = req.getParameter("docId");
		String fieldId = req.getParameter("fieldId");
		String applicationId = req.getParameter("applicationId");
		String _randomCode = req.getParameter("_randomCode");
		
		try {
			DomainProcess domainProcess = (DomainProcess) ProcessFactory.createProcess(DomainProcess.class);
			FormProcess formProcess = (FormProcess) ProcessFactory.createProcess(FormProcess.class);
			
			//if(isFromWeixin(req)){//通过微信客户端访问
				
				DomainVO domain =  (DomainVO) domainProcess.doView(domainId);
				Form form = (Form) formProcess.doView(formId);
				QRCodeField qrcodeField = (QRCodeField) form.findField(fieldId);
				
				String redirect = null;//跳转地址
				if(QRCodeField.HANDLE_TYPE_LINK.equals(qrcodeField.getHandleType())){//跳转到表单
					
					if(DomainVO.WEIXIN_PROXY_TYPE_LOCAL.equals(domain.getWeixinProxyType())){//本地绑定模式
						redirect = domain.getServerHost()+"/portal/dynaform/document/view.action?_formid="+formId+
								"&_docid="+docId+"&application="+applicationId+"&openType=from_weixin_message&_backURL="+Environment.getInstance().getContextPath()+"/portal/share/index.jsp";
						
					}else if(DomainVO.WEIXIN_PROXY_TYPE_CLOUD.equals(domain.getWeixinProxyType())){//云端代理模式
						redirect = weioa365_addr.replace("{site_id}", Environment.getMACAddress())+"/portal/dynaform/document/view.action?_formid="+formId+
								"&_docid="+docId+"&application="+applicationId+"&openType=from_weixin_message&_backURL="+Environment.getInstance().getContextPath()+"/portal/share/index.jsp";
					}
					
				}else if(QRCodeField.HANDLE_TYPE_CALLBACK_EVENT.equals(qrcodeField.getHandleType())){//执行扫码回调事件
					
					if(DomainVO.WEIXIN_PROXY_TYPE_LOCAL.equals(domain.getWeixinProxyType())){//本地绑定模式
						redirect = domain.getServerHost()+"/portal/document/qrcodefield/handle?action=excute&domainId="+domain.getId()+"&formId="+formId+"&formId="+formId+"&docId="+docId+"&fieldId="+fieldId+"&applicationId="+applicationId+"&_randomCode="+_randomCode;
					}else if(DomainVO.WEIXIN_PROXY_TYPE_CLOUD.equals(domain.getWeixinProxyType())){//云端代理模式
						redirect = weioa365_addr.replace("{site_id}", Environment.getMACAddress())+"/portal/document/qrcodefield/handle?action=excute&domainId="+domain.getId()+"&formId="+formId+"&formId="+formId+"&docId="+docId+"&fieldId="+fieldId+"&applicationId="+applicationId+"&_randomCode="+_randomCode;
					}
					
				}
				redirect = URLEncoder.encode(redirect, "utf-8");
				String redirect_uri = null;
				if(QRCodeField.HANDLE_TYPE_LINK.equals(qrcodeField.getHandleType())){
					redirect_uri = redirect;
				} else if(QRCodeField.HANDLE_TYPE_CALLBACK_EVENT.equals(qrcodeField.getHandleType())){
					if(DomainVO.WEIXIN_PROXY_TYPE_LOCAL.equals(domain.getWeixinProxyType())){//本地绑定模式
						redirect_uri = domain.getServerHost()+"/portal/phone/resource/component/qrcodeFiled/callback-confirm.jsp";
					}else if(DomainVO.WEIXIN_PROXY_TYPE_CLOUD.equals(domain.getWeixinProxyType())){//云端代理模式
						redirect_uri = weioa365_addr.replace("{site_id}", Environment.getMACAddress())+"/portal/phone/resource/component/qrcodeFiled/callback-confirm.jsp";
					}
					redirect_uri += "?redirect=" + redirect + "&title=" + qrcodeField.getName() + "&discript=" + qrcodeField.getDiscript();
					redirect_uri = URLEncoder.encode(redirect_uri,"utf-8");
				}
				String url = getWeiXinOAuthUrl(domain,redirect_uri);
				resp.sendRedirect(url);
				return;
			//}else{
			//}
			
			
		} catch (CreateProcessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
			
		
		
		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doPost(req, resp);
	}
	
	/**
	 * 获取微信OAuth2用户认证url
	 * @return
	 */
	@SuppressWarnings("static-access")
	private String getWeiXinOAuthUrl(DomainVO domain ,String redirect_uri){
		StringBuilder url = new StringBuilder();
		
		if(DomainVO.WEIXIN_PROXY_TYPE_LOCAL.equals(domain.getWeixinProxyType())){//本地绑定模式
			url.append("https://open.weixin.qq.com/connect/oauth2/authorize?appid=")
			.append(domain.getWeixinCorpID())
			.append("&redirect_uri=").append(redirect_uri)
			.append("&response_type=code&scope=snsapi_base&state=").append(domain.getId())
			.append("#wechat_redirect");
			
		}else if(DomainVO.WEIXIN_PROXY_TYPE_CLOUD.equals(domain.getWeixinProxyType())){//云端代理模式
			String corpId = null;
			try {
				corpId = WeixinServiceProxy.getCorpId(Environment.getInstance().getMACAddress(), domain.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			url.append("https://open.weixin.qq.com/connect/oauth2/authorize?appid=")
			.append(corpId)
			.append("&redirect_uri=").append(redirect_uri)
			.append("&response_type=code&scope=snsapi_base&state=").append(domain.getId())
			.append("#wechat_redirect");
		}
		
		return url.toString();
		
		
	}
	
	

}
