package com.example.liberty.jiaoxiaotong.database.tables;

/**
 * Created by liberty on 2017/7/10.
 */

public class Grade {
    private String studentID;
    private String schoolID;
    private String gradeNo; // 排名

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

    public void setGradeNo(String gradeNo) {
        this.gradeNo = gradeNo;
    }

    public String getGradeNo() {
        return gradeNo;
    }
}
