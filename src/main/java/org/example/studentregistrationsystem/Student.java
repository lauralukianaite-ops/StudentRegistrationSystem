package org.example.studentregistrationsystem;

public class Student extends Person{
    private String group;
    private int attendedLectures = 0;
    private int totalLectures = 0;

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
}
