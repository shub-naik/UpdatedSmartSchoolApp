package com.pragyatitsolutions.SchoolManagement;

public class TeacherSectionClassSubject {

    String classes,section,subject;

    public TeacherSectionClassSubject(String classes, String section, String subject) {
        this.classes = classes;
        this.section = section;
        this.subject = subject;
    }

    public String getClasses() {
        return classes;
    }

    public String getSection() {
        return section;
    }

    public String getSubject() {
        return subject;
    }
}
