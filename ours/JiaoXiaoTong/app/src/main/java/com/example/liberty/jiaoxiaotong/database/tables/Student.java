package com.example.liberty.jiaoxiaotong.database.tables;

/**
 * Created by liberty on 2017/7/10.
 */

public class Student {
    private String studentID;
    private String schoolID;

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setSchoolID(String schoolID) {
        this.schoolID = schoolID;
    }

    public String getSchoolID() {
        return schoolID;
    }
}
