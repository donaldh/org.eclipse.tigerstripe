#!/bin/bash
#
# This script is a simple copy to an eclipse.org hosted update site
#
#  ./upload.sh
#

if [ $# -ne 1 ]; then
    echo "usage: upload.sh destination-dir"
    exit 1
fi

DST=$1; shift
SITE=/home/data/httpd/download.eclipse.org/technology/tigerstripe/updates/sid-mappings
SITE_NAME="Updates (Release)"
PROMOTED_BUILD=`echo $PROMOTED_URL | awk '{split($0, a, "/");print a[6]}'`
 
echo "Uploading build ${PROMOTED_BUILD} to $SITE_NAME"
cd ${WORKSPACE}/../builds/${PROMOTED_BUILD}/archive/target/site
scp -rv * jstrawn@download1.eclipse.org:"$SITE"