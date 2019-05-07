package com.svr.techno.Adapters.Models;

import java.util.ArrayList;

public class CategoryModel {

    String title;
    ArrayList<ItemModel> itemModels;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<ItemModel> getItemModels() {
        return itemModels;
    }

    public void setItemModels(ArrayList<ItemModel> itemModels) {
        this.itemModels = itemModels;
    }
}
