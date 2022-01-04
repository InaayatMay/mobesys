package viewModels;

public class CourseActivationViewModel {
    public Long courseId;
    public String programme;
    public boolean isActive;

    public CourseActivationViewModel(Long courseId, String programme, boolean isActive) {
        this.courseId = courseId;
        this.programme = programme;
        this.isActive = isActive;
    }
}
