#!/bin/bash
export TEST_NAME=page_labels
javac -cp .:../../../lib/fsdk.jar *.java
java -Djava.library.path=../../../lib -classpath .:../../../lib/fsdk.jar ${TEST_NAME}
