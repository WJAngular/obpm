/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.model;


/** 
 * @ClassName: ClientVO 
 * @Description: 客户模型
 * @author: WUJING 
 * @date :2016-07-08 下午3:18:46 
 *  
 */
public class ClientVO extends ObjectVO {
//  ,[ITEM_客户名称]
//  ,[ITEM_客户性质]
//  ,[ITEM_所属行业]
//  ,[ITEM_联系人]
//  ,[ITEM_联系电话]
//  ,[ITEM_传真]
//  ,[ITEM_E_MAIL]
//  ,[ITEM_联系地址]
//  ,[ITEM_URL网址]
//  ,[ITEM_法人代表]
//  ,[ITEM_经营范围]
//  ,[ITEM_描述]
//  ,[ID]
//  ,[ITEM_客户编号]
//	,[ITEM_合伙伙伴性质]
	
	private String id;
	private String clientName;
	private String clientNature;
	private String clientProfession;
	private String clienter;
	private String clientTelephone;
	private String clientFax;
	private String clientEmail;
	private String clientAddress;
	private String clientUrl;
	private String clientCorporate;
	private String clientScope;
	private String description;
	private String clientNo;
	private String partner;
	public ClientVO() {
		super();
		// TODO 自动生成的构造函数存根
	}
	
	public ClientVO(String id, String clientName, String clientNature,
			String clientProfession, String clienter, String clientTelephone,
			String clientFax, String clientEmail, String clientAddress,
			String clientUrl, String clientCorporate, String clientScope,
			String description, String clientNo, String partner) {
		super();
		this.id = id;
		this.clientName = clientName;
		this.clientNature = clientNature;
		this.clientProfession = clientProfession;
		this.clienter = clienter;
		this.clientTelephone = clientTelephone;
		this.clientFax = clientFax;
		this.clientEmail = clientEmail;
		this.clientAddress = clientAddress;
		this.clientUrl = clientUrl;
		this.clientCorporate = clientCorporate;
		this.clientScope = clientScope;
		this.description = description;
		this.clientNo = clientNo;
		this.partner = partner;
	}

	/** 
	 * @return id 
	 */
	public String getId() {
		return id;
	}
	/** 
	 * @param id 要设置的 id 
	 */
	public void setId(String id) {
		this.id = id;
	}
	/** 
	 * @return clientName 
	 */
	public String getClientName() {
		return clientName;
	}
	/** 
	 * @param clientName 要设置的 clientName 
	 */
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	/** 
	 * @return clientNo 
	 */
	public String getClientNo() {
		return clientNo;
	}
	/** 
	 * @param clientNo 要设置的 clientNo 
	 */
	public void setClientNo(String clientNo) {
		this.clientNo = clientNo;
	}
	/** 
	 * @return clientNature 
	 */
	public String getClientNature() {
		return clientNature;
	}
	/** 
	 * @param clientNature 要设置的 clientNature 
	 */
	public void setClientNature(String clientNature) {
		this.clientNature = clientNature;
	}
	/** 
	 * @return clientProfession 
	 */
	public String getClientProfession() {
		return clientProfession;
	}
	/** 
	 * @param clientProfession 要设置的 clientProfession 
	 */
	public void setClientProfession(String clientProfession) {
		this.clientProfession = clientProfession;
	}
	/** 
	 * @return clienter 
	 */
	public String getClienter() {
		return clienter;
	}
	/** 
	 * @param clienter 要设置的 clienter 
	 */
	public void setClienter(String clienter) {
		this.clienter = clienter;
	}
	/** 
	 * @return clientTelephone 
	 */
	public String getClientTelephone() {
		return clientTelephone;
	}
	/** 
	 * @param clientTelephone 要设置的 clientTelephone 
	 */
	public void setClientTelephone(String clientTelephone) {
		this.clientTelephone = clientTelephone;
	}
	/** 
	 * @return clientFax 
	 */
	public String getClientFax() {
		return clientFax;
	}
	/** 
	 * @param clientFax 要设置的 clientFax 
	 */
	public void setClientFax(String clientFax) {
		this.clientFax = clientFax;
	}
	/** 
	 * @return clientEmail 
	 */
	public String getClientEmail() {
		return clientEmail;
	}
	/** 
	 * @param clientEmail 要设置的 clientEmail 
	 */
	public void setClientEmail(String clientEmail) {
		this.clientEmail = clientEmail;
	}
	/** 
	 * @return clientAddress 
	 */
	public String getClientAddress() {
		return clientAddress;
	}
	/** 
	 * @param clientAddress 要设置的 clientAddress 
	 */
	public void setClientAddress(String clientAddress) {
		this.clientAddress = clientAddress;
	}
	/** 
	 * @return clientUrl 
	 */
	public String getClientUrl() {
		return clientUrl;
	}
	/** 
	 * @param clientUrl 要设置的 clientUrl 
	 */
	public void setClientUrl(String clientUrl) {
		this.clientUrl = clientUrl;
	}
	/** 
	 * @return clientCorporate 
	 */
	public String getClientCorporate() {
		return clientCorporate;
	}
	/** 
	 * @param clientCorporate 要设置的 clientCorporate 
	 */
	public void setClientCorporate(String clientCorporate) {
		this.clientCorporate = clientCorporate;
	}
	/** 
	 * @return clientScope 
	 */
	public String getClientScope() {
		return clientScope;
	}
	/** 
	 * @param clientScope 要设置的 clientScope 
	 */
	public void setClientScope(String clientScope) {
		this.clientScope = clientScope;
	}
	/** 
	 * @return description 
	 */
	public String getDescription() {
		return description;
	}
	/** 
	 * @param description 要设置的 description 
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/** 
	 * @return partner 
	 */
	public String getPartner() {
		return partner;
	}
	/** 
	 * @param partner 要设置的 partner 
	 */
	public void setPartner(String partner) {
		this.partner = partner;
	}

	/** 
	* @Title: toString 
	* @Description: TODO
	* @param: @return 
	* @throws 
	*/
	@Override
	public String toString() {
		return "ClientVO [id=" + id + ", clientName=" + clientName
				+ ", clientNature=" + clientNature + ", clientProfession="
				+ clientProfession + ", clienter=" + clienter
				+ ", clientTelephone=" + clientTelephone + ", clientFax="
				+ clientFax + ", clientEmail=" + clientEmail
				+ ", clientAddress=" + clientAddress + ", clientUrl="
				+ clientUrl + ", clientCorporate=" + clientCorporate
				+ ", clientScope=" + clientScope + ", description="
				+ description + ", clientNo=" + clientNo + ", Partner="
				+ partner + "]";
	}
	
}
