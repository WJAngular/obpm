package cn.myapps.core.links.ejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.constans.Web;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.util.StringUtil;
import cn.myapps.util.http.UrlUtil;
import cn.myapps.util.json.JsonUtil;

/**
 * 链接对象
 * 
 * @author Happy
 * 
 */
public class LinkVO extends ValueObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -461865145129976119L;

	public enum LinkType {
		FORM("00"), // 表单类型
		VIEW("01"), // 视图类型
		REPORT("02"), // 报表类型
		CUSTOMIZE_REPORT("09"), // 自定义报表类型
		RUNQIAN_REPORT("12"),//润乾报表
		EXCELIMPORT("03"), // excel导入类型
		ACTION("04"), // 平台控制器类型
		MANUAL_INTERNAL("05"), // 内部手动链接类型
		MANUAL_EXTERNAL("06"), // 外部手动链接类型
		SCRIPT("07"), // 脚本类型
		EMAIL("08"), // 邮件链接
		BBS("10"), // 论坛
		NetworkDisk("11"), // 论坛
		NONE("@@");

		private String code;

		private LinkType(String code) {
			this.code = code;
		}

		public String getCode() {
			return code;
		}

		public static LinkType getByCode(String code) {
			LinkType[] tyeps = values();
			for (int i = 0; i < tyeps.length; i++) {
				if (tyeps[i].getCode().equals(code)) {
					return tyeps[i];
				}
			}
			return NONE;
		}
	}

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 类型
	 */
	private String type;

	/**
	 * 所属模块
	 */
	private String moduleid;
	
	/**
	 * 链接内容指定的目录(如:portal/下)
	 */
	private String directory;

	/**
	 * 链接内容
	 */
	private String actionContent;

	/**
	 * 请求参数
	 */
	private String queryString;

	/**
	 * 获取链接名称
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置链接名称
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取链接描述
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 设置连接描述
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 获取链接类型
	 * 
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置链接类型
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 获取模块ID
	 * 
	 * @return
	 */
	public String getModuleid() {
		return moduleid;
	}

	/**
	 * 设置模块ID
	 * 
	 * @param moduleid
	 */
	public void setModuleid(String moduleid) {
		this.moduleid = moduleid;
	}

	/**
	 * 获取链接内容
	 * 
	 * @return
	 */
	public String getActionContent() {
		return actionContent;
	}

	/**
	 * 设置链接内容
	 * 
	 * @param actionContent
	 */
	public void setActionContent(String actionContent) {
		this.actionContent = actionContent;
	}

	/**
	 * 获取请求参数(Json 格式)
	 * 
	 * @return
	 */
	public String getQueryString() {
		return queryString;
	}

	/**
	 * 设置请求参数(Json 格式)
	 * 
	 * @param queryString
	 */
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	/**
	 * 设置链接内容指定的目录
	 * 
	 * @param directory
	 */
	public void setDirectory(String directory) {
		this.directory = directory;
	}

	/**
	 * 获取链接内容指定的目录
	 * 
	 * @return directory(example: portal)
	 */
	public String getDirectory() {
		return directory;
	}

	/**
	 * 根据链接对象的配置生成URL
	 * 
	 * @param doc
	 * @param params
	 * @param user
	 * @return
	 */
	public String toLinkUrl(Document doc, ParamsTable params, WebUser user) {
		LinkType type = LinkType.getByCode(this.getType());
		String tempUrl = "";
		StringBuffer url = new StringBuffer();
		switch (type) {
		case FORM:
			tempUrl = "/portal/dynaform/document/newWithPermission.action";
			tempUrl = UrlUtil.parameterize(tempUrl, "_formid", actionContent);
			tempUrl = UrlUtil.parameterize(tempUrl, "_isJump", "1");
			tempUrl = UrlUtil.parameterize(tempUrl, "application", applicationid);
			url.append(tempUrl);
			break;
		case VIEW:
			tempUrl = "/portal/dynaform/view/displayViewWithPermission.action";
			tempUrl = UrlUtil.parameterize(tempUrl, "_viewid", actionContent);
			tempUrl = UrlUtil.parameterize(tempUrl, "clearTemp", "true");
			tempUrl = UrlUtil.parameterize(tempUrl, "application", applicationid);
			url.append(tempUrl);
			break;
		case REPORT:
			if (actionContent.startsWith("$/")) {
				url.append(UrlUtil.parameterize(actionContent.substring(2, actionContent.length()), "application", applicationid));
			} else {
				tempUrl = "/portal/report/crossreport/runtime/runreport.action";
				tempUrl = UrlUtil.parameterize(tempUrl, "reportId", actionContent);
				tempUrl = UrlUtil.parameterize(tempUrl, "application", applicationid);
				url.append(tempUrl);
			}
			break;
		case EXCELIMPORT:
			tempUrl = "/portal/share/dynaform/dts/excelimport/importbyid.jsp";
			tempUrl = UrlUtil.parameterize(tempUrl, "id", actionContent);
			tempUrl = UrlUtil.parameterize(tempUrl, "applicationid", applicationid);
			url.append(tempUrl);
			break;
		case MANUAL_INTERNAL:
			url.append(UrlUtil.parameterize(this.toManual_InternalUrl(), "application", this.applicationid));
			break;
		case EMAIL:
			url.append(UrlUtil.parameterize(actionContent, "application", this.applicationid));
			break;
		case MANUAL_EXTERNAL:
			if (!actionContent.toLowerCase().startsWith("http://")) {
				actionContent = "http://" + actionContent;
			}
			url.append(actionContent);
			break;
		case SCRIPT: // 脚本模式
			try {
				url.append(toScriptUrl4transit());
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case CUSTOMIZE_REPORT:
			url.append(UrlUtil.parameterize(actionContent, "application", this.applicationid));
			break;
		case RUNQIAN_REPORT:
			tempUrl = actionContent;
			tempUrl = UrlUtil.parameterize(actionContent, "_linkid", this.id);
			tempUrl = UrlUtil.parameterize(tempUrl, "applicationid", applicationid);
			url.append(tempUrl);
			break;
		case BBS:
			HttpServletRequest request = params.getHttpRequest();
			String requestUrl = request.getRequestURL().toString();
			String contextPath = request.getContextPath();
			String prex = "";
			int index = requestUrl.indexOf(contextPath);
			if (index > -1) {
				prex = requestUrl.substring(0, index);
			}
			url.append(actionContent != null && "".equals(actionContent) ? prex + actionContent : prex
					+ "/bbs/index.htm");
			break;
		case NetworkDisk:
			url.append(UrlUtil.parameterize("/portal/share/component/networkdisk/networkdisk.jsp", "application", this.applicationid));
			break;
		}
		if (getQueryString().trim().length() > 0 && !"07".equals(getType())&& !"12".equals(getType())) {
			appendQueryString(url, this);
		}
		return url.toString();
	}

	/**
	 * 根据链接对象的配置生成URL
	 * 
	 * @return
	 */
	public String toLinkUrl() {
		try {
			return toLinkUrl(new Document(), new ParamsTable(), new WebUser(new BaseUser()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 根据链接对象的配置生成URL
	 * 
	 * @param user
	 * @return
	 */
	public String toLinkUrl(WebUser user, ParamsTable params) {
		return toLinkUrl(new Document(), params, user);
	}
	
	/**
	 * 根据链接对象的配置生成URL
	 * 
	 * @param user
	 * @return
	 */
	public String toLinkUrl(WebUser user, HttpServletRequest rquest) {
		ParamsTable params = ParamsTable.convertHTTP(rquest);
		return toLinkUrl(new Document(), params, user);
	}

	/**
	 * 在链接对象生成的URL基础上拼接请求参数 返回带请求参数的URL
	 * 
	 * @param url
	 * @param link
	 * @return
	 */
	public String appendQueryString(StringBuffer url, LinkVO link) {
		Collection<Object> qs = JsonUtil.toCollection(link.getQueryString(), JSONObject.class);
		Iterator<Object> iterator = qs.iterator();
		while (iterator.hasNext()) {
			JSONObject object = JSONObject.fromObject(iterator.next());
			if (url.toString().indexOf("?") > -1) {
				url.append("&");
			} else {
				url.append("?");
			}
			url.append(object.get("paramKey")).append("=").append(object.get("paramValue"));
		}

		return url.toString();
	}

	/**
	 * 生成链接类型为脚本类型的URL
	 * 
	 * @param doc
	 * @param params
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public String toScriptUrl(Document doc, ParamsTable params, WebUser user) throws Exception {
		String application = params.getParameterAsString(Web.REQUEST_ATTRIBUTE_APPLICATION);

		IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), application);
		runner.initBSFManager(doc, params, user, new ArrayList<ValidateMessage>());

		int type = Integer.parseInt(this.getType());
		StringBuffer url = new StringBuffer();
		switch (type) {
		case 7:
			StringBuffer label = new StringBuffer();
			label.append("Link(").append(this.getId()).append(").").append(this.getName()).append(".Sript");
			Object result = runner.run(label.toString(), this.getActionContent());
			if (String.valueOf(result).trim().length() > 0) {
				url.append(String.valueOf(result));
			}
			break;
		}
		if (this.getQueryString().trim().length() > 0) {
			appendQueryString(url, this);
		}

		return url.toString();
	}
	
	public String toScriptUrl4transit() throws Exception {
		return "/portal/LinkForScript"; //如果链接类型为脚本链接,生成一个servlet链接,在servlet去运行脚本生成URL
	}
	
	/**
	 * 获取完整的自定义链接(内部)，即(directory + actionContent)
	 * @return 自定义链接url(内部)
	 */
	public String toManual_InternalUrl(){
		try {
			if(StringUtil.isBlank(directory)){
				directory = "portal";//自定义链接(内部),默认链接portal目录下文件
				if(!actionContent.startsWith("/portal") && !actionContent.startsWith("portal")){
					if (actionContent.startsWith("/")){
						actionContent = directory + actionContent;
					}else{
						actionContent = directory + "/" + actionContent;
					}
				}
			}else{
				if("portal".equals(directory)){
					if(!actionContent.startsWith("/portal") && !actionContent.startsWith("portal")){
						if (actionContent.startsWith("/")){
							actionContent = directory + actionContent;
						}else{
							actionContent = directory + "/" + actionContent;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return actionContent;
	}

}
