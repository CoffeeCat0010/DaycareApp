package edu.lawrence.daycareapp.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Parent implements Parcelable {
    private int id;
    private String name;
    private String phone;
    private String address;
    private String city;
    private String email;
    private int user;

    public Parent(){}


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getUser() { return user; }
    public void setUser(int user) { this.user = user; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(email);
        dest.writeInt(user);
    }

    public Parent(Parcel parcel){
        id = parcel.readInt();
        name = parcel.readString();
        phone = parcel.readString();
        address = parcel.readString();
        city = parcel.readString();
        email = parcel.readString();
        user = parcel.readInt();

    }

    public static final Parcelable.Creator<Parent> CREATOR = new Parcelable.Creator<Parent>() {

        @Override
        public Parent createFromParcel(Parcel source) {
            return new Parent(source);
        }

        @Override
        public Parent[] newArray(int size) {
            return new Parent[size];
        }
    };
}
