package cn.myapps.core.fieldextends.ejb;

import java.util.List;

import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.ejb.IDesignTimeProcess;

/**
 * 
 * 用户操作接口
 * 
 */
public interface FieldExtendsProcess extends IDesignTimeProcess<FieldExtendsVO> {
	/**
	 * 查询所有属于用户表的扩展字段
	 * @return
	 */
	public List<FieldExtendsVO> queryUserFieldExtends(String domain) throws Exception;
	
	/**
	 * 获取所有扩展字段名
	 * @return
	 * @throws Exception
	 */
	public List<String> queryFieldNames() throws Exception;
	
	/**
	 * 根据所属模块和字段名，查询出字段对象
	 * @param forTable 模块名
	 * @param name 字段名
	 * @return 返回TURE表示字段已被使用，FALSE表示未被使用。
	 * @throws Exception
	 */
	public boolean queryFieldExtendsByForTableAndName(String domain, String forTable,String name) throws Exception;
	
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
	 * @return 返回TURE表示字段已有数据，FALSE表示未有数据
	 * @throws Exception
	 */
	public boolean checkFieldHasData(String domain, String forTable,String fieldName)throws Exception;
	
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

	public DataPackage<FieldExtendsVO> queryByTypeAndForTable(String domain, String type, String table,
			int page, int lines) throws Exception;
	
	public DataPackage<FieldExtendsVO> queryUserFieldExtends(String domain, int page, int lines)
		throws Exception;
	
	public FieldExtendsVO qeuryFieldByLabelAndDomain(String label, String domain, String forTable) throws Exception;
	
}
