import java.util.ArrayList;

public class ReportCard {

    private String studentName;
    private int yearOfStudy;
    private int periodOfStudy;
    private ArrayList<String> classes;
    private ArrayList<Integer> grades;

    public ReportCard(String studentName, int yearOfStudy, int periodOfStudy, ArrayList<String> classes, ArrayList<Integer> grades){
        this.studentName = studentName;
        this.yearOfStudy = yearOfStudy;
        this.periodOfStudy = periodOfStudy;
        this.classes = classes;
        this.grades = grades;
    }

    //Including getters for each private attribute to be able to use them for custom display
    //Including setters for each private attribute to be able to set/modify it, if needed
    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public int getPeriodOfStudy() {
        return periodOfStudy;
    }

    public void setPeriodOfStudy(int periodOfStudy) {
        this.periodOfStudy = periodOfStudy;
    }

    public ArrayList<String> getClasses() {
        return classes;
    }

    public void setClasses(ArrayList<String> classes) {
        this.classes = classes;
    }

    public ArrayList<Integer> getGrades() {
        return grades;
    }

    public void setGrades(ArrayList<Integer> grades) {
        this.grades = grades;
    }

    @Override
    public String toString() {
        return "Name: "+ studentName +
                "\nYear: " + yearOfStudy +
                "\nPeriod: " + periodOfStudy +
                "\nClass: " + classes +
                "\nGrade: " + grades;
    }
}
