#!/bin/bash

# Change this to your netid
netid=yxk171930

# Root directory of your project
PROJDIR=/home/012/y/yx/yxk171930/AOS2

# Directory where the config file is located on your local system
CONFIGLOCAL=$HOME/SFTP/AOS2/config.txt

CONFIGSERVER=$PROJDIR/config.txt
# Directory your java classes are in
BINDIR=$PROJDIR

# Your main project class
PROG=Server

n=0

cat $CONFIGLOCAL | sed -e "s/#.*//" | sed -e "/^\s*$/d" |
(
    read i
    echo $i
    i=$(( i + 1 ))

    while [[ $n -lt $i ]]
    do
        read line
        # p=$( echo $line | awk '{ print $1 }' )
        host=$( echo $line | awk '{ print $1 }' )
        port=$( echo $line | awk '{ print $2 }' ) # port
        
        osascript -e 'tell app "Terminal"
        do script "ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no '$netid@$host.utdallas.edu' java -cp '$BINDIR' '$PROG' '$port' '$n' '$CONFIGSERVER'"
        end tell' 
        n=$(( n + 1 ))
    done
)

