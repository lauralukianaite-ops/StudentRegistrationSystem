package org.example.studentregistrationsystem;

public class Student extends Person{
    private String group;
    private String attendanceRate;

    public Student(String name, String email, String group, String attendanceRate) {
        super(name,email);
        this.group = group;
        this.attendanceRate = attendanceRate;
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

    public String getAttendanceRate() {
        return attendanceRate;
    }
    public void setAttendanceRate(String attendanceRate){
        this.attendanceRate = attendanceRate;
    }
}
