package forms;

public class AssessmentInfoFormData {
    protected String assessment;
    protected String assessmentType;
    protected int fullMarks;
    protected int weightage;
    protected String cloTitle;

    public String getAssessment() {
        return assessment;
    }

    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }

    public int getFullMarks() {
        return fullMarks;
    }

    public void setFullMarks(int fullMarks) {
        this.fullMarks = fullMarks;
    }

    public int getWeightage() {
        return weightage;
    }

    public void setWeightage(int weightage) {
        this.weightage = weightage;
    }

    public String getCloTitle() {
        return cloTitle;
    }

    public void setCloTitle(String cloTitle) {
        this.cloTitle = cloTitle;
    }
}
