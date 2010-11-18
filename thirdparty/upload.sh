#!/bin/bash
#
# This script is a simple copy to an eclipse.org hosted update site for the sid mapping plugin
#
#  ./upload.sh
#

DST=$1; shift
SITE=/home/data/httpd/download.eclipse.org/technology/tigerstripe/updates/thirdparty
SITE_NAME="Updates (Release)"
PROMOTED_BUILD=`echo $PROMOTED_URL | awk '{split($0, a, "/");print a[6]}'`
 
echo "Uploading build ${PROMOTED_BUILD} to $SITE_NAME"
cd ${WORKSPACE}/../builds/${PROMOTED_BUILD}/archive/org.eclipse.tigerstripe.thirdparty.tmf.sid.update-site/target/site
scp -rv * edillon@download1.eclipse.org:"$SITE"