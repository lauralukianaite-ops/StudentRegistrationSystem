package org.example.studentregistrationsystem;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Student extends Person{
    private String group;
    private int attendedLectures = 0;
    private int totalLectures = 0;
    private final BooleanProperty attendingNow = new SimpleBooleanProperty(false);
    private Map<LocalDate, Boolean> attendanceRecord = new HashMap<>();

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

    public void markAttendance(LocalDate date, boolean attended) {
        attendanceRecord.put(date, attended);
    }

    public boolean wasPresent(LocalDate date) {
        return attendanceRecord.getOrDefault(date, false);
    }

    public double calculateAttendancePercentage() {
        if (attendanceRecord.isEmpty()) return 0.0;
        long presentCount = attendanceRecord.values().stream().filter(v -> v).count();
        return (double) presentCount / attendanceRecord.size() * 100;
    }

    public Map<LocalDate, Boolean> getAttendanceRecord() {
        return attendanceRecord;
    }
}
