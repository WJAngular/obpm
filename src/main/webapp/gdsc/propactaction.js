$(function(){
	$("input[title='显示']").click(function(){
		var proNo = $("input[name='事项编号']").val();
		if(proNo == ""){
			alert("请选择项目!");
		}else{
			$this.checkHisAction("", proNo, "TLK_合同DEMO");
		}
	});
	
});

var $this = {
	'checkHisAction':function(proName,proNo,primaryTable){
		var sendData = {
			'proName':proName,
			'proNo':proNo,
			'primaryTable': primaryTable  //"TLK_合同DEMO"
		};
		//提交表单数据
		$.ajax({
            cache: true,
            type: "POST",
            url:'/obpm/pact/propact.action',
            data:sendData,
            async: false,
            error: function(request) {
                alert("Connection error");
            },
            success: function(data) {
            	var d = eval("("+data+")");//将数据转换成json类型
            	if(d.isExist == "0"){  //表示存在
            		$("input[name='以往合同金额']").val(d.nowSum);
            		$("input[name='现合同金额']").val(d.nowSum);
            		$("input[name='以往收入总金额']").val(d.oldInCome);
            	}else if(d.isExist == "1"){//表示不存在
            		$("textarea[name='以往收入明细']").html(d.prInComeHis);
            	}
				alert(JSON.stringify(d));
            },
            datatype:"json"
        });
	}
}