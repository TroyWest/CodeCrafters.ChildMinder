import java.sql.Date;

public class Child {
    private int id;
    private String name;
    private Date dob;

    public Child(String _name, int _id) {
        name = _name;
        id = _id;
    }

    public Child(String _name, Date _dob) {
        name = _name;
        dob = _dob;
    }

    public Child(int _id, String _name, Date _dob) {
        id = _id;
        name = _name;
        dob = _dob;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Date getDob() {
        return dob;
    }
}
