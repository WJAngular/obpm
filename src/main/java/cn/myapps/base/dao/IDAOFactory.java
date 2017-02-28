package cn.myapps.base.dao;


/**
 * The DAO Factory interface.
 */
public interface IDAOFactory {
	/**
	 * Get the Dao
	 * @param className The value object class name.
	 * @return The relate dao object.
	 */
	public IDesignTimeDAO<?> getDAO(String className);
}
