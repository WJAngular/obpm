@echo off
set OBPM_PATH=../
echo ����һ��filePath���ļ��������ƣ��磺d:/exp.zip�� 
echo ��������applicationid��Ӧ��ID��
java -cp %OBPM_PATH%\WEB-INF\classes -Djava.ext.dirs=%OBPM_PATH%\WEB-INF\lib -Dfilepath=%1 -Dapplicationid=%2 cn.myapps.core.expimp.imp.util.ImpMain