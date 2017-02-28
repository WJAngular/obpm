package cn.myapps.core.dynaform.form.ejb;

import org.apache.commons.lang3.StringUtils;

import cn.myapps.constans.Environment;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.dynaform.PermissionType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;

/**
 * 二维码控件
 * @author Happy
 *
 */
public class QRCodeField extends FormField {
	
	/**
	 * 微OA365反向代理模式外网访问地址
	 */
	private static final String weioa365_addr = "https://yun.weioa365.com/{site_id}";
	
	/**
	 * 显示值
	 */
	public static final String HANDLE_TYPE_TEXT = "text";
	/**
	 * 打开链接（打开表单）
	 */
	public static final String HANDLE_TYPE_LINK = "link";
	/**
	 * 执行扫码事件回调脚本
	 */
	public static final String HANDLE_TYPE_CALLBACK_EVENT = "callback_event";
	
	/**
	 * 二维码默认大小
	 */
	public static final String DEFAULT_SIZE = "200";
	
	/**
	 * 扫码处理类型（显示值|打开链接|执行回调事件）
	 */
	protected String handleType;	
	
	/**
	 * 二维码大小（默认值为200像素）
	 */
	protected String size;
	
	/**
	 * 扫码回调脚本
	 */
	protected String callbackScript;

	private static final long serialVersionUID = -7343130117030142047L;

	public String getCallbackScript() {
		return callbackScript;
	}

	public void setCallbackScript(String callbackScript) {
		this.callbackScript = callbackScript;
	}

	public String getHandleType() {
		return handleType;
	}

	public void setHandleType(String handleType) {
		this.handleType = handleType;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	@Override
	public String toHtmlTxt(Document doc, IRunner runner, WebUser webUser, int permissionType)
			throws Exception {
		StringBuffer html = new StringBuffer();

		int displayType = getDisplayType(doc, runner, webUser, permissionType);
		
		if(doc != null) {
			if(displayType==PermissionType.HIDDEN){
				return getHiddenValue();
			}else{
				html.append(toHtml(doc, runner, webUser, displayType));
			}
		}
		
		return html.toString();
	}
	
	public String toHtml(Document doc, IRunner runner, WebUser webUser,int displayType)throws Exception {
		StringBuffer html = new StringBuffer();
		
		Object value = null;
		if(HANDLE_TYPE_TEXT.equals(handleType) && !StringUtil.isBlank(this.getValueScript())){
			value = runner.run("QRCodeField ["+this.getFullName()+"].ValueScript", this.getValueScript());
		}
		value = value != null ? value : "";
		html.append("<input moduleType=\"qrcodefield\" type=\"hidden\" id=\"").append(doc.getId()+"_"+getId())
		.append("\" name=\"").append(getName()).append("\" ")
		.append("value=\"").append(value).append("\" ")
		.append("data-refresh_on_changed=\"").append(this.refreshOnChanged).append("\" ")
		.append("data-display-type=\"").append(displayType).append("\" ")
		.append("data-field-id=\"").append(getId()).append("\" ")
		.append("data-handle-type=\"").append(handleType).append("\" ")
		.append("data-server-host=\"").append(getServerHost(doc.getDomainid())).append("\" ")
		.append("data-size=").append(!StringUtils.isNumeric(size) ? DEFAULT_SIZE : size).append(" ")
		.append("data-discription=\"").append(getDiscript()).append("\" />");
		
		return html.toString();
	}
	
	/**
	 * 获取服务器外网地址
	 * @param domainId
	 * 		企业域id
	 * @return
	 */
	private String getServerHost(String domainId){
		try {
			DomainProcess domainProcess = (DomainProcess) ProcessFactory.createProcess(DomainProcess.class);
			DomainVO domain =  (DomainVO) domainProcess.doView(domainId);
			if(DomainVO.WEIXIN_PROXY_TYPE_LOCAL.equals(domain.getWeixinProxyType())){//本地绑定模式
				return domain.getServerHost();
			}else if(DomainVO.WEIXIN_PROXY_TYPE_CLOUD.equals(domain.getWeixinProxyType())){//云端代理模式
				return weioa365_addr.replace("{site_id}", Environment.getMACAddress());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
		
	}

	@Override
	public String toPrintHtmlTxt(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		if (doc != null) {
			int printDisplayType = getPrintDisplayType(doc, runner, webUser);
			//如果为"打印时隐藏",则隐藏
			if (printDisplayType == PermissionType.HIDDEN) {
				return this.getPrintHiddenValue();
			}
			return toHtml(doc,runner,webUser,getDisplayType(doc, runner, webUser));
		}
		return "";
	}

	@Override
	public String toMbXMLText(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toMbXMLText2(Document doc, IRunner runner, WebUser webUser)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

}
