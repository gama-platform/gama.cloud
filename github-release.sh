#!/bin/bash


function update_tag() {
	echo "update tag " $1 
	git config --global user.email "hqnghi88@gmail.com"
	git config --global user.name "HUYNH Quang Nghi"
	git remote rm origin
	git remote add origin https://hqnghi88:$HQN_TOKEN@github.com/gama-platform/gama.git
	git config remote.origin.fetch "+refs/heads/*:refs/remotes/origin/*"
	git fetch
	git checkout master
	git pull origin master
	git push origin :refs/tags/$1
	git tag -d $1
	git tag -fa $1 -m "$1"
	git push --tags -f
	git ls-remote --tags origin
	git show-ref --tags
}



set -e
COMMIT=$@

REPO="gama-platform/gama.cloud"
RELEASE="latest"
thePATH="/home/travis/build/gama-platform/gama.cloud/cict.gama.tomcat/target/GamaWeb.zip"









COMMIT="${COMMIT:0:7}"

timestamp=$(date '+_%D')
SUFFIX=$timestamp'_'$COMMIT'.war'
echo $SUFFIX




LK1="https://api.github.com/repos/gama-platform/gama.cloud/releases/tags/$RELEASE"

echo   "Getting info of release ...  "
RESULT1=`curl  -s -X GET \
-H "Authorization: token $HQN_TOKEN"   \
"$LK1"`	
echo $RESULT1

	json=$RESULT1
	prop='id'
	
    temp=`echo $json | sed 's/\\\\\//\//g' | sed 's/[{}]//g' | awk -v k="text" '{n=split($0,a,","); for (i=1; i<=n; i++) print a[i]}' | sed 's/\"\:\"/\|/g' | sed 's/[\,]/ /g' | sed 's/\"//g' | grep -w $prop`
    
	assets=`echo ${temp##*|}`

	for theid in $assets; do
		if [ "$theid" != "id:" ]; then
	LK1="https://api.github.com/repos/gama-platform/gama.cloud/releases/$theid"

	echo   "Deleting release ...  "
	RESULT1=`curl  -s -X DELETE \
	-H "Authorization: token $HQN_TOKEN"   \
	"$LK1"`	
	echo $RESULT1
	break
		fi
	done 


	#update_tag $RELEASE 

	echo   "Creating release ...  "
LK="https://api.github.com/repos/gama-platform/gama.cloud/releases"

  RESULT=` curl -s -X POST \
  -H "X-Parse-Application-Id: sensitive" \
  -H "X-Parse-REST-API-Key: sensitive" \
  -H "Authorization: token $HQN_TOKEN"   \
  -H "Content-Type: application/json" \
  -d '{"tag_name": "'$RELEASE'", "name":"GAMA 1.8","body":"to be official Released on 17 May","draft": false,"prerelease": true}' \
    "$LK"`
echo $RESULT	

















echo
echo "Getting info of $RELEASE tag..."
echo 
LK="https://api.github.com/repos/gama-platform/gama.cloud/releases/tags/$RELEASE"

  RESULT=` curl -s -X GET \
  -H "X-Parse-Application-Id: sensitive" \
  -H "X-Parse-REST-API-Key: sensitive" \
  -H "Authorization: token $HQN_TOKEN"   \
  -H "Content-Type: application/json" \
  -d '{"name":"value"}' \
    "$LK"`
echo $RESULT	
RELEASEID=`echo "$RESULT" | sed -ne 's/^  "id": \(.*\),$/\1/p'`
echo $RELEASEID


  LK="https://api.github.com/repos/gama-platform/gama.cloud/releases/$RELEASEID/assets"
  
  RESULT=` curl -s -X GET \
  -H "X-Parse-Application-Id: sensitive" \
  -H "X-Parse-REST-API-Key: sensitive" \
  -H "Authorization: token $HQN_TOKEN"   \
  -H "Content-Type: application/json" \
  -d '{"name":"value"}' \
    "$LK"`

json=$RESULT
check=${#json}
echo $check
if [ $check -gt 4 ]; then
	echo 
	echo "Remove old files..."
	echo
	prop='id'
	
    temp=`echo $json | sed 's/\\\\\//\//g' | sed 's/[{}]//g' | awk -v k="text" '{n=split($0,a,","); for (i=1; i<=n; i++) print a[i]}' | sed 's/\"\:\"/\|/g' | sed 's/[\,]/ /g' | sed 's/\"//g' | grep -w $prop`
    
	assets=`echo ${temp##*|}`
	echo $assets
	for theid in $assets; do
		if [ "$theid" != "id:" ]; then
		  LK1="https://api.github.com/repos/gama-platform/gama.cloud/releases/assets/$theid"
		  
			echo   "Deleting $LK1...  "
		  RESULT1=`curl  -s -X  "DELETE"                \
			-H "Authorization: token $HQN_TOKEN"   \
			"$LK1"`	
			echo $RESULT1
		fi
	done 
fi


echo 
echo "Upload new files..."
echo

	FILE="${thePATH}"
	NFILE='GamaWeb.zip'

  FILENAME=`basename $FILE`
  echo   "Uploading $NFILE...  "
  LK="https://uploads.github.com/repos/gama-platform/gama.cloud/releases/$RELEASEID/assets?name=$NFILE"
  
  RESULT=`curl -s -w  "\n%{http_code}\n"                   \
    -H "Authorization: token $HQN_TOKEN"                \
    -H "Accept: application/vnd.github.manifold-preview"  \
    -H "Content-Type: application/zip"                    \
    --data-binary "@$FILE"                                \
    "$LK"`
	echo $RESULT

echo DONE
