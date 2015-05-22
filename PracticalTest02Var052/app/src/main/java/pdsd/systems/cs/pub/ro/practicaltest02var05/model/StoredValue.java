package pdsd.systems.cs.pub.ro.practicaltest02var05.model;

/**
 * Created by mihai on 22-May-15.
 */
public class StoredValue {

    String value;
    DateTime date;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public StoredValue(String value, DateTime date) {
        this.value = value;
        this.date = date;
    }
}
