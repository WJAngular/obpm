public.js    公有的javascript
matter.js    立项登记中的脚本
matterclient.jsp 立项登记中新增客户页面
client。js    立项登记中新增客户页面的脚本
prostage.jsp   立项登记中新增模版页面
prostage.js    立项登记中新增模版页面中的脚本

财务管理中资金申请管理模块中的相关脚本
procurementFund.js  采购货款费用申请中的脚本
businessFee.js      业务费用申请中的脚本
contact.js          往来款申请中的脚本
entertain.js        招待费用申请中的脚本
sporadic.js         零星费用申请中的脚本
tender.js			招投标费用申请中的脚本

account.js   财务报销   报销申请中的脚本
propact.js   财务结算  项目合同脚本 （查询字段中）  （无效）
propactaction.js 财务结算   -->项目合同脚本（表单中)   （新建的demo 后来无用，无效）

changeTitle.js  用于每个表单中更改license key中右下角的名称

probation.js    入职管理      试用期评价表中的脚本

test.js  用来测试用


/*********************主表单中存在子表，导出Excel修改***************/  注释的为原来的代码
HibernateSQLUtils.java   555行修改为newSQL = start + end.substring(0, index + 7) + " (" + condition + ")"; //or " + end.substring(index + 7); 2013-12-06 Excel导出子表不需要后续条件

//				newSQL = start + end.substring(0, index + 7) + " (" + condition + ")) tb_excl "; //or " + end.substring(index + 7); 2013-12-06 Excel导出子表不需要后续条件
				newSQL = start + end.substring(0, index + 7) + " (" + condition + ")"; //or " + end.substring(index + 7); 2013-12-06 Excel导出子表不需要后续条件

/**********************因ntext 与 varchar格式不兼容，故改为下面sql语句 *********************************/  注释的为原来的代码
cn.myapps.core.dynaform.view.ejb.editmode.DesignEditMode  
304 行
//			sql.append(" OR d." + columnName + "='").append(user.getId()).append("'");  //因ntext 与 varchar格式不兼容，故改为下面sql语句 
			sql.append(" OR " + "cast(d."+columnName +" AS varchar(2000)) ='").append(user.getId()).append("'");

/****************************用户选择框，微信图片获取出错，字符串不全********************************/
//				if(!StringUtil.isBlank(tempUser.getAvatar())){
//					JSONObject json = JSONObject.fromObject(tempUser.getAvatar());
//					avatar = json.getString("url");
//				}



/************重要的脚本**************/

<script src='<s:url value='/portal/share/component/view/common.js' />'></script>

<script src='<s:url value='/portal/share/component/view/view.js' />'></script>

<script src='<s:url value="/portal/dwz/resource/document/obpm.ui.js"/>'></script>

<script src='<s:url value="/portal/share/script/document/document.js"/>'></script>   
该脚本有//查看流程图表   //有关前端流程的方法脚本                       
Activity.makeAllFieldAble()  Activity插件主要是 <p>框架主要封装操作按钮的执行流程，和一些通用操作接口，在执行过程中，具体的行为实现由具体的按钮类型实例完成</p>   obpm。activity.core.js



$("<div></div>")  追加一个div标签


/************系统相关文档******/
baseLib.js    向iScript注册相关方法
SystemBaseLib.java   增加相关函数名称，以便用于判断函数名称是否已经存在

