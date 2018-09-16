#!/bin/bash
set -e
COMMIT=$@

REPO="gama-platform/gama.cloud"
RELEASE="latest"
thePATH="/home/travis/build/gama-platform/gama.cloud/cict.gama.tomcat/target/GamaWeb.zip"









COMMIT="${COMMIT:0:7}"

timestamp=$(date '+_%D')
SUFFIX=$timestamp'_'$COMMIT'.war'
echo $SUFFIX





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

check=${#RESULT}

if [ $check -ge 0 ]; then
	json=$RESULT
	prop='id' 
	echo $json
	echo $prop
    temp=`echo $json | sed 's/\\\\\//\//g' | sed 's/[{}]//g' | awk -v k="text" '{n=split($0,a,","); for (i=1; i<=n; i++) print a[i]}' | sed 's/\"\:\"/\|/g' | sed 's/[\,]/ /g' | sed 's/\"//g' | grep -w $prop`
    
	echo $temp
	assets=`echo ${temp##*|}` 
	num=`echo $assets| grep -o : | wc -l`
	echo 
	echo "Remove old files..."
	echo
	if [ $num -gt 2 ]; then
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
fi


echo 
echo "Upload new files..."
echo

	FILE="${thePATH}"
	NFILE="GamaWeb.zip"

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
