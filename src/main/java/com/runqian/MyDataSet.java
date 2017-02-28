package com.runqian;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.dynaform.view.ejb.Column;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.dynaform.view.ejb.ViewProcessBean;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserProcess;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.ProcessFactory;

import com.runqian.report4.dataset.DataSet;
import com.runqian.report4.dataset.IDataSetFactory;
import com.runqian.report4.dataset.Row;
import com.runqian.report4.ide.GVIde;
import com.runqian.report4.usermodel.Context;
import com.runqian.report4.usermodel.CustomDataSetConfig;
import com.runqian.report4.usermodel.DataSetConfig;

public class MyDataSet implements IDataSetFactory {

	private ViewProcess viewprocess = null;
	private View view = null;
	private UserProcess userProcess = null;
	private WebUser webUser = null;
	private String[] args = null;
	private String[] vals = null;
	private String viewId = null;
	private String userId = null;
	private ParamsTable params = null;
//	private String _submitType = null;//提交类型，判断是否页面加载或查询表单查询
	
	public DataSet createDataSet(Context ctx, DataSetConfig dsc, boolean isinput) {
		return create(ctx, dsc, isinput);
	}

	public DataSet createDemoDataSet11(Context ctx, DataSetConfig dsc,
			boolean isinput) {
		// 构造一个数据集
		DataSet ds2 = new DataSet("ds1");
		String[] filds = dataset[0];
		for (int i = 0; i < filds.length; i++) {
			ds2.addCol(filds[i]);// 设置数据集的字段
			System.out.println("设置数据集的字段:" + filds[i]);
		}

		// 设置数据集中的数据
		for (int i = 1; i < dataset.length; i++) {
			String[] datas = dataset[i];
			Row rr = ds2.addRow();
			for (int j = 0; j < datas.length; j++) {
				rr.setData(j + 1, datas[j]);
				System.out.println("设置数据集的数据第" + i + "行 第" + j + "列的值:"
						+ datas[j]);
			}
		}

		return ds2;
	}

	// 定义一个二维数组作为本自定义数据集的来源
	String[][] dataset = { { "字段一", "字段二", "字段二" }, { "a", "1", "c" },
			{ "d", "1", "f" }, { "g", "1", "k" } };

	public DataSet create(Context ctx, DataSetConfig dsc, boolean isinput) {

		// 读取定义数据集时定义的传入参数
		CustomDataSetConfig cdsc = (CustomDataSetConfig) dsc; // 把数据集定义类转成自定义数据集类
		
		if (cdsc != null) {
			args = cdsc.getArgNames(); // 获取自定义数据集传入参数名的集合
			vals = cdsc.getArgValue(); // 获取自定义数据集传入参数值的集合
			if (args != null && args.length>0) {
				for (int i = 0; i < args.length; i++) { // 依次获取传入参数值
					String key = args[i];
					if (key == null)
						continue;
					String value = vals[i];
					if(key.equals("viewId")){
						viewId = value;
					}else if(key.equals("userId")){
						userId = value;
					}
					System.out.println("定义数据集时传入参数" + key + "的值是:" + value); // 打出传入参数值
				}
			}
		} 
		params = (ParamsTable)ctx.getParamValue("params");
		if (params!=null) {
			webUser = (WebUser)ctx.getParamValue("webUser");
			
		}
		
		//判断视图编号是否为空，不为空是从报表配置中获取，为空从页面传递获取
		if (params!=null){
			viewId = params.getParameterAsString("_viewid");
			webUser = (WebUser)ctx.getParamValue("webUser");
			userId = webUser.getId();
			ctx.setParamValue("webUser", null);
			ctx.setParamValue("params", null);
			ctx.setParamValue("view",null);
		}
		
		if(webUser==null){
			try {
				userProcess = (UserProcess) ProcessFactory.createProcess(UserProcess.class);
				UserVO user = (UserVO) userProcess.doView(userId);
				webUser = new WebUser(user);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
		if(params == null){
			params = new ParamsTable();
		}
		
		// 构造一个数据集
		DataSet ds2 = new DataSet("ds1");
		try {
			viewprocess =(ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
			view = (View) viewprocess.doView(viewId);
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally{
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (view != null) {
			Document searchDocument = null;
			if (view.getSearchForm() != null) {
				try {
					searchDocument = view.getSearchForm().createDocument(params, webUser);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				searchDocument = new Document();
			}
			for (Iterator<Column> iter = view.getColumns().iterator(); iter.hasNext();) {
				Column column = (Column) iter.next();
				ds2.addCol(column.getName());// 设置数据集的字段
				System.out.println("设置数据集的字段:" + column.getName());
			}
			DataPackage<Document> datas = null;
			try {
				datas = view.getViewTypeImpl().getViewDatasPage(params, 1,Integer.MAX_VALUE, webUser, searchDocument);
				int i = 0;
				for (Iterator<Document> iter = datas.datas.iterator(); iter.hasNext();) {
					Document doc = (Document) iter.next();
					IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), view.getApplicationid());
					try {
						runner.initBSFManager(doc, params, webUser,new ArrayList<ValidateMessage>());
					} catch (Exception e) {
						e.printStackTrace();
					}
					Row rr = ds2.addRow();
					int j = 0;
					Iterator<Column> iter1 = view.getColumns().iterator();
					while (iter1.hasNext()) {
						Column col = (Column) iter1.next();
						String result = null;
						try {
							result = (String) col.getTextString(doc, runner,webUser);
						} catch (Exception e) {
							e.printStackTrace();
						}
						rr.setData(j + 1, result);
						System.out.println("设置数据集的数据第" + i + "行 第" + j + "列的值:"+ result);
						j++;
					}
					i++;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return ds2;
	}

	public static void main(String[] agrs) {
		try {
			MyDataSet myDataSet = new MyDataSet();
			Context ctx = new Context();
			
			CustomDataSetConfig cdsc = new CustomDataSetConfig();
			
			//cdsc.setArgNames(new String[]{"viewId","userId"});
			//cdsc.setArgValues(new String[]{"11de-a80a-8b78c300-b638-55c259677fcd","11de-c13a-0cf76f8b-a3db-1bc87eaaad4c"});
			
			ParamsTable parmas = new ParamsTable();
			parmas.setParameter("_viewid", "11de-a80a-8b78c300-b638-55c259677fcd");
			ctx.setParamValue("params", parmas);
			
			UserProcess userProcess = (UserProcess)ProcessFactory.createProcess(UserProcess.class);
			UserVO user = (UserVO)userProcess.doView("11de-c13a-0cf76f8b-a3db-1bc87eaaad4c");
			WebUser webUser = new WebUser(user);
			ctx.setParamValue("webUser", webUser);
			
			myDataSet.createDataSet(ctx, cdsc, false);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
