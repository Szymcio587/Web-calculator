package com.example.projekt.model.data;

import com.example.projekt.model.Point;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class InterpolationData{

    private String dataType;
    private String username;
    private int pointsNumber;
    private double searchedValue;
    private List<Point> points;

    @JsonProperty
    private boolean isTest;

    public InterpolationData(int pointsNumber, double searchedValue, List<Point> points) {
        this.dataType = "InterpolationData";
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

    @Override
    public String toString() {
        return "InterpolationData{" +
                "pointsNumber=" + pointsNumber +
                ", searchedValue=" + searchedValue +
                ", points=" + points +
                '}';
    }
}
