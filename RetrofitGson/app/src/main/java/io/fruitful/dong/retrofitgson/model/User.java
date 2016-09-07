package io.fruitful.dong.retrofitgson.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ares on 8/22/2016.
 */
public class User implements Parcelable {
    private String name;
    private String url;
    private String description;


    //getter - setter


    public String getName() {
        return name;
    }

    public void setName(String username) {
        this.name = username;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // contructer
    public User(String name, String url, String description) {
        this.name = name;
        this.url = url;
        this.description = description;
    }

    // set up parcelable

    protected User(Parcel in) {
        name = in.readString();
        url = in.readString();
        description = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(url);
        parcel.writeString(description);
    }
}

