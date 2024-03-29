#!/bin/sh

path=`realpath $0`
dir=`dirname $path`
cd "${dir}"
./*.out "$1" "$2"
# $dir now refers to the directory in which this script actually lives

