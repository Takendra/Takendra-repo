#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
echo "DIR"$DIR
cd $DIR

POST_URL="${18}/${16}/android/appcms/build/status"
UPLOAD_URL="${18}/${16}/appcms/android/build/apk/link"

echo "111111111"
echo ${19}
echo "***********"
echo $19
echo "***********"
echo "111111111"



echo 'piyush ****'
echo $POST_URL
echo ${16}
echo ${18}
echo 'piyush *****'
echo ${21}
echo 'piyush *****'
echo ${22}


postBuildStatus(){ #buildid, posturl, status, errormsg
BODY_DATA="{\"buildId\":$1,\"status\":\"$3\",\"errorMessage\":\"$4\"}"
BODY_DATA="{\"buildId\":$1,\"status\":\"$3\",\"errorMessage\":\"$4\",\"message\":\"$5\",\"percentComplete\":$6,\"isAppOnStore\":\"$7\",\"buildVersion\":$8}"
echo "\n**********BUILD_STATUS_UPDATE**********\nPOST_URL=$2\nBODY_DATA=["$BODY_DATA"]\n---------------------------------------"
curl -H 'Content-Type: application/json' -X POST -d "$BODY_DATA" $2
}

postUpdateLink(){ #buildid, posturl, status, errormsg
BODY_DATA="{\"buildId\":$1,\"apkLink\":\"$3\",\"platform\":\"android\"}"
echo "\n**********BUILD_STATUS_UPDATE**********\UPLOAD_URL=$2\nBODY_DATA=["$BODY_DATA"]\n---------------------------------------"
curl -H 'Content-Type: application/json' -X PUT -d "$BODY_DATA" $2
}

downloadFile(){ #url, output, buildid, posturl
status=$(curl -s -w %{http_code} $1 -o $2)
if [ "$status" -eq 200 ]
then
echo "File downloaded:[$1]"
else
echo "Missing required file:[$1]"
postBuildStatus $3 $4 'FAILED' "Missing required file:$1"
trap "echo exitting because my child killed me due to asset file not found" 0
exit 1
fi
}


base64 --decode ./AppCMS/crfile.txt > ./AppCMS/google-services.json
base64 --decode ./credentialfile.txt > ./googleplay_android.json

# if [ "${16}" -eq "0" ]
# then
# else
# echo "App is not Present on Play Store"
# fi

postBuildStatus ${17} $POST_URL "STARTED" "No ERROR" "Build Successfully Started" 5 false 0
#1: Slack First Message. Build Success Full Started

fastlane android slackSendMessage my_slack_msg:"${4} -> ANDROID BUILD SUCCESSFULLY STARTED. BUILD-ID -> ${17}. VERSION-NUMBER -> ${3}. Build Triggered By --> ${23}" my_user_name:"Viewlift fastlane" mySlackUrl:"{24}"


rm -rf ./AppCMS/src/main/res/drawable/logo.jpg
rm -rf ./AppCMS/src/main/res/drawable/logo.png

rm -rf ./AppCMS/src/main/res/drawable-xhdpi/logo.jpg
rm -rf ./AppCMS/src/main/res/drawable-xhdpi/logo.png

rm -rf ./AppCMS/src/main/res/drawable-xxxhdpi/logo.jpg
rm -rf ./AppCMS/src/main/res/drawable-xxxhdpi/logo.png

rm -rf ./AppCMS/src/main/res/drawable-xxhdpi/logo.jpg
rm -rf ./AppCMS/src/main/res/drawable-xxhdpi/logo.png


rm -rf ./AppCMS/src/main/res/drawable-mdpi/logo.jpg
rm -rf ./AppCMS/src/main/res/drawable-mdpi/logo.png

rm -rf ./AppCMS/src/main/res/drawable-hdpi/logo.jpg
rm -rf ./AppCMS/src/main/res/drawable-hdpi/logo.png



rm -rf ./AppCMS/src/main/res/drawable-mdpi/logo_icon.jpg
rm -rf ./AppCMS/src/main/res/drawable-mdpi/logo_icon.png

rm -rf ./AppCMS/src/main/res/drawable-hdpi/logo_icon.jpg
rm -rf ./AppCMS/src/main/res/drawable-hdpi/logo_icon.png

rm -rf ./AppCMS/src/main/res/drawable/logo_icon.jpg
rm -rf ./AppCMS/src/main/res/drawable/logo_icon.png

rm -rf ./AppCMS/src/main/res/drawable-xhdpi/logo_icon.jpg
rm -rf ./AppCMS/src/main/res/drawable-xhdpi/logo_icon.png

rm -rf ./AppCMS/src/main/res/drawable-xxxhdpi/logo_icon.jpg
rm -rf ./AppCMS/src/main/res/drawable-xxxhdpi/logo_icon.png

rm -rf ./AppCMS/src/main/res/drawable-xxhdpi/logo_icon.jpg
rm -rf ./AppCMS/src/main/res/drawable-xxhdpi/logo_icon.png


rm -rf ./AppCMS/src/main/res/drawable-mdpi/logo_icon.9.jpg
rm -rf ./AppCMS/src/main/res/drawable-mdpi/logo_icon.9.png

rm -rf ./AppCMS/src/main/res/drawable-hdpi/logo_icon.9.jpg
rm -rf ./AppCMS/src/main/res/drawable-hdpi/logo_icon.9.png

rm -rf ./AppCMS/src/main/res/drawable/logo_icon.9.jpg
rm -rf ./AppCMS/src/main/res/drawable/logo_icon.9.png

rm -rf ./AppCMS/src/main/res/drawable-xhdpi/logo_icon.9.jpg
rm -rf ./AppCMS/src/main/res/drawable-xhdpi/logo_icon.9.png

rm -rf ./AppCMS/src/main/res/drawable-xxxhdpi/logo_icon.9.jpg
rm -rf ./AppCMS/src/main/res/drawable-xxxhdpi/logo_icon.9.png

rm -rf ./AppCMS/src/main/res/drawable-xxhdpi/logo_icon.9.jpg
rm -rf ./AppCMS/src/main/res/drawable-xxhdpi/logo_icon.9.png



rm -rf ./AppCMS/src/main/res/drawable/vid_image_placeholder_16x9.jpg
rm -rf ./AppCMS/src/main/res/drawable/vid_image_placeholder_16x9.png
rm -rf ./AppCMS/src/main/res/drawable/vid_image_placeholder_16x9_season.png
rm -rf ./AppCMS/src/main/res/drawable/vid_image_placeholder_32x9.png

#rm -rf ./AppCMS/src/main/res/drawable/ic_skylight_notification_d.png
#rm -rf ./AppCMS/src/main/res/drawable-mdpi/ic_skylight_notification_d.png
#rm -rf ./AppCMS/src/main/res/drawable-xhdpi/ic_skylight_notification_d.png
#rm -rf ./AppCMS/src/main/res/drawable-xxhdpi/ic_skylight_notification_d.png
#rm -rf ./AppCMS/src/main/res/drawable-xxxhdpi/ic_skylight_notification_d.png

rm -rf ./AppCMS/src/main/res/drawable/vid_image_placeholder_land.jpg
rm -rf ./AppCMS/src/main/res/drawable/vid_image_placeholder_port.jpg
rm -rf ./AppCMS/src/main/res/drawable/vid_image_placeholder_land.png
rm -rf ./AppCMS/src/main/res/drawable/vid_image_placeholder_port.png
rm -rf ./AppCMS/src/main/res/drawable/vid_image_placeholder_square.jpg
rm -rf ./AppCMS/src/main/res/drawable/vid_image_placeholder_square.png




echo "viewlift"

echo $1
echo ${1}

echo "viewlift"


aws s3 cp s3://${19}/$1/build/android/resource/drawable ./AppCMS/src/main/res/drawable --recursive
postBuildStatus ${17} $POST_URL "DOWNLOADING_RESOURCES" "No ERROR" "Downloading the build drawable resources" 6 " " 0

fastlane android slackSendMessage my_slack_msg:"${4} -> ANDROID DOWNLOADING RESOURCES AND METADATA. BUILD-ID -> ${17}. VERSION-NUMBER -> ${3}. Build Triggered By --> ${23}" my_user_name:"Viewlift fastlane" mySlackUrl:"{24}"


aws s3 cp s3://${19}/$1/build/android/resource/mipmap-hdpi ./AppCMS/src/main/res/mipmap-hdpi/ --recursive
aws s3 cp s3://${19}/$1/build/android/resource/mipmap-hdpi ./AppCMS/src/mobile/res/mipmap-hdpi/ --recursive
postBuildStatus ${17} $POST_URL "DOWNLOADING_RESOURCES" "No ERROR" "Downloading the build mipmap resources" 10 " " 0

aws s3 cp s3://${19}/$1/build/android/resource/mipmap-mdpi ./AppCMS/src/main/res/mipmap-mdpi/ --recursive
aws s3 cp s3://${19}/$1/build/android/resource/mipmap-mdpi ./AppCMS/src/mobile/res/mipmap-mdpi/ --recursive
postBuildStatus ${17} $POST_URL "DOWNLOADING_RESOURCES" "No ERROR" "Downloading the build mipmap resources" 11 " " 0

aws s3 cp s3://${19}/$1/build/android/resource/mipmap-xhdpi ./AppCMS/src/main/res/mipmap-xhdpi/ --recursive
aws s3 cp s3://${19}/$1/build/android/resource/mipmap-xhdpi ./AppCMS/src/mobile/res/mipmap-xhdpi/ --recursive
postBuildStatus ${17} $POST_URL "DOWNLOADING_RESOURCES" "No ERROR" "Downloading the build mipmap resources" 12 " " 0

aws s3 cp s3://${19}/$1/build/android/resource/mipmap-xxhdpi ./AppCMS/src/main/res/mipmap-xxhdpi/ --recursive
aws s3 cp s3://${19}/$1/build/android/resource/mipmap-xxhdpi ./AppCMS/src/mobile/res/mipmap-xxhdpi/ --recursive
postBuildStatus ${17} $POST_URL "DOWNLOADING_RESOURCES" "No ERROR" "Downloading the build mipmap resources" 13 " " 0

aws s3 cp s3://${19}/$1/build/android/resource/mipmap-xxxhdpi ./AppCMS/src/main/res/mipmap-xxxhdpi/ --recursive
aws s3 cp s3://${19}/$1/build/android/resource/mipmap-xxxhdpi ./AppCMS/src/mobile/res/mipmap-xxxhdpi/ --recursive
postBuildStatus ${17} $POST_URL "DOWNLOADING_RESOURCES" "No ERROR" "Downloading the build mipmap resources" 14 " " 0

echo "creating placeholder for season tray 170x95 16:9 ratio "

convert ./AppCMS/src/main/res/drawable/vid_image_placeholder_16x9.png -resize 170x95 ./AppCMS/src/main/res/drawable/vid_image_placeholder_16x9_season.png
convert ./AppCMS/src/main/res/drawable/vid_image_placeholder_land.png -resize 711^x200 ./AppCMS/src/main/res/drawable/vid_image_placeholder_32x9.png

echo "creating placeholder for season tray 100x100 1:1 ratio "

convert ./AppCMS/src/main/res/drawable/vid_image_placeholder_square.png -resize 100x100! ./AppCMS/src/main/res/drawable/vid_image_placeholder_1x1.png

echo "creating placeholder for season tray 150x200 3:4 ratio "

convert ./AppCMS/src/main/res/drawable/vid_image_placeholder_port.png -resize 150x200^ ./AppCMS/src/main/res/drawable/vid_image_placeholder_3x4.png

echo "creating placeholder for 9:16 ratio with size of 270x480^ "
convert ./AppCMS/src/main/res/drawable/vid_image_placeholder_port.png -resize 270x480^ ./AppCMS/src/main/res/drawable/vid_image_placeholder_9x16.png

echo "creating circular image for app logo "

convert ./AppCMS/src/mobile/res/mipmap-hdpi/app_logo.png -background none -vignette 0x0+0+0 ./AppCMS/src/mobile/res/mipmap-hdpi/app_logo_circular.png
convert ./AppCMS/src/mobile/res/mipmap-mdpi/app_logo.png -background none -vignette 0x0+0+0 ./AppCMS/src/mobile/res/mipmap-mdpi/app_logo_circular.png
convert ./AppCMS/src/mobile/res/mipmap-xhdpi/app_logo.png -background none -vignette 0x0+0+0 ./AppCMS/src/mobile/res/mipmap-xhdpi/app_logo_circular.png
convert ./AppCMS/src/mobile/res/mipmap-xxhdpi/app_logo.png -background none -vignette 0x0+0+0 ./AppCMS/src/mobile/res/mipmap-xxhdpi/app_logo_circular.png
convert ./AppCMS/src/mobile/res/mipmap-xxxhdpi/app_logo.png -background none -vignette 0x0+0+0 ./AppCMS/src/mobile/res/mipmap-xxxhdpi/app_logo_circular.png


cp ./AppCMS/src/mobile/res/mipmap-mdpi/ic_skylight_notification.png "./AppCMS/src/mobile/res/drawable-mdpi/ic_skylight_notification_d.png"
cp ./AppCMS/src/mobile/res/mipmap-xhdpi/ic_skylight_notification.png "./AppCMS/src/mobile/res/drawable-xhdpi/ic_skylight_notification_d.png"
cp ./AppCMS/src/mobile/res/mipmap-xxhdpi/ic_skylight_notification.png "./AppCMS/src/mobile/res/drawable-xxhdpi/ic_skylight_notification_d.png"
cp ./AppCMS/src/mobile/res/mipmap-xxxhdpi/ic_skylight_notification.png "./AppCMS/src/mobile/res/drawable-xxxhdpi/ic_skylight_notification_d.png"

rm -rf ./fastlane/metadata

rm -rf ./AppCMS/build/outputs/apk/mobileNonflavour/debug/AppCMS-mobile-debug.apk
rm -rf ./AppCMS/build/outputs/apk/androidTest/mobile/debug/AppCMS-mobile-debug-androidTest.apk
rm -rf ./AppCMS/build/outputs/apk/mobile/release/AppCMS-mobile-release-unsigned.apk
rm -rf ./AppCMS/build/outputs/apk/nonflavour/release/AppCMS-mobile-release-unsigned.apk
rm -rf ./AppCMS/build/outputs/apk/nonflavour/release/AppCMS-mobile-release.apk
rm -rf ./AppCMS/build/outputs/apk/mobile/release/AppCMS-mobile-release.apk

rm -rf ./AppCMS/build/outputs/apk/mobileNonflavour/debug/AppCMS-mobile-kiswe-debug.apk
rm -rf ./AppCMS/build/outputs/apk/androidTest/mobile/debug/AppCMS-mobile-kiswe-debug-androidTest.apk
rm -rf ./AppCMS/build/outputs/apk/mobile/release/AppCMS-mobile-kiswe-release-unsigned.apk
rm -rf ./AppCMS/build/outputs/apk/mobile/release/AppCMS-mobile-kiswe-release.apk

fastlane supply init --package_name $6 --json_key ./googleplay_android.json


IS_APP_ONPLAYSTORE="1"

echo "$IS_APP_ONPLAYSTORE"


if [ "$IS_APP_ONPLAYSTORE" -eq "0" ]
then
echo "App is Present on Play Store"

postBuildStatus ${17} $POST_URL "DOWNLOADING_RESOURCES" "No ERROR" "Downloading FeatureGraphic" 15 "true" -1
# downloadFile ${12} ./fastlane/metadata/android/en-US/images/featureGraphic.png ${17} $POST_URL
# postBuildStatus ${17} $POST_URL "DOWNLOADING_RESOURCES" "No ERROR" "Downloading PromoGraphic" 16 "true" -1
# downloadFile ${13} ./fastlane/metadata/android/en-US/images/promoGraphic.png ${17} $POST_URL
# postBuildStatus ${17} $POST_URL "DOWNLOADING_RESOURCES" "No ERROR" "Downloading the TV Banner" 17 "true" -1
# downloadFile ${14} ./fastlane/metadata/android/en-US/images/tvBanner.png ${17} $POST_URL
# postBuildStatus ${17} $POST_URL "DOWNLOADING_RESOURCES" "No ERROR" "Downloading the icon images" 18 "true" -1
# downloadFile ${15} ./fastlane/metadata/android/en-US/images/icon.png ${17} $POST_URL

postBuildStatus ${17} $POST_URL "DOWNLOADING_RESOURCES" "No ERROR" "Downloading the MetaData of the Application" 19 "true" -1

rm -rf ./fastlane/metadata/android/en-US/images/featureGraphic.png
rm -rf ./fastlane/metadata/android/en-US/images/featureGraphic.jpg
rm -rf ./fastlane/metadata/android/en-US/images/icon.jpg
rm -rf ./fastlane/metadata/android/en-US/images/icon.png
rm -rf ./fastlane/metadata/android/en-US/images/promoGraphic.png
rm -rf ./fastlane/metadata/android/en-US/images/promoGraphic.jpg
rm -rf ./fastlane/metadata/android/en-US/images/tvBanner.png
rm -rf ./fastlane/metadata/android/en-US/images/tvBanner.jpg

aws s3 cp s3://${19}/$1/build/android/resource/playstore/images ./fastlane/metadata/android/en-US/images/ --recursive

echo ${21} > ./fastlane/metadata/android/en-US/full_description.txt
echo ${22} > ./fastlane/metadata/android/en-US/short_description.txt
echo ${4} > ./fastlane/metadata/android/en-US/title.txt

slackMessageThree = "${4}" + " - ANDROID BUILD SUCCESSFULLY STARTED. BUILD-ID:" + "${17}" + ". VERSION-NUMBER:" + "${3}"

#3: Slack Second Message. Downloading Resources
fastlane android slackSendMessage my_slack_msg:"${4} -> ANDROID UPDATING VERSION PROPERTIES OF THE APPLICATION. BUILD-ID -> ${17}. VERSION-NUMBER -> ${3}. Build Triggered By --> ${23}" my_user_name:"Viewlift fastlane" mySlackUrl:"{24}"

#Now we need to check the Version of App on the Play Store.
postBuildStatus ${17} $POST_URL "UPDATING_VERSION_PROPERTIES" "No ERROR" "Fetching latest version from playstore and incrementing the same" 19 " " 0
fastlane android updateTheVersion app_package_name:$6 buildid:${17} json_key_file:./googleplay_android.json posturl:$POST_URL
postBuildStatus ${17} $POST_URL "UPDATING_VERSION_PROPERTIES" "No ERROR" "Version Name is Incremented" 20 " " 0

else
echo "App is not Present on Play Store"
fi

postBuildStatus ${17} $POST_URL "CONFIGURING_BUILD" "No ERROR" "Checking if app already exists on the App Store" 25 " " 0

echo "KEYSTORE PASSWORD"
echo "KEYSTORE PASSWORD"

keyPassWord=${20}
if [[ ${6} == *"myoutdoortv"* ]]; then
	keystorePassword=".6'3d7h\\\$MjJ{>AbF"
	keyPassWord="\"${keystorePassword}\""
  	echo ${keyPassWord}
fi

if [[ ${6} == *"marquee"* ]]; then
	keystorePassword="M,q<3@#B;mq3B"
	keyPassWord="\"${keystorePassword}\""
  	echo ${keyPassWord}
fi


if [[ ${6} == *"ksl"* ]]; then
	keystorePassword="5G%tY:"
	keyPassWord="\"${keystorePassword}\""
  	echo ${keyPassWord}
fi

fastlane android buildLane app_package_name:${6} buildid:${17} app_apk_path:./AppCMS/buildout/outputs/apk/mobileNonflavour/debug/AppCMS-mobile-nonflavour-debug.apk tests_apk_path:./AppCMS/buildout/outputs/apk/androidTest/mobile/debug/AppCMS-mobile-debug-androidTest.apk posturl:$POST_URL keystore_path:$8 alias:${9} storepass:${keyPassWord} apk_path:./AppCMS/buildout/outputs/apk/mobileNonflavour/release/AppCMS-mobile-nonflavour-universal-release-unsigned.apk track:${10} json_key_file:./googleplay_android.json username:"Viewlift fastlane" mySlackUrl:${24} myAppName:${4} myAppVersion:${3} myEmailId:${23} myBuildId:${17}
mv ./AppCMS/buildout/outputs/apk/mobileNonflavour/release/AppCMS-mobile-nonflavour-universal-release.apk "./AppCMS/buildout/outputs/apk/mobileNonflavour/release/build.apk"
/Users/fastlane/Library/Android/sdk/build-tools/29.0.3/zipalign -v 4 ./AppCMS/buildout/outputs/apk/mobileNonflavour/release/build.apk ./AppCMS/buildout/outputs/apk/mobileNonflavour/release/AppCMS-mobile-nonflavour-universal-release.apk


IS_APP_SUCCESS="$?"

echo "$IS_APP_SUCCESS"

if [ "$IS_APP_SUCCESS" -eq "0" ]
then

echo "Uploading Apk File On Partner Portal"


postBuildStatus ${17} $POST_URL "BUILD_PROGRESS" "No ERROR" "Build Generated and Preparing the Build for upload to S3 Bucket" 70 " " 0

appNamewithoutSpace=${4// /_}
echo $appNamewithoutSpace
myApkName="${appNamewithoutSpace}-android-${3}.apk"

cp ./AppCMS/buildout/outputs/apk/mobileNonflavour/release/AppCMS-mobile-nonflavour-universal-release.apk "./AppCMS/buildout/outputs/apk/mobileNonflavour/release/androidbuild.apk"

mv ./AppCMS/buildout/outputs/apk/mobileNonflavour/release/AppCMS-mobile-nonflavour-universal-release.apk "./AppCMS/buildout/outputs/apk/mobileNonflavour/release/${myApkName}"

aws s3 cp "./AppCMS/buildout/outputs/apk/mobileNonflavour/release/${myApkName}" s3://${19}/$1/build/android/

postBuildStatus ${17} $POST_URL "BUILD_PROGRESS" "No ERROR" "Build Created Successfully and Fetching the link from S3 Bucket" 75 " " 0

postUpdateLink ${17} $UPLOAD_URL "http://${19}.s3.amazonaws.com/$1/build/android/${myApkName}"

if [ "$IS_APP_ONPLAYSTORE" -eq "0" ]
then
echo "App is Present on Play Store"
postBuildStatus ${17} $POST_URL "SUCCESS_S3_BUCKET" "No ERROR" "Uploaded to the S3 Bucket" 80 "true" 0
sleep 5
postBuildStatus ${17} $POST_URL "UPLOADING_PLAY_STORE" "No ERROR" "Uploading the App MetaData and Build to Play Store" 85 "true" 0
fastlane android supply_onplaystore package_name:$6 track:${10} json_key_file:./googleplay_android.json apk_path:'./AppCMS/buildout/outputs/apk/nonflavour/release/androidbuild.apk' buildid:${17} posturl:$POST_URL mySlackUrl:${24} myAppName:${4} myAppVersion:${3} myEmailId:${23} myBuildId:${17}

else
echo "App is not Present on Play Store"
postBuildStatus ${17} $POST_URL "SUCCESS_S3_BUCKET" "No ERROR" "Download Apk and go to <a href='https://play.google.com/apps/publish/' target='_blank'> Playstore </a> for first time manual Uploading" 100 false 0
trap "echo exiting because first apk is not Present on playstore" 0
exit 1
fi
else
echo "Error Building"
# postBuildStatus ${17} $POST_URL "FAILED_BUILD_ERROR" "ERROR" "Build Generaton Failed" 65 " " 0
trap "echo exitting because my child killed me due to asset file not found" 0
exit 1
fi


# postBuildStatus ${17} $POST_URL "SUCCESS_PLAY_STORE" "No ERROR" "Successfully Uploaded to Play Store and Apk link is Updated" 100 " " 0



