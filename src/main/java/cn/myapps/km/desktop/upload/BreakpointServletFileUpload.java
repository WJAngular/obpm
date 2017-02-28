package cn.myapps.km.desktop.upload;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemHeaders;
import org.apache.commons.fileupload.FileItemHeadersSupport;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

/**
 * 改造fileupload组件使之支持断点续传
 * 
 * @author Happy
 * 
 */
public class BreakpointServletFileUpload extends ServletFileUpload {
	
	/**
	 * 是否与客户端断开了连接
	 */
	private boolean disconnect = false;
	
	public boolean isDisconnect() {
		return disconnect;
	}
	
	

	public BreakpointServletFileUpload() {
		super();
	}

	public BreakpointServletFileUpload(FileItemFactory fileItemFactory) {
		super(fileItemFactory);
	}

	@Override
	public List parseRequest(RequestContext ctx) throws FileUploadException {
		List items = null;
		try {
			FileItemIterator iter = getItemIterator(ctx);
			items = new ArrayList();
			FileItemFactory fac = getFileItemFactory();
			if (fac == null)
				throw new NullPointerException(
						"No FileItemFactory has been set.");
			FileItem fileItem;
			for (; iter.hasNext(); items.add(fileItem)) {
				FileItemStream item = iter.next();
				fileItem = fac.createItem(item.getFieldName(), item
						.getContentType(), item.isFormField(), item.getName());
				try {
					Streams.copy(item.openStream(), fileItem.getOutputStream(),
							true);
				} catch (FileUploadIOException e) {
					throw (FileUploadException) e.getCause();
				} catch (IOException e) {
					throw new IOFileUploadException(
							"Processing of multipart/form-data request failed. "
									+ e.getMessage(), e);
				}
				if (fileItem instanceof FileItemHeadersSupport) {
					FileItemHeaders fih = item.getHeaders();
					((FileItemHeadersSupport) fileItem).setHeaders(fih);
				}
			}

		} catch (Exception e) {
			this.disconnect = true;
			e.printStackTrace();
		}

		return items;
	}

}
