$(function(){
	//设置客户编号的值
	setClientNo();
	//重置按钮事件以及已经保存记录之后的提示信息
	$("#resetBtn").click(function(){
		if($("#saveBtn").attr("disabled") == undefined){
			clients.valToZero();
		}else if($("#saveBtn").attr("disabled") == "disabled"){
			//提示层
			layer.msg('该条客户信息记录已经保存！');
		}
	});
//	隐藏按钮
	clients.setHidden();
	
//	重置客户编号
	$("#resetNoBtn").click(function(){
		setClientNo();
		$("#resetNoBtn").hide();
	});
});

var clients = {
	'isEmpty':function (valida){
			if(valida!=null&&valida!=undefined&&valida.trim().length>0){
				return true;
			}
			return false;
		},
	'valToZero':function(){
		$("#11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_客户名称").val('');
		$("#11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_所属行业").val('');
		$("#11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_客户性质").val('');
		$("#11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_联系人").val('');
		$("#11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_联系电话").val('');
		$("#11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_E_Mail").val('');
		$("#11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_传真").val('');
		$("#11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_URL网址").val('');
		$("#11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_法人代表").val('');
		$("#11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_合作伙伴性质").val('');
		$("#11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_联系地址").val('');
		$("#11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_经营范围").val('');
		$("#11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_描述").val('');
	},
	'checkURL':function(obj){
		var pattern = /^(http?:\/\/)?([\da-z\.-]+)\.([a-z\.]{2,6})([\/\w \.-]*)*\/?$/;
		var url = /^(https?:\/\/)?([\da-z\.-]+)\.([a-z\.]{2,6})([\/\w \.-]*)*\/?$/;
		if(obj != ""){
			if(!(pattern.test(obj)||url.test(obj))){
				return false;
			}
		}
		return true;
	},
	'checkTelephone':function(obj){
        var pattern =/^((0\d{2,3})-)(\d{7,8})?$/;
        var phone = /^0?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/;
        if(obj !="")
        {
            if(!(pattern.test(obj)||phone.test(obj)))
            {
             return false;
            }
        }
        return true;
	},
	'checkEmail':function(obj){
        var pattern =/^[a-zA-Z0-9_\-]{1,}@[a-zA-Z0-9_\-]{1,}\.[a-zA-Z0-9_\-.]{1,}$/;
        if(obj !="")
        {
            if(!pattern.test(obj))
            {
             return false;
            }
        }
        return true;
	},
	'submitForm':function(){
					//检查新增客户时某些字段是否为空
					var str = checkNull();
					var valideStr = valide();
					var rtnValue = "";
					var addSuccess = true;
					if(str != ""){
						addSuccess = false;
						if(str.indexOf("客户名称") > -1){layer.tips('客户名称不能为空!', '#11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_客户名称',{tips: [1, '#3595CC'],time: 3000});}
						if(str.indexOf("联系人") > -1){layer.tips('联系人不能为空!', '#11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_联系人',{tips: [1, '#3595CC'],time: 3000});}
						if(str.indexOf("联系电话") > -1){layer.tips('联系电话不能为空!', '#11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_联系电话',{tips: [1, '#3595CC'],time: 3000});}
					}
					if(valideStr != ""){
						addSuccess = false;
						if(valideStr.indexOf("邮箱地址") > -1){layer.tips('请输入正确的邮箱地址!', '#11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_E_Mail',{tips: [1, '#3595CC'],time: 3000});}
						if(valideStr.indexOf("电话号码或手机号") > -1){layer.tips('请输入正确的电话号码或手机号!', '#11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_联系电话',{tips: [1, '#3595CC'],time: 3000});}
						if(valideStr.indexOf("URL网址") > -1){layer.tips('请输入正确的URL网址!', '#11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_URL网址',{tips: [1, '#3595CC'],time: 3000});}
					}
					if(addSuccess){
						//提交表单数据
						$.ajax({
			                cache: true,
			                type: "POST",
			                url:'/obpm/client/addClient.action',
			                data:$('#client').serialize(),// 你的formid
			                async: false,
			                error: function(request) {
			                    alert("Connection error");
			                },
			                success: function(data) {
			                	var d = eval("("+data+")");//将数据转换成json类型
			                	//判断客户编号是否存在，如果是0：存在，1：不存在
			                	if(d.isExist == "0" && d.isSuccess == "1" && d.dataFlag != ""){ //已经存在编号
			                		var no =  $("#11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_客户编号").val();
			                		rtnValue = "编号"+no+"已经存在，请重置编号！";
									layer.alert(rtnValue, {
										  icon: 0,
										  skin: 'layer-ext-moon' 
										});
									$("#resetNoBtn").show();
									$("#dataFlag").val(d.dataFlag);
									
			                	}
			                	if(d.isExist == "1" && d.isSuccess == "0"){//保存成功
			                		//数据保存之后，将按钮设置为不能点击
//			                		$("#saveBtn").attr("disabled", "true");
//			                		$("#saveBtn").css("color","black");
			                		var clientName = $("#11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_客户名称").val();
			                		var clienter = $("#11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_联系人").val();
			                		var clientTelephone = $("#11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_联系电话").val();
			                		//将子页面的值，赋给父页面元素
			                		window.parent.setDefaultValue(clientName,clienter,clientTelephone);
			                		rtnValue = "新增用户信息成功！";
			                		//询问框
			    	            	layer.alert(rtnValue, {
			    	            		  skin: 'layui-layer-molv' //样式类名
			    	            		  ,closeBtn: 0
//			    	            		  ,shift: 1 //动画类型
			    	            		}, function(){
			    	            			parent.layer.closeAll();
			    	            		});
			                	}
			                },
			                datatype:"json"
			            });
					}
	},
	'setHidden':function(){//刚开始隐藏某个按钮
		$("#resetNoBtn").hide();
	}
};
//设置客户编号
var setClientNo = function(){
	var no = $("#dataFlag").val();
	var endNo = "";
	var value = "";
	var one = "";
	if(no != null ){
		endNo = no.substr(no.length - 2,no.length);
		one = (parseInt(endNo)+parseInt(1)).toString();
		if(one.length == 1){ //例如：3 则在前面加上0
			one = "0"+one;
		}
		value = no.substr(0,no.length - 2)+one;
		$("#11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_客户编号").val(value);
		$("#11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_客户编号").attr("readonly","true");
	}
};
//检查 字段是否为空？
var checkNull = function(){
	var clientName = $("#11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_客户名称").val();
	var clienter = $("#11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_联系人").val();
	var clientTelephone = $("#11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_联系电话").val();
	var rtnStr = "";
	if(clientName == undefined || clientName == null || clientName == ""){
		rtnStr = "客户名称不能为空;";
	}else if(clienter == undefined || clienter == null || clienter == ""){
		rtnStr = "联系人不能为空;";
	}else if(clientTelephone == undefined || clientTelephone == null || clientTelephone == ""){
		rtnStr = "联系电话不能为空;";
	}
	return rtnStr;
};
//判断有效性
var valide = function(){
	var email = $("#11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_E_Mail").val();
	var tel = $("#11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_联系电话").val();
	var url = $("#11e6-4576-9c59d8b9-a82b-a1e71e6bef4f_URL网址").val();
	var rtnStr = "";
	if(!clients.checkEmail(email)){
		rtnStr = "请输入正确的邮箱地址;";
	}else if(!clients.checkTelephone(tel)){
		rtnStr = "请输入正确的电话号码或手机号;";
	}else if(!clients.checkURL(url)){
		rtnStr = "请输入正确的URL网址;";
	}
	return rtnStr;
}