'On Error Resume Next

'Connect to database.
Set cn = CreateObject("ADODB.Connection")
Set cmd = CreateObject("ADODB.Command")
cn.Open "DSN=xddb;UID=xddb_rt;PWD=helloworld;"

cmd.ActiveConnection = cn

'refresh Wookbok
RefreshWorkbook "E:\java\workspace\obpm2\src\main\webapp\core\fordemoreport\report.xls"

'Close the connection
Set cmd = Nothing
cn.close
Set cn = Nothing
'MsgBox "Done!"

Sub RefreshWorkbook(file)
   'Open the execel file
	Set wbook = CreateObject("excel.application")
    wbook.DisplayAlerts =false
    wbook.Workbooks.Open file

	'Refresh Pivot Table by name
    wbook.ActiveWorkbook.Sheets("按卖场汇总").PivotTables("PV_summaryBYstore").PivotCache.Refresh
    wbook.ActiveWorkbook.Sheets("按分类汇总").PivotTables("PV_summaryBYcatalog").PivotCache.Refresh
    'wbook_hq.visible = True
    wbook.Save

    'Close the excel file    
    wbook.Quit
    Set wbook = nothing
End Sub
