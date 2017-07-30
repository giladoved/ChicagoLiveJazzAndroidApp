package giladoved.chicagolivejazz.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by giladoved on 7/29/17.
 */

public class Show implements Parcelable {
    private String headline;
    private String time;
    private String price;
    private String details;
    private String video;

    public Show() {
        this.headline = "";
        this.time = "";
        this.price = "";
        this.details = "";
        this.video = "";
    }

    public Show(String headline, String time, String price, String details, String video) {
        this.headline = headline;
        this.time = time;
        this.price = price;
        this.details = details;
        this.video = video;
    }

    protected Show(Parcel in) {
        headline = in.readString();
        time = in.readString();
        price = in.readString();
        details = in.readString();
        video = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(headline);
        dest.writeString(time);
        dest.writeString(price);
        dest.writeString(details);
        dest.writeString(video);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Show> CREATOR = new Creator<Show>() {
        @Override
        public Show createFromParcel(Parcel in) {
            return new Show(in);
        }

        @Override
        public Show[] newArray(int size) {
            return new Show[size];
        }
    };

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
