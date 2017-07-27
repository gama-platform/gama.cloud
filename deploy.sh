#!/bin/bash
cd msi.gama.lang.gaml.web.build && 
mvn clean deploy:deploy-file -Dfile=/home/travis/build/gama-platform/gama.cloud/msi.gama.lang.gaml.web.build/GamaWeb/target/GamaWeb.war -P p2Repo --settings ../settings.xml -Dmaven.test.skip=true && 
cd -


