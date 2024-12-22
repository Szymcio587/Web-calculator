
package com.example.projekt.model.results;

import java.util.List;

public class SystemOfEquationsResult {

    private List<Double> solutions;
    private String name;

    public SystemOfEquationsResult(List<Double> solutions, String name) {
        this.solutions = solutions;
        this.name = name;
    }

    public List<Double> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<Double> solutions) {
        this.solutions = solutions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SystemOfEquationsResult{solutions=" + solutions.toString() + ", name='" + name + "'}";
    }
}
