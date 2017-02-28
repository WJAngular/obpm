Contacts.Service = {
	
	/**
	 * 获取部门列表(含部门成员)
	 * @params {"parentId":""}
	 */
	getContactsTree : function(params,callback){
		$.ajax({
    		url: "getContactsTree.action",
    		async: false,
    		cache:false,
			data:params,
			success: function(result){
    			if(1==result.status){
					if(callback && typeof callback == "function"){
						callback(result.data);
					}
				}else{
					//Utils.showMessage(result.message, "error");
				}
    		}
    	})
	},
	/**
	 * 获取全部联系人
	 */
	getAllUser : function(callback){
		$.ajax({
    		url: "getAllUser.action",
    		async: false,
    		cache:false,
			data:{},
			success: function(result){//{message: "", status: "", data: Array[]}
    			if(1==result.status){
					if(callback && typeof callback == "function"){
						callback(result.data);
					}
				}else{
					//Utils.showMessage(result.message, "error");
				}
    		}
    	})
	},
	/**
	 * 获取角色
	 * @params {"applicationId":"","roleId":""}
	 */
	getRoleTree : function(params,callback){
		$.ajax({
    		url: "getApplicationAndRoleContactsTree.action",
    		async: false,
    		cache:false,
			data:params,
			success: function(result){
    			if(1==result.status){
					if(callback && typeof callback == "function"){
						callback(result.data);
					}
				}else{
					//Utils.showMessage(result.message, "error");
				}
    		}
    	})
	},
	/**
	 * 获取常用联系人
	 */
	getFavorite : function(params,callback){
		$.ajax({
    		url: "getFavoriteContacts.action",
    		async: false,
    		cache:false,
    		data:params,
    		success: function(result){
    			if(1==result.status){
					if(callback && typeof callback == "function"){
						callback(result.data);
					}
				}else{
					callback("");
					//Utils.showMessage(result.message, "error");
				}
    		}
		})
	},
	/**
	 * 获取部门角色下级人员数量
	 * @params {"id":"","type":""}
	 */
	getRoleOrDeptUserCounts : function(params,callback){
		$.ajax({
    		url: "getRoleOrDeptUserCounts.action",
    		async: false,
    		cache:false,
			data:params,
			success: function(result){
    			if(1==result.status){
					if(callback && typeof callback == "function"){
						callback(result.data);
					}
				}else{
					//Utils.showMessage(result.message, "error");
				}
    		}
    	})
	},
	/**
	 * 是否常用联系人
	 */
	isFavoriteContact : function(params,callback){
		$.ajax({
    		url: "isFavoriteContact.action",
    		async: false,
    		cache:false,
			data:params,
			success: function(result){//{message: "", status: "", data: Array[]}
    			if(1==result.status){
					if(callback && typeof callback == "function"){
						callback(result.data);
					}
				}else{
					//Utils.showMessage(result.message, "error");
				}
    		}
    	})
	},
	
	/**
	 * 加入常用联系人
	 * @params {"userId":""}
	 */
	addFavorite : function(params,callback){
		$.ajax({
    		url: "addFavoriteContact.action",
    		async: false,
    		cache:false,
			data:params,
			success: function(result){
    			if(1==result.status){
					if(callback && typeof callback == "function"){
						callback(result.data);
					}
				}else{
					//Utils.showMessage(result.message, "error");
				}
    		}
    	})
	},
	
	
	/**
	 * 移除常用联系人
	 * @params {"userId":""}
	 */
	removeFavoriteContact : function(params,callback){
		$.ajax({
    		url: "removeFavoriteContact.action",
    		async: false,
    		cache:false,
			data:params,
			success: function(result){
    			if(1==result.status){
					if(callback && typeof callback == "function"){
						callback(result.data);
					}
				}else{
					//Utils.showMessage(result.message, "error");
				}
    		}
    	})
	},
	/**
	 * 查询
	 * @params {"keyWord":""}
	 */
	searchContacts : function(params,callback){
		$.ajax({
    		url: "getContactsBySearch.action",
    		async: false,
    		cache:false,
			data:params,
			success: function(result){
    			if(1==result.status){
					if(callback && typeof callback == "function"){
						callback(result.data);
					}
				}else{
					//Utils.showMessage(result.message, "error");
				}
    		}
    	})
	}
}