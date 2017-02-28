package cn.myapps.km.baike.history.action;
import java.util.Date;

import cn.myapps.km.baike.history.ejb.History;
import cn.myapps.km.baike.history.ejb.HistoryProcess;
import cn.myapps.km.baike.history.ejb.HistoryProcessBean;
import cn.myapps.km.baike.user.ejb.BUser;
import cn.myapps.km.base.action.AbstractRunTimeAction;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NRunTimeProcess;

/**
 * 
 * @author Able 驳回原因
 * 
 */
public class HistoryAction extends AbstractRunTimeAction<History> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6276527528940564465L;

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
	private String entryId;


	/**
	 * 驳回原因
	 * @return
	 */
	private History history;
	
	/**
	 *作者 
	 */
	private BUser author;
	
	/**
	 * 浏览记录
	 */
	private Date readTime;
	
	/**
	 * 当前点击菜单
	 */
	private String selectedMenu;
	

	public String getEntryId() {
		return entryId;
	}

	public void setEntryId(String entryId) {
		this.entryId = entryId;
	}

	public History getHistory() {
		return history;
	}

	public void setHistory(History history) {
		this.history = history;
	}

	public BUser getAuthor() {
		return author;
	}

	public void setAuthor(BUser author) {
		this.author = author;
	}

	public Date getReadTime() {
		return readTime;
	}

	public void setReadTime(Date readTime) {
		this.readTime = readTime;
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
	
	public String getSelectedMenu() {
		return selectedMenu;
	}

	public void setSelectedMenu(String selectedMenu) {
		this.selectedMenu = selectedMenu;
	}

	/**
	 * 创建驳回原因
	 * @return
	 */
	public String doSave(){
		try {
			((HistoryProcess)getProcess()).doCreate(history);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * 通过userId查找浏览记录
	 * @return
	 */
	public String doQueryByUserId(){
		try{
			if(page==0 || lines==0){
				//当前页
				page=1;
				//每页显示行数
				lines=8;
			}
		DataPackage<History> historys = new HistoryProcessBean().queryHistoryByUserId(page, lines, getUser().getId());
		this.setDatas(historys);
		return SUCCESS;
		}catch(Exception e){
			e.printStackTrace();
			return ERROR;
		}
	}


	@Override
	public NRunTimeProcess<History> getProcess() {
		process =new HistoryProcessBean();
		return null;
	}
}
