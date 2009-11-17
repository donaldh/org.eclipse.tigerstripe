#!/bin/bash
#
# This script is a simple copy to a locally mount directory that is mirrored
# and served as the Update site.
#
#  ./copy-to-updatesite.sh <destination-dir>
#

if [ $# -ne 1 ]; then
    echo "usage: copy-to-updatesite.sh destination-dir"
    exit 1
fi


DST=$1; shift
DST=$(mkdir -p $DST; cd $DST; pwd)
 
cp -Rf "$WORKSPACE/target/xmp_sdk/releng/com.cisco.xmp.sdk.site/trunk/web" $DST
cp -Rf "$WORKSPACE/target/xmp_sdk/releng/com.cisco.xmp.sdk.site/trunk/index.html" $DST
cp -Rf "$WORKSPACE/target/xmp_sdk/releng/com.cisco.xmp.sdk.site/trunk/target/site"/* $DST
