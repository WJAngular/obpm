(function ($, window) {
	var applicationPath = window.ctx == "" ? "" : window.ctx;
	function S4() {
		return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
	}
 
    function initWebUpload(item, options) {
     
        if (!WebUploader.Uploader.support()) {
            var error = "上传控件不支持您的浏览器！请尝试升级flash版本或者使用Chrome引擎的浏览器。<a target='_blank' href='http://se.360.cn'>下载页面</a>";
            if (window.console) {
                window.console.log(error);
            }
            $(item).text(error);
            return;
        }
     
        var defaults = {
            hiddenInputId: "uploadifyHiddenInputId", // input hidden id
            onAllComplete: function (event) { }, // 当所有file都上传后执行的回调函数
            onComplete: function (event) { },// 每上传一个file的回调函数
            innerOptions: {},
            fileNumLimit: undefined,
            fileSizeLimit: undefined,
            fileSingleSizeLimit: undefined,
            accept :null,
            PostbackHold: false
        };
        var fileCount = 0;
        var opts = $.extend({}, defaults, options);
        var hdFileData = $("#" + opts.hiddenInputId);
  
        var target = $(item);//容器
        var pickerid = "";
        if (typeof guidGenerator36 != 'undefined')//给一个唯一ID
            pickerid = guidGenerator36();
        else
            pickerid = (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
  
        var uploaderStrdiv = '<div class="webuploader">' +
            '<div id="thelist" class="uploader-list"></div>' +
            '<div class="btns">' +
            '<div id="' + pickerid + '">选择文件</div>' +
            '<a id="ctlBtn" class="btn btn-default">开始上传</a>' +
            '</div>' +
        '</div>';
        target.append(uploaderStrdiv);
  
        var $list = target.find('#thelist'),
             $btn = target.find('#ctlBtn'),//这个留着，以便随时切换是否要手动上传
             state = 'pending',
             uploader;
  
        var jsonData = {
            fileList: []
        };
  
        var webuploaderoptions = $.extend({
            // swf文件路径
            swf: applicationPath + '/resource/js/webuploader/Uploader.swf',
  
            // 文件接收服务端。
            server: applicationPath + '/file/fileUpload.do?fileType='+opts.fileType,
  
            // 选择文件的按钮。可选。
            // 内部根据当前运行是创建，可能是input元素，也可能是flash.
            pick: '#' + pickerid,
  
            // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
            resize: false,
            fileNumLimit: opts.fileNumLimit,
            fileSizeLimit: opts.fileSizeLimit,
            accept:opts.accept,
            fileSingleSizeLimit: opts.fileSingleSizeLimit
        },
        opts.innerOptions);
        var uploader = WebUploader.create(webuploaderoptions);
  

		//回发时还原hiddenfiled的保持数据
		var fileDataStr = hdFileData.val();
		if (fileDataStr && opts.PostbackHold) {
			jsonData = JSON.parse(fileDataStr);
			$.each(jsonData.fileList, function(index, fileData) {
				var newid = S4();
				fileData.queueId = newid;
				$list.append('<div id="' + newid + '" class="item">' + '<div class="info">' + fileData.name + '</div>' + '<div class="state">已上传</div>' + '<div class="del"></div>' + '</div>');
			});
			hdFileData.val(JSON.stringify(jsonData));
		} 
		uploader.on('fileQueued', function (file) {//队列事件
            $list.append('<div id="' + file.id + '" class="item">' +
                '<div class="info">' + file.name + '<span data-fileId="'+file.id+'" class="delFile">删除</span></div>' +
                '<div class="state">等待上传...</div>' +
            '</div>');
        });
        uploader.on('uploadProgress', function (file, percentage) {//进度条事件
            var $li = target.find('#' + file.id),
                $percent = $li.find('.progress .bar');
            // 避免重复创建
            if (!$percent.length) {
                $percent = $('<span class="progress">' +
                    '<span  class="percentage"><span class="text"></span>' +
                  '<span class="bar" role="progressbar" style="width: 0%">' +
                  '</span></span>' +
                '</span>').appendTo($li).find('.bar');
            }
  
            $li.find('div.state').text('上传中');
            $li.find(".text").text(Math.round(percentage * 100) + '%');
            $percent.css('width', percentage * 100 + '%');
        });
        uploader.on('uploadSuccess', function (file, response) {//上传成功事件
            target.find('#' + file.id).find('div.state').text('已上传');
            var fileEvent = {
                queueId: file.id,
                name: file.name,
                size: file.size,
                type: file.type,
                filePath: response.filePath,
                fileName: response.fileName
            };
            jsonData.fileList.push(fileEvent);
            opts.onComplete(fileEvent);
  
        });
  
        uploader.on('uploadError', function (file) {
            target.find('#' + file.id).find('div.state').text('上传出错,文件超出大小');
        });
  
        uploader.on('uploadComplete', function (file) {//文件上传完成事件
            target.find('#' + file.id).find('.progress').fadeOut();
            var fp = $("#" + opts.hiddenInputId);
            fp.val(JSON.stringify(jsonData));
            if(jsonData.fileList.length==fileCount){
            	opts.onAllComplete(jsonData.fileList);
            	fileCount=0;
            }
            
        });
  
        uploader.on('fileQueued', function (file) {
            //uploader.upload();
        	fileCount++;
        });
        uploader.on('fileDequeued',function( file ) {
            fileCount--;
        });
        
  
        uploader.on('all', function (type) {
            if (type === 'startUpload') {
                state = 'uploading';
            } else if (type === 'stopUpload') {
                state = 'paused';
            } else if (type === 'uploadFinished') {
                state = 'done';
            }
  
            if (state === 'uploading') {
                $btn.text('暂停上传');
            } else {
                $btn.text('开始上传');
            }
        });
  
        $btn.on('click', function () {
            if (state === 'uploading') {
                uploader.stop();
            } else {
                uploader.upload();
            }
        });
        //删除
         $list.on("click", ".delFile", function () {
         	 var $ele = $(this);
            var id = $ele.parent().parent().attr("id");
            var deletefile = {};
            uploader.removeFile(uploader.getFile(id));//不要遗漏
            $ele.parent().parent().remove();
            return;
         });
        $list.on("click", ".del", function () {
            var $ele = $(this);
            var id = $ele.parent().attr("id");
            var deletefile = {};
            $.each(jsonData.fileList, function (index, item) {
                if (item && item.queueId === id) {
                	uploader.removeFile(uploader.getFile(id));//不要遗漏
                    deletefile = jsonData.fileList.splice(index, 1)[0];
                    $("#" + opts.hiddenInputId).val(JSON.stringify(jsonData));
                    $.post(applicationi + "/Webploader/Delete", {  'filepathname': deletefile.filePath }, function (returndata) {
                        $ele.parent().remove();
                    });
                    return;
                }
            });
        });
  
    }
  
  
    $.fn.powerWebUpload = function (options) {
        var ele = this;
        if (typeof PowerJs != 'undefined') {
            $.lazyLoad(applicationPath + "/resource/js/webuploader/webuploader.css", function () { }, 'css');
            $.lazyLoad(applicationPath + "/resource/js/webuploader/webuploader.min.js", function () {
                initWebUpload(ele, options);
            });
        }
        else {
            initWebUpload(ele, options);
        }
    };
})(jQuery, window);