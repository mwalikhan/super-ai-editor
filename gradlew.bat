@ECHO OFF
SET DIRNAME=%~dp0
IF "%DIRNAME%" == "" SET DIRNAME=.
SET APP_BASE_NAME=%~n0
SET APP_NAME=Gradle
SET CLASSPATH=%DIRNAME%\gradle\wrapper\gradle-wrapper.jar

"%JAVA_HOME%\bin\java.exe" -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*
