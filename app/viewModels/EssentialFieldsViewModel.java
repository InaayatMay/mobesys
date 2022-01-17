package viewModels;

public class EssentialFieldsViewModel {
    public Long userId;
    public String username;
    public String programme;
    public String courseName;
    public String image;

    public EssentialFieldsViewModel (Long userId, String username, String programme, String courseName, String image) {
        this.userId = userId;
        this.username = username;
        this.programme = programme;
        this.courseName = courseName;
        this.image = image;
    }
}
