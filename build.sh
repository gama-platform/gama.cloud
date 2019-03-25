#!/bin/bash
cd msi.gama.lang.gaml.web.build -X &&
mvn clean install &&
cd GamaWeb && 
mvn clean install && 
cd ../.. &&
cd cict.gama.tomcat && 
mvn clean install && 
cd - &&
cp cict.gama.tomcat/target/tomcat_launcher.jar cict.gama.tomcat/target/GamaWeb/tomcat_launcher.jar

