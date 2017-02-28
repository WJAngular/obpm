package cn.myapps.km.base.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Data package, usually use in the list.
 */
public class DataPackage<E> implements Serializable {
	private static final long serialVersionUID = -3847321538691386074L;
	/**
	 * 总行数
	 * 
	 * @uml.property name="rowCount"
	 */
	public int rowCount;
	/**
	 * 每页显示的行数
	 * 
	 * @uml.property name="linesPerPage"
	 */
	public int linesPerPage;
	/**
	 * 页码
	 * 
	 * @uml.property name="pageNo"
	 */
	public int pageNo;
	/**
	 * 当前页数据集
	 * 
	 * @uml.property name="datas"
	 */
	public Collection<E> datas;
	
	/**
	 * 当前页数据集的序号。
	 * Map中的“key”为datas数据对象<E>的hashCode,“value”为该数据对象<E>的序号
	 * @uml.property name="rowNumMap"
	 */
	public Map<Integer,Integer> rowNumMap = new HashMap<Integer,Integer>();

	/**
	 * Gets 当前页数据集
	 * 
	 * @return Returns the datas.
	 * @uml.property name="datas"
	 */
	public Collection<E> getDatas() {
		if (datas == null) {
			datas = new ArrayList<E>();
		}

		return datas;
	}

	/**
	 * Set当前页数据集
	 * 
	 * @param datas
	 *            The datas to set.
	 * @uml.property name="datas"
	 */
	public void setDatas(Collection<E> datas) {
		this.datas = datas;
	}

	/**
	 * Gets 每页显示的行数
	 * 
	 * @return Returns the linesPerPage.
	 * @uml.property name="linesPerPage"
	 */
	public int getLinesPerPage() {
		return linesPerPage;
	}

	/**
	 * Set 每页显示的行数
	 * 
	 * @param linesPerPage
	 *            The linesPerPage to set.
	 * @uml.property name="linesPerPage"
	 */
	public void setLinesPerPage(int linesPerPage) {
		this.linesPerPage = linesPerPage;
	}

	/**
	 * Gets 页码
	 * 
	 * @return Returns the pageNo.
	 * @uml.property name="pageNo"
	 */
	public int getPageNo() {
		return pageNo;
	}

	/**
	 * Set 页码
	 * 
	 * @param pageNo
	 *            The pageNo to set.
	 * @uml.property name="pageNo"
	 */
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	/**
	 * Gets 总行数
	 * 
	 * @return Returns the rowCount.
	 * @uml.property name="rowCount"
	 */
	public int getRowCount() {
		return rowCount;
	}

	/**
	 * Set 总行数
	 * 
	 * @param rowCount
	 *            The rowCount to set.
	 * @uml.property name="rowCount"
	 */
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	/**
	 * Gets 总页数
	 * 
	 * 
	 * @return The total page number.
	 */
	public int getPageCount() {
		return (int) Math.ceil((double) rowCount / (double) linesPerPage);
	}
	

	/**
	 * Get 序号
	 * @return rowNumMap
	 */
	public Map<Integer, Integer> getRowNumMap() {
		if(!rowNumMap.isEmpty())
			return rowNumMap;
		initRowNumMap();
		return rowNumMap;
	}

	/**
	 * Set 序号
	 * @param
	 */
	public void setRowNumMap(Map<Integer, Integer> rowNumMap) {
		this.rowNumMap = rowNumMap;
	}

	/**
	 * Clear 序号
	 */
	public void clearRowNumMap(){
		rowNumMap = new HashMap<Integer, Integer>();
	}
	
	/**
	 * Init 序号
	 */
	public void initRowNumMap(){
		Map<Integer,Integer> temp = new HashMap<Integer,Integer>();
		int i = (pageNo - 1) * linesPerPage;
		for(Iterator<E> it = datas.iterator();it.hasNext();){
			E e = it.next();
			temp.put(e.hashCode(), ++i);
		}
		rowNumMap.putAll(temp);
	}
}
