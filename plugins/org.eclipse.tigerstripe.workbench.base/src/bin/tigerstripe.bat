@REM *******************************************************************************
@REM   Copyright (c) 2007 Cisco Systems, Inc.
@REM   All rights reserved. This program and the accompanying materials
@REM   are made available under the terms of the Eclipse Public License v1.0
@REM   which accompanies this distribution, and is available at
@REM   http://www.eclipse.org/legal/epl-v10.html
@REM  
@REM   Contributors:
@REM      E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
@REM      J. Strawn (Cisco Systems, Inc.) - modify to run via headless Eclipse
@REM *******************************************************************************/

@REM ----------------------------------------------------------------------------
@REM Tigerstripe Start Up Batch script
@REM
@REM Required ENV vars:
@REM JAVA_HOME - location of a JDK home dir
@REM TIGERSTRIPE_HOME - location of Tigerstripe install home dir
@REM TIGERSTRIPE_OPTS - parameters passed to the eclipse command line
@REM ----------------------------------------------------------------------------

@echo off

@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" @setlocal

@REM ==== START VALIDATION ====
if not "%JAVA_HOME%" == "" goto OkJHome

echo.
echo ERROR: JAVA_HOME not found in your environment.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation
echo.
goto end

:OkJHome
if exist "%JAVA_HOME%\bin\java.exe" goto chkMHome

echo.
echo ERROR: JAVA_HOME is set to an invalid directory.
echo JAVA_HOME = %JAVA_HOME%
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation
echo.
goto end

:chkMHome
if not "%TIGERSTRIPE_HOME%"=="" goto valMHome

echo.
echo ERROR: TIGERSTRIPE_HOME not found in your environment.
echo Please set the TIGERSTRIPE_HOME variable in your environment to match the
echo location of the Tigerstripe installation
echo.
goto end

:valMHome
if exist "%TIGERSTRIPE_HOME%\bin\tigerstripe.bat" goto init

echo.
echo ERROR: TIGERSTRIPE_HOME is set to an invalid directory.
echo TIGERSTRIPE_HOME = %TIGERSTRIPE_HOME%
echo Please set the TIGERSTRIPE_HOME variable in your environment to match the
echo location of the Tigerstripe installation
echo.
goto end
@REM ==== END VALIDATION ====

:init
@REM Decide how to startup depending on the version of windows

@REM -- Win98ME
if NOT "%OS%"=="Windows_NT" goto Win9xArg

@REM -- 4NT shell
if "%eval[2+2]" == "4" goto 4NTArgs

@REM -- Regular WinNT shell
set TIGERSTRIPE_CMD_LINE_ARGS=%*
goto endInit

@REM The 4NT Shell from jp software
:4NTArgs
set TIGERSTRIPE_CMD_LINE_ARGS=%$
goto endInit

:Win9xArg
@REM Slurp the command line arguments.  This loop allows for an unlimited number
@REM of agruments (up to the command line limit, anyway).
set TIGERSTRIPE_CMD_LINE_ARGS=
:Win9xApp
if %1a==a goto endInit
set TIGERSTRIPE_CMD_LINE_ARGS=%TIGERSTRIPE_CMD_LINE_ARGS% %1
shift
goto Win9xApp

@REM Reaching here means variables are defined and arguments have been captured
:endInit
if "%TIGERSTRIPE_OPTS%"=="" SET TIGERSTRIPE_OPTS=-vmargs -Xmx512m
SET TIGERSTRIPE_ECLIPSE_EXE="%TIGERSTRIPE_HOME%\..\eclipsec.exe"
SET TIGERSTRIPE_HEADLESS_APP=org.eclipse.tigerstripe.workbench.headless.tigerstripe

@REM echo %TIGERSTRIPE_OPTS%
@REM cho %TIGERSTRIPE_ECLIPSE_EXE% -nosplash %TIGERSTRIPE_OPTS% -application %TIGERSTRIPE_HEADLESS_APP%

@REM Start TIGERSTRIPE without TIGERSTRIPE_HOME_LOCAL override
%TIGERSTRIPE_ECLIPSE_EXE% -nosplash -application %TIGERSTRIPE_HEADLESS_APP% %TIGERSTRIPE_OPTS%
goto :end

:end
@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" goto endNT

@REM For old DOS remove the set variables from ENV - we assume they were not set
@REM before we started - at least we don't leave any baggage around
set TIGERSTRIPE_JAVA_EXE=
set TIGERSTRIPE_HEADLESS_APP=
set TIGERSTRIPE_CMD_LINE_ARGS=
goto postExec

:endNT
@endlocal











