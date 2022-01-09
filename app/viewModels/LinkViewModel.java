package viewModels;

import java.util.ArrayList;
import java.util.List;

public class LinkViewModel {
    public String label;
    public String url;

    public boolean hasUrl() {
        return url != null && !url.isEmpty();
    }

}
