#!/bin/sh

VER=3.3.5

echo "============================================================"
echo "Overiding variables.less"
echo "============================================================"
cp variables.less bootstrap/less/

echo "============================================================"
echo "Diffing variables.less"
echo "============================================================"
diff bootstrap/less/variables.less bootstrap-$VER/less/variables.less

cd bootstrap

echo "============================================================"
echo "Generating customized bootstrap"
echo "============================================================"
grunt dist
