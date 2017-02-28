package cn.myapps.km.baike.reason.action;
import java.util.Collection;
import cn.myapps.km.baike.reason.ejb.RejectReason;
import cn.myapps.km.baike.reason.ejb.RejectReasonProcess;
import cn.myapps.km.baike.reason.ejb.RejectReasonProcessBean;
import cn.myapps.km.base.action.AbstractRunTimeAction;
import cn.myapps.km.base.ejb.NRunTimeProcess;

/**
 * 
 * @author Able 驳回原因
 * 
 */
public class RejectReasonAction extends AbstractRunTimeAction<RejectReason> {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7450590083936357417L;
	/**
	 * 页码
	 */
	private int page;
	/**
	 * 页数
	 */
	private int lines;
	
	/**
	 * 词条内容id
	 */
	private String contentId;

	/**
	 * 驳回所有原因
	 */
	private Collection<RejectReason> reasons;
	
	/**
	 * 驳回原因
	 * @return
	 */
	private RejectReason reason;
	
	public Collection<RejectReason> getReasons() {
		return reasons;
	}

	public void setReasons(Collection<RejectReason> reasons) {
		this.reasons = reasons;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getLines() {
		return lines;
	}

	public void setLines(int lines) {
		this.lines = lines;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public RejectReason getReason() {
		return reason;
	}

	public void setReason(RejectReason reason) {
		this.reason = reason;
	}

	@Override
	public NRunTimeProcess<RejectReason> getProcess() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 创建驳回原因
	 * @return
	 */
	public String doSave(){
		try {
			((RejectReasonProcess)getProcess()).doCreate(reason);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * 查询所有的驳回原因
	 * @return
	 * @throws Exception
	 */
	public String doQueryAllReason() throws Exception{
		try {
			reasons = new RejectReasonProcessBean().queryAllReason(contentId);
			return SUCCESS;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ERROR;
	}
}
