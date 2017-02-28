package cn.myapps.core.personalmessage.attachment.ejb;

import cn.myapps.base.ejb.IDesignTimeProcess;

public interface AttachmentProcess extends IDesignTimeProcess<PM_AttachmentVO> {
	
	public PM_AttachmentVO getAttachment(String id) throws Exception;
}
