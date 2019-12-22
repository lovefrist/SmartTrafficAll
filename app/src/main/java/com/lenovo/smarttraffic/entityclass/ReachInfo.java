package com.lenovo.smarttraffic.entityclass;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author asus
 */
public class ReachInfo implements Parcelable {

    String Number;
    String CarNumber;
    String Money;
    String User;
    String ReachTime;
    public static final Creator<ReachInfo> CREATOR = new Creator<ReachInfo>() {
        @Override
        public ReachInfo createFromParcel(Parcel in) {
            ReachInfo reachInfo = new ReachInfo();
            reachInfo.Number = in.readString();
            reachInfo.CarNumber = in.readString();
            reachInfo.Money = in.readString();
            reachInfo.User = in.readString();
            reachInfo.ReachTime = in.readString();
            return reachInfo;
        }

        @Override
        public ReachInfo[] newArray(int size) {
            return new ReachInfo[size];
        }
    };

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getCarNumber() {
        return CarNumber;
    }

    public void setCarNumber(String carNumber) {
        CarNumber = carNumber;
    }

    public String getMoney() {
        return Money;
    }

    public void setMoney(String money) {
        Money = money;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getReachTime() {
        return ReachTime;
    }

    public void setReachTime(String reachTime) {
        ReachTime = reachTime;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Number);
        dest.writeString(CarNumber);
        dest.writeString(Money);
        dest.writeString(User);
        dest.writeString(ReachTime);
    }
}
