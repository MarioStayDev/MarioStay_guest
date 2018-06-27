package com.mariostay.guest.mariostay;

import java.util.HashMap;
import java.util.Map;
import com.google.firebase.firestore.GeoPoint;

class Property {
	private int PID, HID, Floors;
	private int /* float */ MinStayTime, SecurityMultiplier, NoticePeriod;
	private String Name, Type, Address, Landmark, ShortDescription, Rules;
	private GeoPoint Location;
	private Map<String, Boolean> Amenities;

	Property() {
		Amenities = new HashMap<>();
	}

	Property(int propertyID, String name) {
		PID = propertyID;
		Name = name;
		Amenities = new HashMap<>();
	}

	int getPID() { return PID; }
	int getHID() { return HID; }
	int getFloors() { return Floors; }
	int getMinStayTime() { return MinStayTime; }
	int getSecurityMultiplier() { return SecurityMultiplier; }
	int getNoticePeriod() { return NoticePeriod; }
	String getName() { return Name; }
	String getType() { return Type; }
	String getAddress() { return Address; }
	String getLandmark() { return Landmark; }
	String getShortDescription() { return ShortDescription; }
	String getRules() { return Rules; }
	GeoPoint getLocation() {return Location; }
	Map getAmenities() { return Amenities; }

	void setPID(int pID) { PID = pID; }
	void setHID(int hID) { HID = hID; }
	void setFloors(int floors) { Floors = floors; }
	void setMinStayTime(int minStayTime) { MinStayTime = minStayTime; }
	void setSecurityMultiplier(int securityMultiplier) { SecurityMultiplier = securityMultiplier; }
	void setNoticePeriod(int noticePeriod) { NoticePeriod = noticePeriod; }
	void setName(String name) { Name = name; }
	void setType(String type) { Type = type; }
	void setAddress(String address) { Address = address; }
	void setLandmark(String landmark) { Landmark = landmark; }
	void setShortDescription(String desc) { ShortDescription = desc; }
	void setRules(String rules) { Rules = rules; }
	void setLocation(GeoPoint location) {Location = location; }
	void setAmenities(Map<String, Boolean> amenities) { Amenities.clear();Amenities.putAll(amenities); }
}