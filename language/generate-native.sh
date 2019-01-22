#!/bin/bash
# This is executed in the context of the language folder. So we have to move up and to the bin folder
cd ../bin
if ! [ -z $(which native-image) ]; then
    pwd
    OS=$(uname -s | tr '[:upper:]' '[:lower:]')
    ARCH=$(arch)
    cp blz-ospl.jar blz-"$OS"-"$ARCH"-native.jar
    native-image -jar blz-"$OS"-"$ARCH"-native.jar
    rm blz-"$OS"-"$ARCH"-native.jar
fi