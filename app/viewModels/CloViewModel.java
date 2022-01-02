package viewModels;

import models.CloAttainmentReport;
import models.CloWithTotalWeightage;
import models.PreviousCloRecord;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.*;

public class CloViewModel {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger("application");

    public static DecimalFormat df = new DecimalFormat("0.00");

    public List<CloMaxMark> cloMaxMarkList;
    public List<CloAttainment> cloAttainmentList;
    public List<CloAnalysis> cloAnalysisList;

    public List<CloMaxMark> ploMaxMarkList;
    public List<CloAttainment> ploAttainmentList;
    public List<CloAnalysis> ploAnalysisList;

    public static CloViewModel build(List<CloWithTotalWeightage> cloWithTotalWeightages,
                                     List<CloAttainmentReport> cloAttainmentReports, int totalStudents,
                                     List<PreviousCloRecord> previousCloRecordList) {

        List<String> cloCodes = new ArrayList<>(Arrays.asList("CLO1", "CLO2", "CLO3", "CLO4", "CLO5", "CLO6", "CLO7", "CLO8"));
        List<String> ploCodes = new ArrayList<>(Arrays.asList("PLO1", "PLO2", "PLO3", "PLO4", "PLO5", "PLO6", "PLO7",
                "PLO8", "PLO9", "PLO10", "PLO11", "PLO12"));

        List<CloMaxMark> cloMaxMarks = new ArrayList<>();
        List<CloMaxMark> ploMaxMarksUnordered = new ArrayList<>();

        main: for(String code: cloCodes) {
            boolean foundCode = false;
            for(CloWithTotalWeightage cloWithTotalWeightage: cloWithTotalWeightages) {
                if(code.equals(cloWithTotalWeightage.cloCode)) {
                    CloMaxMark cloMaxMark = new CloMaxMark(cloWithTotalWeightage.cloCode, cloWithTotalWeightage.totalWeightage);
                    CloMaxMark ploMaxMark = new CloMaxMark(cloWithTotalWeightage.ploCode, cloWithTotalWeightage.totalWeightage);
                    cloMaxMarks.add(cloMaxMark);
                    ploMaxMarksUnordered.add(ploMaxMark);
                    foundCode = true;
                    continue main;
                }
            }
            if(!foundCode) {
                CloMaxMark cloMaxMark = new CloMaxMark(code, 0.0);
                cloMaxMarks.add(cloMaxMark);
            }
        }

        List<CloMaxMark> ploMaxMarks = new ArrayList<>();
        for(String ploCode: ploCodes) {
            boolean foundCode = false;
            for(CloMaxMark ploMaxMark: ploMaxMarksUnordered) {
                if(ploCode.equals(ploMaxMark.cloCode)) {
                    ploMaxMarks.add(ploMaxMark);
                    foundCode = true;
                }
            }

            if(!foundCode) {
                ploMaxMarks.add(new CloMaxMark(ploCode, 0.0));
            }
        }

        CloViewModel cloViewModel = new CloViewModel();

        List<CloAttainment> cloAttainmentList = new ArrayList<>();
        List<CloAttainment> ploAttainmentList = new ArrayList<>();

        List<Long> includedIdList = new ArrayList<>();

        Map<String, CloAnalysis> cloAnalysisMap = new LinkedHashMap<>();
        Map<String, CloAnalysis> ploAnalysisMap = new LinkedHashMap<>();

        for(int i=0; i<cloAttainmentReports.size(); i++) {

            if(!includedIdList.contains(cloAttainmentReports.get(i).studentId)) {
                CloAttainment cloAttainment = new CloAttainment();
                cloAttainment.serialNo = i+1;
                cloAttainment.studentId = cloAttainmentReports.get(i).studentId;
                cloAttainment.studentCode = cloAttainmentReports.get(i).codeNumber;
                cloAttainment.studentName = cloAttainmentReports.get(i).studentName;

                CloAttainment ploAttainment = new CloAttainment();
                ploAttainment.serialNo = i+1;
                ploAttainment.studentId = cloAttainmentReports.get(i).studentId;
                ploAttainment.studentCode = cloAttainmentReports.get(i).codeNumber;
                ploAttainment.studentName = cloAttainmentReports.get(i).studentName;

                List<CloMaxMark> cloAttainmentMarks = new ArrayList<>();
                List<CloMaxMark> cloAttainmentPercentage = new ArrayList<>();

                List<CloMaxMark> ploAttainmentMarks = new ArrayList<>();
                List<CloMaxMark> ploAttainmentPercentage = new ArrayList<>();

                main: for(String code: cloCodes) {
                    boolean foundCode = false;

                    for(CloAttainmentReport report: cloAttainmentReports) {
                        if(code.equals(report.cloCode) && report.studentId == cloAttainmentReports.get(i).studentId) {
                            cloAttainmentMarks.add(new CloMaxMark(report.cloCode, report.totalWeightage));
                            cloAttainmentPercentage.add(new CloMaxMark(report.cloCode, report.cloAttainment));
                            foundCode = true;
                            continue main;
                        }
                    }

                    if(!foundCode) {
                        cloAttainmentMarks.add(new CloMaxMark(code, null));
                        cloAttainmentPercentage.add(new CloMaxMark(code, null));
                    }
                }

                main: for(String code: ploCodes) {
                    boolean foundCode = false;


                    for(CloAttainmentReport report: cloAttainmentReports) {

                        if(code.equals(report.ploCode) && report.studentId == cloAttainmentReports.get(i).studentId) {
                            ploAttainmentMarks.add(new CloMaxMark(report.ploCode, report.totalWeightage));
                            ploAttainmentPercentage.add(new CloMaxMark(report.ploCode, report.cloAttainment));
                            foundCode = true;
                            continue main;
                        }
                    }

                    if(!foundCode) {
                        ploAttainmentMarks.add(new CloMaxMark(code, null));
                        ploAttainmentPercentage.add(new CloMaxMark(code, null));
                    }
                }

                for(String code: cloCodes) {
                    Double classAverage = 0.0;
                    int numberOfPassedStudents = 0;

                    for(CloAttainmentReport report: cloAttainmentReports) {
                        if(code.equals(report.cloCode)) {
                            classAverage += report.cloAttainment;
                            if(report.cloAttainment > 50) {
                                numberOfPassedStudents++;
                            }
                        }
                    }

                    CloAnalysis cloAnalysis = new CloAnalysis();

                    for(PreviousCloRecord previousCloRecord: previousCloRecordList) {
                        if(code.equals(previousCloRecord.cloCode)) {
                            cloAnalysis.previousSemClassAvg = previousCloRecord.previousSemesterClassAverage;
                            cloAnalysis.comments = previousCloRecord.comments;
                        }
                    }

                    cloAnalysis.classAverage = Double.parseDouble(df.format(classAverage/totalStudents));
                    cloAnalysis.cloCode = code;
                    Double percentage = (numberOfPassedStudents/(totalStudents*1.0))*100.0;
                    cloAnalysis.percentageOfPassedStudent = Double.parseDouble(df.format(percentage));

                    cloAnalysisMap.put(code, cloAnalysis);
                }

                for(String code: ploCodes) {
                    Double classAverage = 0.0;
                    int numberOfPassedStudents = 0;
                    for(CloAttainmentReport report: cloAttainmentReports) {
                        if(code.equals(report.ploCode)) {
                            classAverage += report.cloAttainment;
                            if(report.cloAttainment > 50) {
                                numberOfPassedStudents++;
                            }
                        }
                    }

                    CloAnalysis ploAnalysis = new CloAnalysis();

                    for(PreviousCloRecord previousCloRecord: previousCloRecordList) {
                        if(code.equals(previousCloRecord.cloCode)) {
                            ploAnalysis.previousSemClassAvg = previousCloRecord.previousSemesterClassAverage;
                            ploAnalysis.comments = previousCloRecord.comments;
                        }
                    }

                    ploAnalysis.classAverage = Double.parseDouble(df.format(classAverage/totalStudents));
                    ploAnalysis.cloCode = code;
                    Double percentage = (numberOfPassedStudents/(totalStudents*1.0))*100.0;
                    ploAnalysis.percentageOfPassedStudent = Double.parseDouble(df.format(percentage));

                    ploAnalysisMap.put(code, ploAnalysis);
                }

                cloAttainment.cloAttainmentMarks = cloAttainmentMarks;
                cloAttainment.cloAttainmentPercentage = cloAttainmentPercentage;

                ploAttainment.cloAttainmentMarks = ploAttainmentMarks;
                ploAttainment.cloAttainmentPercentage = ploAttainmentPercentage;

                cloAttainmentList.add(cloAttainment);
                ploAttainmentList.add(ploAttainment);
                includedIdList.add(cloAttainmentReports.get(i).studentId);
            }
        }

        cloViewModel.cloMaxMarkList = cloMaxMarks;
        cloViewModel.ploMaxMarkList = ploMaxMarks;

        cloViewModel.cloAttainmentList = cloAttainmentList;
        cloViewModel.ploAttainmentList = ploAttainmentList;

        cloViewModel.cloAnalysisList = new ArrayList<>(cloAnalysisMap.values());
        cloViewModel.ploAnalysisList = new ArrayList<>(ploAnalysisMap.values());

        return cloViewModel;
    }


    public static class CloMaxMark {
        public String cloCode;
        public Double marks;

        public CloMaxMark(String cloCode, Double marks) {
            this.cloCode = cloCode;
            this.marks = marks;
        }
    }

    public static class CloAttainment {
        public int serialNo;
        public Long studentId;
        public String studentCode;
        public String studentName;
        public List<CloMaxMark> cloAttainmentMarks;
        public List<CloMaxMark> cloAttainmentPercentage;
    }

    public static class CloAnalysis {
        public String cloCode;
        public Double classAverage;
        public Double percentageOfPassedStudent;
        public Double previousSemClassAvg;
        public String comments;
    }
}
