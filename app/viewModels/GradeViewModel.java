package viewModels;

import models.CourseInformation;
import models.StatisticsReport;
import models.Student;
import models.StudentStatisticsReport;
import org.slf4j.LoggerFactory;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class GradeViewModel {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger("application");

    public static DecimalFormat df = new DecimalFormat("0.00");

    public Long courseId;
    public String programme;
    public String courseCode;
    public String courseName;
    public String intakeBatch;
    public String semester;
    public int numberOfStudent;
    public List<Statistics> statistics;
    public Statistics finalExamStatistics;
    public Statistics courseworkStatistics;
    public Statistics grandTotalStatistics;
    public List<StudentStatistics> studentStatisticsList;
    public List<AnalysisOfResults> analysisOfResultsList;
    public Double failPercentage;
    public Double passPercentage;


    public static GradeViewModel build(CourseInformation courseInformation, List<Student> studentList,
                                       List<StatisticsReport> statisticsReports, List<StudentStatisticsReport> studentStatisticsReports) {

        GradeViewModel viewModel = new GradeViewModel();
        viewModel.courseId = courseInformation.id;
        viewModel.programme = courseInformation.programme;
        viewModel.courseCode = courseInformation.courseCode;
        viewModel.courseName = courseInformation.courseName;
        viewModel.intakeBatch = courseInformation.intakeBatch;
        viewModel.semester = courseInformation.semester;
        viewModel.numberOfStudent = studentList.size();

        List<Statistics> statisticsList = new ArrayList<>();

        List<String> assessmentTypes = Statistics.getAssessmentType(courseInformation.courseType);
        Double courseworkTotalMarks = 0.0;
        Double courseworkTotalWeightage = 0.0;
        for(String assessmentType: assessmentTypes) {
            boolean foundAssessmentType = false;
            for(StatisticsReport report: statisticsReports) {
                if(report.assessmentType.equals(assessmentType)) {
                    foundAssessmentType = true;
                    Statistics stat = new Statistics();
                    stat.assessmentType = assessmentType;
                    stat.totalMarks = report.totalStatisticsMarks;
                    stat.weightage = report.totalWeightage / studentList.size();
                    stat.averageMarks = Double.parseDouble(df.format(report.totalStatisticsMarks/studentList.size()));
                    statisticsList.add(stat);

                    courseworkTotalMarks += report.totalStatisticsMarks;
                    courseworkTotalWeightage += report.totalWeightage;

                }
                else if(report.assessmentType.equals("Final Exam")) {
                    Statistics stat = new Statistics();
                    stat.assessmentType = report.assessmentType;
                    stat.totalMarks = report.totalStatisticsMarks;
                    stat.weightage = report.totalWeightage / studentList.size();
                    stat.averageMarks = Double.parseDouble(df.format(report.totalStatisticsMarks/studentList.size()));
                    viewModel.finalExamStatistics = stat;
                }
            }

            if(!foundAssessmentType) {
                Statistics stat = new Statistics();
                stat.assessmentType = assessmentType;
                stat.totalMarks = 0.0;
                stat.weightage = 0.0;
                stat.averageMarks = 0.0;
                statisticsList.add(stat);
            }
        }

        Statistics coursework = new Statistics();
        coursework.assessmentType = "Course Work";
        coursework.totalMarks = courseworkTotalMarks;
        coursework.weightage = courseworkTotalWeightage / studentList.size();
        coursework.averageMarks = Double.parseDouble(df.format(courseworkTotalMarks/studentList.size()));

        Statistics grandTotal = new Statistics();
        grandTotal.assessmentType = "Grand Total";
        grandTotal.totalMarks = Double.parseDouble(df.format(viewModel.finalExamStatistics.totalMarks + courseworkTotalMarks));
        grandTotal.weightage = Double.parseDouble(df.format(viewModel.finalExamStatistics.weightage + coursework.weightage));
        grandTotal.averageMarks = Double.parseDouble(df.format(viewModel.finalExamStatistics.averageMarks + coursework.averageMarks));

        viewModel.courseworkStatistics = coursework;
        viewModel.statistics = statisticsList;
        viewModel.grandTotalStatistics = grandTotal;

        List<StudentStatistics> studentStatistics = new ArrayList<>();
        List<Long> studentIds = new ArrayList<>();
        int serialNo = 1;

        for(int i=0; i<studentStatisticsReports.size(); i++) {
            if(studentIds.contains(studentStatisticsReports.get(i).studentId)) {
                continue;
            }
            else {
                StudentStatistics statistics = new StudentStatistics();
                statistics.serialNo = serialNo++;
                statistics.studentId = studentStatisticsReports.get(i).studentId;
                statistics.studentCodeNumber = studentStatisticsReports.get(i).codeNumber;
                statistics.studentName = studentStatisticsReports.get(i).studentName;
                statistics.numberOfAttempt = studentStatisticsReports.get(i).numberOfAttempt;

                List<String> assessmentTypeList = Statistics.getAssessmentType(courseInformation.courseType);
                List<Double> marksList = new ArrayList<>();

                Double courseworkTotal = 0.0;
                List<String> includedAssessment = new ArrayList<>();

                forLoop: for(String type: assessmentTypeList) {
                    int j=0;
                    boolean foundAssessmentType = false;
                    while (j<studentStatisticsReports.size()) {
                        if(studentStatisticsReports.get(j).studentId == studentStatisticsReports.get(i).studentId) {
                            if(studentStatisticsReports.get(j).assessmentType.equals("Final Exam")) {
                                statistics.finalExamTotal = studentStatisticsReports.get(j).totalStatisticsMarks;
                            }
                            else if(!includedAssessment.contains(type) && studentStatisticsReports.get(j).assessmentType.equals(type)) {
                                includedAssessment.add(type);
                                logger.debug("Assessment type : " + type + " _ " + "Marks : " + studentStatisticsReports.get(j).totalStatisticsMarks);
                                marksList.add(studentStatisticsReports.get(j).totalStatisticsMarks);
                                courseworkTotal += studentStatisticsReports.get(j).totalStatisticsMarks;
                                foundAssessmentType = true;
                                continue forLoop;
                            }
                        }
                        ++j;
                    }
                    if(!foundAssessmentType) {
                        marksList.add(0.0);
                    }
                }
                statistics.courseworkTotal = Double.parseDouble(df.format(courseworkTotal));

                statistics.totalAssessmentMarks = marksList;
                statistics.grandTotal = Double.parseDouble(df.format(statistics.finalExamTotal + statistics.courseworkTotal));
                statistics.grade = StudentStatistics.getGrade(statistics.grandTotal);
                statistics.point = StudentStatistics.getPoint(statistics.grade);
                statistics.status = statistics.grade.equals("F") ? "Repeat" : "Pass";

                studentStatistics.add(statistics);
                studentIds.add(studentStatisticsReports.get(i).studentId);
            }
        }

        viewModel.studentStatisticsList = studentStatistics;
        viewModel.analysisOfResultsList = AnalysisOfResults.generateAnalysis(studentStatistics);

        Double failPercentage = 0.0;
        Double passPercentage = 0.0;
        for(AnalysisOfResults results: viewModel.analysisOfResultsList) {
            if(results.grade.equals("F")) {
                failPercentage += results.percentage;
            }
            else {
                passPercentage += results.percentage;
            }
        }

        DecimalFormat dfHalfUp = new DecimalFormat("###");
        dfHalfUp.setRoundingMode(RoundingMode.HALF_UP);

        viewModel.failPercentage = Double.parseDouble(dfHalfUp.format(failPercentage));
        viewModel.passPercentage = Double.parseDouble(dfHalfUp.format(passPercentage));

        return viewModel;
    }

    public static class Statistics{
        public String assessmentType;
        public Double weightage;
        public Double totalMarks;
        public Double averageMarks;

        public static List<String> getAssessmentType(String courseType) {
            List<String> assessmentTypes = Arrays.asList("Assignment", "Quiz", "Test-1", "Test-2", "Mini project");
            if(courseType.equals("Theory with Lab")) {
                assessmentTypes.add("Lab Coursework");
                assessmentTypes.add("Lab Test/Exam");
            }
            return assessmentTypes;
        }
    }

    public static class StudentStatistics{
        public int serialNo;
        public Long studentId;
        public String studentCodeNumber;
        public String studentName;
        public Integer numberOfAttempt;
        public List<Double> totalAssessmentMarks;
        public Double finalExamTotal;
        public Double courseworkTotal;
        public Double grandTotal;
        public String grade;
        public Double point;
        public String status;

        public static String getGrade(Double grandTotal) {
            if(grandTotal >= 90 && grandTotal <= 100 ) return "A+";
            else if(grandTotal >= 80 && grandTotal <= 89 ) return "A";
            else if(grandTotal >= 75 && grandTotal <= 79 ) return "A-";
            else if(grandTotal >= 70 && grandTotal <= 74 ) return "B+";
            else if(grandTotal >= 65 && grandTotal <= 69 ) return "B";
            else if(grandTotal >= 60 && grandTotal <= 64 ) return "B-";
            else if(grandTotal >= 55 && grandTotal <= 59 ) return "C+";
            else if(grandTotal >= 50 && grandTotal <= 54 ) return "C";
            else if(grandTotal >= 40 && grandTotal <= 49 ) return "D";
            else return "F";
        }

        public static Double getPoint(String grade) {
            switch (grade) {
                case "A+":
                case "A":
                    return 4.00;
                case "A-": return 3.67;
                case "B+": return 3.33;
                case "B": return 3.00;
                case "B-": return 2.67;
                case "C+": return 2.33;
                case "C": return 2.00;
                case "D": return 1.67;
                default: return 0.00;
            }
        }
    }

    public static class AnalysisOfResults{
        public String marks;
        public String grade;
        public int frequency;
        public Double percentage;

        public AnalysisOfResults(String marks, String grade, int frequency, Double percentage) {
            this.marks = marks;
            this.grade = grade;
            this.frequency = frequency;
            this.percentage = percentage;
        }

        static List<AnalysisOfResults> generateAnalysis(List<StudentStatistics> studentStatistics) {
            Map<String, AnalysisOfResults> analysisOfResultsMap = new LinkedHashMap<>();
            analysisOfResultsMap.put("A+", new AnalysisOfResults("90 - 100", "A+", 0, 0.0));
            analysisOfResultsMap.put("A", new AnalysisOfResults("80 - 89", "A", 0, 0.0));
            analysisOfResultsMap.put("A-", new AnalysisOfResults("75 - 79", "A-", 0, 0.0));
            analysisOfResultsMap.put("B+", new AnalysisOfResults("70 - 74", "B+", 0, 0.0));
            analysisOfResultsMap.put("B", new AnalysisOfResults("65 - 69", "B", 0, 0.0));
            analysisOfResultsMap.put("B-", new AnalysisOfResults("60 - 64", "B-", 0, 0.0));
            analysisOfResultsMap.put("C+", new AnalysisOfResults("55 - 59", "C+", 0, 0.0));
            analysisOfResultsMap.put("C", new AnalysisOfResults("50 - 54", "C", 0, 0.0));
            analysisOfResultsMap.put("D", new AnalysisOfResults("40 - 49", "D", 0, 0.0));
            analysisOfResultsMap.put("F", new AnalysisOfResults("0 - 39", "F", 0, 0.0));

            for(StudentStatistics statistics: studentStatistics) {
                AnalysisOfResults results = analysisOfResultsMap.get(StudentStatistics.getGrade(statistics.grandTotal));
                logger.debug("Grand : " + results.grade);
                results.frequency = results.frequency + 1;
                Double numberOfStudent = studentStatistics.size() * 1.0;
                logger.debug("Percentage : " + results.frequency/numberOfStudent);
                results.percentage = Double.parseDouble(df.format((results.frequency/numberOfStudent) * 100));
                analysisOfResultsMap.replace(statistics.grade, results);
            }

            return new ArrayList<>(analysisOfResultsMap.values());
        }
    }
}
