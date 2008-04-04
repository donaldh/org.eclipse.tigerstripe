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
# Usage: bootstrap.sh
#

####
## YOU NEED TO ADJUST THESE TO YOUR LOCAL SETTINGS
##
export	ANT_HOME=/usr/share/ant
export	JAVA_HOME=/System/Library/Frameworks/JavaVM.framework/Versions/1.5/Home
export	PATH=${JAVA_HOME}/bin:${PATH}:${ANT_HOME}/bin

${ANT_HOME}/bin/ant -f ./bootstrap.xml
