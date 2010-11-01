#!/bin/bash
#
# This script is a simple copy to a locally mount directory that is mirrored
# and served as the Update site.
#
#  ./upload-to.sh <iteration|release>
#

if [ $# -ne 1 ]; then
    echo "usage: upload-to.sh destination-dir"
    exit 1
fi

DST=$1; shift
TS_UPLOAD=/home/data/httpd/download.eclipse.org/technology/tigerstripe/updates/3.6
PROMOTED_BUILD=`echo $PROMOTED_URL | awk '{split($0, a, "/");print a[6]}'`

case "$DST" in
release)
SITE="$TS_DOWNLOAD"/release
SITE_NAME="updates (Release)"
;;
interim)
SITE="$TS_DOWNLOAD"/iteration
SITE_NAME="updates (Iteration)"
;;
esac
 
echo "Uploading build ${PROMOTED_BUILD} to $SITE_NAME"
cd ${WORKSPACE}/../builds/${PROMOTED_BUILD}/archive/target/site
scp -rv * edillon@download1.eclipse.org:"$SITE"
