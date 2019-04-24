package com.sportskonnect.api.Callbacks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Citynamearray {
    @SerializedName("cityname")
    @Expose
    private String cityname;

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public Citynamearray(String cityname) {
        this.cityname = cityname;
    }
}
