//Source file: C:\\Java\\workspace\\MyApps\\src\\cn\\myapps\\core\\department\\ejb\\DepartmentVO.java

package cn.myapps.core.department.ejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.user.ejb.UserDepartmentSet;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.HtmlEncoder;
import cn.myapps.util.ProcessFactory;

/**
 * 本类用于表示组织架构中具有上下级关系的属性 包含部门、职位、区域、城市，他们统属于一个根节点
 * 
 * @hibernate.class table="T_DEPARTMENT" batch-size="10" lazy="false"
 */
public class DepartmentVO extends ValueObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5767170740302380341L;

	/**
	 * 主键
	 * 
	 * @uml.property name="id"
	 */
	private String id;

	/**
	 * 部门名称
	 * 
	 * @uml.property name="name"
	 */
	private String name;

	/**
	 * @uml.property name="users"
	 */
	private Collection<UserVO> users;

	private DepartmentVO superior;

	/**
	 * @uml.property name="code"
	 */
	private String code;

	/**
	 * @uml.property name="level"
	 */
	private int level;
	
	/**
	 * 索引代码 (组成规则 上级indexCode+‘_’+自身Id，顶级部门的indexCode = 自身id)
	 */
	private String indexCode;

	private DomainVO domain;

	private Collection<UserDepartmentSet> userDepartmentSets;
	
	private int valid = 1;
	
	/**
	 * 部门排序号
	 */
	private int orderByNo;
	
	/**
	 * 企业微信部门id
	 */
	private String weixinDeptId;
	
	
	private String field1;//扩展字段
	private String field2;//扩展字段
	private String field3;//扩展字段
	private String field4;//扩展字段
	private String field5;//扩展字段
	private String field6;//扩展字段
	private String field7;//扩展字段
	private String field8;//扩展字段
	private String field9;//扩展字段
	private String field10;//扩展字段
	private List<String> fieldExtendsValues = new ArrayList<String>();//要显示在列表的扩展字段值的集合,这个值不映射到Hibernate
	
	
	public Collection<UserDepartmentSet> getUserDepartmentSets() {
		if (userDepartmentSets == null) {
			userDepartmentSets = new HashSet<UserDepartmentSet>();
		}

		return userDepartmentSets;
	}

	public void setUserDepartmentSets(Collection<UserDepartmentSet> userDepartmentSets) {
		this.userDepartmentSets = userDepartmentSets;
	}

	/**
	 * 获取域
	 * 
	 * @return DomainVO
	 */
	public DomainVO getDomain() {
		return domain;
	}

	/**
	 * 设置域
	 * 
	 * @param domain
	 */
	public void setDomain(DomainVO domain) {
		this.domain = domain;
	}


	/**
	 * 获取部门标识
	 * 
	 * @return java.lang.String
	 * @hibernate.id column="ID" generator-class="assigned"
	 * @roseuid 44C5FCE0027C
	 * @uml.property name="id"
	 */
	public String getId() {
		return id;
	}

	/**
	 * 获取部门名
	 * 
	 * @return java.lang.String
	 * @hibernate.property column="NAME"
	 * @roseuid 44C5FCE002C2
	 * @uml.property name="name"
	 */
	public String getName() {
		//return HtmlEncoder.encode((String)name);
		return name;
	}

	/**
	 * 获取上级部门
	 * 
	 * @return DepartmentVO
	 * @hibernate.many-to-one class="cn.myapps.core.department.ejb.DepartmentVO"
	 *                        column="SUPERIOR"
	 * @roseuid 44C5FCE00395
	 * @uml.property name="superior"
	 */
	public cn.myapps.core.department.ejb.DepartmentVO getSuperior() {
		return superior;
	}

	/**
	 * 获取部门以下的用户
	 * 
	 * @return 用户集合<java.util.Collection>
	 * @hibernate.set name="users" table="T_USER_DEPARTMENT_SET" cascade="none"
	 *                lazy="false" inverse="true"
	 * @hibernate.collection-key column="DEPARTMENTID"
	 * @hibernate.collection-many-to-many class="cn.myapps.core.user.ejb.UserVO"
	 *                                    column="USERID"
	 * @roseuid 44C5FCE10061
	 * @uml.property name="users"
	 */
	public Collection<UserVO> getUsers() {
		try {
			if (isLazyLoad && users == null) {
				UserProcess userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
				users = userProcess.queryByDepartment(this.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return users;
	}

	/**
	 * 设置标识
	 * 
	 * @param aId
	 * @roseuid 44C5FCE00290
	 * @uml.property name="id"
	 */
	public void setId(String aId) {
		id = aId;
	}

	/**
	 * 设置名字
	 * 
	 * @param aName
	 * @roseuid 44C5FCE002D6
	 * @uml.property name="name"
	 */
	public void setName(String aName) {
		name = aName;
	}

	/**
	 * 设置部门上级
	 * 
	 * @param aSuperior
	 * @roseuid 44C5FCE003B3
	 * @uml.property name="superior"
	 */
	public void setSuperior(cn.myapps.core.department.ejb.DepartmentVO aSuperior) {
		superior = aSuperior;
	}

	/**
	 * 设置部门用户
	 * 
	 * @param aUsers
	 * @roseuid 44C5FCE1007F
	 * @uml.property name="users"
	 */
	public void setUsers(Collection<UserVO> aUsers) {
		users = aUsers;
	}

	/**
	 * 获取部门级别
	 * 
	 * @hibernate.property column="LEVELS"
	 * @return
	 * @uml.property name="level"
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * 设置部门级别
	 * 
	 * @param level
	 *            the level to set
	 * @uml.property name="level"
	 */
	public void setLevel(int level) {
		this.level = level;
	}
	
	/**
	 * 获取索引代码
	 * @return
	 */
	public String getIndexCode() {
		return indexCode;
	}

	/**
	 * 设置索引代码
	 * @param depthCode
	 */
	public void setIndexCode(String indexCode) {
		this.indexCode = indexCode;
	}

	/**
	 * 获取部门代码
	 * 
	 * @hibernate.property column="CODE"
	 * @uml.property name="code"
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 设置部门代码
	 * 
	 * @param code
	 *            The code to set.
	 * @uml.property name="code"
	 */
	public void setCode(String code) {
		this.code = code;
	}

	public boolean equals(Object obj) {
		if (obj instanceof DepartmentVO) {
			DepartmentVO vo = (DepartmentVO) obj;
			return this.id.equals(vo.getId());
		}
		return false;
	}

	public int hashCode() {
		if (id != null) {
			return id.hashCode();
		}
		return super.hashCode();

	}

	public int getValid() {
		return valid;
	}

	public void setValid(int valid) {
		this.valid = valid;
	}
	
	public int getOrderByNo() {
		return orderByNo;
	}

	public void setOrderByNo(int orderByNo) {
		this.orderByNo = orderByNo;
	}

	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		if ("".equals(field1))
			field1 = null;
		this.field1 = field1;
	}

	public String getField2() {
		return field2;
	}

	public void setField2(String field2) {
		if ("".equals(field2))
			field2 = null;
		this.field2 = field2;
	}

	public String getField3() {
		return field3;
	}

	public void setField3(String field3) {
		if ("".equals(field3))
			field3 = null;
		this.field3 = field3;
	}

	public String getField4() {
		return field4;
	}

	public void setField4(String field4) {
		if ("".equals(field4))
			field4 = null;
		this.field4 = field4;
	}

	public String getField5() {
		return field5;
	}

	public void setField5(String field5) {
		if ("".equals(field5))
			field5 = null;
		this.field5 = field5;
	}

	public String getField6() {
		return field6;
	}

	public void setField6(String field6) {
		if ("".equals(field6))
			field6 = null;
		this.field6 = field6;
	}

	public String getField7() {
		return field7;
	}

	public void setField7(String field7) {
		if ("".equals(field7))
			field7 = null;
		this.field7 = field7;
	}

	public String getField8() {
		return field8;
	}

	public void setField8(String field8) {
		if ("".equals(field8))
			field8 = null;
		this.field8 = field8;
	}

	public String getField9() {
		return field9;
	}

	public void setField9(String field9) {
		if ("".equals(field9))
			field9 = null;
		this.field9 = field9;
	}

	public String getField10() {
		return field10;
	}

	public void setField10(String field10) {
		if ("".equals(field10))
			field10 = null;
		this.field10 = field10;
	}

	public List<String> getFieldExtendsValues() {
		return fieldExtendsValues;
	}

	public void setFieldExtendsValues(List<String> fieldExtendsValues) {
		this.fieldExtendsValues = fieldExtendsValues;
	}

	public String getWeixinDeptId() {
		return weixinDeptId;
	}

	public void setWeixinDeptId(String weixinDeptId) {
		this.weixinDeptId = weixinDeptId;
	}
	
	
}
