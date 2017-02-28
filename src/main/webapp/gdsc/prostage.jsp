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
	<script type="text/javascript" src="<%=request.getContextPath()%>/gdsc/prostage.js"></script>
	
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
			vertical-align: middle;
			padding: 10px;
			border: 1px solid #ddd;
			background: #f4f4f4;
		}
		
		.tr-title {
			vertical-align: middle;
			padding: 10px;
			border: 1px solid #ddd;
			background: #C4C4C4;
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
	<form id="prostatge" action="" method="post">
	<div id="activityTable" class="activity-box">
		<table id="oLayer" class="table_noborder" style="position: relative">
			<tbody>
				<tr id="act" class="front-table-act front-table-full-width">
					<td style="width:100%;" class="formActBtn">
						<input class="42 vbtn btn btn-warning" type="button" id="saveBtn" name="saveBtn" onclick="javascript:prostage.stage();" value="保存" />
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
		<table>
			<tbody>
				<tr>
					<th align="center" width="5%" class="tr-title" >标记</th>
					<th align="center" width="20%" class="tr-title" >项目类型</th>
					<th align="center" width="75%" class="tr-title" >阶段明细</th>
				</tr>
				<tr class="td-label">
					<td class="td-input" >
						<input type="radio" name="choose" value="1"/>
					</td>
					<td class="td-input">软件</td>
					<td class="td-input">勘查->初步设计->招投标->深化设计->开发->测试->上线->验收->维护</td>
				</tr>
				<tr>
					<td class="td-input" >
						<input type="radio" name="choose" value="2"/>
					</td>
					<td class="td-input">工程</td>
					<td class="td-input">立项->设计->施工</td>
				</tr>
				<tr>
					<td class="td-input" >
						<input type="radio" name="choose" value="3"/>
					</td>
					<td class="td-input">自定义</td>
					<td class="td-input">
						<input type="checkbox" name="checkbox" value="勘查" disabled="disabled" class="checkbox"/>勘查
						<input type="checkbox" name="checkbox" value="立项" disabled="disabled" class="checkbox"/>立项
						<input type="checkbox" name="checkbox" value="初步设计" disabled="disabled" class="checkbox"/>初步设计
						<input type="checkbox" name="checkbox" value="招投标" disabled="disabled" class="checkbox"/>招投标
						<input type="checkbox" name="checkbox" value="设计" disabled="disabled" class="checkbox"/>设计
						<input type="checkbox" name="checkbox" value="深化设计" disabled="disabled" class="checkbox"/>深化设计
						<input type="checkbox" name="checkbox" value="开发" disabled="disabled" class="checkbox"/>开发
						<input type="checkbox" name="checkbox" value="测试" disabled="disabled" class="checkbox"/>测试
						<input type="checkbox" name="checkbox" value="上线" disabled="disabled" class="checkbox"/>上线
						<input type="checkbox" name="checkbox" value="实施" disabled="disabled" class="checkbox"/>实施
						<input type="checkbox" name="checkbox" value="验收" disabled="disabled" class="checkbox"/>验收
						<input type="checkbox" name="checkbox" value="维护" disabled="disabled" class="checkbox"/>维护
					</td>
				</tr>
				<tr id="preHide">
					<td colspan="3">自定义模版预览：<span id="previous"></span></td>
				</tr>
			</tbody>
		</table>
	</div>
	</form>
</body>
</html>
</o:MultiLanguage>