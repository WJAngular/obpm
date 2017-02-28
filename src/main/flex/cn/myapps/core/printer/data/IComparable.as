package main.flex.cn.myapps.core.printer.data
{
	
	/**
	 * 组件类别接口，提供判断两个控件是否是同一个类的实例
	 * 
	 */
	public interface IComparable
	{
		 /**
         * Compare this object with rhs.
         * @param rhs the second Comparable.
         * @return 0 if two objects are equal;
         *     less than zero if this object is smaller;
         *     greater than zero if this object is larger.
         */
        function compareTo(rhs:IComparable):int;
	}
}