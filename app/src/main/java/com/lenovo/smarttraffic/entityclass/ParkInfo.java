package com.lenovo.smarttraffic.entityclass;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 停车场信息的实体类
 * @author asus
 *Parcelable
 */
public class ParkInfo implements Parcelable {
    String titleName;
    int EmptySpace;
    int rate;
    String address;
    int distance;
    int open;
    int AllSpace;
    int allRate;
    double longitude;
    double latitude;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getAllRate() {
        return allRate;
    }

    public void setAllRate(int allRate) {
        this.allRate = allRate;
    }

    public int getAllSpace() {
        return AllSpace;
    }

    public void setAllSpace(int allSpace) {
        AllSpace = allSpace;
    }

    public static final Creator<ParkInfo> CREATOR = new Creator<ParkInfo>() {
        @Override
        public ParkInfo createFromParcel(Parcel in) {
            ParkInfo info = new ParkInfo();
            info.titleName = in.readString();
            info.EmptySpace = in.readInt(          );
            info.rate = in.readInt();
            info.address = in.readString();
            info.distance = in.readInt();
            info.open = in.readInt();
            info.AllSpace = in.readInt();
            info.allRate = in.readInt();
            info.latitude = in.readDouble();
            info.longitude = in.readDouble();
            return info;
        }

        @Override
        public ParkInfo[] newArray(int size) {
            return new ParkInfo[size];
        }
    };

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public int getEmptySpace() {
        return EmptySpace;
    }

    public void setEmptySpace(int emptySpace) {
        EmptySpace = emptySpace;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(titleName);
        dest.writeInt(EmptySpace);
        dest.writeInt(rate);
        dest.writeString(address);
        dest.writeInt(distance);
        dest.writeInt(open);
        dest.writeInt(AllSpace);
        dest.writeInt(allRate);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }
}
