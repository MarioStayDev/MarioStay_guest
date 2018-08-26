package com.mariostay.guest.mariostay;

import java.util.HashMap;
import java.util.Map;

public class Room {

    private int roomNo, floor, beds, rent;
    private Map<String, Boolean> imap;
    //private Photos pics;

    Room() {
        imap = new HashMap<>();
    }

    public int getRoomNo() { return roomNo; }
    public int getFloor() { return floor; }
    public int getBeds() { return beds; }
    public int getRent() { return rent; }
    public Map<String, Boolean> getAmenities() { return imap; }

    public void setRoomNo(int r) { this.roomNo = r; }
    public void setFloor(int r) { this.floor = r; }
    public void setBeds(int r) { this.beds = r; }
    public void setRent(int r) { this.rent = r; }
    public void setAmenities(Map<String, Boolean> m) { imap.putAll(m); }
}
