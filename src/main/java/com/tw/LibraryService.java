package com.tw;

import java.util.*;
import java.util.regex.Pattern;

public class LibraryService {
    private List<Student> students;
    private Pattern numberFormat = Pattern.compile("^[0-9]+(.[0-9]{1,2})?$");

    public LibraryService() {
        students = new ArrayList<>();
    }

    public boolean addStudent(String wholeInfoStr) {
        String[] infoStrs = trim(wholeInfoStr.split(","));
        if (infoStrs.length < 3 || infoStrs[0].split(":").length > 1 || infoStrs[1].split(":").length > 1) {
            System.out.println("A");
            return false;
        }

        Student student = new Student(infoStrs[0], infoStrs[1]);

        for (int i = 2; i < infoStrs.length; i++) {
            String[] parts = infoStrs[i].split(":");
            if (parts.length != 2) {
                System.out.println("B");
                return false;
            }

            if (numberFormat.matcher(parts[1]).matches()) {
                student.addGrade(parts[0], Float.parseFloat(parts[1]));
            } else {
                System.out.println("C");
                return false;
            }
        }

        for (Student addedStudent: students) {
            if (addedStudent.equals(student)) {
                students.remove(addedStudent);
            }
        }

        students.add(student);
        System.out.printf("学生 %s 的成绩被添加\n\n", student.getName());
        return true;
    }

    public boolean generateReports(String wholeInfoStr) {
        String[] infoStrs = trim(wholeInfoStr.split(","));

        List<String> studentNumbers = new ArrayList<>();
        for (String number: infoStrs) {
            if (numberFormat.matcher(number).matches()) {
                studentNumbers.add(number);
            } else {
                return false;
            }
        }

        Set<String> subjects = new HashSet<>();
        List<Student> selectedStudents = new ArrayList<>();
        for (String number: studentNumbers) {
            for (Student student: students) {
                if (student.getNumber().equals(number)) {
                    selectedStudents.add(student);
                    subjects.addAll(student.getGrades().keySet());
                }
            }
        }

        StringBuilder outPut = new StringBuilder("成绩单\n姓名");
        for (String subject: subjects) {
            outPut.append("|").append(subject);
        }
        outPut.append("|平均分|总分\n========================\n");

        List<Float> allPointsOfAll = new ArrayList<>();
        for (Student student: selectedStudents) {
            outPut.append(student.getName());
            Map<String, Float> grades = student.getGrades();

            float allPoints = 0;
            int subjectCount = 0;
            for (String subject: subjects) {
                outPut.append("|");

                if (grades.containsKey(subject)) {
                    float point = grades.get(subject);
                    allPoints += point;
                    subjectCount ++;
                    outPut.append(point);
                } else {
                    outPut.append("--");
                }
            }

            allPointsOfAll.add(allPoints);
            outPut.append("|").append(allPoints / subjectCount);
            outPut.append("|").append(allPoints).append("\n");

        }

        outPut.append("========================\n").append("全班总分平均数：")
                .append(getAverage(allPointsOfAll)).append("\n");
        outPut.append("全班总分中位数：").append(getMedian(allPointsOfAll)).append("\n");

        System.out.println(outPut);
        return true;
    }

    private float getAverage(List<Float> all) {
        float sum = 0;
        for (Float f: all) {
            sum += f;
        }
        return sum / all.size();
    }

    private float getMedian(List<Float> all) {
        Float[] points = all.toArray(new Float[0]);

        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points.length - i - 1; j++) {
                if (points[j] > points[j + 1]) {
                    float temp = points[j];
                    points[j] = points[j + 1];
                    points[j + 1] = temp;
                }
            }
        }

        int len = points.length;
        if (len % 2 != 0) {
            return points[len / 2];
        } else {
            return (points[len / 2] + points[(len / 2) - 1]) / 2;
        }

    }

    private String[] trim(String[] strings) {
        for (int i = 0; i < strings.length; i++) {
            strings[i] = strings[i].trim();
            System.out.println(strings[i]);
        }
        return strings;
    }
}
