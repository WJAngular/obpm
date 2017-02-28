package cn.myapps.rm.base.ejb;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.rm.base.dao.DataPackage;


/**
 * The base process interface.
 */
public interface RunTimeProcess<E> extends java.io.Serializable {
	/**
	 * Create a new value object
	 * 
	 * @param vo
	 *            The value object
	 * @throws Exception
	 */
	public void doCreate(BaseObject vo) throws Exception;

	/**
	 * Update the value object
	 * 
	 * @param vo
	 *            The value object.
	 * @throws Exception
	 */
	public void doUpdate(BaseObject vo) throws Exception;

	/**
	 * Remove the value object
	 * 
	 * @param pk
	 *            The value object's primary key.
	 * @throws Exception
	 */
	public void doRemove(String pk) throws Exception;

	/**
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public BaseObject doView(String id) throws Exception;

	/**
	 * Begin transaction
	 * 
	 * @throws Exception
	 */
	public void beginTransaction() throws Exception;

	/**
	 * Commit the transaction.
	 * 
	 * @throws Exception
	 */
	public void commitTransaction() throws Exception;

	/**
	 * Roll back transaction.
	 * 
	 * @throws Exception
	 */
	public void rollbackTransaction() throws Exception;

	/**
	 * Close the connection
	 * 
	 * @throws Exception
	 */
	public void closeConnection() throws Exception;

	/**
	 * Query the object
	 * 
	 * @param params
	 *            The parameter table
	 * @param user
	 *            The web user.
	 * @return The result data package.
	 * @throws Exception
	 */
	public DataPackage<E> doQuery(ParamsTable params, WebUser user)
			throws Exception;

}
