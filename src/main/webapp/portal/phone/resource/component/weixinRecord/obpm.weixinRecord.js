;
/**
 * 微信录音控件
 * @author Happy
 * @param $
 */
(function($){
	
	var $bigRecBox="";
	
	/**
	 * 显示类型. 分别：1.只读(READONLY)2.修改(MODIFY),3.隐藏(HIDDEN),4.禁用(DISABLED)
	 */
	var DisplayType = {
			READONLY:1,//只读
			MODIFY:2,//修改
			HIDDEN:3,//隐藏
			DISABLED:4 //禁用
			

	};
	
	/**
	 * 控件初始化
	 */
	function _init(t){
		var options = _parseOptions(t);
		var panel = $('<div id="weixinRecord_container_'+options.id+'"></div>');
		var content = $('<div></div>');
		content.append(' <div class="formfield-wrap record-panel" style="display:none;">'
			       +' <div class="record-box">'
			       +'   <a class="btn btn-block btn-record startRecord" >我要说话</a>'
			       +' </div>'
			       +'</div>');
		
		content.append('<div class="formfield-wrap player-panel" style="display:none;margin-bottom:10px;">'
				+' <div class="record-box">'
				+'  <a class="btn-sound-play">'
				+'    <div class="sound-play-arrow"></div>'
				+'    <div class="sound-play-box">'
				+'      <div class="play-ico sound-play-ico sound-stop"></div>'
				+'    </div>'
				+' </a>'
				+'  <a class="btn-sound-space"></a>'
				+'  <a class="btn-sound-delete">'
				+'    <span class="icon icon-close"></span>'
				+'  </a>'
				+'  <div class="sound-delete-box">'
				+'      <div class="header">提示</div>'
				+'      <div class="contenter"><p>确认删除当前？</p></div>'
				+'      <div class="foot">'
				+'        <a class="btn pull-right  btn-link red btn-delete" data-ignore="push">删除</a>'
				+'        <a class="btn pull-right btn-link gay btn-cancel">取消</a>'
				+'      </div>'
				+'    </div>'
				+' </div>'
				+'</div>');
		
		$(document).find("#div_button_box").before("<div id='wxRec_" + options.id + "' class='bigrec-box' style='display:none'></div>");
		$bigRecBox = $("body").find("#wxRec_" + options.id);
		
		$bigRecBox.append('<div class="modal modal-iframe recording-panel">'
		       +'     <div class="record-play-box">'
		       +'       <div class="record-play-ico">'
		       +'         <div class="sound-box"></div>' 
		       +'       </div>'
		       +'       <div class="sound-text"><p>正在录音</p><p class="time-total">0</p></div>'
		       +'       <div class="btn-record-stop text-center">'
		       +'         <span class="icon icon-stop stopRecord"></span>'
		       +'         <p>停止</p>'
		       +'       </div>'
		       +' </div>'
		       +'</div>');

		if(options.value && options.value.length>0){
			var voice = JSON.parse(options.value);
			var vioceItem = $('<audio><source src="'+contextPath+voice.path+'" type="audio/mpeg" /></audio>');
			panel.append(vioceItem);
			content.find(".player-panel").show();
			if(options.displayType ==DisplayType.READONLY || options.displayType ==DisplayType.DISABLED){
				content.find(".btn-sound-delete").hide();
			}
		}else if(options.displayType ==DisplayType.MODIFY){
				content.find(".record-panel").show();
			}
			
		
		panel.append(content);
		panel.insertAfter(t);
		
		return panel;
	}
	
	
	/**
	 * 事件绑定
	 */
	function _bindEvents(t){
		var panel = t.data("weixinRecord").panel;
		panel.find(".startRecord").on("click",function(e){
			$bigRecBox.show();
			setTimeout(function(){
				$bigRecBox.find(".recording-panel").addClass("active");
			},100);
			$bigRecBox.find(".record-play-ico").addClass("animate-play");
			var timer = _getTimer(t);
			_startRecord(t);
		});
		
		$bigRecBox.find(".stopRecord").on("click",function(e){
			$bigRecBox.find(".recording-panel").removeClass("active");
			setTimeout(function(){
				$bigRecBox.hide();
			},1000);
			var timer = _getTimer(t);
			clearInterval(timer);
			t.data("timer",null);
			_stopRecord(t);
		});
		
		panel.find(".sound-play-box").on("click",function(e){
			_playVoice(t);
		});
		
		panel.find(".btn-sound-delete").on("click",function(e){
			panel.find(".sound-delete-box").show().addClass("animated bounceIn");
		});
		panel.find(".btn-delete").on("click",function(e){
			_removeRecord(t);
		});
		panel.find(".btn-cancel").on("click",function(e){
			panel.find(".sound-delete-box").hide();
		});
		
	}
	
	/**
	 * 解析控件参数
	 */
	function _parseOptions(t){
		var options = {};
		options.id = t.attr("id");
		options.name = t.attr("name");
		options.discription = HTMLDencode(t.data("discription"))? HTMLDencode(t.data("discription")):options.name;
		options.value = t.val();
		options.displayType = t.data("displayType");
		return options;
	}
	
	/**
	 *	开始微信录音
	 */
	function _startRecord(t){
		var _wx = top.wx ? top.wx : wx;
		_wx.startRecord();
	}
	/**
	 *	停止微信录音
	 */
	function _stopRecord(t){
		var panel = t.data("weixinRecord").panel;
		var _wx = top.wx ? top.wx : wx;
		
		_wx.stopRecord({
		    success: function (res) {
		        var localId = res.localId;
		        _wx.uploadVoice({
		            localId: localId, // 需要上传的音频的本地ID，由stopRecord接口获得
		            isShowProgressTips: 1,// 默认为1，显示进度提示
		                success: function (res) {
		                var serverId = res.serverId; // 返回音频的服务器端ID
		                $.ajax({
				        	  url:contextPath+"/portal/weixin/jsapi/upload.action",
				        	  async:false,
				        	  type:"get",
				        	  data:{"serverId":serverId,"folder":"voice","fileType":"amr"},
				        	  dataType:"json",
				        	  success:function(result){
						          if(result.status==1){
						        	  var path = result.data.replace(".amr",".mp3");
						        	  var name = result.data.substring(result.data.lastIndexOf("/")+1,result.data.length);
						        	  var vioceItem = $('<audio><source src="'+contextPath+path+'" type="audio/mpeg" /></audio>');
						        	  t.data("voice",{"localId":localId,"path":path});
						        	 panel.append(vioceItem);
						        	 _addRecord(t,{"path":path,"count":$bigRecBox.find(".time-total").text()});
						        	 $bigRecBox.find(".time-total").text(0);
						          }
						       }
				          });
		            },
		            fail: function (res) {
				          alert("网络异常，请再次尝试！");
			        }
		        });
		    }
		});
	}
	/**
	 * 播放录音
	 */
	function _playVoice(t){
		var panel = t.data("weixinRecord").panel;
		panel.find(".play-ico").removeClass("sound-stop").addClass("sound-play");
		var voice = t.data("voice");
		var count = JSON.parse(t.val()).count;
		if(voice){
			var _wx = top.wx ? top.wx : wx;
			_wx.playVoice({
			    localId: voice.localId // 需要播放的音频的本地ID，由stopRecord接口获得
			});
		}else{
			var panel = t.data("weixinRecord").panel;
			panel.find("audio")[0].play();
			
		}
		setTimeout(function(){
			panel.find(".play-ico").removeClass("sound-play").addClass("sound-stop");
		},count*1000)
	}
	/**
	 * 暂停播放录音
	 */
	function _stopVoice(t){
		var panel = t.data("weixinRecord").panel;
		panel.find(".play-ico").removeClass("sound-play").addClass("sound-stop");
		var voice = t.data("voice");
		if(voice && false){
			var _wx = top.wx ? top.wx : wx;
			_wx.stopVoice({
			    localId: voice.localId // 需要播放的音频的本地ID，由stopRecord接口获得
			});
		}else{
			panel.find("audio")[0].pause();
		}
	}
	
	/**
	 * 添加录音记录
	 */
	function _addRecord(t,data){
		var state = t.data("weixinRecord");
		var options = state.options;
		var panel = state.panel;
		var vf = $("#"+options.id);
		vf.val(JSON.stringify(data));
		panel.find(".player-panel").show();
		panel.find(".record-panel").hide();
	}
	/**
	 * 删除录音记录
	 */
	function _removeRecord(t){
		var state = t.data("weixinRecord");
		var panel =state.panel;
		var options = state.options;
		var vf = $("#"+options.id);
		vf.val("");
		panel.find(".sound-delete-box").hide();
		panel.find(".player-panel").hide();
		panel.find(".record-panel").show();
	}
	
	/**
	 * 获取录音计时器
	 */
	function _getTimer(t){
		var panel = t.data("weixinRecord").panel;
		var timer = t.data("timer");
		if(!timer){
			timer = setInterval(function(){
						var tl = $bigRecBox.find(".time-total");
						tl.text(parseInt(tl.text())+1);
				},1000);
			t.data("timer",timer);
		}
		return timer;
	} 
	
	$.fn.obpmWeixinRecord = function(options, param){
		
		if(typeof options=="string"){
			return $.fn.obpmWeixinRecord.method[options](this,param);
		}
		options = options || {};
		return this.each(function(){
			var t = $(this);
			var state = t.data("weixinRecord");
			if(state){
				$.extend(state.options,options);
			}else{
				var r =_init(t);
				state = t.data('weixinRecord', {
					options: $.extend({}, $.fn.obpmWeixinRecord.defaults, _parseOptions(t), options),
					panel: r
				});
				_bindEvents(t);
			}
		});
	},
	
	$.fn.obpmWeixinRecord.defaults = {
			id:'',
			name:'',
			value:'',
			discription:'',
			displayType:null
	},
	
	$.fn.obpmWeixinRecord.method= {
			
		setValue:function(jq,value){
			
		},
		getValue:function(jq){
			jq.each(function(){
				//nothing
			});
		}
	}
	
})(jQuery);
