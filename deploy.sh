#!/bin/bash
cd msi.gama.lang.gaml.web.build && 
mvn clean install && 
cd GamaWeb && 
mvn deploy -P p2Repo --settings ../../settings.xml -Dmaven.test.skip=true && 
cd -


