$(function(){
	//首先设置页面的客户编号
	var clientName = $("input[name='客户名称']").val();
	if(clientName == "" || clientName == null){
		$this.getClientNo();
	}
	$("a[title='重置编号']").click(function(){
		$this.checkNoExist();
		$this.setClientNo();
	})
});
var $this = {
	'getClientNo':function(){
		$.ajax({
			url:"/obpm/clientjson/clientDefault.action",
			type:'POST',
			async: false,
			data:{},
			success:function (data){
				if(data != null){
					var endNo = "";
					var value = "";
					var one = "";
					endNo = data.substr(data.length - 2,data.length);
					one = (parseInt(endNo)+parseInt(1)).toString();
					if(one.length == 1){ //例如：3 则在前面加上0
						one = "0"+one;
					}
					value = data.substr(0,data.length - 2)+one;
					$("input[name='客户编号']").val(value);
					$("input[name='客户编号']").attr("readonly","true");
				}
			},
			error:function(e){
				 alert("Connection error");
			},
			datatype:"json"
		});
	},
	'checkNoExist':function(){
		var value = $("input[name='客户编号']").val();
		$.ajax({
            cache: true,
            type: "POST",
            url:'/obpm/client/checkNo.action',
            data:{'resetNo':value},
            async: false,
            error: function(request) {
            },
            success: function(data) {
            	var d = eval("("+data+")");//将数据转换成json类型  isExist:0表示存在  isExist:1表示不存在
            	if(d.isExist == "0"){
					$("#dataFlag").val(d.dataFlag);
            	}
            },
            datatype:"json"
        });
	},
	'setClientNo':function(){
		var dataFlag = $("#dataFlag").val();
		setClientNo(dataFlag);
	}
};
//设置客户编号
var setClientNo = function(val){
	var no = val;
	var endNo = "";
	var value = "";
	var one = "";
	if(no != null && no != ""){
		endNo = no.substr(no.length - 2,no.length);
		one = (parseInt(endNo)+parseInt(1)).toString();
		if(one.length == 1){ //例如：3 则在前面加上0
			one = "0"+one;
		}
		value = no.substr(0,no.length - 2)+one;
		$("input[name='客户编号']").val(value);
		$("input[name='客户编号']").attr("readonly","true");
	}
};
