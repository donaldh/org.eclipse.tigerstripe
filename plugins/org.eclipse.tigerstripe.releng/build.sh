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

${JAVA_HOME}/java -Xmx1024m -cp pde/org.eclipse.releng.basebuilder/plugins/org.eclipse.equinox.launcher.jar \
	org.eclipse.core.launcher.Main -application org.eclipse.ant.core.antRunner "$@"
