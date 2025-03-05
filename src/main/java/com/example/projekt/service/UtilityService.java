package com.example.projekt.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class UtilityService {

    public static double Round(double number, int precision) {
        BigDecimal bd = new BigDecimal(Double.toString(number));
        bd = bd.setScale(precision, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static List<Double> Round(List<Double> numbers, int precision) {
        for (int q = 0; q < numbers.size(); q++) {
            BigDecimal bd = new BigDecimal(Double.toString(numbers.get(q)));
            numbers.set(q, bd.setScale(precision, RoundingMode.HALF_UP).doubleValue());
        }
        return numbers;
    }
}
