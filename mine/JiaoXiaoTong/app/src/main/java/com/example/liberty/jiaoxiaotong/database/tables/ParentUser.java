package com.example.liberty.jiaoxiaotong.database.tables;

/**
 * Created by liberty on 2017/7/8.
 */

public class ParentUser {
    private String parentID;
    private String parentName;
    private String studentID;
    private String schoolID;

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentName() {
        return parentName;
    }

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
