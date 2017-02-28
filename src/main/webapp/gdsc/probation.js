$(function(){
	$this.clickOperation();
});
var $this = {
	'clickOperation':function(){
		$("input[name='试用期阶段']").click(function(){
			var stage = $("input[name='试用期阶段']:checked").val();
			if(stage == "一周"){
				$(".oneOrTwoMonth").hide();
				$(".positive").hide();
				$(".oneWeek").show();
			}else if(stage == "一个月" || stage == "两个月"){
				$(".oneWeek").hide();
				$(".positive").hide();
				$(".oneOrTwoMonth").show();
			}else if(stage == "转正"){
				$(".oneWeek").hide();
				$(".oneOrTwoMonth").hide();
				$(".positive").show();
			}
		});
	}
}