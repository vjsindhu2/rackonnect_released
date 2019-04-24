package com.sportskonnect.api.Callbacks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sportskonnect.modal.CategoryDatum;

import java.util.List;

public class CategoriesDataResponse {


    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("category_data")
    @Expose
    private List<CategoryDatum> categoryData = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<CategoryDatum> getCategoryData() {
        return categoryData;
    }

    public void setCategoryData(List<CategoryDatum> categoryData) {
        this.categoryData = categoryData;
    }
}
