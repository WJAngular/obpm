package cn.myapps.km.disk.ejb;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;

import cn.myapps.km.util.FileUtils;


/**
 * 文件
 * @author Jarod
 *
 */
public class NFile extends IFile{
 
	/**
	 * 
	 */
	private static final long serialVersionUID = -2826297213857786020L;
	
	
	/**
	 * doc文档类型
	 */
	public static final String TYPE_DOC = "doc";
	/**
	 * docx文档类型
	 */
	public static final String TYPE_DOCX = "docx";
	public static final String TYPE_RTF = "rtf";
	public static final String TYPE_XLS = "xls";
	public static final String TYPE_XLSX = "xlsx";
	public static final String TYPE_ET = "et";
	public static final String TYPE_PPT = "ppt";
	public static final String TYPE_DPS = "dps";
	public static final String TYPE_POT = "pot";
	public static final String TYPE_PPS = "pps";
	public static final String TYPE_PPTX = "pptx";
	public static final String TYPE_WPS = "wps";
	public static final String TYPE_HTML = "html";
	public static final String TYPE_HTM = "htm";
	public static final String TYPE_JPG = "jpg";
	public static final String TYPE_JPEG = "jpeg";
	public static final String TYPE_PNG = "png";
	public static final String TYPE_GIF = "gif";
	/**
	 * pdf文档类型
	 */
	public static final String TYPE_PDF = "pdf";
	/**
	 * txt文档类型
	 */
	public static final String TYPE_TXT = "txt";
	/**
	 * swf文档类型
	 */
	public static final String TYPE_SWF = "swf";
	
	public static final int STATE_PUBLIC = 1;
	public static final int STATE_PRIVATE = 0;
	
	private String name;
	 
	private String ownerId;
	 
	/**
	 * 版本号
	 */
	private int version;
	 
	/**
	 * 文件类型
	 */
	private String type;
	
	/**
	 * 所属目录ID
	 */
	private String nDirId;
	 
	/**
	 * 便笺
	 */
	private String memo;
	 
	/**
	 * 物理位置
	 */
	private String url;
	
	/**
	 * 状态（私有、公开）
	 */
	private int state;
	
	/**
	 * 最后修改时间
	 */
	private Date lastmodify;
	 
	/**
	 * 创建者ID
	 */
	private String creatorId;
	
	/**
	 * 创建者部门（默认部门）
	 */
	private String department;
	
	/**
	 * 创建者部门id
	 */
	private String departmentId;
	
	/**
	 * 分享源文件id
	 */
	private String shareId;
	 
	/**
	 * 所属目录
	 */
	private NDir nDir;
	
	/**
	 * 标题
	 */
	private String title;
	
	/**
	 * 内容
	 */
	private String content;
	
	/**
	 * 分类
	 */
	private String classification;
	
	private String rootCategoryId;
	
	private String subCategoryId;
	
	/**
	 * 浏览次数
	 */
	private int views;
	
	/**
	 * 下载次数
	 */
	private int downloads;
	
	/**
	 * 收藏次数
	 */
	private int favorites;
	
	private int good;
	
	private int bad;
	
	public int getGood() {
		return good;
	}

	public void setGood(int good) {
		this.good = good;
	}

	public int getBad() {
		return bad;
	}

	public void setBad(int bad) {
		this.bad = bad;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	private Collection<Term> term;

	 
	public String getNDirId() {
		return nDirId;
	}

	public void setNDirId(String nDirId) {
		this.nDirId = nDirId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getLastmodify() {
		return lastmodify;
	}

	public void setLastmodify(Date lastmodify) {
		this.lastmodify = lastmodify;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	
	public String getShareId() {
		return shareId;
	}

	public void setShareId(String shareId) {
		this.shareId = shareId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public NDir getnDir() {
		return nDir;
	}

	public void setnDir(NDir nDir) {
		this.nDir = nDir;
	}

	public Collection<Term> getTerm() {
		return term;
	}

	public void setTerm(Collection<Term> term) {
		this.term = term;
	}
	
	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getRootCategoryId() {
		return rootCategoryId;
	}

	public void setRootCategoryId(String rootCategoryId) {
		this.rootCategoryId = rootCategoryId;
	}

	public String getSubCategoryId() {
		return subCategoryId;
	}

	public void setSubCategoryId(String subCategoryId) {
		this.subCategoryId = subCategoryId;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public int getDownloads() {
		return downloads;
	}

	public void setDownloads(int downloads) {
		this.downloads = downloads;
	}

	public int getFavorites() {
		return favorites;
	}

	public void setFavorites(int favorites) {
		this.favorites = favorites;
	}
	
	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	/**
	 * 生成下载链接
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String toDownloadURL() throws Exception {
		StringBuffer uri = new StringBuffer();
		try {
			uri.append(File.separator).append(FileUtils.uploadFolder);
			uri.append(getUrl());
		} catch (Exception e) {
		}
		
		return uri.toString();
	}
	
	public String toSwfURL() {
		StringBuffer uri = new StringBuffer();
		try {
			uri.append(File.separator).append(FileUtils.uploadFolder);
			
			int pos = getUrl().lastIndexOf(getId()+".");
			
			if (pos<=0) {
				pos = getUrl().lastIndexOf(getShareId());
			}
			
			uri.append(getUrl().substring(0, pos));
			
			uri.append(FileUtils.SWF_PATH);
			uri.append(File.separator);
			if(ORIGN_FAVORITE==this.getOrigin() || ORIGN_SHARE==this.getOrigin()){
				uri.append(getShareId());
			}else {
				uri.append(getId());
			}
			uri.append(".").append(NFile.TYPE_SWF);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uri.toString();
	}
	
	public String toPdfURL() {
		StringBuffer uri = new StringBuffer();
		try {
			uri.append(File.separator).append(FileUtils.uploadFolder);
			
			String fileUrl = getUrl();
			
			//如果文件本身为PDF文件，则转换时不会生成中间PDF文件
			if (NFile.TYPE_PDF.equalsIgnoreCase(getType())) {
				uri.append(fileUrl);
				return uri.toString();
			}
			int pos = fileUrl.lastIndexOf(File.separator);
			uri.append(fileUrl.substring(0,pos+1));
			uri.append("swf");
			uri.append(File.separator);
			uri.append(fileUrl.substring(pos+1, fileUrl.lastIndexOf(".")));
			uri.append(".").append(NFile.TYPE_PDF);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uri.toString();
	}
	
	public double getScore(){
		double score = 3;
		DecimalFormat format = new DecimalFormat("#.#");
		try {
			//double s = 5*(1*good-0.2*bad)/(good+bad);
			if(good+bad>0){
				double s = 5.0*good/(good+bad);
				score = Double.parseDouble(format.format(s));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return score;
	}
	
}
 
