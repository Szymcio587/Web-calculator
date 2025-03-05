package com.example.projekt.model.database;

import com.example.projekt.model.data.InterpolationData;
import com.example.projekt.model.data.Savable;
import com.example.projekt.model.data.SystemOfEquationsData;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "system_of_equations_data")
public class SystemOfEquationsRecord extends SystemOfEquationsData implements Savable {

    @Id
    private String id;

    private List<Double> solutions;
    private String explanation;

    public SystemOfEquationsRecord(SystemOfEquationsData data, List<Double> solutions, String explanation) {
        super(data.getUsername(), data.getCoefficients(), data.getConstants());
        this.solutions = solutions;
        this.explanation = explanation;
    }
}
