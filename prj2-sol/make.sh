#!/bin/sh

path=`realpath $0`
dir=`dirname $path`
cd "${dir}"
gcc -g -o program.out *.c
# $dir now refers to the directory in which this script actually lives


