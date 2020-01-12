package com.pragyatitsolutions.SchoolManagement;

public class ShowHomeWorkModel {

    String TeacherName, TeacherSubjectName, Deadline, Remarks;

    public ShowHomeWorkModel(String teacherName, String teacherSubjectName, String deadline, String remarks) {
        TeacherName = teacherName;
        TeacherSubjectName = teacherSubjectName;
        Deadline = deadline;
        Remarks = remarks;
    }

    public String getTeacherName() {
        return TeacherName;
    }


    public String getTeacherSubjectName() {
        return TeacherSubjectName;
    }


    public String getDeadline() {
        return Deadline;
    }


    public String getRemarks() {
        return Remarks;
    }

}
