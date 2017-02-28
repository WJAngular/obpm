Contacts.Util = {
	cache : {
		breadcrumbMain : [],
		breadcrumbItem : [],
		scrollTop : [],
		currentId : ""
	},
	/**
	 * 获取用户头像图片url地址
	 * @param userId
	 */	
	getAvatar : function(userId){
		if(!this.cache[userId]){
			$.ajax({
				type: "GET",
				url: contextPath + "/contacts/getAvatar.action",
				data: {"id":userId},
				async: false,
				dataType: "json",
				success:function(result){
					if(1==result.status){
						QM.Util.cache[userId] = result.data;
					}
				}
			});
		}
		
		return this.cache[userId];;
	},
	/**
	 * 控制loading层
	 */
	controlLoading : function(active) {
		if(active == "show"){
			$("#loadingToast").show();
		}else{
			setTimeout(function(){
				$("#loadingToast").hide();
        	},300)
		}
	},
	/**
	 * 控制空数据占位符
	 */
	controlPlaceholder : function(active) {
		if(active == "show"){
			$("#contacts").addClass("placeholder");
		}else{
			
			$("#contacts").removeClass("placeholder");
		}
	}
}