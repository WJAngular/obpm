<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<meta http-equiv="X-UA-Compatible" content="IE=100" />
	<%@ page contentType="text/html; charset=UTF-8"%>
	<script type="text/javascript" src="<%=request.getContextPath()%>/gdsc/script/jquery-1.7.2.min.js"></script>
	<link rel="stylesheet" href="<%=request.getContextPath() %>/gdsc/layer/skin/layer.css">
	<link rel="stylesheet" href="<%=request.getContextPath() %>/gdsc/layer/skin/layer.ext.css">
	<script type="text/javascript" src="<%=request.getContextPath() %>/gdsc/layer/layer.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/gdsc/layer/extend/layer.ext.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/gdsc/client.js"></script>
	
	<o:MultiLanguage value="FRONTMULTILANGUAGETAG">
	<title>客户新增</title>
	<style>
		* {
			-webkit-box-sizing: border-box;
			-moz-box-sizing: border-box;
			box-sizing: border-box;
		}
		
		body {
			margin: 0;
			padding: 12px;
			font-size: 14px;
			font-family: "Helvetica Neue", "Microsoft Yahei", "sans-serif";
			background: #f6f7fb;
		}
		
		a {
			text-decoration: none;
		}
		table {
			width: 100%;
			border-spacing: 0;
			border-collapse: collapse;
		}
		
		.td-label {
			text-align: center;
			vertical-align: middle;
			padding: 10px;
			border: 1px solid #ddd;
			background: #f4f4f4;
		}
		
		.td-input {
			padding: 15px 25px;
			border: 1px solid #ddd;
		}
		
		.form-control {
			display: block;
			width: 100%;
			height: 34px;
			padding: 6px 12px;
			font-size: 14px;
			line-height: 1.42857143;
			color: #555;
			background-color: #F2f2f2;
			outline:none;
			border: none;
			-webkit-transition: border-color ease-in-out .15s, -webkit-box-shadow ease-in-out .15s;
			-o-transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
			transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
		}
		.btn {
			display: inline-block;
			padding: 6px 12px;
			margin-bottom: 0;
			font-size: 14px;
			font-weight: 400;
			line-height: 1.42857143;
			text-align: center;
			white-space: nowrap;
			vertical-align: middle;
			-ms-touch-action: manipulation;
			touch-action: manipulation;
			cursor: pointer;
			-webkit-user-select: none;
			-moz-user-select: none;
			-ms-user-select: none;
			user-select: none;
			background-image: none;
			border: 1px solid transparent;
			border-radius: 4px;
		}
		.btn-warning {
			color: #fff;
			background-color: #f0ad4e;
			border-color: #eea236;
		}
		.btn-default {
			color: #333;
			background-color: #fff;
			border-color: #EAEAEA;
		}
		.btn-resetNo {
			color: #333;
			background-color: #3595CC;
			border-color: #EAEAEA;
		}
		.vbtn {
			margin-right: 10px;
		}
		.activity-box {
			border: 1px solid #E3E3E3;
			border-top: 0;
			border-bottom: 0;
			padding: 10px 15px;
			position: fixed;
			top: 0;
			left: 12px;
   			right: 12px;
			z-index: 100;
			background-color: #ffffff;
		}
		.custom-form {
			margin-top: 50px;
			border: 1px solid #ddd;
			padding: 0 25px 25px;
			background: #ffffff;
		}
	</style>
</head>

<body>
	<form id="client" action="" method="post">
	<div id="activityTable" class="activity-box">
		<table id="oLayer" class="table_noborder" style="position: relative">
			<tbody>
				<tr id="act" class="front-table-act front-table-full-width">
					<td style="width:100%;" class="formActBtn">
						<a class="42 vbtn btn btn-warning" id="saveBtn" name="saveBtn" onclick="javascript:clients.submitForm();" title="保存">保存</a>
						<a class="10 vbtn btn btn-default" id="resetBtn" name="resetBtn" title="重置">重置</a>
						<a class="10 vbtn btn btn-resetNo" id="resetNoBtn" name="resetNoBtn" title="重置编号">重置编号</a>
						<!-- <input class="42 vbtn btn btn-warning" type="button" id="saveBtn" name="saveBtn" onclick="javascript:clients.submitForm();" value="保存" /> 
						<input class="10 vbtn btn btn-default" type="button" id="resetBtn" name="resetBtn" value="重置" />
						<input class="10 vbtn btn btn-default" type="button" id="resetNoBtn" name="resetNoBtn" value="重置编号" />-->
					</td>			
					<td id="processorHtml">
						<!-- 当前审批人列表 -->				
					</td>
				
					<td id="flowStateHtml">
						<!-- 当前流程状态 -->					
					</td>
					<td class="flowbtn" style="display: none;">
						<div class="button-cmd">
							<div class="btn_mid">
								<a class="flowicon icon16 flowicon-tubiao" href="###" id="button_act" title="流程图表">图表</a>
							</div>
						</div>
					</td>
					<td class="flowbtn" style="display: none;">
						<div class="button-cmd">
							<div class="btn_mid">
								<a class="flowicon icon16 flowicon-lishi" href="###" id="button_act" title="历史">历史</a>
							</div>
						</div>	
					</td>
					<!-- 流程节点样式修改 ：三个皮肤，国际化-->
					<td id="current-processing-node-td">
						<!-- 当前流程节点 -->							
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="custom-form">
		<p style="text-align: center;">
			<span style="font-size: x-large;">客户管理</span>
		</p>
		<table>
			<tbody>
				<tr>
					<td class="td-label">客户名称<span style="color: rgb(255, 0, 0);">*</span></td>
					<td class="td-input"><span id="11e6-4252-60791c4d-b5bd-ed52628c16b2_divid"><input class="form-control component-input" type="text" style="width:100%;" id="11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_客户名称" name="vo.clientName" classname="cn.myapps.core.dynaform.form.ejb.InputField" fieldtype="VALUE_TYPE_VARCHAR" fieldkeyevent="Tabkey" value="" title="" isrefreshonchanged="false" text="text"></span></td>
					<td class="td-label">客户编号<span style="color: rgb(255, 0, 0);">*</span></td>
					<td class="td-input"><span id="11e6-4289-710563bd-b5bd-ed52628c16b2_divid"><input class="form-control component-input component-input-readonly" type="text" style="width:100%;" id="11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_客户编号" name="vo.clientNo" classname="cn.myapps.core.dynaform.form.ejb.InputField" fieldtype="VALUE_TYPE_VARCHAR" fieldkeyevent="Tabkey" value="" title="CN16070902" isrefreshonchanged="false" text="text"></span></td>
				</tr>
				<tr>
					<td class="td-label">所属行业</td>
					<td class="td-input">
						<span id="11e6-4252-f7b7d4a4-b5bd-ed52628c16b2_divid">
							<select class="form-control component-select" id="11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_所属行业" name="vo.clientProfession" fieldtype="SelectField" onchange="" style="width:100%;">
								<option value="" class="select-cmd">请选择</option>
								<option value="农、林、牧、渔业；采矿业" class="select-cmd">农、林、牧、渔业；采矿业</option>
								<option value="制造业" class="select-cmd">制造业</option>
								<option value="租赁和商务服务业" class="select-cmd">租赁和商务服务业</option>
								<option value="科学研究、技术服务和地质勘查业" class="select-cmd">科学研究、技术服务和地质勘查业</option>
								<option value="水利、环境和 公共设施管理业" class="select-cmd">水利、环境和 公共设施管理业</option>
								<option value="居民服务和其他服务业" class="select-cmd">居民服务和其他服务业</option>
								<option value="教育" class="select-cmd">教育</option>
								<option value="卫生、社会保障和社会福利业" class="select-cmd">卫生、社会保障和社会福利业</option>
								<option value="文化、体育和娱乐业" class="select-cmd">文化、体育和娱乐业</option>
								<option value="民间组织" class="select-cmd">民间组织</option>
								<option value="政府机构" class="select-cmd">政府机构</option>
								<option value="电力、煤气及水的生产和供应业" class="select-cmd">电力、煤气及水的生产和供应业</option>
								<option value="建筑业" class="select-cmd">建筑业</option>
								<option value="交通运输、仓储	及邮政业" class="select-cmd">交通运输、仓储	及邮政业</option>
								<option value="信息传输、计算机服务和软件业" class="select-cmd">信息传输、计算机服务和软件业</option>
								<option value="批发和零售业" class="select-cmd">批发和零售业</option>
								<option value="住宿和餐饮业" class="select-cmd">住宿和餐饮业</option>
								<option value="金融业" class="select-cmd">金融业</option>
								<option value="房地产业" class="select-cmd">房地产业</option>
							</select>
						</span>
					</td>
					<td class="td-label">客户性质</td>
					<td class="td-input">
						<a id="fck_paste_padding">
							<span id="11e6-4289-53ff946f-b5bd-ed52628c16b2_divid">
								<select class="form-control component-select" id="11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_客户性质" name="vo.clientNature" fieldtype="SelectField" onchange="" style="width:100%;">
									<option value="" class="select-cmd">请选择</option>
									<option value="教育培训机构" class="select-cmd">教育培训机构</option>
									<option value="政府行政单位" class="select-cmd">政府行政单位</option>
									<option value="个体工商户" class="select-cmd">个体工商户</option>
									<option value="自然人" class="select-cmd">自然人</option>
									<option value="国有企业" class="select-cmd">国有企业</option>
									<option value="有限责任公司" class="select-cmd">有限责任公司</option>
									<option value="股份有限公司" class="select-cmd">股份有限公司</option>
									<option value="集体企业" class="select-cmd">集体企业</option>
									<option value="联营企业" class="select-cmd">联营企业</option>
									<option value="股份合作制企业" class="select-cmd">股份合作制企业</option>
									<option value="私营企业" class="select-cmd">私营企业</option>
									<option value="合伙企业" class="select-cmd">合伙企业</option>
								</select>
							</span>
						</a>
					</td>
				</tr>
				<tr>
					<td class="td-label">联系人<span style="color: rgb(255, 0, 0);">*</span></td>
					<td class="td-input"><span id="11e6-4289-200db25d-b5bd-ed52628c16b2_divid"><input class="form-control component-input" type="text" style="width:100%;" id="11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_联系人" name="vo.clienter" classname="cn.myapps.core.dynaform.form.ejb.InputField" fieldtype="VALUE_TYPE_VARCHAR" fieldkeyevent="Tabkey" value="" title="" isrefreshonchanged="false" text="text"></span></td>
					<td class="td-label">联系电话<span style="color: rgb(255, 0, 0);">*</span></td>
					<td class="td-input"><span id="11e6-4252-a21dc490-b5bd-ed52628c16b2_divid"><input class="form-control component-input" type="text" placeholder="模版：0752-8888888或13888888888" style="width:100%;" id="11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_联系电话" name="vo.clientTelephone" classname="cn.myapps.core.dynaform.form.ejb.InputField" fieldtype="VALUE_TYPE_VARCHAR" fieldkeyevent="Tabkey" value="" title="" isrefreshonchanged="false" text="text"></span></td>
				</tr>
				<tr>
					<td class="td-label">E_Mail</td>
					<td class="td-input"><span id="11e6-4252-d3634a9a-b5bd-ed52628c16b2_divid"><input class="form-control component-input" type="text" style="width:100%;" id="11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_E_Mail" name="vo.clientEmail" classname="cn.myapps.core.dynaform.form.ejb.InputField" fieldtype="VALUE_TYPE_VARCHAR" fieldkeyevent="Tabkey" value="" title="" isrefreshonchanged="false" text="text"></span></td>
					<td class="td-label">&nbsp;传真
						<a id="fck_paste_padding"></a>
					</td>
					<td class="td-input"><span id="11e6-4289-8a18ea3b-b5bd-ed52628c16b2_divid"><input class="form-control component-input" type="text" style="width:100%;" id="11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_传真" name="vo.clientFax" classname="cn.myapps.core.dynaform.form.ejb.InputField" fieldtype="VALUE_TYPE_VARCHAR" fieldkeyevent="Tabkey" value="" title="" isrefreshonchanged="false" text="text"></span></td>
				</tr>
				<tr>
					<td class="td-label">URL网址</td>
					<td class="td-input"><span id="11e6-4252-fc3546a2-b5bd-ed52628c16b2_divid"><input class="form-control component-input" type="text" style="width:100%;" id="11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_URL网址" name="vo.clientUrl" classname="cn.myapps.core.dynaform.form.ejb.InputField" fieldtype="VALUE_TYPE_VARCHAR" fieldkeyevent="Tabkey" value="" title="" isrefreshonchanged="false" text="text"></span></td>
					<td class="td-label">法人代表</td>
					<td class="td-input"><span id="11e6-4252-fff35f40-b5bd-ed52628c16b2_divid"><input class="form-control component-input" type="text" style="width:100%;" id="11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_法人代表" name="vo.clientCorporate" classname="cn.myapps.core.dynaform.form.ejb.InputField" fieldtype="VALUE_TYPE_VARCHAR" fieldkeyevent="Tabkey" value="" title="" isrefreshonchanged="false" text="text"></span></td>
				</tr>
				<tr>
					<td class="td-label">合作伙伴性质</td>
					<td class="td-input" colspan="3">
						<a id="fck_paste_padding">
							<span id="11e6-4289-53ff946f-b5bd-ed52628c16b2_divid">
								<select class="form-control component-select" id="11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_合作伙伴性质" name="vo.partner" fieldtype="SelectField" onchange="" style="width:100%;">
									<option value="" class="select-cmd">请选择</option>
									<option value="客户" class="select-cmd">客户</option>
									<option value="供应商" class="select-cmd">供应商</option>
									<option value="商务伙伴" class="select-cmd">商务伙伴</option>
								</select>
							</span>
						</a>
					</td>
				</tr>
				<tr>
					<td class="td-label">联系地址</td>
					<td class="td-input" colspan="3"><span id="11e6-4252-ed6290f6-b5bd-ed52628c16b2_divid"><input class="form-control component-input" type="text" style="width:100%;;" id="11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_联系地址" name="vo.clientAddress" classname="cn.myapps.core.dynaform.form.ejb.InputField" fieldtype="VALUE_TYPE_VARCHAR" fieldkeyevent="Tabkey" value="" title="" isrefreshonchanged="false" text="text"></span>
						<a id="fck_paste_padding"></a>
					</td>
				</tr>
				<tr>
					<td class="td-label">经营范围</td>
					<td class="td-input" colspan="3"><span id="11e6-4252-c6958dae-b5bd-ed52628c16b2_divid"><textarea class="form-control" id="11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_经营范围" name="vo.clientScope" classname="cn.myapps.core.dynaform.form.ejb.TextareaField" fieldtype="TextAreaField" title="" style="width:100%;height:80px;"></textarea></span></td>
				</tr>
				<tr>
					<td class="td-label">描述</td>
					<td class="td-input" colspan="3"><span id="11e6-4252-cbd0aa5c-b5bd-ed52628c16b2_divid"><textarea class="form-control" id="11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_描述" name="vo.description" classname="cn.myapps.core.dynaform.form.ejb.TextareaField" fieldtype="TextAreaField" title="" style="width:100%;height:80px;"></textarea></span>
						<a id="fck_paste_padding"></a>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	</form>
	<input type="hidden" name="isSuccess" id="isSuccess" value="${isSuccess }" />
	<input type="hidden" name="clientName" id="clientName" value="${vo.clientName }" />
	<input type="hidden" name="clientNature" id="clientNature" value="${vo.clientNature }" />
	<input type="hidden" name="clientProfession" id="clientProfession" value="${vo.clientProfession }" />
	<input type="hidden" name="clienter" id="clienter" value="${vo.clienter }" />
	<input type="hidden" name="clientTelephone" id="clientTelephone" value="${vo.clientTelephone }" />
	<input type="hidden" name="clientFax" id="clientFax" value="${vo.clientFax }" />
	<input type="hidden" name="clientEmail" id="clientEmail" value="${vo.clientEmail }" />
	<input type="hidden" name="clientAddress" id="clientAddress" value="${vo.clientAddress }" />
	<input type="hidden" name="clientUrl" id="clientUrl" value="${vo.clientUrl }" />
	<input type="hidden" name="clientCorporate" id="clientCorporate" value="${vo.clientCorporate }" />
	<input type="hidden" name="clientScope" id="clientScope" value="${vo.clientScope }" />
	<input type="hidden" name="description" id="description" value="${vo.description }" />
	<input type="hidden" name="clientNo" id="clientNo" value="${vo.clientNo }" />
	<input type="hidden" name="partner" id="partner" value="${vo.partner }" />
	<input type="hidden" name="dataFlag" id="dataFlag" value="${dataFlag }"/>
</body>
</html>
</o:MultiLanguage>