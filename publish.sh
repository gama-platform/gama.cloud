#!/bin/bash

release(){
	echo "Upload continuous release to github"		
	bash ./github-release.sh "$TRAVIS_COMMIT" 
}

MESSAGE=$(git log -1 HEAD --pretty=format:%s)
echo $MESSAGE
echo $MSG
if [[ "$TRAVIS_EVENT_TYPE" == "cron" ]]; then 	
	release  
else
	if  [[ ${MESSAGE} == *"ci release"* ]]; then	
		release 
	fi	
		
		release 
fi
