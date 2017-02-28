package cn.myapps.core.dynaform.view.ejb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import cn.myapps.base.OBPMValidateException;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.constans.Environment;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.Item;
import cn.myapps.core.dynaform.form.ejb.ValidateMessage;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.StringUtil;
import cn.myapps.util.property.DefaultProperty;
import cn.myapps.util.sequence.Sequence;
import cn.myapps.util.sequence.SequenceException;

public class ExcelFileBuilder {
	private static final Logger LOG = Logger.getLogger(ExcelFileBuilder.class);

	private WebUser user;

	// 每页总行数1000条
	private final int LINES = 1000;

	private String fileRealPath = "";

	private boolean isWorkbookCreated = false;

	private Map<String, Integer> view2rowCountMap = new HashMap<String, Integer>();
	
	private static Set<String> fileNameSet = new CopyOnWriteArraySet<String>();

	public ExcelFileBuilder(WebUser user) {
		this.user = user;
	}

	/**
	 * 创建工作薄
	 * 
	 * @param view
	 * @param params
	 * @throws Exception
	 */
	public void buildSheet(View view, HSSFWorkbook workbook, ParamsTable params) throws Exception {
		try {
			Document currdoc = new Document();
			StringBuffer parents = new StringBuffer();
			if (view.getSearchForm() != null) {
				currdoc = view.getSearchForm().createDocument(params, user);
			}

			IRunner runner = JavaScriptFactory.getInstance(params.getSessionid(), view.getApplicationid());
			// 获得总条数
			int count = (int) view.getViewTypeImpl().countViewDatas(params, user, currdoc);
			if (count > 0) {
				// 获得总页数
				int pageSize = (int) Math.ceil((double) count / (double) LINES);

				Collection<String> heads = this.getHeads(view, runner);
				// Collection<View> subViewList = view.getSubViewList();

				// 分页查询写Excel文件
				for (int tempPage = 1; tempPage <= pageSize; tempPage++) {
					parents.setLength(0);
					// 1000条
					DataPackage<Document> datas = view.getViewTypeImpl().getViewDatasPage(params, tempPage, LINES,
							user, currdoc);
					HSSFSheet sheet = getSheet(view, workbook);

					double rownum = 1;
					for (Iterator<Document> iterator = datas.datas.iterator(); iterator.hasNext();) {
						Document doc = (Document) iterator.next();
						params.setParameter("rownum", rownum ++);//设置rownum，作为输出序号列的值
						Map<String, Object> excelData = convertToExcelData(doc, view, user, params, runner,
								new ArrayList<ValidateMessage>());

						// 判断是否第一次加载数据
						if (getRowCount(view) == 0) {
							// 添加工作薄头
							addSheetHead(view, runner, workbook, sheet);
						}
						// 添加工作薄数据
						addSheetData(workbook, view, sheet, heads, excelData);

						if (params.getParameterAsBoolean("isExpSub")) {
							parents.append("'").append(doc.getId()).append("',");
							// 添加子工作薄 
							/*Collection<View> subViewList = view.getSubViewList();
							if (subViewList != null && !subViewList.isEmpty()) {
								for (Iterator<View> viewIterator = subViewList.iterator(); viewIterator.hasNext();) {
									View subView = viewIterator.next();
									ParamsTable newParams = new ParamsTable();
									newParams.setParameter("parentid", doc.getId());
									newParams.setParameter("isRelate", "true");

									buildSheet(subView, workbook, newParams);
								}
							}*/
						}
					}
					// 优化性能,一次导出1000条子表单
					if (params.getParameterAsBoolean("isExpSub")) {
						if(parents.length()>0){
							parents.setLength(parents.length()-1);
						}
						// 添加子工作薄
						Collection<View> subViewList = view.getSubViewList();
						if (subViewList != null && !subViewList.isEmpty()) {
							for (Iterator<View> viewIterator = subViewList.iterator(); viewIterator.hasNext();) {
								View subView = viewIterator.next();
								ParamsTable newParams = new ParamsTable();								
								newParams.setParameter("parentid", parents.toString());
								newParams.setParameter("isRelate", "true");
								newParams.setParameter("isExcelExpOperation", true);
								buildSheet(subView, workbook, newParams);
							}
						}
					}
					writeToFilePath(fileRealPath, workbook);
				}
			}else{
				//视图没有数据时，也要把表头导出excel
				HSSFSheet sheet = getSheet(view, workbook);
				// 判断是否第一次加载数据
				if (getRowCount(view) == 0) {
					// 添加工作薄头
					addSheetHead(view, runner, workbook, sheet);
				}
				writeToFilePath(fileRealPath, workbook);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 创建工作薄
	 * 
	 * @param view
	 * @param params
	 * @throws Exception
	 */
	public void buildSheet(View view, ParamsTable params) throws Exception {
		String fileName = getFileName(params); // 文件名
		String path = getSavePath(params); // 文件完整路径
		
		StringBuffer sb = new StringBuffer();
		getFileNameOther(sb,path,fileName);
		fileRealPath = path +sb.toString();
		
		fileNameSet.add(sb.toString());
		buildSheet(view, getWorkbook(), params);
		fileNameSet.remove(sb.toString());
	}

	private HSSFSheet getSheet(View view, HSSFWorkbook workbook) {
		String sheetName = view.getName();
		String viewDescription = view.getDescription();
		if (viewDescription!=null && viewDescription.trim().length()>0) {
			sheetName = viewDescription;
		}
		HSSFSheet sheet = workbook.getSheet(sheetName);
		if (sheet == null) {
			sheet = workbook.createSheet(sheetName);
		}
		return sheet;
	}

	private HSSFWorkbook getWorkbook() throws FileNotFoundException, IOException {
		HSSFWorkbook workbook = null;
		if (!isWorkbookCreated) {
			workbook = new HSSFWorkbook();
			isWorkbookCreated = true;

			// 写入一个空文件
			writeToFilePath(fileRealPath, workbook);
		} else {
			// 从原有文件中打开workbook
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(fileRealPath));
			workbook = new HSSFWorkbook(fs);
		}

		return workbook;
	}

	/**
	 * 生成Excel文件
	 * 
	 * @return
	 */
	public File toExcelFile() {
		if (!StringUtil.isBlank(fileRealPath)) {
			File file = new File(fileRealPath);
			if (file.exists() && file.isFile()) {
				return file;
			}
		}
		return null;
	}

	protected Map<String, Object> convertToExcelData(Document doc, View view, WebUser user, ParamsTable params,
			IRunner runner, ArrayList<ValidateMessage> errors) throws Exception {
		Collection<Column> columns = view.getDisplayColumns(runner);
		runner.initBSFManager(doc, params, user, errors);
		Map<String, Object> line = new HashMap<String, Object>();
		Object rownum = params.getParameter("rownum");
		for (Iterator<Column> iterator = columns.iterator(); iterator.hasNext();) {
			Column col = iterator.next();
			if(col.isVisible4ExpExcel()){
				Object result = null;
				if("$stateLabel".equalsIgnoreCase(col.getFormField().getName())){
					result = doc.getStateLabel();
				}else if("$auditorNames".equalsIgnoreCase(col.getFormField().getName())){
					result = doc.getAuditorNames();
				}else{
					result = col.getTextString(doc, runner, user,null);
				}
				if(result !=null && col.getFormField() !=null && Item.VALUE_TYPE_NUMBER.equals(col.getFormField().getFieldtype())){
					if(result.toString().replaceAll("&nbsp;", "").length()>0){
						try{
							result = Double.parseDouble(result.toString());
						}catch(Exception ne) {
							result = result.toString();
						}
					}
				}
				if(Column.COLUMN_TYPE_ROWNUM.equals(col.getType())){
					result = rownum;
				}
				line.put(col.getName(), result);
			}
		}

		return line;
	}

	/**
	 * 获得存入Excel数据
	 * 
	 * @param datas
	 * @param view
	 * @param user
	 * @param params
	 * @param runner
	 * @param errors
	 * @return
	 * @throws Exception
	 */
	public Collection<Map<String, Object>> getExcelData(DataPackage<Document> datas, View view, WebUser user,
			ParamsTable params, IRunner runner, ArrayList<ValidateMessage> errors) throws Exception {
		Collection<Map<String, Object>> excelData = new ArrayList<Map<String, Object>>();// excel数据
		Collection<Column> columns = view.getDisplayColumns(runner);

		for (Iterator<Document> iter = datas.datas.iterator(); iter.hasNext();) {
			Document doc = (Document) iter.next();
			runner.initBSFManager(doc, params, user, errors);
			Object result = null;

			Map<String, Object> line = new HashMap<String, Object>();
			for (Iterator<Column> iterator = columns.iterator(); iterator.hasNext();) {
				Column col = iterator.next();
				if(col.isVisible4ExpExcel()){
					result = col.getTextString(doc, runner, user);
					line.put(col.getName(), result);
				}
			}

			excelData.add(line);
		}

		return excelData;
	}

	/**
	 * 获得Excel的头
	 * 
	 * @param datas
	 * @param view
	 * @return
	 * @throws Exception
	 */
	public Collection<String> getHeads(View view, IRunner runner) throws Exception {
		Collection<String> heads = new ArrayList<String>();// excel头
		Collection<Column> columns = view.getDisplayColumns(runner);

		for (Iterator<Column> iter = columns.iterator(); iter.hasNext();) {
			Column col = iter.next();
			if(col.isVisible4ExpExcel()){
				if(!Column.COLUMN_TYPE_LOGO.equals(col.getType()) && !Column.COLUMN_TYPE_OPERATE.equals(col.getType())){
					heads.add(col.getName());
				}
			}
		}

		return heads;
	}

	public String getFileName(ParamsTable params) throws SequenceException {
		// 生成文件
		String fileName = params.getParameterAsString("filename").toLowerCase().trim();
		if (StringUtil.isBlank(fileName)) {
			fileName = Sequence.getSequence();
		}

		return fileName;
	}

	/**
	 * 获得文件真实路径
	 * 
	 * @param excelData
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String getSavePath(ParamsTable params) throws Exception {

		// 生成保存目录
		String fileDir = params.getParameterAsString("filedir");
		if (StringUtil.isBlank(fileDir)) {
			fileDir = DefaultProperty.getProperty("REPORT_PATH");
		}

		String savePath = Environment.getInstance().getRealPath(fileDir);
		File saveDir = new File(savePath);
		if (!saveDir.exists()) {
			if (!saveDir.mkdirs())
				throw new OBPMValidateException("Folder create failure");
		}

		if (savePath.lastIndexOf("/") < 0 && savePath.lastIndexOf("\\") < 0) {
			savePath += savePath + "/";
		}
		
		return savePath;
	}
	
	/**
	 * 判断文件存在并处于打开的时候就要重新生成另外的文件名
	 * @param sb
	 * @param savePath
	 * @param fileName
	 * @throws SequenceException 
	 */
	public void getFileNameOther(StringBuffer sb,String savePath,String fileName) throws SequenceException{
		int i = 1;
		File file = new File(savePath + fileName+".xls");
		if(file.exists()&&file.isFile()){
			while(fileNameSet.contains(fileName+".xls")){
				fileName = fileName+"("+i+")";
				i++;
			}
		}
		sb.append(fileName+".xls");
	}

	/**
	 * 创建Excel工作薄标题行
	 * 
	 * @param view
	 * @param sheet
	 * @param rowCount
	 * @SuppressWarnings("deprecation") 使用了过时的API
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public void addSheetHead(View view, IRunner runner, HSSFWorkbook workbook, HSSFSheet sheet) throws Exception {
		HSSFRow headrow = sheet.createRow(0);
		int i = 0;
		Collection<String> heads = getHeads(view, runner);
		// HSSFCellStyle style = createCellStyle(workbook);
		for (Iterator<String> iter = heads.iterator(); iter.hasNext();) {
			String head = iter.next();
			HSSFCell cell = headrow.createCell((short) i);
//			cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			// cell.setCellStyle(style);
			cell.setCellValue(head);
			i++;
		}

		increaseRowCount(view);
	}

	/**
	 * 增加行数
	 * 
	 * @param view
	 */
	private void increaseRowCount(View view) {
		if (view2rowCountMap.containsKey(view.getId())) {
			int rowCount = view2rowCountMap.get(view.getId());
			view2rowCountMap.put(view.getId(), rowCount + 1);
		} else {
			view2rowCountMap.put(view.getId(), 1);
		}
	}

	/**
	 * 获取行数
	 * 
	 * @param view
	 * @return
	 */
	private int getRowCount(View view) {
		return view2rowCountMap.get(view.getId()) != null ? view2rowCountMap.get(view.getId()) : 0;
	}

	/**
	 * 创建Excel Cell Style
	 * 
	 * @param workbook
	 * @return
	 */
	private HSSFCellStyle createCellStyle(HSSFWorkbook workbook) {
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));

		return cellStyle;
	}

	/**
	 * 
	 * @param workbook
	 * @param sheet
	 * @param heads
	 * @param datas
	 * @param outputFile
	 * @param rowCount
	 * @return
	 * @SuppressWarnings("deprecation") 使用了过时的API
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public void addSheetData(HSSFWorkbook workbook, View view, HSSFSheet sheet, Collection<String> heads,
			Map<String, Object> map) throws IOException {

		HSSFRow row = sheet.createRow(getRowCount(view));
		int k = 0;

		for (Iterator<String> iterator = heads.iterator(); iterator.hasNext();) {
			String columnName = (String) iterator.next();
			Object result = (Object) map.get(columnName);
			HSSFCell cell = row.createCell((short) k);
//			cell.setEncoding(HSSFCell.ENCODING_UTF_16);

			if (result instanceof String) {
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue((String) result);
			} else if (result instanceof Double) {
				cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				cell.setCellValue(((Double) result).doubleValue());
			} else if (result instanceof Date) {
				cell.setCellStyle(createCellStyle(workbook));
				cell.setCellValue((Date) result);
			}
			k++;
		}

		LOG.info(view.getName() + ": line(" + getRowCount(view) + ")..........");
		increaseRowCount(view);
	}

	/**
	 * 把workbook写到具体的文件路径
	 * 
	 * @param fileRealPath
	 * @param workbook
	 * @throws IOException
	 */
	private void writeToFilePath(String fileRealPath, HSSFWorkbook workbook) throws IOException {
		// 新建一输出文件流
		FileOutputStream fOut = new FileOutputStream(fileRealPath);
		// 把相应的Excel 工作簿存盘
		workbook.write(fOut);
		fOut.flush();

		// 操作结束，关闭文件
		fOut.close();
		LOG.info("Excel文件追加成功.................");
		//new TestThread(fileRealPath,workbook).run();
	}
}
