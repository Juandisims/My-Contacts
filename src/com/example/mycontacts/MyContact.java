package com.example.mycontacts;

import android.os.Parcel;
import android.os.Parcelable;

public class MyContact implements Parcelable{
	
	public static final String CONTACT_KEY = "com.example.mycontacts.MyContact";
	
	private String name;
	private String surname;

	public MyContact(Parcel in) {
		name = in.readString();
		surname = in.readString();
	}

	public MyContact() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int arg1) {
		out.writeString(name);
		out.writeString(surname);

	}

	public static final Parcelable.Creator<MyContact> CREATOR
	= new Parcelable.Creator<MyContact>() {
		public MyContact createFromParcel(Parcel in) {
			return new MyContact(in);
		}

		public MyContact[] newArray(int size) {
			return new MyContact[size];
		}
	};

}
