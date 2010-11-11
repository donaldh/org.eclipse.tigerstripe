#!/bin/bash

export MAVEN_OPTS=-Xmx1024m

if [ ! -e "$WORKSPACE/repository" ]; then
	echo "It exists"
fi