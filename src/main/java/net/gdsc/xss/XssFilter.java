package net.gdsc.xss;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;

import cn.myapps.util.StringUtil;
import cn.myapps.util.property.PropertyUtil;

public class XssFilter implements Filter {

	private static final Logger log = Logger.getLogger(XssFilter.class);
	private String[] excludeChars = new String[] {};// 非法字符
	private Set<Pattern> excludePatterns = new HashSet<Pattern>();// 非法字符正则表达式
	private boolean stopCSRF = false;// 是否阻止跨站请求伪造

	public void init(FilterConfig filterConfig) throws ServletException {
		PropertyUtil.load("security");

		String _excludeChars = PropertyUtil.get("SecurityFilter.excludeChars");
		String _excludePatterns = PropertyUtil
				.get("SecurityFilter.excludePatterns");
		String _stopCSRF = PropertyUtil.get("SecurityFilter.stopCSRF");
		if (!StringUtil.isBlank(_excludeChars)) {
			excludeChars = _excludeChars.split("\\|");
		}
		if (!StringUtil.isBlank(_excludePatterns)) {
			String[] regexs = _excludePatterns.split("\\|");
			for (String regex : regexs) {
				excludePatterns.add(Pattern.compile(StringEscapeUtils
						.unescapeJava(regex)));
			}
		}
		if (!StringUtil.isBlank(_stopCSRF)) {
			stopCSRF = Boolean.parseBoolean(_stopCSRF);
		}
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		log.info("#######################sql and xss,to do filter###################################");
		HttpServletRequest hreq = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		try {
			if (!interceptIllegalInjection(hreq, resp))
				return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper(
				(HttpServletRequest) request);
		chain.doFilter(xssRequest, response);
	}

	/**
	 * 获取服务器地址
	 * 
	 * @param request
	 * @return
	 */
	private String getServerHost(HttpServletRequest request) {
		StringBuilder host = new StringBuilder();
		host.append(request.getScheme()).append("://")
				.append(request.getServerName());

		if (request.getServerPort() != 80) {
			host.append(":").append(request.getServerPort());
		}
		return host.toString();
	}

	private boolean interceptIllegalInjection(HttpServletRequest hreq,
			HttpServletResponse resp) throws Exception {

		// 拦截跨站请求伪造攻击
		if (stopCSRF) {
			String referer = hreq.getHeader("REFERER");
			if (referer != null && !referer.startsWith(getServerHost(hreq))) {
				resp.setStatus(500);
				resp.setCharacterEncoding("UTF-8");
				resp.setContentType("text/html; charset=UTF-8");
				resp.getWriter().write("非法操作，出于安全考虑系统不允许跨域请求！");

				resp.getWriter().flush();
				resp.getWriter().close();
				return false;
			}
		}

		String uri = hreq.getRequestURI().toLowerCase();
		log.info("请求地址：" + uri);
		if (uri.contains(".jsp") || uri.contains(".action")) {// 只拦截jsp和action的请求

			if (!checkParametersLegal(getRequestPayload(hreq))) {
				hreq.setAttribute("error",
						"{*[你提交或打开的链接里可能存在非法字符,请不要在参数中包含非法字符尝试注入攻击！]*}");
				RequestDispatcher dispatcher = hreq
						.getRequestDispatcher("/portal/share/error.jsp");
				dispatcher.forward(hreq, resp);
				return false;
			}
		}
		return true;
	}

	/**
	 * 检查请求参数的合法性
	 * <p>
	 * 如果请求参数包含非法字符，返回false，合法则返回true
	 * </p>
	 * 
	 * @param queryString
	 * @return
	 */
	private boolean checkParametersLegal(String queryString) {
		log.info(queryString);
		if (StringUtil.isBlank(queryString))
			return true;
		queryString = queryString.toLowerCase().trim();

		for (Pattern pattern : excludePatterns) {
			if (pattern.matcher(queryString).find()) {
				return false;
			}
		}

		for (int i = 0; i < excludeChars.length; i++) {
			String excludeChar = excludeChars[i];
			if (StringUtil.isBlank(excludeChar))
				continue;
			if (queryString.contains(excludeChar)) {
				return false;
			}
		}

		return true;
	}

	private String getRequestPayload(HttpServletRequest req) {
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		//try {
			String account=req.getParameter("vo.account");
			log.info("account:"+account);
			//reader = req.getReader();
//			String line = " ";
//			while ((line = reader.readLine()) != null) {
//				buffer.append(line);
//			}
		//} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		//}

		return buffer.toString();
	}

	public void destroy() {
	}
}
