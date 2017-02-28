package cn.myapps.core.report.crossreport.runtime.action;

import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

import cn.myapps.core.report.crossreport.runtime.analyzer.AnalyseDimension;
import cn.myapps.core.report.crossreport.runtime.analyzer.AnalyseTable;
import cn.myapps.core.report.crossreport.runtime.dataset.ConsoleData;
import cn.myapps.core.report.crossreport.runtime.dataset.ConsoleDataSet;
import cn.myapps.core.report.crossreport.runtime.dataset.ConsoleDataType;
import cn.myapps.core.report.crossreport.runtime.dataset.ConsoleMetaData;
import cn.myapps.core.report.crossreport.runtime.dataset.ConsoleRow;
import cn.myapps.util.StringUtil;

/**
 * The excel console writer which implement the ConsoleWriter interface, it can
 * export the console object(like data set) to excel file.
 * 
 */
public class ExcelWriter {

	private static int MAX_EXCEL_ROW_COUNT = 65535;

	/**
	 * 
	 * @param url
	 * @param dataSet
	 * @SuppressWarnings 使用了过时的POI API
	 * @return
	 *
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public long write(String url, AnalyseTable analyseTable) throws Exception {
		try {
			if (url != null && url.length() > 0 && analyseTable != null) {
				// 1.Create the excel work sheet
				HSSFWorkbook workbook = new HSSFWorkbook();
				HSSFSheet sheet = workbook.createSheet("result");
				// 2.Export the title
				short rowIndex = 0;
				//HSSFRow row = sheet.createRow(0);
				rowIndex=getColumnSetExcel(sheet, analyseTable, rowIndex);
				// 3.Export the data
				getRowSetExcel(sheet, analyseTable, rowIndex);
				// 4.Save the file
				FileOutputStream fos = new FileOutputStream(url);
				workbook.write(fos);
				fos.close();
			}

			return -1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * Get the analyse table column Excel header 
	 * 
	 * @param analyseTable
	 *            The target analyse table.
	 * @return rowIndex
	 */
	@SuppressWarnings("deprecation")
	private short getColumnSetExcel(HSSFSheet sheet, AnalyseTable analyseTable,short rowIndex) {
		for (Iterator<AnalyseDimension> iterator = analyseTable.getColumnSet().iterator(); iterator.hasNext();) {
			HSSFRow row = sheet.createRow(rowIndex);
			AnalyseDimension column = (AnalyseDimension) iterator.next();
			List<String> keySet = column.getDatasKeyIterator();
			short columnIndex = 0;
			for (Iterator<AnalyseDimension> iterator2 = analyseTable.getRowSet().iterator(); iterator2.hasNext();) {
				AnalyseDimension deimension = (AnalyseDimension) iterator2.next();
				ConsoleMetaData metaData = deimension.getMetaData();
				if(metaData != null){
					String columnName = metaData.getColumnName().toUpperCase();
					HSSFCell cell = row.createCell(columnIndex);
					cell.setCellValue(columnName.indexOf("ITEM_") >= 0 ? columnName.substring(columnName.indexOf("ITEM_") + 5)
							: columnName);
					columnIndex++;
				}
			}
			for (Iterator<String> iterator2 = keySet.iterator(); iterator2.hasNext();) {
				String key = (String) iterator2.next();
				if(analyseTable.getCalculationMethod()!=null){
				  key = StringUtil.isBlank(key)?"汇总":key;
				  HSSFCell cell = row.createCell(columnIndex);
					cell.setCellValue(getKeyLabel(key));
					columnIndex++;
				}
			}
			if (analyseTable.isDisplayCol() && rowIndex == 0) {
				HSSFCell cell0 = row.createCell(columnIndex);
				cell0.setCellValue("总计:");
				columnIndex++;
			}
			rowIndex++;
		}
		return rowIndex;
	}
	/**
	 * Get the key label.
	 * 
	 * @param key
	 *            The whole key
	 * @return The key label
	 */
	private String getKeyLabel(String key) {
		String[] keys = key.split(AnalyseDimension.KEY_KEY_SEPARATOR);
		return keys[keys.length - 1];
	}
	
	/**
	 * Get the analyse table Excel row 
	 * 
	 * @param analyseTable
	 *            The target analyse table.
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private void getRowSetExcel(HSSFSheet sheet, AnalyseTable analyseTable , short rowIndex) {
		DecimalFormat df = new DecimalFormat("######0.00");
		DecimalFormat df1 = new DecimalFormat();

		Object[] rowDimesions = analyseTable.getRowSet().toArray();
		AnalyseDimension lastRowDimension = (AnalyseDimension) rowDimesions[rowDimesions.length - 1];

		Object[] columnDimesions = analyseTable.getColumnSet().toArray();
		AnalyseDimension lastColumnDimension  = null;
		if(columnDimesions == null ||columnDimesions.length == 0)
		{
			ConsoleMetaData metaData = new ConsoleMetaData(ConsoleDataType.toDataType(-1),"小计",0);
		    ConsoleDataSet dataSet = new ConsoleDataSet(new HashMap<String, ConsoleMetaData>());
			lastColumnDimension = new AnalyseDimension(metaData,dataSet);
		}else{
			lastColumnDimension = (AnalyseDimension) columnDimesions[columnDimesions.length - 1];
		}

		Map<String, Collection<ConsoleRow>> resultDatas = lastRowDimension.getCrossDatas(lastColumnDimension);
		Collection<String> outputValue = new ArrayList<String>();
		//Collection rowSum = new ArrayList();
		Map<String, Double> map = new LinkedHashMap<String, Double>();
		Collection<String> mergeCellList = new ArrayList<String>();

		for (Iterator<String> iterator = lastRowDimension.getDatasKeyIterator().iterator(); iterator.hasNext();) {
			if (rowIndex > MAX_EXCEL_ROW_COUNT)
				break;
			short columnIndex = 0;
			HSSFRow row = sheet.createRow(rowIndex);
			String lastRowKey = (String) iterator.next();
			String[] keys = lastRowKey.split(AnalyseDimension.KEY_KEY_SEPARATOR);

			for (int i = 0; i < keys.length; ++i) {
				String currentKey = "";
				for (int j = 0; j <= i; ++j) {
					currentKey = (currentKey.length() > 0) ? currentKey + AnalyseDimension.KEY_KEY_SEPARATOR +(StringUtil.isBlank(keys[j])?" ":keys[j]) 
							: (StringUtil.isBlank(keys[j])?" ":keys[j]);
				}
				if (!outputValue.contains(currentKey)) {
					int rowSpan = getRowSpan(currentKey, analyseTable) - 1;
					if(rowSpan > 0){
						int mergeStartRowIndex = rowIndex;
						int mergeEndRowIndex = rowIndex+rowSpan;
						//merge the cell
						sheet.addMergedRegion(new Region(mergeStartRowIndex, (short) columnIndex, mergeEndRowIndex, (short) columnIndex));
						for(int k=rowIndex; k<=mergeEndRowIndex;k++){
							String mergeCellStr = String.valueOf(k) + String.valueOf(columnIndex);
							mergeCellList.add(mergeCellStr.trim());
						}
					}else{
						String cellStr = String.valueOf(rowIndex) + String.valueOf(columnIndex);
						if(mergeCellList.contains(cellStr)){
							//columnIndex++;  //屏蔽此行--Jarod
							
							//导出Excel 由于合并单元格出现的错位问题，Excel在理解合并单元格问题的时候，
							//虽然已经有合并，但Column定位并不会因此调整，如果执行columnIndex ++
							//则出现错位问题。 --Jarod
						}
					}
					HSSFCell cell = row.createCell(columnIndex);
					cell.setCellValue((getKeyLabel(currentKey)));
					outputValue.add(currentKey);
					columnIndex++;
				}else{
					columnIndex++;
				}
			}

			// Output the calculation result.
			double colSummarize = 0.0;
			double colValue = 0.0;
			double countColumn = 0;
			for (Iterator<String> iterator2 = lastColumnDimension.getDatasKeyIterator().iterator(); iterator2.hasNext();) {
				String lastColumnKey = (String) iterator2.next();
				String resultKey = lastRowKey + AnalyseDimension.DIM_DIM_SEPARATOR + lastColumnKey;
				Collection<ConsoleRow> collection = (Collection<ConsoleRow>) resultDatas.get(resultKey);
				Collection<ConsoleRow> datas = collection;
				//实现没有汇总的情况
				if(analyseTable.getCalculationMethod()!=null){
					getCalculationCell(datas, analyseTable.getCalculationField(), analyseTable.getCalculationMethod(),row,columnIndex);
					columnIndex++;
				}

				if (analyseTable.isDisplayCol()) {
					colValue = this.getSummarizeTotal(datas, analyseTable.getCalculationField(), analyseTable
							.getCalculationMethod());
					colSummarize += colValue;
				}

				if (analyseTable.isDisplayRow()) {
					double rowValue = this.getSummarizeTotal(datas, analyseTable.getCalculationField(), analyseTable
							.getCalculationMethod());
					if (map.containsKey(lastColumnKey)) {
						double temp = ((Double) (map.get(lastColumnKey))).doubleValue();
						map.put(lastColumnKey, new Double(temp + rowValue));
					} else
						map.put(lastColumnKey, new Double(rowValue));
				}
				countColumn++;
			}
			if (analyseTable.isDisplayCol()) {
				countColumn = countColumn == 0 ? 1 : countColumn;

				if (CalculationMethod.valueOf(analyseTable.getColCalMethod()).equals(CalculationMethod.AVERAGE)){
					HSSFCell cell = row.createCell(columnIndex);
					cell.setCellValue(df.format(colSummarize / countColumn));
				}else{
					HSSFCell cell = row.createCell(columnIndex);
					cell.setCellValue(colSummarize);
				}

				if (map.containsKey("Total")) {
					double temp = ((Double) (map.get("Total"))).doubleValue();
					map.put("Total", new Double(temp + colSummarize));
				} else {
					map.put("Total", new Double(colSummarize));
				}

			}
			rowIndex++;
		}
		
		rowIndex = rowIndex == 0 ? 1 : rowIndex;
		if (analyseTable.isDisplayRow()) {
			int columnIndex=0;
			HSSFRow row = sheet.createRow(rowIndex);
			HSSFCell cell0 = row.createCell(columnIndex);
			cell0.setCellValue("总计:");
			columnIndex+=analyseTable.getRowSet().size();
			for (Iterator<Entry<String, Double>> iterator = map.entrySet().iterator();iterator != null && iterator.hasNext();) {
				Map.Entry<String, Double> entry = iterator.next();
				Double value = entry.getValue();
				if (CalculationMethod.valueOf(analyseTable.getRowCalMethod()) == CalculationMethod.AVERAGE){
					HSSFCell cell = row.createCell(columnIndex);
					cell.setCellValue(df.format(value / rowIndex));
					columnIndex++;
				}
				else{
					HSSFCell cell = row.createCell(columnIndex);
					cell.setCellValue(df1.format(value));
					columnIndex++;
				}
			}
		}
	}
	
	/**
	 * Get the column span.
	 * 
	 * @param analyseRowKey
	 *            The analyse column key.
	 * @param analyseTable
	 *            The analyse table.
	 * @return The The analyse column span count.
	 */
	private int getRowSpan(String analyseRowKey, AnalyseTable analyseTable) {

		Object[] rowSet = analyseTable.getRowSet().toArray();

		AnalyseDimension lastAnalyseColumn = (AnalyseDimension) rowSet[rowSet.length - 1];

		Collection<String> keySet = lastAnalyseColumn.getDatas().keySet();

		int count = 0;

		for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			if (key.startsWith(analyseRowKey + AnalyseDimension.KEY_KEY_SEPARATOR))
				count++;
		}

		return count;
	}
	
	/**
	 * Get the calculation result Excel Cell
	 * 
	 * @param datas
	 *            The result row
	 * @param calculationField
	 *            The calculation field meta data.
	 * @param method
	 *            The calculation method
	 * @return
	 */
	private void getCalculationCell(Collection<ConsoleRow> datas, ConsoleMetaData calculationField, CalculationMethod method,HSSFRow HFrow,int columnIndex) {
		Calculator calculator = new Calculator();
		if (method.equals(CalculationMethod.DISTINCT)) {
			if (datas != null && datas.size() > 0) {
				for (Iterator<ConsoleRow> iterator = datas.iterator(); iterator.hasNext();) {
					ConsoleRow row = (ConsoleRow) iterator.next();
					ConsoleData data = row.getData(calculationField);
					HSSFCell cell = HFrow.createCell(columnIndex);
					cell.setCellValue(data != null ? data.getStringValue() : "");
				}
			}
		} else if (method.equals(CalculationMethod.MAX)) {
			if (datas != null && datas.size() > 0){
				HSSFCell cell = HFrow.createCell(columnIndex);
				cell.setCellValue(calculator.max(datas, calculationField).getStringValue());
			}else{
				HSSFCell cell = HFrow.createCell(columnIndex);
				cell.setCellValue("");
			}
		} else if (method.equals(CalculationMethod.MIN)) {
			if (datas != null && datas.size() > 0){
				HSSFCell cell = HFrow.createCell(columnIndex);
				cell.setCellValue(calculator.min(datas, calculationField).getStringValue());
			}else{
				HSSFCell cell = HFrow.createCell(columnIndex);
				cell.setCellValue("");
			}
		} else {
			DecimalFormat f=new DecimalFormat();
			HSSFCell cell = HFrow.createCell(columnIndex);
			cell.setCellValue(f.format(calculator.compute(datas, method, calculationField)));
		}
	}
	
	/**
	 * Get the summarize result Excel Cell
	 * 
	 * @param datas
	 *            The result row
	 * @param calculationField
	 *            The calculation field meta data.
	 * @param method
	 *            The calculation method
	 * @return
	 */
	private double getSummarizeTotal(Collection<ConsoleRow> datas, ConsoleMetaData calculationField, CalculationMethod method) {
		//StringBuffer buffer = new StringBuffer();
		Calculator calculator = new Calculator();
		return calculator.compute(datas, method, calculationField);
	}
}
