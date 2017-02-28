package cn.myapps.constans;

import java.io.Serializable;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.dynaform.form.ejb.WordFieldIsEdit;
import cn.myapps.core.permission.ejb.PermissionProcess;
import cn.myapps.core.permission.ejb.PermissionProcessBean;
import cn.myapps.core.permission.ejb.PermissionVO;
import cn.myapps.core.privilege.operation.ejb.OperationProcess;
import cn.myapps.core.privilege.operation.ejb.OperationVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.property.DefaultProperty;

/**
 * The environment variable.
 */
public class Environment implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7874636871392290110L;

	private String _wcpath;

	private String _contextPath;

	private String encoding = "UTF-8";
	
	private String baseUrl;
	
	private static Object LOCK = new Object();
	public static Map<String,String> PERMISSIONS = new HashMap<String,String>();
	public static Map<String,String> OPERATIONS = new HashMap<String,String>();
	
	
	/**
	 * 网卡mac地址
	 */
	private static String macAddress = null;
	/**
	 * 前台word控件编辑占用
	 */
	public static Map<String,WordFieldIsEdit> wordFieldIsEdit = new ConcurrentHashMap<String, WordFieldIsEdit>();
	
	/**
	 * 正在执行流程提交事务的document
	 */
	public static Map<String,Boolean> documentsOnFlowCommiting = new ConcurrentHashMap<String, Boolean>();
	
	/**
	 * baselib.js公共变量MAP对象
	 */
	public static Map<?,?> GLOBAL_MAP = new HashMap<Object,Object>();
	
	public static String licenseType = "S.标准版";

	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @param encoding
	 *            the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	private static Environment env = null;
	
	public CdnConfig cdnConfig = null;

	public static Environment getInstance() {
		if (env == null) {
			env = new Environment();
		}

		return env;
	}

	public Environment() {
		super();
		this.cdnConfig = new CdnConfig();
	}

	/**
	 * @param wcpath
	 *            The web context path.
	 */
	public void setApplicationRealPath(String wcpath) {
		_wcpath = wcpath;
	}

	/**
	 * Retrieve the web context path.
	 * 
	 * @return The web context path.
	 */
	public String getApplicationRealPath() {
		return (_wcpath != null) ? _wcpath : "";
	}

	/**
	 * Retrieve the web context physicsal path.
	 * 
	 * @param path
	 *            The web path.
	 * @return The web context physicsal path.
	 */
	public String getRealPath(String path) {
		String realpath = (path != null) ? getApplicationRealPath() + path : "";
		realpath = realpath.replaceAll("\\\\", "/");

		return realpath;
	}

	/**
	 * Set request context path;
	 * 
	 * @param cpath
	 * @return
	 */
	public String setContextPath(String cpath) {
		return _contextPath = cpath;
	}

	public String getContextPath() {
		return _contextPath != null ? _contextPath : "/";
	}

	public String getContext(String uri) {
		if (_contextPath.equals("/")) {
			return uri;
		} else {
			return _contextPath + uri;
		}
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	
	public static void cleanPermissionMap(){
		synchronized (LOCK) {
			PERMISSIONS = new HashMap<String,String>();
		}
	}
	
	public static void initPermissionMap(){
		synchronized (LOCK) {
			Map<String,String> temp = new HashMap<String,String>();
			PermissionProcess process;
			try {
				process = new PermissionProcessBean();
				String countHql = "from cn.myapps.core.permission.ejb.PermissionVO where type = '1'";
				int counts = process.doGetTotalLines(countHql);
				int pagelines = 100000;//为防止内存溢出,每次只提取10W条记录
				int pages = counts % pagelines == 0 ? counts / pagelines : counts / pagelines + 1;
				for (int i = 0; i < pages; i++) {
					process = new PermissionProcessBean();
					Collection<PermissionVO> permissions = process.doQueryByHQL(countHql, i+1, pagelines);
					for (Iterator<PermissionVO> iterator = permissions.iterator(); iterator.hasNext();) {
						PermissionVO permissionVO = (PermissionVO) iterator.next();
						temp.put(permissionVO.getRoleId()+"_"+permissionVO.getResId()+"_"+permissionVO.getOperationId()+"_"+permissionVO.getOperationCode(), "true");
					}
					PersistenceUtils.closeSessionAndConnection();
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
				try {
					PersistenceUtils.closeSessionAndConnection();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			PERMISSIONS.putAll(temp);
		}
	}
	
	public static void cleanOperationMap(){
		synchronized (LOCK) {
			OPERATIONS = new HashMap<String,String>();
		}
	}
	
	public static void initOperationMap(){
		synchronized (LOCK) {
			Map<String,String> temp = new HashMap<String,String>();
			OperationProcess process;
			try {
				process = (OperationProcess)ProcessFactory.createProcess(OperationProcess.class);
				
				Collection<OperationVO> operations = process.doSimpleQuery(null);
				for (Iterator<OperationVO> iterator = operations.iterator(); iterator.hasNext();) {
					OperationVO operation = (OperationVO) iterator.next();
					temp.put(String.valueOf(operation.getCode())+String.valueOf(operation.getResType())+operation.getApplicationid(), operation.getId());
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			OPERATIONS.putAll(temp);
		}
	}
	
	public static String getMACAddress() {
		if(macAddress !=null) return macAddress;
		try {
			StringBuffer sb = new StringBuffer();
			byte[] mac = getHardwareAddress(false,false);
			if(mac.length<=1){
				 mac = getHardwareAddress(false,true);
			}
			if(mac.length<=1){
				 mac = getHardwareAddress(true,false);
			}
			if(mac.length<=1){
				 mac = getHardwareAddress(true,true);
			}
			for (int i = 0; i < mac.length; i++) {

				int n = mac[i] & 0xFF << 8 | mac[i++] & 0xFF;
				String s = Integer.toString(n, 35);
				sb.append(s);
			}
			macAddress = "Z"+(sb.toString().toUpperCase());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return macAddress;
	}
	
	private static byte[] getHardwareAddress(boolean includeSoftEther,boolean abs) throws Exception{
		byte[] mac = new byte[]{0};
		Enumeration netInterfaces;
		InetAddress ia = null;
		netInterfaces = NetworkInterface.getNetworkInterfaces();
		 while (netInterfaces.hasMoreElements()) {
	            NetworkInterface ni = (NetworkInterface) netInterfaces
	                    .nextElement();
	            Enumeration nii = ni.getInetAddresses();
	            
	            while (nii.hasMoreElements()) {
	            	ia = (InetAddress) nii.nextElement();
	            	// 获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
	            	NetworkInterface nf = NetworkInterface.getByInetAddress(ia);
	            	if(!includeSoftEther && (nf.getDisplayName().indexOf("VMware")!=-1
	            			||nf.getDisplayName().indexOf("Microsoft")!=-1)){
	            		continue;
	            	}
	            		
	            	if(nf.isVirtual()
	            			||nf.isLoopback()
	            			||nf.isPointToPoint()
	            			) continue;
	            	
	            	byte[] tempMac = nf.getHardwareAddress();
	            	
	            	if(tempMac==null)  continue;
	            	if(abs){
	            		if(new BigInteger(tempMac).abs().compareTo(new BigInteger(mac).abs())>=0){
		            		mac = tempMac;
		            	}
	            	}else{
	            		if(new BigInteger(tempMac).compareTo(new BigInteger(mac))>=0){
		            		mac = tempMac;
		            	}
	            	}
	            	
	            }
	        }
		 
		 return mac;
	}
	
	/**
	 * cdn配置信息
	 * 
	 * @author Happy
	 *
	 */
	public class CdnConfig{
		/**
		 * 是否启用
		 */
		private boolean enable;
		
		/**
		 * 回源HOST
		 */
		private String sourceHost;
		
		/**
		 * 资源版本
		 */
		private String resVersion;
		
		

		public CdnConfig() {
			super();
			enable = "true".equalsIgnoreCase(DefaultProperty.getProperty("cdn.config.enable"));
			sourceHost = DefaultProperty.getProperty("cdn.config.sourceHost");
			resVersion = DefaultProperty.getProperty("cdn.config.resVersion");
		}

		public boolean isEnable() {
			return enable;
		}

		public String getSourceHost() {
			return sourceHost;
		}

		public String getResVersion() {
			return resVersion;
		}
		
	}
}
