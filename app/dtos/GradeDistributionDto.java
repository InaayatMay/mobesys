package dtos;

import viewModels.GradeViewModel;

import java.util.List;
import java.util.stream.Collectors;

public class GradeDistributionDto {
    public String grade;
    public int numberOfStudent;

    public static List<GradeDistributionDto> to(GradeViewModel gradeViewModel) {
        List<GradeViewModel.AnalysisOfResults> analysisOfResults = gradeViewModel.analysisOfResultsList;

        return analysisOfResults.stream().map(analysis -> {
            GradeDistributionDto dto = new GradeDistributionDto();
            dto.grade = analysis.grade;
            dto.numberOfStudent = analysis.frequency;
            return dto;
        }).collect(Collectors.toList());
    }
}
