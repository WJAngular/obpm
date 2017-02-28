package main.flex.cn.myapps.core.printer.data
{
	/**
	 *控件类别 类，把一组件的类名作为构造参数实例化一个组件类别实例 
	 * 
	 */	
	public class C_K_Str implements IComparable{
	    private var dbKeyStr:String; 
	
	    public function C_K_Str(key:String) 
	    { 
	        dbKeyStr = key; 
	    } 
	    
	    public function getStr():String{
	    	return dbKeyStr;
	    }
		/**
		 *传递另一个组件的类别实例判断是否为同一类别
		 * @param key
		 * @return 
		 * 
		 */	
	    public function compareTo(key:IComparable):int 
	    { 
	    	
	        var intReturn:int=0;
	        var pkey:*=key;
	        var mKey:String=pkey.dbKeyStr;//(C_K_Str)
	        if (dbKeyStr!=null && mKey!=null){
	           //intReturn = strMyKey.compareToIgnoreCase(mKey);
	           if (dbKeyStr>mKey){
	           		return 1;
	           }else if (dbKeyStr<mKey){
	           		return -1;
	           }else{
	           		return 0;
	           }
	        }else{
	        	if (dbKeyStr==null){
	        		return -1;
	        	}else{
	        		return 1;
	        	}
	        }
	        return intReturn;
	    } 
	}
}