package com.example.liberty.jiaoxiaotong.util;

/**
 * Created by liberty on 2017/7/9.
 */


import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.io.*;

import jxl.*;
import jxl.read.biff.BiffException;

import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.liberty.jiaoxiaotong.database.tables.*;

public class ImportUtilities {
    private static final int GRADE_SHEET = 0;
    private static final int PARENTUSER_SHEET = 1;
    private static final int TEACHERUSER_SHEET = 2;
    private static final int SHEETSNUM = 3;
    private Workbook workbook = null;
    private Sheet[] sheets = new Sheet[SHEETSNUM];
    private int[] rows = new int[SHEETSNUM];

    public ImportUtilities(String xlsFile) {
        try {
            workbook = Workbook.getWorkbook(new File(xlsFile));
            for (int i = 0; i < SHEETSNUM; i++) {
                sheets[i] = workbook.getSheet(i);
                rows[i] = sheets[i].getRows();
            }
        } catch (IOException | BiffException e) {
            // TODO handle the exception
            workbook = null;
            sheets = null;
        }
    }

    public List<Grade> getGrades() {
        List<Grade> grades = new ArrayList<>();

        if (workbook != null && sheets != null) {
            // studentID, schoolID, gradeNo 位于0, 1, 2列
            for (int rowNo = 1; rowNo < rows[GRADE_SHEET]; rowNo++) {
                String studentID = sheets[GRADE_SHEET].getCell(0, rowNo).getContents();
                String schoolID = sheets[GRADE_SHEET].getCell(1, rowNo).getContents();
                String gradeNo = sheets[GRADE_SHEET].getCell(2, rowNo).getContents();

                Grade grade = new Grade();
                grade.setSchoolID(schoolID);
                grade.setStudentID(studentID);
                grade.setGradeNo(gradeNo);
                grades.add(grade);
            }
        }

        return grades;
    }

    public List<ParentUser> getParentUsers() {
        List<ParentUser> parentUsers = new ArrayList<>();

        if (workbook != null && sheets != null) {
            // parentID, parentName, studentID, schoolID 位于 0, 1, 2, 3 列
            for (int rowNo = 1; rowNo < rows[PARENTUSER_SHEET]; rowNo++) {
                String parentID = sheets[PARENTUSER_SHEET].getCell(0, rowNo).getContents();
                String parentName = sheets[PARENTUSER_SHEET].getCell(1, rowNo).getContents();
                String studentID = sheets[PARENTUSER_SHEET].getCell(2, rowNo).getContents();
                String schoolID = sheets[PARENTUSER_SHEET].getCell(3, rowNo).getContents();

                ParentUser parentUser = new ParentUser();
                parentUser.setParentID(parentID);
                parentUser.setParentName(parentName);
                parentUser.setStudentID(studentID);
                parentUser.setSchoolID(schoolID);
                parentUsers.add(parentUser);
            }
        }

        return parentUsers;
    }

    public List<TeacherUser> getTeacherUsers() {
        List<TeacherUser> teacherUsers = new ArrayList<>();

        if (workbook != null && sheets != null) {
            for (int rowNo = 1; rowNo < rows[TEACHERUSER_SHEET]; rowNo++) {
                // teacherID, teacherName, classID, schoolID 位于 0, 1, 2, 3 列
                String teacherID = sheets[TEACHERUSER_SHEET].getCell(0, rowNo).getContents();
                String teacherName = sheets[TEACHERUSER_SHEET].getCell(1, rowNo).getContents();
                String classID = sheets[TEACHERUSER_SHEET].getCell(2, rowNo).getContents();
                String schoolID = sheets[TEACHERUSER_SHEET].getCell(3, rowNo).getContents();

                TeacherUser teacherUser = new TeacherUser();
                teacherUser.setTeacherID(teacherID);
                teacherUser.setTeacherName(teacherName);
                teacherUser.setClassID(classID);
                teacherUser.setSchoolID(schoolID);
                teacherUsers.add(teacherUser);
            }
        }

        return teacherUsers;
    }

    public List<Student> getStudents() {
        List<Student> students = new ArrayList<>();

        if (workbook != null && sheets != null) {
            for (int rowNo = 1; rowNo < rows[GRADE_SHEET]; rowNo++) {
                // Student 在 excel 中没有单独的 sheet, 其数据来自 Grade;
                // 在 Grade 中, studentID, schoolID 位于 0, 1 列
                String studentID = sheets[GRADE_SHEET].getCell(0, rowNo).getContents();
                String schoolID = sheets[GRADE_SHEET].getCell(1, rowNo).getContents();

                Student student = new Student();
                student.setStudentID(studentID);
                student.setSchoolID(schoolID);
                students.add(student);
            }
        }

        return students;
    }

    public void uploadGradeInfo(final List<Grade> grades) {
        // 将现有数据全部删除后再添加新的记录

        AVQuery.doCloudQueryInBackground("select * from Grade", new CloudQueryCallback<AVCloudQueryResult>() {
            @Override
            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                List<? extends AVObject> list = avCloudQueryResult.getResults();
                for (AVObject avObject : list) {
                    try {
                        avObject.delete(); // 对Grade表的所有记录执行delete()
                    } catch (AVException ave) {
                        // TODO handle the exception
                    }
                }

                // 批量删除
                AVObject.deleteAllInBackground(list, new DeleteCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            // 成功清空原有记录后再添加记录
                            List<AVObject> todos = new ArrayList<>();

                            for (Grade grade : grades) {
                                AVObject avGrade = new AVObject("Grade");
                                avGrade.put("studentID", grade.getStudentID());
                                avGrade.put("schoolID", grade.getSchoolID());
                                avGrade.put("gradeNo", grade.getGradeNo());
                                todos.add(avGrade);
                            }
                            // 批量保存
                            AVObject.saveAllInBackground(todos, new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    if (e == null) {
                                        // 保存成功
                                    } else {
                                        // 保存失败
                                    }
                                }
                            });
                        } else {
                            // 删除现有记录失败
                        }
                    }
                });
            }
        });
    }

    public void uploadParentUser(final List<ParentUser> parentUsers) {
        // 将现有数据全部删除后再添加新的记录

        AVQuery.doCloudQueryInBackground("select * from ParentUser", new CloudQueryCallback<AVCloudQueryResult>() {
            @Override
            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                List<? extends AVObject> list = avCloudQueryResult.getResults();
                for (AVObject avObject : list) {
                    try {
                        avObject.delete(); // 对Grade表的所有记录执行delete()
                    } catch (AVException ave) {
                        // TODO handle the exception
                    }
                }

                // 批量删除
                AVObject.deleteAllInBackground(list, new DeleteCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            // 成功清空原有记录后再添加记录
                            List<AVObject> todos = new ArrayList<>();

                            for (ParentUser parentUser : parentUsers) {
                                AVObject avParentUser = new AVObject("ParentUser");
                                avParentUser.put("parentID", parentUser.getParentID());
                                avParentUser.put("parentName", parentUser.getParentName());
                                avParentUser.put("studentID", parentUser.getStudentID());
                                avParentUser.put("schoolID", parentUser.getSchoolID());
                                todos.add(avParentUser);
                            }
                            // 批量保存
                            AVObject.saveAllInBackground(todos, new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    if (e == null) {
                                        // 保存成功
                                    } else {
                                        // 保存失败
                                    }
                                }
                            });
                        } else {
                            // 删除现有记录失败
                        }
                    }
                });
            }
        });
    }

    public void uploadTeacherUserInfo(final List<TeacherUser> teacherUsers) {
        // 将现有数据全部删除后再添加新的记录

        AVQuery.doCloudQueryInBackground("select * from TeacherUser", new CloudQueryCallback<AVCloudQueryResult>() {
            @Override
            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                List<? extends AVObject> list = avCloudQueryResult.getResults();
                for (AVObject avObject : list) {
                    try {
                        avObject.delete(); // 对Grade表的所有记录执行delete()
                    } catch (AVException ave) {
                        // TODO handle the exception
                    }
                }

                // 批量删除
                AVObject.deleteAllInBackground(list, new DeleteCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            // 成功清空原有记录后再添加记录
                            List<AVObject> todos = new ArrayList<>();

                            for (TeacherUser teacherUser : teacherUsers) {
                                AVObject avTeacherUser = new AVObject("TeacherUser");
                                avTeacherUser.put("teacherID", teacherUser.getTeacherID());
                                avTeacherUser.put("teacherName", teacherUser.getTeacherName());
                                avTeacherUser.put("classID", teacherUser.getClassID());
                                avTeacherUser.put("schoolID", teacherUser.getSchoolID());
                                todos.add(avTeacherUser);
                            }
                            // 批量保存
                            AVObject.saveAllInBackground(todos, new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    if (e == null) {
                                        // 保存成功
                                    } else {
                                        // 保存失败
                                    }
                                }
                            });
                        } else {
                            // 删除现有记录失败
                        }
                    }
                });
            }
        });
    }

    public void uploadStudentInfo(final List<Student> students) {
        // 将现有数据全部删除后再添加新的记录

        AVQuery.doCloudQueryInBackground("select * from Student", new CloudQueryCallback<AVCloudQueryResult>() {
            @Override
            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                List<? extends AVObject> list = avCloudQueryResult.getResults();
                for (AVObject avObject : list) {
                    try {
                        avObject.delete(); // 对Grade表的所有记录执行delete()
                    } catch (AVException ave) {
                        // TODO handle the exception
                    }
                }

                // 批量删除
                AVObject.deleteAllInBackground(list, new DeleteCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            // 成功清空原有记录后再添加记录
                            List<AVObject> todos = new ArrayList<>();

                            for (Student student : students) {
                                AVObject avStudent = new AVObject("Student");
                                avStudent.put("studentID", student.getStudentID());
                                avStudent.put("schoolID", student.getSchoolID());
                                todos.add(avStudent);
                            }
                            // 批量保存
                            AVObject.saveAllInBackground(todos, new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    if (e == null) {
                                        // 保存成功
                                    } else {
                                        // 保存失败
                                    }
                                }
                            });
                        } else {
                            // 删除现有记录失败
                        }
                    }
                });
            }
        });
    }
}
