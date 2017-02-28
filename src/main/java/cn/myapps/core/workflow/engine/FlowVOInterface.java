package cn.myapps.core.workflow.engine;

import java.sql.Date;

public interface FlowVOInterface {
	public long getId();

	public void setId(long id);

	public String get_recorddescription();// 记录相应描述（为每个模块的title或name等）

	public String get_recordtype();// 类型（收文、发文、用车等）

	public String get_recordcreator();// 记录作者

	public Date get_recordcreatedate();// 记录创建日期

	public String getOwner();
}
