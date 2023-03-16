package carsharing.domain;

public class Car {
    private Integer id;
    private String name;

    public Car(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Car() {
        this(-1, "");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id + ". " + name;
    }
}
