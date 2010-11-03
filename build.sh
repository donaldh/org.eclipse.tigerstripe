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

x=${MAVEN_TEST_SKIP:=false}
export MAVEN_OPTS=-Xmx1024m

if [ ! -z "${EXECUTOR_NUMBER}" ]; then
    export DISPLAY=localhost:$((1+${EXECUTOR_NUMBER}%4)).0
fi

set -x

env|sort
pwd

# ------------------------------------------------------------------------------
mkdir -p $WORKSPACE/target/repository
SETTINGS=$WORKSPACE/target/settings.xml
if [ -e ~/.m2/settings.xml ]; then
    sed -e "s&<!-- <localRepository/> -->&<localRepository>$WORKSPACE/target/repository</localRepository>&g" \
        ~/.m2/settings.xml \
        > $SETTINGS
else
    cat >$SETTINGS <<EOF
<settings>
  <localRepository>$WORKSPACE/target/repository</localRepository>
</settings>
EOF
fi

# ------------------------------------------------------------------------------
rebuildTarget()
{
    rm -rf target
    mkdir -p target

    rsync -a --delete features target/ || exit 1
    rsync -a --delete plugins target/ || exit 1
    rsync -a --delete releng target/ || exit 1
	rsync -a --delete tests target/ || exit 1
	rsync -a --delete samples target/ || exit 1
    rsync -a --delete pom.xml target/ || exit 1
}

# ------------------------------------------------------------------------------
if [ -d target ]; then
    if find . -type d -name org.eclipse.tigerstripe -maxdepth 1 -cnewer target 2>/dev/null | grep -q org.eclipse.tigerstripe ; then 
        echo "[info] newer Tigerstripe codebase found: rebuilding target hierarchy"
        rebuildTarget
    fi
else
    echo "[info] non-existing target hierarchy: rebuilding"
    rebuildTarget
fi

# ------------------------------------------------------------------------------
#rsync -av --delete --exclude "CVS/" features plugins relengs tests samples pom.xml target/
rsync -av --delete --exclude "CVS/" features plugins releng tests samples pom.xml target/

# ------------------------------------------------------------------------------ 
if [ ! -z "$BUILD_VERSION" ]; then
	chmod +x ./patch_version.sh
    ./patch_version.sh . target $BUILD_VERSION
fi

# Run pre-build script
chmod +x ./pre-build.sh
./pre-build.sh

#./$MAVEN/bin/mvn --fail-at-end -Dtycho.showEclipseLog=true  -Dosgi.ws=cocoa -Dmaven.test.skip=$MAVEN_TEST_SKIP install -e -B

(cd target/
 find * -type d -name target -exec rm -rf {} \; 2>/dev/null
 #"$MAVEN_HOME/bin/mvn" -B -X -fae -s "$SETTINGS" -Dtycho.showEclipseLog=true -Dmaven.test.skip=false -Dcom.xored.q7.location=/auto/xmpsdk/q7/launcher -Dosgi.os=linux -Dosgi.ws=gtk -Dosgi.arch=x86 -Dcom.xored.directorPlatformPath=/auto/xmpsdk/eclipse clean install
 "$MAVEN_HOME/bin/mvn" -B -s "$SETTINGS" -Dtycho.showEclipseLog=true -Dmaven.test.skip=false -Dcom.xored.q7.location=/auto/xmpsdk/q7/launcher -Dosgi.os=linux -Dosgi.ws=gtk -Dosgi.arch=x86 -Dcom.xored.directorPlatformPath=/auto/xmpsdk/eclipse clean install
 cd ..
 cp -rf target/releng/org.eclipse.tigerstripe.update-site/target/site target
 rm -rf /auto/tigerstripe/xmpsdk/tigerstripe-test-p2/*
 cp -rf target/releng/org.eclipse.tigerstripe.update-site/target/site/* /auto/tigerstripe/xmpsdk/tigerstripe-test-p2
)
