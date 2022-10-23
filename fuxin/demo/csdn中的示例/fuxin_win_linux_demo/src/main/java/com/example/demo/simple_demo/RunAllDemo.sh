#!/bin/sh

for DEMO in *
do
	if [ -d $DEMO ]
	then
		if [ -e $DEMO/RunDemo.sh ]
		then
			cd $DEMO
			echo "$Demo running"
			sh RunDemo.sh
			cd ..
			echo "$Demo finished. Press enter to continue..."
			read -p "$*" a
		fi
	fi
done
