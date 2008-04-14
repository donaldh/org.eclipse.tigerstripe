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
export	ANT_HOME=/auto/tigerstripe/java/apache-ant-1.7.0
export	JAVA_HOME=/auto/tigerstripe/java/jdk1.6.0_10
export	PATH=${JAVA_HOME}/bin:${PATH}:${ANT_HOME}/bin

####
## NO CHANGE NEEDED BELOW THIS LINE
## It relies on the Environment variables set by Hudson to extract timestamp, etc.
##
cd $WORKSPACE/org.eclipse.tigerstripe/plugins/org.eclipse.tigerstripe.releng/

export	TSDATE=`echo $BUILD_ID | sed "s/[_-]//g" | awk '{ print substr($1, 0, 8)}'`
export	TSTIME=`echo $BUILD_ID | sed "s/[_-]//g" | awk '{ print substr($1, 9, 4)}'`

./build.sh -f $@

