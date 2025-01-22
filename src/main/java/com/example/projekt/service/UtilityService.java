package com.example.projekt.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class UtilityService {

    public static double Round(double number, int precision) {
        BigDecimal bd = new BigDecimal(Double.toString(number));
        bd = bd.setScale(precision, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
