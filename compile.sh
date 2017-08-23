#!/bin/bash

compile (){
	echo "Compile GAMA cloud project"			
	
	
	cd msi.gama.lang.gaml.web.build 
	
	if  [[ $MSG == *"ci debug"* ]]; then		
		mvn -X clean compile 
	else
		mvn dependency:resolve -U -e -X clean compile
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
