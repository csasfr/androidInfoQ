package com.sport.infoquest.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Ionut on 11/03/2017.
 */

public class IdHint implements Serializable {
    private String id;
    private String hint;

    public IdHint(String id, String hint) {
        this.setId(id);
        this.setHint(hint);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(id);
//        dest.writeString(hint);
//    }
//
//    public static final Creator<IdHint> CREATOR = new Creator<IdHint>() {
//        @Override
//        public IdHint createFromParcel(Parcel in) {
//            return new IdHint(in);
//        }
//
//        @Override
//        public IdHint[] newArray(int size) {
//            return new IdHint[size];
//        }
//    };

//    protected IdHint(Parcel in) {
//        id = in.readString();
//        hint = in.readString();
//    }
}
