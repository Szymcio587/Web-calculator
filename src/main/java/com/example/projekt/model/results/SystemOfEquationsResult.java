
package com.example.projekt.model.results;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemOfEquationsResult {

    private List<Double> solutions;
    private String prompt;
    private String explanation;

}
