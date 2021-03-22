import subprocess
import requests
import base64
import time
import json
import os
from .logger import get_logger

class ExecuteFireTv:
    def __init__(self, data):
        self.logger = get_logger("fire_tv_{0}".format(data["build_id"]))
        self.project_path = data["project_path"]
        self.platform = data["platform"]
        self.siteId = data['siteId']
        self.baseUrl = data['baseUrl']
        self.app_version = data['appVersion']
        self.app_name = data['appName']
        self.AppVersionCode = str(data['versionCode'])
        self.app_package_name = data['packageName']
        self.appResourcePath = data.get('resourcePath') or 'resourcePath'
        self.keystore_file = f"{data['keystoreFile']}?x={time.time()}"
        self.keystore_alias = data["keystoreAliasName"]
        self.track = data.get("track") or "beta"
        self.googleCredentialsFile = data["google_credential_data"]
        self.site = data["site"] # siteName
        self.build_id = data["build_id"]
        self.tools_url = data["tools_url"] # uploadHostName
        self.bucket_name = data["bucket_name"] # bucketName
        self.keystore_password =  "M,q<3@#B;mq3B" if "marquee" in self.site else data["keystore_password"]  # keystorePass
        self.email_id = data["email_id"] # myEmailId

        self.tools_status_url = f"{self.tools_url}/{self.site}/{self.platform}/appcms/build/status"
        self.apk_upload_url = f"{self.tools_url}/{self.site}/appcms/{self.platform}/build/apk/link"
        self.progress = 0

        # self.hostName = data.get('hostName') or 'hostName'
        # self.jsonKeyFile = data.get("jsonKeyFile")
        # self.promoVideo = data.get("promoVideo")
        # self.featureGraphic = data.get("featureGraphic") or "featureGraphic"
        # self.promoGraphic = data.get("promoGraphic") or "promoGraphic"
        # self.tvBanner = data.get("tvBanner") or "tvBanner"
        # self.appIcon = data.get("playIcon")
        # self.facebookAppId = data.get("facebookAppId")
        # self.appsFlyerDevKey = data.get("appsFlyerDevKey")
        # self.appsFlyerProdKey = data.get("appsFlyerProdKey")
        # self.whatsnew = data.get("whatsnew") or "whatsnew"
    
    def update_build_status(self, status, error_message, message, percent_completed, build_version):
        self.logger.debug("Sending build status")
        self.progress = percent_completed
        payload = {
            "buildId": int(self.build_id),
            "status": status,
            "errorMessage": error_message,
            "message": message,
            "percentComplete": percent_completed,
            "buildVersion": build_version
        }

        self.logger.debug("Payload: {0}\n".format(payload))
        resp = requests.post(self.tools_status_url, data=json.dumps(payload))
        self.logger.debug("Build status response: {0}\n".format(resp.status_code))

    def update_apk_link(self, apkLink):
        self.logger.debug("Updating apk link")
        payload = {
            "buildId": int(self.build_id),
            "apkLink": apkLink,
            "platform": "android"
        }

        self.logger.debug("Payload: {0}\n".format(payload))
        resp = requests.put(self.apk_upload_url, data=json.dumps(payload))
        self.logger.debug("Update apk link response: {0}\n".format(resp.status_code))


    def send_to_shell(self, cmd):
        os.chdir(self.project_path)
        self.logger.debug("CMD: {0} - {1}\n".format(os.getcwd(), cmd))
        status = subprocess.call(cmd, shell=True)
        return status

    def copy_from_bucket(self, remote_path, local_path, platform, recursive=True):
        platform = platform if platform else self.platform
        cmd = "aws s3 cp s3://{bucket_name}/{site_id}/build/{platform}/{remote_path} {local_path}"
        cmd = "{} --recursive".format(cmd) if recursive else cmd
        cmd = cmd.format(bucket_name=self.bucket_name, site_id=self.siteId, platform=platform, remote_path=remote_path, local_path=local_path)
        self.send_to_shell(cmd)

    def download_s3_resources(self):
        drawable_main_path = f"{self.project_path}/AppCMS/src/main/res/drawable"
        drawable_tv_path = f"{self.project_path}/AppCMS/src/tv/res/drawable"
        drawable_xhdpi_main_path = f"{self.project_path}/AppCMS/src/main/res/drawable-xhdpi"
        drawable_xhdpi_tv_path = f"{self.project_path}/AppCMS/src/tv/res/drawable-xhdpi"

        self.copy_from_bucket("resource/drawable", drawable_main_path, self.platform)
        self.copy_from_bucket("resource/drawable", drawable_tv_path, self.platform)
        self.copy_from_bucket("resource/drawable-xhdpi", drawable_xhdpi_main_path, self.platform)
        self.copy_from_bucket("resource/drawable-xhdpi", drawable_xhdpi_tv_path, self.platform)
        
    def convert_images(self):
        drawable_tv_path = f"{self.project_path}/AppCMS/src/tv/res/drawable"
        drawable_xhdpi_main_path = f"{self.project_path}/AppCMS/src/main/res/drawable-xhdpi"
        drawable_xhdpi_tv_path = f"{self.project_path}/AppCMS/src/tv/res/drawable-xhdpi"

        cmd = f"cp {drawable_xhdpi_tv_path}/ott_app_icon.png {drawable_xhdpi_tv_path}/manage_app_logo.png"
        self.send_to_shell(cmd)

        cmd = f"cp {drawable_xhdpi_tv_path}/ott_app_icon.png {drawable_tv_path}/manage_app_logo.png"
        self.send_to_shell(cmd)

        cmd = f"convert {drawable_xhdpi_tv_path}/mobile_app_logo.png -resize 433x140 {drawable_xhdpi_tv_path}/footer_logo.png"
        self.send_to_shell(cmd)

        cmd = f"convert {drawable_xhdpi_tv_path}/mobile_app_logo.png -resize 433x140 {drawable_tv_path}/footer_logo.png"
        self.send_to_shell(cmd)

        cmd = f"convert {drawable_xhdpi_tv_path}/video_image_placeholder.png -resize 1000x281! {drawable_xhdpi_tv_path}/video_image_placeholder_wide.png"
        self.send_to_shell(cmd)

        cmd = f"convert {drawable_xhdpi_tv_path}/manage_app_logo.png -resize 320x180! {drawable_xhdpi_tv_path}/androidtv_banner_image.png"
        self.send_to_shell(cmd)

        cmd = f"convert {drawable_xhdpi_tv_path}/manage_app_logo.png -resize 800x450! {drawable_tv_path}/androidtv_banner_image.png"
        self.send_to_shell(cmd)

    def create_google_service_file(self):
        google_service_file_path = "{0}/AppCMS/crfile.txt".format(self.project_path)

        if os.path.exists(google_service_file_path):
            with open(google_service_file_path) as r:
                gs_data = r.read()
                gs_string = base64.b64decode(gs_data)
                gs_json = json.loads(gs_string)

                gs_file_loc = f"{self.project_path}/AppCMS/google-services.json"
                self.logger.info("GS JSON File Loc: {gs_file_loc}")

                with open(gs_file_loc, "w") as w:
                    json.dump(gs_json, w)

    def create_google_play_file(self):
        google_cred_file_path = "{0}/credentialfile.txt".format(self.project_path)

        if os.path.exists(google_cred_file_path):
            with open(google_cred_file_path) as r:
                ps_data = r.read()
                ps_string = base64.b64decode(ps_data)
                ps_json = json.loads(ps_string)

                ps_file_loc = f"{self.project_path}/googleplay_android.json"
                self.logger.info("Playstore JSON File Loc: {ps_file_loc}")

                with open(ps_file_loc, "w") as w:
                    json.dump(ps_json, w)

    def tv_build_creation(self):
        params = [
            ("buildid", self.build_id),
            ("myBuildId", self.build_id),
            ("app_package_name", self.app_package_name),
            ("app_apk_path", f"{self.project_path}/AppCMS/buildout/outputs/apk/tv/debug/AppCMS-tv-debug.apk"),
            ("tests_apk_path", f"{self.project_path}/AppCMS/buildout/outputs/apk/androidTest/tv/debug/AppCMS-tv-debug-androidTest.apk"),
            ("apk_path", f"{self.project_path}/AppCMS/buildout/outputs/apk/tvNonflavour/release/AppCMS-tv-nonflavour-universal-release-unsigned.apk"),
            ("posturl", self.tools_status_url),
            ("keystore_path", self.keystore_file),
            ("alias", self.keystore_alias),
            ("storepass", self.keystore_password),
            ("myAppName", self.app_name),
            ("myAppVersion", self.app_version),
            ("myEmailId", self.email_id),
            ("mySlackUrl", "")
        ]

        self.logger.debug("TV build params: {0}".format(params))
        params_string = " ".join([":".join(param) for param in params])
        cmd = f"fastlane android tvbeta {params_string}"
        status = self.send_to_shell(cmd)
        return status

    def cleanup(self):
        self.logger.debug("Files cleanup...")
        files = [
            f"{self.project_path}/AppCMS/src/main/res/drawable-xhdpi/video_image_placeholder.jpg",
            f"{self.project_path}/AppCMS/src/main/res/drawable-xhdpi/video_image_placeholder.png",
            f"{self.project_path}/AppCMS/src/tv/res/drawable-xhdpi/video_image_placeholder.jpg",
            f"{self.project_path}/AppCMS/src/tv/res/drawable-xhdpi/video_image_placeholder.png",
            f"{self.project_path}/AppCMS/src/main/res/drawable/poster_image_placeholder.jpg",
            f"{self.project_path}/AppCMS/src/main/res/drawable/poster_image_placeholder.png",
            f"{self.project_path}/AppCMS/src/main/res/drawable-xhdpi/poster_image_placeholder.jpg",
            f"{self.project_path}/AppCMS/src/main/res/drawable-xhdpi/poster_image_placeholder.png",
            f"{self.project_path}/AppCMS/src/tv/res/drawable/poster_image_placeholder.jpg",
            f"{self.project_path}/AppCMS/src/tv/res/drawable/poster_image_placeholder.png",
            f"{self.project_path}/AppCMS/src/tv/res/drawable-xhdpi/poster_image_placeholder.jpg",
            f"{self.project_path}/AppCMS/src/tv/res/drawable-xhdpi/poster_image_placeholder.png",
            f"{self.project_path}/AppCMS/src/main/res/drawable-xhdpi/tv_logo.jpg",
            f"{self.project_path}/AppCMS/src/main/res/drawable-xhdpi/tv_logo.png",
            f"{self.project_path}/AppCMS/src/tv/res/drawable-xhdpi/tv_logo.jpg",
            f"{self.project_path}/AppCMS/src/tv/res/drawable-xhdpi/tv_logo.png",
            f"{self.project_path}/AppCMS/src/main/res/drawable-xhdpi/app_logo.jpg",
            f"{self.project_path}/AppCMS/src/main/res/drawable-xhdpi/app_logo.png",
            f"{self.project_path}/AppCMS/src/tv/res/drawable-xhdpi/app_logo.jpg",
            f"{self.project_path}/AppCMS/src/tv/res/drawable-xhdpi/app_logo.png",
            f"{self.project_path}/AppCMS/src/tv/res/drawable-xhdpi/manage_app_logo.png",
            f"{self.project_path}/AppCMS/src/tv/res/drawable-xhdpi/manage_app_logo.jpg",
            f"{self.project_path}/AppCMS/src/tv/res/drawable/manage_app_logo.png",
            f"{self.project_path}/AppCMS/src/tv/res/drawable/manage_app_logo.jpg",
            f"{self.project_path}/AppCMS/src/tv/res/drawable-xhdpi/androidtv_banner_image.png",
            f"{self.project_path}/AppCMS/src/tv/res/drawable-xhdpi/androidtv_banner_image.jpg",
            f"{self.project_path}/AppCMS/src/tv/res/drawable/androidtv_banner_image.png",
            f"{self.project_path}/AppCMS/src/tv/res/drawable/androidtv_banner_image.jpg",
            f"{self.project_path}/AppCMS/src/tv/res/drawable-xhdpi/footer_logo.png",
            f"{self.project_path}/AppCMS/src/tv/res/drawable-xhdpi/footer_logo.jpg",
            f"{self.project_path}/AppCMS/src/tv/res/drawable/footer_logo.png",
            f"{self.project_path}/AppCMS/src/tv/res/drawable/footer_logo.jpg",
            f"{self.project_path}/AppCMS/src/tv/res/drawable-xhdpi/video_image_placeholder_wide.png",
            f"{self.project_path}/AppCMS/build/outputs/apk/tvNonflavour/release/AppCMS-tv-release-unsigned.apk"
            f"{self.project_path}/AppCMS/build/outputs/apk/tvNonflavour/release/AppCMS-tv-release.apk"
            f"{self.project_path}/AppCMS/build/outputs/apk/tvNonflavour/release/AppCMS-tv-release-unaligned.apk"
        ]

        for _file in files:
            if os.path.exists(_file): os.remove(_file)

    def build(self):
        try:
            # Build Status : 5%
            self.update_build_status("STARTED", "No ERROR", "Build Process Initiated Successfully", 5, 0)

            # 1. crtfile
            self.create_google_service_file()
            self.update_build_status("STARTED", "No ERROR", "1. Created google service file", 10, 0)

            # 2. cred file
            self.create_google_play_file()
            self.update_build_status("STARTED", "No ERROR", "2. Created google play file", 15, 0)

            # 3. cleanup
            self.cleanup()
            self.update_build_status("STARTED", "No ERROR", "3. Cleaup done", 20, 0)

            # 4. Download resources from S3
            self.download_s3_resources()
            self.update_build_status("DOWNLOADING_RESOURCES", "No ERROR", "4. Downloaded resource from S3", 25, 0)

            # 5. Connvert images
            self.convert_images()
            self.update_build_status("BUILD_PROGRESS", "No ERROR", "Coverted images...", 30, 0)

            # 6: Run fastlane build
            self.update_build_status("BUILD_PROGRESS", "No ERROR", "Creating tv build", 35, 0)
            status = self.tv_build_creation()
            is_success = True if status == 0 else False

            if is_success:
                self.logger.info("Build created sussessfully")
                self.logger.info("Preparing build to upload on S3")
                self.update_build_status("BUILD_PROGRESS", "No ERROR", "Build created successfully", 70, 0)

                cmd = f"mv {self.project_path}/AppCMS/buildout/outputs/apk/tvNonflavour/release/AppCMS-tv-nonflavour-universal-release.apk {self.project_path}/AppCMS/buildout/outputs/apk/tvNonflavour/release/build.apk"
                self.send_to_shell(cmd)

                self.update_build_status("BUILD_PROGRESS", "No ERROR", "zipalign...", 75, 0)
                cmd = f"/Users/fastlane/Library/Android/sdk/build-tools/29.0.3/zipalign -v 4 {self.project_path}/AppCMS/buildout/outputs/apk/tvNonflavour/release/build.apk {self.project_path}/AppCMS/buildout/outputs/apk/tvNonflavour/release/AppCMS-tv-nonflavour-universal-release.apk"
                self.send_to_shell(cmd)

                apk_name = self.app_name.replace(" ", "")
                apk_name = f"{apk_name}-{self.platform}-{self.app_version}.apk"

                cmd = f"mv {self.project_path}/AppCMS/buildout/outputs/apk/tvNonflavour/release/AppCMS-tv-nonflavour-universal-release.apk {self.project_path}/AppCMS/buildout/outputs/apk/tvNonflavour/release/{apk_name}"
                self.send_to_shell(cmd)

                self.update_build_status("BUILD_PROGRESS", "No ERROR", "uploading apk to s3...", 85, 0)
                cmd = f"aws s3 cp {self.project_path}/AppCMS/buildout/outputs/apk/tvNonflavour/release/{apk_name} s3://{self.bucket_name}/{self.siteId}/build/{self.platform}/"
                self.send_to_shell(cmd)
                self.update_build_status("BUILD_PROGRESS", "No ERROR", "Apk uploaded to s3", 90, 0)

                self.update_build_status("BUILD_PROGRESS", "No ERROR", "Updating apk link", 95, 0)
                self.update_apk_link(f"http://{self.bucket_name}.s3.amazonaws.com/{self.siteId}/build/{self.platform}/{apk_name}")
                self.update_build_status("SUCCESS_S3_BUCKET", "No ERROR", "Updated apk link", 100, 0)

            else:
                self.update_build_status("FAILED_BUILD_ERROR", "Not able to create the build", "Fastlane build failed", self.progress, 0)
                self.logger.error("BUILD FAILED: Not able to create the build")

        except Exception as err:
            self.update_build_status("FAILED_BUILD_ERROR", "Unexpeced error", str(err), self.progress, 0)
            self.logger.error("Exception occurred", exc_info=True)
