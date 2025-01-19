package com.example.projekt.model.data;

import com.example.projekt.model.Point;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "interpolation_data")
public class InterpolationData implements Savable{

    @Id
    private String id;

    private String dataType;
    private String username;
    private int pointsNumber;
    private double searchedValue;
    private List<Point> points;

    public InterpolationData(int pointsNumber, double searchedValue, List<Point> points) {
        this.pointsNumber = pointsNumber;
        this.searchedValue = searchedValue;
        this.points = points;
    }

    public InterpolationData(String username, int pointsNumber, double searchedValue, List<Point> points) {
        this.dataType = "InterpolationData";
        this.username = username;
        this.pointsNumber = pointsNumber;
        this.searchedValue = searchedValue;
        this.points = points;
    }


}
