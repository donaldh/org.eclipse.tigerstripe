#!/bin/bash
# This script is executed before the Tycho build begins, once checkout occurred
# and versions have been set straight.
#

DATE=`date`
ANT=/auto/tigerstripe/local/ant/bin/ant

# Run javadoc in core plugin so it gets packaged up as part of the documentation
echo "Running Javadoc in org.eclipse.tigerstripe.workbench.base"
cd plugins/org.eclipse.tigerstripe.workbench.base
$ANT -f javadoc.xml
echo "Running Javadoc in org.eclipse.tigerstripe.annotation.core"
cd ../org.eclipse.tigerstripe.annotation.core
$ANT -f javadoc.xml
cd ../..

