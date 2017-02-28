package cn.myapps.core.fieldextends.dao;

import java.util.List;

import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.fieldextends.ejb.FieldExtendsVO;
import cn.myapps.core.user.ejb.UserVO;

public interface FieldExtendsDAO extends IDesignTimeDAO<FieldExtendsVO> {
	/**
	 * 查询所有已使用的扩展字段
	 * @return 返回所有扩展的字段
	 */
	public List<FieldExtendsVO> queryUserFieldExtends(String domain) throws Exception;
	
	/**
	 * 根据所属模块和字段名，查询出字段对象
	 * @param forTable 模块名
	 * @param name 字段名
	 * @return 返回FieldExtendsVO对像集合
	 * @throws Exception
	 */
	public List<FieldExtendsVO> queryFieldExtendsByForTableAndName(String domain, String forTable,String name) throws Exception;
	
	/**
	 * 根据ID查询FieldExtendsVO对象
	 * @param fid FieldExtendsVO对象的ID
	 * @return
	 * @throws Exception
	 */
	public List<FieldExtendsVO> queryFieldExtendsByFid(String fid) throws Exception;
	
	/**
	 * 查询当前字段是否存在数据
	 * @param forTable 字段所在表
	 * @param fieldName 字段名
	 * @return
	 * @throws Exception
	 */
	public List<UserVO> checkFieldHasData(String domain, String forTable,String fieldName)throws Exception;
	
	/**
	 * 根据ID集合删除字段
	 * @param fids 字段ID集合
	 * @throws Exception
	 */
	public void deleteFieldExtendsByIds(List<String> fids)throws Exception;
	
	/**
	 * 根据模块名查找字段集合
	 * @param forTable 字段所属模块
	 * @throws Exception
	 */
	public List<FieldExtendsVO> queryFieldExtendsByTable(String domain, String forTable)throws Exception;
	
	/**
	 * 根据模块名和字段可见性查找字段集合
	 * @param forTable 字段所属模块
	 * @param enabel 可见性
	 * @return
	 * @throws Exception
	 */
	public List<FieldExtendsVO> queryFieldExtendsByTableAndEnabel(String domain, String forTable,Boolean enabel)throws Exception;
	
	/**
	 * 清空field在相应表中的数据
	 * @param tableName 表名
	 * @param fieldName 字段名
	 * @throws Exception
	 */
	public void cleanFieldData(String domain, String tableName,String fieldName)throws Exception;
	
	public DataPackage<FieldExtendsVO> queryByTypeAndForTable(String domain, String type, String table, int page, int lines) throws Exception;
	
	public DataPackage<FieldExtendsVO> queryUserFieldExtends(String domain, int page, int lines) throws Exception;
	
	public FieldExtendsVO qeuryFieldByLabelAndDomain(String label, String domain, String forTable) throws Exception;
	
}
