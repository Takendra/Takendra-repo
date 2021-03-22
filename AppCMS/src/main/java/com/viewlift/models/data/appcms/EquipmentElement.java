package com.viewlift.models.data.appcms;

import android.graphics.drawable.Drawable;

public class EquipmentElement {
    int image;
    String name;
    String equipment_needed;
    int brand;
    boolean isRequired;

    public int getBrand() {
        return brand;
    }

    public void setBrand(int brand) {
        this.brand = brand;
    }

    public String getEquipment_needed() {
        return equipment_needed;
    }

    public void setEquipment_needed(String equipment_needed) {
        this.equipment_needed = equipment_needed;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }
}
