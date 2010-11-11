#!/bin/bash

export MAVEN_OPTS=-Xmx1024m

if[ ! -e $WORKSPACE/repository ]; then
	echo " ## Creating repository directory in workspace."
	mkdir -p $WORKSPACE/repository
fi
