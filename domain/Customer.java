package carsharing.domain;

public class Customer {
    private Integer id;
    private String name;
    private Integer rentedCarId = null;

    public Customer(Integer id, String name, Integer rentedCarId) {
        this.id = id;
        this.name = name;
        this.rentedCarId = rentedCarId;
    }

    public Customer() {
        this(-1, "", null);
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return id + ". " + name + " rented " + rentedCarId;
    }
}
