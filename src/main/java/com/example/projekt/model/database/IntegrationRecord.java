package com.example.projekt.model.database;

import com.example.projekt.model.data.IntegrationData;
import com.example.projekt.model.data.InterpolationData;
import com.example.projekt.model.data.Savable;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "integration_data")
public class IntegrationRecord extends IntegrationData implements Savable {
    @Id
    private String id;
    private double result;
    private String explanation;

    public IntegrationRecord(IntegrationData data, double result, String explanation) {
        super(data.getUsername(), data.getDegree(), data.getFactors(), data.getSections(), data.getXp(), data.getXk(), data.getCustomFunction());
        this.result = result;
        this.explanation = explanation;
    }
}
