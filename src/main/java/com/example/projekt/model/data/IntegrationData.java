package com.example.projekt.model.data;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "calculation_data")
public class IntegrationData implements Savable{
    @Id
    private String id;

    private String dataType;

    private String username;
    private int degree;
    private List<Double> factors;
    private int sections;

    public IntegrationData(String username, int degree, List<Double> factors, int sections, double xp, double xk) {
        this.dataType = "IntegrationData";
        this.username = username;
        this.degree = degree;
        this.factors = factors;
        this.sections = sections;
        Xp = xp;
        Xk = xk;
    }

    private double Xp;
    private double Xk;

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
