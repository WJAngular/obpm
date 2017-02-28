@echo off
	echo 切换目录至：%~dp0
	cd /d %~dp0
	set CURRENT_DIR=%cd%
	
	if defined MAVEN_REPOSITORY (
	echo 加载MAVEN库%MAVEN_REPOSITORY%
	) else (
	echo "MAVEN_REPOSITORY"变量尚未定义，请指定maven reposiotry目录路径
	)
	rem set方法如果在if中使用无法改变外面区域的值
	set "LIB_PATH=%LIB_PATH%;%MAVEN_REPOSITORY%"
	echo LIB_PATH: %LIB_PATH%
	
	if exist %CURRENT_DIR%\..\target\classes (
	echo CLASS_PATH: %CURRENT_DIR%\..\target\classes
	java -cp %CURRENT_DIR%\..\target\classes -Djava.ext.dirs=%LIB_PATH%;%CURRENT_DIR%\..\src\main\webapp\WEB-INF\lib cn.myapps.version.transfer.AllTransfer version=2.4 transfer=permission
	)
	if exist %CURRENT_DIR%\..\src\main\webapp\WEB-INF\classes (
	echo CLASS_PATH: %CURRENT_DIR%..\src\main\webapp\WEB-INF\classes
	java -cp %CURRENT_DIR%\..\src\main\webapp\WEB-INF\classes -Djava.ext.dirs=%LIB_PATH%;%CURRENT_DIR%\..\src\main\webapp\WEB-INF\lib cn.myapps.version.transfer.AllTransfer version=2.4 transfer=all
	)
	pause