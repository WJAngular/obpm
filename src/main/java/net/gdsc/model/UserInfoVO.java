/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package net.gdsc.model;

/** 
 * @ClassName: UserVO 
 * @Description: TODO 
 * @author: WUJING 
 * @date :2016-08-27 上午9:34:12 
 *  
 */
public class UserInfoVO extends ObjectVO{
	private String name;//    ,[ITEM_姓名]
	private String sex;//    ,[ITEM_性别]
	private String photo;//    ,[ITEM_个人照片]
	private String nameEn;//    ,[ITEM_英文名]
	private String nation;//    ,[ITEM_民族]
	private String birth;//    ,[ITEM_出生日期]
	private String birthPlace;//    ,[ITEM_籍贯]
	private String age;//    ,[ITEM_年龄]
	private String political;//    ,[ITEM_政治面貌]
	private String graduationTime;//    ,[ITEM_毕业时间]
	private String education;//    ,[ITEM_学历]
	private String school;//    ,[ITEM_毕业院校]
	private String profession;//    ,[ITEM_专业]
	private String cert;//    ,[ITEM_身份证]
	private String marriage;//    ,[ITEM_婚姻状况]
	private String blood;//    ,[ITEM_血型]
	private String no;//    ,[ITEM_员工编号]
	private String category;//    ,[ITEM_类别]
	private String jobTitle;//    ,[ITEM_职称]
	private String post;//    ,[ITEM_职务]
	private String wageLevel;//    ,[ITEM_工资级别]
	private String bankName;//    ,[ITEM_开户银行]
	private String wageNo;//    ,[ITEM_工资账号]
	private String entryDate;//    ,[ITEM_入职日期]
	private String positiveDate;//    ,[ITEM_转正日期]
	private String separationDate;//    ,[ITEM_离职日期]
	private String telephoneOne;//    ,[ITEM_手机1]
	private String companyTel;//    ,[ITEM_公司电话]
	private String fax;//    ,[ITEM_传真]
	private String homePhone;//    ,[ITEM_家庭电话]
	private String email;//    ,[ITEM_邮箱]
	private String homeProvince;//    ,[ITEM_家庭省]
	private String homeCity;//    ,[ITEM_家庭市]
	private String homeZone;//    ,[ITEM_家庭区]
	private String homeCode;//    ,[ITEM_家庭地址邮编]
	private String certCode;//    ,[ITEM_身份证地址邮编]
	private String otherDescription;//    ,[ITEM_其他描述]
	private String status;//    ,[ITEM_状态]
	private String userId;//    ,[ITEM_用户ID]
	private String department;//    ,[ITEM_部门]
	private String homeAddress;//    ,[ITEM_家庭详细地址]
	private String certProvince;//    ,[ITEM_身份证省]
	private String certCity;//    ,[ITEM_身份证市]
	private String certZone;//    ,[ITEM_身份证区]
	private String certAddress;//    ,[ITEM_身份证详细地址]
	private String telephoneTwo;//    ,[ITEM_手机2]
	private String marriedCount;//    ,[ITEM_已婚子女]
	
	public UserInfoVO(String name, String sex, String photo, String nameEn,
			String nation, String birth, String birthPlace, String age,
			String political, String graduationTime, String education,
			String school, String profession, String cert, String marriage,
			String blood, String no, String category, String jobTitle,
			String post, String wageLevel, String bankName, String wageNo,
			String entryDate, String positiveDate, String separationDate,
			String telephoneOne, String companyTel, String fax,
			String homePhone, String email, String homeProvince,
			String homeCity, String homeZone, String homeCode, String certCode,
			String otherDescription, String status, String userId,
			String department, String homeAddress, String certProvince,
			String certCity, String certZone, String certAddress,
			String telephoneTwo, String marriedCount) {
		super();
		this.name = name;
		this.sex = sex;
		this.photo = photo;
		this.nameEn = nameEn;
		this.nation = nation;
		this.birth = birth;
		this.birthPlace = birthPlace;
		this.age = age;
		this.political = political;
		this.graduationTime = graduationTime;
		this.education = education;
		this.school = school;
		this.profession = profession;
		this.cert = cert;
		this.marriage = marriage;
		this.blood = blood;
		this.no = no;
		this.category = category;
		this.jobTitle = jobTitle;
		this.post = post;
		this.wageLevel = wageLevel;
		this.bankName = bankName;
		this.wageNo = wageNo;
		this.entryDate = entryDate;
		this.positiveDate = positiveDate;
		this.separationDate = separationDate;
		this.telephoneOne = telephoneOne;
		this.companyTel = companyTel;
		this.fax = fax;
		this.homePhone = homePhone;
		this.email = email;
		this.homeProvince = homeProvince;
		this.homeCity = homeCity;
		this.homeZone = homeZone;
		this.homeCode = homeCode;
		this.certCode = certCode;
		this.otherDescription = otherDescription;
		this.status = status;
		this.userId = userId;
		this.department = department;
		this.homeAddress = homeAddress;
		this.certProvince = certProvince;
		this.certCity = certCity;
		this.certZone = certZone;
		this.certAddress = certAddress;
		this.telephoneTwo = telephoneTwo;
		this.marriedCount = marriedCount;
	}
	public UserInfoVO() {
		super();
		// TODO 自动生成的构造函数存根
	}
	/** 
	 * @return name 
	 */
	public String getName() {
		return name;
	}
	/** 
	 * @param name 要设置的 name 
	 */
	public void setName(String name) {
		this.name = name;
	}
	/** 
	 * @return sex 
	 */
	public String getSex() {
		return sex;
	}
	/** 
	 * @param sex 要设置的 sex 
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}
	/** 
	 * @return photo 
	 */
	public String getPhoto() {
		return photo;
	}
	/** 
	 * @param photo 要设置的 photo 
	 */
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	/** 
	 * @return nameEn 
	 */
	public String getNameEn() {
		return nameEn;
	}
	/** 
	 * @param nameEn 要设置的 nameEn 
	 */
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}
	/** 
	 * @return nation 
	 */
	public String getNation() {
		return nation;
	}
	/** 
	 * @param nation 要设置的 nation 
	 */
	public void setNation(String nation) {
		this.nation = nation;
	}
	/** 
	 * @return birth 
	 */
	public String getBirth() {
		return birth;
	}
	/** 
	 * @param birth 要设置的 birth 
	 */
	public void setBirth(String birth) {
		this.birth = birth;
	}
	/** 
	 * @return birthPlace 
	 */
	public String getBirthPlace() {
		return birthPlace;
	}
	/** 
	 * @param birthPlace 要设置的 birthPlace 
	 */
	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}
	/** 
	 * @return age 
	 */
	public String getAge() {
		return age;
	}
	/** 
	 * @param age 要设置的 age 
	 */
	public void setAge(String age) {
		this.age = age;
	}
	/** 
	 * @return political 
	 */
	public String getPolitical() {
		return political;
	}
	/** 
	 * @param political 要设置的 political 
	 */
	public void setPolitical(String political) {
		this.political = political;
	}
	/** 
	 * @return graduationTime 
	 */
	public String getGraduationTime() {
		return graduationTime;
	}
	/** 
	 * @param graduationTime 要设置的 graduationTime 
	 */
	public void setGraduationTime(String graduationTime) {
		this.graduationTime = graduationTime;
	}
	/** 
	 * @return education 
	 */
	public String getEducation() {
		return education;
	}
	/** 
	 * @param education 要设置的 education 
	 */
	public void setEducation(String education) {
		this.education = education;
	}
	/** 
	 * @return school 
	 */
	public String getSchool() {
		return school;
	}
	/** 
	 * @param school 要设置的 school 
	 */
	public void setSchool(String school) {
		this.school = school;
	}
	/** 
	 * @return profession 
	 */
	public String getProfession() {
		return profession;
	}
	/** 
	 * @param profession 要设置的 profession 
	 */
	public void setProfession(String profession) {
		this.profession = profession;
	}
	/** 
	 * @return cert 
	 */
	public String getCert() {
		return cert;
	}
	/** 
	 * @param cert 要设置的 cert 
	 */
	public void setCert(String cert) {
		this.cert = cert;
	}
	/** 
	 * @return marriage 
	 */
	public String getMarriage() {
		return marriage;
	}
	/** 
	 * @param marriage 要设置的 marriage 
	 */
	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}
	/** 
	 * @return blood 
	 */
	public String getBlood() {
		return blood;
	}
	/** 
	 * @param blood 要设置的 blood 
	 */
	public void setBlood(String blood) {
		this.blood = blood;
	}
	/** 
	 * @return no 
	 */
	public String getNo() {
		return no;
	}
	/** 
	 * @param no 要设置的 no 
	 */
	public void setNo(String no) {
		this.no = no;
	}
	/** 
	 * @return category 
	 */
	public String getCategory() {
		return category;
	}
	/** 
	 * @param category 要设置的 category 
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	/** 
	 * @return jobTitle 
	 */
	public String getJobTitle() {
		return jobTitle;
	}
	/** 
	 * @param jobTitle 要设置的 jobTitle 
	 */
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	/** 
	 * @return post 
	 */
	public String getPost() {
		return post;
	}
	/** 
	 * @param post 要设置的 post 
	 */
	public void setPost(String post) {
		this.post = post;
	}
	/** 
	 * @return wageLevel 
	 */
	public String getWageLevel() {
		return wageLevel;
	}
	/** 
	 * @param wageLevel 要设置的 wageLevel 
	 */
	public void setWageLevel(String wageLevel) {
		this.wageLevel = wageLevel;
	}
	/** 
	 * @return bankName 
	 */
	public String getBankName() {
		return bankName;
	}
	/** 
	 * @param bankName 要设置的 bankName 
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	/** 
	 * @return wageNo 
	 */
	public String getWageNo() {
		return wageNo;
	}
	/** 
	 * @param wageNo 要设置的 wageNo 
	 */
	public void setWageNo(String wageNo) {
		this.wageNo = wageNo;
	}
	/** 
	 * @return entryDate 
	 */
	public String getEntryDate() {
		return entryDate;
	}
	/** 
	 * @param entryDate 要设置的 entryDate 
	 */
	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}
	/** 
	 * @return positiveDate 
	 */
	public String getPositiveDate() {
		return positiveDate;
	}
	/** 
	 * @param positiveDate 要设置的 positiveDate 
	 */
	public void setPositiveDate(String positiveDate) {
		this.positiveDate = positiveDate;
	}
	/** 
	 * @return separationDate 
	 */
	public String getSeparationDate() {
		return separationDate;
	}
	/** 
	 * @param separationDate 要设置的 separationDate 
	 */
	public void setSeparationDate(String separationDate) {
		this.separationDate = separationDate;
	}
	/** 
	 * @return telephoneOne 
	 */
	public String getTelephoneOne() {
		return telephoneOne;
	}
	/** 
	 * @param telephoneOne 要设置的 telephoneOne 
	 */
	public void setTelephoneOne(String telephoneOne) {
		this.telephoneOne = telephoneOne;
	}
	/** 
	 * @return companyTel 
	 */
	public String getCompanyTel() {
		return companyTel;
	}
	/** 
	 * @param companyTel 要设置的 companyTel 
	 */
	public void setCompanyTel(String companyTel) {
		this.companyTel = companyTel;
	}
	/** 
	 * @return fax 
	 */
	public String getFax() {
		return fax;
	}
	/** 
	 * @param fax 要设置的 fax 
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}
	/** 
	 * @return homePhone 
	 */
	public String getHomePhone() {
		return homePhone;
	}
	/** 
	 * @param homePhone 要设置的 homePhone 
	 */
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}
	/** 
	 * @return email 
	 */
	public String getEmail() {
		return email;
	}
	/** 
	 * @param email 要设置的 email 
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/** 
	 * @return homeProvince 
	 */
	public String getHomeProvince() {
		return homeProvince;
	}
	/** 
	 * @param homeProvince 要设置的 homeProvince 
	 */
	public void setHomeProvince(String homeProvince) {
		this.homeProvince = homeProvince;
	}
	/** 
	 * @return homeCity 
	 */
	public String getHomeCity() {
		return homeCity;
	}
	/** 
	 * @param homeCity 要设置的 homeCity 
	 */
	public void setHomeCity(String homeCity) {
		this.homeCity = homeCity;
	}
	/** 
	 * @return homeZone 
	 */
	public String getHomeZone() {
		return homeZone;
	}
	/** 
	 * @param homeZone 要设置的 homeZone 
	 */
	public void setHomeZone(String homeZone) {
		this.homeZone = homeZone;
	}
	/** 
	 * @return homeCode 
	 */
	public String getHomeCode() {
		return homeCode;
	}
	/** 
	 * @param homeCode 要设置的 homeCode 
	 */
	public void setHomeCode(String homeCode) {
		this.homeCode = homeCode;
	}
	/** 
	 * @return certCode 
	 */
	public String getCertCode() {
		return certCode;
	}
	/** 
	 * @param certCode 要设置的 certCode 
	 */
	public void setCertCode(String certCode) {
		this.certCode = certCode;
	}
	/** 
	 * @return otherDescription 
	 */
	public String getOtherDescription() {
		return otherDescription;
	}
	/** 
	 * @param otherDescription 要设置的 otherDescription 
	 */
	public void setOtherDescription(String otherDescription) {
		this.otherDescription = otherDescription;
	}
	/** 
	 * @return status 
	 */
	public String getStatus() {
		return status;
	}
	/** 
	 * @param status 要设置的 status 
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/** 
	 * @return userId 
	 */
	public String getUserId() {
		return userId;
	}
	/** 
	 * @param userId 要设置的 userId 
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/** 
	 * @return department 
	 */
	public String getDepartment() {
		return department;
	}
	/** 
	 * @param department 要设置的 department 
	 */
	public void setDepartment(String department) {
		this.department = department;
	}
	/** 
	 * @return homeAddress 
	 */
	public String getHomeAddress() {
		return homeAddress;
	}
	/** 
	 * @param homeAddress 要设置的 homeAddress 
	 */
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
	/** 
	 * @return certProvince 
	 */
	public String getCertProvince() {
		return certProvince;
	}
	/** 
	 * @param certProvince 要设置的 certProvince 
	 */
	public void setCertProvince(String certProvince) {
		this.certProvince = certProvince;
	}
	/** 
	 * @return certCity 
	 */
	public String getCertCity() {
		return certCity;
	}
	/** 
	 * @param certCity 要设置的 certCity 
	 */
	public void setCertCity(String certCity) {
		this.certCity = certCity;
	}
	/** 
	 * @return certZone 
	 */
	public String getCertZone() {
		return certZone;
	}
	/** 
	 * @param certZone 要设置的 certZone 
	 */
	public void setCertZone(String certZone) {
		this.certZone = certZone;
	}
	/** 
	 * @return certAddress 
	 */
	public String getCertAddress() {
		return certAddress;
	}
	/** 
	 * @param certAddress 要设置的 certAddress 
	 */
	public void setCertAddress(String certAddress) {
		this.certAddress = certAddress;
	}
	/** 
	 * @return telephoneTwo 
	 */
	public String getTelephoneTwo() {
		return telephoneTwo;
	}
	/** 
	 * @param telephoneTwo 要设置的 telephoneTwo 
	 */
	public void setTelephoneTwo(String telephoneTwo) {
		this.telephoneTwo = telephoneTwo;
	}
	/** 
	 * @return marriedCount 
	 */
	public String getMarriedCount() {
		return marriedCount;
	}
	/** 
	 * @param marriedCount 要设置的 marriedCount 
	 */
	public void setMarriedCount(String marriedCount) {
		this.marriedCount = marriedCount;
	}
	/** 
	* @Title: toString 
	* @Description: TODO
	* @param: @return 
	* @throws 
	*/
	@Override
	public String toString() {
		return "UserVO [name=" + name + ", sex=" + sex + ", photo=" + photo
				+ ", nameEn=" + nameEn + ", nation=" + nation + ", birth="
				+ birth + ", birthPlace=" + birthPlace + ", age=" + age
				+ ", political=" + political + ", graduationTime="
				+ graduationTime + ", education=" + education + ", school="
				+ school + ", profession=" + profession + ", cert=" + cert
				+ ", marriage=" + marriage + ", blood=" + blood + ", no=" + no
				+ ", category=" + category + ", jobTitle=" + jobTitle
				+ ", post=" + post + ", wageLevel=" + wageLevel + ", bankName="
				+ bankName + ", wageNo=" + wageNo + ", entryDate=" + entryDate
				+ ", positiveDate=" + positiveDate + ", separationDate="
				+ separationDate + ", telephoneOne=" + telephoneOne
				+ ", companyTel=" + companyTel + ", fax=" + fax
				+ ", homePhone=" + homePhone + ", email=" + email
				+ ", homeProvince=" + homeProvince + ", homeCity=" + homeCity
				+ ", homeZone=" + homeZone + ", homeCode=" + homeCode
				+ ", certCode=" + certCode + ", otherDescription="
				+ otherDescription + ", status=" + status + ", userId="
				+ userId + ", department=" + department + ", homeAddress="
				+ homeAddress + ", certProvince=" + certProvince
				+ ", certCity=" + certCity + ", certZone=" + certZone
				+ ", certAddress=" + certAddress + ", telephoneTwo="
				+ telephoneTwo + ", marriedCount=" + marriedCount + "]";
	}
	
}
