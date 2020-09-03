#!/bin/bash
cd msi.gama.lang.gaml.web.build && 
cd GamaWeb && 
mvn deploy -P p2Repo --settings ../../settings.xml -Dmaven.test.skip=true && 
cd ../.. &&
cd cict.gama.jetty && 
mvn clean install && 
cd target  && 
cp gamaweb.jar GamaWeb  && 
cd -


