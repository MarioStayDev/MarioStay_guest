package com.mariostay.guest.mariostay;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;
//import com.google.firebase.firestore.GeoPoint;

class Property implements Parcelable {
	private String PID, HID;
	private int Floors, Rooms;
	private int /* float */ MinStayTime, SecurityMultiplier, NoticePeriod;
	private int Rent;
	private String Name, Type, Address, Landmark, ShortDescription, Rules, inTime, outTime;
	//private GeoPoint Location;
	private Map<String, Boolean> Amenities;

	Property() {
		Amenities = new HashMap<>();
	}

	private Property(Parcel parcel) {
		this.PID = parcel.readString();
		this.HID = parcel.readString();
		this.Floors = parcel.readInt();
		this.Rooms = parcel.readInt();

		this.MinStayTime = parcel.readInt();
		this.SecurityMultiplier = parcel.readInt();
		this.NoticePeriod = parcel.readInt();

		this.Rent = parcel.readInt();

		this.Name = parcel.readString();
		this.Type = parcel.readString();
		this.Address = parcel.readString();
		this.Landmark = parcel.readString();
		this.ShortDescription = parcel.readString();
		this.Rules = parcel.readString();
		this.inTime = parcel.readString();
		this.outTime = parcel.readString();
		Amenities = new HashMap<>();
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

	public String getPID() { return PID; }
	public String getHID() { return HID; }
	public int getFloors() { return Floors; }
	public int getRooms() { return Rooms; }
	public int getMinStayTime() { return MinStayTime; }
	public int getSecurityMultiplier() { return SecurityMultiplier; }
    public int getNoticePeriod() { return NoticePeriod; }
    public int getRent() { return Rent; }
	public String getName() { return Name; }
	public String getType() { return Type; }
	public String getAddress() { return Address; }
	public String getLandmark() { return Landmark; }
	public String getShortDescription() { return ShortDescription; }
	public String getRules() { return Rules; }
	public String getInTime() { return inTime; }
	public String getOutTime() { return outTime; }
	//GeoPoint getLocation() {return Location; }
	public Map<String, Boolean> getAmenities() { return Amenities; }

	public void setPID(String pID) { PID = pID; }
	public void setHID(String hID) { HID = hID; }
	public void setFloors(int floors) { Floors = floors; }
	public void setRooms(int rooms) { Rooms = rooms; }
	public void setMinStayTime(int minStayTime) { MinStayTime = minStayTime; }
	public void setSecurityMultiplier(int securityMultiplier) { SecurityMultiplier = securityMultiplier; }
    public void setNoticePeriod(int noticePeriod) { NoticePeriod = noticePeriod; }
    public void setRent(int rent) { Rent = rent; }
	public void setName(String name) { Name = name; }
	public void setType(String type) { Type = type; }
	public void setAddress(String address) { Address = address; }
	public void setLandmark(String landmark) { Landmark = landmark; }
	public void setShortDescription(String desc) { ShortDescription = desc; }
	public void setRules(String rules) { Rules = rules; }
	public void setInTime(String in) { inTime = in; }
	public void setOutTime(String in) { outTime = in; }
	//void setLocation(GeoPoint location) {Location = location; }
	public void setAmenities(Map<String, Boolean> amenities) { Amenities.clear();Amenities.putAll(amenities); }

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.PID);
		dest.writeString(this.HID);
		dest.writeInt(this.Floors);
		dest.writeInt(this.Rooms);

		dest.writeInt(this.MinStayTime);
		dest.writeInt(this.SecurityMultiplier);
		dest.writeInt(this.NoticePeriod);

        dest.writeInt(this.Rent);

		dest.writeString(this.Name);
		dest.writeString(this.Type);
		dest.writeString(this.Address);
		dest.writeString(this.Landmark);
		dest.writeString(this.ShortDescription);
		dest.writeString(this.Rules);
		dest.writeString(this.inTime);
		dest.writeString(this.outTime);

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