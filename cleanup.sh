#!/bin/bash


# Change this to your netid
netid=gxg171430

#
# Root directory of your project
PROJDIR=/home/010/g/gx/gxg171430/CS6378/AOS2

#
# Directory where the config file is located on your local system
CONFIGLOCAL=$PROJDIR/config.txt

n=0

cat $CONFIGLOCAL | sed -e "s/#.*//" | sed -e "/^\s*$/d" |
(
    read i
    echo $i
    read root
    echo $root
    while [[ $n -lt $i ]]
    do
        read line
        host=$( echo $line | awk '{ print $1 }' )

        echo $host
        bash -c "ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no $netid@$host.utdallas.edu killall -u $netid" &
        sleep 1

        n=$(( n + 1 ))
    done
   
)


echo "Cleanup complete"
