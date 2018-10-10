package com.mariostay.guest.mariostay;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room implements Parcelable {
    private String roomId;
    private int roomNo, floor, beds, rent;
    private Map<String, Boolean> imap;
    //private Photos pics;
    private List<Integer> bedStats;
    private List<String> bedStatsNew;

    Room() {
        imap = new HashMap<>();
        bedStats = new ArrayList<>();
        bedStatsNew = new ArrayList<>();
    }

    private Room(Parcel p) {
        this.roomId = p.readString();
        this.roomNo = p.readInt();
        this.floor = p.readInt();
        this.beds = p.readInt();
        this.rent = p.readInt();
        imap = new HashMap<>();
        p.readMap(this.imap, Map.class.getClassLoader());
        bedStats = new ArrayList<>();
        p.readList(this.getBedStats(), List.class.getClassLoader());
        bedStatsNew = new ArrayList<>();
        p.readList(this.getBedStatsNew(), Map.class.getClassLoader());
    }

    public String getRoomId() { return roomId; }
    public int getRoomNo() { return roomNo; }
    public int getFloor() { return floor; }
    public int getBeds() { return beds; }
    public int getRent() { return rent; }
    public Map<String, Boolean> getAmenities() { return imap; }
    public List<Integer> getBedStats() { return bedStats; }
    public List<String> getBedStatsNew() { return bedStatsNew; }

    public void setRoomId(String id) { roomId = id; }
    public void setRoomNo(int r) { this.roomNo = r; }
    public void setFloor(int r) { this.floor = r; }
    public void setBeds(int r) { this.beds = r; }
    public void setRent(int r) { this.rent = r; }
    public void setAmenities(Map<String, Boolean> m) { imap.putAll(m); }
    public void setBedStats(List<Integer> beds_stats) { this.bedStats = beds_stats; }
    public void setBedStatsNew(List<String> bedStatsNew) { this.bedStatsNew = bedStatsNew; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.roomId);
        dest.writeInt(this.roomNo);
        dest.writeInt(this.floor);
        dest.writeInt(this.beds);
        dest.writeInt(this.rent);
        dest.writeMap(this.imap);
        dest.writeList(this.bedStats);
        dest.writeList(this.bedStatsNew);
    }

    public static final Parcelable.Creator<Room> CREATOR = new Parcelable.Creator<Room>() {

        @Override
        public Room createFromParcel(Parcel source) {
            return new Room(source);
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[size];
        }
    };
}
