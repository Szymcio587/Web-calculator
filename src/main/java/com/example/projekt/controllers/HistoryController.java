package com.example.projekt.controllers;

import com.example.projekt.model.data.InterpolationData;
import com.example.projekt.service.InterpolationDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "https://localhost:4200")
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    private final InterpolationDataService interpolationDataService;

    public HistoryController(InterpolationDataService interpolationDataService) {
        this.interpolationDataService = interpolationDataService;
    }

    @PostMapping("")
    public List<InterpolationData> TreatResultsHistorySearch(@RequestBody String username) {
        return interpolationDataService.getDataByUserId(username);
    }
}
