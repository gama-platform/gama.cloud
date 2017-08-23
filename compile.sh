#!/bin/bash

compile (){
	echo "Compile GAMA cloud project"			
	
	cd msi.gama.lang.gaml.web 
	
		mvn clean compile
	cd -
	cd msi.gama.lang.gaml.web.build 
	
	if  [[ $MSG == *"ci debug"* ]]; then		
		mvn -X clean compile > output.txt
		
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
		mvn clean compile
	fi
		
	cd -
}

clean(){
	echo "Clean p2 update site"		
	sshpass -e ssh gamaws@51.255.46.42 /var/www/gama_cloud/clean.sh
}

deploy(){	
	echo "Deploy to p2 update site"	
	sh ./deploy.sh
	
	echo "Deploy tomcat website"		
	sshpass -e ssh gamaws@51.255.46.42 /var/www/gama_cloud/deployweb.sh
}


MESSAGE=$(git log -1 HEAD --pretty=format:%s)
echo $MESSAGE
echo $MSG
if [[ "$TRAVIS_EVENT_TYPE" == "cron" ]]; then 	
	deploy  
else
	if  [[ ${MESSAGE} == *"ci deploy"* ]]; then		
		if  [[ ${MESSAGE} == *"ci clean"* ]] || [[ $MSG == *"ci clean"* ]]; then
			clean
		fi 
		deploy 
	else	
		
		compile 
	fi
fi
