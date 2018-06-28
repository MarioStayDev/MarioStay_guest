package com.mariostay.guest.mariostay;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;
import com.google.firebase.firestore.GeoPoint;

class Property implements Parcelable {
	private int PID, HID, Floors;
	private int /* float */ MinStayTime, SecurityMultiplier, NoticePeriod;
	private String Name, Type, Address, Landmark, ShortDescription, Rules;
	private GeoPoint Location;
	private Map<String, Boolean> Amenities;

	Property() {
		Amenities = new HashMap<>();
	}

	private Property(Parcel parcel) {
		this.PID = parcel.readInt();
		this.HID = parcel.readInt();
		this.Floors = parcel.readInt();

		this.MinStayTime = parcel.readInt();
		this.SecurityMultiplier = parcel.readInt();
		this.NoticePeriod = parcel.readInt();

		this.Name = parcel.readString();
		this.Type = parcel.readString();
		this.Address = parcel.readString();
		this.Landmark = parcel.readString();
		this.ShortDescription = parcel.readString();
		this.Rules = parcel.readString();

		parcel.readMap(this.Amenities, null);
	}

	/*Property(int propertyID, int hostID, int floors,
			int min, int secmul, int notper
			, String name, String type, String add, String land, String desc, String rules, GeoPoint loc, Map<String, Boolean> map) {
		PID = propertyID;
		HID = hostID;
		Floors = floors;
		MinStayTime = min;
		SecurityMultiplier = secmul;
		NoticePeriod = notper;
		Name = name;
		Type = type;
		Address = add;
		Landmark = land;
		ShortDescription = desc;
		Rules = rules;
		Location = loc;
		Amenities = new HashMap<>();
		Amenities.putAll(map);
	}*/

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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.PID);
		dest.writeInt(this.HID);
		dest.writeInt(this.Floors);

		dest.writeInt(this.MinStayTime);
		dest.writeInt(this.SecurityMultiplier);
		dest.writeInt(this.NoticePeriod);

		dest.writeString(this.Name);
		dest.writeString(this.Type);
		dest.writeString(this.Address);
		dest.writeString(this.Landmark);
		dest.writeString(this.ShortDescription);
		dest.writeString(this.Rules);

		dest.writeMap(Amenities);

	}

	public static final Parcelable.Creator<Property> CREATOR = new Parcelable.Creator<Property>() {

		@Override
		public Property createFromParcel(Parcel source) {
			return new Property(source);
		}

		@Override
		public Property[] newArray(int size) {
			return new Property[size];
		}
	};
}