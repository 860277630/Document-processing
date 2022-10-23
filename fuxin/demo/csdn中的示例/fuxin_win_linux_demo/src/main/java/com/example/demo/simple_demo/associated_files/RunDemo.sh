#!/bin/bash
export TEST_NAME=associated_files
javac -cp .:../../../lib/fsdk.jar *.java
java -Djava.library.path=../../../lib -classpath .:../../../lib/fsdk.jar ${TEST_NAME}
