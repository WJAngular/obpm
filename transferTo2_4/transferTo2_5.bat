@echo off
	echo �л�Ŀ¼����%~dp0
	cd /d %~dp0
	set CURRENT_DIR=%cd%
	
	if defined MAVEN_REPOSITORY (
	echo ����MAVEN��%MAVEN_REPOSITORY%
	) else (
	echo "MAVEN_REPOSITORY"������δ���壬��ָ��maven reposiotryĿ¼·��
	)
	rem set���������if��ʹ���޷��ı����������ֵ
	set "LIB_PATH=%LIB_PATH%;%MAVEN_REPOSITORY%"
	echo LIB_PATH: %LIB_PATH%
	
	if exist %CURRENT_DIR%\..\target\classes (
	echo CLASS_PATH: %CURRENT_DIR%\..\target\classes
	java -cp %CURRENT_DIR%\..\target\classes -Djava.ext.dirs=%LIB_PATH%;%CURRENT_DIR%\..\src\main\webapp\WEB-INF\lib cn.myapps.version.transfer.AllTransfer version=2.5 transfer=all
	)
	if exist %CURRENT_DIR%\..\src\main\webapp\WEB-INF\classes (
	echo CLASS_PATH: %CURRENT_DIR%..\src\main\webapp\WEB-INF\classes
	java -cp %CURRENT_DIR%\..\src\main\webapp\WEB-INF\classes -Djava.ext.dirs=%LIB_PATH%;%CURRENT_DIR%\..\src\main\webapp\WEB-INF\lib cn.myapps.version.transfer.AllTransfer version=2.5 transfer=all
	)
	pause