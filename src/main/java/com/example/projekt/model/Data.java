package com.example.projekt.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Data {
    private int pointsNumber;
    private double searchedValue;
    private List<Point> points;
}
