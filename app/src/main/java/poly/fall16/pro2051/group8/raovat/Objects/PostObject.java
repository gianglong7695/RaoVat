package poly.fall16.pro2051.group8.raovat.objects;

/**
 * Created by giang on 9/23/2016.
 */

public class PostObject {
    private String title, price, time, image;

    public PostObject(String title, String price, String time, String image) {
        this.title = title;
        this.price = price;
        this.time = time;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
