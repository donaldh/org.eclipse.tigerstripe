#!/bin/bash
#
# This script is a simple copy to a locally mount directory that is mirrored
# and served as the Update site.
#
#  ./upload-to.sh <unstable|interim|release>
#

if [ $# -ne 1 ]; then
    echo "usage: upload-to.sh destination-dir"
    exit 1
fi


DST=$1; shift
TS_DOWNLOAD=/home/data/users/edillon/downloads/technology/tigerstripe
SITE="$TS_DOWNLOAD"/updates-3.5-unstable
SITE_NAME=updates-3.5-unstable
PROMOTED_BUILD=`echo $PROMOTED_URL | awk '{split($0, a, "/");print a[6]}'`

case "$DST" in
release)
SITE="$TS_DOWNLOAD"/updates
SITE_NAME="updates (Release)"
;;
interim)
SITE="$TS_DOWNLOAD"/updates-3.5-interim
SITE_NAME="updates-3.5-interim"
;;
*)
SITE="$TS_DOWNLOAD"/updates-3.5-unstable
SITE_NAME="updates-3.5-unstable"
;;
esac
 
echo "Uploading build ${PROMOTED_BUILD} to $SITE_NAME"
cd ${WORKSPACE}/../builds/${PROMOTED_BUILD}/archive/target/site
scp -rv * edillon@download1.eclipse.org:"$SITE"
