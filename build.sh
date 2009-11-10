#!/bin/bash

DATE=`date`

#MAVEN_OPTS=-Xmx1024m -DsocksProxyHost=proxy-sjc-1.cisco.com -DsocksProxyPort=1080

MAVEN=tycho-distribution-0.4.0-DEV-3023
MAVEN=tycho-distribution-0.4.0-DEV-3076
#MAVEN=tycho-distribution-0.4.0-DEV-3170

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

    (cd target; tar xjf /auto/surf-tp/tools/maven/$MAVEN-bin.tar.bz2; cd ..)

    rsync -a --delete org.eclipse.tigerstripe target/ || exit 1

    SETTINGS=target/$MAVEN/conf/settings.xml
    mv $SETTINGS $SETTINGS.org
    sed '/<settings>/ a\
\
  <proxies>\
   <proxy>\
      <active>true</active>\
      <protocol>http</protocol>\
      <host>proxy-sjc-1.cisco.com</host>\
      <port>80</port>\
      <nonProxyHosts>*.cisco.com|localhost</nonProxyHosts>\
    </proxy>\
  </proxies>\
' $SETTINGS.org >$SETTINGS
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
rsync -av --exclude '.svn/' org.eclipse.tigerstripe target/ || exit 1

# ------------------------------------------------------------------------------ 
if [ ! -z "$BUILD_VERSION" ]; then
		chmod +x ./patch_version.sh
    ./patch_version.sh . target $BUILD_VERSION
fi

#./$MAVEN/bin/mvn --fail-at-end -Dtycho.showEclipseLog=true  -Dosgi.ws=cocoa -Dmaven.test.skip=$MAVEN_TEST_SKIP install -e -B

(cd target/org.eclipse.tigerstripe
 find * -type d -name target -exec rm -rf {} \; 2>/dev/null
 ../$MAVEN/bin/mvn \
    -e -B \
    --fail-at-end \
    -Dhttp.proxyHost=proxy-sjc-1.cisco.com -Dhttp.nonProxyHosts="*.cisco.com|localhost" -Dhttp.proxyPort=80 \
    -Dtycho.showEclipseLog=true \
    -Dmaven.repo.local=$HOME/.m2/tycho-repository \
    -Dmaven.test.skip=$MAVEN_TEST_SKIP \
    install
# cp -Rf "$WORKSPACE/target/xmp_sdk/releng/com.cisco.xmp.sdk.site/trunk/web" /auto/tigerstripe/xmpsdk-updates
# cp -Rf "$WORKSPACE/target/xmp_sdk/releng/com.cisco.xmp.sdk.site/trunk/index.html" /auto/tigerstripe/xmpsdk-updates
# cp -Rf "$WORKSPACE/target/xmp_sdk/releng/com.cisco.xmp.sdk.site/trunk/target/site"/* /auto/tigerstripe/xmpsdk-updates
)

