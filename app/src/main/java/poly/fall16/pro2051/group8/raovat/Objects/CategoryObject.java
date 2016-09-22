package poly.fall16.pro2051.group8.raovat.Objects;

/**
 * Created by giang on 9/20/2016.
 */

public class CategoryObject {
    private String title;
    private int background;

    public CategoryObject(String title, int background) {
        this.title = title;
        this.background = background;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }
}
