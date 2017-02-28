package cn.myapps.core.email.runtime.mail;

import java.util.List;

import javax.mail.Message;

import cn.myapps.core.email.runtime.model.EmailHeader;


/**
 * 协议
 * @author Tom
 *
 */
public interface Protocol {
	
	/**
	 * 连接并打开给定的文件目录
	 * @param connectType 文件操作类型<br>类型：Folder.READ_ONLY, Folder.READ_WRITE
	 * @return
	 * @throws Exception
	 */
	public ConnectionMetaHandler connect(int connectType) throws Exception;
	
	/**
	 * 连接并打开给定的文件目录
	 * @param connectType 文件操作类型<br>类型：Folder.READ_ONLY, Folder.READ_WRITE
	 * @param debug debug
	 * @return
	 * @throws Exception
	 */
	public ConnectionMetaHandler connect(int connectType, boolean debug) throws Exception;
	
	/**
	 * 关闭连接
	 * @throws Exception
	 */
	public void disconnect() throws Exception;
	
	public ConnectionMetaHandler deleteMessages(int indexs[]) throws Exception;
	
	public Message getMessage(int index) throws Exception;
	
	public Message[] getMessages(int stratIndex, int endIndex) throws Exception;
	
	public void emptyFolder() throws Exception;
	
	public int getTotalMessageCount() throws Exception;
	
	public int getUnreadMessageCount() throws Exception;
	
	public void flagAsDeleted(int[] indexs) throws Exception;
	
	public List<EmailHeader> fetchAllHeaders() throws Exception;
	
	public List<Message> fetchAllHeadersAsMessages() throws Exception;
	
	public List<EmailHeader> fetchHeaders(int[] indexs) throws Exception;
	
	public List<EmailHeader> getHeadersSortList(String sortCriteriaRaw, String sortDirectionRaw) throws Exception;
	
}
