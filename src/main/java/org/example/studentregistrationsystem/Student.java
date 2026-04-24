package org.example.studentregistrationsystem;

public class Student {
    private String name;
    private String email;
    private String group;
    private String attendanceRate;

    public Student(String name, String email, String group, String attendanceRate) {
        this.name = name;
        this. email = email;
        this.group = group;
        this.attendanceRate = attendanceRate;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getGroup() {
        return group;
    }

    public String getAttendanceRate() {
        return attendanceRate;
    }
}
