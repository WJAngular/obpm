package cn.myapps.km.base.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import cn.myapps.km.util.BaikeUtils;
import cn.myapps.km.util.PersistenceUtils;


public class KMPersistenceFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {

		try {
			chain.doFilter(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				PersistenceUtils.closeConnection();
				BaikeUtils.closeConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	public void destroy() {
		// TODO Auto-generated method stub

	}
}
