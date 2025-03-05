package com.example.projekt.model.results;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntegrationResult {

    private double result;

    private String prompt;

    private String explanation;

}
