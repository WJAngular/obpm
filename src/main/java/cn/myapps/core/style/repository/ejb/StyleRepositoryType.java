package cn.myapps.core.style.repository.ejb;

public class StyleRepositoryType {

	/**
	 * 打开Form之前执行脚本 当返回值非空时打开失败
	 */
	public static String beforopenscript;

	/**
	 * 打开Form之后执行脚本
	 */
	public static String afteropenscript;

	/**
	 * 保存Form之前执行脚本 当返回值非空时保存失败
	 */
	public static String beforsavescript;

	/**
	 * 保存Form之后执行脚本
	 */
	public static String aftersavescript;

	/**
	 * 退出时执行脚本 当返回值非空时退出失败
	 */
	public static String onexitscript;

}
