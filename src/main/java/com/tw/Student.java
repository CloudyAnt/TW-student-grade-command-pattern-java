package com.tw;

import java.util.HashMap;
import java.util.Map;

public class Student {

    private String name, number;
    private Map<String, Float> grades;

    public Student(String name, String number) {
        this.name = name;
        this.number = number;
        grades = new HashMap<>();
    }

    public void addGrade(String subject, float point) {
        grades.put(subject, point);
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public Map<String, Float> getGrades() {
        return grades;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Student) {
            Student student = (Student)obj;
            if (student.getNumber().equals(getNumber())) {
                return true;
            }
        }
        return super.equals(obj);
    }
}
