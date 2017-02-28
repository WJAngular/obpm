package cn.myapps.km.baike.user.ejb;

import cn.myapps.km.base.ejb.NObject;

/**
 * 
 * @author dragon
 * 用户收藏词条表的实体类，继承BObject
 * BUserCollection
 *
 */
public class BUserEntrySet extends NObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3806651698495075289L;
	
	/**
	 * 用户与词条关联类型:收藏
	 */
	public static final String  TYPE_FAVORITE = "FAVORITE";//favorite
	
	/**
	 * 用户与词条关联类型:分享
	 */
	public static final String  TYPE_SHARE = "SHARE";
	
	/**
	 * 词条编号
	 */
	private String entryId;
	
	/**
	 * 百科用户ID
	 */
	private String userId;
	
	/**
	 * 类型:收藏或者分享
	 */
	private String type;
	
	public String getEntryId() {
		return entryId;
	}

	public void setEntryId(String entryId) {
		this.entryId = entryId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BUserEntrySet(){
		
	}

	public BUserEntrySet(String entryId, String userId, String type) {
		super();
		this.entryId = entryId;
		this.userId = userId;
		this.type = type;
	}

	
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null){
			return false;
		}else if(obj instanceof BUserEntrySet){
			BUserEntrySet bUserEntrySet=(BUserEntrySet)obj;
			return (this.getId()==null && bUserEntrySet.getId()==null)?true:this.getId().equals(bUserEntrySet.getId())
					&&this.getUserId()==null && bUserEntrySet.getUserId()==null?true:this.getUserId().equals(bUserEntrySet.getUserId())
					&&this.getEntryId().equals("") && bUserEntrySet.getEntryId()==null?true:this.getEntryId().equals(bUserEntrySet.getEntryId())
					&&this.getType()==null && bUserEntrySet.getType()==null?true:this.getType().equals(bUserEntrySet.getType());
		}
		return false;
	}

	@Override
	public String toString() {
		return "BUserEntrySet [entryId=" + entryId + ", userId=" + userId
				+ ", type=" + type + "]";
	}
	
}
