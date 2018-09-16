#!/bin/bash
RESULT='{ "url": "https://api.github.com/repos/gama-platform/gama.cloud/releases/6299897", "assets_url": "https://api.github.com/repos/gama-platform/gama.cloud/releases/6299897/assets", "upload_url": "https://uploads.github.com/repos/gama-platform/gama.cloud/releases/6299897/assets{?name,label}", "html_url": "https://github.com/gama-platform/gama.cloud/releases/tag/latest", "id": 6299897, "node_id": "MDc6UmVsZWFzZTYyOTk4OTc=", "tag_name": "latest", "target_commitish": "master", "name": "", "draft": false, "author": { "login": "hqnghi88", "id": 6105384, "node_id": "MDQ6VXNlcjYxMDUzODQ=", "avatar_url": "https://avatars2.githubusercontent.com/u/6105384?v=4", "gravatar_id": "", "url": "https://api.github.com/users/hqnghi88", "html_url": "https://github.com/hqnghi88", "followers_url": "https://api.github.com/users/hqnghi88/followers", "following_url": "https://api.github.com/users/hqnghi88/following{/other_user}", "gists_url": "https://api.github.com/users/hqnghi88/gists{/gist_id}", "starred_url": "https://api.github.com/users/hqnghi88/starred{/owner}{/repo}", "subscriptions_url": "https://api.github.com/users/hqnghi88/subscriptions", "organizations_url": "https://api.github.com/users/hqnghi88/orgs", "repos_url": "https://api.github.com/users/hqnghi88/repos", "events_url": "https://api.github.com/users/hqnghi88/events{/privacy}", "received_events_url": "https://api.github.com/users/hqnghi88/received_events", "type": "User", "site_admin": false }, "prerelease": false, "created_at": "2018-07-03T01:04:32Z", "published_at": "2017-05-06T10:07:25Z", "assets": [ ], "tarball_url": "https://api.github.com/repos/gama-platform/gama.cloud/tarball/latest", "zipball_url": "https://api.github.com/repos/gama-platform/gama.cloud/zipball/latest", "body": "" }' 


	json=$RESULT
	prop='id'
	
    temp=`echo $json | sed 's/\\\\\//\//g' | sed 's/[{}]//g' | awk -v k="text" '{n=split($0,a,","); for (i=1; i<=n; i++) print a[i]}' | sed 's/\"\:\"/\|/g' | sed 's/[\,]/ /g' | sed 's/\"//g' | grep -w $prop`
    
	assets=`echo ${temp##*|}`  
	num=`echo $assets| grep -o : | wc -l`
	if [ $num -gt 2 ]; then
		for theid in $assets; do
			if [ "$theid" != "id:" ]; then 
				echo "$theid"
			fi
		done 
	fi

RESULT='{ "url": "https://api.github.com/repos/gama-platform/gama.cloud/releases/6299897", "assets_url": "https://api.github.com/repos/gama-platform/gama.cloud/releases/6299897/assets", "upload_url": "https://uploads.github.com/repos/gama-platform/gama.cloud/releases/6299897/assets{?name,label}", "html_url": "https://github.com/gama-platform/gama.cloud/releases/tag/latest", "id": 6299897, "node_id": "MDc6UmVsZWFzZTYyOTk4OTc=", "tag_name": "latest", "target_commitish": "master", "name": "", "draft": false, "author": { "login": "hqnghi88", "id": 6105384, "node_id": "MDQ6VXNlcjYxMDUzODQ=", "avatar_url": "https://avatars2.githubusercontent.com/u/6105384?v=4", "gravatar_id": "", "url": "https://api.github.com/users/hqnghi88", "html_url": "https://github.com/hqnghi88", "followers_url": "https://api.github.com/users/hqnghi88/followers", "following_url": "https://api.github.com/users/hqnghi88/following{/other_user}", "gists_url": "https://api.github.com/users/hqnghi88/gists{/gist_id}", "starred_url": "https://api.github.com/users/hqnghi88/starred{/owner}{/repo}", "subscriptions_url": "https://api.github.com/users/hqnghi88/subscriptions", "organizations_url": "https://api.github.com/users/hqnghi88/orgs", "repos_url": "https://api.github.com/users/hqnghi88/repos", "events_url": "https://api.github.com/users/hqnghi88/events{/privacy}", "received_events_url": "https://api.github.com/users/hqnghi88/received_events", "type": "User", "site_admin": false }, "prerelease": false, "created_at": "2018-07-03T01:04:32Z", "published_at": "2017-05-06T10:07:25Z", "assets": [ { "url": "https://api.github.com/repos/gama-platform/gama.cloud/releases/assets/8645065", "id": 8645065, "node_id": "MDEyOlJlbGVhc2VBc3NldDg2NDUwNjU=", "name": "GamaWeb.zip", "label": "", "uploader": { "login": "hqnghi88", "id": 6105384, "node_id": "MDQ6VXNlcjYxMDUzODQ=", "avatar_url": "https://avatars2.githubusercontent.com/u/6105384?v=4", "gravatar_id": "", "url": "https://api.github.com/users/hqnghi88", "html_url": "https://github.com/hqnghi88", "followers_url": "https://api.github.com/users/hqnghi88/followers", "following_url": "https://api.github.com/users/hqnghi88/following{/other_user}", "gists_url": "https://api.github.com/users/hqnghi88/gists{/gist_id}", "starred_url": "https://api.github.com/users/hqnghi88/starred{/owner}{/repo}", "subscriptions_url": "https://api.github.com/users/hqnghi88/subscriptions", "organizations_url": "https://api.github.com/users/hqnghi88/orgs", "repos_url": "https://api.github.com/users/hqnghi88/repos", "events_url": "https://api.github.com/users/hqnghi88/events{/privacy}", "received_events_url": "https://api.github.com/users/hqnghi88/received_events", "type": "User", "site_admin": false }, "content_type": "application/zip", "state": "uploaded", "size": 140934780, "download_count": 2, "created_at": "2018-09-12T15:59:46Z", "updated_at": "2018-09-12T15:59:50Z", "browser_download_url": "https://github.com/gama-platform/gama.cloud/releases/download/latest/GamaWeb.zip" } ], "tarball_url": "https://api.github.com/repos/gama-platform/gama.cloud/tarball/latest", "zipball_url": "https://api.github.com/repos/gama-platform/gama.cloud/zipball/latest", "body": "" }'
	json=$RESULT
	prop='id'
	
    temp=`echo $json | sed 's/\\\\\//\//g' | sed 's/[{}]//g' | awk -v k="text" '{n=split($0,a,","); for (i=1; i<=n; i++) print a[i]}' | sed 's/\"\:\"/\|/g' | sed 's/[\,]/ /g' | sed 's/\"//g' | grep -w $prop`
    
	assets=`echo ${temp##*|}` 
	
	num=`echo $assets| grep -o : | wc -l`
	if [ $num -gt 2 ]; then
		for theid in $assets; do
			if [ "$theid" != "id:" ]; then 
				echo "$theid"
			fi
		done 
	fi
	