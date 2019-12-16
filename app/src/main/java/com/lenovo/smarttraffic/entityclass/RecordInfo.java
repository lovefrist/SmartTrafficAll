package com.lenovo.smarttraffic.entityclass;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 违章查询的实体类用于获取违章的详情页面
 * @author asus
 */
public class RecordInfo implements Parcelable {
    private int number;
    private int imgUri;
    private  String carNumber;
    private String paddr;
    private String premarks;
    private int pscore;
    private int pmoney;
    private String pdate;
    private String time;



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(number);
        dest.writeInt(imgUri);
        dest.writeString(carNumber);
        dest.writeString(paddr);
        dest.writeString(premarks);
        dest.writeInt(pscore);
        dest.writeInt(pmoney);
        dest.writeString(pdate);
        dest.writeString(time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RecordInfo> CREATOR = new Creator<RecordInfo>() {
        @Override
        public RecordInfo createFromParcel(Parcel in) {
            RecordInfo recordInfo = new RecordInfo();

            recordInfo.number = in.readInt();
            recordInfo.imgUri = in.readInt();
            recordInfo.carNumber = in.readString();
            recordInfo.paddr = in.readString();
            recordInfo.premarks = in.readString();
            recordInfo.pscore = in.readInt();
            recordInfo.pmoney = in.readInt();
            recordInfo.pdate = in.readString();
            recordInfo.time = in.readString();
            return recordInfo;
        }

        @Override
        public RecordInfo[] newArray(int size) {

            return new RecordInfo[size];
        }
    };

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getImgUri() {
        return imgUri;
    }

    public void setImgUri(int imgUri) {
        this.imgUri = imgUri;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getPaddr() {
        return paddr;
    }

    public void setPaddr(String paddr) {
        this.paddr = paddr;
    }

    public String getPremarks() {
        return premarks;
    }

    public void setPremarks(String premarks) {
        this.premarks = premarks;
    }

    public int getPscore() {
        return pscore;
    }

    public void setPscore(int pscore) {
        this.pscore = pscore;
    }

    public int getPmoney() {
        return pmoney;
    }

    public void setPmoney(int pmoney) {
        this.pmoney = pmoney;
    }

    public String getPdate() {
        return pdate;
    }

    public void setPdate(String pdate) {
        this.pdate = pdate;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

}
