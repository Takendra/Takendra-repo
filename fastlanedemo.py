from __future__ import with_statement # only for python 2.5 
from xmlbuilder import XMLBuilder 


import base64

import urllib, json, subprocess
from collections import OrderedDict
import sys
import os
import requests
import json
import time;



fileDirPath = os.path.dirname(os.path.abspath(__file__))
print fileDirPath
platform = sys.argv[1];
print platform
url = sys.argv[2];
print url

headers = {
    'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.155 Safari/537.36',
    'cache-control': 'private, max-age=0, no-cache'
}

url=url+"?x="+str(time.time())
r = requests.get(url, headers=headers)
urllib.urlcleanup()
response = urllib.urlopen(url)
urllib.urlcleanup()
data = json.loads(r.text, object_pairs_hook=OrderedDict)
mySplashBackground="#000000"
# print (data)
print "******************************************8"
file = open(fileDirPath + '/AppCMS/src/main/assets/version.properties', 'w')
file.close()

param = ""
siteName = sys.argv[3]
buildId = sys.argv[4]
uploadHostName = sys.argv[5]
print('Upload Host Name --> ' + uploadHostName)
bucketName = sys.argv[6]
myEmailId=sys.argv[7]
sampleSlackWebHookUrl="https://hooks.slack.com/services/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"

myApiSecretKey="df0813d31adc3b10b9884f5caf57d26a";

if bucketName=="appcms-config":
    global myApiSecretKey
    myApiSecretKey="df0813d31adc3b10b9884f5caf57d26a"

if bucketName=="appcms-config-prod":
    global myApiSecretKey
    myApiSecretKey="25db16e90345ea2bb1960ede8ee97bdb"
    

siteId = ""
baseUrl = ""
hostName = ""
appResourcePath = ""
appName = ""
fullDescription = "fullDescription"
shortDescription = ""
appVersionName = ""
appVersionCode = ""
appPackageName = ""
apptentiveApiKey = ""
keystoreFileName = ""
aliasName = ""
keystorePass = ""
track = ""
jsonKeyFile = ""
promoVideo = ""
appTitle = ""
featureGraphic="featureGraphic"
promoGraphic="promoGraphic"
tvBanner="tvBanner"
appIcon="tvBanner"
urbanAirshipDevKey = ""
urbanAirshipDevSecret = ""
urbanAirshipProdKey = ""
urbanAirshipProdSecret = ""
isUrbanAirshipInProduction = ""
gcmSender = ""
facebookAppId = ""
appsFlyerDevKey = ""
appsFlyerProdKey = ""
whatsNew = ""
keyval = ""
googleCredentialsFile = "googleCredentialsFile"
x = XMLBuilder('resources') 

myHostNameSuffix='*.site-snag.viewlift.com'
myHostName='site-snag.viewlift.com'

print "**************platform**********build*****************siteName*************"
print platform
print buildId
print siteName
print "myApiSecretKey"
print myApiSecretKey
print "myApiSecretKey"

# def buildFailError():
#     failParams=options[:buildid] + " " + options[:posturl] +  " " + "'FAILED_BUILD_ERROR'" + " " + "'Build Failure. Execution failed '" + " " + "'Build Failed'" + " " + progress.to_s  + " false " + myBuildVersion.to_s
#     subprocess.call("sh " + fileDirPath + "/fastlane/PostBuildStatus.sh " + param, shell=True)



def getUrbanAirshipAppKey():
    url = uploadHostName + '/appcms/build/data'

    payload = {
        'platform': platform,
        'buildId' : buildId,
        'siteInternalName' : siteName,
        'fieldName' : 'urbanAirshipAppKey'
    }
    # Adding empty header as parameters are being sent in payload
    headers = {"Content-Type": "application/json","secretKey" : myApiSecretKey}
    r = requests.post(url, data=json.dumps(payload), headers=headers)
    # print(r.json()["data"])
    urbanAirshipApplicationKey = r.json()["data"]

    if len(urbanAirshipApplicationKey)<22 :
        urbanAirshipApplicationKey ="1111111111111111111111"

    return urbanAirshipApplicationKey


def getUrbanAirshipAppSecret():
    url = uploadHostName + '/appcms/build/data'

    payload = {
        'platform': platform,
        'buildId' : buildId,
        'siteInternalName' : siteName,
        'fieldName' : 'urbanAirshipAppSecret'
    }
    # Adding empty header as parameters are being sent in payload
    headers = {"Content-Type": "application/json","secretKey" : myApiSecretKey}
    r = requests.post(url, data=json.dumps(payload), headers=headers)
    # print(r.json()["data"])
    urbanAirshipApplicationSecretKey = r.json()["data"]

    if len(urbanAirshipApplicationSecretKey)<22 :
        urbanAirshipApplicationSecretKey ="1111111111111111111111"

    return urbanAirshipApplicationSecretKey


def getKeyStorePassword():
    url = uploadHostName + '/appcms/build/data'

    payload = {
        'platform': platform,
        'buildId' : buildId,
        'siteInternalName' : siteName,
        'fieldName' : 'keystorePassword'
    }
    # Adding empty header as parameters are being sent in payload
    headers = {"Content-Type": "application/json","secretKey" : myApiSecretKey}
    r = requests.post(url, data=json.dumps(payload), headers=headers)
    # print(r.json()["data"])
    keystorePass = r.json()["data"]
    return keystorePass

splitservices = ""


def getServicesFile():
    url = uploadHostName + '/appcms/build/data'

    payload = {
        'platform': platform,
        'buildId' : buildId,
        'siteInternalName' : siteName,
        'fieldName' : 'googleServicesFile'
    }
    # Adding empty header as parameters are being sent in payload
    headers = {"Content-Type": "application/json","secretKey" : myApiSecretKey}
   
    r = requests.post(url, data=json.dumps(payload), headers=headers)
    # print(r.json()["data"])
    credentailsData = r.json()["data"]

    if credentailsData is None:
        credentailsData="credentailsData"
    else:
        credentailsData =credentailsData.split(",")
        splitservices = credentailsData[1]
        # print splitservices
        crfile = open(fileDirPath + '/AppCMS/crfile.txt', 'w')
        with open(fileDirPath + "/AppCMS/crfile.txt", "a") as myfile:
            crfile.write(splitservices.encode("utf-8"))
        crfile.close()
        gcmSenderFile=base64.b64decode(splitservices)
        global gcmSender
        gcmSender = json.loads(gcmSenderFile)
        gcmSender=gcmSender["project_info"]["project_number"]
        print gcmSender
    print "** END CALLING SERVICE FILE **"


getServicesFile()

splitCredentails = ""

def getCredentailsFile():
    url = uploadHostName + '/appcms/build/data'

    payload = {
        'platform': platform,
        'buildId' : buildId,
        'siteInternalName' : siteName,
        'fieldName' : 'googleCredentialFile'
    }
    # Adding empty header as parameters are being sent in payload
    headers = {"Content-Type": "application/json","secretKey" : myApiSecretKey}

    r = requests.post(url, data=json.dumps(payload), headers=headers)
    print r.status_code
    # print(r.json()["data"])

    credentailsData = r.json()["data"]

    if credentailsData is None:
        credentailsData="credentailsData"
    else:
        credentailsData =credentailsData.split(",")
        splitCredentails = credentailsData[1]
        # print "piyush"
        # print splitCredentails

        crfile = open(fileDirPath + '/credentialfile.txt', 'w')
        with open(fileDirPath + "/credentialfile.txt", "a") as myfile:
            crfile.write(splitCredentails.encode("utf-8"))
        crfile.close()
    # print "piyush"
    # print credentailsData

def getAppsFlyerProdKey():
    url = uploadHostName + '/appcms/build/data'

    payload = {
        'platform': platform,
        'buildId' : buildId,
        'siteInternalName' : siteName,
        'fieldName' : 'appFlyerKey'
    }
    # Adding empty header as parameters are being sent in payload
    headers = {"Content-Type": "application/json","secretKey" : myApiSecretKey}
    r = requests.post(url, data=json.dumps(payload), headers=headers)
    # print(r.json()["data"])
    appsFlyerProdKey = r.json()["data"]
    return appsFlyerProdKey




if platform == "android":
    getCredentailsFile()
    inProduction="true"
    developmentLogLevel="DEBUG"
    productionLogLevel="ERROR"
    notificationIcon="app_logo"
    notificationAccentColor="f4181c"
    #Writing Urban Airship Properties to the File
    path = fileDirPath+'/AppCMS/src/main/assets/airshipconfig.properties'

    fship= open(path,"w+")
    fship.write("productionAppKey="+str(getUrbanAirshipAppKey())+"\n")
    fship.write("productionAppSecret="+str(getUrbanAirshipAppSecret())+"\n\n\n")
    fship.write("inProduction="+inProduction+"\n\n\n")
    fship.write("developmentLogLevel="+developmentLogLevel+"\n")
    fship.write("productionLogLevel="+productionLogLevel+"\n\n\n")
    fship.write("fcmSender="+gcmSender+"\n\n\n")
    fship.write("notificationIcon="+notificationIcon+"\n")
    fship.write("notificationAccentColor="+notificationAccentColor+"\n")
    fship.close() 
    #Writing Urban Airship Properties to the File


for k in data.keys():
    
    print str(k)
    if k == "siteId":
        keyval = "SiteId" + ":" + data[k]
        siteId = data[k]

    if k == "versionCode":
        keyval = "AppVersionCode" + ":" + str(data[k])
        AppVersionCode = data[k]

    elif k == "baseUrl":
        # keyval = "BaseUrl" + ":" + data[k]
        baseUrl = data[k]
        continue

    elif k == "hostName":
        keyval = "HostName" + ":" + data[k]
        hostName = data[k]
        if hostName is None:
            hostName="hostName"

    elif k == "resourcePath":
        # keyval = "AppResourcesPath" + ":" + data[k]
        appResourcePath = data[k]
        if appResourcePath is None:
            appResourcePath="resourcePath"
        continue


    elif k == "appName":
        keyval = "AppName" + ":" + data[k]
        appName = data[k]


    elif k == "description":
        fullDescription = "data[k]"
        if fullDescription is None:
            fullDescription="fullDescription"
        continue

    elif k == "shortDescription":
        shortDescription = data[k]
        if shortDescription is None:
            shortDescription="shortDescription"
        continue


    elif k == "appVersion":
        keyval = "AppVersionName" + ":" + data[k]
        appVersionName = data[k]
        

    elif k == "packageName":
        keyval = "AppPackageName" + ":" + data[k]
        appPackageName = data[k]

    elif k == "apptentiveApiKey":
        apptentiveApiKey = data[k]
        if apptentiveApiKey is None:
            apptentiveApiKey="apptentiveApiKey"
        continue

    elif k == "keystoreFile":
        # keyval = "KeystorePath" + ":" + data[k]
        keystoreFileName=data[k]
        keystoreFileName=keystoreFileName+"?x="+str(time.time())
        continue


    elif k == "keystoreAliasName":
        # keyval = "AliasName" + ":" + data[k]
        aliasName = data[k]
        continue


    elif k == "track":
        # keyval = "Track" + ":" + data[k]
        track = data[k]
        if track is None:
            track="beta"
        continue


    elif k == "jsonKeyFile":
        # keyval = "JsonKeyFile" + ":" + data[k]
        jsonKeyFile = data[k]
        continue


    elif k == "promoVideo":
        # keyval = "PromoVideoUrl" + ":" + data[k]
        promoVideo = data[k]
        continue
     
    elif k == "featureGraphic":
        # keyval = "FeatureGraphicUrl" + ":" + data[k]
        featureGraphic = data[k]
        if featureGraphic is None:
            featureGraphic="featureGraphic"
        continue


    elif k == "promoGraphic":
        # keyval = "PromoGraphicUrl" + ":" + data[k]
        promoGraphic = data[k]
        if promoGraphic is None:
            promoGraphic="promoGraphic"
        continue


    elif k == "tvBanner":
        # keyval = "TvBannerUrl" + ":" + data[k]
        tvBanner = data[k]
        if tvBanner is None:
            tvBanner="tvBanner"
        continue

    elif k == "playIcon":
        # keyval = "AppIconUrl" + ":" + data[k]
        appIcon = data[k]
        continue

    elif k == "facebookAppId":
        keyval = "FacebookAppId" + ":" + data[k]
        facebookAppId = data[k]

    elif k == "splashBackgroud":
        x.color(data[k], name='splashbackgroundColor')
        etree_node = ~x
        print str(x)
        print "---------------------------- Theme ----------------------------"
        with open(fileDirPath + "/AppCMS/src/main/res/values/colors.xml", "w") as myfile:
            myfile.write(str(x).encode("utf-8") + "\n")

    elif k == "appsFlyerDevKey":
        # keyval = "AppsFlyerDevKey" + ":" + data[k]
        appsFlyerDevKey = data[k]
        continue


    elif k == "appsFlyerProdKey":
        # keyval = "AppsFlyerProdKey" + ":" + data[k]
        appsFlyerProdKey = data[k]
        continue


    elif k == "whatsnew":
        # keyval = "WhatsNew" + ":" + data[k]
        whatsNew = data[k]
        if whatsNew is None:
            whatsNew="whatsnew"
        continue


    elif k == "slackWebHook":
        sampleSlackWebHookUrl = data[k]
        if sampleSlackWebHookUrl is None:
            sampleSlackWebHookUrl="sampleSlackWebHookUrl"
        continue


    elif k == "theme":
        print "---------------------------- Theme ----------------------------"

        if data[k]["statusBar"] is None:
            print "statusBar is Null"
        else:
            if data[k]["statusBar"]["backgroundColor"] is None:
                x.color("#000000", name='colorPrimaryDark') 
            else:
                print(data[k]["statusBar"]["backgroundColor"])
                if "kronon" in data["packageName"]:
                    print "TRUE"
                    x.color("#cccccc", name='colorPrimaryDark') 
                else:
                    x.color(data[k]["statusBar"]["backgroundColor"], name='colorPrimaryDark') 

        if data[k]["general"]["backgroundColor"] is None:
             x.color("#1d5f79", name='colorPrimary') 
        else:
            print(data[k]["general"]["backgroundColor"])
            x.color(data[k]["general"]["backgroundColor"], name='colorPrimary') 


        if data[k]["cta"]['primary']["backgroundColor"] is None:
            x.color("#1d5f79", name='colorAccent') 
        else:
            print(data[k]["cta"]['primary']["backgroundColor"])
            x.color(data[k]["cta"]['primary']["backgroundColor"], name='colorAccent')  
    
        if data[k]["general"]["backgroundColor"] is None:
            x.color("#000000", name='backgroundColor') 
        else:
            x.color(data[k]["general"]["backgroundColor"], name='backgroundColor')

        if data[k]["cta"]["primary"]["border"]["color"] is None:
            x.color("#828282", name='cursorColor')
        else:
            x.color(data[k]["cta"]["primary"]["border"]["color"], name='cursorColor')


        x.color('#000000', name='splash_progress_color')
        x.color('#8F000000', name='blackTransparentColor') 
        x.color('#8c8c8c', name='colorNavBarText') 
        x.color('#3b5998', name='facebookBlue') 
        x.color('#c04b4c', name='googleRed') 
        x.color('#9D9FA2', name='disabledButtonColor') 
        x.color('#47000000', name='semiTransparentColor') 
        x.color('#00000000', name='transparentColor') 
        x.color('#414344', name='audioSeekBg') 
        x.color('#B5B5B5', name='volumeProgress') 

        x.color('#AE616161', name='appcms_shimmer_color')
        x.color('#7a7a7a', name='color_grey')
        x.color('#ffffff', name='color_white')

        x.color('#131415', name='expandable_grid_color')
        x.color('#ea0000', name='browse_category_selection')
        x.color('#4a90e2', name='link')
        x.color('#9b9b9b', name='noClassesScheduled')


        x.color('#4a4a4a', name='weekviewGridNodataold')
        x.color('#ffffff', name='weekviewGridNodata')
        x.color('#40ffffff', name='instagramBackground')

        x.color('#6A6A6A', name='backgroundTextViewColor')
        x.color('#ff1d38', name='colorLive')
        x.color('#d4d8dc', name='colorGrayLight')

        x.color('#26FFFFFF', name='left_navigation_focused_color')
        x.color('#0DFFFFFF', name='left_navigation_unFocused_color')
        x.color('#ea0000', name='player_focus')
        x.color('#ffe7d6', name='rest_workout_bg_color')
        x.color('#262626', name='recommendedbackgroundColor')

        x.color('#333', name='colorBlack')
        x.color('#fff', name='colorWhite')



    with open(fileDirPath + "/AppCMS/src/main/assets/version.properties", "a") as myfile:
         myfile.write(keyval.encode("utf-8") + "\n")


def getApptentiveSignatureKey():
    url = uploadHostName + '/appcms/build/data'
    payload = {
        'platform': platform,
        'buildId' : buildId,
        'siteInternalName' : siteName,
        'fieldName' : 'apptentiveAppSignature'
    }
    headers = {"Content-Type": "application/json","secretKey" : myApiSecretKey}
    r = requests.post(url, data=json.dumps(payload), headers=headers)
    credentailsData = r.json()["data"]
    print credentailsData
    return credentailsData


def getApptentiveApiKey():
    url = uploadHostName + '/appcms/build/data'
    payload = {
        'platform': platform,
        'buildId' : buildId,
        'siteInternalName' : siteName,
        'fieldName' : 'apptentiveAppKey'
    }
    headers = {"Content-Type": "application/json","secretKey" : myApiSecretKey}
    r = requests.post(url, data=json.dumps(payload), headers=headers)
    if r.status_code==200:
        credentailsData = r.json()["data"]
    else:
        credentailsData=None

    print credentailsData
    return credentailsData




def getFaceBookAppId():
    print "Gettnng Facebook App id"
    url = uploadHostName + '/appcms/build/data'
    payload = {
        'platform': platform,
        'buildId' : buildId,
        'siteInternalName' : siteName,
        'fieldName' : 'facebookId'
    }
    headers = {"Content-Type": "application/json","secretKey" : myApiSecretKey}
    r = requests.post(url, data=json.dumps(payload), headers=headers)
    credentailsData = r.json()["data"]
    print credentailsData
    return credentailsData


def getDevAppKey():
    url = uploadHostName + '/appcms/build/data'
    payload = {
        'platform': platform,
        'buildId' : buildId,
        'siteInternalName' : siteName,
        'fieldName' : 'googleServicesFile'
    }
    headers = {"Content-Type": "application/json","secretKey" : myApiSecretKey}
    r = requests.post(url, data=json.dumps(payload), headers=headers)
    credentailsData = r.json()["data"]
    return credentailsData


def getDevAppSecret():
    url = uploadHostName + '/appcms/build/data'
    payload = {
        'platform': platform,
        'buildId' : buildId,
        'siteInternalName' : siteName,
        'fieldName' : 'googleServicesFile'
    }
    headers = {"Content-Type": "application/json","secretKey" : myApiSecretKey}
    r = requests.post(url, data=json.dumps(payload), headers=headers)
    credentailsData = r.json()["data"]
    return credentailsData

#def getSocialAppId():
#    print "Gettnng Facebook App id"
#    url = uploadHostName + '/appcms/build/data'
#    payload = {
#        'platform': platform,
#        'buildId' : buildId,
#        'siteInternalName' : siteName,
#        'fieldName' : 'getSocialAppId'
#    }
#    headers = {"Content-Type": "application/json","secretKey" : myApiSecretKey}
#    r = requests.post(url, data=json.dumps(payload), headers=headers)
#    credentailsData = r.json()["data"]
#    print credentailsData
#    return credentailsData


# versionCodeValue = 2
# keyval = "AppVersionCode" + ":" + str(versionCodeValue)
# with open(fileDirPath + "/AppCMS/src/main/assets/version.properties", "a") as myfile:
#          myfile.write(keyval.encode("utf-8") + "\n")


keyval = "isWebSubscription" + ":" + "false"
with open(fileDirPath + "/AppCMS/src/main/assets/version.properties", "a") as myfile:
         myfile.write(keyval.encode("utf-8") + "\n")

keyval = "XAPI" + ":" + "vdTAMerEdh8t5t7xtUAa199qBKQuFLXb5cuG93ZF"
with open(fileDirPath + "/AppCMS/src/main/assets/version.properties", "a") as myfile:
         myfile.write(keyval.encode("utf-8") + "\n")


# myHostNameSuffix='*.http\://'+appName.lower()+'.viewlift.com/'
# myHostName='http\://'+appName.lower()+'.viewlift.com/'




keyval = "HostNameSuffix" + ":" + "*." + hostName
with open(fileDirPath + "/AppCMS/src/main/assets/version.properties", "a") as myfile:
         myfile.write(keyval.encode("utf-8") + "\n")

keyval = "BaseUrl" + ":" + baseUrl[:-1]
with open(fileDirPath + "/AppCMS/src/main/assets/version.properties", "a") as myfile:
         myfile.write(keyval.encode("utf-8") + "\n")

keyval = "HostName" + ":" + hostName
with open(fileDirPath + "/AppCMS/src/main/assets/version.properties", "a") as myfile:
         myfile.write(keyval.encode("utf-8") + "\n")


if platform=="android":
    keyval = "FacebookAppId" + ":" + str(getFaceBookAppId())
    with open(fileDirPath + "/AppCMS/src/main/assets/version.properties", "a") as myfile:
             myfile.write(keyval.encode("utf-8") + "\n")

    #keyval = "GetSocialAppId" + ":" + str(getSocialAppId())
    #with open(fileDirPath + "/AppCMS/src/main/assets/version.properties", "a") as myfile:
    #         myfile.write(keyval.encode("utf-8") + "\n")

    #keyval = "ApptentiveApiKey" + ":" + str(getApptentiveApiKey())
    #with open(fileDirPath + "/AppCMS/src/main/assets/version.properties", "a") as myfile:
    #     myfile.write(keyval.encode("utf-8") + "\n")

    #keyval = "ApptentiveSignatureKey" + ":" + str(getApptentiveSignatureKey())
    #with open(fileDirPath + "/AppCMS/src/main/assets/version.properties", "a") as myfile:
    #     myfile.write(keyval.encode("utf-8") + "\n")

    keyval = "AppsFlyerKey" + ":" + str(getAppsFlyerProdKey())
    with open(fileDirPath + "/AppCMS/src/main/assets/version.properties", "a") as myfile:
         myfile.write(keyval.encode("utf-8") + "\n")

elif platform == "fireTv":
    keyval = "FacebookAppId" + ":" + "217708136458067"
    with open(fileDirPath + "/AppCMS/src/main/assets/version.properties", "a") as myfile:
             myfile.write(keyval.encode("utf-8") + "\n")

    keyval = "ApptentiveApiKey" + ":" + "ANDROID-ARENA-FOOTBALL-LEAGUE"
    with open(fileDirPath + "/AppCMS/src/main/assets/version.properties", "a") as myfile:
         myfile.write(keyval.encode("utf-8") + "\n")

    keyval = "ApptentiveSignatureKey" + ":" + "3662489474d4a82ad0f6dda0abfbb19c"
    with open(fileDirPath + "/AppCMS/src/main/assets/version.properties", "a") as myfile:
         myfile.write(keyval.encode("utf-8") + "\n")


keystorePass = getKeyStorePassword()
appVersionCode = 1
shortDescription = shortDescription.replace(" ", "_")
whatsNew = whatsNew.replace(" ", "_")

track="alpha"

if platform == "android":
    param = siteId + " " \
        + baseUrl + " " \
        + appVersionName + " '" \
        + appName + "' " \
        + str(appVersionCode) + " " \
        + appPackageName + " " \
        + appResourcePath + " " \
        + keystoreFileName + " " \
        + aliasName + " " \
        + track + " " \
        + googleCredentialsFile + " " \
        + featureGraphic + " " \
        + promoGraphic + " " \
        + tvBanner + " " \
        + appIcon + " " \
        + siteName + " " \
        + str(buildId) + " " \
        + uploadHostName + " " \
        + bucketName + " '" \
        + keystorePass + "' '" \
        + shortDescription + "' '" \
        + whatsNew + "' '" \
        + myEmailId + "' " \
        + "sampleSlackWebHookUrl"
elif platform == "fireTv":
     param = siteId + " " \
        + baseUrl + " " \
        + appVersionName + " '" \
        + appName + "' " \
        + str(appVersionCode) + " " \
        + appPackageName + " " \
        + appResourcePath + " " \
        + keystoreFileName + " " \
        + aliasName + " " \
        + track + " " \
        + googleCredentialsFile + " " \
        + siteName + " " \
        + str(buildId) + " " \
        + uploadHostName + " " \
        + bucketName + " " \
        + keystorePass + " '" \
        + myEmailId + "' " \
        + "sampleSlackWebHookUrl"

print param


if platform == "android":
    subprocess.call("sh " + fileDirPath + "/fastlanemobile.sh " + param, shell=True)
elif platform == "fireTv":
    subprocess.call("sh " + fileDirPath + "/fastlanetv.sh " + param, shell=True)