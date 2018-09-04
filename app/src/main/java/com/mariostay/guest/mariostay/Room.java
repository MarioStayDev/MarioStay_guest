package com.mariostay.guest.mariostay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room {

    private int roomNo, floor, beds, rent;
    private Map<String, Boolean> imap;
    //private Photos pics;
    private List<Integer> bedStats;

    Room() {
        imap = new HashMap<>();
        bedStats = new ArrayList<>();
    }

    public int getRoomNo() { return roomNo; }
    public int getFloor() { return floor; }
    public int getBeds() { return beds; }
    public int getRent() { return rent; }
    public Map<String, Boolean> getAmenities() { return imap; }
    public List<Integer> getBedStats() { return bedStats; }

    public void setRoomNo(int r) { this.roomNo = r; }
    public void setFloor(int r) { this.floor = r; }
    public void setBeds(int r) { this.beds = r; }
    public void setRent(int r) { this.rent = r; }
    public void setAmenities(Map<String, Boolean> m) { imap.putAll(m); }
    public void setBedStats(List<Integer> beds_stats) { this.bedStats = beds_stats; }
}
