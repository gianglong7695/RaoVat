package poly.fall16.pro2051.group8.raovat.objects;

/**
 * Created by giang on 10/13/2016.
 */

public class CategoryObject {
    private int logo;
    private String name;

    public CategoryObject(int logo, String name) {
        this.logo = logo;
        this.name = name;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
