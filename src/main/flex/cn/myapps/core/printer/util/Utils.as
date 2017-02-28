package main.flex.cn.myapps.core.printer.util
{
	public class Utils
	{
		public function Utils()
		{
		}
		
		/**
		 * 产生一个指定范围的随机数
		*/
		public static function random(min: int, max: int): int{
			return int((max - min) * Math.random() + 1 + min);
		}
		
		/**
		 * 	生成一个随机字符串,长度由参数指定
		*/
		public static function randomString(n: int): String{
			var i: int = 0;
			var str: String = "";
			while(i < n){
				str += random(0, 9);
				i ++;
			}
			return str;
		}
		
		public static function nodeHTML(node: String): String{
			return "<b style='color:blue;'>" + node + "</b>";
		}
		
		public static function attributeHTML(attribute: String): String{
			return "<i style='color:red;'>" + attribute + "</i>";
		}
		
		public static function space(n: int): String{
			var i: int = 0;
			var str: String = "";
			while(i < n){
				str += "&nbsp;";
				i ++;
			}
			return str;
		}
	}
}