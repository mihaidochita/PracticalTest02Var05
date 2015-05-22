package pdsd.systems.cs.pub.ro.practicaltest02var05.model;

/**
 * Created by mihai on 22-May-15.
 */
public class DateTime {

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;

    public DateTime() {
        this.year = -1;
        this.month = -1;
        this.day = -1;
        this.hour = -1;
        this.minute = -1;
        this.second = -1;
    }

    public DateTime(
            int year,
            int month,
            int day,
            int hour,
            int minute,
            int second) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getMonth() {
        return month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getDay() {
        return day;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getHour() {
        return hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getMinute() {
        return minute;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getSecond() {
        return second;
    }

    public long toLong() {
        return (((((year * 12 + month) * 30 + day) * 24 + hour) * 60 + minute) * 60 + second);
    }

    public String toString() {
        return year+"-"+month+"-"+day+"T"+hour+":"+minute+":"+second;
    }
}
