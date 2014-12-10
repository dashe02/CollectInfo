@echo off
REM Copyright 1999-2012 Alibaba Group.                                      
REM                                                                         
REM Licensed under the Apache License, Version 2.0 (the "License");         
REM you may not use this file except in compliance with the License.        
REM You may obtain a copy of the License at                                 
REM                                                                         
REM      http://www.apache.org/licenses/LICENSE-2.0                         
REM                                                                         
REM Unless required by applicable law or agreed to in writing, software     
REM distributed under the License is distributed on an "AS IS" BASIS,       
REM WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
REM See the License for the specific language governing permissions and     
REM limitations under the License.

REM check JAVA_HOME & java
if "%JAVA_HOME%" == "" goto noJavaHome
if exist "%JAVA_HOME%\bin\java.exe" goto mainEntry
:noJavaHome
echo ---------------------------------------------------
echo Error: JAVA_HOME environment variable is not set. 
echo ---------------------------------------------------
goto end

:mainEntry
REM set JAVA_OPTS
set "JAVA_OPTS=-server -Xms500m -Xmx500m -Xmn256m -Xss128k"
REM performance Options
set "JAVA_OPTS=%JAVA_OPTS% -XX:+AggressiveOpts"
set "JAVA_OPTS=%JAVA_OPTS% -XX:+UseBiasedLocking"
set "JAVA_OPTS=%JAVA_OPTS% -XX:+UseFastAccessorMethods"
set "JAVA_OPTS=%JAVA_OPTS% -XX:+DisableExplicitGC"
set "JAVA_OPTS=%JAVA_OPTS% -XX:+UseParNewGC"
set "JAVA_OPTS=%JAVA_OPTS% -XX:+UseConcMarkSweepGC"
set "JAVA_OPTS=%JAVA_OPTS% -XX:+CMSParallelRemarkEnabled"
set "JAVA_OPTS=%JAVA_OPTS% -XX:+UseCMSCompactAtFullCollection"
set "JAVA_OPTS=%JAVA_OPTS% -XX:+UseCMSInitiatingOccupancyOnly"
set "JAVA_OPTS=%JAVA_OPTS% -XX:CMSInitiatingOccupancyFraction=75"
REM GC Log Options
REM set "JAVA_OPTS=%JAVA_OPTS% -XX:+PrintGCApplicationStoppedTime"
REM set "JAVA_OPTS=%JAVA_OPTS% -XX:+PrintGCTimeStamps"
REM set "JAVA_OPTS=%JAVA_OPTS% -XX:+PrintGCDetails"
REM debug Options
REM set "JAVA_OPTS=%JAVA_OPTS% -Xdebug -Xrunjdwp:transport=dt_socket,address=8065,server=y,suspend=n"

REM set HOME_DIR
set "CURR_DIR=%cd%"
cd ..
set "COLLECT_HOME=%cd%"
cd %CURR_DIR%
if exist "%COLLECT_HOME%\bin\startup.bat" goto okHome
echo ---------------------------------------------------
echo Error: COLLECT_HOME environment variable is not defined correctly.
echo ---------------------------------------------------
goto end

:okHome
set "APP_VERSION=1.2.6"

REM set COLLECT_CLASSPATH
set "COLLECT_CLASSPATH=%COLLECT_HOME%\conf;%COLLECT_HOME%\lib\classes"
set "COLLECT_CLASSPATH=%COLLECT_CLASSPATH%;%COLLECT_HOME%\lib\CollectInfo.jar"
set "COLLECT_CLASSPATH=%COLLECT_CLASSPATH%;%COLLECT_HOME%\lib\mina-core-2.0.2.jar"
set "COLLECT_CLASSPATH=%COLLECT_CLASSPATH%;%COLLECT_HOME%\lib\slf4j-api-1.5.11.jar"
set "COLLECT_CLASSPATH=%COLLECT_CLASSPATH%;%COLLECT_HOME%\lib\slf4j-simple-1.5.6.jar"
REM FOR %%I IN ("%COLLECT_HOME%\lib\*.jar") DO (
REM   set "COLLECT_CLASSPATH=%COLLECT_CLASSPATH%;%%I"
REM )
REM echo %COBAR_CLASSPATH%

REM startup Server
set "RUN_CMD="%JAVA_HOME%\bin\java.exe""
set "RUN_CMD=%RUN_CMD% -Dcobar.home="%COLLECT_HOME%""
set "RUN_CMD=%RUN_CMD% -classpath "%COLLECT_HOME%""
set "RUN_CMD=%RUN_CMD% %JAVA_OPTS%"
set "RUN_CMD=%RUN_CMD% org.collectinfo.StartCollect"
call %RUN_CMD%

:end
