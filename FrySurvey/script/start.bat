rem set JAVA_HOME="C:\Program Files\Java\jdk1.7.0_40"
set APP_NAME=FrySurvey
set INSTALL_DIR=C:\FryScop\%APP_NAME%
set JAR=%INSTALL_DIR%\lib\*.jar

if "%JAVA_HOME%" == "" set JAVA_HOME="C:\Program Files\Java\jdk1.7.0_40"

echo %INSTALL_DIR%


setlocal enableDelayedExpansion
set CP=%INSTALL_DIR%\conf\
for /r %INSTALL_DIR%\lib\ %%i in (*.jar) do set CP=!CP!;%%i

set START_CLASS=fr.fryscop.FrySurvey
%JAVA_HOME%\bin\java  -cp %CP% %START_CLASS%



pause