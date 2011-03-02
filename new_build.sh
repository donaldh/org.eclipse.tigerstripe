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

if [ ! -z "${EXECUTOR_NUMBER}" ]; then
    export DISPLAY=localhost:$((1+${EXECUTOR_NUMBER}%4)).0
fi

export MAVEN_OPTS="-Xms256m -Xmx1024m -XX:MaxPermSize=512m"

# Run pre-build script
chmod +x ./pre-build.sh
./pre-build.sh

"$MAVEN_HOME/bin/mvn" -fae -B -Dtycho.showEclipseLog=true -Dmaven.repo.local=$WORKSPACE/repository clean install

# move update site
rm -rf $WORKSPACE/site
SITE=$WORKSPACE/releng/org.eclipse.tigerstripe.update-site/target/site
rsync -a --delete $SITE $WORKSPACE/

echo "new_build.sh done."
