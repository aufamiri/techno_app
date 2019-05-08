package com.svr.techno.Adapters.Models;

import com.google.type.LatLng;

import java.io.Serializable;

public class orderModel implements Serializable {

    private String id_item,id_seller, id_buyer, date;
    private int confr_seller, confr_buyer;

    public orderModel(String id_buyer, String id_item, String id_seller, String date, int confr_buyer, int confr_seller, LatLng coordinat_buyer) {
        this.confr_buyer=confr_buyer;
        this.confr_seller=confr_seller;
        this.id_item=id_item;
        this.id_buyer=id_buyer;
        this.id_seller=id_seller;
        this.date = date;
    }

    public orderModel() {}

    public String getId_item() {return id_item;}
    public String getId_seller() {return id_seller;}
    public String getId_buyer() {return id_buyer;}
    public String getDate() {return date;}

    public int getConfr_seller() {return confr_seller;}
    public int getConfr_buyer() {return confr_buyer;}



}
