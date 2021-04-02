#!/bin/bash
b(){
#cd msi.gama.lang.gaml.web.build/GamaWeb 
#mvn clean install -o
#cd ../../cict.gama.jetty
cd cict.gama.jetty
mvn clean install -o
cd target
cp gamaweb.jar GamaWeb
cd ../..
if  [ $1 = "B" ] || [ $1 = "b" ]
	then
		b 
	else
		if  [ $1 = "O" ] || [ $1 = "o" ]
		then
			o 
		else
			a
		fi
	fi
}
a(){
rm -rf ummisco.gama.opengl_web/gaml/*
rm -rf ummisco.gama.java2d_web/gaml/*
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
cd ../../cict.gama.jetty
mvn clean install
cd target
cp gamaweb.jar GamaWeb
cd ../..
}

o(){
rm -rf ummisco.gama.opengl_web/gaml/*
rm -rf ummisco.gama.java2d_web/gaml/*
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
cd ../../cict.gama.jetty
mvn clean install -o
cd target
cp gamaweb.jar GamaWeb
cd ../..
}


if  [ "$1" = "A" ] || [ "$1" = "a" ]
then
    a 
	start java -jar cict.gama.jetty/target/GamaWeb/gamaweb.jar GamaWeb 8080 127.0.0.1:8080
elif [ "$1" = "B" ] || [ "$1" = "b" ]
then
    b 
elif [ "$1" = "O" ] || [ "$1" = "o" ]
then
    o 
else 
	a
fi