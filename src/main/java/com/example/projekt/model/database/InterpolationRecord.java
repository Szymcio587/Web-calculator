package com.example.projekt.model.database;

import com.example.projekt.model.data.InterpolationData;
import com.example.projekt.model.data.Savable;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "interpolation_data")
public class InterpolationRecord extends InterpolationData implements Savable {
    @Id
    private String id;

    private double result;

    private double[] coefficients;

    public InterpolationRecord(InterpolationData data, double result, double[] coefficients) {
        super(data.getUsername(), data.getPointsNumber(), data.getSearchedValue(), data.getPoints());
        this.result = result;
        this.coefficients = coefficients;
    }
}
