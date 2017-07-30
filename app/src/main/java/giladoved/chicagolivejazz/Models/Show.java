package giladoved.chicagolivejazz.Models;

/**
 * Created by giladoved on 7/29/17.
 */

public class Show {
    private String headline;
    private String time;
    private String price;
    private String details;
    private String video;

    public Show(String headline, String time, String price, String details, String video) {
        this.headline = headline;
        this.time = time;
        this.price = price;
        this.details = details;
        this.video = video;
    }

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
