 (function($) {
	var ALBUM = false;
	//微信拍照
	$("#takephoto").click(function(){
		PM.task.CreateTask();
		var oField = jQuery("#onlinePhoto")
		var _wx = top.wx ? top.wx : wx;
		 _wx.chooseImage({
			 count: 1, // 默认9
			 sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
			 sourceType: ALBUM? ['album','camera']:['camera'],
		     success: function (res) {
		        var localIds = res.localIds;
		        setTimeout(function(){
		            _wx.uploadImage({
				        localId: localIds[0],
				        success: function (res) {
				          var serverId = res.serverId;
							// 本地上传方法
				          $.get(contextPath+"/portal/weixin/jsapi/PMupload.action",{"serverId":serverId,"taskid":$("#form-create-task-id").val()},function(result){
					          if(result.status==1){
							 	$("#tmplUpload").tmpl(result).appendTo(".list");
								$(".list").find(".up-list[name='"+result.data+"']").find("i").hide();
								$(".list").find(".up-list[name='"+result.data+"']").find("img").attr("src",localIds[0]).show();
					          }
					       });
				        },
				        fail: function (res) {
				          alert("网络异常，请再次尝试！");
				        }
				      });
		        }, 100)
		      }
		    });
	});
		
	/**
	 * 获取录音计时器
	 */
	function _getTimer(){
		var timer = $(".time-total").text();
		if(timer==0){
			timer = setInterval(function(){
						var tl = $(".time-total");
						tl.text(parseInt(tl.text())+1);
				},1000);
		}
		return timer;
	} 

})(jQuery);
 
function upload(taskId) {
	var uploadLiTmp = '<li class="task-attachment-li task-attachment-li-status"><div class="task-attachment-li-content"></div></li>';
	var uploadError = '<i class="weui_icon_warn"></i>';
	var $uploadLi;
		
	$("#fileupload_input").fileupload({
	    formData : {
	    	taskid : taskId
		 	// 如果需要额外添加参数可以在这里添加
	 	},
	    url:contextPath+"/pm/task/addAttach",// 文件上传地址，当然也可以直接写在input的data-url属性内
	    dataType: 'json', 
	    start: function(e){
	    	$uploadLi = $(uploadLiTmp);
	    	$(".task-attachment").css("padding","10px");
	    	$(".task-attachment").find(".task-attachment-ul").append($uploadLi);
	    	PM.task.hideActionSheet($('#weui_actionsheet'), $('#mask'));
	    },
	 	success: function(data, status){
	 		$uploadLi.css("background-image","url(../../"+data.url.replace(new RegExp("\\\\","g"),"/")+")");
	 		$uploadLi.attr("_id",data.id);
	 		$uploadLi.attr("_url","../../"+data.url.replace(new RegExp("\\\\","g"),"/"));
	 		$uploadLi.addClass("task-attachment-pic");
	 		//$uploadLi.find("img").attr("src","../../"+data.url);
	 		if(location.hash=="#/tpl_task-create"){
	 			attachmentVal = $("#form-create-task-attachment").val();
	 		}else{
	 			attachmentVal = $("#form-edit-task-attachment").val();
	 		}
	 		
	 		if(attachmentVal!=""){
	 			var attachmentJson = JSON.parse(attachmentVal);
	 		}else{
	 			var attachmentJson = {};
	 		}
	 		
	 		attachmentJson[data.id] = {"id": data.id,"name":data.name,"url":data.url};
	 		//attachmentJson.push(attachmentJson[data.id]);
	 		
	 		if(location.hash=="#/tpl_task-create"){
	 			$("#form-create-task-attachment").val(JSON.stringify(attachmentJson));
	 		}else{
	 			$("#form-edit-task-attachment").val(JSON.stringify(attachmentJson));
	 		}
			upload_exist_init(taskId);
			$uploadLi.find(".task-attachment-li-content").remove();
			$uploadLi.removeClass("task-attachment-li-status");
			$.alert("上传成功！", "提示");
	    },
	    error: function (data, status, e){
	    	//$uploadLi.find(".task-attachment-li-content").append(uploadError);
	    	//showDialogWithMsg('ideaMsg','提示','文件错误！');  
	    	$uploadLi.find(".task-attachment-li-content").parent().remove();
	    	$.alert("文件错误！", "提示");
	    },
	    done:function(e,result){
	    	
	    	
	        console.log(JSON.stringify(result.result));
	    },
	    progressall: function (e, data) {
	    	
	        var progress = parseInt(data.loaded / data.total * 100, 10);
	        $uploadLi.find(".task-attachment-li-content").text(progress + '%');
	        //$('#progress .upload-jindu').css('width',progress + '%');
	    }
	});
}
//初始化已经上传的附件


function upload_exist_init(taskId){
	var taskId = $("#form-create-task-id").val();
	if(location.hash=="#/tpl_task-edit"){
		taskId = $("#form-edit-task-id").val();
	}
	var params = {};
	params.id = taskId;
	$.getJSON("../task/view.action",params,function(result){
		var data = eval( '(' + result.data.attachment + ')' );
		var keys = leng(data);
		var contents="";
		
		for(;keys.length>=1;){
			var key = keys.pop();
			var time = data[key].time
			if(time==undefined){
				contents= contents+"<p class='upload_exist_p' data-id="+key+"><div class='icoifile'></div><a class='upload_name_a' title='下载'>"+data[key].name+"</a></p>";
			}
		}
		$('#fileNames').html(contents);
		
		$('.upload_exist_p').on("click",".upload_delete_a",function(){
			var TaskId = $('#pm-task-name').attr("data-id");
			var key = $(this).parent().attr("data-id");
			
			params = {};
			params.id = taskId;
			params.key = key;
			$.getJSON("../task/deleteAttachment.action",params,function(result){
				upload_exist_init(TaskId);
			});
		});
		$('.upload_exist_p').on("click",".upload_name_a",function(){
			var TaskId = $('#pm-task-name').attr("data-id");
			var id = $(this).parent().attr("data-id");
			var params = {};
			params.taskid = taskId;
			params.id = id;
				var url = "../task/download.action?taskid="+taskId+"&id="+id ;
				window.open(url) ;
		});
		
	});
}

function upload_exist_initEdit(task){
		if(location.hash=="#task-create"){
			var $taskDiv = $(".task-create");
		}else{
			var $taskDiv = $(".task-edit");
		}
		
		var data = eval( '(' + task.attachment + ')' );
		var keys = leng(data);
		var contents="";
		for(;keys.length>=1;){
			var key = keys.pop();
			var time = data[key].time
			if(time==undefined){
				var name = data[key].name;
				if(name.indexOf(".jpg")>0){
					contents += '<img style="height:32px;width:32px;margin-left: 6px;margin-top:10px" src="'+contextPath+'/uploads/photo/'+name+'">';
				}else{
					contents= contents+"<p class='upload_exist_p' data-id="+key+"><div class='icoifile'></div><a class='upload_name_a' title='下载'>"+data[key].name+"</a></p>";
				}
			}else{
	        	 var path = data[key].name.replace(".amr",".mp3");
				 var vioceItem = $('<audio><source src='+contextPath+'/uploads/voice/'+path+' type="audio/mpeg" /></audio>');
				// $("#taskEdit-Record").css('display',"inline-block");
				 $("#playVoice-edit").append(vioceItem);
				 setTimeout(function(){
					var myAudio = $taskDiv.find("#sound-play-box").find("audio")[0];
					if(isNaN(myAudio.duration)){
						setTimeout(function(){
							if(!isNaN(myAudio.duration)){
								$taskDiv.find("#sound-play-box").find(".pm-edit-Record-time").text(parseInt(myAudio.duration));
							}
						},2000);
					}else{
						$taskDiv.find("#sound-play-box").find(".pm-edit-Record-time").text(parseInt(myAudio.duration));
					}
					
					$taskDiv.find("#sound-play-box").find("#playVoice-edit").click(function(e){
						 myAudio.play();
						 $taskDiv.find(".sound-play-ico").css({"visibility":"hidden","-webkit-animation":"sound-play-ico 1000ms steps(1) infinite","background-postion-x":"0px 0px"})
						 $(myAudio).on("ended",function(){
							 $taskDiv.find(".sound-play-ico").css({"visibility":"visible","-webkit-animation":"initial","background-postion-x":"-48px 0px"});
						 });
					});
				 },500);
				 
			}
		}
		$('#fileEdit').html(contents);
		
		$('.upload_exist_p').on("click",".upload_name_a",function(){
			var TaskId = $('#pm-task-name').attr("data-id");
			var id = $(this).parent().attr("data-id");
			var params = {};
			params.taskid = taskId;
			params.id = id;
				var url = "../task/download.action?taskid="+taskId+"&id="+id ;
				window.open(url) ;
		});
		
}


function leng(data){
	var jsonLength = 0;
	var a = [];
	for(var item in data){
		a.push(item);
	}
	return a;
}