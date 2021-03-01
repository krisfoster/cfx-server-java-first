@echo off

SET ARTIFACT=cxf-server-java-first
SET MAINCLASS=com.cxf.server.ServerApplication
SET VERSION=0.0.1-SNAPSHOT

RMDIR /Q/S build
mkdir build\native-image
mkdir build\graal-agent\META-INF\native-image
SET rootPath=%CD%\%1
echo "root" %rootPath%
SET GRAAL_AGENT_PATH=%rootPath%build\graal-agent
echo "GRAAL_AGENT_PATH" %GRAAL_AGENT_PATH%

echo "Packaging %ARTIFACT% with Gradle"
call gradlew build

echo "***************************** Setting JAR ************************************************"
SET JAR="%ARTIFACT%-%VERSION%.jar"
RMDIR /Q/S %ARTIFACT%
echo "Unpacking %JAR%"
cd build/native-image
jar -xvf ../libs/%JAR%

echo "***************************** Copy jars & classes *****************************************"
xcopy "META-INF" "BOOT-INF/classes" /s /e

SET CP=BOOT-INF\classes;BOOT-INF\lib\*

echo "***************************** CLASSPATH ***********************************************"
echo %CP%
echo "**************************************************************************************"


echo "============== RUNNING THE APPLICATION WITH THE AGENT TO POPULATE CONFIGURATION FILES ========="
echo "(for debug see agent-output.txt)"
echo "Running for 100 seconds"
echo "Once tomcat starts successfully in new window hit ctrl+c to terminate tomcat gracefully. This is very important step!!!!!"

rem start "java-agent"  java -cp %CP% ^
rem   -agentlib:native-image-agent=config-output-dir=""%GRAAL_AGENT_PATH%"" ^
rem  %MAINCLASS% > agent-output.txt

rem TIMEOUT /T 100

echo "Killing..."

taskkill /FI "WindowTitle eq java-agent*" /T
 echo "*****************************GRAALVM_VERSION ******************************************"
 echo native-image --version
 echo "**************************************************************************************"

echo "***************************** CLASSPATH ***********************************************"
 rem set CP=%CP%;%GRAAL_AGENT_PATH%
 rem echo %CP%
 echo "**************************************************************************************"

echo "MAINCLASS" %MAINCLASS%

call native-image ^
        --no-server ^
        --no-fallback ^
        --enable-all-security-services ^
        -H:Name=%ARTIFACT%-agent ^
        -H:TraceClassInitialization=true ^
        -Dspring.native.remove-yaml-support=true ^
        -Dspring.xml.ignore=false ^
        -Dspring.spel.ignore=true ^
        -Dspring.native.remove-jmx-support=true ^
        -Dspring.native.verify=true ^
        --initialize-at-run-time=org.hibernate.validator.internal.engine.messageinterpolation.el.SimpleELContext ^
        -cp %CP% %MAINCLASS%
