#!/bin/bash

export MAVEN_OPTS=-Xmx1024m

if [ ! -e "$WORKSPACE/repository" ]; then
	mkdir "$WORKSPACE/repository"
fi

SETTINGS=$WORKSPACE/settings.xml
if [ -e ~/.m2/settings.xml ]; then
    sed -e "s&<!-- <localRepository/> -->&<localRepository>$WORKSPACE/target/repository</localRepository>&g" ~/.m2/settings.xml > $SETTINGS
else
	echo "No local .m2/settings.xml file!"
fi

"$MAVEN_HOME/bin/mvn" -s "$SETTINGS" -Dtycho.showEclipseLog=true clean install