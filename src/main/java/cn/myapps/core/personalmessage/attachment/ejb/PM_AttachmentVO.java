package cn.myapps.core.personalmessage.attachment.ejb;

import java.util.Date;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.email.util.Utility;
import cn.myapps.core.personalmessage.attachment.action.AttachmentUtil;


public class PM_AttachmentVO extends ValueObject {

	private static final long serialVersionUID = 1L;

	private String id;

	private String fileName;

	private String realFileName;

	private byte[] fileText;

	private String path;

	private Date createDate;
	
	private long size;
	
	private Object content;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}


	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the realFileName
	 */
	public String getRealFileName() {
		return realFileName;
	}

	/**
	 * @param realFileName
	 *            the realFileName to set
	 */
	public void setRealFileName(String realFileName) {
		this.realFileName = realFileName;
	}

	/**
	 * @return the fileText
	 */
	public byte[] getFileText() {
		return fileText;
	}

	/**
	 * @param fileText
	 *            the fileText to set
	 */
	public void setFileText(byte[] fileText) {
		this.fileText = fileText;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate
	 *            the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public String getFileAllPath() {
		try {
			return AttachmentUtil.getAttachmentDir() + "/" + getFileName();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * @return the size
	 */
	public long getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(long size) {
		this.size = size;
	}
	
	public String getSizeString() {
		return Utility.sizeToHumanReadable(size);
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}


}
