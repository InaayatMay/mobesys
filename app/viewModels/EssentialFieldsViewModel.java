package viewModels;

public class EssentialFieldsViewModel {
    public Long userId;
    public String username;
    public String programme;
    public String courseName;

    public EssentialFieldsViewModel (Long userId, String username, String programme, String courseName) {
        this.userId = userId;
        this.username = username;
        this.programme = programme;
        this.courseName = courseName;
    }
}
