package com.dyp.web.model;
import org.springframework.web.multipart.MultipartFile;

public class StudentForm {
    private String studentName;
    private MultipartFile studentPhoto;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public MultipartFile getStudentPhoto() {
        return studentPhoto;
    }

    public void setStudentPhoto(MultipartFile studentPhoto) {
        this.studentPhoto = studentPhoto;
    }
}
