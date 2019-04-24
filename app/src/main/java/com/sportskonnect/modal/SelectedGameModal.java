package com.sportskonnect.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SelectedGameModal {

    @SerializedName("catid")
    @Expose
    public int catId;

    @SerializedName("levelid")
    @Expose
    public int levelId;

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public SelectedGameModal(int catId, int levelId) {
        this.catId = catId;
        this.levelId = levelId;
    }

    public SelectedGameModal() {
    }
}
