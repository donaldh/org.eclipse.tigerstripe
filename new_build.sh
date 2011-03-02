#!/bin/bash
export PRJ=$(cd `dirname $0`; pwd)
if [ -z "$WORKSPACE" ]; then
    export WORKSPACE=$PRJ
fi
if [ -z "$MAVEN_HOME" ]; then
    echo "MAVEN_HOME not set"
    exit 1
fi
DATE=`date`

set -x
env|sort
pwd

# Run pre-build script
chmod +x ./pre-build.sh
./pre-build.sh

"$MAVEN_HOME/bin/mvn" -fae -B -Dtycho.showEclipseLog=true -Dmaven.repo.local=$WORKSPACE/repository clean install

# move update site
rm -rf $WORKSPACE/site
SITE=/releng/org.eclipse.tigerstripe.update-site/target/site
rsync -a --delete $SITE $WORKSPACE/

echo "new_build.sh done."
