package cn.myapps.core.dynaform.activity.ejb;

import java.util.Collection;

import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.IDesignTimeProcess;

/**
 * 
 * @author marky
 * 
 */
public interface ActivityProcess extends IDesignTimeProcess<Activity> {

	/**
	 * 从Activity集合中获取最大的顺序号
	 * 
	 * @param activityList
	 *            Activity集合
	 * @return
	 */
	public int getActivityMaxOrderNo(Collection<Activity> activityList);

	/**
	 * 更改Activity顺序号
	 * 
	 * @param id
	 *            要更改顺序的Activity的id
	 * @param parent
	 *            父元素(Form|View)
	 * @param flag
	 *            标记(前移|后移)
	 */
	public void changeOrder(String id, ActivityParent parent, String flag);

	public void doRemove(ActivityParent parent, String[] pks) throws Exception;

	public void doUpdate(ActivityParent parent, ValueObject vo) throws Exception;
}
