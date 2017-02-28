@echo off
@rem Change to current directory
@rem cd /d %~dp0

@echo 构建Eclipse项目工程配置:
@call mvn eclipse:clean eclipse:eclipse
@pause