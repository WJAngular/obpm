package cn.myapps.core.overview;

import com.lowagie.text.Table;

/**
 * 概览的pdf表格生成接口
 * 
 * 2.6版本新增的类
 * 
 * @author keezzm
 *
 */
public interface IOverview {

	public Table buildOverview(String applicationId) throws Exception;
}
