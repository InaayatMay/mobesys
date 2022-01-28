package dtos;

public class StudentAnalysisDto {
    public Double failedPercentage;
    public Double passedPercentage;

    public StudentAnalysisDto(Double failedPercentage, Double passedPercentage) {
        this.failedPercentage = failedPercentage;
        this.passedPercentage = passedPercentage;
    }
}
