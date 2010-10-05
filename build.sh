#!/bin/bash

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
rebuildTarget()
{
    rm -rf target
    mkdir -p target

    rsync -a --delete features target/ || exit 1
    rsync -a --delete plugins target/ || exit 1
    rsync -a --delete releng target/ || exit 1
    rsync -a --delete pom.xml target/ || exit 1

#    SETTINGS=target/$MAVEN/conf/settings.xml
#    mv $SETTINGS $SETTINGS.org
#    sed '/<settings>/ a\
#\
#  <proxies>\
#   <proxy>\
#      <active>true</active>\
#      <protocol>http</protocol>\
#      <host>proxy-sjc-1.cisco.com</host>\
#      <port>80</port>\
#      <nonProxyHosts>*.cisco.com|localhost</nonProxyHosts>\
#    </proxy>\
#  </proxies>\
#' $SETTINGS.org >$SETTINGS
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
rsync -av --delete features target/ || exit 1
rsync -av --delete plugins target/ || exit 1
rsync -av --delete releng target/ || exit 1
rsync -av  pom.xml target/ || exit 1

# ------------------------------------------------------------------------------ 
if [ ! -z "$BUILD_VERSION" ]; then
	chmod +x ./patch_version.sh
    ./patch_version.sh . target $BUILD_VERSION
fi

# Run pre-build script
chmod +x ./pre-build.sh
./pre-build.sh

#./$MAVEN/bin/mvn --fail-at-end -Dtycho.showEclipseLog=true  -Dosgi.ws=cocoa -Dmaven.test.skip=$MAVEN_TEST_SKIP install -e -B

echo $MAVEN_HOME
echo ${MAVEN_HOME}

(cd target/
 find * -type d -name target -exec rm -rf {} \; 2>/dev/null
 /auto/surf-tp/tools/maven/apache-maven-3.0-beta-3/mvn \
    -e -B \
    --fail-at-end \
    -Dtycho.showEclipseLog=true \
    -Dmaven.test.skip=$MAVEN_TEST_SKIP \
    install
 cd ..
 cp -rf target/releng/org.eclipse.tigerstripe.update-site/target/site target
 rm -rf /auto/tigerstripe/xmpsdk/tigerstripe-test-p2/*
 cp -rf target/releng/org.eclipse.tigerstripe.update-site/target/site/* /auto/tigerstripe/xmpsdk/tigerstripe-test-p2
)

