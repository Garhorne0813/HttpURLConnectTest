package com.garhorne.jsonarraytest;

import android.os.Parcel;
import android.os.Parcelable;

public class Goods implements Parcelable{

    private String goodName;
    private double goodPrice;
    private String Detail;

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public double getGoodPrice() {
        return goodPrice;
    }

    public void setGoodPrice(double goodPrice) {
        this.goodPrice = goodPrice;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(goodName);
        parcel.writeString(Detail);
        parcel.writeDouble(goodPrice);
    }

    public static final Parcelable.Creator<Goods> CREATOR = new Parcelable.Creator<Goods>(){

        @Override
        public Goods createFromParcel(Parcel parcel) {
            Goods goods = new Goods();
            goods.goodName = parcel.readString();
            goods.Detail = parcel.readString();
            goods.goodPrice = parcel.readDouble();
            return goods;
        }

        @Override
        public Goods[] newArray(int i) {
            return new Goods[i];
        }
    };
}
