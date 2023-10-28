import java.util.List;

public class Thing {
    public String name;
    public int area;
    public static List<Thing> thingsToUtilization;

    public Thing(String name, int area) {
        this.name = name;
        this.area = area;
    }
    public Thing(String name, int height, int width, int length) {
        this.name = name;
        this.area = height * width * length;
    }

}
