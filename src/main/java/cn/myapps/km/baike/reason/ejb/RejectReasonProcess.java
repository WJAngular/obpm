package cn.myapps.km.baike.reason.ejb;


import java.util.Collection;
import cn.myapps.km.base.ejb.NRunTimeProcess;

/**
 * 
 * @author Able
 *	Reason 的process层的接口
 */
public interface RejectReasonProcess extends  NRunTimeProcess<RejectReason>{
	
	/**
	 * 查询所有原因
	 * @param page
	 * @param lines
	 * @return
	 * @throws Exception
	 */
	public Collection<RejectReason> queryAllReason(String contentId) throws Exception;
	

	
}
