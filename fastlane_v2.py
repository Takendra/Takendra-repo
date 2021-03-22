import json
import base64
import os
import json
import time
import requests
import xml.etree.ElementTree as ET
from .fastlane_tv import ExecuteFireTv
from .logger import get_logger

# {
#   'serverBaseUrl': 'https://staging-tools.viewlift.com',
#   'userName': 'gaurav.vig+staging.motv@viewlift.com',
#   'buildId': 24,
#   'site': 'staging-motv',
#   'buildDetails': 'https://appcmsstaging.viewlift.com/ca367482-b6f0-4467-82e3-278cc6aafc60/build/appleTv/build.json',
#   'platform': 'appleTv',
#   'bucketName': 'appcms-config-staging1'
# }

class Fastlane:
    def __init__(self, platform, build_details, site, build_id, server_base_url, bucket_name, username):
        self.logger = get_logger("fastlane_android_v2_{0}".format(build_id))
        self.PLATFORM = platform
        self.SITE = site
        self.BUILD_ID = build_id
        self.TOOLS_URL = server_base_url
        self.BUCKET_NAME = bucket_name
        self.MY_EMAIL_ID = username
        self.BUILD_JSON_URL = build_details
        self.build_data_url = self.TOOLS_URL + '/appcms/build/data'

        self.urban_airship_app_key = None
        self.urban_airship_application_secret_key = None
        self.keystore_password = None
        self.google_services_data = None
        self.google_credential_data = None
        self.gs_project_number = None
        self.app_flyer_key = None
        self.facebook_id = None

        self.api_secret_key= self.get_api_secret_key()
        self.working_dir = os.path.dirname(os.path.abspath(__file__))
        
    def get_api_secret_key(self):
        secret_key =  "df0813d31adc3b10b9884f5caf57d26a"
        if self.BUCKET_NAME=="appcms-config-prod":
            secret_key =  "25db16e90345ea2bb1960ede8ee97bdb"

        return secret_key
    
    def get_fire_tv_build_data(self):
        payload = {
            'platform': self.PLATFORM,
            'buildId' : self.BUILD_ID,
            'siteInternalName' : self.SITE,
        }
        headers = {"Content-Type": "application/json","secretKey" : self.api_secret_key}

        self.logger.info(f"Build Data URL: {self.build_data_url}")
        self.logger.info(f"payload: {payload}")
        self.logger.info(f"headers: {headers}")

        payload["fieldName"] = "keystorePassword"
        resp = requests.post(self.build_data_url, data=json.dumps(payload), headers=headers)
        data = resp.json()["data"]
        self.keystore_password = data
        self.logger.info("FireTV build keystorePassword from tools: {0}".format(data))

        payload["fieldName"] = "googleServicesFile"
        resp = requests.post(self.build_data_url, data=json.dumps(payload), headers=headers)
        data = resp.json()["data"]
        self.google_services_data = data
        self.logger.info("FireTV build googleServicesFile from tools: {0}".format(data))

        payload["fieldName"] = "googleCredentialFile"
        resp = requests.post(self.build_data_url, data=json.dumps(payload), headers=headers)
        data = resp.json()["data"]
        self.google_credential_data = data
        self.logger.info("FireTV build googleCredentialFile from tools: {0}".format(data))
        
        # self.urban_airship_app_key = build_data["urbanAirshipAppKey"]
        # if len(self.urban_airship_app_key) < 22: self.urban_airship_app_key = "1111111111111111111111"

        # self.urban_airship_application_secret_key = build_data["urbanAirshipAppSecret"]
        # if len(self.urban_airship_application_secret_key) < 22: self.urban_airship_application_secret_key = "1111111111111111111111"

        # self.app_flyer_key = build_data["appFlyerKey"]
        # self.facebook_id = build_data["facebookId"]

    def create_google_service_file(self):
        google_service_file_path = "{0}/AppCMS/crfile.txt".format(self.working_dir)
        if self.google_services_data:
            credentailsData = self.google_services_data.split(",")[1]
            with open(google_service_file_path, "wb") as w:
                w.write(credentailsData.encode("utf-8"))
            
            gs_string = base64.b64decode(credentailsData)
            gs_data = json.loads(gs_string)
            self.gs_project_number  = gs_data["project_info"]["project_number"]

    def create_google_credential_file(self):
        google_cred_file_path = "{0}/credentialfile.txt".format(self.working_dir)
        if self.google_credential_data:
            credentailsData = self.google_credential_data.split(",")[1]
            with open(google_cred_file_path, "wb") as w:
                w.write(credentailsData.encode("utf-8"))

    def create_firetv_version_properties_file(self, build_data):
        version_file_path = "{0}/AppCMS/src/main/assets/version.properties".format(self.working_dir)
        propeties = []

        propeties.append(f"SiteId={build_data['siteId']}")
        propeties.append(f"AppVersionCode={str(build_data['versionCode'])}")
        propeties.append(f"HostName={build_data['hostName']}")
        propeties.append(f"AppName={build_data['appName']}")
        propeties.append(f"AppVersionName={build_data['appVersion']}")
        propeties.append(f"AppPackageName={build_data['packageName']}")
        propeties.append(f"HostNameSuffix=*.{build_data['hostName']}")
        propeties.append(f"BaseUrl={build_data['baseUrl'][:-1]}")
        propeties.append(f"FacebookAppId=217708136458067")
        propeties.append(f"ApptentiveApiKey=ANDROID-ARENA-FOOTBALL-LEAGUE")
        propeties.append(f"ApptentiveSignatureKey=3662489474d4a82ad0f6dda0abfbb19c")
        propeties.append("isWebSubscription=false")
        propeties.append("XAPI=vdTAMerEdh8t5t7xtUAa199qBKQuFLXb5cuG93ZF")
        propeties.append("isWebSubscription=false")

        with open(version_file_path, "w") as w:
            for property in propeties:
                w.write(property + "\n")
        
    def create_color_xml(self, build_data):
        package_name = build_data["packageName"]
        nodes = []

        if build_data.get("splashBackgroud"):
            nodes.append({"name": "splashbackgroundColor", "value": build_data["splashBackgroud"]})

        if build_data.get("theme"):

            if build_data["theme"].get("statusBar"):
                background_color = build_data["theme"]["statusBar"].get("backgroundColor")
                if "kronon" in package_name:
                    nodes.append({"name": "colorPrimaryDark", "value": "#cccccc"})
                else:
                    background_color = background_color or "#000000"
                    nodes.append({"name": "colorPrimaryDark", "value": background_color})
            
            general_background = build_data["theme"]["general"]["backgroundColor"]
            general_background_primary = general_background or "#1d5f79"
            general_background_color = general_background or "#000000"
            nodes.append({"name": "colorPrimary", "value": general_background_primary})
            nodes.append({"name": "backgroundColor", "value": general_background_color})

            cta_background = build_data["theme"]["cta"]['primary']["backgroundColor"]
            cta_background = cta_background or "#1d5f79"
            nodes.append({"name": "colorAccent", "value": cta_background})

            cta_border = build_data["theme"]["cta"]["primary"]["border"]["color"]
            cta_border = cta_border or "#828282"
            nodes.append({"name": "cursorColor", "value": cta_border})

            nodes.append({"value" : "#000000", "name" : "splash_progress_color"})
            nodes.append({"value" : "#8F000000", "name" : "blackTransparentColor"}) 
            nodes.append({"value" : "#8c8c8c", "name" : "colorNavBarText"}) 
            nodes.append({"value" : "#3b5998", "name" : "facebookBlue"}) 
            nodes.append({"value" : "#c04b4c", "name" : "googleRed"}) 
            nodes.append({"value" : "#9D9FA2", "name" : "disabledButtonColor"}) 
            nodes.append({"value" : "#47000000", "name" : "semiTransparentColor"}) 
            nodes.append({"value" : "#00000000", "name" : "transparentColor"}) 
            nodes.append({"value" : "#414344", "name" : "audioSeekBg"}) 
            nodes.append({"value" : "#B5B5B5", "name" : "volumeProgress"}) 
            nodes.append({"value" : "#AE616161", "name" : "appcms_shimmer_color"})
            nodes.append({"value" : "#7a7a7a", "name" : "color_grey"})
            nodes.append({"value" : "#ffffff", "name" : "color_white"})
            nodes.append({"value" : "#131415", "name" : "expandable_grid_color"})
            nodes.append({"value" : "#ea0000", "name" : "browse_category_selection"})
            nodes.append({"value" : "#4a90e2", "name" : "link"})
            nodes.append({"value" : "#9b9b9b", "name" : "noClassesScheduled"})
            nodes.append({"value" : "#4a4a4a", "name" : "weekviewGridNodataold"})
            nodes.append({"value" : "#ffffff", "name" : "weekviewGridNodata"})
            nodes.append({"value" : "#40ffffff", "name" : "instagramBackground"})
            nodes.append({"value" : "#6A6A6A", "name" : "backgroundTextViewColor"})
            nodes.append({"value" : "#ff1d38", "name" : "colorLive"})
            nodes.append({"value" : "#d4d8dc", "name" : "colorGrayLight"})
            nodes.append({"value" : "#26FFFFFF", "name" : "left_navigation_focused_color"})
            nodes.append({"value" : "#0DFFFFFF", "name" : "left_navigation_unFocused_color"})
            nodes.append({"value" : "#ea0000", "name" : "player_focus"})
            nodes.append({"value" : "#ffe7d6", "name" : "rest_workout_bg_color"})
            nodes.append({"value" : "#262626", "name" : "recommendedbackgroundColor"})
            nodes.append({"value" : "#333", "name" : "colorBlack"})
            nodes.append({"value" : "#fff", "name" : "colorWhite"})

        root = ET.Element("resources")
        for node in nodes:
            element = ET.SubElement(root, "color", {"name": node['name']})
            element.text = node['value']
    
        color_xml_path = f"{self.working_dir}/AppCMS/src/main/res/values/colors.xml"
        tree = ET.ElementTree(root)
        tree.write(color_xml_path)

    def run(self):
        try:
            build_json_url = "{0}?timestamp={1}".format(self.BUILD_JSON_URL, time.time())
            self.logger.info("BUILD_JSON_URL: {0}".format(build_json_url))
            build_json_resp = requests.get(build_json_url)
            build_data = build_json_resp.json()
            self.logger.debug("Build Json Data: {0}".format(build_data))

            if self.PLATFORM == "fireTv":
                self.get_fire_tv_build_data()
                self.create_google_service_file()
                self.create_google_credential_file()
                self.create_firetv_version_properties_file(build_data)
                self.create_color_xml(build_data)

            data = build_data
            data["platform"] = self.PLATFORM
            data["google_credential_data"] = self.google_credential_data
            data["site"] = self.SITE
            data["build_id"] = self.BUILD_ID
            data["tools_url"] = self.TOOLS_URL
            data["bucket_name"]  = self.BUCKET_NAME
            data["keystore_password"] = self.keystore_password
            data["project_path"] = self.working_dir
            data["email_id"]  = self.MY_EMAIL_ID
            self.logger.info("DATA: {0}".format(data))
     
            if self.PLATFORM == "fireTv":
                execute_tv = ExecuteFireTv(data)
                execute_tv.build()

        except Exception as err:
            self.logger.error("Exception occurred", exc_info=True)