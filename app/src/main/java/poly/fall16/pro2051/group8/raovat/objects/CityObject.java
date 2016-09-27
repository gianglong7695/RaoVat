package poly.fall16.pro2051.group8.raovat.objects;

/**
 * Created by giang on 9/26/2016.
 */

public class CityObject {
    private String id, name;

    public CityObject(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
