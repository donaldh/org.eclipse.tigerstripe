#!/bin/sh
# 
# Copyright (c) 2008 Cisco Systems, Inc.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#    Cisco Systems, Inc. - erdillon
#
# Usage: buildWrapper.sh <build-xxx.xml>
#

####
## YOU NEED TO ADJUST THESE TO YOUR LOCAL SETTINGS
##
export	ANT_HOME=/usr/share/ant
export	JAVA_HOME=/System/Library/Frameworks/JavaVM.framework/Versions/1.5/Home
export	PATH=${JAVA_HOME}/bin:${PATH}:${ANT_HOME}/bin

## These mimic the environment that would be created by Hudson
export	BUILD_ID=1
export	TSDATE=`date "+%Y%m%d%H%M%S" | sed "s/[_-]//g" | awk '{ print substr($1, 0, 8)}'`
export	TSTIME=`date "+%Y%m%d%H%M%S" | sed "s/[_-]//g" | awk '{ print substr($1, 9, 4)}'`
export	WORKSPACE=/Users/erdillon/Documents/workspace/releng/org.eclipse.tigerstripe.releng/

## Base classpath for Eclipse Launcher	
export CLASSPATH=pde/org.eclipse.releng.basebuilder/plugins/org.eclipse.equinox.launcher.jar

## Adding jSch to classpath to enable "scp" task in Ant.
export CLASSPATH=${CLASSPATH}:lib/jsch-0.1.38.jar


${JAVA_HOME}/bin/java -Xmx1024m -cp ${CLASSPATH} \
	org.eclipse.core.launcher.Main -application org.eclipse.ant.core.antRunner "$@"
