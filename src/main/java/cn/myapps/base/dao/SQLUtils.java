package cn.myapps.base.dao;

/**
 * The SQL utility.
 */
public interface SQLUtils {
    /**
     * Create the where statement
     * @param classname The class name
     * @param params The parameters.
     * @return The where statement.
     */
    public String createWhere(String classname, Object params);
    /**
     * Create the order by statement.
     * @param classname The class name.
     * @param params The parameters
     * @return
     */
    public String createOrderBy(String classname, Object params);
    /**
     * Append the conditions.
     * @param sql The sql statement.
     * @param condition The condition.
     * @return
     */
    public String appendCondition(String sql, String condition);
}
