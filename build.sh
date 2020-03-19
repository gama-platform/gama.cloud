cd msi.gama.lang.gaml.web.target
mvn clean install
cd ../msi.gama.lang.gaml.web.build
mvn clean install
cd GamaWeb 
mvn clean install 
cd ../../cict.gama.tomcat
mvn clean install
cd target
cp tomcat_launcher.jar GamaWeb
cd ../..