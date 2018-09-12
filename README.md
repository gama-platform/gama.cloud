

# gama.cloud
# # Use as standalone mode
Gama can run on local machine with an embedded tomcat server 8.0.48.

Step 1: Download the zip file released at https://github.com/gama-platform/gama.cloud/releases

Step 2: Unzip it to have 4 files
gama.bat
gama.sh
GamaWeb.war
tomcat_launcher.jar

Step 3: Windows users double click on gama.bat, otherwise, use gama.sh script

Step 4: After launched, go to browser, access to http://localhost:8080/GamaWeb/texteditor to start using GAMA (note that on win, it will automatically open that link in default browser)


# # Install on own tomcat server

<p align="center">
  <img src="https://github.com/gama-platform/gama.cloud/blob/master/gamaweb.png?raw=true" width="860"/>
</p>

The current Web IDE of GAMA work with Apache Tomcat 8 server and jdk 8

Step 0: install and configure the Apache Tomcat server

Step 1: Download the zip file released at https://github.com/gama-platform/gama.cloud/releases

Step 2: Extract the zip to get the war file GamaWeb.war

Step 3: Copy the GamaWeb.war to "webapps" folder of Tomcat server (the root folder that contains web apps). 

Step 4: Restart/start tomcat to deploy the war file as folder . It should have GamaWeb folder in webapps folder 

Step 5: access the online IDE with the configured address of tomcat, i.e. localhost:8080 with the webapp name GamaWeb/texteditor :

localhost:8080/GamaWeb/texteditor

Please post any issue at this link https://github.com/gama-platform/gama.cloud/issues
