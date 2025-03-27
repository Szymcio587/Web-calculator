package com.example.projekt.model.data;

import com.example.projekt.model.Point;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
public class SystemOfEquationsData implements Savable{
    private String dataType;
    private String username;

    private List<List<Double>> coefficients;

    private List<Double> constants;

    @JsonProperty
    private boolean isTest;

    public SystemOfEquationsData(List<List<Double>> coefficients, List<Double> constants) {
        this.dataType = "SystemOfEquationsData";
        this.coefficients = coefficients;
        this.constants = constants;
    }

    public SystemOfEquationsData(String username, List<List<Double>> coefficients, List<Double> constants) {
        this.dataType = "SystemOfEquationsData";
        this.username = username;
        this.coefficients = coefficients;
        this.constants = constants;
    }

    @Override
    public String toString() {
        return "SystemOfEquationsData{" +
                "coefficients=" + coefficients +
                ", constants=" + constants +
                '}';
    }
}
