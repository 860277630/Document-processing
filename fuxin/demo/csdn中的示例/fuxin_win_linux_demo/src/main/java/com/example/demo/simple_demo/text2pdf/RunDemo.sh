#!/bin/bash
export TEST_NAME=text2pdf
javac -cp .:../../../lib/fsdk.jar *.java
java -Djava.library.path=../../../lib -classpath .:../../../lib/fsdk.jar ${TEST_NAME}
