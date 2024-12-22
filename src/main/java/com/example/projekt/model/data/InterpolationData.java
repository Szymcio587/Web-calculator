package com.example.projekt.model.data;

import com.example.projekt.model.Point;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class InterpolationData {
    private int pointsNumber;
    private double searchedValue;
    private List<Point> points;

    @Override
    public String toString() {
        return "InterpolationData{" +
                "pointsNumber=" + pointsNumber +
                ", searchedValue=" + searchedValue +
                ", points=" + points +
                '}';
    }
}
