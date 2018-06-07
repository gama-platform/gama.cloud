#!/bin/bash
cd org.dslforge.runtime.releng &&
mvn clean install && 
cd .. &&
cd msi.gama.lang.gaml.web.build && 
mvn clean install && 
cd GamaWeb && 
mvn deploy -P p2Repo --settings ../../settings.xml -Dmaven.test.skip=true && 
cd -


