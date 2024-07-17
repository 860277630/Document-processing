@echo off<nul 3>nul
setlocal
set DEMO_NAME=office2pdf
javac.exe -cp .;D:\foxitpdfsdk_8_3_1_win_java/lib/fsdk.jar office2pdf.java
java.exe -Djava.library.path=D:\foxitpdfsdk_8_3_1_win_java/lib -classpath .;D:\foxitpdfsdk_8_3_1_win_java/lib/fsdk.jar %DEMO_NAME%
endlocal
pause
exit