b(){
cd msi.gama.lang.gaml.web.build/GamaWeb 
mvn clean install 
cd ../../cict.gama.tomcat
mvn clean install
cd target
cp tomcat_launcher.jar GamaWeb
cd ../..
}
a(){
rm -rf ummisco.gama.opengl.web/gaml
rm -rf ummisco.gama.java2d.web/gaml
cd msi.gama.lang.gaml.web.target
mvn clean install
cd ../msi.gama.lang.gaml.web.build
mvn clean install
res=$?
if [[ $res -ne 0 ]]; then
	exit $res
fi
cd GamaWeb 
mvn clean install 
cd ../../cict.gama.tomcat
mvn clean install
cd target
cp tomcat_launcher.jar GamaWeb
cd ../..
}

o(){
rm -rf ummisco.gama.opengl.web/gaml
rm -rf ummisco.gama.java2d.web/gaml
cd msi.gama.lang.gaml.web.target
mvn clean install -o
cd ../msi.gama.lang.gaml.web.build
mvn clean install -o
res=$?
if [[ $res -ne 0 ]]; then
	exit $res
fi
cd GamaWeb 
mvn clean install -o
cd ../../cict.gama.tomcat
mvn clean install -o
cd target
cp tomcat_launcher.jar GamaWeb
cd ../..
}
o