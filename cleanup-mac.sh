#!/bin/bash


# Change this to your netid
netid=yxk171930

#
# Root directory of your project
PROJDIR=/home/012/y/yx/yxk171930/AOS2

#
# Directory where the config file is located on your local system
CONFIGLOCAL=$HOME/SFTP/AOS2/config.txt

n=0

cat $CONFIGLOCAL | sed -e "s/#.*//" | sed -e "/^\s*$/d" |
(
    read i
    echo $i
    i=$(( i + 1 ))

    while [[ $n -lt $i ]]
    do
    	read line
        host=$( echo $line | awk '{ print $1 }' )

        echo $host

        # gnome-terminal -e "ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no $netid@$host killall -u $netid" &
        # sleep 1
        osascript -e 'tell app "Terminal"
        do script "ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no '$netid@$host.utdallas.edu' killall -u '$netid'"
        end tell'


        n=$(( n + 1 ))
    done

)
# killall Terminal

echo "Cleanup complete"
