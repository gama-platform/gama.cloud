#!/bin/bash

install (){
	echo "install GAMA cloud project"			
	
	cd org.dslforge.runtime.releng
	mvn clean install
	cd ..
	cd msi.gama.lang.gaml.web.build 
	
	if  [[ $MSG == *"ci debug"* ]]; then		
		mvn -X clean install > output.txt
		
		git config --global user.email "travis@travis-ci.org"
		git config --global user.name "Travis CI"
		git config --global push.default simple	
		git remote rm origin
		git remote add origin https://hqnghi88:$HQN_TOKEN@github.com/gama-platform/gama.cloud
		git status
		git add -A		
		git commit -m "debug output ci skip"
		git push origin HEAD:master
	else
		mvn clean install
	fi
	
	cd GamaWeb && 
	mvn clean install
		 	
	cd -
}

clean(){
	echo "Clean p2 update site"		
	sshpass -e ssh gamaws@51.255.46.42 /var/www/gama_cloud/clean.sh
}

deploy(){	
	echo "Deploy to p2 update site"	
	
	
	cd msi.gama.lang.gaml.web.build 
	cd GamaWeb 
	mvn deploy -P p2Repo --settings ../../settings.xml -Dmaven.test.skip=true 
	cd ../..
	cd cict.gama.tomcat 
	mvn clean install 
	cd target
	sudo cp tomcat_launcher.jar GamaWeb
	
	sudo zip -qr "GamaWeb.zip" GamaWeb && echo "compressed GamaWeb.zip" || echo "compress fail GamaWeb.zip"

	cd -
	
}


MESSAGE=$(git log -1 HEAD --pretty=format:%s)
echo $MESSAGE
echo $MSG
if [[ "$TRAVIS_EVENT_TYPE" == "cron" ]]; then 	
	deploy  
else
	if  [[ ${MESSAGE} == *"ci deploy"* ]] || [[ $MSG == *"ci deploy"* ]]; then		
		if  [[ ${MESSAGE} == *"ci clean"* ]] || [[ $MSG == *"ci clean"* ]]; then
			clean
		fi 
		deploy 
	else			
		install 
	fi
fi
