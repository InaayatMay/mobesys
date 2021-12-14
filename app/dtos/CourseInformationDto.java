package dtos;

import models.CourseInformation;

public class CourseInformationDto {
    public Long id;
    public String name;
    public String code;

    public static CourseInformationDto to(CourseInformation courseInformation) {
        CourseInformationDto dto = new CourseInformationDto();
        dto.id = courseInformation.id;
        dto.code = courseInformation.courseCode;
        dto.name = courseInformation.courseName;
        return dto;
    }
}
