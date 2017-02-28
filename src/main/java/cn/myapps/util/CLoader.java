package cn.myapps.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.crypto.Cipher;

import cn.myapps.constans.Environment;
import cn.myapps.util.property.DefaultProperty;

public class CLoader extends ClassLoader {
	public static final String KEY_STORE = "JKS";

	public static final String X509 = "X.509";

	public static final String alias = "www.teemlink.com";

	public static final String password = "helloworld";

	static CLoader cl;

	static {
		ClassLoader cld = Thread.currentThread().getContextClassLoader();

		try {
			cl = new CLoader(cld);

			FileInputStream fis = loadLicenseFile();
			ZipInputStream zis = new ZipInputStream(fis);

			try {

				ZipEntry zEntry = null;
				do {
					zEntry = zis.getNextEntry();
					if (zEntry != null && zEntry.getName().endsWith(".scz")) {
						cl.loadClass(zEntry.getName());
					}

				} while (zEntry != null);
			} finally {
				zis.close();
				fis.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static public Class init() throws Exception {
		return cl.loadClass("cn.myapps.protection.util.WarpProcessFactory");
	}

	static public Class initSMSSendMode() throws ClassNotFoundException {
		return cl.loadClass("cn.myapps.protection.sms.SMSMode");
	}

	static public Class initReceiveJob() throws ClassNotFoundException {
		return cl.loadClass("cn.myapps.protection.sms.ReceiveJob");
	}
	
	public static Class initWeixinService() throws ClassNotFoundException {
		return cl.loadClass("cn.myapps.protection.weixin.WeixinService");
	}

	private byte[] cerData;

	private CLoader(ClassLoader parent) throws Exception {
		super(parent);
		cerData = readZipEnry("obpm.keystore");
	}

	protected Class<?> findClass(final String name)
			throws ClassNotFoundException {

		try {
			return super.findClass(name);
		} catch (ClassNotFoundException e) {
			if (name != null) {
				try {
					byte[] classData = readZipEnry(name);
					if(classData ==null) return null;
					byte[] decryptData = decryptByPrivateKey(classData,
							new ByteArrayInputStream(cerData));
					return defineClass(decryptData, 0, decryptData.length);

				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			throw new ClassNotFoundException(name);
		}
	}

	public static byte[] decryptByPrivateKey(byte[] data,
			InputStream keyStoreInputStream) throws Exception {
		
		ByteArrayOutputStream rtn = null;	
		try {
			PrivateKey privateKey = getPrivateKey(keyStoreInputStream, alias,
					password);
			Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
	
			rtn = new ByteArrayOutputStream();

			for (int i = 0; i < data.length; i += 128) {
				int len = data.length - i > 128 ? 128 : data.length - i;
				byte[] tmp = cipher.doFinal(data, i, len);
				rtn.write(tmp);
			}

			rtn.flush();
			return rtn.toByteArray();
		} catch (Exception e) {
			throw e;
		}finally {
			if(rtn != null)rtn.close();
		}

	}

	private static PrivateKey getPrivateKey(InputStream keyStoreInputStream,
			String alias, String password) throws Exception {
		PrivateKey key =null;
		try {
		
			KeyStore ks = getKeyStore(keyStoreInputStream, password);
			key = (PrivateKey) ks.getKey(alias, password.toCharArray());
		} catch (Exception e) {
			throw e;
		}
		return key;
	}

	private static KeyStore getKeyStore(InputStream is, String password)
			throws Exception {

		KeyStore ks = KeyStore.getInstance(KEY_STORE);
		ks.load(is, password.toCharArray());
		is.close();
		return ks;
	}

	private static FileInputStream loadLicenseFile()
			throws FileNotFoundException {
		String path = "";
		String copyPath = DefaultProperty.getProperty("license.file");
		if(StringUtil.isBlank(copyPath)){
			copyPath = System.getProperty("user.home")
			+ System.getProperty("file.separator") + "license.zip";
		}
		Environment evt = Environment.getInstance();
		String applicationRealPath = evt.getApplicationRealPath();
		if(!StringUtil.isBlank(applicationRealPath)){
			if(applicationRealPath.endsWith("/") || applicationRealPath.endsWith("\\")){
				path = applicationRealPath+ "WEB-INF/license.zip";
			}else {
				path = applicationRealPath+ "/WEB-INF/license.zip";
			}
			File from = new File(path);
			File copy = new File(copyPath);
			if(from.exists()){
				doCopy(from, copy);
			}
		}
		
		if(!StringUtil.isBlank(copyPath) && StringUtil.isBlank(applicationRealPath)){
			path = copyPath;
		}
			
		FileInputStream fis = null;
		fis = new FileInputStream(path);
		return fis;
	}

	public static byte[] readZipEnry(String entryName) throws Exception {

		try {
			FileInputStream fis = loadLicenseFile();
			ZipInputStream zis = null;

			try {
				zis = new ZipInputStream(fis);
				ZipEntry zEntry = null;
				do {
					zEntry = zis.getNextEntry();
					if (zEntry != null && zEntry.getName().equals(entryName)) {
						ByteArrayOutputStream baos = new ByteArrayOutputStream();

						while (zis.available() > 0) {
							int b = zis.read();
							if (b >= 0)
								baos.write(b);
						}

						byte[] data = baos.toByteArray();

						zis.closeEntry();
						zis.read(data);

						return data;

					}

				} while (zEntry != null);
			} finally {
				zis.close();
				fis.close();
			}
		} catch (Exception e) {
			throw new Exception("没有找到授权文件，系统授权失败" + e.getMessage() + ")");
		}
		return null;
	}
	
	private static void doCopy(File from, File to) {
		FileInputStream in = null;
		FileOutputStream out = null;
		byte[] buffer = new byte[10240];
		try {
			in = new FileInputStream(from);
			out = new FileOutputStream(to);
			int num = 0;
			while ((num = in.read(buffer)) != -1) {
				out.write(buffer, 0, num);
			}
		} catch (FileNotFoundException ex) {
		} catch (IOException e) {
		} finally {
			try {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			} catch (IOException ex) {
			}

		}
	}

}