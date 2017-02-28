package cn.myapps.km.baike.user.action;
import java.math.BigDecimal;
import cn.myapps.km.baike.user.ejb.BUser;
import cn.myapps.km.baike.user.ejb.BUserAttribute;
import cn.myapps.km.baike.user.ejb.BUserAttributeProcessBean;
import cn.myapps.km.base.action.AbstractRunTimeAction;
import cn.myapps.km.base.dao.DataPackage;
import cn.myapps.km.base.ejb.NRunTimeProcess;

/**
 * 
 * @author dragon
 * 用户表的基本操作
 *
 */
public class BUserAttributeAction extends AbstractRunTimeAction<BUserAttribute>{
	
	private static final long serialVersionUID = -2014700496839634066L;
	
	private BUserAttribute bUserAttribute;
	
	/**
	 * 页数
	 */
	private int page;
	
	/**
	 * 行数
	 */
	private int lines;

	/**
	 * 用户创建词条数
	 */
	private long countcreateEntry;
	
	/**
	 * 用户创建词条内容数
	 */
	private long countcreateEntryContent;
	
	/**
	 * 通过词条内容
	 */
	private long countpassedEntryContent;
	
	/**
	 * 用户
	 */
	private BUser buser;
	
	/**
	 * 用户ID
	 */
	private String userId;
	
	/**
	 * 积分
	 */
	private String integral;

	/**
	 * 通过率
	 */
	private double throughputRate;
	
	/**
	 * Entry集合
	 */
	private DataPackage<BUserAttribute> userAttribute;
	
	public DataPackage<BUserAttribute> getUserAttribute() {
		return userAttribute;
	}

	public void setUserAttribute(DataPackage<BUserAttribute> userAttribute) {
		this.userAttribute = userAttribute;
	}
	
	public long getCountcreateEntryContent() {
		return countcreateEntryContent;
	}

	public void setCountcreateEntryContent(long countcreateEntryContent) {
		this.countcreateEntryContent = countcreateEntryContent;
	}

	public double getThroughputRate() {
		return throughputRate;
	}

	public void setThroughputRate(double throughputRate) {
		this.throughputRate = throughputRate;
	}

	public String getIntegral() {
		return integral;
	}

	public void setIntegral(String integral) {
		this.integral = integral;
	}

	public BUser getBuser() {
		return buser;
	}

	public void setBuser(BUser buser) {
		this.buser = buser;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public long getCountpassedEntryContent() {
		return countpassedEntryContent;
	}

	public void setCountpassedEntryContent(long countpassedEntryContent) {
		this.countpassedEntryContent = countpassedEntryContent;
	}

	public long getCountcreateEntry() {
		return countcreateEntry;
	}

	public void setCountcreateEntry(long countcreateEntry) {
		this.countcreateEntry = countcreateEntry;
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

	public BUserAttribute getbUserAttribute() {
		return bUserAttribute;
	}

	public void setbUserAttribute(BUserAttribute bUserAttribute) {
		this.bUserAttribute = bUserAttribute;
	}
	
	/**
	 * 根据编号查询用户
	 * @return
	 */
	public String doView(){
		try {
			BUserAttribute bkUserAttribute =(BUserAttribute) ((BUserAttributeProcessBean)getProcess()).doView(bUserAttribute.getId());
			this.setContent(bkUserAttribute);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	/**
	 * 根据   用户   编号查询用户
	 * @param bUserId
	 * @return
	 * @throws Exception
	 */
	public String doFindByBUserId() throws Exception{
		//System.out.println("执行:个人中心!根据用户编号查询用户");
		//没有登录,所以先用一个死的.
		if (userId == null || userId.trim().length()<=0) {
			userId=getUser().getId();
		}
		buser=((BUserAttributeProcessBean)getProcess()).findBUserById(userId);
	
		//查询用户创建词条数
		countcreateEntry=((BUserAttributeProcessBean)getProcess()).doCountCreateEntry(userId);
		
		//查询用户创建词条数
		countcreateEntryContent=((BUserAttributeProcessBean)getProcess()).doCountCreateEntryContent(userId);
		
		//查询用户创建的通过的词条版本数
		countpassedEntryContent=((BUserAttributeProcessBean)getProcess()).doCountPassedEntry(userId);
		
		if(countcreateEntryContent==0){
			throughputRate=0;
		}else{
			double d=countpassedEntryContent*100.00/countcreateEntryContent;
			BigDecimal bd = new BigDecimal(d);
			
			throughputRate=bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			
		}
		BUserAttribute user=((BUserAttributeProcessBean)getProcess()).findByUserId(userId);
		this.setContent(user);
		return SUCCESS;
	}
	

	/**
	 * 增加用户
	 * @return
	 */
	public String doSave(){
		try {
			((BUserAttributeProcessBean)getProcess()).doCreate(bUserAttribute);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	
	/**
	 * 删除用户action层实现
	 * @return
	 */
	public String doRemove(){
		try {
			((BUserAttributeProcessBean)getProcess()).doRemove(bUserAttribute.getId());
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	
	/**
	 * 修改用户action层实现
	 * @return
	 */
	public String doUpdate(){
		try {
			((BUserAttributeProcessBean)getProcess()).doUpdate(bUserAttribute);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	
	
	@Override
	public NRunTimeProcess<BUserAttribute> getProcess() {
		return new BUserAttributeProcessBean();
	}
	
	/**
	 * 增加用户积分
	 * @param pk
	 * 			用户ID
	 * @param point
	 * 			增加积分
	 * @throws Expception
	 */
	public void addPoint() throws Exception{
		((BUserAttributeProcessBean)getProcess()).addPoint(this.getUser(), bUserAttribute.getIntegral());
	}
	
	
}
