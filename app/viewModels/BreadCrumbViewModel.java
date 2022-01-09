package viewModels;


import java.util.ArrayList;
import java.util.List;

public  class BreadCrumbViewModel {
    static public BreadCrumbViewModel build(){
        return new BreadCrumbViewModel();
    }
    private List<LinkViewModel> linkViewModels = new ArrayList<>();
    public BreadCrumbViewModel add(String label, String url){

        LinkViewModel linkViewModel = new LinkViewModel();
        linkViewModel.label = label;
        linkViewModel.url = url;
        linkViewModels.add(linkViewModel);
        return this;
    }

    public BreadCrumbViewModel add(String label) {
        LinkViewModel linkViewModel = new LinkViewModel();
        linkViewModel.label = label;
        linkViewModels.add(linkViewModel);
        return this;
    }

    public List<LinkViewModel> toList(){
        return linkViewModels;
    }
}
