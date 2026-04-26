package org.example.studentregistrationsystem;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Student extends Person{
    private String group;
    private int attendedLectures = 0;
    private int totalLectures = 0;
    private final BooleanProperty attendingNow = new SimpleBooleanProperty(false);

    public Student(String name, String email, String group) {
        super(name,email);
        this.group = group;
    }

    @Override
    public String getRole(){
        return "Studentas";
    }

    public String getGroup() {
        return group;
    }
    public void setGroup(String group){
        this.group = group;
    }

    public String getAttendanceRate(){
        if (totalLectures == 0) return "0%";
        int percent = (int)(((double)attendedLectures/totalLectures)*100);
        return percent + "%";
    }

    public void addAttendance(boolean wasPresent){
        this.totalLectures++;
        if (wasPresent) this.attendedLectures++;
    }

    public BooleanProperty attendingNowProperty(){
        return attendingNow;
    }

    public boolean isAttendingNow() {
        return attendingNow.get();
    }

    public void setAttendingNow(boolean attendingNow){
        this.attendingNow.set(attendingNow);
    }

    @Override
    public String toString() {
        return getName();
    }
}
