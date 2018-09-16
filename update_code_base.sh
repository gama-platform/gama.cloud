#!/bin/bash
cd ummisco.gama.ui.experiment.web 
ls
rm -rf src
cp -r ../../gama/ummisco.gama.ui.experiment/src src 
cp -r internal_src/* src 
cd .. 


cd ummisco.gama.ui.modeling.web 
ls 
rm -rf src 
cp -r ../../gama/ummisco.gama.ui.modeling/src src 
cp -r internal_src/* src 
cd .. 

cd ummisco.gama.ui.navigator.web 
ls 
rm -rf src 
cp -r ../../gama/ummisco.gama.ui.navigator/src src 
cp -r internal_src/* src 
cd .. 

cd ummisco.gama.ui.shared.web 
ls 
rm -rf src 
cp -r ../../gama/ummisco.gama.ui.shared/src src 
cp -r internal_src/* src 
cd .. 

cd ummisco.gama.java2d.web 
ls 
rm -rf src 
cp -r ../../gama/ummisco.gama.java2d/src src 
cp -r internal_src/* src 
cd .. 

cd ummisco.gama.opengl.web 
ls 
rm -rf src 
cp -r ../../gama/ummisco.gama.opengl/src src
cp -r internal_src/* src 
cp -r modern_src/* src 
