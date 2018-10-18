For the first time or every time you change shell files you need to type:
chmod +x compile.sh launcher.sh cleanup.sh

if you want to open indivisual terminal for each machine:
	1. log into "engnx.utdallas.edu" using NoMachine with SSH connection type
	2. open a konsole (terminal does not work ) and the machine's name in the command line should be {engnx12:~}
	3. go to your current directory

How to compile:
	1. type ./compile.sh
	2. if you encounter following error:
           -bash: ./compile.sh: /bin/sh^M: bad interpreter: No such file or directory
	   do:
	     a. vim compile.sh
	     b. type "shift + :"
	     c. type "set ff=unix" and press enter
	     d. shift+z+z to close

How to distribute programs to each machine:
	1. type ./launcher (change the content to fix your case)
	2. multiple terminals should pop out
	3. type ./cleanup.sh (shuld be done before you run launcher.sh for next time)

	
if there is an error message ")syntax error: invalid arithmetic operator (error token is " when running launcher.sh
	edit a new config.txt
	
if there is an error when running compile.sh
    edit a new compile.sh



	
   
