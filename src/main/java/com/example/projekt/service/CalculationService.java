package com.example.projekt.service;

import com.example.projekt.calculations.InterpolationCalculator;
import com.example.projekt.model.InterpolationData;
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

    private InterpolationData interpolationData;

    CalculationService(InterpolationCalculator interpolationCalculator) {
        this.interpolationCalculator = interpolationCalculator;
    }

    @CachePut(key = "'cachedData'")
    public void CacheData(InterpolationData interpolationData) {
        this.interpolationData = interpolationData;
    }

    @Cacheable(key = "'cachedData'")
    public InterpolationData GetCachedData() {
        return this.interpolationData;
    }

    @Cacheable
    public double CalculateInterpolation(InterpolationData interpolationData) {
        return interpolationCalculator.Calculate(interpolationData);
    }
}
