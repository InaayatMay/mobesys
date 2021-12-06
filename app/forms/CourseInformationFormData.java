package forms;

public class CourseInformationFormData {
    protected String programme;
    protected String courseCode;
    protected String courseName;
    protected String semester;
    protected String intakeBatch;
    protected String courseType;

    public String getProgramme() {
        return programme;
    }

    public void setProgramme(String programme) {
        this.programme = programme;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getIntakeBatch() {
        return intakeBatch;
    }

    public void setIntakeBatch(String intakeBatch) {
        this.intakeBatch = intakeBatch;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }
}
