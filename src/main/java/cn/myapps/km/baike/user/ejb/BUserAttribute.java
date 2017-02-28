package cn.myapps.km.baike.user.ejb;
import cn.myapps.km.base.ejb.NObject;
/**
 * 
 * @author dragon
 * 用户基本信息实体类
 * 继承平台用户
 *
 */
public class BUserAttribute extends NObject{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3531756947552708897L;
	
	//用户ID
	private String userId;
	
	//创建词条数量
	private long createNum;
	
	//积分
	private int integral = 0;
	
	//通过率
	private int throughputRate = 0;
	
	private String field1;
	
	private String field2;
	
	private String field3;
	
	private String field4;
	
	private String field5;
	
	
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public int getThroughputRate() {
		return throughputRate;
	}

	public void setThroughputRate(int throughputRate) {
		this.throughputRate = throughputRate;
	}
	
	
	/**
	 * 获取用户创建词条数量
	 * @return
	 */
	public long getCreateNum() {
		try {
			return new BUserAttributeProcessBean().doCountCreateEntry(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void setCreateNum(long createNum) {
		this.createNum = createNum;
	}

	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	public String getField2() {
		return field2;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}

	public String getField3() {
		return field3;
	}

	public void setField3(String field3) {
		this.field3 = field3;
	}

	public String getField4() {
		return field4;
	}

	public void setField4(String field4) {
		this.field4 = field4;
	}

	public String getField5() {
		return field5;
	}

	public void setField5(String field5) {
		this.field5 = field5;
	}	

	public BUserAttribute(){}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null){
			return false;
		}else if(obj instanceof BUserAttribute){
			BUserAttribute bUserAttribute=(BUserAttribute)obj;
			return (this.getId().equals(bUserAttribute.getId()))
					&&this.getIntegral()==bUserAttribute.getIntegral()
					&&this.getThroughputRate()==bUserAttribute.getThroughputRate()
					&&this.getUserId()==null&&bUserAttribute.getUserId()==null?true:this.getUserId().equals(bUserAttribute.getUserId())
					&&this.getField1()==null&&bUserAttribute.getField1()==null?true:this.getField1().equals(bUserAttribute.getField1())
					&&this.getField2()==null&&bUserAttribute.getField2()==null?true:this.getField2().equals(bUserAttribute.getField2())
					&&this.getField3()==null&&bUserAttribute.getField3()==null?true:this.getField3().equals(bUserAttribute.getField3())
					&&this.getField4()==null&&bUserAttribute.getField4()==null?true:this.getField4().equals(bUserAttribute.getField4())
					&&this.getField5()==null&&bUserAttribute.getField5()==null?true:this.getField5().equals(bUserAttribute.getField5());
			
		}
		return false;
	}
}
