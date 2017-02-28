(function($) {
	var isNoDeleteFile = true;
	var isReloadFile = false;
	map = {};

	function FMFileInfo(file) {
		var webIndex = file.path.lastIndexOf("/");
		var urlIndex = file.path.lastIndexOf("_/uploads");

		this.webPath = file.path;
		if (urlIndex >= 0) {
			this.webPath = file.path.substring(urlIndex + 1);
		}

		this.realName = file.path.substring(webIndex + 1, file.path.length);
		this.showName = file.name;
		this.url = contextPath + this.webPath;
	}

	function File(name, path) {
		this.name = name;
		this.path = path;
	}

	function refreshUploadListSub(fileFullName, uploadListId, readonly,
			refresh, applicationid, opentype, subGridView, disabled) {
		if (fileFullName == "clear") {
			jQuery("#" + uploadListId).html(''); // 清空显示值
			// map[uploadListId] = "";
			jQuery("#" + uploadListId.substring(uploadListId.indexOf("_") + 1))
					.val("");
		} else {
			if (fileFullName != "") {
				var $fileContent = jQuery("#" + uploadListId);
				$fileContent.empty();
				$fileContent.append(getDivContent(fileFullName, uploadListId,
						readonly, refresh, applicationid, opentype,
						subGridView, disabled));
			} else {
				jQuery("#" + uploadListId).html('');
				jQuery(
						"#"
								+ uploadListId.substring(uploadListId
										.indexOf("_") + 1)).val("");
				// map[uploadListId] = '';
			}
		}
		// isNoDeleteFile = true;
		// isReloadFile = false;
	}

	// 文件附件
	function refreshUploadList(fileFullName, uploadListId, readonly, refresh,
			applicationid, opentype, subGridView, disabled) {
		refreshUploadListSub(fileFullName, uploadListId, readonly, refresh,
				applicationid, opentype, subGridView, disabled);
		if (refresh != 'false') {
			if (subGridView) {
				dy_view_refresh(uploadListId);
			} else {
				window.setTimeout("dy_refresh('"
						+ uploadListId.substring(uploadListId.indexOf("_") + 1)
						+ "')", 500);
			}
		}
	}

	// 显示文件列表
	function showUploadPic(index, id, webPath, applicationid) {
		var $picDiv = jQuery("#" + id + "pic" + index);
		if ($picDiv.size() != 0) {
			var url = encodeURI(encodeURI(contextPath
					+ "/portal/upload/fileInfor.action?applicationid="
					+ applicationid + "&fileFullName=" + webPath));
			jQuery.post(url, function(x) {
				// 提交成功回调
					var oldTitle = $picDiv.attr("title");
					$picDiv.attr("title", oldTitle + x);
				});
		}
	}

	// 获得显示列表
	function getDivContent(fileFullName, uploadListId, readonly, refresh,
			applicationid, opentype, subGridView, disabled) {
		var files = JSON.parse(HTMLDencode(fileFullName));
		var divContent = '';
		divContent += '<div class="hidepic" id="' + uploadListId + 'showFileDiv"' + 'style="background:#fff;width:text-align:left;left:0px;top:17px;"></div>';

		var tohtml = "";
		for ( var i = 0; i < files.length; i++) {
			var info2 = new FMFileInfo(files[i]);
			tohtml += '<span class="showOptions" style="display:inline-block;position:relative;z-index:1;margin:2px;width:155px;">';

			var flag = false;// 判断文档类型是否为OFFIC文档

			// 获取文件类型 根据类型给文件赋予前置图标
			var fileType = info2.showName.substring(
					info2.showName.lastIndexOf(".")).toLowerCase();
			if (fileType == ".doc" || fileType == ".docx") {
				tohtml += '<img  border="0" src="' +contextPath+ '/resource/image/word.gif"/>';
			} else if (fileType == ".xls" || fileType == ".xlsx") {
				tohtml += '<img  border="0" src="' + contextPath + '/resource/image/excel.gif"/>';
			} else if (fileType == ".pdf") {
				tohtml += '<img  border="0" src="' +contextPath+ '/resource/image/pdf.gif"/>';
			} else if (fileType == ".jpg" || fileType == ".png"
					|| fileType == ".jpeg" || fileType == ".gif") {
				tohtml += '<img  border="0" src="' +contextPath+ '/resource/image/image.gif"/>';
			} else {
				tohtml += '<img  border="0" src="' +contextPath+ '/resource/image/empty.gif"/>';
			}
			//可在线预览的文档
			if (fileType == ".doc" || fileType == ".docx" || fileType == ".xls"
					|| fileType == ".xlsx" || fileType == ".pdf"
					|| fileType == ".txt" || fileType == ".rtf"
					|| fileType == ".et" || fileType == ".ppt"
					|| fileType == ".pptx"
					|| fileType == ".dps" || fileType == ".pot"
					|| fileType == ".pps" || fileType == ".wps"
					|| fileType == ".html" || fileType == ".htm") {
				DWREngine.setAsync(false);
				FormHelper.hasSwfFile(info2.webPath, info2.realName, function(
						result) {
					flag = result;
				});
				DWREngine.setAsync(true);
			}

			var title = info2.showName;
			var fileType = title.substring(title.lastIndexOf("."))
					.toLowerCase();
			var fileName = title.substring(0, title.lastIndexOf("."));
			if (fileName != null && fileName.length > 8) {
				title = fileName.substring(0, 7) + ".."
						+ fileName.charAt(fileName.length - 1) + fileType;
			}

			tohtml += '<a type="show" style="cursor:pointer;">' + title + '</a>';

			tohtml += '<div class="showdiv" style="position:absolute;display:none">';
	//		tohtml += '<div style="text-align:center;margin:0px;padding:0px;"><img style="margin:0px;padding:0px;" src="data:image/png;base64,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"/></div>';
			tohtml += '<div style="border:1px solid  rgb(255, 153, 0);white-space:nowrap;background:#FFFFFF;padding:5px">';

			// true为打开office预览 false为普通预览
			if (flag) {
				tohtml += '<span type="showOffice" style="cursor:pointer;margin:2px;font-size:12px;" onselectstart="return false;">预览</span>';
			} else {
				if (fileType == ".jpg" || fileType == ".png"
						|| fileType == ".jpeg" || fileType == ".gif"
						|| fileType == ".ceb") {
					tohtml += '<span type="showPreview" style="cursor:pointer;margin:2px;font-size:12px;" onselectstart="return false;"><a href="#" url="' + files[i].path + '" >预览</a></span>';
				}
			}
			tohtml += '<span type="showDownLoad" style="cursor:pointer;margin:2px;font-size:12px" onselectstart="return false;">下载</span>';
			if (!readonly) {
				tohtml += '<span type="showDelete" style="cursor:pointer;margin:2px;font-size:12px" onselectstart="return false;">删除</span>';
			}

			tohtml += '</div>';
			tohtml += '</div>';
			tohtml += '</span>';
		}

		var $fileContent = jQuery("#" + uploadListId).html(divContent);

		$fileContent.find('.hidepic').html(tohtml);
		var timeout1 = "";

		$fileContent.find(".showOptions").each(function(index) {
			$(this).bind("click mouseover",function() {
				$(".showOptions").css("z-index", "1");
				var $div = $(this).css("z-index", "100").children(".showdiv");
				var aaW = $(this).width();
				var zzW = $div.width();
				var aaH = $(this).height();
				var left = (aaW - zzW) / 2;
				if(subGridView){
					$(this).mousemove(function(e){
						if($div.is(":hidden")){
							var mx=e.pageX;
							var my=e.pageY;
							$div.css({
								"position":"fixed",
								"top":my,
								"left":mx
							})
						}
					}); 
				}else{
					$div.css("top", aaH - 10);
					$div.css("left", left);
				}
				timeout1 = setTimeout(function(){
					$div.stop().show();
				},300);
				
				
			}).bind("mouseout",function(){
				window.clearTimeout(timeout1);
			});
		});

		$fileContent.find(".showdiv").each(function(index) {
			$(this).mouseover(function() {
				$(this).stop().show();
			});
			$(this).mouseout(function() {
				$(this).fadeOut(300);
			});
			$(this).find("span").each(function(index2) {
				$(this).mousedown(function(e) {
					$(this).css("background","#efe4b3");
					var type = $(this).attr("type");
					var file = new FMFileInfo(files[index]);
					if (type == "showPreview") {
						var $preview = $(this).find("a");
						var url = file.webPath;
						var showName = file.showName;
						var webPath = contextPath + url;
						var fileType = showName.substring(showName.lastIndexOf(".")).toLowerCase();
						if (fileType == ".png" || fileType == ".jpg"
						 || fileType == ".gif" || fileType == ".jpeg") {
							showImageEffect(webPath);
						} else if (fileType == ".ceb") {
							previewInNewTab(webPath,showName);
						} else {
							$preview.target = "_blank";
							$preview.href = webPath;
							$preview.triggerHandler("click");
						}
					} else if (type == "showDownLoad") {
						var showName = file.showName;
						var webPath = file.webPath;
						downloadFile(showName,webPath);
					} else if (type == "showDelete") {
						deleteOneFile(index,uploadListId,refresh,applicationid,opentype,subGridView,disabled);
					} else if (type == "showOffice") {// 在线预览OFFIC文档
						previewFile(file.showName,file.webPath);
					}
					
					$(this).css("background","#ffffff");
					$(this).parent().parent().hide();
				});
			});
		});

		$fileContent.find("a[type='deleteOne']").each(
				function(index) {
					$(this).click(
							function() {
								deleteOneFile(index, uploadListId, refresh,
										applicationid, opentype, subGridView,
										disabled);
							});
				});
		$fileContent.find("a[type='showpic']").each(
				function(index) {
					showUploadPic(index, uploadListId, files[index].path,
							applicationid);
				});

		$fileContent.find("a[type='showword']").each(function(index) {
			$(this).click(function() {
				var showName = $(this).attr("showName");
				var url = $(this).attr("url");
				showFileDialog(this, showName, url, readonly);
			});
		});

		// 当打开方式为弹出层时
		$fileContent.find(".showpic1").each(function() {
			$(this).click(function() {
				var showName = $(this).attr("showName");
				var url = $(this).attr("url");
				showFileDialog(this, showName, url, readonly);
			});
		});

		var time;

		$fileContent.mouseover(function() {
			if (time) {
				window.clearTimeout(time);
			}
		});

		divContent = "";

		//设置点击事件，点击时去除弹出框
		$(document).bind("mousedown",function(event){
			$("div.showdiv").fadeOut(300);
		});
	}

	/**
	 * 文件预览
	 */
	function previewFile(fileName, path) {
		var url = encodeURI(encodeURI(contextPath
				+ "/portal/share/component/upload/preview.jsp?fileName="
				+ fileName + "&path=" + path));
		var _tmpwin = window.open(url, "_blank");
		_tmpwin.location.href = url;
	}

	/**
	 * 获取文字长度
	 */
	function GetCurrentStrWidth(text) {
		var currentObj = $('<pre>').hide().appendTo(document.body);
		$(currentObj).html(text);
		var width = currentObj.width();
		currentObj.remove();
		return width;
	}

	// 删除一个文件
	function deleteOneFile(index, id, refresh, applicationid, opentype,
			subGridView, disabled) {
		if (confirm("你确定删除当前文件吗？此操作不可恢复！")) {
			var files = [];
			var filefullname = "";
			var oldstr = jQuery("#" + id.substring(id.indexOf("_") + 1)).val();// 用于恢复数据

			var oField = jQuery("#" + id.substring(id.indexOf("_") + 1));
			files = JSON.parse(oField.val());
			var webpath = files[index].path;
			files.splice(index, 1);
			if (files.length > 0) {
				filefullname = JSON.stringify(files);
			}

			oField.val(filefullname);

			jQuery
					.ajax( {
						type : 'POST',
						async : false,
						url : encodeURI(encodeURI(contextPath
								+ "/portal/upload/deleteOne.action?fileFullName="
								+ webpath)),
						dataType : 'text',
						data : jQuery("#document_content").serialize(),
						success : function(x) {
							refreshUploadList(filefullname, id, false, refresh,
									applicationid, opentype, subGridView,
									disabled);
							filefullname = "";
						},
						error : function(x) {
							jQuery("#" + id.substring(id.indexOf("_") + 1))
									.val(oldstr);
						}
					});

		}
	}

	function refreshImgListSub(fileFullName, uploadListId, myheigh, mywidth,
			readonly, refresh, applicationid, opentype, subGridView) {
		if (jQuery.trim(fileFullName) != "" && fileFullName != "clear") {
			var image = JSON.parse(fileFullName);
			
			for(var i = 0; i < image.length; i++){
				fileFullName = image[i].path;
				var divContent = '';

				var urlIndex = fileFullName.lastIndexOf("_/uploads");
				var url = fileFullName;
				if (urlIndex >= 0) {
					url = fileFullName.substring(urlIndex + 1);
				}
				
				
				
				divContent += '<div data-name="'+image[i].name+'" style="display: inline-block;position: relative;margin-right:5px">';
				divContent += '<div>';
				if (opentype == "dialog") {
					divContent += '<a id="'
							+ uploadListId
							+ 'pic0" href="#" class="showhidefilepic" '
							+ ' rel="lightbox" title="'
							+ fileFullName
									.substring(fileFullName.lastIndexOf("/") + 1)
							+ '; ">';
				} else {
					divContent += '<a id="'
							+ uploadListId
							+ 'pic0" href="#" href="'
							+ url
							+ '"  rel="lightbox" title="'
							+ fileFullName
									.substring(fileFullName.lastIndexOf("/") + 1)
							+ '" target="_blank">';
				}
				if (fileFullName.indexOf("pdf") == -1) {
					divContent += '<img  src="../../..' + url + '" width='
							+ mywidth + ' height=' + myheigh + ' border="0" '
							+ '/>';
				} else {
					divContent += '<font size=2 color=red>' + fileFullName
							.substring(fileFullName.lastIndexOf("/") + 1) + '</font>';
				}
				divContent += '</a>';
				divContent += '</div>';
				
				divContent += "<div class='upbtns-panel' " +
					"style='display:none;position: absolute;top: 0px;right: 0px;font-size: 16px;font-weight: bold;" +
					"padding: 5px;background: rgb(255, 255, 255);cursor: pointer;'>×</div>";
				divContent += '</div>';

				if(jQuery("#" + uploadListId).find("[data-name='"+image[i].name+"']").size()<=0){
					
					jQuery("#" + uploadListId).append(divContent);
					
					var $divContent = jQuery("#" + uploadListId).find("[data-name='"+image[i].name+"']");
					
					$divContent.on( 'mouseenter', function() {
						$(this).find(".upbtns-panel").show();
				    });
					$divContent.on( 'mouseleave', function() {
						$(this).find(".upbtns-panel").hide();
				    });
					$divContent.find(".upbtns-panel").click(function(){
						if(confirm("确定删除？")){
							var _id = $(this).parents(".upload-pic-box").data("id");
							var _name = $(this).parent().data("name");
							var _images = JSON.parse($("#" + _id).val());
							$(this).parent().remove();
							for(var i = 0; i < _images.length; i++){
								if(_images[i].name == _name){
									_images.splice(i,1)
								}
							}
							$("#" + _id).val(JSON.stringify(_images));
						}
					});

					$divContent.find(".showhidefilepic").click(
							function() {
								showFileDialog(this, fileFullName
										.substring(fileFullName.lastIndexOf("/") + 1),
										url, readonly);
							});

					$divContent.find(".showhidefilepic").each(function() {
						showUploadPic(0, uploadListId, fileFullName, applicationid);
					});
				}
				
				/*$content = jQuery("#" + uploadListId).html(divContent);
				$content.find(".showhidefilepic").click(
						function() {
							showFileDialog(this, fileFullName
									.substring(fileFullName.lastIndexOf("/") + 1),
									url, readonly);
						});

				$content.find(".showhidefilepic").each(function() {
					showUploadPic(0, uploadListId, fileFullName, applicationid);
				});*/
			}
			
			

		} else {
			jQuery("#" + uploadListId).html('');
		}
	}

	// refresh iamge uploaded list
	function refreshImgList(fileFullName, uploadListId, myheigh, mywidth,
			readonly, refresh, applicationid, opentype, subGridView) {
		refreshImgListSub(fileFullName, uploadListId, myheigh, mywidth,
				readonly, refresh, applicationid, opentype, subGridView);
		if (refresh != 'false') {
			if (subGridView) {
				dy_view_refresh(uploadListId);
			} else {
				window.setTimeout("dy_refresh('"
						+ uploadListId.substring(uploadListId.indexOf("_") + 1)
						+ "')", 500);
			}
		}
	}

	/**
	 * 其他类别显示
	 */
	function showTextDialog(webPath,name) {
		OBPM.dialog.show( {
			opener : window.parent.parent,
			width : 1000,
			height : 580,
			url : webPath,
			args : {},
			title : name,
			close : function(result) {

			}
		});
	}
	
	function previewInNewTab(webPath){
		var _tmpwin = window.open(webPath, "_blank");
		_tmpwin.location.href = webPath;
	}

	// 弹出层来加载文件为了处理乱码问题
	function showFileDialog(obj, name, webPath, readonly) {
		var url = "";
		var fileType = name.substring(name.lastIndexOf(".")).toLowerCase();
		if (fileType == ".doc" || fileType == ".docx" || fileType == ".xls"
				|| fileType == ".xlsx") {
			url = contextPath + '/portal/dynaform/document/doViewWordFile.action';
			OBPM.dialog.show( {
				opener : window.parent.parent,
				width : 1000,
				height : 550,
				url : url,
				args : {
					"webPath" : contextPath + webPath,
					"readonly" : readonly
				},
				title : name,
				close : function(result) {

				}
			});
		} else {
			url = contextPath + webPath;
			obj.target = "_blank";
			obj.href = url;
			obj.triggerHandler("click");
		}
	}

	/**
	 * 图片预览窗口
	 * 
	 * url 图片路径
	 */
	function showImageEffect(url) {
		// 构造外围窗体 将窗体改为隐藏
		$("body").append('<div id="showImageDialog"></div>');
		var $div = $("#showImageDialog");
		$div.css( {
			"display" : "none",
			"position" : "fixed",
			"z-index" : "999",
			"top" : "0px",
			"right" : "0px",
			"bottom" : "0px",
			"left" : "0px"
		});
		$div
				.append('<div style="filter:alpha(opacity=70);-moz-opacity:0.7;-khtml-opacity: 0.7;position: fixed; z-index: 101; top: 0px; right: 0px; bottom: 0px; left: 0px; opacity: 0.7; background-color: rgb(0, 0, 0);"></div>');

		// 添加关闭按钮
		$div
				.append('<div id="imageClose" style="color: rgb(255, 255, 255); padding: 4px 10px ;  background-color: rgb(0, 0, 0);cursor: pointer;right: 0px;position: absolute;z-index:104;font-size:20px;">x</div>');
		// 添加图片外围div
		var imgdiv = '<div style="position: absolute; width: 300%; height: 100%; left: -100%;">';
		// 添加图片
		imgdiv += '<img style="position: absolute; z-index: 102; margin: auto; width: auto; top: 0px; right: 0px; bottom: 0px; left: 0px; display: block; cursor: pointer; max-width: 32%; max-height: 95%;" src="' + url + '"/>';
		imgdiv += "</div>";
		$div.append(imgdiv);

		// 鼠标滑进关闭按钮样式
		$("#imageClose").mouseover(function(e) {
			$("#imageClose").css("background-color", "rgb(55, 55, 55)");
		});
		$("#imageClose").mouseout(function(e) {
			$("#imageClose").css("background-color", "rgb(0, 0, 0)");
		});

		// 点击后淡出然后remove
		$("#imageClose").click(function(e) {
			$("#showImageDialog").fadeOut(50, function() {
				$div.remove();
			});
		});

		// 淡入显示
		$("#showImageDialog").fadeIn(50);
	}

	/**
	 * 前台删除文件
	 * 
	 * @param {}
	 *            valueField 文件路径保存字段
	 * @param {}
	 *            uploadListId
	 */
	function deleteFrontFile(valueField, uploadListId, applicationid) {
		// 删除URL
		if (confirm("你确定删除全部文件吗？此操作不可恢复！")) {
			var fileFullName = "";
			if (valueField.val() != '') {
				var files = JSON.parse(valueField.val());
				for ( var i = 0; i < files.length; i++) {
					fileFullName += files[i].path + ";";
				}
				if (fileFullName.length > 1) {
					fileFullName = fileFullName.substring(0,
							fileFullName.length - 1);
				}
			}
			this.url = contextPath
					+ "/portal/upload/delete.action?fileFullName="
					+ fileFullName;
			fileFullName = "";
			deleteFileCommon(valueField, uploadListId, url);
		}
	}

	/**
	 * 后台删除文件
	 * 
	 * @param {}
	 *            valueField 文件路径保存字段
	 * @param {}
	 *            uploadListId
	 */
	function deleteFile(valueField, uploadListId, applicationid) {
		// 删除URL
		this.url = contextPath + "/core/upload/delete.action?applicationid="
				+ applicationid + "&fileFullName=" + valueField.val();
		deleteFileCommon(valueField, uploadListId, url);
	}

	/**
	 * 前后台共有的删除文件方法
	 * 
	 * @param {}
	 *            valueField 文件路径保存字段
	 * @param {}
	 *            uploadListId
	 * @param {}
	 *            url 删除文件请求的路径
	 */
	function deleteFileCommon(valueField, uploadListId, URL) {
		// 保存原来的值用于删除发生异常时可以恢复数据
		var uploadListIdHTML = jQuery("#" + uploadListId).html();
		var uploadListIdValue = jQuery(
				"#" + uploadListId.substring(uploadListId.indexOf("_") + 1))
				.val();

		jQuery("#" + uploadListId).html(''); // 清空显示值
		jQuery("#" + uploadListId.substring(uploadListId.indexOf("_") + 1))
				.val("");

		jQuery.ajax( {
			type : 'POST',
			async : false,
			url : encodeURI(url),
			dataType : 'text',
			data : jQuery("#document_content").serialize(),
			success : function(x) {
			},
			error : function(x) {
				// 恢复数据
			jQuery("#" + uploadListId).html(uploadListIdHTML);
			jQuery("#" + uploadListId.substring(uploadListId.indexOf("_") + 1))
					.val(uploadListIdValue);
		}
		});
		// jQuery("#" + uploadListId).html(''); // 清空显示值
		// valueField.val(''); // 清空值
		// map[uploadListId] = "";

	}

	/**
	 * 后台文件上传
	 * 
	 * @param {}
	 *            pathname 文件目录
	 * @param {}
	 *            pathFieldId 文件路径域ID
	 * @param {}
	 *            viewid 视图ID
	 * @param {}
	 *            allowedTypes 允许上传的类型
	 * @param {}
	 *            maximumSize 最大值
	 * @param {}
	 *            fileSaveMode 文件保存模式
	 * @return {} 文件网络路径
	 */
	function uploadFile(pathname, pathFieldId, viewid, allowedTypes,
			maximumSize, layer, fileSaveMode, applicationid) {
		var url = contextPath + '/core/upload/upload.jsp?path=' + pathname
				+ "&applicationid=" + applicationid;

		var oField = jQuery("#" + pathFieldId);
		OBPM.dialog.show( {
			opener : window.parent.parent,
			width : 800,
			height : 450,
			url : url,
			args : {},
			title : '文件上传',
			close : function(result) {
				if (result == null || result == undefined
						|| result == "undefined") {

				} else {
					if (oField != null) {
						oField.val(result);
					}
					return resutl;
				}
			}
		});
	}

	/**
	 * 前台文件上传
	 * 
	 * @param {}
	 *            pathname 文件目录
	 * @param {}
	 *            pathFieldId 文件路径域ID
	 * @param {}
	 *            viewid 视图ID
	 * @param {}
	 *            allowedTypes 允许上传的类型
	 * @param {}
	 *            maximumSize 最大值
	 * 
	 * @param {}
	 *            fileSaveMode 文件保存模式
	 * @param {}
	 *            callback 回调函数
	 * @return {} 文件网络路径
	 */
	function uploadFrontFile(title, pathname, pathFieldId, viewid,
			allowedTypes, maximumSize, fileSaveMode, callback, applicationid,
			limitNumber, fileType, customizeType) {
		hiddenDocumentFieldIncludeIframe();// in util.js
		var url = contextPath
				+ '/portal/share/component/upload/upload.jsp?path=' + pathname;
		url = addParameters(pathname, pathFieldId, viewid, allowedTypes,
				maximumSize, fileSaveMode, url);
		url += '&applicationid=' + applicationid;
		url += '&limitNumber=' + limitNumber;
		url += '&fileType=' + fileType;
		url += '&customizeType=' + customizeType;
		OBPM.dialog.show( {
			width : 700,
			height : 450,
			url : url,
			args : {
				"webPath" : "aaaaaaaa",
				"readonly" : "222222"
			},
			title : title,
			close : function(result) {
				showDocumentFieldIncludeIframe();// //in util.js
			var oField = jQuery("#" + pathFieldId);
			var rtn = getReturnValue(oField, result);
			// 把上传的文件json格式 [{'name':'aaa.doc','path':'XXX'}] 放进文件上传控件的value
			var files = [];
			if (isReloadFile) {
				var fileStrs = result.split(";");
				if (oField && oField.val() && allowedTypes != 'image') {
					files = JSON.parse(oField.val());
				}
				for ( var i = 0; i < fileStrs.length; i++) {
					var fileStr = fileStrs[i].split(",");
					var file = new File(fileStr[0], fileStr[1]);
					files.push(file);
				}
			}
			if (files.length > 0) {
				rtn = JSON.stringify(files);
			}
			oField.val(rtn);
			if (callback && typeof (callback) == "function") {
				callback();
			}
		}
		});
	}

	/**
	 * 简单上传
	 * 
	 * @param {}
	 *            pathname 文件存放路径名称
	 * @param {}
	 *            pathFieldId 文件路径域ID
	 * @param {}
	 *            callback 回调函数
	 */
	function uploadFrontSimple(pathname, pathFieldId, applicationid) {
		var url = contextPath
				+ '/portal/share/component/upload/upload.jsp?path=' + pathname
				+ '&applicationid=' + applicationid;
		var oField = jQuery("#" + pathFieldId);
		OBPM.dialog.show( {
			opener : window.parent.parent,
			width : 800,
			height : 450,
			url : url,
			args : {},
			title : 'upload',
			close : function(result) {
				if (result == null || result == undefined
						|| result == "undefined" || result == "clear") {
					oField.val('');
				} else {
					oField.val(result);
				}

			}
		});
		/*
		 * showfrontframe({ title : "upload", url : url, w : 650, h : 500,
		 * callback : callback });
		 */
	}

	/**
	 * 为URL添加参数
	 * 
	 * @param {}
	 *            pathname
	 * @param {}
	 *            pathFieldId
	 * @param {}
	 *            viewid
	 * @param {}
	 *            allowedTypes
	 * @param {}
	 *            maximumSize
	 * @param {}
	 *            fileSaveMode
	 * @param {}
	 *            url
	 * @return {}
	 */
	function addParameters(pathname, pathFieldId, viewid, allowedTypes,
			maximumSize, fileSaveMode, url) {
		var oField = jQuery("#" + pathFieldId);
		var oView = jQuery("#" + viewid);

		if (allowedTypes) {
			url += '&allowedTypes=' + allowedTypes;
		}

		if (maximumSize) {
			url += '&maximumSize=' + maximumSize;
		}

		if (fileSaveMode) {
			// 自定义
			url += '&fileSaveMode=' + fileSaveMode;
		} else {
			// 系统
			url += '&fileSaveMode=' + 00;
		}

		return url;
	}

	/**
	 * 获取返回值
	 * 
	 * @param {}
	 *            oField
	 * @return {}
	 */
	function getReturnValue(oField, rtn) {
		if (rtn == null || rtn == 'undefined') {
			isReloadFile = false;
			if (oField && oField.val()) {
				rtn = oField.val();
			} else {
				rtn = '';
			}
		} else {
			isReloadFile = true;
		}
		return rtn;
	}

	$.fn.obpmUploadField = function() {
		return this.each(function() {
			var $field = jQuery(this);
			var id = $field.attr("id");
			var disabled = $field.attr("disabled");
			var readonly = (disabled == 'disabled');
			var callbakFunction = $field.attr("callbakFunction");
			var path = $field.attr("path");
			var fileSaveMode = $field.attr("fileSaveMode");
			var application = $field.attr("application");
			var value = $field.attr("value");
			var name = $field.attr("name");
			var maxsize = $field.attr("maxsize");
			var uploadList = $field.attr("uploadList");
			var text = $field.html();
			var refreshOnChanged = $field.attr("refreshOnChanged");

			var uploadLabel = $field.attr("uploadLabel");
			var filelabel = $field.attr("filelabel");
			var deleteLabel = $field.attr("deleteLabel");
			var limitsize = $field.attr("limitsize");
			var limitNumber = $field.attr("limitNumber");
			var tagName = $field.attr("tagName");
			var fileType = $field.attr("fileType");
			var customizeType = $field.attr("customizeType");
			var limitType = $field.attr("limitType");
			var opentype = $field.attr("opentype");

			var imgHeight = $field.attr("imgHeight");
			var imgWidth = $field.attr("imgWidth");

			var subGridView = $field.attr("subGridView");
			subGridView = (subGridView == 'true');

			var html = "";
			var $tableHtml = $("<table></table>");
			var $trHtml = $("<tr></tr>");

			// td1
				var $td1Html = $("<td  class='upload-pic-box' data-id='"+id+"' style='border:0;white-space:nowrap' ></td>");

				text = text ? text : "";
				value = value ? value : "";
				if (subGridView) {
					var divHtml = "<div name='" + name + "_gridView' value='"
							+ value + "' fieldType='" + tagName
							+ "' style='display:none;'>" + text + "</div>";
					var $divHtml = $(divHtml);
					$divHtml.appendTo($td1Html);
				}
				// image
				if (limitType == "image") {
					var td2Html = "<div style='border:0' id='" + uploadList
							+ "' GridType='uploadFile'></div>";
					var $td2Html = $(td2Html);
					$td2Html.appendTo($td1Html);
				} else {
					var td2Html = "<td style='border:0'><div  id='"
							+ uploadList
							+ "' GridType='uploadFile'></div></td>";
					var $td2Html = $(td2Html);
					$trHtml.append($td2Html);
				}
				// inputHidden
				var inputHtml = "<input type='hidden' name='" + name
						+ "' value='" + value + "' fieldType='" + tagName
						+ "' id='" + id + "'/>";
				var $inputHtml = $(inputHtml);
				if($field.attr("moduleType") != "uploadFileRefresh"){
					$field.before($inputHtml);
				}
				
				if(!readonly){//只读状态下不显示上传和下载按钮
					// uploadinput
					var uploadHtml = "<input class='uploadinput' type='button'  value='"
							+ uploadLabel + "' name='btnSelectDept'";
					if (disabled && disabled != '') {
						uploadHtml += " disabled='" + disabled + "' ";
					}
					uploadHtml += "/>";
					var $uploadHtml = $(uploadHtml);
					$uploadHtml.bind(
							"click",
							function() {
								uploadFrontFile(filelabel + uploadLabel, path, id,
										'_viewid', limitType, maxsize,
										fileSaveMode, _callback, application,
										limitNumber, fileType, customizeType);
							}).appendTo($td1Html);
	
					// deleteinput
					var delHtml = "<input class='uploaddelete' type='button' name='btnDelete'  value='"
							+ deleteLabel + "'";
					if (disabled && disabled != '') {
						delHtml += " disabled='" + disabled + "' ";
					}
					delHtml += "/>";
					var $delHtml = $(delHtml);
					$delHtml.bind("click", function() {
	
						deleteFrontFile(jQuery("#" + id), uploadList, application);
					}).appendTo($td1Html);
				}

				// 描述
				$trHtml.append($td1Html).appendTo($tableHtml);
				if($field.attr("moduleType") != "uploadFileRefresh"){
					$field.attr("moduleType","uploadFileRefresh")
						.css("display","none");
				}
				$field.after($tableHtml);

				var _callback = function() {

					if (limitType == 'image') {
						refreshImgList(jQuery("#" + id).val(), uploadList,
								imgHeight, imgWidth, readonly,
								refreshOnChanged, application, opentype,
								subGridView);
					} else {
						refreshUploadList(jQuery("#" + id).val(), uploadList,
								readonly, refreshOnChanged, application,
								opentype, subGridView, disabled);
					}
				}, init = function() {
					if (limitType == 'image') {
						refreshImgListSub(jQuery("#" + id).val(), uploadList,
								imgHeight, imgWidth, readonly,
								refreshOnChanged, application, opentype,
								subGridView);
					} else {
						refreshUploadListSub(jQuery("#" + id).val(),
								uploadList, readonly, refreshOnChanged,
								application, opentype, subGridView, disabled);
					}
				};
				init();
			});
	};
	$.fn.obpmViewImageUpload = function() {
		return this
				.each(function() {
					var $field = jQuery(this);
					var imgw = $field.attr("imgw");
					var imgh = $field.attr("imgh");
					var viewReadOnly = $field.attr("viewReadOnly");
					var docId = $field.attr("docId");
					var docFormid = $field.attr("docFormid");
					var url = $field.attr("url");
					var fileName = $field.attr("fileName");

					viewReadOnly = (viewReadOnly == "true") ? true : false;
					var imgwHalf = imgh / 2;
					var isSubGridView = jQuery("#obpm_subGridView").size() > 0 ? true
							: false;

					var html = "<div  class='bigImg' style='position:relative;width:"
							+ imgw + "; height:" + imgh + "'>";
					if (viewReadOnly) {
						html += "<img alt='" + fileName + "' border='0' src='"
								+ url + "' width='" + imgw + "' height='"
								+ imgh + "' />";
					} else {
						html += "<a ";
						if (!isSubGridView) {
							html += " href=\"javaScript:viewDoc('" + docId
									+ "','" + docFormid + "')\"";
						}
						html += " title='" + fileName + "'>";
						html += "<img alt='" + fileName + "' border='0' src='"
								+ url + "' width='" + imgw + "' height='"
								+ imgh + "' />";
						html += "</a>";
					}
					html += "<div  class='smallIcon' style='display:none;position:absolute;right:0px;top:"
							+ imgwHalf
							+ "px;z-index:100;'><a class='imgClick' href='"
							+ url + "' target='blank'>";
					html += "<img alt='"
							+ fileName
							+ "' border='0' src='../../../resource/images/picture_go.png' title='点击查看原图' /></a><div>";
					html += "</div>";
					var $html = $(html);
					$html.mouseover(function(event) {
						event.stopPropagation();
						jQuery(this).find(".smallIcon").show();
					}).mouseout(function(event) {
						event.stopPropagation();
						jQuery(this).find(".smallIcon").hide();
					});
					$field.replaceWith($html);
				});
	};

})(jQuery);