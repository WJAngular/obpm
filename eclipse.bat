@echo off
@rem Change to current directory
@rem cd /d %~dp0

@echo ����Eclipse��Ŀ��������:
@call mvn eclipse:clean eclipse:eclipse
@pause