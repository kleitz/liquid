#!/bin/sh

wget https://github.com/twbs/bootstrap/archive/v3.3.5.zip

cp variables.less bootstrap/less/

cd bootstrap

npm install

grunt dist
