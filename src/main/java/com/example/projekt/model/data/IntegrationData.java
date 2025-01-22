package com.example.projekt.model.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "integration_data")
public class IntegrationData implements Savable{
    @Id
    private String id;

    private String dataType;

    private String username;
    private int degree;
    private List<Double> factors;
    private int sections;

    private boolean isTest;
    @JsonProperty("Xp")
    private double Xp;
    @JsonProperty("Xk")
    private double Xk;

    public IntegrationData(String username, int degree, List<Double> factors, int sections, double xp, double xk) {
        this.dataType = "IntegrationData";
        this.username = username;
        this.degree = degree;
        this.factors = factors;
        this.sections = sections;
        this.Xp = xp;
        this.Xk = xk;
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
