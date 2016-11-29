## Installation


###On Osprey:
Usage of this software requires that you initially run a makefile, followed by a command to execute the software — afterwards 
you can follow the command-line menu interface.


The makefile will be provided in the root directory of the software folder. In order to run the makefile, navigate to the root 
directory of the software folder and execute the command “make”. Running this command will ready the software to be executed and 
should only be required for the initial setup.


Once “make” has been run the software can be executed with the command “./SQLtoXMLConverter”. SQLtoXMLConverter is a bash script 
that runs the java program while liking the ojdbc6.jar driver.


Note: In order to change the type of database connection an appropriate driver would need to be acquired, and the Database.java
source file would need to be recompiled with a link to the new driver. Then to run the software with the new driver the 
SQLtoXMLConverter script would need to be edited to point to the new driver. 


### In An IDE:
First, add the .jar database driver to your project’s library. In Eclipse this can be done by right clicking on the root folder, 
clicking “properties”, selecting “Java Build Path”, and then clicking “Add External JARS…” Once this is done the software can be 
run the same way as any Eclipse project.  


## Changing the Database Link


Utilizing the provided sample software, navigate to the software folder and find the “SQLtoXMLConverter.java” file. Open the file 
and find the line that reads the following:


	`Database db = new Database("jdbc:oracle:thin:@olympia.unfcsd.unf.edu:1521:dworcl", "ozzieosprey", "mytastytacos");`


After locating this line, you may change the portion of the string that reads “olympia.unfcsd.unf.edu:1521:dworcl” and edit with 
the necessary host string to which you wish to connect. The preceding portion of the host string (“jdbc:oracle:thin”) is strictly to 
inform the software which driver you will be utilizing to connect to the database, in this case we used the Oracle driver. Furthermore, 
the “ozzieosprey” portion of this line is the username required to connect to the database while the “mytastytacos” portion is the 
password. Another option is to leave the strings in this line empty (“”). In doing this the user will be prompted for the information 
that is left blank in the following order: hostname, username, and password. Any of these strings can be left blank independently so 
users can be prompted just for username and password, etc.


Keep in mind any time you edit the source files, including editing the database connection information, you will need to recompile 
the software. This can be done by utilizing the “make” command and then usage of the software may continue as desired. If you change 
the type of driver then the “makefile” and “SQLtoXMLConverter” script would need to be edited to point to the new driver, then the 
source would need to be recompiled.
