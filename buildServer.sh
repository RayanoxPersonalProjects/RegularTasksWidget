#!/bin/sh

TRAVIS_BUILD_DIR=Server

echo $TRAVIS_BUILD_DIR
cd $TRAVIS_BUILD_DIR
mvn package