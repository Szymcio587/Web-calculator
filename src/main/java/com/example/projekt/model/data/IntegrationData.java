package com.example.projekt.model.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
public class IntegrationData implements Savable{

    private String dataType;

    private String username;
    private int degree;
    private List<Double> factors;
    private int sections;

    @JsonProperty
    private boolean isTest;
    @JsonProperty("Xp")
    private double Xp;
    @JsonProperty("Xk")
    private double Xk;

    @JsonProperty
    private String customFunction;

    public IntegrationData(int degree, List<Double> factors, int sections, double xp, double xk, String customFunction) {
        this.dataType = "IntegrationData";
        this.degree = degree;
        this.factors = factors;
        this.sections = sections;
        this.Xp = xp;
        this.Xk = xk;
        this.customFunction = customFunction;
    }

    public IntegrationData(String username, int degree, List<Double> factors, int sections, double xp, double xk, String customFunction) {
        this.dataType = "IntegrationData";
        this.username = username;
        this.degree = degree;
        this.factors = factors;
        this.sections = sections;
        this.Xp = xp;
        this.Xk = xk;
        this.customFunction = customFunction;
    }

    @Override
    public String toString() {
        return "IntegrationData{" +
                "degree=" + degree +
                ", factors=" + factors +
                ", sections=" + sections +
                ", Xp=" + Xp +
                ", Xk=" + Xk +
                '}';
    }
}
