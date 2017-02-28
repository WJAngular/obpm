/**************************************************************************************************
 * Copyright (C) 2016 SICHENG
 * All Rights Reserved.
 * 本软件为思程科技开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、修改或发布本软件.
 **************************************************************************************************/

package cn.myapps;

import cn.myapps.util.Security;

/** 
 * @ClassName: MainTest 
 * @Description: TODO 
 * @author: WUJING 
 * @date :2016-09-19 下午3:03:44 
 *  
 */
public class MainTest {
	public static void main(String[] args){
		String password = Security.decryptPassword("89ee33bcbfb573c3390726691bc9f116afea5369e830f3c1");
		System.out.println(password);
//		try {
//			Class obj = Class.forName("StageVO");
//			obj.getMethod("", new Class<?>[]{});
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (NoSuchMethodException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		} catch (SecurityException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		}
		
	}
}
