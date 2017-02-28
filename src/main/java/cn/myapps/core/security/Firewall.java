package cn.myapps.core.security;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;

import cn.myapps.util.StringUtil;
import cn.myapps.util.property.PropertyUtil;

/**
 * 防火墙
 * <p>
 * 用于拦截非法渗透攻击，包含：
 * SQL注入、
 * XSS脚本攻击、
 * CSRF跨站请求伪造攻击、
 * </p>
 * @author Happy
 *
 */
public class Firewall {
	
	
	/**
	 * 是否开启防火墙
	 */
	private boolean startFirewall = false;
	
	/**
	 * 是否开启关键词拦截
	 */
	private boolean validKeyword = false;
	
	/**
	 * 拦截关键词
	 */
	private String[] excludeChars = new String[]{};//非法字符
	
	/**
	 * 拦截关键词的正则表达式
	 */
	private Set<Pattern> excludePatterns = new HashSet<Pattern>();
	
	/**
	 * 是否阻止跨站请求伪造
	 */
	private boolean stopCSRF = false;
	
	/**
	 * 跨站请求伪造 例外地址（格式：http://xxx.xx.com:8001）服务器由前置机反向代理的情况下需要配置此属性
	 */
	private String[] excludeHostAddress = null;
	
	/**
	 * 参数拦截例外URL
	 */
	private String[] ignoreURL = new String[]{};
	
	private static Firewall instance = null;
	
	
	
	private Firewall(boolean startFirewall, boolean validKeyword, String[] excludeChars, Set<Pattern> excludePatterns,
			boolean stopCSRF, String[] excludeHostAddress,String[] ignoreURL) {
		super();
		this.startFirewall = startFirewall;
		this.validKeyword = validKeyword;
		this.excludeChars = excludeChars;
		this.excludePatterns = excludePatterns;
		this.stopCSRF = stopCSRF;
		this.excludeHostAddress = excludeHostAddress;
		this.ignoreURL = ignoreURL;
	}

	public static Firewall getInstance(){
		
		if(instance == null){
			init();
		}
		
		return instance;
	}
	
	private static synchronized void init(){
		
		PropertyUtil.load("security");
		
		boolean startFirewall = false;
		boolean validKeyword = false;
		boolean stopCSRF = false;
		
		String[] excludeChars = new String[]{};
		Set<Pattern> excludePatterns = new HashSet<Pattern>();
		String[] excludeHostAddress = null;
		String[] ignoreURL = new String[]{};
		
		String _startFirewall = PropertyUtil.get("SecurityFilter.firewall.startFirewall");
		String _validKeyword = PropertyUtil.get("SecurityFilter.firewall.interceptor.keyword");
		String _stopCSRF = PropertyUtil.get("SecurityFilter.firewall.interceptor.CSRF");
		String _excludeChars = PropertyUtil.get("SecurityFilter.firewall.interceptor.keyword.excludeChars");
		String _excludePatterns = PropertyUtil.get("SecurityFilter.firewall.interceptor.keyword.excludePatterns");
		String _excludeHostAddress = PropertyUtil.get("SecurityFilter.firewall.interceptor.CSRF.excludeHostAddress");
		String _ignoreURL = PropertyUtil.get("SecurityFilter.firewall.interceptor.keyword.ignoreURL");
		
		
		if(!StringUtil.isBlank(_excludeChars)){
			excludeChars = _excludeChars.split("\\|");
		}
		if(!StringUtil.isBlank(_ignoreURL)){
			ignoreURL = _ignoreURL.split("\\|");
		}
		if(!StringUtil.isBlank(_excludePatterns)){
			String[] regexs = _excludePatterns.split("\\|");
			for(String regex : regexs){
				excludePatterns.add(Pattern.compile(StringEscapeUtils.unescapeJava(regex)));
			}
		}
		if(!StringUtil.isBlank(_excludeHostAddress)){
			excludeHostAddress = _excludeHostAddress.split("\\|");
		}
		if(!StringUtil.isBlank(_startFirewall)){
			startFirewall = Boolean.parseBoolean(_startFirewall);
		}
		if(!StringUtil.isBlank(_validKeyword)){
			validKeyword = Boolean.parseBoolean(_validKeyword);
		}
		if(!StringUtil.isBlank(_stopCSRF)){
			stopCSRF = Boolean.parseBoolean(_stopCSRF);
		}
		
		instance = new Firewall(startFirewall, validKeyword, excludeChars, excludePatterns, stopCSRF, excludeHostAddress,ignoreURL);
	}
	
	/**
	 * 执行防火墙拦截
	 * @param hreq
	 * @param resp
	 * @throws Exception
	 */
	public boolean excute(HttpServletRequest hreq,HttpServletResponse resp) throws Exception {
		
		if(!startFirewall) return true;
		
		//跨站请求伪造攻击拦截
		if(stopCSRF){
			String referer = hreq.getHeader("REFERER");
			if(referer !=null){
				if (excludeHostAddress!=null) {
					boolean flag = true;
					for (String address : excludeHostAddress) {
						if (address==null || referer.startsWith(address.trim())) {
							flag = false;
							break;
						}
					}
					if (flag) {
						resp.setStatus(500);
						resp.setCharacterEncoding("UTF-8");
						resp.setContentType("text/html; charset=UTF-8");
						resp.getWriter().write("非法操作，出于安全考虑系统不允许跨域请求！");
						resp.getWriter().flush();
						resp.getWriter().close();
						return false;
					}
				}
			}
		}
		//请求参数关键字拦截
		if(validKeyword){
			String uri = hreq.getRequestURI().toLowerCase();
			
			if(uri.contains(".jsp") || uri.contains(".action")){//只拦截jsp和action的请求
				
				for (int i = 0; i < ignoreURL.length; i++) {
					if(StringUtil.isBlank(ignoreURL[i])) continue;
					
					if(uri.contains(ignoreURL[i].trim())){
						return true;
					}
					
				}
				
				Map<String, String[]> params = hreq.getParameterMap();  
		        StringBuffer queryString = new StringBuffer();  
		        for (String key : params.keySet()) {  
		            String[] values = params.get(key);  
		            for (int i = 0; i < values.length; i++) {  
		                String value = values[i];  
		                queryString.append(key + "=" + value + "&");  
		            }  
		        }  
		        // 去掉最后一个空格 
		        if(queryString.length()>1){
		        	 queryString.setLength(queryString.length() - 1);  
		        }
				
				if(!checkParametersLegal(queryString.toString())){
					hreq.setAttribute("error", "{*[你提交或打开的链接里可能存在非法字符,请不要在参数中包含非法字符尝试注入攻击！]*}");
					RequestDispatcher dispatcher = hreq.getRequestDispatcher("/portal/share/error.jsp");
					dispatcher.forward(hreq, resp);
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * 检查请求参数的合法性
	 * <p>如果请求参数包含非法字符，返回false，合法则返回true</p>
	 * @param queryString
	 * @return
	 */
	private boolean checkParametersLegal(String queryString){
		if(StringUtil.isBlank(queryString)) return true;
		queryString = queryString.toLowerCase().trim();
		
		for(Pattern pattern : excludePatterns){
			if(pattern.matcher(queryString).find()){
				return false;
			}
		}
		
		for (int i = 0; i < excludeChars.length; i++) {
			String excludeChar = excludeChars[i];
			if(StringUtil.isBlank(excludeChar)) continue;
			if(queryString.contains(excludeChar)){
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * 获取服务器地址
	 * @param request
	 * @return
	 */
	private String getServerHost(HttpServletRequest request){
		StringBuilder host = new StringBuilder();
		host.append(request.getScheme())
			.append("://")
			.append(request.getServerName());
		
		if(request.getServerPort()!=80){
			host.append(":")
				.append(request.getServerPort());
		}
		return host.toString();
	}


}
