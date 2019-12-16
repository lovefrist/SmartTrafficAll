package com.lenovo.smarttraffic.entityclass;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author asus
 */
public class UserInfo implements Parcelable {
    private String username;
    private String pname;
    private String ptel;
    private String datetime;
    private String Admin;
    private int imgUri;
    private String state;
    private int stateTop;
    private String psex;

    public String getPsex() {return psex;}

    public void setPsex(String psex) { this.psex = psex;}

    public int getStateTop() {
        return stateTop;
    }

    public void setStateTop(int stateTop) {this.stateTop = stateTop;}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPtel() {
        return ptel;
    }

    public void setPtel(String ptel) {
        this.ptel = ptel;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getAdmin() {
        return Admin;
    }

    public void setAdmin(String admin) {
        Admin = admin;
    }

    public int getImgUri() {
        return imgUri;
    }

    public void setImgUri(int imgUri) {
        this.imgUri = imgUri;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            UserInfo userInfo = new UserInfo();
            userInfo.username = in.readString();
            userInfo.pname = in.readString();
            userInfo.ptel = in.readString();
            userInfo.datetime = in.readString();
            userInfo.Admin = in.readString();
            userInfo.imgUri = in.readInt();
            userInfo.state = in.readString();
            userInfo.stateTop = in.readInt();
            userInfo.psex = in.readString();
            return userInfo;
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(pname);
        dest.writeString(ptel);
        dest.writeString(datetime);
        dest.writeString(Admin);
        dest.writeInt(imgUri);
        dest.writeString(state);
        dest.writeInt(stateTop);
        dest.writeString(psex);
    }
}
