package com.app.citycareservice.modals.AddressModal;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "AddressDB")
public class AddressModal {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "landmark")
    private String landmark;

    @ColumnInfo(name = "houseNo")
    private String houseNo;

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "place")
    private String place;

    @Ignore
    public AddressModal(String name, String landmark, String houseNo, String address, String place) {
        this.name = name;
        this.landmark = landmark;
        this.houseNo = houseNo;
        this.address = address;
        this.place = place;
    }

    public AddressModal(int id, String name, String landmark, String houseNo, String address, String place) {
        this.id = id;
        this.name = name;
        this.landmark = landmark;
        this.houseNo = houseNo;
        this.address = address;
        this.place = place;
    }

    @Ignore
    public AddressModal() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
