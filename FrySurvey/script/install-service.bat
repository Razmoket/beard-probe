@rem echo off


setlocal
@rem note that if JVM not found, service 'does not report an error' when startup fails, although event logged

set APP_NAME=FrySurvey
set INSTALL_DIR="C:\FryScop\%APP_NAME%"
set JAR=%INSTALL_DIR%\lib\%APP_NAME%.jar
set START_CLASS=fr.fryscop.FrySurvey
set description="FryScop - Service de gestion de sondes"


if "%JAVA_HOME%" == "" set JAVA_HOME="C:\Program Files\Java\jdk1.7.0_40"
set JVMDIR=%JAVA_HOME%\jre\bin\server
set JSBINDIR=%CD%
set JSEXE=script\JavaService.exe
set SSBINDIR=%JSBINDIR%

cd %INSTALL_DIR%
@echo . Using following version of JavaService executable:
@echo .
"%JSEXE%" -version
@echo .


@echo Installing service... Press Control-C to abort
@pause
@echo .

setlocal enableDelayedExpansion
set CP=%INSTALL_DIR%\conf\
for /r %INSTALL_DIR%\lib\ %%i in (*.jar) do set CP=!CP!;%%i

%JSEXE% -install %APP_NAME% %JVMDIR%\jvm.dll -cp %CP%  -start %START_CLASS% -params start memory -out log\stdout.log -err log\stderr.log -current %INSTALL_DIR% -manual -description %description%
@echo .


@echo Starting service... Press Control-C to abort
@pause
@echo .
net start %APP_NAME%
@echo .

