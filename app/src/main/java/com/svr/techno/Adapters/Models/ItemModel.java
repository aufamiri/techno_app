package com.svr.techno.Adapters.Models;

public class ItemModel {


    private String category, name, itemId, sellerId, cover, description, uploadDate;
    private int price, itemSold;

    public ItemModel(String category, String name, String itemId, String sellerId, String cover, String description, String uploadDate, int price, int itemSold) {
        this.category = category;
        this.name = name;
        this.itemId = itemId;
        this.sellerId = sellerId;
        this.cover = cover;
        this.description = description;
        this.uploadDate = uploadDate;
        this.price = price;
        this.itemSold = itemSold;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getItemSold() {
        return itemSold;
    }

    public void setItemSold(int itemSold) {
        this.itemSold = itemSold;
    }

    public String getName() {
        return name;
    }

    public ItemModel() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public int getPrice() {
        return price;
    }

    public String getPriceText() {
        return "Rp. " + price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
