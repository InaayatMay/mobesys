package viewModels;

import models.Student;
import models.StudentMarks;

import java.util.List;

public class StudentMarksViewModel {
    public Long studentId;
    public String studentCode;
    public String studentName;
    public List<StudentMarks> studentMarks;

    public static StudentMarksViewModel build(Student student, List<StudentMarks> studentMarks) {
        StudentMarksViewModel viewModel = new StudentMarksViewModel();
        viewModel.studentId = student.id;
        viewModel.studentCode = student.codeNumber;
        viewModel.studentName = student.firstName + " " + student.lastName;
        viewModel.studentMarks = studentMarks;
        return viewModel;
    }

    public static class AssessmentOrder{
        public int order;
        public String assessmentType;

        public AssessmentOrder(int order, String assessmentType) {
            this.order = order;
            this.assessmentType = assessmentType;
        }
    }
}
