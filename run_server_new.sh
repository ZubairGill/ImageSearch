#!/bin/bash

if [ $# -eq 0 ]; then
	echo "Action argument not found."
	echo "Possible arguments: start/stop/status"
else
	ACTION=$1
fi


function run {

	nohup java -jar target/googleImagesSearch-0.0.1-SNAPSHOT.jar > logs.txt &
	echo $! > pid.dat
	echo "GOOGLE search Server started succesfully..."
}


if [[ $ACTION == 'start' ]]; then
	
	if [ ! -s pid.dat ]; then

		if [ -s pid.dat ]; then
			
			PID=`cat pid.dat`
			if ! ps -p $PID > null ; then
				run
			fi

		else
			run
		fi
	else
		PID=`cat pid.dat`
		echo "GOOGLE image  Service already running under PID $PID"
	fi

elif [[ $ACTION == 'stop' ]]; then

		PID=`cat pid.dat`
		
		if [ ! -z "$PID" ]; then

			sudo kill -9 $PID
			> pid.dat
			echo "GOOGLE image Service stoped successfully."

		else 

			echo "No Server Running..."
		fi

elif [[ $ACTION == 'status' ]]; then

			if [ -s pid.dat ]; then

				PID=`cat pid.dat`

				if ps -p $PID > null ;then
					echo "GOOGLE image Service running under PID $PID"
				else

					> pid.dat
					echo "No Service Running."
				fi
			else
				echo "No Service Running."
			fi
else
	echo "Unknown arguments' $*"
fi