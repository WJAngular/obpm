$(function(){
	
	//默认隐藏
	prostage.defaultHide();
	//父页面中项目类型选择，影响子页面的radio选择
	prostage.defaultVlaue();
	//选择单选框 复选框
	$(":radio").parent().parent().click(function(){
		$(this).children().eq(0).children().attr("checked","checked");
		if($(this).children().eq(2).children().hasClass("checkbox")){
			$(":checkbox").removeAttr("disabled");
			$(":checkbox").click(function(){
				$("#preHide").show();
				var checkbox = $(":checkbox");
				var rj = "";
				for(var i = 0; i < checkbox.length; i++){
					//遍历选中的复选框值
					if(checkbox[i].checked){
						rj += checkbox[i].value+"->";
					}
				} 
				rj = rj.substr(0,rj.length - 2);//截取掉"->"
				$("#previous").html(rj);
			});
		}else{
			$(":checkbox").attr("disabled","disabled");
			$(":checkbox").removeAttr("checked");
			$("#preHide").hide();
			$("#previous").html('');
		}
		$(this).addClass("td-label").siblings().removeClass("td-label");
	});
	
});
var prostage = {
	'defaultHide':function(){
		$("#preHide").hide();
	},
	'defaultVlaue':function(){
		var proTypeVal = window.parent.getProType();
		if(proTypeVal == "软件"){
			$("input[name='choose'][value='1']").attr("checked",'checked');
		}else if(proTypeVal == "工程"){
			$("input[name='choose'][value='2']").attr("checked",'checked');
		}
	},
	'stage' : function(){
		var radioVal = $("input[name='choose']:checked").val();
		var rj = "";
		var proTemVal = $("input[name='项目阶段模版']",parent.document).val();
		var flag = "noValue";
		var isDelete = false;
		if(proTemVal != ""){
			flag="hasValue";
			if(confirm("是否重新设置模版?选择是，原有的项目进展阶段明细将会被删除!")){
				isDelete = true;
			}else{
				isDelete = false;
			}
		}else{
			isDelete = true;
		}
		if(isDelete){
			if(radioVal == 1){
				rj = "勘查->初步设计->招投标->深化设计->开发->测试->上线->验收->维护";
			}else if(radioVal==2){
				rj = "立项->设计->施工";
			}else if(radioVal==3){
				var checkboxValue = $("input[name='checkbox']:checked").val();
				if(checkboxValue == undefined){
					layer.alert("请选择具体阶段!", {
						  icon: 0,
						  skin: 'layer-ext-moon' 
						});
					return;
				}
				var checkbox = $(":checkbox");
				for(var i = 0; i < checkbox.length; i++){
					//遍历选中的复选框值
					if(checkbox[i].checked){
						rj += checkbox[i].value+"->";
					}
				} 
				rj = rj.substr(0,rj.length - 2);//截取掉"->"
			}
			var sendData = {
					'stage.proName' :$("input[name='事项名称']",parent.document).val(),
					'stage.proNo' :$("input[name='事项编号']",parent.document).val(),
					'stage.clientName' :$("input[name='客户名称']",parent.document).val(),
					'stage.proStage' : rj,
					'stage.parentId':$("input[name='当前文档ID']",parent.document).val(),
					'stage.status':"未开始",
					'stage.flag':flag
			};
			$.ajax({
	            cache: true,
	            type: "POST",
	            url:'/obpm/stage/prostage.action',
	            data:sendData,
	            async: false,
	            error: function(request) {
	            	alert("Connection error");
	            },
	            success: function(data) {
	            	var d = eval("("+data+")");//将数据转换成json类型  insertBatch:0新增成功  insertBatch:1表示新增失败
	            	var rtnValue = "";
	            	if(d.insertBatch == "0"){  //表示新增成功
	            		rtnValue = "模版设置成功!";
	            		//设置成功之后，自动给页面刷新
	            		$("input[name='项目阶段模版']",parent.document).attr("value",rj);
	        			$("input[name='项目阶段模版']",parent.document).attr("title",rj);
	            		var id = $("input[name='项目阶段模版']",parent.document).attr("id");
	        			window.parent.dy_refresh(id);//dy_refresh刷新
	            	}else if(d.insertBatch == "1"){//表示新增失败
	            		rtnValue = "模版设置失败!";
	            	}
	            	//询问框
	            	layer.alert(rtnValue, {
	            		  skin: 'layui-layer-molv' //样式类名
	            		  ,closeBtn: 0
//	            		  ,shift: 1 //动画类型
	            		}, function(){
	            			window.parent["$this"].inputWidthHeight();
	            			parent.layer.closeAll();
	            		});
	            },
	            datatype:"json"
	        });
		}
		}
		
};
/** 获取系统当前时间  例如：2016-05-16 17:48:34*/
function currDateTime() {
    var d = new Date();
    var result = d.getFullYear();
    var month = d.getMonth() + 1;
    var date = d.getDate();
    var hour = d.getHours();
    var minute = d.getMinutes();
    var second = d.getSeconds();
    if (month > 9) {
    	result = result + "-" + month ;
    } else {
    	result = result + "-0" + month ;
    }
    if (date > 9) {
    	result = result + "-" + date;
    } else {
    	result = result + "-0" + date;
    }
    if (hour > 9) {
    	result = result + " " + hour;
    } else {
    	result = result + " 0" + hour;
    }
    if (minute > 9) {
    	result = result + ":" + minute;
    } else {
    	result = result + ":0" + minute;
    }
    if (second > 9) {
    	result = result + ":" + second;
    } else {
    	result = result + ":0" + second;
    }
    return result;
}