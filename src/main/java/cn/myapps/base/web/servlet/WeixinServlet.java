package cn.myapps.base.web.servlet;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.myapps.support.weixin.WeixinServiceProxy;


/**
 * 微信事件回调处理
 * @author Happy
 *
 */
public class WeixinServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2213756886191169294L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
        try {
			WeixinServiceProxy.validWeixinUrl(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		 try {
			WeixinServiceProxy.handelWeixinCallbackEvent(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
