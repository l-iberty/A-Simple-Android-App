package com.example.liberty.jiaoxiaotong.database.tables;

/**
 * Created by liberty on 2017/7/8.
 */

public class TeacherUser {
    private String teacherID;
    private String teacherName;
    private String schoolID;
    private String classID;

    public void setTeacherID(String teacherID) {
        this.teacherID = teacherID;
    }

    public String getTeacherID() {
        return teacherID;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setSchoolID(String schoolID) {
        this.schoolID = schoolID;
    }

    public String getSchoolID() {
        return schoolID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public String getClassID() {
        return classID;
    }
}
