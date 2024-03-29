#!/bin/sh

# Sets dir to the directory containing this script
dir=$(dirname "$0")

# Compile Java files directly in the current directory
javac $dir/Parser.java 