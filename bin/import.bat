@echo off
set OBPM_PATH=../
echo 参数一：filePath（文件完整名称，如：d:/exp.zip） 
echo 参数二：applicationid（应用ID）
java -cp %OBPM_PATH%\WEB-INF\classes -Djava.ext.dirs=%OBPM_PATH%\WEB-INF\lib -Dfilepath=%1 -Dapplicationid=%2 cn.myapps.core.expimp.imp.util.ImpMain