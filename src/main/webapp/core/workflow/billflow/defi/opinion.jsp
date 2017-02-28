<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String flowid = request.getParameter("flowid");
%>
<!DOCTYPE html>
<html>
<o:MultiLanguage>
	<head>
<style type="text/css">
.opinion_li {
	list-style-type: none;
	border: 1px dashed gray;
	padding: 4px 10px;
	font-size: 14px;
}

.opinion_li a {
	cursor: pointer;
	float: right;
}

.opinion_li a:hover {
	color: red;
}
</style>
<script type="text/javascript" src="jquery.js"></script>
<script type="text/javascript">
var flowid="<%=flowid%>";
	$(document).ready(function() {
		initopinion();
		//绑定保存事件
		$('#save_id').on("click", function() {
			var value = $("#textarea_id")[0].value;
			var id = flowid;
			var url = "addopinion.action";
			jQuery.ajax({
				type : "POST",
				url : url,
				data : "flowid=" + id + "&value=" + value,
				dataType : "text",//预期服务器返回的数据类型。如果不指定，jQuery 将自动根据 HTTP 包 MIME 信息来智能判断
				async : true, //true为异步请求，false为同步请求
				success : function(data) {
					if (data == "success") {
						initopinion();
					} else
						alert("保存失败！");
				},
				error : function() { //请求失败后的回调函数。
					alert("请求失败");
				}
			});
		});
	});

	//获得json的key值
	function leng(data) {
		var jsonLength = 0;
		var a = [];
		for ( var item in data) {
			a.push(item);
		}
		return a;
	}

	function initopinion() {
		//渲染select选择框/core/workflow/billflow/defi/initopinion
		var url = "initopinion.action";
		jQuery
				.ajax({
					type : "POST",
					url : url,
					data : "flowid=" + flowid,
					dataType : "text",//预期服务器返回的数据类型。如果不指定，jQuery 将自动根据 HTTP 包 MIME 信息来智能判断
					async : true, //true为异步请求，false为同步请求
					success : function(result) {
						if (result != "null") {
							var data = eval('(' + result + ')');
							var keys = leng(data);
							var contents = "";
							for (; keys.length >= 1;) {
								var key = keys.pop();
								contents = contents
										+ "<li data-id="+key+" class='opinion_li'><span class='opinion_class'>"
										+ data[key]
										+ "</span> <a class='delete_class'>X</a></li>";
							}
							$("#opinion_block").html(contents);
							bind();
						}
						//else
						//alert("常用意见为空！");
					},
					error : function() { //请求失败后的回调函数。
						alert("请求失败");
					}
				});
	}

	function bind() {
		//绑定删除事件
		$('.delete_class').on("click", function() {
			var id = flowid;
			var opinionid = $(this).parent().attr('data-id'); //li opinionid
			var url = "deleteopinion.action";
			jQuery.ajax({
				type : "POST",
				url : url,
				data : "flowid=" + id + "&opinionid=" + opinionid,
				dataType : "text",//预期服务器返回的数据类型。如果不指定，jQuery 将自动根据 HTTP 包 MIME 信息来智能判断
				async : true, //true为异步请求，false为同步请求
				success : function(data) {
					if (data == "success") {
						initopinion();
					} else
						alert("删除失败！");
				},
				error : function() { //请求失败后的回调函数。
					alert("请求失败");
				}
			});
		});

	}
</script>
	</head>
	<body>
		<h2>常用意见：</h2>
		<div style="width: 350px;">
			<ul id="opinion_block">
			</ul>
		</div>
		<div style="width: 350px; margin-left: 40px;">
			<textarea id="textarea_id" style="width: 250px; height: 40px;"></textarea>
			<input type="button" id="save_id" value="保存">
		</div>

	</body>
</o:MultiLanguage>
</html>