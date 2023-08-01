package com.example.projekt.model;

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
}
