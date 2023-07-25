package com.example.projekt.service;

import com.example.projekt.calculations.InterpolationCalculator;
import com.example.projekt.model.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "calculationCache")
public class CalculationService {

    @Autowired
    private final InterpolationCalculator interpolationCalculator;

    private Data data;

    CalculationService(InterpolationCalculator interpolationCalculator) {
        this.interpolationCalculator = interpolationCalculator;
    }

    @CachePut(key = "'cachedData'")
    public void CacheData(Data data) {
        this.data = data;
    }

    @Cacheable(key = "'cachedData'")
    public Data GetCachedData() {
        return this.data;
    }

    @Cacheable
    public double CalculateInterpolation(Data data) {
        return interpolationCalculator.Calculate(data);
    }
}
