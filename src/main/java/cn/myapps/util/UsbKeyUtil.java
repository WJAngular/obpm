package cn.myapps.util;

import java.math.BigInteger;
import java.security.spec.RSAPublicKeySpec;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import cn.myapps.constans.Web;
import cn.myapps.core.sysconfig.ejb.AuthConfig;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.property.PropertyUtil;

/**
 * @author Happy
 *
 */
public class UsbKeyUtil {
	
	private static final Logger log = Logger.getLogger(UsbKeyUtil.class);
	
	public static final String KEY_ALGORITHM = "RSA";  
	public static final String CIPHER_ALGORITHM = "SHA1withRSA";  
	
	
	/**
	 * 获得随机数
	 * @param userId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static String getRandomCode(String userId,HttpServletRequest request) throws Exception {
		String result = "";
		WebUser user = (WebUser) request.getSession().getAttribute(Web.SESSION_ATTRIBUTE_TEMP_FRONT_USER);
		if(user != null){
			if(user.getId().equals(userId.trim().substring(0,user.getId().length()))){
				result = genRandomCode(20);
				request.getSession().setAttribute(Web.UK_AUTH_RANDOM_CODE, result);
			}
		}
		return result;
	}
	
	public static String getRandomCode4Flow(String userId,HttpServletRequest request)  throws Exception {
		String result = "";
		WebUser user = (WebUser) request.getSession().getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
		if(user != null){
			if(user.getId().equals(userId.trim().substring(0,user.getId().length()))){
				result = genRandomCode(20);
			}
		}
		return result;
	}
	
	
	
	/**
	 * 校验数字签名,此方法不会抛出任务异常,成功返回true,失败返回false,要求全部参数不能为空
	 * @param pubKeyText
	 * 		公钥的模
	 * @param plainText
	 * 		原文
	 * @param signedText
	 * 		签名摘要的模
	 * @return
	 * @throws Exception
	 */
	public static boolean verify(String pubKeyText,String plainText,String signedText) throws Exception {
		try {
			byte[] pubKey = Security.hexStringToByte("00"+Security.decodeBASE64(pubKeyText));//公钥字节数组
			byte[] signedBytes = Security.hexStringToByte(signedText);//数字签名的密文节数组
			return verifySign(pubKey, plainText, signedBytes, KEY_ALGORITHM, CIPHER_ALGORITHM);
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	/**
	 * 校验数字签名,此方法不会抛出任务异常,成功返回true,失败返回false,要求全部参数不能为空
	 * @param pubKey
	 * 		公钥
	 * @param plainText
	 * 		明文
	 * @param signedBytes
	 * 		签名后的摘要
	 * @param keyAlgorithm
	 * 		加密算法
	 * @param cipherAlgorithm
	 * 		签名算法
	 * @return
	 */
	public static boolean verifySign(byte[] pubKey, String plainText, byte[] signedBytes,String keyAlgorithm,String cipherAlgorithm) throws Exception {
		try {
			
	        byte[] e = {0x0, 0x1, 0x0, 0x1};//公钥指数
	        
			java.security.KeyFactory keyFactory = java.security.KeyFactory.getInstance(keyAlgorithm);
			//构造RSAPublicKeySpec对象
			 RSAPublicKeySpec pubkf = new RSAPublicKeySpec(new BigInteger(pubKey),new BigInteger(e));
			// 取公钥匙对象
			java.security.PublicKey publicKey = keyFactory.generatePublic(pubkf);
			
			java.security.Signature signatureChecker = java.security.Signature.getInstance(cipherAlgorithm);
			signatureChecker.initVerify(publicKey);
			signatureChecker.update(plainText.getBytes());
			// 验证签名是否正常
			if (signatureChecker.verify(signedBytes)) return true;
		} catch (Exception e) {
			log.error("校验签名发生异常："+e.getMessage());
			throw e;
		}
		return false;
	}
	
	public static String verifyUK(String userId,String plainText,String signedText,HttpServletRequest request) throws Exception {
		try {
			WebUser user = (WebUser) request.getSession().getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
			if(user.getId().equals(userId.substring(0, user.getId().length()))){
				return verify(user.getPublicKey(), plainText, signedText)? "true" : "false";
			}
		} catch (Exception e) {
			throw e;
		}
		return "false";
	}
	
	/**
	 * 生成给定长度的随机数字符串
	 * @param length
	 * 		随机数长度
	 * @return
	 */
	private static String genRandomCode(int length){
		String r = "0123456789abcdefghijklmnopqrstuvwxyz";
		StringBuffer result = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			result.append(r.charAt(random.nextInt(34)));
		} 
		return result.toString();
	}
	
	/**	
	 * 把USBKey公钥的模储存到用户
	 * @param userId
	 * @param pubKey
	 * @return
	 * @throws Exception
	 */
	public int storePublicKey(String userId,String pubKey) throws Exception {
		int result = -1;
		UserProcess up = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
		UserVO user = (UserVO)up.doView(userId);
		if(user != null){
			user.setPublicKey(Security.encodeToBASE64(pubKey));
			up.doUpdate(user);
			result = 0;
		}
		
		return result;
	}
	
	public String toActiveXHtmlText() throws Exception {
		StringBuffer html = new StringBuffer();
		PropertyUtil.reload("sso");
		String usbkeyAuthenticate = PropertyUtil.get(AuthConfig.UK_AUTHENTICATE);
		if("true".equals(usbkeyAuthenticate)){
			html.append("<div style=\"display:none;\">")
				.append("<OBJECT id=\"GDInitCtrl\" classid=\"clsid:0F7C23A0-233A-4D9E-915B-E7EA2E0C873D\" height=\"0\" width=\"0\" VIEWASTEXT></OBJECT>")
				.append("<OBJECT id=\"GDHidCtrl\" classid=\"clsid:220ED87A-CB03-45A8-A81E-1C5597E11186\" height=\"0\" width=\"0\" VIEWASTEXT></OBJECT>")
				.append("</div>");
		
		}
		return html.toString();
	}
	
	public boolean isNtkoUsbKeyEnable(){
		PropertyUtil.reload("usbkey");
		String usbkeyAuthenticate = PropertyUtil.get("usbkey.enable");
		if("true".equals(usbkeyAuthenticate)){
			return true;
		}
		return false;
	}

}
