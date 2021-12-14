package dtos;

import models.Department;

public class DepartmentDto {
    public Long id;
    public String name;

    public static DepartmentDto to(Department department) {
        DepartmentDto dto = new DepartmentDto();
        dto.id = department.id;
        dto.name = department.departmentName;
        return dto;
    }
}
