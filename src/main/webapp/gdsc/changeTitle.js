$(function(){
	//改变页面右下角的文字，该文字由license key生成
	removeTitle();
})

//改变页面右下角的文字，该文字由license key生成
var removeTitle = function(){
	var title = $("td[title='SVNNO:']");
	title.html("<font color='red'>广东思程科技有限公司  | 管理平台</font>");
};