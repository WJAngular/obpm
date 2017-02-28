/**
 * 流程历史
 */
(function($){
	
	var ShowMode = {
			SHOW_MODE_TEXT : 'text',//只显示文本
			SHOW_MODE_DIAGRAM : 'diagram',//只显示图表
			SHOW_MODE_TEXT_AND_DIAGRAM : 'textAndDiagram'//显示文本与图表
	};
	
	function buildFlowHistoryTextList($field){
		//TODO html中的css样式代码抽离到css文件
		var html = [];
		html.push("<table cellSpacing='0' cellPadding='1' width='100%' align='center' style='border:solid #cccccc;border-width:1px 0px 0px 1px;'><tbody>");
		
		$field.find("table tr").each(function(i){//行<tr>
			var $trField = jQuery(this);
			if(i==0){//列头
				html.push("<thead>");
				html.push("<tr style='line-height:22px;'>");
				$trField.children().each(function(){
					var $tdField = jQuery(this);
					html.push("<th style='align:center;font-family: Arial;border:solid #cccccc;border-width:0px 1px 1px 0px;background-color:#EFF0F1;width="+$tdField.attr("width")+"' >");
					html.push($tdField.text()+"</th>");
					
				});
				html.push("</tr></thead>");
			}else{
				html.push("<tr style='line-height:22px;'>");
				$trField.children().each(function(){
					var $tdField = jQuery(this);
					//var textIndent = 30*$tdField.text().split("\\").length-1;
					html.push("<td style='font-family: Arial;border:solid #cccccc;border-width:0px 1px 1px 0px;align:center;'>");
					html.push($tdField.text()? $tdField.text() : "&nbsp;");
					if($tdField.hasClass('attitude') && $tdField.find("span").length==1){
						var data = $tdField.find("span").data("datas");
						html.push("<a href='");
						html.push("data:" + data['type'] + "," + data['data']);
						html.push("' target='_blank' title='查看大图' >");
						html.push("<img height='32px' src='");
						html.push("data:" + data['type'] + "," + data['data']);
						html.push("' >");
						html.push("</a>");
					}
					html.push("</td>");
					
				});
				html.push("</tr>");
			}
		});
		html.push("</tbody></table>");
		
		return html.join('');
	}
	
	
	$.fn.obpmFlowHistoryField = function(){
		return this.each(function(){
			var $field = jQuery(this);
//			$field.remove();//隐藏不渲染
			var showMode = $field.attr("showMode");
			var flowDiagram = $field.attr("flowDiagram");
			var mobile = $field.attr("mobile");
			var name = $field.attr("_name");
			var discript = $field.attr("_discript");
			discript = discript? discript : name;
			var html = [];
			if(mobile=="true"){
				html.push("<div style='list-style-type:none;margin-left: 3px;margin-right: 3px;margin-top: 3px;margin-bottom: 3px;'>");
				html.push("<label class='field-title contact-name' for='" + name + "'>" + discript + "</label>");
				if(showMode == ShowMode.SHOW_MODE_TEXT){
					html.push("<li>"+buildFlowHistoryTextList($field)+"</li>");
				}else if(showMode == ShowMode.SHOW_MODE_DIAGRAM){
					html.push("<li><img src='"+flowDiagram+"' style='width:100%' /></li>");
				}else if(showMode == ShowMode.SHOW_MODE_TEXT_AND_DIAGRAM){
					html.push("<li><img src='"+flowDiagram+"' style='width:100%' /></li>");
					html.push("<li>"+buildFlowHistoryTextList($field)+"</li>");
				}
				html.push("</div>");
			}
			$field.replaceWith($(html.join('')));
		});
	};
})(jQuery);

