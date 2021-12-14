package forms;

public class LecturerCourseMapFormData {

    protected Long courseInformationId;
    protected Long departmentId;
    protected Long schoolId;

    public Long getCourseInformationId() {
        return courseInformationId;
    }

    public void setCourseInformationId(Long courseInformationId) {
        this.courseInformationId = courseInformationId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }
}
