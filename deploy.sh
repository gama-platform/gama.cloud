#!/bin/bash
cd msi.gama.lang.gaml.web.build && 
cd GamaWeb && 
mvn deploy -P p2Repo --settings ../../settings.xml -Dmaven.test.skip=true && 
cd ../.. &&
cd cict.gama.tomcat && 
mvn clean install && 
cd -


