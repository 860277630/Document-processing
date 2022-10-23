#!/bin/bash
export TEST_NAME=marked_content
javac -cp .:../../../lib/fsdk.jar *.java
java -Djava.library.path=../../../lib -classpath .:../../../lib/fsdk.jar ${TEST_NAME}
