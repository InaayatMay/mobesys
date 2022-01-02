package viewModels;

import forms.CloToPloMapFormData;
import models.ProgrammeLearningOutcome;
import play.data.Form;

import java.util.List;

public class SubjectViewModel {
    public CoursePlanViewModel coursePlanViewModel;
    public Form<CloToPloMapFormData> form;
    public List<ProgrammeLearningOutcome> unlinkedPloList;
    public int numberOfCloToPloMaps;

    public SubjectViewModel(CoursePlanViewModel coursePlanViewModel, Form<CloToPloMapFormData> form, List<ProgrammeLearningOutcome> unlinkedPloList, int numberOfCloToPloMaps) {
        this.coursePlanViewModel = coursePlanViewModel;
        this.form = form;
        this.unlinkedPloList = unlinkedPloList;
        this.numberOfCloToPloMaps = numberOfCloToPloMaps;
    }
}
