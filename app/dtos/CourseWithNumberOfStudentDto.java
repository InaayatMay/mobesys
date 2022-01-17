package dtos;

import models.CourseWithNumberOfStudent;

public class CourseWithNumberOfStudentDto {

    public Long courseId;
    public String courseCode;
    public String courseName;
    public String programme;
    public int numberOfStudents;

    public static CourseWithNumberOfStudentDto to(CourseWithNumberOfStudent model) {
        CourseWithNumberOfStudentDto dto = new CourseWithNumberOfStudentDto();
        dto.courseId = model.courseId;
        dto.courseCode = model.courseCode;
        dto.courseName = model.courseName;
        dto.numberOfStudents = model.numberOfStudents;
        dto.programme = model.programme;
        return dto;
    }
}
