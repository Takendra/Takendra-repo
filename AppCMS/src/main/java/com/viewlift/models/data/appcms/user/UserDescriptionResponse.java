package com.viewlift.models.data.appcms.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.viewlift.models.data.appcms.api.AppCMSPageAPI;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Module;
import com.vimeo.stag.UseStag;

import java.util.ArrayList;
import java.util.List;
@UseStag
public class UserDescriptionResponse {
    @SerializedName("username")
    @Expose
    String username;
    @SerializedName("location")
    @Expose
    String location;
    @SerializedName("name")
    @Expose
    String name;
    @SerializedName("lastName")
    @Expose
    String lastName;
    @SerializedName("gender")
    @Expose
    String gender;
    @SerializedName("DOB")
    @Expose
    String DOB;
    @SerializedName("zipCode")
    @Expose
    String zipCode;
    @SerializedName("questions")
    @Expose
    List<Question> questions;
    @SerializedName("avatarUrl")
    @Expose
    String avatarUrl;

    public String getUsername() {
        return username;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getDOB() {
        return DOB;
    }

    public String getZipCode() {
        return zipCode;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public AppCMSPageAPI convertToAppCMSPageAPI(String id, UserDescriptionResponse userDescriptionResponse) {
        AppCMSPageAPI appCMSPageAPI = new AppCMSPageAPI();
        Module module = new Module();
        List<ContentDatum> data = new ArrayList<>();

        ContentDatum contentDatum = new ContentDatum();
        contentDatum.setId(id);
        contentDatum.setUserDescriptionResponse(userDescriptionResponse);
        data.add(contentDatum);
        module.setContentData(data);
        appCMSPageAPI.setId(id);
        List<Module> moduleList = new ArrayList<>();
        moduleList.add(module);
        appCMSPageAPI.setModules(moduleList);

        return appCMSPageAPI;
    }
}
