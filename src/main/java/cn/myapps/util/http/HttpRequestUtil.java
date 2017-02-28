package cn.myapps.util.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;


import net.sf.json.JSONObject;

/**
 * HTTP 通讯工具类
 * @author Happy
 *
 */
public class HttpRequestUtil {
	
	/**
	 * 发起https Get请求并获取结果
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param outputStr
	 *            提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject get(String requestUrl,String outputStr) throws Exception {
		return request(requestUrl, "GET", outputStr);
	}
	
	/**
	 * 发起https Post请求并获取结果
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param outputStr
	 *            提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject post(String requestUrl,String outputStr) throws Exception {
		return request(requestUrl, "POST", outputStr);
	}
	
	/**
	 * 发起https请求并获取结果
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * @param outputStr
	 *            提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	private static JSONObject request(String requestUrl,
			String requestMethod, String outputStr)  throws Exception{
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url
					.openConnection();
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			//httpUrlConn.setRequestProperty("Accept-Charset", "UTF-8");
			httpUrlConn.setRequestProperty("contentType", "UTF-8");
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);
			if ("GET".equalsIgnoreCase(requestMethod)){
				httpUrlConn.connect();
			}else{
				httpUrlConn.setRequestProperty("content-type", "text/html"); 
			}

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (ConnectException ce) {
			throw new Exception("server connection timed out.");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("http request error:"+e.getMessage());
		}
		return jsonObject;
	}
	
	/**
	 * 获取http post 发送的数据，返回字符串格式数据
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static String getHttpPostDataAsString(HttpServletRequest request)  throws Exception{
		StringBuffer buffer = new StringBuffer();
		try {
			InputStream inputStream = request.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("解析http post 发送的数据时发送异常："+e.getMessage(),e);
		}
		return buffer.toString();
	}
	
}
