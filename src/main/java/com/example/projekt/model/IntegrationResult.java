package com.example.projekt.model;

public class IntegrationResult {

    private double result;
    private String name;

    public IntegrationResult(double result, String name) {
        this.result = result;
        this.name = name;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
